package com.wama.frontend.controllers;

import com.wama.frontend.HttpRequest;
import com.wama.frontend.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductsController {
    @FXML
    private TilePane tilePane;

    @FXML
    private Button createButton;

    @FXML
    private String encodedImage;

    @FXML
    private Text productsCountText;

    @FXML
    private TilePane productGrid;

    public void initialize() {
        loadProducts();
        UpdaterThread.createNew(this::loadProducts, 5);
    }

    private void loadProducts() {
        Platform.runLater(() -> {
            productGrid.getChildren().clear();
            try {
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("id", "all");
                String response = HttpRequest.get("/products", parameters);
                Map<String, HashMap<String, String>> products = parseProductData(response);

                for (HashMap<String, String> product : products.values()) {
                    VBox productBox = createProductBox(product);
                    productGrid.getChildren().add(productBox);
                }

                productsCountText.setText("Products (" + products.size() + ")");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private VBox createProductBox(HashMap<String, String> product) {
        VBox productBox = new VBox(5);
        productBox.getStyleClass().add("product-box");
        productBox.setOnMouseClicked(event -> showProductPopup(product));

        Label nameLabel = new Label(product.get("name"));
        nameLabel.getStyleClass().add("product-name");

        Label priceLabel = new Label("$" + product.get("price"));
        priceLabel.getStyleClass().add("product-price");

        Label descriptionLabel = new Label(product.get("description"));
        descriptionLabel.getStyleClass().add("product-description");

        Label quantityLabel = new Label("Stock: " + product.get("current_stock"));
        quantityLabel.getStyleClass().add("product-stock");

        ImageView imageView = new ImageView();
        if (product.containsKey("encoded_image")) {
            byte[] decodedBytes = Base64.getDecoder().decode(product.get("encoded_image"));
            imageView.setImage(new Image(new ByteArrayInputStream(decodedBytes)));
        }
        imageView.setFitHeight(80);
        imageView.setFitWidth(80);

        productBox.getChildren().addAll(imageView, nameLabel, priceLabel, descriptionLabel, quantityLabel);
        return productBox;
    }

    private void showProductPopup(HashMap<String, String> product) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Product Details");
        dialog.setHeaderText(null);

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, cancelButtonType);

        VBox contentBox = createProductPopupContent(product);
        dialog.getDialogPane().setContent(contentBox);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                updateProduct(product, contentBox);
            }
            return null;
        });

        dialog.showAndWait();
    }

    private VBox createProductPopupContent(HashMap<String, String> product) {
        VBox contentBox = new VBox(10);

        TextField nameField = new TextField(product.get("name"));
        TextArea descriptionArea = new TextArea(product.get("description"));
        TextField priceField = new TextField(product.get("price"));
        TextField quantityField = new TextField(product.get("current_stock"));
        TextField reorderPointField = new TextField(product.get("reorder_point"));

        ImageView imageView = new ImageView();
        if (product.containsKey("encoded_image")) {
            byte[] decodedBytes = Base64.getDecoder().decode(product.get("encoded_image"));
            imageView.setImage(new Image(new ByteArrayInputStream(decodedBytes)));
        }
        imageView.setFitHeight(150);
        imageView.setFitWidth(150);

        Button selectImageButton = new Button("Select Image");
        selectImageButton.setOnAction(event -> handleSelectImage(imageView));

        contentBox.getChildren().addAll(
                imageView, selectImageButton, new Label("Name:"), nameField,
                new Label("Description:"), descriptionArea,
                new Label("Price:"), priceField,
                new Label("Quantity:"), quantityField,
                new Label("Reorder Point:"), reorderPointField
        );

        return contentBox;
    }

    private void updateProduct(HashMap<String, String> product, VBox contentBox) {
        String name = ((TextField) contentBox.getChildren().get(3)).getText();
        String description = ((TextArea) contentBox.getChildren().get(5)).getText();
        String price = ((TextField) contentBox.getChildren().get(7)).getText();
        String quantity = ((TextField) contentBox.getChildren().get(9)).getText();
        String reorderPoint = ((TextField) contentBox.getChildren().get(11)).getText();

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("id", product.get("id"));
        parameters.put("name", name);
        parameters.put("description", description);
        parameters.put("price", price);
        parameters.put("current_stock", quantity);
        parameters.put("reorder_point", reorderPoint);
        parameters.put("encoded_image", encodedImage);

        try {
            String response = HttpRequest.put("/products", parameters);
            System.out.println("Product updated: " + response);
            loadProducts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCreateProduct() {
        HashMap<String, String> emptyProduct = new HashMap<>();
        emptyProduct.put("name", "");
        emptyProduct.put("description", "");
        emptyProduct.put("price", "");
        emptyProduct.put("current_stock", "");

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Create Product");
        dialog.setHeaderText(null);

        ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, cancelButtonType);

        VBox contentBox = createProductPopupContent(emptyProduct);
        dialog.getDialogPane().setContent(contentBox);

        Button createButton = (Button) dialog.getDialogPane().lookupButton(createButtonType);
        createButton.setDisable(true);

        try {
            contentBox.getChildren().stream()
                    .filter(node -> node instanceof TextField || node instanceof TextArea)
                    .forEach(node -> {
                        TextInputControl textInputControl = (TextInputControl) node;
                        textInputControl.textProperty().addListener((observable, oldValue, newValue) -> {
                            boolean allFieldsFilled = contentBox.getChildren().stream()
                                    .filter(n -> n instanceof TextField || n instanceof TextArea)
                                    .allMatch(n -> !((TextInputControl) n).getText().isEmpty());
                            createButton.setDisable(!allFieldsFilled);
                        });
                    });

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == createButtonType) {
                    createNewProduct(contentBox);
                }
                return null;
            });
        } catch (Exception e) {
            System.out.println("Error creating product: " + e.getMessage());
        }

        dialog.showAndWait();
    }

    private void createNewProduct(VBox contentBox) {
        String name = ((TextField) contentBox.getChildren().get(3)).getText();
        String description = ((TextArea) contentBox.getChildren().get(5)).getText();
        String price = ((TextField) contentBox.getChildren().get(7)).getText();
        String quantity = ((TextField) contentBox.getChildren().get(9)).getText();
        String reorderPoint = ((TextField) contentBox.getChildren().get(11)).getText();

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("description", description);
        parameters.put("price", price);
        parameters.put("initial_stock", quantity);
        parameters.put("reorder_point", reorderPoint);
        parameters.put("encoded_image", encodedImage);

        try {
            String response = HttpRequest.post("/products", parameters);
            System.out.println("Product created: " + response);
            loadProducts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleSelectImage(ImageView imageView) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Product Image");
        File selectedFile = fileChooser.showOpenDialog(imageView.getScene().getWindow());
        if (selectedFile != null) {
            try {
                byte[] imageBytes = Files.readAllBytes(selectedFile.toPath());
                encodedImage = Base64.getEncoder().encodeToString(imageBytes);
                imageView.setImage(new Image(new ByteArrayInputStream(imageBytes)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Map<String, HashMap<String, String>> parseProductData(String response) {
        Map<String, HashMap<String, String>> productData = new HashMap<>();

        // regular expression pattern to match key-value pairs
        String outerPattern = "(\\d+)=(\\{[^}]*\\})";
        Pattern outerRegex = Pattern.compile(outerPattern);
        Matcher outerMatcher = outerRegex.matcher(response);

        while (outerMatcher.find()) {
            HashMap<String, String> detailsMap = new HashMap<>();
            String[] keyValuePairs = outerMatcher.group(1).split(",\\s+");
            String details = outerMatcher.group(2);

            String innerPattern = "(\\w+)=([^,}]+)";
            Pattern innerRegex = Pattern.compile(innerPattern);
            Matcher innerMatcher = innerRegex.matcher(details);
            while (innerMatcher.find()) {
                detailsMap.put(innerMatcher.group(1), innerMatcher.group(2));
            }
            productData.put(keyValuePairs[0], detailsMap);
        }
        return productData;
    }

    @FXML
    void handleDashboardEmployeeButtonAction(ActionEvent event) {
        Main.switchToSceneDashboardEmployee();
    }

    @FXML
    void handleProductsButtonAction(ActionEvent event) {
        Main.switchToSceneProducts();
    }

    @FXML
    void handleShippingButtonAction(ActionEvent event) {
        Main.switchToSceneShipping();
    }

    @FXML
    void handleFeedbackEmployeeButtonAction(ActionEvent event) {
        Main.switchToSceneFeedbackEmployee();
    }

    @FXML
    void handleSignoutButtonAction(ActionEvent event) {
        Main.switchToSceneStartUp();
    }
}
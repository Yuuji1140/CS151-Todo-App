package com.wama.frontend.controllers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wama.frontend.HttpRequest;
import com.wama.frontend.LoggedInUser;
import com.wama.frontend.Main;
import com.wama.frontend.ShoppingCart;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class CatalogController {
    private ShoppingCart shoppingCart = new ShoppingCart();
    private int current_stock;
    private LoggedInUser user = LoggedInUser.getInstance();

    @FXML
    private TilePane tilePane;

    @FXML
    private Button purchaseButton;

    @FXML
    private Button viewCartButton;

    @FXML
    private Text totalText;

    @FXML
    private ScrollPane mainContent;
    private Node originalContent;

    public void initialize() {
        originalContent = mainContent.getContent();
        loadProducts();

        // Run to update images
        // storeImages();
    }

    @SuppressWarnings("unused")
    private void storeImages() {
        String folderPath = "frontend/src/main/resources/com/wama/frontend/images/products";
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String imageName = file.getName();
                    String imagePath = folderPath + File.separator + imageName;

                    String encodedImage = encodeImages(imagePath);

                    // checks if imageName contains whitespace
                    String pattern = (imageName.matches(".*\\s.*")) ? "(\\w+\\s+\\w+)\\d\\.(\\w+)"
                            : "(\\w+)\\d\\.(\\w+)";
                    Pattern regex = Pattern.compile(pattern);
                    Matcher matcher = regex.matcher(imageName);

                    if (matcher.find()) {
                        String name = matcher.group(1);
                        HashMap<String, String> parameters = new HashMap<>();
                        parameters.put("name", name);
                        parameters.put("encoded_image", encodedImage);

                        try {
                            String response = HttpRequest.put("/products", parameters);
                            System.out.println(response);
                        } catch (Exception e) {
                            throw new IllegalArgumentException(e.getMessage());
                        }
                    }
                }
            }
        }
    }

    private String encodeImages(String imagePath) {
        try {
            byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private void loadProducts() {
        try {
            /*
             * "id" is a required parameter for the GET request to the /products endpoint.
             * add it with the all value
             */
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("id", "all");
            String response = HttpRequest.get("/products", parameters);
            Map<String, HashMap<String, String>> products = parseProductData(response);

            for (int i = 0; i < products.size(); i++) {
                HashMap<String, String> product = products.get(Integer.toString(i));
                VBox productBox = new VBox(5);
                productBox.getStyleClass().add("product-box");

                productBox.setOnMouseClicked(event -> handleProductClick(product));

                Text name = new Text(product.get("name"));
                name.getStyleClass().add("product-name");

                Text price = new Text("$" + product.get("price"));
                price.getStyleClass().add("product-price");

                Text description = new Text(product.get("description"));
                description.getStyleClass().add("product-description");

                current_stock = Integer.parseInt(product.get("current_stock"));
                Text current_stock = new Text(product.get("current_stock"));
                current_stock.getStyleClass().add("product-stock");

                ImageView imageView = new ImageView();
                byte[] decodedBytes = Base64.getDecoder().decode(product.get("encoded_image"));
                imageView.setImage(new Image(new ByteArrayInputStream(decodedBytes)));
                // imageView.setImage(new Image("/com/wama/frontend/images/icon.png"));
                imageView.setFitHeight(80);
                imageView.setFitWidth(80);

                productBox.getChildren().addAll(imageView, name, price, description, current_stock);
                tilePane.getChildren().add(productBox);
            }

        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
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

    private void handleProductClick(HashMap<String, String> product) {
        shoppingCart.addItem(product);
        System.out.println("Added to cart: " + product.get("name") + ", ID: " + product.get("id"));

        double total = shoppingCart.getTotal();
        Text totalText = (Text) mainContent.getScene().lookup("#totalText");

        if (totalText != null)
            totalText.setText("Total: $" + String.format("%.2f", total));
    }

    @FXML
    private void handlePurchase(ActionEvent event) {
        if (shoppingCart.getItems().isEmpty()) {
            showAlert("Error", "Empty shopping cart");
            return;
        }

        try {
            Map<String, String> orderParameters = new HashMap<>();
            String textDate = (new Timestamp(System.currentTimeMillis())).toString();

            orderParameters.put("customer_id", user.getUserId());
            orderParameters.put("order_date", textDate);
            orderParameters.put("status", "Processing");
            orderParameters.put("total", Double.toString(shoppingCart.getTotal()));

            String orderResponse = HttpRequest.post("/orders", orderParameters);
            String pattern = "(\\w+)=([^,}]+)";
            Pattern regex = Pattern.compile(pattern);
            Matcher matcher = regex.matcher(orderResponse);
            while (matcher.find()) {
                if (!orderParameters.containsKey(matcher.group(1))) {
                    orderParameters.put(matcher.group(1), matcher.group(2));
                }
            }

            ArrayList<Map<String, String>> orderItems = new ArrayList<>();
            for (HashMap<String, String> item : shoppingCart.getItems().keySet()) {
                HashMap<String, String> itemParameters = new HashMap<>();
                if (item.containsKey("id") && item.containsKey("price")) {
                    itemParameters.put("product_id", item.get("id"));
                    itemParameters.put("price", item.get("price"));
                }
                itemParameters.put("order_id", orderParameters.get("id"));
                itemParameters.put("quantity", Integer.toString(shoppingCart.getQuantity(item)));

                String orderItemResponse = HttpRequest.post("/orderItems", itemParameters);
                orderItems.add(itemParameters);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }

    }

    @FXML
    private void handleViewCart(ActionEvent event) {
        if (mainContent.getContent() == originalContent)
            showCart();
        else
            showCatalog();
    }

    private void updateTotalDisplay() {
        double total = shoppingCart.getTotal();
        if (totalText != null)
            totalText.setText("Total: $" + String.format("%.2f", total));
        purchaseButton.setVisible(total > 0);
    }

    private void showCart() {
        VBox cartLayout = new VBox(10);
        TilePane tilePane = new TilePane();
        tilePane.setPadding(new Insets(15, 15, 15, 15));
        tilePane.setVgap(29);
        tilePane.setHgap(29);
        tilePane.setPrefColumns(5); // You can set the number of columns based on your UI requirement

        for (HashMap<String, String> item : shoppingCart.getItems().keySet()) {
            VBox itemBox = new VBox(5);
            itemBox.getStyleClass().add("cart-item-box");

            Text itemName = new Text(item.get("name"));
            itemName.getStyleClass().add("cart-item-name");

            Text itemPrice = new Text("$" + item.get("price"));
            itemPrice.getStyleClass().add("cart-item-price");

            Spinner<Integer> quantitySpinner = new Spinner<>(0, current_stock, shoppingCart.getItems().get(item));
            quantitySpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue == 0) {
                    shoppingCart.removeItem(item);
                    tilePane.getChildren().remove(itemBox);
                    updateTotalDisplay();
                } else {
                    shoppingCart.updateQuantity(item, newValue);
                    updateTotalDisplay();
                }
            });

            quantitySpinner.getStyleClass().add("cart-item-spinner");
            quantitySpinner.setEditable(true);

            itemBox.getChildren().addAll(itemName, itemPrice, quantitySpinner);
            tilePane.getChildren().add(itemBox);
        }

        cartLayout.getChildren().add(tilePane);
        mainContent.setContent(cartLayout);
        viewCartButton.setText("Back to Catalog");
        purchaseButton.setVisible(!shoppingCart.getItems().isEmpty());
    }

    private void showCatalog() {
        mainContent.setContent(originalContent);
        viewCartButton.setText("Shopping Cart");
        purchaseButton.setVisible(false);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void handleDashboardCustomerButtonAction(ActionEvent event) {
        Main.switchToSceneDashboardCustomer();
    }

    @FXML
    void handleOrdersButtonAction(ActionEvent event) {
        Main.switchToSceneOrders();
    }

    @FXML
    void handleCatalogButtonAction(ActionEvent event) {
        Main.switchToSceneCatalog();
    }

    @FXML
    void handleFeedbackCustomerButtonAction(ActionEvent event) {
        Main.switchToSceneFeedbackCustomer();
    }

    @FXML
    void handleSignoutButtonAction(ActionEvent event) {
        Main.switchToSceneStartUp();
    }
}
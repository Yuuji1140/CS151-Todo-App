package com.wama.frontend;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CatalogController {
    private ShoppingCart shoppingCart = new ShoppingCart();

    @FXML
    private TilePane tilePane;

    public void initialize() {
        loadProducts();
        storeImages();
    }

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

                    // Checks if imageName contains whitespace
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

                Text current_stock = new Text(product.get("current_stock"));
                current_stock.getStyleClass().add("product-stock");

                ImageView imageView = new ImageView();
                byte[] decodedBytes = Base64.getDecoder().decode(product.get("encoded_image"));
                imageView.setImage(new Image(new ByteArrayInputStream(decodedBytes)));
                // imageView.setImage(new Image("/com/wama/frontend/images/icon.png"));
                imageView.setFitHeight(80);
                imageView.setFitWidth(80);

                productBox.getChildren().addAll(imageView, name, price, description,
                        current_stock);
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
                String key = innerMatcher.group(1);
                String value = innerMatcher.group(2);
                detailsMap.put(key, value);
            }
            productData.put(keyValuePairs[0], detailsMap);
        }
        return productData;
    }

    private void handleProductClick(HashMap<String, String> product) {
        shoppingCart.addItem(product);
        System.out.println("Added to cart: " + product.get("name") + ", ID: " + product.get("id"));
        updateCartView(); // updates UI to reflect items in the cart
    }

    private void updateCartView() {
        // update cart UI here
    }

    @FXML
    private void handleViewCart(ActionEvent event) {
        Stage cartStage = new Stage();
        VBox layout = new VBox(10);
        Scene scene = new Scene(layout, 300, 400);
        ListView<String> listView = new ListView<>();
        for (HashMap<String, String> item : shoppingCart.getItems()) {
            listView.getItems().add(item.get("name") + " - $" + item.get("price"));
        }
        layout.getChildren().addAll(listView);
        cartStage.setScene(scene);
        cartStage.show();
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
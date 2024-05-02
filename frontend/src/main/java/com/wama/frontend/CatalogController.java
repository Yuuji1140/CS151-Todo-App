package com.wama.frontend;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

import javax.imageio.IIOException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.wama.DatabaseManager;

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
    }

    private void loadProducts() {
        try {
            String response = HttpRequest.get("http://localhost:9876/products");

            JsonArray productsArray = new Gson().fromJson(response, JsonArray.class);
            for (int i = 0; i < productsArray.size(); i++) {
                JsonObject productJson = productsArray.get(i).getAsJsonObject();
                HashMap<String, String> product = new HashMap<>();
                product.put("id", productJson.get("id").getAsString());
                product.put("name", productJson.get("name").getAsString());
                product.put("description", productJson.get("description").getAsString());
                product.put("price", productJson.get("price").getAsString());
                product.put("current_stock", productJson.get("current_stock").getAsString());
                product.put("encoded_image", productJson.get("encoded_image").getAsString());

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

                productBox.getChildren().addAll(imageView, name, price, description, current_stock);
                tilePane.getChildren().add(productBox);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        // ArrayList<HashMap<String, String>> products =
        // DatabaseManager.selectRecords("Products", new String[]{"id", "name",
        // "description", "price", "current_stock", "encoded_image"}, null);
        // for (HashMap<String, String> product : products) {
        // VBox productBox = new VBox(5);
        // productBox.getStyleClass().add("product-box");

        // productBox.setOnMouseClicked(event -> handleProductClick(product));

        // Text name = new Text(product.get("name"));
        // name.getStyleClass().add("product-name");

        // Text price = new Text("$" + product.get("price"));
        // price.getStyleClass().add("product-price");

        // Text description = new Text(product.get("description"));
        // description.getStyleClass().add("product-description");

        // Text current_stock = new Text(product.get("current_stock"));
        // current_stock.getStyleClass().add("product-stock");

        // ImageView imageView = new ImageView();
        // byte[] decodedBytes =
        // Base64.getDecoder().decode(product.get("encoded_image"));
        // imageView.setImage(new Image(new ByteArrayInputStream(decodedBytes)));
        // // imageView.setImage(new Image("/com/wama/frontend/images/icon.png"));
        // imageView.setFitHeight(80);
        // imageView.setFitWidth(80);

        // productBox.getChildren().addAll(imageView, name, price, description,
        // current_stock);
        // tilePane.getChildren().add(productBox);
        // }
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
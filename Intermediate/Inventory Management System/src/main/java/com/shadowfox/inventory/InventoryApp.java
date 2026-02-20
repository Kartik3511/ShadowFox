package com.shadowfox.inventory;

import com.shadowfox.inventory.controller.MainController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class InventoryApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Inventory Management System - ShadowFox");
        
        MainController controller = new MainController();
        Scene scene = controller.createScene(primaryStage);
        
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(500);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

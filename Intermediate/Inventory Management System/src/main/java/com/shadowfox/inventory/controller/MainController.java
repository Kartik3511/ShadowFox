package com.shadowfox.inventory.controller;

import com.shadowfox.inventory.model.InventoryItem;
import com.shadowfox.inventory.service.InventoryManager;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainController {
    private InventoryManager inventoryManager;
    private TableView<InventoryItem> tableView;
    private TextField nameField, quantityField, priceField, categoryField, searchField;
    private Button addButton, updateButton, deleteButton, clearButton;
    
    public MainController() {
        this.inventoryManager = new InventoryManager();
    }

    public Scene createScene(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Top: Title and Search
        VBox topSection = createTopSection();
        root.setTop(topSection);

        // Center: Table View
        tableView = createTableView();
        root.setCenter(tableView);

        // Right: Form Panel
        VBox formPanel = createFormPanel();
        root.setRight(formPanel);

        Scene scene = new Scene(root, 1200, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        
        return scene;
    }

    private VBox createTopSection() {
        VBox topSection = new VBox(10);
        topSection.setPadding(new Insets(0, 0, 10, 0));

        Label titleLabel = new Label("Inventory Management System");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        Label searchLabel = new Label("Search:");
        searchField = new TextField();
        searchField.setPromptText("Enter item name or category...");
        searchField.setPrefWidth(300);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterTable(newValue));
        searchBox.getChildren().addAll(searchLabel, searchField);

        topSection.getChildren().addAll(titleLabel, searchBox);
        return topSection;
    }

    private TableView<InventoryItem> createTableView() {
        TableView<InventoryItem> table = new TableView<>();
        table.setItems(inventoryManager.getInventoryItems());

        TableColumn<InventoryItem, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);

        TableColumn<InventoryItem, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(200);

        TableColumn<InventoryItem, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityCol.setPrefWidth(100);

        TableColumn<InventoryItem, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setPrefWidth(100);

        TableColumn<InventoryItem, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryCol.setPrefWidth(150);

        table.getColumns().addAll(idCol, nameCol, quantityCol, priceCol, categoryCol);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFields(newSelection);
            }
        });

        return table;
    }

    private VBox createFormPanel() {
        VBox formPanel = new VBox(15);
        formPanel.setPadding(new Insets(10, 10, 10, 20));
        formPanel.setPrefWidth(350);
        formPanel.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #cccccc; -fx-border-width: 1;");

        Label formTitle = new Label("Item Details");
        formTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Name Field
        Label nameLabel = new Label("Name:");
        nameField = new TextField();
        nameField.setPromptText("Enter item name");

        // Quantity Field
        Label quantityLabel = new Label("Quantity:");
        quantityField = new TextField();
        quantityField.setPromptText("Enter quantity");

        // Price Field
        Label priceLabel = new Label("Price:");
        priceField = new TextField();
        priceField.setPromptText("Enter price");

        // Category Field
        Label categoryLabel = new Label("Category:");
        categoryField = new TextField();
        categoryField.setPromptText("Enter category");

        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        addButton = new Button("Add");
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        addButton.setOnAction(e -> handleAdd());

        updateButton = new Button("Update");
        updateButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        updateButton.setOnAction(e -> handleUpdate());

        deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        deleteButton.setOnAction(e -> handleDelete());

        clearButton = new Button("Clear");
        clearButton.setOnAction(e -> clearFields());

        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton, clearButton);

        formPanel.getChildren().addAll(
            formTitle,
            nameLabel, nameField,
            quantityLabel, quantityField,
            priceLabel, priceField,
            categoryLabel, categoryField,
            buttonBox
        );

        return formPanel;
    }

    private void handleAdd() {
        if (!validateFields()) {
            return;
        }

        try {
            String name = nameField.getText().trim();
            int quantity = Integer.parseInt(quantityField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());
            String category = categoryField.getText().trim();

            inventoryManager.addItem(name, quantity, price, category);
            clearFields();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Item added successfully!");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter valid numbers for quantity and price.");
        }
    }

    private void handleUpdate() {
        InventoryItem selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select an item to update.");
            return;
        }

        if (!validateFields()) {
            return;
        }

        try {
            String name = nameField.getText().trim();
            int quantity = Integer.parseInt(quantityField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());
            String category = categoryField.getText().trim();

            inventoryManager.updateItem(selectedItem, name, quantity, price, category);
            tableView.refresh();
            clearFields();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Item updated successfully!");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter valid numbers for quantity and price.");
        }
    }

    private void handleDelete() {
        InventoryItem selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select an item to delete.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Item");
        confirmAlert.setContentText("Are you sure you want to delete: " + selectedItem.getName() + "?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                inventoryManager.deleteItem(selectedItem);
                clearFields();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Item deleted successfully!");
            }
        });
    }

    private boolean validateFields() {
        if (nameField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Name cannot be empty.");
            return false;
        }
        if (quantityField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Quantity cannot be empty.");
            return false;
        }
        if (priceField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Price cannot be empty.");
            return false;
        }
        if (categoryField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Category cannot be empty.");
            return false;
        }

        try {
            int quantity = Integer.parseInt(quantityField.getText().trim());
            if (quantity < 0) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Quantity must be a positive number.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Quantity must be a valid integer.");
            return false;
        }

        try {
            double price = Double.parseDouble(priceField.getText().trim());
            if (price < 0) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Price must be a positive number.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Price must be a valid number.");
            return false;
        }

        return true;
    }

    private void populateFields(InventoryItem item) {
        nameField.setText(item.getName());
        quantityField.setText(String.valueOf(item.getQuantity()));
        priceField.setText(String.valueOf(item.getPrice()));
        categoryField.setText(item.getCategory());
    }

    private void clearFields() {
        nameField.clear();
        quantityField.clear();
        priceField.clear();
        categoryField.clear();
        tableView.getSelectionModel().clearSelection();
    }

    private void filterTable(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            tableView.setItems(inventoryManager.getInventoryItems());
        } else {
            String lowerCaseFilter = searchText.toLowerCase();
            ObservableList<InventoryItem> filteredList = inventoryManager.getInventoryItems()
                .filtered(item -> 
                    item.getName().toLowerCase().contains(lowerCaseFilter) ||
                    item.getCategory().toLowerCase().contains(lowerCaseFilter)
                );
            tableView.setItems(filteredList);
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

# Inventory Management System

A JavaFX-based Inventory Management System with full CRUD operations and data persistence.

## Features

- **Add Items**: Add new inventory items with name, quantity, price, and category
- **Update Items**: Select and update existing inventory items
- **Delete Items**: Remove items with confirmation dialog
- **Search/Filter**: Real-time search by item name or category
- **Data Persistence**: Automatic save/load using JSON file storage
- **Modern GUI**: Clean and intuitive JavaFX interface

## Requirements

- Java 11 or higher
- Maven
- JavaFX 17.0.2

## Project Structure

```
inventory-management-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── shadowfox/
│   │   │           └── inventory/
│   │   │               ├── InventoryApp.java           # Main application
│   │   │               ├── model/
│   │   │               │   └── InventoryItem.java      # Data model
│   │   │               ├── service/
│   │   │               │   └── InventoryManager.java   # Business logic
│   │   │               └── controller/
│   │   │                   └── MainController.java     # GUI controller
│   │   └── resources/
│   │       └── styles.css                              # CSS styling
├── pom.xml                                             # Maven configuration
└── inventory_data.json                                 # Data storage (auto-generated)
```

## Setup Instructions

1. **Install dependencies**:
   ```batch
   mvn clean install
   ```

2. **Run the application**:
   ```batch
   mvn javafx:run
   ```

## Usage

### Adding an Item
1. Fill in the item details in the form on the right panel
2. Click the **Add** button
3. The item will appear in the table

### Updating an Item
1. Select an item from the table (click on the row)
2. The form will populate with the item's details
3. Modify the fields as needed
4. Click the **Update** button

### Deleting an Item
1. Select an item from the table
2. Click the **Delete** button
3. Confirm the deletion in the dialog

### Searching Items
1. Type in the search box at the top
2. The table will filter items in real-time by name or category

## Data Persistence

All inventory data is automatically saved to `inventory_data.json` in the project root directory. The data is loaded automatically when the application starts.

## Technologies Used

- **JavaFX**: GUI framework
- **Gson**: JSON serialization/deserialization
- **Maven**: Build and dependency management
- **MVC Pattern**: Clean architecture separation


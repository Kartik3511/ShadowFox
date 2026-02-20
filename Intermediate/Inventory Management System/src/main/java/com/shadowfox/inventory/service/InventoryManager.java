package com.shadowfox.inventory.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shadowfox.inventory.model.InventoryItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
    private ObservableList<InventoryItem> inventoryItems;
    private int nextId;
    private static final String DATA_FILE = "inventory_data.json";
    private final Gson gson;

    public InventoryManager() {
        this.inventoryItems = FXCollections.observableArrayList();
        this.nextId = 1;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        loadData();
    }

    public ObservableList<InventoryItem> getInventoryItems() {
        return inventoryItems;
    }

    public void addItem(String name, int quantity, double price, String category) {
        InventoryItem item = new InventoryItem(nextId++, name, quantity, price, category);
        inventoryItems.add(item);
        saveData();
    }

    public void updateItem(InventoryItem item, String name, int quantity, double price, String category) {
        item.setName(name);
        item.setQuantity(quantity);
        item.setPrice(price);
        item.setCategory(category);
        saveData();
    }

    public void deleteItem(InventoryItem item) {
        inventoryItems.remove(item);
        saveData();
    }

    public void saveData() {
        try (Writer writer = new FileWriter(DATA_FILE)) {
            List<ItemData> dataList = new ArrayList<>();
            for (InventoryItem item : inventoryItems) {
                dataList.add(new ItemData(
                    item.getId(),
                    item.getName(),
                    item.getQuantity(),
                    item.getPrice(),
                    item.getCategory()
                ));
            }
            gson.toJson(dataList, writer);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    public void loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return;
        }

        try (Reader reader = new FileReader(DATA_FILE)) {
            Type listType = new TypeToken<ArrayList<ItemData>>(){}.getType();
            List<ItemData> dataList = gson.fromJson(reader, listType);
            
            if (dataList != null) {
                for (ItemData data : dataList) {
                    inventoryItems.add(new InventoryItem(
                        data.id,
                        data.name,
                        data.quantity,
                        data.price,
                        data.category
                    ));
                    if (data.id >= nextId) {
                        nextId = data.id + 1;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }

    private static class ItemData {
        int id;
        String name;
        int quantity;
        double price;
        String category;

        ItemData(int id, String name, int quantity, double price, String category) {
            this.id = id;
            this.name = name;
            this.quantity = quantity;
            this.price = price;
            this.category = category;
        }
    }
}

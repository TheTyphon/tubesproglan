package com.example.kataloglaptop;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.*;
import java.util.stream.Collectors;

public class LaptopRecommendationApp extends Application {

    private ComboBox<String> categoryComboBox;
    private ComboBox<String> priceComboBox;
    private TextArea recommendationTextArea;
    private Path currentFilePath;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Katalog Rekomendasi Laptop 2023");

        // Initialize GUI elements
        categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll("Bisnis", "Gaming");

        priceComboBox = new ComboBox<>();
        priceComboBox.getItems().addAll("Rp. 10 juta - Rp. 20 juta", "Rp. 20 juta - Rp. 30 juta", "Di atas Rp. 30 juta");

        recommendationTextArea = new TextArea();
        recommendationTextArea.setWrapText(true);

        Button showRecommendationButton = new Button("Tampilkan Rekomendasi");
        Button createButton = new Button("Tambah Rekomendasi");
        Button updateButton = new Button("Perbarui Rekomendasi");
        Button deleteButton = new Button("Hapus Rekomendasi");
        Button saveButton = new Button("Simpan");

        // Event handlers
        showRecommendationButton.setOnAction(event -> showRecommendation());
        createButton.setOnAction(event -> createRecommendation());
        updateButton.setOnAction(event -> updateRecommendation());
        deleteButton.setOnAction(event -> deleteRecommendation());
        saveButton.setOnAction(event -> saveRecommendation());

        // Layout setup
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Set background image for the entire gridPane
        String imageUrl = "file:C:/Users/faceb/Downloads/186150857_l.jpg"; // Change to your image path
        Image backgroundImage = new Image(imageUrl);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, false));
        gridPane.setBackground(new Background(background));

        // Menambahkan gambar sebagai latar belakang pada VBox (Kategori dan Range Harga)
        String imageUrlVBox = "file:C:/Users/faceb/Downloads/186150857_l.jpg"; // Ganti dengan path gambar Anda
        categoryComboBox.setStyle("-fx-background-image: url('" + imageUrlVBox + "'); -fx-background-size: cover;");
        priceComboBox.setStyle("-fx-background-image: url('" + imageUrlVBox + "'); -fx-background-size: cover;");

        // Menentukan warna latar belakang dan opasitas pada VBox (Kategori dan Range Harga)
        categoryComboBox.setStyle("-fx-background-color: linear-gradient(to bottom, #6495ED, #A9A9A9); -fx-control-inner-background: rgba(173, 216, 230, 0.25);");
        priceComboBox.setStyle("-fx-background-color: linear-gradient(to bottom, #6495ED, #A9A9A9); -fx-control-inner-background: rgba(173, 216, 230, 0.25);");




        // Membuat TextArea lebih besar secara vertikal
        recommendationTextArea.setPrefHeight(400);

        // Memberikan efek latar belakang gambar pada TextArea
        String imageUrlTextArea = "file:path/to/your/image_textarea.jpg"; // Ganti dengan path gambar Anda
        recommendationTextArea.setStyle("-fx-background-image: url('" + imageUrlTextArea + "'); -fx-background-size: cover;");

        // Menentukan warna latar belakang dan opasitas pada TextArea
        recommendationTextArea.setStyle("-fx-control-inner-background: rgba(173, 216, 230, 0.25);");


        Label labelCategory = new Label("Pilih Kategori Laptop:");
        labelCategory.setStyle("-fx-text-fill: white;");
        gridPane.add(labelCategory, 0, 0);

        gridPane.add(categoryComboBox, 1, 0);

        Label labelPrice = new Label("Pilih Range Harga:");
        labelPrice.setStyle("-fx-text-fill: white;");
        gridPane.add(labelPrice, 0, 1);

        gridPane.add(priceComboBox, 1, 1);

        gridPane.add(showRecommendationButton, 0, 2, 2, 1);

        gridPane.add(recommendationTextArea, 0, 3, 2, 1);

        HBox buttonHBox = new HBox(10);
        buttonHBox.getChildren().addAll(createButton, updateButton, deleteButton, saveButton);

        gridPane.add(buttonHBox, 0, 4, 2, 1);

        // Set preferred sizes for the components
        categoryComboBox.setPrefWidth(200);  // Adjust as needed
        buttonHBox.setPrefHeight(40); // Adjust as needed

        // Scene and stage setup
        Scene scene = new Scene(gridPane, 600, 400);
        primaryStage.setScene(scene);

        primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> {
            double width = (double) newValue;
            double textAreaWidth = width - categoryComboBox.getWidth() - 30; // 30 is an approximate sum of padding and margin
            recommendationTextArea.setPrefWidth(textAreaWidth);
        });

        // Menentukan warna latar belakang dan opasitas pada HBox (tombol)
        buttonHBox.setStyle("-fx-background-color: linear-gradient(to bottom, #6495ED, #A9A9A9);");


        primaryStage.show();
    }

    private void showRecommendation() {
        String category = categoryComboBox.getValue();
        String priceRange = priceComboBox.getValue();
        String fileName = category.toLowerCase() + "_" + priceRange.replace(" ", "") + ".txt";

        try {
            currentFilePath = Paths.get("C:\\Users\\faceb\\Documents\\LatihanProglan\\TB PROGLAN\\", fileName);
            String recommendations = readRecommendationsFromFile(currentFilePath);
            recommendationTextArea.setText(recommendations);
        } catch (IOException e) {
            e.printStackTrace();
            recommendationTextArea.setText("Gagal membaca rekomendasi.");
        }
    }

    private void createRecommendation() {
        clearFields();
    }

    private void updateRecommendation() {
        if (currentFilePath != null) {
            try {
                String updatedRecommendation = recommendationTextArea.getText();
                writeRecommendationToFile(currentFilePath, updatedRecommendation);
                showRecommendation();
            } catch (IOException e) {
                e.printStackTrace();
                recommendationTextArea.setText("Gagal memperbarui rekomendasi.");
            }
        } else {
            showAlert("Perbarui Gagal", "Anda harus memilih rekomendasi terlebih dahulu.");
        }
    }

    private void deleteRecommendation() {
        if (currentFilePath != null) {
            try {
                Files.deleteIfExists(currentFilePath);
                showAlert("Hapus Berhasil", "Rekomendasi berhasil dihapus.");
                clearFields();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Hapus Gagal", "Gagal menghapus rekomendasi.");
            }
        } else {
            showAlert("Hapus Gagal", "Anda harus memilih rekomendasi terlebih dahulu.");
        }
    }

    private void saveRecommendation() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Lokasi untuk Menyimpan Rekomendasi");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File selectedFile = fileChooser.showSaveDialog(null);

        if (selectedFile != null) {
            try {
                String newRecommendation = recommendationTextArea.getText();
                writeRecommendationToFile(selectedFile.toPath(), newRecommendation);
                showAlert("Simpan Berhasil", "Rekomendasi berhasil disimpan.");
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Simpan Gagal", "Gagal menyimpan rekomendasi.");
            }
        }
    }

    private String readRecommendationsFromFile(Path filePath) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    private void writeRecommendationToFile(Path filePath, String content) throws IOException {
        Files.createDirectories(filePath.getParent());
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write(content);
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        currentFilePath = null;
        categoryComboBox.getSelectionModel().clearSelection();
        priceComboBox.getSelectionModel().clearSelection();
        recommendationTextArea.clear();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

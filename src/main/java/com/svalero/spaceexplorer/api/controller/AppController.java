package com.svalero.spaceexplorer.api.controller;

import com.svalero.spaceexplorer.api.model.APOD;
import com.svalero.spaceexplorer.api.model.Launch;
import com.svalero.spaceexplorer.api.model.LaunchInfo.Result;
import com.svalero.spaceexplorer.api.task.APODTask;
import com.svalero.spaceexplorer.api.task.LaunchTask;
import io.reactivex.functions.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.Properties;
import java.util.ResourceBundle;

public class AppController implements Initializable {
    @FXML
    public Button btAPOD;
    @FXML
    public Button btLaunch;
    @FXML
    public ImageView APODview;
    @FXML
    public DatePicker DPapod;
    @FXML
    public TextArea textArea1;
    @FXML
    public TextArea textArea2;
    @FXML
    public ListView listView1;
    @FXML
    public ListView listViewApod;
    @FXML
    public ProgressBar progressBar;
    private String API_KEY = loadApiKey();
    public APODTask apodTask;
    public LaunchTask launchTask;
    Integer records;
    LocalDate date;


    @FXML
    public void updateAPOD(ActionEvent actionEvent) throws InterruptedException {
        progressBar.progressProperty().unbind();
        date = LocalDate.now();
        if (DPapod.getValue() != null) {
            date = DPapod.getValue();
        }

        Consumer<APOD> apodConsumer = (apod) -> {
            ObservableList<String> apodList = FXCollections.observableArrayList();
            apodList.add(apod.getTitle());
            apodList.add(String.valueOf(apod.getDate()));
            listViewApod.setFixedCellSize(40);
            listViewApod.setItems(apodList);
            Image image = new Image(apod.getUrl());
            APODview.setImage(image);
            ContextMenu contextMenu = new ContextMenu();
            MenuItem saveItem = new MenuItem("Save");
            contextMenu.getItems().add(saveItem);
        };

        this.apodTask = new APODTask(apodConsumer, API_KEY, date);
        progressBar.progressProperty().bind(apodTask.progressProperty());
        new Thread(apodTask).start();
        Thread.sleep(500);
    }

    public void getLaunch(ActionEvent actionEvent) throws InterruptedException {
        date = LocalDate.now();
        if (DPapod.getValue() != null) {
            date = DPapod.getValue();
        }
        records = 10;
        //todo ver modificaciones
        Consumer<Launch> launchConsumer = (launches) -> {
            ObservableList<String> resultsName = FXCollections.observableArrayList();
            for (Result result : launches.getResults()) {
                resultsName.add(result.getName() + result.getLaunch_service_provider().getName());
            }
            System.out.println(resultsName);
            listView1.setFixedCellSize(40);
            listView1.setItems(resultsName);
        };

        this.launchTask = new LaunchTask(launchConsumer, records, date);
        new Thread(launchTask).start();
        Thread.sleep(500);

    }

    public String loadApiKey() {
        Properties props = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (inputStream != null) {
                props.load(inputStream);
            } else {
                throw new FileNotFoundException("Archivo no encontrado");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error de lectura", e);
        }
        String apiKey = props.getProperty("API_KEY");
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new RuntimeException("No existe API_KEY");
        }
        return apiKey;
    }


    public void initialize(URL location, ResourceBundle resources) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem saveItem = new MenuItem("Save");
        contextMenu.getItems().add(saveItem);

        APODview.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(APODview, event.getScreenX(), event.getScreenY());
            }
        });

        saveItem.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();

            // Set extension filter for image files
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (*.png, *.jpg, *.jpeg, *.gif)", "*.png", "*.jpg", "*.jpeg", "*.gif");
            fileChooser.getExtensionFilters().add(extFilter);

            // Show save file dialog
            File file = fileChooser.showSaveDialog(APODview.getScene().getWindow());

            if (file != null) {
                try {
                    // Get image from the ImageView and write it to the file
                    Image image = APODview.getImage();
                    BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                    ImageIO.write(bufferedImage, "png", file);
                } catch (IOException ex) {
                    // Handle the exception
                    ex.printStackTrace();
                }
            }
        });
    }
}

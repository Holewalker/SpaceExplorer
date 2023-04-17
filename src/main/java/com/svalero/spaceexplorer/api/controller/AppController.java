package com.svalero.spaceexplorer.api.controller;

import com.svalero.spaceexplorer.api.model.APOD;
import com.svalero.spaceexplorer.api.model.Launch;
import com.svalero.spaceexplorer.api.model.LaunchInfo.Result;
import com.svalero.spaceexplorer.api.task.APODTask;
import com.svalero.spaceexplorer.api.task.LaunchTask;
import io.reactivex.functions.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Properties;

public class AppController {
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
    private String API_KEY = loadApiKey();

    public APODTask apodTask;
    public LaunchTask launchTask;
    Integer records;
    LocalDate date;

    @FXML
    public void updateAPOD(ActionEvent actionEvent) throws InterruptedException {

        date = LocalDate.now();
        if (DPapod.getValue() != null) {
            date = DPapod.getValue();
        }

        Consumer<APOD> apodConsumer = (apod) -> {
            System.out.println(apod.getUrl());
            Image image = new Image(apod.getUrl());
            APODview.setImage(image);
        };
        this.apodTask = new APODTask(apodConsumer, API_KEY, date);
        new Thread(apodTask).start();
        System.out.println("time to sleep");
        Thread.sleep(2000);
        System.out.println("wake up");
    }

    public void getLaunch(ActionEvent actionEvent) throws InterruptedException {
        date = LocalDate.now();
        if (DPapod.getValue() != null) {
            date = DPapod.getValue();
        }
        records = 10;

        Consumer<Launch> launchConsumer = (launches) -> {
            ObservableList<String> resultsName = FXCollections.observableArrayList();
            for (Result result : launches.getResults()) {
                resultsName.add(result.getName() + result.getLaunchServiceProvider().getName());
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
}

package com.svalero.spaceexplorer.api.controller;

import com.svalero.spaceexplorer.api.model.APOD;
import com.svalero.spaceexplorer.api.model.Launch;
import com.svalero.spaceexplorer.api.model.LaunchInfo.Result;
import com.svalero.spaceexplorer.api.task.APODTask;
import com.svalero.spaceexplorer.api.task.LaunchTask;
import io.reactivex.functions.Consumer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import static com.svalero.spaceexplorer.api.util.PrettyLaunchResult.prettyfyResult;

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
    @FXML
    public TextField textFieldFilter;
    private String API_KEY = loadApiKey();
    public APODTask apodTask;
    public LaunchTask launchTask;
    Integer records;
    LocalDate date;
    ObservableList<String> resultsNameList = FXCollections.observableArrayList();
    ObservableList<String> resultsNameListFallBack = FXCollections.observableArrayList();

    List<Result> resultList;


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

    public void getConcurrent(ActionEvent actionEvent) throws InterruptedException {
        getLaunch(actionEvent);
        updateAPOD(actionEvent);
    }

    public void getLaunch(ActionEvent actionEvent) throws InterruptedException {
        date = LocalDate.now();
        if (DPapod.getValue() != null) {
            date = DPapod.getValue();
        }
        records = 10;
        //todo ver modificaciones
        Consumer<Launch> launchConsumer = (launches) -> {
            resultList = launches.getResults();
            for (Result result : launches.getResults()) {
                resultsNameList.add(result.getName() + result.getLaunch_service_provider().getName());
            }
            resultsNameListFallBack.setAll(resultsNameList);
            System.out.println(resultsNameList);
            listView1.setFixedCellSize(40);
            listView1.setItems(resultsNameList);
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

        textFieldFilter.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (t1.length() != 0) {
                    Predicate<String> nameFilter = result -> result.contains(t1);
                    resultsNameList = resultsNameListFallBack;
                    resultsNameList = resultsNameList.filtered(nameFilter);
                } else {
                    resultsNameList = resultsNameListFallBack;
                }
                listView1.setItems(resultsNameList);
                listView1.refresh();
            }
        });
        listView1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Result textResult = resultList.get(listView1.getSelectionModel().getSelectedIndex());
                textArea2.setText(prettyfyResult(textResult));
            }
        });
    }
}

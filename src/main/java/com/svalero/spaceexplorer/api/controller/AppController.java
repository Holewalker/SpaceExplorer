package com.svalero.spaceexplorer.api.controller;

import com.svalero.spaceexplorer.api.model.APOD;
import com.svalero.spaceexplorer.api.model.Launch;
import com.svalero.spaceexplorer.api.model.LaunchInfo.Result;
import com.svalero.spaceexplorer.api.task.APODTask;
import com.svalero.spaceexplorer.api.task.ExportTask;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Callback;

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
    public TextArea textAreaLaunch;
    @FXML
    public ListView listViewLaunch;
    @FXML
    public ListView listViewApod;
    @FXML
    public ProgressBar progressBar;
    @FXML
    public TextField textFieldFilter;
    private String API_KEY = loadApiKey();
    public APODTask apodTask;
    public LaunchTask launchTask;
    public ExportTask exportTask;
    Integer records;
    LocalDate date;
    ObservableList<Result> resultsNameList = FXCollections.observableArrayList();
    ObservableList<Result> resultsNameListFallBack = FXCollections.observableArrayList();


    @FXML
    public void updateAPOD(ActionEvent actionEvent) throws InterruptedException {
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
        new Thread(apodTask).start();
        Thread.sleep(500);
    }

    public void getLaunch(ActionEvent actionEvent) throws InterruptedException {

        if (DPapod.getValue() != null) {
            date = DPapod.getValue();
        }
        records = 25;
        //todo ver modificaciones
        Consumer<Launch> launchConsumer = (launches) -> {
            resultsNameList.addAll(launches.getResults());
            resultsNameListFallBack.setAll(resultsNameList);
            listViewLaunch.setFixedCellSize(40);
            listViewLaunch.setItems(resultsNameList);
        };

        this.launchTask = new LaunchTask(launchConsumer, records, date);
        new Thread(launchTask).start();
        Thread.sleep(500);

    }

    public void getCsv(ActionEvent actionEvent) {
        progressBar.progressProperty().unbind();

        FileChooser fileChooser = new FileChooser();

        // Set extension filter for image files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(listViewLaunch.getScene().getWindow());

        if (file != null) {
            //  writeToCSV(resultsNameList, file);
            exportTask = new ExportTask(resultsNameList, file);
            progressBar.progressProperty().bind(exportTask.progressProperty());
            new Thread(exportTask).start();
        }
    }

    public void getZip(ActionEvent actionEvent) {

        //todo
    }

    public void getConcurrent(ActionEvent actionEvent) throws InterruptedException {
        getLaunch(actionEvent);
        updateAPOD(actionEvent);
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
                    Predicate<Result> nameFilter = result -> result.getLaunch_service_provider().getName().contains(t1);
                    resultsNameList = resultsNameListFallBack;
                    resultsNameList = resultsNameList.filtered(nameFilter);
                } else {
                    resultsNameList = resultsNameListFallBack;
                }
                listViewLaunch.setItems(resultsNameList);
                listViewLaunch.refresh();
            }
        });

        listViewLaunch.setCellFactory(new Callback<ListView<Result>, ListCell<Result>>() {
            @Override
            public ListCell<Result> call(ListView<Result> listView) {
                return new ListCell<>() {
                    protected void updateItem(Result result, boolean empty) {
                        super.updateItem(result, empty);
                        if (result != null && !empty) {

                            Text titleText = new Text(result.getName());
                            String hoursCell = result.getLaunch_service_provider().getName();
                            Text subtitleText = new Text(hoursCell);

                            VBox vBox = new VBox(titleText, subtitleText);
                            HBox hbox = new HBox();
                            hbox.getChildren().addAll(vBox);
                            hbox.setSpacing(10);
                            setGraphic(hbox);
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });

        listViewLaunch.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Result result = resultsNameList.get(listViewLaunch.getSelectionModel().getSelectedIndex());
                textAreaLaunch.setText(prettyfyResult(result));
            }
        });
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

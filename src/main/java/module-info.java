module com.svalero.spaceexplorer {
    requires javafx.controls;
    requires javafx.fxml;


    exports com.svalero.spaceexplorer.api;
    opens com.svalero.spaceexplorer.api to javafx.fxml;
}
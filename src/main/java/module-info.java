module com.svalero.spaceexplorer {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.svalero.spaceexplorer to javafx.fxml;
    exports com.svalero.spaceexplorer;
}
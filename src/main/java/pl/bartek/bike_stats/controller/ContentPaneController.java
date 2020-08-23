package pl.bartek.bike_stats.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import pl.bartek.bike_stats.model.Record;

public class ContentPaneController {

    @FXML
    private ComboBox<String> recordsFilterComboBox;

    @FXML
    private TableView<Record> contentTable;

    @FXML
    private Button viewButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    public ComboBox<String> getRecordsFilterComboBox() {
        return recordsFilterComboBox;
    }

    public TableView<Record> getContentTable() {
        return contentTable;
    }

    public Button getViewButton() {
        return viewButton;
    }

    public Button getEditButton() {
        return editButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }
}

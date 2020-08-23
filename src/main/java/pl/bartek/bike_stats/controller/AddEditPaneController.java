package pl.bartek.bike_stats.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class AddEditPaneController {

    @FXML
    private TextField startEndPointTextField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField startTimeTextField;

    @FXML
    private TextField endTimeTextField;

    @FXML
    private TextField distanceTextField;

    @FXML
    private TextField breakTimeTextField;

    @FXML
    private Button addButton;

    public TextField getStartEndPointTextField() {
        return startEndPointTextField;
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }

    public TextField getStartTimeTextField() {
        return startTimeTextField;
    }

    public TextField getEndTimeTextField() {
        return endTimeTextField;
    }

    public TextField getDistanceTextField() {
        return distanceTextField;
    }

    public TextField getBreakTimeTextField() {
        return breakTimeTextField;
    }

    public Button getAddButton() {
        return addButton;
    }

}

package pl.bartek.bike_stats.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class MessageStatsPaneController {

    @FXML
    private TextArea messageTextArea;

    @FXML
    private TextArea recordViewTextArea;

    @FXML
    private TextArea statsTextArea;

    public TextArea getMessageTextArea() {
        return messageTextArea;
    }

    public TextArea getRecordViewTextArea() {
        return recordViewTextArea;
    }

    public TextArea getStatsTextArea() {
        return statsTextArea;
    }

}

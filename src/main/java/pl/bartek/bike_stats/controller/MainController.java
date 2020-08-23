package pl.bartek.bike_stats.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import pl.bartek.bike_stats.exception.CreateRecordException;
import pl.bartek.bike_stats.model.Record;
import pl.bartek.bike_stats.model.StatsLibrary;
import pl.bartek.bike_stats.time.DateTimeParser;

import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainController {

    @FXML
    private AddEditPaneController addEditPaneController;

    @FXML
    private ContentPaneController contentPaneController;

    @FXML
    private MessageStatsPaneController messageStatsPaneController;

    private StatsLibrary statsLibrary;

    private final String DATE_COLUMN = "Data:";
    private final String START_END_POINT_COLUMN = "Opis trasy:";
    private final String DISTANCE_COLUMN = "Dystans: [km]";
    private final String CREATE_FAILURE_MESSAGE = "Wprowadzono niepoprawne dane lub nie wprowadzono wszystkich danych";

    private List<String> statsSelectionType = List.of("ostatni dzień", "ostatni tydzień", "ostatni miesiąc",
            "ostatni kwartał", "ostatni rok", "wszystkie");




    public void initialize(){

        statsLibrary = new StatsLibrary();
        configureTable();
        configureAddRecordButton();
        configureViewButton();
        configureEditButton();
        configureDeleteButton();
        configureRecordsFilterComboBox();
    }


    private void configureTable(){
        TableColumn<Record, LocalDate> dateColumn = new TableColumn<>(DATE_COLUMN);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Record, String> startEndPointColumn = new TableColumn<>(START_END_POINT_COLUMN);
        startEndPointColumn.setCellValueFactory(new PropertyValueFactory<>("startEndPoint"));

        TableColumn<Record, Double> distanceColumn = new TableColumn<>(DISTANCE_COLUMN);
        distanceColumn.setCellValueFactory(new PropertyValueFactory<>("distance"));

        contentPaneController.getContentTable().getColumns().add(dateColumn);
        contentPaneController.getContentTable().getColumns().add(startEndPointColumn);
        contentPaneController.getContentTable().getColumns().add(distanceColumn);

        contentPaneController.getContentTable().setItems(statsLibrary.getFilteredStatsRecords());
    }


    private void configureAddRecordButton(){
        addEditPaneController.getAddButton().setOnAction(actionEvent -> addRecord());
    }


    private void configureViewButton() {
        contentPaneController.getViewButton().setOnAction(actionEvent -> viewRecord());
    }


    private void configureEditButton(){
        contentPaneController.getEditButton().setOnAction(actionEvent -> editRecord());
    }


    private void configureDeleteButton(){
        contentPaneController.getDeleteButton().setOnAction(actionEvent -> deleteRecord());
    }


    private void configureRecordsFilterComboBox(){
        contentPaneController.getRecordsFilterComboBox().setItems(FXCollections.observableArrayList(statsSelectionType));
        contentPaneController.getRecordsFilterComboBox().setValue("wszystkie");
        selectFilteredRecords();
        contentPaneController.getRecordsFilterComboBox().setOnAction(actionEvent -> selectFilteredRecords());
    }


    private void addRecord(){
        try{
            Record record = createRecordFromTextFields();
            statsLibrary.createRecord(record);
            clearRecordTextFields();
            selectFilteredRecords();
            messageStatsPaneController.getMessageTextArea().setText("Pomyślnie dodano wpis");
        } catch (CreateRecordException e){
            messageStatsPaneController.getMessageTextArea().setText(e.getMessage());
        } catch (IllegalArgumentException | DateTimeException e) {
            messageStatsPaneController.getMessageTextArea().setText(CREATE_FAILURE_MESSAGE);
        } catch (SQLException e){
            messageStatsPaneController.getMessageTextArea().setText("Wystąpił błąd przy zapisie danych do bazy");
        }
    }


    private void viewRecord(){
        int index = contentPaneController.getContentTable().getSelectionModel().getSelectedIndex();
        if(index != -1) {
            Record record = statsLibrary.getFilteredStatsRecords().get(index);
            messageStatsPaneController.getRecordViewTextArea().setText(record.toString());
            clearRecordTextFields();
        }
    }


    private void editRecord(){
        int index = contentPaneController.getContentTable().getSelectionModel().getSelectedIndex();
        if(index != -1) {
            Record record = statsLibrary.getFilteredStatsRecords().get(index);
            setTextFields(record);
            addEditPaneController.getAddButton().setText("Zatwierdź");
            addEditPaneController.getAddButton().setOnAction(actionEvent1 -> {
                try{
                    int id = statsLibrary.getFilteredStatsRecords().get(index).getId();
                    statsLibrary.updateRecord(id, createRecordFromTextFields());
                    clearRecordTextFields();
                    selectFilteredRecords();
                    addEditPaneController.getAddButton().setText("Dodaj");
                    messageStatsPaneController.getMessageTextArea().setText("Pomyślnie zaktualizowano wpis");
                } catch (CreateRecordException e){
                    messageStatsPaneController.getMessageTextArea().setText(e.getMessage());
                } catch (IllegalArgumentException e){
                    messageStatsPaneController.getMessageTextArea().setText(CREATE_FAILURE_MESSAGE);
                } catch (SQLException e) {
                    messageStatsPaneController.getMessageTextArea().setText("Wystąpił błąd przy zapisie do bazy danych");
                    e.printStackTrace();
                    e.getMessage();
                }
            });

        }
    }


    private void deleteRecord(){
        int index = contentPaneController.getContentTable().getSelectionModel().getSelectedIndex();
        if(index != -1) {
            try {
                int id = statsLibrary.getFilteredStatsRecords().get(index).getId();
                statsLibrary.deleteRecord(id);
                selectFilteredRecords();
                messageStatsPaneController.getMessageTextArea().setText("Pomyślnie usunięto wpis");
            } catch (SQLException e) {
                messageStatsPaneController.getMessageTextArea().setText("Wystąpił błąd podczas usuwania z bazy danych");
                e.printStackTrace();
                e.getMessage();
            }
        }
    }


    private void selectFilteredRecords(){
        {
            if(!contentPaneController.getRecordsFilterComboBox().getSelectionModel().isEmpty()){
                String statsType = contentPaneController.getRecordsFilterComboBox().getValue();
                statsLibrary.getFilteredStatsRecords().clear();
                try {
                    statsLibrary.getFilteredStatsRecords().addAll(statsLibrary.readRecords(statsType));
                    calculateAndPrintStats(statsLibrary.getFilteredStatsRecords());
                } catch (SQLException | CreateRecordException e){
                    messageStatsPaneController.getMessageTextArea().setText("Wystąpił problem z odczytem danych z bazy");
                }
            }
        }
    }


    private Record createRecordFromTextFields() throws CreateRecordException, NumberFormatException, DateTimeException {
        String startEndPoint = addEditPaneController.getStartEndPointTextField().getText();
        LocalDate date = addEditPaneController.getDatePicker().getValue();
        LocalTime startTime = DateTimeParser.createTimeFromString(addEditPaneController.getStartTimeTextField().getText());
        LocalTime endTime = DateTimeParser.createTimeFromString(addEditPaneController.getEndTimeTextField().getText());
        double distance = Double.parseDouble(addEditPaneController.getDistanceTextField().getText());
        int breakTime = Integer.parseInt(addEditPaneController.getBreakTimeTextField().getText());
        return new Record(startEndPoint, date, startTime, endTime, distance, breakTime);
    }


    private void setTextFields(Record record) {
        addEditPaneController.getStartEndPointTextField().setText(record.getStartEndPoint());
        addEditPaneController.getDatePicker().setValue(record.getDate());
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        addEditPaneController.getStartTimeTextField().setText(record.getStartTime().format(timeFormatter));
        addEditPaneController.getEndTimeTextField().setText(record.getEndTime().format(timeFormatter));
        addEditPaneController.getDistanceTextField().setText(String.valueOf(record.getDistance()));
        addEditPaneController.getBreakTimeTextField().setText(String.valueOf(record.getBreakTime()));
    }


    private void clearRecordTextFields(){
        addEditPaneController.getStartEndPointTextField().clear();
        addEditPaneController.getDatePicker().setValue(null);
        addEditPaneController.getStartTimeTextField().clear();
        addEditPaneController.getEndTimeTextField().clear();
        addEditPaneController.getDistanceTextField().clear();
        addEditPaneController.getBreakTimeTextField().clear();
    }


    private void calculateAndPrintStats(List<Record> records) {
        double totalDistance = 0;
        double totalTime = 0;
        double totalBreaks = 0;

        messageStatsPaneController.getStatsTextArea().clear();
        if(records.size() == 0 ) {
            messageStatsPaneController.getStatsTextArea().setText("Brak statystyk dla wskazanego okresu");
        } else {
            for(Record record:records) {
                totalDistance += record.getDistance();
                Duration duration = Duration.between(record.getStartTime(), record.getEndTime());
                totalTime += duration.toHours();
                totalBreaks += record.getBreakTime();
            }
            StringBuilder message = new StringBuilder();
            message.append("Całkowity dystans: ");
            message.append(totalDistance);
            message.append(" km");
            message.append("\nCałkowity czas: ");
            message.append(totalTime);
            message.append(" h");
            if(totalTime > 0){
                message.append("\nŚrednia prędkość: ");
                message.append(String.format("%.2f", totalDistance/totalTime));
                message.append(" km/h");
            }
            message.append("\nCzas przerw: ");
            message.append(totalBreaks);
            message.append(" min");
            messageStatsPaneController.getStatsTextArea().setText(message.toString());
        }

    }


}

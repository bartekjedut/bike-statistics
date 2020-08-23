package pl.bartek.bike_stats.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.bartek.bike_stats.database.RecordDAO;
import pl.bartek.bike_stats.exception.CreateRecordException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StatsLibrary{

    private static int recordsCount;
    private static RecordDAO recordDAO = new RecordDAO();
    private ObservableList<Record> filteredStatsRecords = FXCollections.observableList(new ArrayList<>());


    public StatsLibrary() {
        try {
            recordDAO = new RecordDAO();
            recordDAO.openConnection();
            recordsCount = recordDAO.getMaxId();
        } catch (SQLException e){
            System.out.println("Problem z otwarciem połączenia z bazą danych");
        }
    }


    public void createRecord(Record record) throws SQLException{
        recordsCount++;
        int id = recordsCount;
        record.setId(id);
        recordDAO.insertRecord(record);
    }


    public List<Record> readRecords(String filter) throws SQLException, CreateRecordException {
        return recordDAO.selectRecords(filter);
    }


    public void updateRecord(int id, Record recordUpdate) throws SQLException{
            recordDAO.updateRecord(id, recordUpdate);
    }


    public void deleteRecord(int id) throws SQLException{
        recordDAO.deleteRecord(id);
    }


    public static RecordDAO getRecordDAO() {
        return recordDAO;
    }


    public ObservableList<Record> getFilteredStatsRecords() {
        return filteredStatsRecords;
    }
}

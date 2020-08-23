package pl.bartek.bike_stats.database;

import pl.bartek.bike_stats.exception.CreateRecordException;
import pl.bartek.bike_stats.model.Record;
import pl.bartek.bike_stats.time.DateTimeParser;

import java.sql.*;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class RecordDAO {

    public static final String DATABASE_DRIVER = "org.sqlite.JDBC";
    public static final String DATABASE_URL = "jdbc:sqlite:bike_stats.db";

    private Connection conn;
    private Statement stat;
    private RecordParser recordParser;


    public RecordDAO() {
        recordParser = new RecordParser();
    }


    public void openConnection() {
        try {
            Class.forName(RecordDAO.DATABASE_DRIVER);
            conn = DriverManager.getConnection(DATABASE_URL);
            stat = conn.createStatement();
            createTable();
        } catch (ClassNotFoundException e) {
            System.err.println("Brak sterownika JDBC");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Problem z otwarciem połączenia");
            e.printStackTrace();
        }

    }


    public void createTable() throws SQLException{
        String createRecords = recordParser.createTableQuery();
        stat.execute(createRecords);

    }


    public void insertRecord(Record record) throws SQLException{
        String insertRecord = recordParser.createInsertQuery(record);
        stat.execute(insertRecord);
    }


    public List<Record> selectRecords(String filter) throws SQLException, CreateRecordException  {
        List<Record> recordsResults = new ArrayList<>();
        String selectRecords;
        if(filter.equals("wszystkie")){
            selectRecords = recordParser.createReadAllQuery();
        } else {
            selectRecords = recordParser.createReadQuery(filter);
        }
        ResultSet resultSet = stat.executeQuery(selectRecords);
        while(resultSet.next()) {
            Record record = createRecordFromQuery(resultSet);
            recordsResults.add(record);
        }
        return recordsResults;
    }


    public void updateRecord(int id, Record record) throws SQLException{
        String updateRecord = recordParser.createUpdateQuery(id, record);
        stat.executeUpdate(updateRecord);

    }


    public void deleteRecord(int id) throws SQLException{
        String deleteRecord = recordParser.createDeleteQuery(id);
        stat.execute(deleteRecord);

    }


    public int getMaxId() throws SQLException{
        String maxIdQuery = recordParser.selectMaxIdQuery();
        int id = 0;

        ResultSet resultSet = stat.executeQuery(maxIdQuery);
        while(resultSet.next()){
            if(resultSet.getInt("id") > id) {
                id = resultSet.getInt("id");
            }
        }

        return id;
    }


    public void closeConnection() throws SQLException{
            conn.close();
    }


    private Record createRecordFromQuery(ResultSet resultSet) throws SQLException, CreateRecordException{
        int id = resultSet.getInt("id");
        String startEndPoint = resultSet.getString("start_end_Point");
        LocalDate date = DateTimeParser.createDateFromString(resultSet.getString("date"));
        LocalTime startTime = DateTimeParser.createTimeFromString(resultSet.getString("start_time"));
        LocalTime endTime = DateTimeParser.createTimeFromString(resultSet.getString("end_time"));
        double distance = resultSet.getDouble("distance");
        int breakTime = resultSet.getInt("break_time");
        Record record = new Record(startEndPoint, date, startTime, endTime, distance, breakTime);
        record.setId(id);
        return record;
    }

}

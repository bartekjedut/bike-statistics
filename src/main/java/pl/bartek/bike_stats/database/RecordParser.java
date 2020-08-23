package pl.bartek.bike_stats.database;

import pl.bartek.bike_stats.model.Record;


import java.util.Map;


public class RecordParser {

    private Map<String, Integer> filterType = Map.of("ostatni dzień", 1, "ostatni tydzień", 7, "ostatni miesiąc", 30,
            "ostatni kwartał", 90, "ostatni rok", 365);

    public String createTableQuery(){
        String query = "CREATE TABLE IF NOT EXISTS records (id INTEGER, start_end_point TEXT, date VARCHAR(10), start_time VARCHAR(5), end_time VARCHAR(5), " +
                "distance REAL, break_time INTEGER)";
        return query;
    }


    public String createInsertQuery(Record record){
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO records VALUES (");
        sb.append(record.getId());
        sb.append(", '");
        sb.append(record.getStartEndPoint());
        sb.append("', '");
        sb.append(record.getDate());
        sb.append("', '");
        sb.append(record.getStartTime());
        sb.append("', '");
        sb.append(record.getEndTime());
        sb.append("', ");
        sb.append(record.getDistance());
        sb.append(", ");
        sb.append(record.getBreakTime());
        sb.append(" );");
        return sb.toString();

    }


    public String createReadQuery(String filter){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM records WHERE (JULIANDAY('now') - JULIANDAY(date)) <= ");
        sb.append(filterType.get(filter));
        sb.append("; ");
        return sb.toString();
    }


    public String createReadAllQuery(){
        return "SELECT * FROM records;";
    }


    public String createUpdateQuery(int id, Record record){
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE records SET start_end_point = '");
        sb.append(record.getStartEndPoint());
        sb.append("', date = '");
        sb.append(record.getDate());
        sb.append("', start_time = '");
        sb.append(record.getStartTime());
        sb.append("', end_time = '");
        sb.append(record.getEndTime());
        sb.append("', distance = ");
        sb.append(record.getDistance());
        sb.append(", break_time = ");
        sb.append(record.getBreakTime());
        sb.append(" WHERE id = ");
        sb.append(id);
        sb.append(" ;");
        return sb.toString();

    }


    public String createDeleteQuery(int id){
        String query = "DELETE FROM records WHERE id = " + id + " ;";
        return query;
    }


    public String selectMaxIdQuery() {
        return "SELECT id FROM records;";
    }

}

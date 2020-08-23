package pl.bartek.bike_stats.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pl.bartek.bike_stats.model.StatsLibrary;

import java.sql.SQLException;

public class Main extends Application {


    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Pane pane = FXMLLoader.load(getClass().getResource("/fxml/mainPane.fxml"));
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setTitle("Bike stats v1.0");
        stage.show();
        stage.setOnCloseRequest(windowEvent -> {
            try{
                StatsLibrary.getRecordDAO().closeConnection();
                System.out.println("Zamknięto okno i zapisano dane");
            } catch (SQLException e) {
                System.out.println("Nie udało się zamknąć połączenia z bazą danych");
            }
            Platform.exit();
        });
    }
}
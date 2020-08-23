module bike.stats {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports pl.bartek.bike_stats.app to javafx.graphics;
    opens pl.bartek.bike_stats.controller to javafx.fxml;
    opens pl.bartek.bike_stats.model to javafx.base;
}


module org.robocop {
    requires javafx.controls;
    requires javafx.fxml;
    exports org.robocop;
    exports org.robocop.modelo;

    opens org.robocop.grafica to javafx.fxml;

    exports org.robocop.grafica;
    exports org.robocop.modelo.Celda;
    exports org.robocop.modelo.Robot;
}

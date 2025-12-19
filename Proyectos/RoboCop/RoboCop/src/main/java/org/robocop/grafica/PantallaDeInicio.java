package org.robocop.grafica;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class PantallaDeInicio {
    @FXML
    private Button btnJugar;

    @FXML
    private void onJugarClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/robocop/grafica/selectorDeTablero.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) btnJugar.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Seleccionar tamaño del tablero");
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


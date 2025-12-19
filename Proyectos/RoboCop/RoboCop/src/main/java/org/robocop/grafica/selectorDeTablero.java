package org.robocop.grafica;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class selectorDeTablero {

    @FXML private Button btn20x20;
    @FXML private Button btn30x30;
    @FXML private Button btn40x40;
    @FXML private Button btn50x50;
    final int TAM_CELDA = 15;

    @FXML
    private void onTablero20x20Clicked() {
        crearTablero(20, (Stage) btn20x20.getScene().getWindow());
    }

    @FXML
    private void onTablero30x30Clicked() {
        crearTablero(30, (Stage) btn30x30.getScene().getWindow());
    }

    @FXML
    private void onTablero40x40Clicked() {
        crearTablero(40, (Stage) btn40x40.getScene().getWindow());
    }

    @FXML
    private void onTablero50x50Clicked() {
        crearTablero(50, (Stage) btn50x50.getScene().getWindow());
    }

    private void crearTablero(int tamanio, Stage actual) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/robocop/grafica/pantallaDeTablero.fxml"));
            Parent root = loader.load();

            PantallaDeTablero controlador = loader.getController();
            controlador.iniciarJuego(tamanio, TAM_CELDA);

            Stage stage = new Stage();
            stage.setTitle("Tablero " + tamanio + "x" + tamanio);

            stage.setWidth(tamanio * TAM_CELDA + 50);
            stage.setHeight(tamanio * TAM_CELDA + 100);
            stage.setResizable(false);

            stage.setMaxWidth(800);
            stage.setMaxHeight(650);

            stage.setScene(new Scene(root));
            stage.show();

            actual.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

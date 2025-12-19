package org.robocop.grafica;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.robocop.modelo.*;
import org.robocop.modelo.Celda.Celda;
import org.robocop.modelo.Celda.EstadoDeCelda;
import org.robocop.modelo.Robot.Robot;
import org.robocop.modelo.Robot.TipoRobot;

import java.io.IOException;
import java.util.ArrayList;

public class PantallaDeTablero {
    @FXML private GridPane tablero;
    @FXML private HBox hboxBotones;
    @FXML private Label lblNivelYScore;

    private Juego juego;
    private int TAM_CELDA;
    private GestorDeImagenes gestorDeImagenes;
    private boolean esperandoClicDeTeletransporte = false;

    /**
     *  Inicializar el juego con el tablero y los botones, recibiendo tambien los movimientos de teclas del jugador y
     *  actualizando el tablero en base al movimiento
     *
     * @param tamanio: tamanio del tablero
     * @param TAM_CELDA: tamanio de la celda
     */
    public void iniciarJuego(int tamanio, int TAM_CELDA) {
        this.juego = new Juego(tamanio);

        gestorDeImagenes = new GestorDeImagenes("/org/robocop/grafica/robots_con_diamante.png");
        inicializarTablero(tamanio, TAM_CELDA);
        inicializarBotones();
        actualizarVistaDelTablero();

            Platform.runLater(() -> {
                tablero.getScene().getWindow().sizeToScene();
                tablero.requestFocus();
                configurarEntradaDeTeclado(tamanio, TAM_CELDA);
            });
    }

    /**
     * Configuracion de eventos del teclado
     * @param tamanio: tamanio del tablero
     * @param TAM_CELDA: tamanio de la celda
     */
    private void configurarEntradaDeTeclado(int tamanio, int TAM_CELDA) {
        tablero.setOnKeyPressed(event -> {
            if (esperandoClicDeTeletransporte) return;

            switch (event.getCode()) {
                case UP -> juego.moverJugador(juego.getJugador(), -1, 0);
                case DOWN -> juego.moverJugador(juego.getJugador(), 1, 0);
                case LEFT -> juego.moverJugador(juego.getJugador(), 0, -1);
                case RIGHT -> juego.moverJugador(juego.getJugador(), 0, 1);
            }

            juego.moverRobots();
            verificarFinDelJuego();

            if (juego.nivelGanado()) {
                juego.avanzarNivel();
                inicializarTablero(tamanio, TAM_CELDA);
                inicializarBotones();
            }
            actualizarVistaDelTablero();
            actualizarMarcador();
        });
    }

    /**
     *  Verifica si el juego termino y en caso verdadero crea la ventana de fin de juego
     */
    private void verificarFinDelJuego() {
        if (!juego.getJugador().estaVivo()) {
            crearPantallaDePerdida((Stage) tablero.getScene().getWindow());
        }
    }

    /**
     * Inicializa los botones del juego con sus estilos y funciones
     */
    private void inicializarBotones() {
        hboxBotones.getChildren().clear();
        hboxBotones.setPrefHeight(60);
        hboxBotones.setMaxHeight(60);
        hboxBotones.setMinHeight(60);

        Button botonAleatorio = crearBotonTeletransporteAleatorio();
        Button botonSeguro = crearBotonTeletransporteSeguro();
        Button botonEsperar = crearBotonEsperarRobots();

        HBox.setHgrow(botonAleatorio, Priority.ALWAYS);
        HBox.setHgrow(botonSeguro, Priority.ALWAYS);
        HBox.setHgrow(botonEsperar, Priority.ALWAYS);

        hboxBotones.getChildren().addAll(botonAleatorio, botonSeguro, botonEsperar);
    }

    /**
     * Se crea el boton de teletransportes aleatorios
     * @return: devuelve el boton creado
     */
    private Button crearBotonTeletransporteAleatorio() {
        Button boton = new Button("Teletransporte Aleatorio");
        configurarEstiloBoton(boton);
        boton.setOnAction(e -> {
            juego.usarTeletrasnportesAleatorios();
            juego.moverRobots();
            actualizarVistaDelTablero();
            tablero.requestFocus();
        });
        return boton;
    }

    /**
     * Se crea el boton de teletransportes seguros
     * @return: devuelve el boton
     */
    private Button crearBotonTeletransporteSeguro() {
        int cantidad = juego.getJugador().getTeletransportesSeguros();
        Button boton = new Button("Teletransporte Seguro\n(Cantidad: " + cantidad + ")");
        configurarEstiloBoton(boton);
        actualizarBotonTeletransporteSeguro(boton);

        if (juego.getJugador().puedeTeletransportarse()) {
            boton.setOnAction(e -> {
                esperandoClicDeTeletransporte = true;
                teletransportarJugador(boton);
                tablero.requestFocus();
            });
        }

        return boton;
    }

    /**
     * Se crea el boton de espera de robots
     * @return: devuelve el boton
     */
    private Button crearBotonEsperarRobots() {
        Button boton = new Button("Esperar Robots");
        configurarEstiloBoton(boton);
        boton.setOnAction(e -> {
            juego.moverRobots();
            actualizarVistaDelTablero();
            tablero.requestFocus();
        });
        return boton;
    }

    /**
     * Configura el estilo que tendra el boton
     * @param boton: boton a aplicar el estilo
     */
    private void configurarEstiloBoton(Button boton) {
        boton.setPrefHeight(60);
        boton.setPrefWidth(80);
        boton.setMaxWidth(Double.MAX_VALUE);
        boton.setWrapText(true);
        boton.setTextAlignment(TextAlignment.CENTER);
    }

    /**
     * Actualiza la cantidad de teletransportes seguros
     * @param boton: boton que llama a teletransporte seguro
     */
    private void actualizarBotonTeletransporteSeguro(Button boton) {
        int cantidadTeletransportesSeguros = juego.getJugador().getTeletransportesSeguros();

        boton.setText("Teletransporte Seguro\n(Cantidad: " + cantidadTeletransportesSeguros + ")");

        if (cantidadTeletransportesSeguros == 0) {
            boton.setDisable(true);
        } else {
            boton.setDisable(false);
        }
    }

    /**
     * Lee la celda tecleada por el mouse, mueve el jugador a esa posicion y actualiza el juego
     * @param boton: boton de teletransporte seguro
     */
    private void teletransportarJugador(Button boton) {
        tablero.setOnMouseClicked(event -> {
            if (esperandoClicDeTeletransporte) {
                int col = (int) (event.getX() / TAM_CELDA);
                int fila = (int) (event.getY() / TAM_CELDA);

                if (fila >= 0 && fila < juego.getTablero().getFilas()
                        && col >= 0 && col < juego.getTablero().getColumnas()) {
                    Posicion pos = new Posicion(fila, col);
                    juego.usarTeletransportesSeguros(pos);
                    juego.moverRobots();
                    actualizarVistaDelTablero();
                    actualizarBotonTeletransporteSeguro(boton);
                    tablero.requestFocus();
                }
            }
            esperandoClicDeTeletransporte = false;
        });
    }

    /**
     * Inicilizar tablero con sus celdas
     * @param tamanio: tamanio de tablero
     * @param TAM_CELDA: tamanio de celda
     */
    private void inicializarTablero(int tamanio, int TAM_CELDA) {
        tablero.getChildren().clear();
        this.TAM_CELDA = TAM_CELDA;

        double anchoTablero = tamanio * TAM_CELDA;

        tablero.setPrefSize(anchoTablero, tamanio * TAM_CELDA);
        tablero.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        tablero.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        for (int row = 0; row < tamanio; row++) {
            for (int col = 0; col < tamanio; col++) {
                StackPane celda = crearCeldaVisual(row, col);
                tablero.add(celda, col, row);
            }
        }
        tablero.setFocusTraversable(true);
        tablero.setOnMouseClicked(e -> tablero.requestFocus());
    }

    /**
     * Crear la celda con el estilo definido
     * @param row: fila de celda
     * @param col: columna de celda
     * @return: celda con estilos
     */
    private StackPane crearCeldaVisual(int row, int col) {
        StackPane celda = new StackPane();
        celda.setPrefSize(TAM_CELDA, TAM_CELDA);

        Region fondo = new Region();
        fondo.setPrefSize(TAM_CELDA, TAM_CELDA);
        if ((row + col) % 2 == 0) {
            fondo.setStyle("-fx-background-color: #7faad9;");
        } else {
            fondo.setStyle("-fx-background-color: #4682B4;");
        }

        Pane contenido = new Pane();
        celda.getChildren().addAll(fondo, contenido);

        return celda;
    }

    /**
     * Actualiza el tablero y las celdas del mismo para mostrar las imagenes del juego
     */
    private void actualizarVistaDelTablero() {
        tablero.getChildren().clear();
        int filas = juego.getTablero().getFilas();
        int columnas = juego.getTablero().getColumnas();

        for (int row = 0; row < filas; row++) {
            for (int col = 0; col < columnas; col++) {
                StackPane celda = crearCeldaVisual(row, col);
                agregarContenidoACelda(celda, row, col);
                tablero.add(celda, col, row);
            }
        }

        actualizarMarcador();
        tablero.setFocusTraversable(true);
        tablero.setOnMouseClicked(e -> tablero.requestFocus());
    }

    /**
     * Agrega el contenido visual a la celda
     * @param celda StackPane ya inicializado
     * @param row Fila de la celda
     * @param col Columna de la celda
     */
    private void agregarContenidoACelda(StackPane celda, int row, int col) {
        Celda celdaModelo = juego.getTablero().getCelda(new Posicion(row, col));
        Image imagen = determinarImagenPara(celdaModelo, row, col);

        ImageView imageView = new ImageView(imagen);
        imageView.setFitWidth(TAM_CELDA);
        imageView.setFitHeight(TAM_CELDA);
        imageView.setPreserveRatio(true);

        Pane contenido = new Pane();
        contenido.setPrefSize(TAM_CELDA, TAM_CELDA);
        contenido.getChildren().add(imageView);

        celda.getChildren().add(contenido);
    }

    /**
     * Actualizar marcador del juego
     */
    private void actualizarMarcador(){
        lblNivelYScore.setText("Level: " + juego.getNivel() + "   Score: " + juego.getRecursosTotales());
    }

    /**
     * Determinar imagen segun lo que contenga la celda
     * @param celda: celda a introducir imagen
     * @param row: fila de celda
     * @param col: columna de celda
     * @return: imagen de celda o null en caso de no tener imagen
     */
    private Image determinarImagenPara(Celda celda, int row, int col) {
        if (juego.getFilaDeJugador() == row &&
                juego.getColumnaDeJugador() == col) {
            return gestorDeImagenes.obtenerImagenPorIndice(0);
        }

        if (celda.getEstado() == EstadoDeCelda.INCENDIADA) {
            return gestorDeImagenes.obtenerImagenPorIndice(13);
        }

        if (celda.getEstado() == EstadoDeCelda.CON_RECURSO) {
            return gestorDeImagenes.obtenerImagenPorIndice(14);
        }

        ArrayList<Robot> robots = juego.getRobots();

        for (int i = 0; i < robots.size(); i++) {
            if (robots.get(i).getTipoRobot() == TipoRobot.LENTO
                    && juego.getFilaDeRobot(robots.get(i)) == row
                    && juego.getColumnaDeRobot(robots.get(i)) == col) {

                return gestorDeImagenes.obtenerImagenPorIndice(5);

            } else if (robots.get(i).getTipoRobot() == TipoRobot.RAPIDO
                    && juego.getFilaDeRobot(robots.get(i)) == row
                    && juego.getColumnaDeRobot(robots.get(i)) == col) {

                return gestorDeImagenes.obtenerImagenPorIndice(9);
            }
        }
        return null;
    }

    /**
     * Crea la pantalla de perdida de juego
     * @param actual: pantalla actual
     */
    private void crearPantallaDePerdida(Stage actual) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/robocop/grafica/pantallaDePerdida.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("¡Perdiste!");

            stage.setMaxWidth(800);
            stage.setMaxHeight(650);
            stage.setResizable(false);

            stage.setScene(new Scene(root));
            stage.show();

            actual.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void reiniciarJuego(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/robocop/grafica/selectorDeTablero.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

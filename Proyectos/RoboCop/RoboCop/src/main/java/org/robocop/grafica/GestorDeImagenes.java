package org.robocop.grafica;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class GestorDeImagenes {

    private Image spriteSheet;

    public GestorDeImagenes(String rutaSpriteSheet) {
        this.spriteSheet = new Image(getClass().getResource(rutaSpriteSheet).toExternalForm());
    }

    public Image obtenerImagenPorIndice(int indice) {
        int tamCelda = 32;
        return obtenerImagen(indice, 0, tamCelda);
    }

    private Image obtenerImagen(int columna, int fila, int tamCelda) {
        double x = columna * tamCelda;
        double y = fila * tamCelda;
        double ancho = tamCelda;
        double alto = tamCelda;

        WritableImage imagenRecortada = new WritableImage(spriteSheet.getPixelReader(), (int) x, (int) y, (int) ancho, (int) alto);
        return imagenRecortada;
    }
}

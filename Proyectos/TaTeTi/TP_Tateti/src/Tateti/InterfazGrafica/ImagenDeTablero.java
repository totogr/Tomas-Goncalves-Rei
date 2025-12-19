package Tateti.InterfazGrafica;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class ImagenDeTablero {
	private BufferedImage imagenTablero = null;
	private ImagenDeCasillero imagenDeCasillero = null;
	private Integer alto = null;
	private Integer ancho = null;
	private Integer alturaTablero = null;
	private Integer anchuraTablero = null;
	/**
	 * crea un tablero con la imagen del casillero, de las dimensiones de ancho y alto pasado por parametro 
	 * @param ancho: la cantidad de casilleros que se desea agregar de manera horizontal 
	 * @param alto: la cantidad de casilleros que se desea agregar de manera vertical
	 * @param imagenDeCasillero
	 */
	ImagenDeTablero(Integer ancho, Integer alto , ImagenDeCasillero imagenDeCasillero){
		this.alto = alto;
		this.ancho = ancho;
		
		this.imagenDeCasillero = imagenDeCasillero;
		this.anchuraTablero = this.ancho*imagenDeCasillero.getTamanio();
		this.alturaTablero = this.alto*imagenDeCasillero.getTamanio();
		
		BufferedImage imagenTablero = new BufferedImage(this.anchuraTablero, this.alturaTablero,  BufferedImage.TYPE_INT_RGB); 
        
        Graphics imagenCompleta = imagenTablero.getGraphics();
        for(int i = 0 ; i<this.ancho; i++) {
        	for(int j = 0 ; j< this.alto ; j++) {
        		imagenCompleta.drawImage(imagenDeCasillero.getImagenCasillero(),i*imagenDeCasillero.getTamanio(),
        				j*imagenDeCasillero.getTamanio(),null);
            }
        }
        this.imagenTablero=imagenTablero;
	}
	/**
	 * regresa la imagen del buffer del tablero
	 * @return
	 */
	BufferedImage getImagenTablero() {
		return this.imagenTablero;
	}
	/**
	 * regresa la longitud de horizontal del tablero
	 * @return
	 */
	int getAlturaTablero() {
		return this.alturaTablero;
	}
	/**
	 * regresa la longitud de vertical del tablero
	 * @return
	 */
	int getAnchuraTablero() {
		return this.anchuraTablero;
	}
	/**
	 * regresa la cantidad de casillero de manera horizontal
	 * @return
	 */
	int getAncho() {
		return this.ancho;
	}
	/**
	 * regresa la cantidad de casillero de manera vertical
	 * @return
	 */
	int getAlto() {
		return this.alto;
	}
	/**
	 * regresa una imagen de casillero que se tomo de referencia 
	 * @return
	 */
	ImagenDeCasillero getImagenCasillero() {
		return this.imagenDeCasillero;
	}
}

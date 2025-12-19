package Tateti.InterfazGrafica;
import java.awt.image.BufferedImage;

import Enums.Color;
import Tateti.ColorRGB;

public class ImagenDeCasillero {
	
	private final int TAMANIO_CASILLERO = 50;
	private final int TAMANIO_BORDES_CASILLERO= 1;
	private BufferedImage imagenCasillero = null;
	private ColorRGB colorDeCasillero = null;
	private ColorRGB colorDeMarco = null;
	
	/**
	 * crea una imagen de un casillero con bordes en el buffer, de dimension 50x50px  
	 * @param colorDeCasillero: el color que tendra el casillero
	 * @param colorDeMarco: el color del marco
	 * @throws Exception
	 */
	ImagenDeCasillero(ColorRGB colorDeCasillero, ColorRGB colorDeMarco) throws Exception{
		this.colorDeCasillero = colorDeCasillero; 
		this.colorDeMarco = colorDeMarco;
		
		int rgbColorCasillero = colorDeCasillero.getRGB();
		int rgbColorDeMarco = colorDeMarco.getRGB();
		
		
		int[][] matrizRGB = new int[TAMANIO_CASILLERO][TAMANIO_CASILLERO];
		for(int i = 0; i<TAMANIO_CASILLERO ; i++) {
			for(int j = 0 ; j< TAMANIO_CASILLERO; j++ ) {
				matrizRGB[i][j] = rgbColorCasillero;
			}
		}
		
		
		for(int i = 0; i<TAMANIO_CASILLERO ; i++) {
			for(int j = 0; j<=TAMANIO_BORDES_CASILLERO ;j++) {
				matrizRGB[i][TAMANIO_BORDES_CASILLERO-j] = rgbColorDeMarco;
				matrizRGB[TAMANIO_BORDES_CASILLERO-j][i] = rgbColorDeMarco;
				matrizRGB[TAMANIO_CASILLERO-TAMANIO_BORDES_CASILLERO-j][i] = rgbColorDeMarco;
				matrizRGB[i][TAMANIO_CASILLERO-TAMANIO_BORDES_CASILLERO-j] = rgbColorDeMarco;
			}
			
		}
		BufferedImage imagenCasillero = new BufferedImage(TAMANIO_CASILLERO, TAMANIO_CASILLERO, BufferedImage.TYPE_INT_RGB);
        for(int i = 0; i < 50 ; i++) {
			for( int j = 0; j < 50 ; j++) {
				imagenCasillero.setRGB(i, j, matrizRGB[i][j]);
			}
		}
        this.imagenCasillero=imagenCasillero;
	}
	/**
	 * regresa la imagen del casillero que se encuntra en el buffer
	 * @return
	 */
	public BufferedImage getImagenCasillero() {
		return this.imagenCasillero;
	}
	
	/**
	 * agrega una imagen de una ficha al casillero
	 * @param colorFicha
	 * @throws Exception
	 */
	public void agregarFicha(ColorRGB colorFicha) throws Exception {
		int rgbColorFicha = colorFicha.getRGB();
		int rgbColorDeMarco = this.colorDeMarco.getRGB();
		
		int[][] matrizRGB = new int[TAMANIO_CASILLERO][TAMANIO_CASILLERO];
		for(int i = 0; i<TAMANIO_CASILLERO ; i++) {
			for(int j = 0; j<=TAMANIO_BORDES_CASILLERO ;j++) {
				matrizRGB[i][TAMANIO_BORDES_CASILLERO-j] = rgbColorDeMarco;
				matrizRGB[TAMANIO_BORDES_CASILLERO-j][i] = rgbColorDeMarco;
				matrizRGB[TAMANIO_CASILLERO-TAMANIO_BORDES_CASILLERO-j][i] = rgbColorDeMarco;
				matrizRGB[i][TAMANIO_CASILLERO-TAMANIO_BORDES_CASILLERO-j] = rgbColorDeMarco;
			}
			
		}
		
		for(int i = 2*TAMANIO_BORDES_CASILLERO; i < (TAMANIO_CASILLERO-2*TAMANIO_BORDES_CASILLERO); i++) {
			for (int j = 2*TAMANIO_BORDES_CASILLERO; j < (TAMANIO_CASILLERO-2*TAMANIO_BORDES_CASILLERO); j++) {
	            int distanciaAlCentro = (int) Math.sqrt(Math.pow(i - (TAMANIO_CASILLERO-2*TAMANIO_BORDES_CASILLERO)/2, 2) + 
	            		Math.pow(j - (TAMANIO_CASILLERO-2*TAMANIO_BORDES_CASILLERO)/2, 2));
	            if(distanciaAlCentro <= (TAMANIO_CASILLERO-2*TAMANIO_BORDES_CASILLERO)/2) {
	            	matrizRGB[i][j] = rgbColorFicha;
	            	if(distanciaAlCentro == (TAMANIO_CASILLERO-2*TAMANIO_BORDES_CASILLERO)/2) {
		            	matrizRGB[i][j] = rgbColorDeMarco;
		            }
	            }
	    	}
	    }
		
		BufferedImage imagenCasillero = new BufferedImage(TAMANIO_CASILLERO, TAMANIO_CASILLERO, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < 50 ; i++) {
			for( int j = 0; j < 50 ; j++) {
				imagenCasillero.setRGB(i, j, matrizRGB[i][j]);
			}
		}
        this.imagenCasillero=imagenCasillero;
	}
	/**
	 * agrega un simbolo de bloqueo a la ficha
	 * @throws Exception
	 */
	public void agregarBloqueoAFicha() throws Exception {
		
		int rgbColorBorde = new ColorRGB(Color.NEGRO).getRGB();
		int rgbColorBloqueo = new ColorRGB(Color.ROJO).getRGB();
		
		int[][] matrizRGB = new int[TAMANIO_CASILLERO][TAMANIO_CASILLERO];
		
		
		for(int i = 2*TAMANIO_BORDES_CASILLERO; i < (TAMANIO_CASILLERO-2*TAMANIO_BORDES_CASILLERO); i++) {
			for (int j = 2*TAMANIO_BORDES_CASILLERO; j < (TAMANIO_CASILLERO-2*TAMANIO_BORDES_CASILLERO); j++) {
	            int distanciaAlCentro = (int) Math.sqrt(Math.pow(i - (TAMANIO_CASILLERO-2*TAMANIO_BORDES_CASILLERO)/2, 2) + 
	            		Math.pow(j - (TAMANIO_CASILLERO-2*TAMANIO_BORDES_CASILLERO)/2, 2));
	            if(distanciaAlCentro == (TAMANIO_CASILLERO-2*TAMANIO_BORDES_CASILLERO)/2) {
	            	matrizRGB[i][j] = rgbColorBorde;
	            }
	            if(distanciaAlCentro == (TAMANIO_CASILLERO-2*TAMANIO_BORDES_CASILLERO)/2-1) {
	            	matrizRGB[i][j] = rgbColorBloqueo;
	            }
	            if(distanciaAlCentro == (TAMANIO_CASILLERO-2*TAMANIO_BORDES_CASILLERO)/2-2) {
	            	matrizRGB[i][j] = rgbColorBorde;
	            }
	            if(distanciaAlCentro <= (TAMANIO_CASILLERO-2*TAMANIO_BORDES_CASILLERO)/2) {
	            	if(i==j) {
	            		matrizRGB[i-1][j] = rgbColorBorde;
	            		matrizRGB[i][j] = rgbColorBloqueo;
	            		matrizRGB[i+1][j] = rgbColorBorde;
	            	}
	            }
	    	}
	    }
		
		BufferedImage imagenCasillero = new BufferedImage(TAMANIO_CASILLERO, TAMANIO_CASILLERO, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < 50 ; i++) {
			for( int j = 0; j < 50 ; j++) {
				imagenCasillero.setRGB(i, j, matrizRGB[i][j]);
			}
		}
        this.imagenCasillero=imagenCasillero;
	}
	/**
	 * crea un casillero con una cruz
	 * @throws Exception
	 */
	public void bloquearCasillero() throws Exception{
		int rgbColorCruz = new ColorRGB(Color.NEGRO).getRGB();
		int rgbColorCruz2 = new ColorRGB(Color.ROJO_OSCURO).getRGB();
		
		int[][] matrizRGB = new int[TAMANIO_CASILLERO][TAMANIO_CASILLERO];
		
		for(int i = 1; i<TAMANIO_CASILLERO-1 ; i++) {
			matrizRGB[i][i] = rgbColorCruz2;
			matrizRGB[i-1][i] = rgbColorCruz;
			matrizRGB[i+1][i] = rgbColorCruz;
			matrizRGB[TAMANIO_CASILLERO-1-i][i] = rgbColorCruz2;
			matrizRGB[TAMANIO_CASILLERO-1-i-1][i] = rgbColorCruz;
			matrizRGB[TAMANIO_CASILLERO-1-i+1][i] = rgbColorCruz;
		}
		
		BufferedImage imagenCasillero = new BufferedImage(TAMANIO_CASILLERO, TAMANIO_CASILLERO, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < 50 ; i++) {
			for( int j = 0; j < 50 ; j++) {
				imagenCasillero.setRGB(i, j, matrizRGB[i][j]);
			}
		}
        this.imagenCasillero=imagenCasillero;
	}
	
	/**
	 * devuelve el color del casillero
	 * @return
	 */
	ColorRGB getColorCasillero() {
		return this.colorDeCasillero;
	}
	/**
	 * devuelve el color del marco
	 * @return
	 */
	ColorRGB getColorMarco() {
		return this.colorDeMarco;
	}
	/**
	 * devuelve el color del casillero en RGB
	 * @return
	 */
	int getColorCasilleroRGB() {
		return this.colorDeCasillero.getRGB();
	}
	/**
	 * devuelve el color del marco del casillero en RGB
	 * @return
	 */
	int getColorMarcoRGB() {
		return this.colorDeMarco.getRGB();
	}
	/**
	 * obtiene el tamanio de un lado del casillero 
	 * @return
	 */
	int getTamanio() {
		return this.TAMANIO_CASILLERO;
	}
	/**
	 * obtiene el tamanio del ancho de los bordes del casillero
	 * @return
	 */
	int getTamanioBordesCasillero() {
		return this.TAMANIO_BORDES_CASILLERO;
	}
	
}

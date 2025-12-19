package Tateti.InterfazGrafica;


import java.awt.Graphics;
import java.awt.image.BufferedImage;
import Enums.Color;
import Tateti.ColorRGB;

public class ImagenDeTableroCompleto {
	private ImagenDeTablero imagenDeTablero = null;
	private ColorRGB colorFondo = null;
	private TipoDeImagen tipoDeImagen = null;
	private Integer cantidadDeTableros = null;
	private int numeroDivisor;
	private int TAMANIO_BORDES=20;
	private BufferedImage imagenTableroCompleta = null;
	
	/**
	 * pre: ninguno de los parametros deben ser null
	 * post: crea la imagen de los tableros segun sus dimensiones
	 * @param nombreDeArchivo
	 * @param imagenDeTablero
	 * @param colorFondo
	 * @param cantidadDeTableros
	 */
	ImagenDeTableroCompleto( ImagenDeTablero imagenDeTablero, ColorRGB colorFondo, Integer cantidadDeTableros){
		this.imagenDeTablero = imagenDeTablero;
		this.colorFondo = colorFondo;
		this.cantidadDeTableros=cantidadDeTableros;
		
		if(this.cantidadDeTableros==1) {
			this.tipoDeImagen = TipoDeImagen.IMAGEN_PEQUENIA;
			this.imagenTableroCompleta = crearImagenTotalNivelBasico();
		}
		if(imagenDeTablero.getAlto()*imagenDeTablero.getAncho()*this.cantidadDeTableros>1
				&& imagenDeTablero.getAlto()*this.cantidadDeTableros<=100 &&
				imagenDeTablero.getAncho()*this.cantidadDeTableros<=100 ) {
			this.tipoDeImagen = TipoDeImagen.IMAGEN_MEDIANA;
			this.imagenTableroCompleta = crearImagenTotalNivelMedio();
		}
		else{
			this.tipoDeImagen = TipoDeImagen.IMAGEN_GRANDE;
			this.imagenTableroCompleta = crearImagenTotalNivelGrande();
		}
	}
	/**
	 * crea una imagen del tablero en caso de que solo sea 1 tablero
	 * @return
	 */
	private BufferedImage crearImagenTotalNivelBasico() {
		int anchoImagen = TAMANIO_BORDES+this.imagenDeTablero.getAlturaTablero(); 
		int altoImagen= TAMANIO_BORDES+this.imagenDeTablero.getAnchuraTablero();
		
		int rgbColorDeFondo = this.colorFondo.getRGB();
		
		BufferedImage imagenTotal = new BufferedImage( anchoImagen, altoImagen , BufferedImage.TYPE_INT_RGB);
		Graphics imagenCompleta = imagenTotal.getGraphics();
		for(int i = 0 ; i<anchoImagen ; i++) {
        	for(int j = 0 ; j< altoImagen ; j++) {
        		imagenTotal.setRGB(i, j, rgbColorDeFondo);
            }
        }
		
		for(int i = 0 ; i<this.cantidadDeTableros ; i++) {
        	imagenCompleta.drawImage(this.imagenDeTablero.getImagenTablero(), TAMANIO_BORDES/2, TAMANIO_BORDES/2,null);
        	imagenCompleta.drawString("Tablero "+(i+1), TAMANIO_BORDES/2, 
					(this.imagenDeTablero.getAlturaTablero())-TAMANIO_BORDES);
        }
		return imagenTotal;
	}
	/**
	 * crea una imagen del tablero de manera escalonada
	 * @return
	 */
	private BufferedImage crearImagenTotalNivelMedio() {
		int alturaImagenTotal = TAMANIO_BORDES*2+this.imagenDeTablero.getAlturaTablero()+
				(this.imagenDeTablero.getAlturaTablero()/2)*(this.cantidadDeTableros-1);
		int anchuraImagenTotal = TAMANIO_BORDES/2+
				(this.imagenDeTablero.getAnchuraTablero()+TAMANIO_BORDES)*this.cantidadDeTableros;
		
		int rgbColorDeFondo = this.colorFondo.getRGB();
		
		BufferedImage imagenTotal = new BufferedImage( anchuraImagenTotal, alturaImagenTotal, BufferedImage.TYPE_INT_RGB);
		Graphics imagenCompleta = imagenTotal.getGraphics();
		for(int i = 0 ; i<anchuraImagenTotal ; i++) {
        	for(int j = 0 ; j< alturaImagenTotal ; j++) {
        		imagenTotal.setRGB(i, j, rgbColorDeFondo);
            }
        }
		for(int i = 0 ; i<this.cantidadDeTableros ; i++) {
			if(i==0) {
				imagenCompleta.drawImage(this.imagenDeTablero.getImagenTablero(), TAMANIO_BORDES/2, 
						(alturaImagenTotal-this.imagenDeTablero.getAlturaTablero())-TAMANIO_BORDES/2,null);
				imagenCompleta.drawString("Tablero "+(i+1), TAMANIO_BORDES/2, 
						(alturaImagenTotal-this.imagenDeTablero.getAlturaTablero())-TAMANIO_BORDES);
			}else {
				imagenCompleta.drawImage(this.imagenDeTablero.getImagenTablero(), 
						TAMANIO_BORDES/2+TAMANIO_BORDES*i+this.imagenDeTablero.getAnchuraTablero()*i,
						(alturaImagenTotal-this.imagenDeTablero.getAlturaTablero()/2*(i+2)-TAMANIO_BORDES/2),null);
				imagenCompleta.drawString("Tablero "+(i+1), TAMANIO_BORDES/2+TAMANIO_BORDES*i+this.imagenDeTablero.getAnchuraTablero()*i,
						(alturaImagenTotal-this.imagenDeTablero.getAlturaTablero()/2*(i+2)-TAMANIO_BORDES));
			}
        }
		return imagenTotal;
	}
	/**
	 * crea una imagen de manera de cuadricula 
	 * @return
	 */
	private BufferedImage crearImagenTotalNivelGrande() {
		double numero = Math.sqrt(this.getCantidadDeTableros());
		int numeroDivisor = (int) Math.ceil(numero);
		this.numeroDivisor = numeroDivisor;
		double diferencia = Math.round(numeroDivisor - numero);
		
		
		int anchuraImagenTotal=(TAMANIO_BORDES
				+this.imagenDeTablero.getAnchuraTablero())*numeroDivisor;
		int alturaImagenTotal=(TAMANIO_BORDES*2
				+this.imagenDeTablero.getAlturaTablero())*(numeroDivisor-(int)diferencia);
		
		
		int rgbColorDeFondo = this.colorFondo.getRGB();
		
		BufferedImage imagenTotal = new BufferedImage(anchuraImagenTotal, alturaImagenTotal , BufferedImage.TYPE_INT_RGB);
		Graphics imagenCompleta = imagenTotal.getGraphics();
		for(int i = 0 ; i<anchuraImagenTotal ; i++) {
        	for(int j = 0 ; j< alturaImagenTotal ; j++) {
        		imagenTotal.setRGB(i, j, rgbColorDeFondo);
            }
        }
		
		int contador=0;
		for(int j = 0 ; j < numeroDivisor ; j++) {
			for(int i = 0 ; i< numeroDivisor; i++) {
        		if(contador<this.cantidadDeTableros) {
		    		imagenCompleta.drawImage(this.imagenDeTablero.getImagenTablero()
		    				, TAMANIO_BORDES/2+TAMANIO_BORDES*i+i*this.imagenDeTablero.getAnchuraTablero()
		    				, TAMANIO_BORDES+2*TAMANIO_BORDES*j+j*this.imagenDeTablero.getAlturaTablero(),null);
		    		imagenCompleta.drawString("Tablero "+(contador+1), TAMANIO_BORDES/2+TAMANIO_BORDES*i+i*this.imagenDeTablero.getAnchuraTablero()
					, TAMANIO_BORDES/2+2*TAMANIO_BORDES*j+j*this.imagenDeTablero.getAlturaTablero());
		    		contador++;
        		}
            }
        }
		
		return imagenTotal;
	}
	/**
	 * agrega una ficha a la imagen del casillero segun la posicion pasado por parametro
	 * @param x
	 * @param y
	 * @param z
	 * @param colorDeCasillero
	 * @throws Exception
	 */
	public void agregarFicha(int x, int y, int z, ColorRGB colorDeCasillero) throws Exception {
		
		ColorRGB colorDemarco = comprobarIgualdad(colorDeCasillero); 
		ImagenDeCasillero imagenCasillero = new ImagenDeCasillero(colorDeCasillero, colorDemarco);
		imagenCasillero.agregarFicha(colorDeCasillero);
        switch(this.tipoDeImagen) {
        	case IMAGEN_PEQUENIA:
        		agregarFichaAlArchivoPequenia(x, y, z, imagenCasillero);
        		break;
        	case IMAGEN_MEDIANA:
        		agregarFichaAlArchivoMedio(x,y,z,imagenCasillero);
        		break;
        	case IMAGEN_GRANDE:
        		agregarFichaAlArchivoGrande(x,y,z,imagenCasillero);
        		break;
        	default:
        		throw new Exception("Error en la escritura del archivo");
        }
	}
	/**
	 * agrega una ficha bloqueada a la imagen del casillero segun la posicion pasado por parametro
	 * @param x
	 * @param y
	 * @param z
	 * @param colorDeFicha
	 * @throws Exception
	 */
	
	public void agregarFichaBloqueada(int x, int y, int z, ColorRGB colorDeFicha) throws Exception {
		ColorRGB colorDemarco = this.imagenDeTablero.getImagenCasillero().getColorMarco(); 
		
		ImagenDeCasillero imagenCasillero = new ImagenDeCasillero(colorDeFicha, colorDemarco);
		imagenCasillero.agregarFicha(colorDeFicha);
		imagenCasillero.agregarBloqueoAFicha();
		
        switch(this.tipoDeImagen) {
        	case IMAGEN_PEQUENIA:
        		agregarFichaAlArchivoPequenia(x, y, z, imagenCasillero);
        		break;
        	case IMAGEN_MEDIANA:
        		agregarFichaAlArchivoMedio(x,y,z,imagenCasillero);
        		break;
        	case IMAGEN_GRANDE:
        		agregarFichaAlArchivoGrande(x,y,z,imagenCasillero);
        		break;
        }
	}
	/**
	 * agrega una cruz al casillero segun la posicion pasado por parametro
	 * @param x
	 * @param y
	 * @param z
	 * @param colorDeCasillero
	 * @throws Exception
	 */
	public void agregarCasilleroBloqueado(int x, int y, int z, ColorRGB colorDeCasillero) throws Exception {
		ColorRGB colorDemarco = this.imagenDeTablero.getImagenCasillero().getColorMarco(); 
		
		ImagenDeCasillero imagenCasillero = new ImagenDeCasillero(colorDeCasillero, colorDemarco);
		imagenCasillero.bloquearCasillero();
        switch(this.tipoDeImagen) {
        	case IMAGEN_PEQUENIA:
        		agregarFichaAlArchivoPequenia(x, y, z, imagenCasillero);
        		break;
        	case IMAGEN_MEDIANA:
        		agregarFichaAlArchivoMedio(x,y,z,imagenCasillero);
        		break;
        	case IMAGEN_GRANDE:
        		agregarFichaAlArchivoGrande(x,y,z,imagenCasillero);
        		break;
        }
	}
	
	/**
	 * vacia al casillero segun el color del casillero segun la posicion pasado por parametro
	 * @param x
	 * @param y
	 * @param z
	 * @param colorDeCasillero
	 * @throws Exception
	 */
	public void vaciarCasillero(int x, int y, int z, ColorRGB colorDeCasillero) throws Exception {
		
		ColorRGB colorDemarco = comprobarIgualdad(colorDeCasillero); 
		ImagenDeCasillero imagenCasillero = new ImagenDeCasillero(colorDeCasillero, colorDemarco);
		
        switch(this.tipoDeImagen) {
        	case IMAGEN_PEQUENIA:
        		agregarFichaAlArchivoPequenia(x, y, z, imagenCasillero);
        		break;
        	case IMAGEN_MEDIANA:
        		agregarFichaAlArchivoMedio(x,y,z,imagenCasillero);
        		break;
        	case IMAGEN_GRANDE:
        		agregarFichaAlArchivoGrande(x,y,z,imagenCasillero);
        		break;
        	default:
        		throw new Exception("Error en la escritura del archivo");
        }
	}
	/**
	 * comprueba si el color de la ficha es igual al color de casillero, en caso de ser verdadero agrega un borde blanco
	 * @param colorDeCasillero
	 * @return
	 * @throws Exception
	 */
	private ColorRGB comprobarIgualdad(ColorRGB colorDeFicha) throws Exception {
		if(colorDeFicha.getRGB()==this.imagenDeTablero.getImagenCasillero().getColorCasillero().getRGB()) {
			return new ColorRGB(Color.BLANCO); 
		}else {
			return this.imagenDeTablero.getImagenCasillero().getColorMarco();
		}
	}
	
	/**
	 * agrega la ficha al tablero para el caso que el archivo sea de IMAGEN_PEQUENIA
	 * @param x
	 * @param y
	 * @param z
	 * @param imagenCasillero
	 */
	private void agregarFichaAlArchivoPequenia(int x, int y, int z, ImagenDeCasillero imagenCasillero) {
		
		int tamanioCasillero = this.imagenDeTablero.getImagenCasillero().getTamanio();
	
		try {
			BufferedImage imagenCompleta = this.imagenTableroCompleta;
			Graphics imagen = imagenCompleta.getGraphics();
			imagen.drawImage(imagenCasillero.getImagenCasillero(),
					TAMANIO_BORDES*(z-1)+TAMANIO_BORDES/2+this.imagenDeTablero.getAnchuraTablero()*(z-1)+
					tamanioCasillero*(x-1),
					(imagenCompleta.getHeight()-this.imagenDeTablero.getAlturaTablero()/2*(z+1)-TAMANIO_BORDES/2
							+tamanioCasillero*(y-1)) ,null);
			this.imagenTableroCompleta=imagenCompleta;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * agrega la ficha al tablero para el caso que el archivo sea de IMAGEN_MEDIANA
	 * @param x
	 * @param y
	 * @param z
	 * @param imagenCasillero
	 */
	private void agregarFichaAlArchivoMedio(int x, int y, int z, ImagenDeCasillero imagenCasillero) {
		
		int tamanioCasillero = this.imagenDeTablero.getImagenCasillero().getTamanio();
	
		try {
			BufferedImage imagenCompleta = this.imagenTableroCompleta;
			Graphics imagen = imagenCompleta.getGraphics();
			imagen.drawImage(imagenCasillero.getImagenCasillero(),
					TAMANIO_BORDES*(z-1)+TAMANIO_BORDES/2+this.imagenDeTablero.getAnchuraTablero()*(z-1)+
					tamanioCasillero*(x-1),
					(imagenCompleta.getHeight()-this.imagenDeTablero.getAlturaTablero()/2*(z+1)-TAMANIO_BORDES/2
							+tamanioCasillero*(y-1)) ,null);
			this.imagenTableroCompleta=imagenCompleta;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * agrega la ficha al tablero para el caso que el archivo sea de IMAGEN_GRANDE
	 * @param x
	 * @param y
	 * @param z
	 * @param imagenCasillero
	 */
	private void agregarFichaAlArchivoGrande(int x, int y, int z, ImagenDeCasillero imagenCasillero) {
		
		try {
			BufferedImage imagenCompleta = this.imagenTableroCompleta;
			Graphics imagen = imagenCompleta.getGraphics();
			int contador=1;
			
			
			for(int j = 0 ; j< this.numeroDivisor ; j++) {
				for(int i = 0 ; i< this.numeroDivisor; i++) {
	        		if(contador==z) {
	        			imagen.drawImage(imagenCasillero.getImagenCasillero()
		        				, TAMANIO_BORDES/2+TAMANIO_BORDES*i+i*this.imagenDeTablero.getAnchuraTablero()
		        				+(x-1)*this.imagenDeTablero.getImagenCasillero().getTamanio()
		        				
		        				, TAMANIO_BORDES+2*TAMANIO_BORDES*j+j*this.imagenDeTablero.getAlturaTablero()
		        				+(y-1)*this.imagenDeTablero.getImagenCasillero().getTamanio()
		        				,null);
	        			
	        		}
	        		contador++;
	            }
	        }
			
			this.imagenTableroCompleta=imagenCompleta;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * devuelve la cantidad de tableros
	 * @return
	 */
	Integer getCantidadDeTableros(){
		return this.cantidadDeTableros;
	}
	/**
	 * devuelve la altura del tableros
	 * @return
	 */
	Integer getAlturaTablero() {
		return this.imagenDeTablero.getAlto();
	}
	/**
	 * devuelve el ancho del tableros
	 * @return
	 */
	Integer getAnchoTablero() {
		return this.imagenDeTablero.getAncho();
	}
	
	/**
	 * regresa la imagen completa de los tableros que se encuntra en el buffer
	 * @return
	 */
	public BufferedImage getImagenTableroCompleta() {
		return this.imagenTableroCompleta;
	}
}

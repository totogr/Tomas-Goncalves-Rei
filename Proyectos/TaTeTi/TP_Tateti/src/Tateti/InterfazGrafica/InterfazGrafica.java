package Tateti.InterfazGrafica;

import java.io.File;

import javax.imageio.ImageIO;

import Enums.Color;
import Enums.EstadoDeBloqueo;
import Tateti.ColorRGB;
import Tateti.Ficha;
import Tateti.Tablero;

public class InterfazGrafica  {
	private String nombreDeArchivo = "SalidaImagenCompleta";
	private String formatoDeArchivo = ".bmp";
	private int turno = 0;
	private ColorRGB colorFondo = null;
	private ColorRGB colorDeCasillero = null;
	private ColorRGB colorDeMarco = null;
	private ImagenDeTableroCompleto imagenDeTablero = null;
	private Tablero<Ficha> tablero = null;
	
	/**
	 * crea una imagen del tablero con sus respectivas dimensiones
	 * @param tablero
	 * @throws Exception
	 */
	public InterfazGrafica(Tablero<Ficha> tablero) throws Exception{
		this.tablero=tablero;
		this.colorFondo=new ColorRGB(102, 205, 170);
		this.colorDeCasillero=new ColorRGB(255, 171, 102);
		this.colorDeMarco=new ColorRGB(Color.BLANCO);
		
		ImagenDeCasillero imagenCasillero = new ImagenDeCasillero(colorDeCasillero, colorDeMarco);
		ImagenDeTablero imagenTablero = new ImagenDeTablero(tablero.getAncho() , tablero.getAlto(), imagenCasillero);
		ImagenDeTableroCompleto imagenCompleto = new ImagenDeTableroCompleto(
				 imagenTablero, this.colorFondo, tablero.getProfundidad());
		this.imagenDeTablero=imagenCompleto;
		this.turno=0;
		crearArchivo();
	}
	
	/**
	 * actualiza los cambios producidos en el tablero a la imagen de salida
	 * @throws Exception
	 */
	public void actualizarTablero() throws Exception {
		for(int x=1; x<=tablero.getAncho() ; x++) {
			for(int y=1; y<=tablero.getAlto() ; y++) {
				for(int z=1; z<=tablero.getProfundidad() ; z++) {
					comprobarEstado(x,y,z);
				}
			}
		}
		crearArchivo();
	}
	
	/**
	 * agrega una ficha en la posicion y color pasado por parametro
	 * @param ancho
	 * @param alto
	 * @param profundidad
	 * @param colorFicha
	 * @throws Exception
	 */
	private void agregarFicha(Integer ancho, Integer alto , Integer profundidad, ColorRGB colorFicha) throws Exception {
		if(validarPosicion( ancho,alto, profundidad)) {
			this.imagenDeTablero.agregarFicha( ancho , alto , profundidad , colorFicha);
		}
		else {
			throw new Exception("Posicion de tablero invalida");
		}
	}
	/**
	 * agrega una ficha bloqueada en la posicion y color pasado por parametro
	 * @param ancho
	 * @param alto
	 * @param profundidad
	 * @param colorFicha
	 * @throws Exception 
	 */
	private void agregarFichaBloqueada(Integer ancho, Integer alto , Integer profundidad, ColorRGB colorFicha) throws Exception {
		if(validarPosicion( ancho,alto, profundidad)) {
			this.imagenDeTablero.agregarFichaBloqueada(ancho, alto, profundidad, colorFicha);
		}
		else {
			throw new Exception("Posicion de tablero invalida");
		}
	}
	/**
	 * bloquea la ficha en la posicion establecida
	 * @param ancho
	 * @param alto
	 * @param profundidad
	 * @throws Exception
	 */
	private void bloquearCasillero(Integer ancho, Integer alto, Integer profundidad) throws Exception {
		this.imagenDeTablero.agregarCasilleroBloqueado(ancho, alto, profundidad, colorDeCasillero);
	}
	/**
	 * vacia al casillero segun el color establecido en colorDeCasillero
	 * @param ancho
	 * @param alto
	 * @param profundidad
	 * @param colorDeCasillero
	 * @throws Exception
	 */
	private void vaciarCasillero(Integer ancho, Integer alto , Integer profundidad, ColorRGB colorDeCasillero ) throws Exception {
		if(validarPosicion( ancho,alto, profundidad)) {
			this.imagenDeTablero.vaciarCasillero(ancho, alto, profundidad, colorDeCasillero);
		}
		else {
			throw new Exception("Posicion de tablero invalida");
		}
	}
	/**
	 * post: comprueba el estado del casillero, validando existe, esta ocupado, esta vacio, habilitado, 
	 * casillero bloqueado, con una ficha bloqueada. segun sea el caso cambia el estado del casillero
	 *  
	 * @param x
	 * @param y
	 * @param z
	 * @throws Exception
	 */
	private void comprobarEstado(Integer x, Integer y, Integer z) throws Exception {
		if(!validarPosicion(x,y,z)) {
			throw new Exception("posicion invalida");
		}
		
		if(validarCasilleroOcupado(x, y, z)) {
			ColorRGB colorFicha = this.tablero.getCasillero(x, y, z).getDato().getColorDeFicha();
			if(validarCasilleroHabilitado(x,y,z)) {
				if(validarFichaBloqueda(x,y,z)) {
					agregarFichaBloqueada(x, y, z, colorFicha);
				}else {
					agregarFicha(x, y, z, colorFicha);
				}
			}
			else if(validarCasilleroBloqueado(x,y,z)) {
				if(validarFichaBloqueda(x,y,z)) {
					agregarFichaBloqueada(x, y, z, colorFicha);
					bloquearCasillero(x, y ,z);
				}else {
					bloquearCasillero(x, y ,z);
				}
			}
		}
		else if(validarCasilleroVacio(x,y,z)) {
			if(validarCasilleroBloqueado(x,y,z)) {
				bloquearCasillero(x, y ,z);
			}
			else if(validarCasilleroHabilitado(x,y,z)) {
				vaciarCasillero(x, y, z, this.colorDeCasillero);
			}
		}
	}
	
	/**
	 * valida si el casillero pasado por parametro se encuntra en estado OCUPADO
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 * @throws Exception
	 */
	private boolean validarCasilleroOcupado(int x, int y, int z) throws Exception {
		return this.tablero.getCasillero(x, y, z).estaOcupado();
	}
	/**
	 * valida si la ficha pasado por parametro se encuentra en estado BLOQUEADO
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 * @throws Exception
	 */
	private boolean validarFichaBloqueda(int x, int y, int z) throws Exception {
		return this.tablero.getCasillero(x, y, z).getDato().estaBloqueado();
	}
	
	/**
	 * valida si el casillero pasado por parametro se encuentra en estado VACIO
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 * @throws Exception
	 */
	private boolean validarCasilleroVacio(int x, int y, int z) throws Exception {
		return this.tablero.getCasillero(x, y, z).estaVacio();
	}
	/**
	 * valida si el casillero pasado por parametro se encuentra en estado HABILITADO
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 * @throws Exception
	 */
	private boolean validarCasilleroHabilitado(int x, int y, int z) throws Exception {
		return (this.tablero.getCasillero(x, y, z).getEstado()==EstadoDeBloqueo.HABILITADO);
	}
	
	/**
	 * valida si el casillero pasado por parametro se encuentra en estado BLOQUEADO
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 * @throws Exception
	 */
	private boolean validarCasilleroBloqueado(int x, int y, int z) throws Exception {
		return (this.tablero.getCasillero(x, y, z).getEstado()==EstadoDeBloqueo.BLOQUEADO);
	}
	
	/**
	 * valida si la posicion se encuentra dentro de los dimensiones del tablero
	 * @param ancho
	 * @param alto
	 * @param profundidad
	 * @return
	 */
	private boolean validarPosicion(Integer ancho, Integer alto , Integer profundidad){
		if(ancho > this.imagenDeTablero.getAnchoTablero()) {
			return false;
		}
		if(alto > this.imagenDeTablero.getAlturaTablero()) {
			return false;
		}
		if(profundidad > this.imagenDeTablero.getCantidadDeTableros()) {
			return false;
		}
		return true;
	}
	private void crearArchivo() {
		try {
			String turno = String.valueOf(this.turno);
			File archivo = new File(this.nombreDeArchivo+" turno "+turno+" "+this.formatoDeArchivo);
			ImageIO.write(this.imagenDeTablero.getImagenTableroCompleta(), "bmp", archivo);
		}catch(Exception e) {
			e.printStackTrace();
		}
		this.turno++;
	}
}

package Tateti;

import Enums.Color;

public class ColorRGB {

	//ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
	//ATRIBUTOS -----------------------------------------------------------------------------------------------

	private int rojo;
	private int verde;
	private int azul;
	private Color color;

	//METODOS GENERALES ---------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return this.color.getNombreColor();
	}

	//CONSTRUCTORES -------------------------------------------------------------------------------------------

	/**
	 * pre:-
	 * @param rojo debe ser un valor entre 0 y 255 inclusive
	 * @param verde debe ser un valor entre 0 y 255 inclusive
	 * @param azul debe ser un valor entre 0 y 255 inclusive
	 * @throws Exception lanza una exepcion si alguno de los parametros esta fuera de rango
	 * post: crea un color RGB 
	 */
	public ColorRGB(int rojo, int verde, int azul)throws Exception{
		if(!Validacion.validarCantidadDeColorRGB(rojo)) {
			throw new Exception("La cantidad de rojo no es valida");
		}
		if(!Validacion.validarCantidadDeColorRGB(verde)) {
			throw new Exception("La cantidad de verde no es valida");
		}
		if(!Validacion.validarCantidadDeColorRGB(azul)) {
			throw new Exception("La cantidad de azul no es valida");
		}
		this.rojo=rojo;
		this.verde=verde;
		this.azul=azul;
	}

	/**
	 * pre: -
	 * post: crea un color RGB a traves de un numero
	 * @param numeroColor: numero de la lista de colores predefinidos
	 * @throws Exception: valida el numero introducido
	 */
	public ColorRGB(int numeroColor)throws Exception{
		if(!(numeroColor>0 && 
				numeroColor<=Color.getCantidadDeColores())) {
			throw new Exception("Numero de color fuera de rango");
		}
		switch (numeroColor) {
		case 1:
			this.rojo = 255;
			this.verde = 0;
			this.azul = 0;
			this.color=Color.ROJO;
			break;
		case 2:
			this.rojo = 34;
			this.verde = 153;
			this.azul = 84;
			this.color=Color.VERDE;
			break;
		case 3:
			this.rojo = 0;
			this.verde = 0;
			this.azul = 255;
			this.color=Color.AZUL;
			break;
		case 4:
			this.rojo = 255;
			this.verde = 255;
			this.azul = 0;
			this.color=Color.AMARILLO;
			break;
		case 5:
			this.rojo = 0;
			this.verde = 255;
			this.azul = 255;
			this.color=Color.CIAN;
			break;
		case 6:
			this.rojo = 255;
			this.verde = 0;
			this.azul = 255;
			this.color=Color.MAGENTA;
			break;
		case 7:
			this.rojo = 0;
			this.verde = 0;
			this.azul = 0;
			this.color=Color.NEGRO;
			break;
		case 8:
			this.rojo = 255;
			this.verde = 255;
			this.azul = 255;
			this.color=Color.BLANCO;
			break;
		case 9:
			this.rojo = 255;
			this.verde = 200;
			this.azul = 0;
			this.color=Color.NARANJA;
			break;
		case 10:
			this.rojo = 169;
			this.verde = 50;
			this.azul = 38;
			this.color=Color.ROJO_OSCURO;
			break;
		case 11: 
			this.rojo = 0;
			this.verde = 255;
			this.azul = 0;
			this.color=Color.VERDE_CLARO;
			break;
		case 12: 
			this.rojo = 26;
			this.verde = 82;
			this.azul = 118;
			this.color=Color.AZUL_OSCURO;
			break;
		case 13:  
			this.rojo = 93;
			this.verde = 173;
			this.azul = 226;
			this.color=Color.CELESTE;
			break;
		case 14:
			this.rojo = 142;
			this.verde = 68;
			this.azul = 173;
			this.color=Color.VIOLETA;
			break;
		case 15:  
			this.rojo = 255	;
			this.verde = 192;
			this.azul = 203;
			this.color=Color.ROSA;
			break;
		case 16:  
			this.rojo = 127;
			this.verde = 78;
			this.azul = 61;
			this.color=Color.MARRON;
			break;
		case 17:  
			this.rojo = 128;
			this.verde = 128;
			this.azul = 128;
			this.color=Color.GRIS;
			break;
		default:
			throw new Exception("El color no existe en la lista de color");
		}
	}

	/**
	 * pre: -
	 * post: crea el color RGB a traves del color introducido
	 * @param color: color a crear
	 * @throws Exception: valida que el color no sea nulo
	 */
	public ColorRGB(Color color)throws Exception{
		if(color == null) {
			throw new Exception("El color no puede ser vacio");
		}
		switch (color) {
		case ROJO:
			this.rojo = 255;
			this.verde = 0;
			this.azul = 0;				
			break;
		case VERDE:
			this.rojo = 34;
			this.verde = 153;
			this.azul = 84;
			break;
		case AZUL:
			this.rojo = 0;
			this.verde = 0;
			this.azul = 255;
			break;
		case AMARILLO:
			this.rojo = 255;
			this.verde = 255;
			this.azul = 0;
			break;
		case CIAN:
			this.rojo = 0;
			this.verde = 255;
			this.azul = 255;
			break;
		case MAGENTA:
			this.rojo = 255;
			this.verde = 0;
			this.azul = 255;
			break;
		case NEGRO:
			this.rojo = 0;
			this.verde = 0;
			this.azul = 0;
			break;
		case BLANCO:
			this.rojo = 255;
			this.verde = 255;
			this.azul = 255;
			break;
		case NARANJA:
			this.rojo = 255;
			this.verde = 200;
			this.azul = 0;
			break;
		case ROJO_OSCURO:
			this.rojo = 169;
			this.verde = 50;
			this.azul = 38;
			break;
		case VERDE_CLARO: 
			this.rojo = 0;
			this.verde = 255;
			this.azul = 0;
			break;
		case AZUL_OSCURO: 
			this.rojo = 26;
			this.verde = 82;
			this.azul = 118;
			break;
		case CELESTE:  
			this.rojo = 93;
			this.verde = 173;
			this.azul = 226;
			break;
		case VIOLETA:  
			this.rojo = 142;
			this.verde = 68;
			this.azul = 173;
			break;
		case ROSA:  
			this.rojo = 255	;
			this.verde = 192;
			this.azul = 203;
			break;
		case MARRON:  
			this.rojo = 127;
			this.verde = 78;
			this.azul = 61;
			break;
		case GRIS:  
			this.rojo = 128;
			this.verde = 128;
			this.azul = 128;
			break;
		default:
			throw new Exception("El color no existe en la lista de color");
		}
		this.color=color;
	}

	//GETTERS SIMPLES -----------------------------------------------------------------------------------------

	/**
	 * pre:-
	 * @return devuelve la cantidad de rojo
	 */
	public int getRojo() {
		return this.rojo;
	}

	/**
	 * pre:-
	 * @return devuelve la cantidad de verde
	 */
	public int getVerde() {
		return this.verde;
	}

	/**
	 * pre:-
	 * @return devuelve la cantidad de azul
	 */
	public int getAzul() {
		return this.azul;
	}
	/**
	 * 
	 * @return devuelve el color en un valor de 32 bits
	 */
	public int getRGB() {
		return (0xff000000 |(getRojo() & 0xFF) << 16) |
				((getVerde() & 0xFF) << 8)  |
				((getAzul() & 0xFF) << 0);
	}

	/**
	 * pre: -
	 * @return: devuelve el color
	 */
	public Color getColor() {
		return this.color;
	}
	
	/**
	 * pre:-
	 * @param color
	 * @return devuelve verdadero si es igual al colorRGB pasado por parametro y falso en caso contrario
	 */
	public boolean esIgual(ColorRGB color) {
		return (this.rojo == color.getRojo() && this.verde == color.getVerde() && this.azul == color.getAzul());
	}
}

package Tateti;

import Estructuras.ListaSimplementeEnlazada;

public class Tablero<T> {

	//ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
	//ATRIBUTOS -----------------------------------------------------------------------------------------------

	private int ancho = 0;
	private int alto = 0;
	private int profundidad = 0;
	private ListaSimplementeEnlazada<ListaSimplementeEnlazada<ListaSimplementeEnlazada<Casillero<T>>>> tablero = null;

	//CONSTRUCTORES -------------------------------------------------------------------------------------------

	/**
	 * pre: -
	 * post: crea un tablero de ancho x alto x profundidad con casilleros
	 * @param ancho: ancho del tablero
	 * @param alto: alto del tablero
	 * @param profundidad: profundidad del tablero
	 * @throws Exception: validaciones de las dimensiones
	 */
	public Tablero(int ancho, int alto, int profundidad) throws Exception {
		if (!Validacion.numeroMayorACero(ancho) ||
				!Validacion.numeroMayorACero(alto) ||
				!Validacion.numeroMayorACero(profundidad)) {
			throw new Exception("Las medidas del tablero deben ser mayor a 0");
		}
		this.ancho = ancho;
		this.alto = alto;
		this.profundidad = profundidad;
		this.tablero = new ListaSimplementeEnlazada<ListaSimplementeEnlazada<ListaSimplementeEnlazada<Casillero<T>>>>();
		for (int i = 1; i <= ancho; i++) {
			ListaSimplementeEnlazada<ListaSimplementeEnlazada<Casillero<T>>> fila = new ListaSimplementeEnlazada<ListaSimplementeEnlazada<Casillero<T>>>();
			for (int j = 1; j <= alto; j++) {
				ListaSimplementeEnlazada<Casillero<T>> columna = new ListaSimplementeEnlazada<Casillero<T>>();
				for (int z = 1; z <= profundidad; z++) {
					columna.agregar(new Casillero<T>(i, j, z));
				}
				fila.agregar(columna);
			}
			this.tablero.agregar(fila);
		}
	}

	//METODOS DE CLASE ----------------------------------------------------------------------------------------
	//METODOS GENERALES ---------------------------------------------------------------------------------------

	@Override
	public String toString() {	
		return "Tablero (" + this.ancho + " de ancho, " + this.alto + " de alto y " + this.profundidad + " de profundidad)";
	}

	//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

	/**
	 * pre: se obtuvo las coordenadas del casillero
	 * post: agrega un dato en un casillero del tablero
	 * @param x: posicion X
	 * @param y: posicion Y
	 * @param z: posicion Z
	 * @param dato: dato a agregar
	 * @throws Exception: validacion del casillero
	 */
	public void agregar(int x, int y, int z, T dato) throws Exception {
		Casillero<T> casillero = getCasillero(x, y, z);
		casillero.setDato(dato);
	}

	/**
	 * pre: -
	 * @param x: posicion X
	 * @param y: posicion Y
	 * @param z: posicion Z
	 * @return: devuelve el casillero en las coordenadas ingresadas
	 * @throws Exception: validacion al obtener el casillero
	 */
	public Casillero<T> getCasillero(int x, int y, int z) throws Exception {
		if (!Validacion.numeroMayorACero(x) ||
				!Validacion.numeroMayorACero(y) ||
				!Validacion.numeroMayorACero(z) ||
				x > this.ancho ||
				y > this.alto ||
				z > this.profundidad) {
			throw new Exception("Las medidas del casillero deben estar dentro de las medidas del tablero");
		}
		return this.tablero.obtener(x).obtener(y).obtener(z);
	}

	/**
	 * pre: -
	 * @param ficha: ficha buscada
	 * @return: devuelve el casillero que contiene la ficha ingresada por parametro
	 * @throws Exception: lanza una excepcion en caso de que la ficha no se encuentre en el tablero
	 */
	public Casillero<T> getCasillero(Ficha ficha) throws Exception{
		for(int x = 1; x <= this.ancho; x++) {
			for(int y = 1; y <= this.alto; y++) {
				for(int z = 1; z <= this.profundidad; z++) {
					if(obtenerDato(x,y,z) != null && obtenerDato(x,y,z)==ficha) {
						return getCasillero(x,y,z);
					}
				}
			}
		}
		throw new Exception("la ficha no fue encontrada en el tablero");
	}

	/**
	 * pre: se obtuvo el casillero en las coordenadas ingresadas
	 * @param x: posicion X
	 * @param y: posicion Y
	 * @param z: posicion Z
	 * @return: devuelve el dato que tiene el casillero
	 * @throws Exception: validacion del casillero
	 */
	public T obtenerDato(int x, int y, int z) throws Exception {
		return getCasillero(x, y, z).getDato();
	}

	/**
	 * pre: -
	 * post: mueve una ficha de un casillero a otro
	 * @param casillero: recibe el casillero en el que esta la ficha que se va a mover
	 * @param casilleroVecino: recibe el casillero al cual se va a mover la ficha
	 * @param ficha: recibe la ficha que va a ser movida
	 * @throws Exception: validacion de casillero y casillero vecino
	 */
	public void mover(Casillero<Ficha> casillero, Casillero<Ficha> casilleroVecino, Ficha ficha) throws Exception {
		if(casillero == null) {
			throw new Exception("El casillero esta vacio");
		}
		if(casilleroVecino.estaOcupado()) {
			throw new Exception("El casillero al cual se quiere mover esta ocupado");
		}
		if(casilleroVecino.estaBloqueado()) {
			throw new Exception("El casillero donde se quiere mover se encuentra bloqueado");
		}
		if(ficha.estaBloqueado()) {
			throw new Exception("La ficha que quiere mover se encuentra bloqueada");
		}
		casilleroVecino.setDato(ficha);
		casillero.desocupar();
	}

	/**
	 * pre: -
	 * @param casillero: casillero donde poner la ficha
	 * @param ficha: ficha a introducir en casillero
	 * @throws Exception: valida que el casillero no este bloqueado
	 * post: ingresa ficha en casillero
	 */
	public void ponerFicha(Casillero<Ficha> casillero, Ficha ficha) throws Exception {
		if(casillero.estaBloqueado()) {
			throw new Exception("El casillero se encuentra bloqueado");
		}
		casillero.setDato(ficha);
		ficha.ubicarEnTablero();
	}

	/**
	 * pre: -
	 * @param ancho
	 * @param alto
	 * @param profundidad
	 * @return: valida la posicion de las coordeandas introducidas
	 */
	public boolean validarPosicion(Integer ancho, Integer alto , Integer profundidad){
		if(!Validacion.numeroMayorACero(ancho) || ancho > getAncho()) {
			return false;
		}
		if(!Validacion.numeroMayorACero(ancho) || alto > getAlto()) {
			return false;
		}
		if(!Validacion.numeroMayorACero(ancho) || profundidad > getProfundidad()) {
			return false;
		}
		return true;
	}

	//GETTERS SIMPLES -----------------------------------------------------------------------------------------

	/**
	 * pre: -
	 * @return: devuelve el ancho del tablero
	 */
	public int getAncho() {
		return this.ancho;
	}

	/**
	 * pre: -
	 * @return: devuelve el ato del tablero
	 */
	public int getAlto() {
		return this.alto;
	}

	/**
	 * pre: -
	 * @return: devuelve la profundidad del tablero
	 */
	public int getProfundidad() {
		return this.profundidad;
	}

	/**
	 * pre: -
	 * @return: devuelve el tablero
	 */
	public ListaSimplementeEnlazada<ListaSimplementeEnlazada<ListaSimplementeEnlazada<Casillero<T>>>> getTablero() {
		return this.tablero;
	}

	/**
	 * pre:-
	 * @return devuelve la longitud mas corta del tablero
	 */
	public int obtenerLadoMasCorto() {
		int ladoMasCorto = this.profundidad;
		if(this.alto < this.ancho && this.alto < this.profundidad) {
			ladoMasCorto= this.alto;
		}else if(this.ancho < this.alto && this.ancho < this.profundidad) {
			ladoMasCorto = this.ancho;
		}else {
			ladoMasCorto = this.profundidad;
		}
		return ladoMasCorto;
	}

	//SETTERS SIMPLES -----------------------------------------------------------------------------------------
}

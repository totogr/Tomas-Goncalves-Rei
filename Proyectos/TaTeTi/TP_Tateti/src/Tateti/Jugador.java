package Tateti;

import Cartas.Carta;
import Estructuras.ListaSimplementeEnlazada;

public class Jugador {
	
	//ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
	
	//ATRIBUTOS -----------------------------------------------------------------------------------------------
	private ListaSimplementeEnlazada<Carta> cartas = null;
	private ListaSimplementeEnlazada<Ficha> fichas = null;
	private ColorRGB colorAsociado = null; 
	private String nombre = null;
	private int numeroDeJugador = 0;

	//CONSTRUCTORES---------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * pre: -
	 * post: se crea un jugador con un color asociado, su cantidad de fichas y su nombre
	 * @param colorAsociado: color del jugador
	 * @param cantidadDeFichas: cantidad de fichas del jugador
	 * @param nombre: nombre del jugador
	 * @throws Exception: valida los parametros introducidos
	 */
	public Jugador (ColorRGB colorAsociado, int cantidadDeFichas, String nombre, int numeroDeJugador) throws Exception {
		if(!Validacion.numeroMayorACero(cantidadDeFichas)) {
			throw new Exception("La cantidad de fichas debe ser mayor a 0");
		}
		if(!Validacion.validarColor(colorAsociado)) {
			throw new Exception("El color no puede ser nulo");
		}
		if(!Validacion.validarString(nombre)) {
			throw new Exception("El nombre no puede ser nulo");
		}
		this.cartas = new ListaSimplementeEnlazada<Carta>();
		this.colorAsociado = colorAsociado;
		this.nombre = nombre;
		this.numeroDeJugador = numeroDeJugador;
		this.fichas = new ListaSimplementeEnlazada<Ficha>();
		for(int i = 0; i < cantidadDeFichas; i++) {
			this.fichas.agregar(new Ficha(colorAsociado));
		}
	}

	//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

	/**
	 * pre: -
	 * post: agrega fichas al jugador
	 * @param cantidadDeFichas: cantidad de fichas a agregar
	 * @throws Exception: valida que la cantidad de fichas sea mayor a 0
	 */
	public void agregarFichas(int cantidadDeFichas) throws Exception {
		if(!Validacion.numeroMayorACero(cantidadDeFichas)) {
			throw new Exception("La cantidad de fichas debe ser mayor a 0");
		}
		for(int i = 0; i < cantidadDeFichas; i++) {
			this.fichas.agregar(new Ficha(this.colorAsociado));
		}
	}

	/**
	 * pre: -
	 * post: quita la ficha de la lista de fichas del jugador
	 * @throws Exception
	 */
	public void quitarFicha(Ficha ficha) throws Exception {
		if(!tieneFichas()) {
			throw new Exception("El jugador no tiene mas fichas");
		}
		if(ficha == null) {
			throw new Exception("La ficha no puede ser nula");
		}
		fichas.remover(ficha);
	}	

	/**
	 * pre:-
	 * @return: devuelve verdadero si tiene fichas
	 */
	public boolean tieneFichas() {
		if(this.fichas == null ||
				this.fichas.getTamanio() == 0) {
			return false;
		}
		return true;
	}

	/**
	 * pre: -
	 * @return: devuelve si el jugador tiene fichas disponibles en mano
	 */
	public boolean tieneFichasEnMano() {
		if(this.fichas == null ||
				(cantidadDeFichasEnMano() <= 0)) {
			return false;
		}
		return true;
	}

	/**
	 * pre:-
	 * post: agrega una carta a la lista de cartas del jugador
	 * @throws Exception lanza una exepcion si la carta es vacia
	 */
	public void agregarCarta(Carta carta)throws Exception {
		if(carta == null) {
			throw new Exception("La carta no puede ser vacia");
		}
		this.cartas.agregar(carta);
	}

	/**
	 * pre: -
	 * @return: devuelve si el jugador tiene todas sus fichas bloqueadas
	 */
	public boolean tieneTodasLasFichasBloqueadas() {
		this.fichas.iniciarCursor();
		while(fichas.avanzarCursor()) {
			Ficha ficha = fichas.obtenerCursor();
			if(!ficha.estaBloqueado()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * pre: -
	 * @return: devuelve si el jugador tiene todas sus fichas en el tablero
	 */
	public boolean tieneTodasLasFichasEnElTablero(){
		return cantidadDeFichasEnTablero() == getCantidadDeFichas();
	}

	/**
	 * pre: -
	 * @return: devuelve la cantidad de fichas que tiene el jugador en el tablero
	 */
	public int cantidadDeFichasEnTablero() {
		int fichasEnTablero = 0;
		this.fichas.iniciarCursor();
		while(this.fichas.avanzarCursor()) {
			Ficha fichaActual = this.fichas.obtenerCursor();
			if(fichaActual.estaEnTablero()) {
				fichasEnTablero ++;
			}
		}
		return fichasEnTablero;
	}

	/**
	 * pre: -
	 * @return: devuelve la cantidad de fichas que tiene el jugador para poner en el tablero
	 */
	public int cantidadDeFichasEnMano() {
		return (getCantidadDeFichas() - cantidadDeFichasEnTablero());
	}
	
	//GETTERS SIMPLES-------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * pre: -
	 * @return: devuelve la lista de cartas del jugador
	 */
	public ListaSimplementeEnlazada<Carta> getCartas () {
		return this.cartas;
	}

	/**
	 * pre: -
	 * @return: devuelve la lista de fichas del jugador
	 */
	public ListaSimplementeEnlazada<Ficha> getFichas () {
		return this.fichas;
	}

	/**
	 * pre: -
	 * @return: devuelve el color del jugador
	 */
	public ColorRGB getColor () {
		return this.colorAsociado;
	}

	/**
	 * pre: -
	 * @return: devuelve el nombre del jugador
	 */
	public String getNombre() {
		return this.nombre;
	}

	/**
	 * pre: -
	 * @return: devuelve el numero de jugador
	 */
	public int getNumeroDejugador() {
		return this.numeroDeJugador;
	}

	/**
	 * pre: -
	 * @return: devuelve la cantidad de fichas que dispone el jugador
	 */
	public int getCantidadDeFichas () {
		return this.fichas.getTamanio();
	}

	/**
	 * pre: -
	 * @return: devuelve la cantidad de cartas del jugador
	 */
	public int getCantidadDeCartas() {
		return this.cartas.getTamanio();
	}

	//SETTERS SIMPLES-------------------------------------------------------------------------------------------------------------------------------------------	
}

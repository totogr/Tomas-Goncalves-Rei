package Tateti;
import java.util.Random;

import Cartas.Carta;
import Cartas.TiposDeCartas.CartaAnularCasillero;
import Cartas.TiposDeCartas.CartaBloquearFicha;
import Cartas.TiposDeCartas.CartaCambiarColorDeFicha;
import Cartas.TiposDeCartas.CartaDesbloquearFicha;
import Cartas.TiposDeCartas.CartaPierdeTurno;
import Cartas.TiposDeCartas.CartaRepetirTurno;
import Cartas.TiposDeCartas.CartaVolverAtrasJugada;
import Estructuras.Pila;

public class Mazo<T> {

	//ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------

	private final int CANTIDAD_DE_CARTAS_MAXIMA = 50;
	private final int CANTIDAD_DE_CARTAS_DISTINTAS = 7;

	//ATRIBUTOS -----------------------------------------------------------------------------------------------

	private Pila<Carta> mazo = null;
	private int cantidadDeCartas = 0;

	//CONSTRUCTORES -------------------------------------------------------------------------------------------

	/**
	 * pre: -
	 * post: crea un mazo de cartas
	 * @param mazo
	 * @throws Exception
	 */
	Mazo(Pila<Carta> mazo) throws Exception{
		if(mazo.contarElementos()<=CANTIDAD_DE_CARTAS_MAXIMA) {
			this.mazo=mazo;
			this.cantidadDeCartas=mazo.contarElementos();
		}
		throw new Exception("Tamaño de pila superior a la cantidad maxima de cartas");
	}

	/** 
	 * pre: -
	 * post: crea la pila de cartas y las agrega al mazo
	 * @throws Exception
	 */
	Mazo() throws Exception{
		Random random = new Random();
		Pila<Carta> pilaDeCarta= new Pila<>();
		for(int i = 0; i<CANTIDAD_DE_CARTAS_MAXIMA ; i++) {
			int j = random.nextInt(this.CANTIDAD_DE_CARTAS_DISTINTAS)+1;
			pilaDeCarta.apilar(crearCartasPorId(j));
		}
		this.cantidadDeCartas=CANTIDAD_DE_CARTAS_MAXIMA;
		this.mazo=pilaDeCarta;
	}

	//METODOS DE CLASE ----------------------------------------------------------------------------------------
	//METODOS GENERALES ---------------------------------------------------------------------------------------
	//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

	/**
	 * pre: -
	 * @return: saca una carta del mazo
	 * @throws Exception
	 */
	public Carta sacarCarta() throws Exception{
		if(this.cantidadDeCartas == 0) {
			mezclarMazo();
			cantidadDeCartas--;
			return this.mazo.desapilar();
		}else {
			cantidadDeCartas--;
			return this.mazo.desapilar();
		}
	}

	/**
	 * pre: -
	 * post: recibe un tipo de carta y la agregar al mazo ademas de aumentar en 1 la cantidad de cartas en mazo
	 * @param carta: carta a agregar al mazo
	 * @throws Exception: valida el tamaño del mazo
	 */
	void dejarUnaCarta(Carta carta) throws Exception{
		if(mazo.contarElementos() < CANTIDAD_DE_CARTAS_MAXIMA) {
			cantidadDeCartas++;
			this.mazo.apilar(carta);
		}else {
			throw new Exception("Cantidad de cartas maxima");
		}
	}

	/**
	 * pre: -
	 * @return: devuelve true si el mazo se encuentra vacio
	 */
	public boolean estaVacio() {
		return (this.cantidadDeCartas == 0);	
	}

	/**
	 * pre: -
	 * post: crea de manera aleatoria el mazo con la cantidad maxima de cartas y las agrega al mazo
	 * @throws Exception: valida que el mazo este vacio para mezclarlo
	 */
	public void mezclarMazo() throws Exception {
		if(estaVacio()) {
			Pila<Carta> pilaDeCarta = new Pila<>();
			Random random = new Random();
			for(int i = 0; i < CANTIDAD_DE_CARTAS_MAXIMA ; i++) {				
				int j = random.nextInt(this.CANTIDAD_DE_CARTAS_DISTINTAS) + 1;
				pilaDeCarta.apilar(crearCartasPorId(j));
			}
			this.mazo = pilaDeCarta;
			this.cantidadDeCartas = CANTIDAD_DE_CARTAS_MAXIMA;
		}
		throw new Exception("El mazo debe estar vacio");
	}

	/**
	 * pre: -
	 * post: crea cartas por numero de id y las agrega al mazo
	 * @param numeroDeCarta: numero de carta a crear
	 */
	private Carta crearCartasPorId(int numeroDeCarta) {
		switch(numeroDeCarta) {
		case 1: Carta carta1 = new CartaAnularCasillero(); return carta1;
		case 2:	Carta carta2 = new CartaBloquearFicha(); return carta2;
		case 3:	Carta carta3 = new CartaCambiarColorDeFicha(); return carta3;
		case 4:	Carta carta4 = new CartaDesbloquearFicha(); return carta4;
		case 5:	Carta carta5 = new CartaPierdeTurno(); return carta5;
		case 6:	Carta carta6 = new CartaRepetirTurno();	return carta6;
		case 7:	Carta carta7 = new CartaVolverAtrasJugada(); return carta7;
		}
		return null;		
	}

	//GETTERS SIMPLES -----------------------------------------------------------------------------------------
	
	/**
	 * pre: -
	 * @return: devuelve la cantidad de cartas que posee el mazo
	 */
	public int getCantidadDeCartas() {
		return this.cantidadDeCartas;
	}

	/**
	 * pre: -
	 * @return: devuelve la cantidad de cartas maxima
	 */
	public int getCANTIDAD_DE_CARTAS_MAXIMA() {
		return this.CANTIDAD_DE_CARTAS_MAXIMA;
	}

	/**
	 * pre: -
	 * @return: devuelve la cantidad de cartas distintas en el mazo
	 */
	public int getCANTIDAD_DE_CARTAS_DISTINTAS() {
		return this.CANTIDAD_DE_CARTAS_DISTINTAS;
	}

	/**
	 * pre: -
	 * @return: devuelve el mazo
	 */
	public Pila<Carta> getMazo() {
		return this.mazo;
	}
	
	//SETTERS SIMPLES -----------------------------------------------------------------------------------------	
}

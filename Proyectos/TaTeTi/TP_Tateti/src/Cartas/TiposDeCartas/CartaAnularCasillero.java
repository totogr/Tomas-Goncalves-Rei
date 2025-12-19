package Cartas.TiposDeCartas;

import Cartas.Carta;
import Jugadas.Jugada;
import Jugadas.JugadaAnularCasillero;

public class CartaAnularCasillero extends Carta {
	//ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
	//ATRIBUTOS -----------------------------------------------------------------------------------------------
	//CONSTRUCTORES -------------------------------------------------------------------------------------------

	/**
	 * pre: -
	 * post: crea la carta de anular casillero
	 */
	public CartaAnularCasillero() {}

	//METODOS DE CLASE ----------------------------------------------------------------------------------------
	//METODOS GENERALES ---------------------------------------------------------------------------------------
	//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

	@Override
	protected String getTituloPorDefecto() {
		return "Anula Casillero";
	}

	@Override
	public Jugada getJugada() {
		return new JugadaAnularCasillero(this);
	}

	//GETTERS SIMPLES -----------------------------------------------------------------------------------------
	//SETTERS SIMPLES -----------------------------------------------------------------------------------------	
}

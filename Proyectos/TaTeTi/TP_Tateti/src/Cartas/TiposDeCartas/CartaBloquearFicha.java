package Cartas.TiposDeCartas;

import Cartas.Carta;
import Jugadas.Jugada;
import Jugadas.JugadaBloquearFicha;

public class CartaBloquearFicha extends Carta {
	//ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
	//ATRIBUTOS -----------------------------------------------------------------------------------------------
	//CONSTRUCTORES -------------------------------------------------------------------------------------------

	/**
	 * pre: -
	 * post: crea la carta de bloquear ficha
	 */
	public CartaBloquearFicha() {}

	//METODOS DE CLASE ----------------------------------------------------------------------------------------
	//METODOS GENERALES ---------------------------------------------------------------------------------------
	//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

	@Override
	protected String getTituloPorDefecto() {
		return "Bloquea Ficha";
	}

	@Override
	public Jugada getJugada() {
		return new JugadaBloquearFicha(this);
	}

	//GETTERS SIMPLES -----------------------------------------------------------------------------------------
	//SETTERS SIMPLES -----------------------------------------------------------------------------------------	
}

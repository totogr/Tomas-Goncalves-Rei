package Cartas.TiposDeCartas;

import Cartas.Carta;
import Jugadas.Jugada;
import Jugadas.JugadaRepetirTurno;

public class CartaRepetirTurno extends Carta {
	//ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
	//ATRIBUTOS -----------------------------------------------------------------------------------------------
	//CONSTRUCTORES -------------------------------------------------------------------------------------------

	/**
	 * pre: -
	 * post: crea la carta de repetir el turno 
	 */
	public CartaRepetirTurno(){}

	//METODOS DE CLASE ----------------------------------------------------------------------------------------
	//METODOS GENERALES ---------------------------------------------------------------------------------------
	//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

	@Override
	protected String getTituloPorDefecto() {
		return "Repetir Turno";
	}

	@Override
	public Jugada getJugada() {
		return new JugadaRepetirTurno(this);
	}

	//GETTERS SIMPLES -----------------------------------------------------------------------------------------
	//SETTERS SIMPLES -----------------------------------------------------------------------------------------	
}

package Cartas.TiposDeCartas;

import Cartas.Carta;
import Jugadas.Jugada;
import Jugadas.JugadaVolverAtrasJugada;

public class CartaVolverAtrasJugada extends Carta {
	//ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
	//ATRIBUTOS -----------------------------------------------------------------------------------------------
	//CONSTRUCTORES -------------------------------------------------------------------------------------------

	/**
	 * pre: -
	 * post: crea la carta de volver atras la jugada 
	 */
	public CartaVolverAtrasJugada() {}

	//METODOS DE CLASE ----------------------------------------------------------------------------------------
	//METODOS GENERALES ---------------------------------------------------------------------------------------
	//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

	@Override
	protected String getTituloPorDefecto() {
		return "Volver Atras Jugada";
	}

	@Override
	public Jugada getJugada() {
		return new JugadaVolverAtrasJugada(this);
	}

	//GETTERS SIMPLES -----------------------------------------------------------------------------------------
	//SETTERS SIMPLES -----------------------------------------------------------------------------------------	
}

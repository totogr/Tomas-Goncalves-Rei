package Cartas.TiposDeCartas;

import Cartas.Carta;
import Jugadas.Jugada;
import Jugadas.JugadaCambiarColorDeFicha;

public class CartaCambiarColorDeFicha extends Carta {
	//ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
	//ATRIBUTOS -----------------------------------------------------------------------------------------------
	//CONSTRUCTORES -------------------------------------------------------------------------------------------

	/**
	 * pre: -
	 * post: crea la carta de cambiar color de ficha
	 */
	public CartaCambiarColorDeFicha() {}

	//METODOS DE CLASE ----------------------------------------------------------------------------------------
	//METODOS GENERALES ---------------------------------------------------------------------------------------
	//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

	@Override
	protected String getTituloPorDefecto() {
		return "Cambia Color De Ficha";
	}

	@Override
	public Jugada getJugada() {
		return new JugadaCambiarColorDeFicha(this);
	}

	//GETTERS SIMPLES -----------------------------------------------------------------------------------------
	//SETTERS SIMPLES -----------------------------------------------------------------------------------------	
}
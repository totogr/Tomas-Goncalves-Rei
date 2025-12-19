package Jugadas;

import Cartas.Carta;
import Estructuras.ListaCircularSimplementeEnlazada;
import Tateti.Casillero;
import Tateti.Ficha;
import Tateti.Tateti;
import Tateti.Turno;

public abstract class Jugada {
	
//ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
//ATRIBUTOS -----------------------------------------------------------------------------------------------

	private Carta carta = null;

//CONSTRUCTORES -------------------------------------------------------------------------------------------

	/**
	 * pre: -
	 * post: se crea la jugada a traves de la carta
	 * @param carta: carta que indica la jugada
	 */
	public Jugada(Carta carta) {
		this.carta = carta;
	}

//METODOS DE CLASE ----------------------------------------------------------------------------------------
//METODOS GENERALES ---------------------------------------------------------------------------------------
//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

	/**
	 * pre: -
	 * post: acciona la jugada de la carta
	 */
	public abstract void jugar(Tateti tateti, 
								Turno turnoActual,
								ListaCircularSimplementeEnlazada<Turno> turnos,
								Casillero<Ficha> casilleroOrigen,
								Casillero<Ficha> casilleroDesitno) throws Exception;

//GETTERS SIMPLES -----------------------------------------------------------------------------------------

	/**
	 * pre: -
	 * @return: devuelve la carta que tiene la jugada
	 */
	public Carta getCarta() {
		return carta;
	}

//SETTERS SIMPLES -----------------------------------------------------------------------------------------	
}

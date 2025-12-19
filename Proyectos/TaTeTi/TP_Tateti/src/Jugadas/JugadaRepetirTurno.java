package Jugadas;

import Cartas.Carta;
import Estructuras.ListaCircularSimplementeEnlazada;
import Tateti.Casillero;
import Tateti.Ficha;
import Tateti.Tateti;
import Tateti.Turno;

public class JugadaRepetirTurno extends Jugada{
	
//CONSTRUCTORES -------------------------------------------------------------------------------------------

	public JugadaRepetirTurno(Carta carta) {
		super(carta);
	}
	
//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

	/**
	 * pre:-
	 * post: agrega un subturno al usuario
	 * @throws Exceptions lanza una exepcion si alguno de los parametros es vacio
	 */
	public void jugar(Tateti tateti,
			Turno turno,
			ListaCircularSimplementeEnlazada<Turno> turnos,
			Casillero<Ficha> casilleroOrigen,
			Casillero<Ficha> casilleroDestino) throws Exception {
		if(tateti == null) {
			throw new Exception("El tateti no puede ser vacio");
		}
		if(turno == null) {
			throw new Exception("El turno no puede ser vacio");
		}
		if(turnos == null) {
			throw new Exception("Los turnos no pueden ser vacios");
		}
		turno.agregarSubturno();
	}
}

package Jugadas;

import Cartas.Carta;
import Estructuras.ListaCircularSimplementeEnlazada;
import Tateti.Casillero;
import Tateti.Ficha;
import Tateti.Tateti;
import Tateti.Turno;

public class JugadaVolverAtrasJugada extends Jugada{
	
//CONSTRUCTORES -------------------------------------------------------------------------------------------
	
	public JugadaVolverAtrasJugada(Carta carta) {
		super(carta);
	}
	
//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

	/**
	 * pre: -
	 * post: realiza la jugada que elimina la jugada anterior realizada y pasa al siguiente turno
	 */
	public void jugar(Tateti tateti,
			Turno turnoActual,
			ListaCircularSimplementeEnlazada<Turno> turnos,
			Casillero<Ficha> casilleroOrigen,
			Casillero<Ficha> casilleroDestino)throws Exception {
		boolean seMovioFicha = ((casilleroOrigen != null) && (casilleroDestino != null));
		boolean sePusoFicha = (casilleroOrigen == null);
		if(seMovioFicha) {
			System.out.println("La ficha ha vuelto a su casillero anterior");
			Ficha ficha = casilleroDestino.getDato();
			casilleroDestino.desocupar();
			casilleroOrigen.setDato(ficha);		
			return;
		}
		if(sePusoFicha) {
			Ficha ficha = casilleroDestino.getDato();
			casilleroDestino.desocupar();
			ficha.ubicarEnJugador();
			System.out.println("La ficha puesta salio del tablero y volvio al jugador");
			return;
		}
		System.out.println("La ficha permanece en el mismo lugar");
	}
}

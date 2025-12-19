package Jugadas;

import Cartas.Carta;
import Estructuras.ListaCircularSimplementeEnlazada;
import Estructuras.ListaSimplementeEnlazada;
import InterfazDeUsuario.InterfazDeJugada;
import Tateti.Casillero;
import Tateti.Ficha;
import Tateti.Tateti;
import Tateti.Turno;

public class JugadaDesbloquearFicha extends Jugada {
	
//CONSTRUCTORES -------------------------------------------------------------------------------------------

	public JugadaDesbloquearFicha(Carta carta) {
		super(carta);
	}

//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

	/**
	 * pre: -
	 * post: realiza la jugada que desbloquea una ficha propia del jugador
	 */
	public void jugar(Tateti tateti, 
			Turno turnoActual, 
			ListaCircularSimplementeEnlazada<Turno> turnos,
			Casillero<Ficha> casilleroOrigen,
			Casillero<Ficha> casilleroDestino)throws Exception {
		ListaSimplementeEnlazada<Ficha> fichasBloqueadas = new ListaSimplementeEnlazada<Ficha>();
		ListaSimplementeEnlazada<Ficha> fichas=turnoActual.getJugador().getFichas();
		fichas.iniciarCursor();
		while(fichas.avanzarCursor()) {
			if(fichas.obtenerCursor().estaBloqueado()) {
				fichasBloqueadas.agregar(fichas.obtenerCursor());
			}
		}
		if(fichasBloqueadas.getTamanio() > 0) {
			Ficha ficha = InterfazDeJugada.elegirUnaFichaDeJugadorEnElTablero(tateti.getTablero(), fichasBloqueadas);
			ficha.habilitarFicha();
			System.out.println("La ficha en el "+ tateti.getTablero().getCasillero(ficha)+" ahora esta habilitada.");
		}else {
			System.out.println("No hay ninguna ficha bloqueada.");
		}
	}
}

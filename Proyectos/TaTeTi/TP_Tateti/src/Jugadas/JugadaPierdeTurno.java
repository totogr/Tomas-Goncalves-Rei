package Jugadas;

import Cartas.Carta;
import Estructuras.ListaCircularSimplementeEnlazada;
import InterfazDeUsuario.InterfazDeJugada;
import Tateti.Casillero;
import Tateti.Ficha;
import Tateti.Jugador;
import Tateti.Tateti;
import Tateti.Turno;

public class JugadaPierdeTurno extends Jugada {

//CONSTRUCTORES -------------------------------------------------------------------------------------------

	public JugadaPierdeTurno(Carta carta) {
		super(carta);
	}
	
//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

	/**
	 * pre: -
	 * post: realiza la jugada para hacer perder el turno a un jugador elegido por el jugador del turno actual
	 */
	public void jugar(Tateti tateti, 
						Turno turnoActual, 
						ListaCircularSimplementeEnlazada<Turno> turnos,
						Casillero<Ficha> casilleroOrigen,
						Casillero<Ficha> casilleroDestino)throws Exception {
		if (tateti == null){
		    throw new Exception ("El tateti no puede ser nulo");
		}
		if(turnoActual == null){
			throw new Exception ("El jugador acutal no puede ser nulo");
		}
		if(turnos == null){
			throw new Exception ("La lista de turnos no puede estar vacía");
		}
		boolean bloqueoValido = false;
		while(!bloqueoValido) {
			try {
				Jugador jugadorBloqueado = InterfazDeJugada.elegirUnJugador(tateti.getJugadores());
				if(jugadorBloqueado.equals(turnoActual.getJugador())) {
					throw new Exception("No te puedes bloquear a ti mismo!");
				}
				boolean encontrado = false;
				turnos.iniciarCursor();
				while(!encontrado && turnos.avanzarCursor()){
					Turno turnoLeido = turnos.obtenerCursor();
					if(turnoLeido.getJugador().equals(jugadorBloqueado)){
						encontrado = true;
						turnoLeido.incrementarBloqueosRestantes(1);
					}
				}
				boolean jugadorDeTurnoEncontrado = false;
				while(!jugadorDeTurnoEncontrado && turnos.avanzarCursor()) {
					if(turnos.obtenerCursor().equals(turnoActual)) {
						jugadorDeTurnoEncontrado=true;
					}
				}
				bloqueoValido = true;
				System.out.println("El jugador " + turnoActual.getJugador().getNombre() + 
						" bloqueó el siguiente turno del jugador "+ jugadorBloqueado.getNombre());
			} catch (Exception e) {
				System.out.print(e.getMessage());
			}
		}
	}	
}

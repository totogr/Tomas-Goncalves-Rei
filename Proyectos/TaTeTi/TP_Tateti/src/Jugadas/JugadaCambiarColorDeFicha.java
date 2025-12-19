package Jugadas;

import Cartas.Carta;
import Estructuras.ListaCircularSimplementeEnlazada;
import Estructuras.ListaSimplementeEnlazada;
import InterfazDeUsuario.InterfazDeJugada;
import Tateti.Casillero;
import Tateti.Ficha;
import Tateti.Jugador;
import Tateti.Tateti;
import Tateti.Turno;
import Tateti.Validacion;

public class JugadaCambiarColorDeFicha extends Jugada {
	
//CONSTRUCTORES -------------------------------------------------------------------------------------------

	public JugadaCambiarColorDeFicha(Carta carta) {
		super(carta);
	}
	
	
	/**
	 * pre:-
	 * post: pregunta al usuario el jugador al que se le desea cambiar el color de la ficha, luego 
	 * 		le pregunta cual de sus fichas desea cambiar el color , le cambia el color y le agrega una ficha disponible para poner en el tablero al jugador que le cambiaron el color de la ficha
	 * 		Si no hay ningun jugador con fichas en el tablero además del jugador al que le toca, entonces imprime un mensaje 
	 * 		de que no hay ningun jugador con fichas en el tablero y termina la jugada
	 * @throws Exceptions: lanza una exepcion si alguno de los parametros es vacio
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
		boolean hayJugadoresConFichasEnTablero = false;
		ListaSimplementeEnlazada<Jugador> jugadores = tateti.getJugadores();
		jugadores.iniciarCursor();
		while(jugadores.avanzarCursor() && !hayJugadoresConFichasEnTablero) {
			if(!(jugadores.obtenerCursor().equals(turno.getJugador()))){
				if(jugadores.obtenerCursor().cantidadDeFichasEnTablero() > 0) {
					hayJugadoresConFichasEnTablero = true;
				}
			}
		}
		if(hayJugadoresConFichasEnTablero == false) {
			System.out.println("No hay ningun jugador con alguna ficha en el tablero al que se le pueda cambiar de color alguna de sus fichas");
			return;
		}
		boolean cambioDeColorValido = false;
		while(!cambioDeColorValido) {
			try {
				Jugador jugador = InterfazDeJugada.elegirUnJugador(tateti.getJugadores());
				Validacion.validarJugador(jugador, turno);
				Ficha ficha = InterfazDeJugada.elegirUnaFichaDeJugadorEnElTablero(tateti.getTablero(),jugador.getFichas());
				ficha.cambiarColor(turno.getJugador().getColor());
				turno.getJugador().getFichas().agregar(ficha);
				jugador.quitarFicha(ficha);;
				jugador.agregarFichas(1);
				cambioDeColorValido = true;
				System.out.println("La ficha del jugador " + jugador.getNombre() + " que estaba en la posicion " + tateti.getTablero().getCasillero(ficha)
						+ " ahora es del jugador " + turno.getJugador().getNombre());
			} catch (Exception e) {
				System.out.print(e.getMessage());
			}
		}				
	}
}

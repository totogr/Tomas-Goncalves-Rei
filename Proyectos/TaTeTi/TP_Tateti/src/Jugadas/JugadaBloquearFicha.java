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

public class JugadaBloquearFicha extends Jugada {

//CONSTRUCTORES -------------------------------------------------------------------------------------------

	public JugadaBloquearFicha(Carta carta) {
		super(carta);
	}
	
//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

	/**
	 * pre: -
	 * postt: realiza la jugada para bloquear la ficha elegida por el jugador si es que hay fichas en el tablero
	 * 		disponibles para bloquear, en caso afirmativo le pregunta al usuario las coordenadas de la ficha a bloquear 
	 * 		y luego lo bloquea si este no esta bloqueado ya, si esta bloqueado debe elegir otras coordenadas de otra ficha
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
		boolean hayFichaParaBloquear = false;
		ListaSimplementeEnlazada<Jugador> jugadores = tateti.getJugadores();
		jugadores.iniciarCursor();
		while(jugadores.avanzarCursor()) {
			Jugador jugador = jugadores.obtenerCursor();
			if(!jugador.equals(turnoActual.getJugador())) {
				if(jugador.cantidadDeFichasEnTablero()>0 && !jugador.tieneTodasLasFichasBloqueadas()) {
					hayFichaParaBloquear=true;
				}
			}
		}
		Casillero<Ficha> casilleroDeFichaABloquear = null;
		if(hayFichaParaBloquear) {
			boolean fichaBloqueada = false;
			while(!fichaBloqueada) {
				try {
					casilleroDeFichaABloquear=InterfazDeJugada.preguntarFichaABloquear(tateti.getTablero());
					if(casilleroDeFichaABloquear.getDato()!=null) {
						casilleroDeFichaABloquear.getDato().bloquearFicha();
						tateti.getJugadores().iniciarCursor();
						Jugador jugadorDeLaFichaABloquear = null;
						while(jugadorDeLaFichaABloquear == null && tateti.getJugadores().avanzarCursor()) {
							if(tateti.getJugadores().obtenerCursor().getColor().esIgual(casilleroDeFichaABloquear.getDato().getColorDeFicha())) {
								jugadorDeLaFichaABloquear=tateti.getJugadores().obtenerCursor();
							}
						}
						System.out.println("Se bloqueo la ficha de "+jugadorDeLaFichaABloquear.getNombre()+" en el " + casilleroDeFichaABloquear );
						fichaBloqueada = true;
						
					}else {
						System.out.println("El casillero no contiene ninguna ficha para bloquear");
					}
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		}else {
			System.out.println("No hay fichas disponibles para bloquear!");
		}
	}

}
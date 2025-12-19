package Tateti;

import Estructuras.ListaSimplementeEnlazada;
import InterfazDeUsuario.IngresoPorTeclado;
import InterfazDeUsuario.InterfazPrincipal;

public class Main {

	public static void main(String[] args) throws Exception {
		IngresoPorTeclado.inicializar();
		InterfazPrincipal.comenzarElJuego();
		Tateti tateti = crearTateti();
		InterfazPrincipal.ingresarCantidadDeFichas(tateti.getCantidadDeFichasAlineadasParaGanar());
		while(!tateti.juegoTerminado()) {
			tateti.jugar();
			if(tateti.juegoTerminado()) {
				System.out.println("El ganador es " + tateti.getGanador());
			}
		}
		IngresoPorTeclado.finalizar();
	}

	/**
	 * pre: -
	 * @return: devuelve el tateti ya creado
	 */
	public static Tateti crearTateti() {
		boolean tatetiValido = false;
		Tateti tateti = null;
		while(!tatetiValido) {
			int cantidadDeJugadores = InterfazPrincipal.ingresarCantidadDeJugadores();
			int[] tamañoDelTablero = InterfazPrincipal.ingresarTamañoDelTablero();
			try {
				Tablero<Ficha> tablero = new Tablero<Ficha>(tamañoDelTablero[0], tamañoDelTablero[1], tamañoDelTablero[2]);		
				ListaSimplementeEnlazada<Jugador> jugadores = crearJugadores(cantidadDeJugadores, tablero.obtenerLadoMasCorto());		
				tateti = new Tateti(jugadores, tablero);
				tatetiValido = true;
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return tateti;
	}

	/**
	 * pre: -
	 * @param cantidadDeJugadores: cantidad de jugadores de la partida
	 * @return: se asgina a cada jugador un color y su cantidad de fichas
	 * @throws Exception: valida la creacion de jugadores en la lista
	 */
	private static ListaSimplementeEnlazada<Jugador> crearJugadores(int cantidadDeJugadores, int fichasPorJugador) throws Exception {
		ListaSimplementeEnlazada<Jugador> jugadores = new ListaSimplementeEnlazada<Jugador>();
		InterfazPrincipal interfazPrincipal = new InterfazPrincipal();  
		for (int i = 1; i <= cantidadDeJugadores; i++) {
			ColorRGB colorDeJugador = interfazPrincipal.ingresarColorDeJugador(i);
			String nombreDeJugador = InterfazPrincipal.ingresarNombreDeJugador(i);
			jugadores.agregar(new Jugador(colorDeJugador, fichasPorJugador, nombreDeJugador, i));
		}
		return jugadores;
	}
}


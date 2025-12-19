package InterfazDeUsuario;

import Estructuras.ListaSimplementeEnlazada;
import Tateti.Casillero;
import Tateti.Ficha;
import Tateti.Jugador;
import Tateti.Tablero;

public class InterfazDeJugada {
	
	/**
	 * pre:-
	 * pos:pregunta al usuario las coordenadas en x y z , para buscar el casillero a anular, si las coordenadas no son validas vuelve a preguntar
	 * @return: devuelve el casillero del tablero a anular
	 * @throws Exception: lanza una exepcion si el tablero es vacio
	 */
	public static Casillero<Ficha> preguntarCasilleroAAnular(Tablero<Ficha> tablero) throws Exception {
		if(tablero == null) {
			throw new Exception("El tablero no puede ser vacio");
		}
		int x=0;
		int y=0;
		int z=0;
		Casillero<Ficha> casillero = null;
		while(casillero == null) {
			try {
				System.out.println("---Elegir casillero a bloquear-----\n");
				System.out.println("Ingrese la posicion en X del casillero [1, "+ tablero.getAncho()+"]: " );
				x = IngresoPorTeclado.leerEntero();
				System.out.println("Ingrese la posicion en Y del casillero [1, "+ tablero.getAlto()+"]: " );
				y = IngresoPorTeclado.leerEntero();
				System.out.println("Ingrese la posicion en Z del casillero [1, "+ tablero.getProfundidad()+"]: " );
				z = IngresoPorTeclado.leerEntero();
				casillero = tablero.getCasillero(x, y, z);				
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return casillero;
	}
	
	/**
	 * pre: -
	 * pos: pregunta las posiciones en x y z de la ficha que se desea bloquear, si estas posiciones no son validas vuelve a preguntar
	 * @return: devuelve el casillero de la ficha a bloquear
	 * @throws Exception: valida el tablero
	 */
	public static Casillero<Ficha> preguntarFichaABloquear(Tablero<Ficha> tablero) throws Exception {
		if(tablero == null) {
			throw new Exception("El tablero no puede ser vacio");
		}
		int x=0;
		int y=0;
		int z=0;
		Casillero<Ficha> casillero = null;
		while(casillero==null) {
			try {
				System.out.println("---Elegir ficha a bloquear-----\n");
				System.out.println("Ingrese la posicion en X de la ficha a bloquear [1, "+ tablero.getAncho()+"]: " );
				x = IngresoPorTeclado.leerEntero();
				System.out.println("Ingrese la posicion en Y de la ficha a bloquear [1, "+ tablero.getAlto()+"]: " );
				y = IngresoPorTeclado.leerEntero();
				System.out.println("Ingrese la posicion en Z de la ficha a bloquear [1, "+ tablero.getProfundidad()+"]: " );
				z = IngresoPorTeclado.leerEntero();
				casillero=tablero.getCasillero(x, y, z);				
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return casillero;
	}
	
	/**
	 * pre:-
	 * pos:le muestra al usuario los jugadores acompañado de un indice, y el jugador debe elegir uno de ellos elegiendo el indice correspondiente
	 * @return: devuelve el jugador elegido por el usuario
	 * @throws Exception: lanza una exepcion si los jugadores son vacio
	 */
	public static Jugador elegirUnJugador(ListaSimplementeEnlazada<Jugador> jugadores) throws Exception {
		if(jugadores == null) {
			throw new Exception("Los jugadores no pueden ser vacio");
		}
		Jugador jugador = null;
		while(jugador == null) {
			int i = 1;
			System.out.println("\nIngrese el numero correspondiente al jugador que desea elegir :");
			jugadores.iniciarCursor();
			while(jugadores.avanzarCursor()) {
				System.out.println("("+i+") " + jugadores.obtenerCursor().getNombre());
				i++;
			}
			int numeroElegido = IngresoPorTeclado.leerEntero();
			try {
				jugador = jugadores.obtener(numeroElegido);
			}catch(Exception e) {
				System.out.println("No es una opcion valida");
			}
		}
		return jugador;
	}
	
	/**
	 * pre:debe haber por lo menos una ficha de la lista de fichas que este en el tablero
	 * pos: muestra al usuario las fichas con un indice asociado, de la lista de fichas, que estan en el tablero para que elija una de ellas con el indice correspondiente
	 * @return: devuelve una de las fichas de la lista de fichas, y dicha ficha esta en el tablero
	 * @throws Exception: lanza una exepcion si los jugadores o el tablero son vacios
	 */
	public static Ficha elegirUnaFichaDeJugadorEnElTablero(Tablero<Ficha> tablero, ListaSimplementeEnlazada<Ficha> fichas) throws Exception {
		if(tablero == null) {
			throw new Exception("El tableor no puede ser vacio");
		}
		if(fichas == null) {
			throw new Exception("Las fichas no puede ser vacio");
		}
		ListaSimplementeEnlazada<Ficha> fichasEnTablero = new ListaSimplementeEnlazada<Ficha>();
		fichas.iniciarCursor();
		while(fichas.avanzarCursor()) {
			if(fichas.obtenerCursor().estaEnTablero()) {
				fichasEnTablero.agregar(fichas.obtenerCursor());
			}
		}
		Ficha ficha = null;
		boolean fichaValida = false;
		while(!fichaValida) {
			try {
				int i = 1;
				System.out.println("\nIngrese el numero correspondiente a la ficha que desea elegir :");
				fichasEnTablero.iniciarCursor();
				while(fichasEnTablero.avanzarCursor()) {
					System.out.println("("+i+")"+ "Ficha en " + tablero.getCasillero(fichasEnTablero.obtenerCursor()));
					i++;
				}
				int numeroElegido = IngresoPorTeclado.leerEntero();
				ficha = fichasEnTablero.obtener(numeroElegido);
				fichaValida = true;
			} catch (Exception e) {
				System.out.print(e.getMessage());
			}
		}
		return ficha;
	}
}

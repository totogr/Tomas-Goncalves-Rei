package InterfazDeUsuario;

import Cartas.Carta;
import Enums.Movimiento;
import Estructuras.ListaSimplementeEnlazada;
import Tateti.Jugador;
import Tateti.Turno;

public class InterfazDeTurno {

	//INTERFACES DE CARTAS -----------------------------------------------------------------------------------------

	/**
	 * pre: -
	 * @return: devuelve true si el jugador ingresa un 1 para jugar una carta
	 */
	public static boolean quiereJugarCarta() {
		System.out.print("Si desea jugar una carta ingrese 1 o cualquier tecla para pasar el turno: ");
		int jugar = IngresoPorTeclado.leerEntero();
		return jugar == 1;
	}

	/**
	 * pre:-
	 * @param jugador
	 * @throws Exception lanza una exepcion si el jugador es vacio
	 * @return: muestra las cartas del jugador y devuelve la carta elegida
	 */
	public static Carta elegirCartaAJugar(Jugador jugador) throws Exception {
		if(jugador == null) {
			throw new Exception("El jugador no puede ser vacio");
		}
		int numeroDeCarta=0;
		while(numeroDeCarta<1 || numeroDeCarta>jugador.getCantidadDeCartas()+1) {
			System.out.println("Indique el numero de que carta quiere utilizar: ");
			listarCartas(jugador);
			numeroDeCarta = IngresoPorTeclado.leerEntero();
		}
		if(numeroDeCarta == jugador.getCantidadDeCartas()+1) {
			return null;
		}
		return devolverCarta(jugador, numeroDeCarta);
	}

	/**
	 * pre: -
	 * @param jugador
	 * @param numeroDeCarta: posicion de la carta a devolver
	 * @return: devuelve la carta elegida en la posicion dada
	 * @throws Exception: valida la lista de cartas
	 */
	private static Carta devolverCarta(Jugador jugador, int numeroDeCarta) throws Exception {
		ListaSimplementeEnlazada<Carta> cartas = jugador.getCartas();
		return cartas.obtener(numeroDeCarta);
	}

	/**
	 * pre: -
	 * post: muestra las cartas del jugador
	 * @param jugador: jugador del que se van a mostrar las cartas
	 */
	public static void mostrarCartas(Jugador jugador) {
		if (jugador.getCantidadDeCartas() == 0) {
			System.out.println("El jugador " + jugador.getNombre() + " no tiene cartas");
		}		
		System.out.println("El jugador " + jugador.getNombre() + " tiene las siguientes cartas: ");
		listarCartas(jugador);
	}

	/**
	 * pre: -
	 * post: lista las cartas del jugador
	 * @param jugador: jugador del que se van a listar las cartas
	 */
	public static void listarCartas(Jugador jugador) {
		ListaSimplementeEnlazada<Carta> cartas = jugador.getCartas();
		cartas.iniciarCursor();
		int i = 1;
		while(cartas.avanzarCursor()) {
			System.out.println(i + " - " + cartas.obtenerCursor());
			i++;
		}
		System.out.println(i+" - Salir");
	}

	//INTERFACES DE FICHAS -----------------------------------------------------------------------------------------

	/**
	 * pre: -
	 * post: imprime la cantidad de fichas restantes del jugador junto al nombre del jugador
	 * @param jugador: jugador a mostrar las fichas
	 */
	public static void imprimirCantidadDeFichasRestantes(Jugador jugador) {
		System.out.println(jugador.getNombre() + " tiene " + jugador.cantidadDeFichasEnMano() + " fichas disponibles");
	}

	/**
	 * pre: -
	 * @return: devuelve la posicion de casillero donde se quiere ingresar una ficha
	 */
	public static int[] ponerFichaEnCasillero(){
		int[] posicion = new int[3];
		System.out.println("Indique las posiciones del casillero en el cual quiere poner su ficha \n");
		System.out.print("Ingrese la posicion X del casillero: ");
		posicion[0] =  IngresoPorTeclado.leerEntero();
		System.out.print("Ingrese la posicion Y del casillero: ");
		posicion[1] =  IngresoPorTeclado.leerEntero();
		System.out.print("Ingrese la posicion Z del casillero: ");
		posicion[2] =  IngresoPorTeclado.leerEntero();
		return posicion; 
	}

	/**
	 * pre: -
	 * @return: devuelve la posicion de la ficha a mover
	 */
	public static int[] cualFichaMover() {
		int[] posicion = new int[3];
		System.out.print("Ingrese la posicion X de la ficha a mover: ");
		posicion[0] =  IngresoPorTeclado.leerEntero();
		System.out.print("Ingrese la posicion Y de la ficha a mover: ");
		posicion[1] =  IngresoPorTeclado.leerEntero();
		System.out.print("Ingrese la posicion Z de la ficha a mover: ");
		posicion[2] =  IngresoPorTeclado.leerEntero();
		return posicion;
	}

	/**
	 * pre: -
	 * @return: devuelve el movimiento que va a hacer la ficha
	 */
	public static Movimiento dondeMoverFicha() {
		int direccion = 0;
		while(direccion<1 || direccion > 6) {
			System.out.println("Ingrese en que direccion desea mover la ficha: ");
			System.out.println("1) Derecha");
			System.out.println("2) Izquierda");
			System.out.println("3) Abajo");
			System.out.println("4) Arriba");
			System.out.println("5) Adelante");
			System.out.println("6) Atras");
			direccion =  IngresoPorTeclado.leerEntero();			
		}
		Movimiento movimiento = null;
		switch (direccion) {
		case 1:
			movimiento = Movimiento.DERECHA; break;
		case 2:
			movimiento = Movimiento.IZQUIERDA; break;
		case 3:
			movimiento = Movimiento.ABAJO; break;
		case 4:
			movimiento = Movimiento.ARRIBA;	break;
		case 5:
			movimiento = Movimiento.ADELANTE; break;
		case 6:
			movimiento = Movimiento.ATRAS; break;
		}
		return movimiento;
	}

	//INTERFACES GENERALES-----------------------------------------------------------------------------------------

	/**
	 * pre: -
	 * @param turnoActual 
	 * @return: devuelve un numero random del 1 al 6
	 */
	public static int tirarDado(Turno turnoActual) {
		int numeroDeDado = (int) ((Math.random() * 5) + 1);	
		System.out.println("Presione una letra y luego enter para tirar el dado");
		IngresoPorTeclado.leerString();
		System.out.println("Se tiro el dado y salio el numero... " + numeroDeDado +"!");
		System.out.println("\n"+turnoActual.getNombreDeJugador() + " levanta " + numeroDeDado + " cartas del mazo.");
		return numeroDeDado;
	}

	/**
	 * pre:-
	 * post: imprime informacion del turno actual, como en el caso de que el turno este bloqueado, o si tiene las fichas bloqueadas, 
	 * 		o la cantidad de subturnos que tiene
	 * @param turnoActual: turno que se esta ejecutando
	 */
	public static void imprimirInformacionDelTurno(Turno turnoActual) {
		if(turnoActual.estaBloqueado()) {
			System.out.println("\n\nTurno: "+ turnoActual.getNombreDeJugador());
			System.out.println("El turno está bloqueado.");
			System.out.println("Le quedan "+ (turnoActual.getBloqueosRestantes() - 1) + " bloqueos restantes");
			return;
		}
		if(turnoActual.getJugador().tieneTodasLasFichasBloqueadas()) {
			System.out.println("\n\nTurno: "+ turnoActual.getNombreDeJugador());
			System.out.println("El jugador tiene todas las fichas bloqueadas y no puede mover ninguna ficha ");
			return;
		}
		System.out.println("\n\nTurno: "+ turnoActual.getNombreDeJugador());
		System.out.println("Subturnos restantes: " + (turnoActual.getCantidadDeSubturnos()-1) );
		System.out.println("Fichas en mano: " + turnoActual.getJugador().cantidadDeFichasEnMano()+"\n");
		
	}
}

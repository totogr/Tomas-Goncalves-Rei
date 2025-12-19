package InterfazDeUsuario;

import Enums.Color;
import Estructuras.Lista;
import Estructuras.ListaSimplementeEnlazada;
import Tateti.ColorRGB;
import Tateti.Ficha;
import Tateti.Jugador;
import Tateti.Tablero;


public class InterfazPrincipal {

	//ATRIBUTOS -----------------------------------------------------------------------------------------------

	private Lista<Color> listaDeColores = null;

	//CONSTRUCTORES -------------------------------------------------------------------------------------------

	/**
	 * pre: -
	 * post: crea una lista con todos los colores de eleccion rapida
	 */
	public InterfazPrincipal() throws Exception{
		this.listaDeColores=Color.listaTodosColores();
	}

	//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

	/**
	 * pre: -
	 * post: texto de inicio de juego
	 */
	public static void comenzarElJuego() {
		System.out.println("Bienvenido al TaTeTi");
		System.out.println("Debera ingresar la cantidad de jugadores y las medidas de un tablero en 3D \n");
	}

	/**
	 * pre: -
	 * @return: devuelve la cantidad de jugadores elegidos por el usuario
	 */
	public static int ingresarCantidadDeJugadores() {
		System.out.print("Ingrese la cantidad de jugadores: ");
		int cantidadDeJugadores = IngresoPorTeclado.leerEntero();
		return cantidadDeJugadores;
	}

	/**
	 * pre: -
	 * @return: devuelve el tamaño del tablero como un vector de la forma [ancho, alto, profundidad]
	 */
	public static int[] ingresarTamañoDelTablero() {
		int[] dimensiones = new int[3];
		System.out.print("Ingrese el ancho del tablero: ");
		dimensiones[0] = IngresoPorTeclado.leerEntero();
		System.out.print("Ingrese el alto del tablero: ");
		dimensiones[1] = IngresoPorTeclado.leerEntero();
		System.out.print("Ingrese la profundidad del tablero: ");
		dimensiones[2] = IngresoPorTeclado.leerEntero();
		return dimensiones;
	}

	/**
	 * pre: -
	 * post: devuelve la cantidad de fichas con las que comenzara cada jugador
	 * @param cantidadDeFichas: cantidad de fichas iniciales
	 */
	public static void ingresarCantidadDeFichas(int cantidadDeFichas) {
		System.out.print("Cada jugador tendra " + cantidadDeFichas + " fichas iniciales para ingresar, antes de poder realizar movimientos \n");
	}

	/**
	 * pre: -
	 * @param numeroDeJugador: numero identificador del jugador
	 * @return: devuelve el color que selecciona el jugador
	 * @throws Exception: valida que el nuevo color no sea nulo
	 */
	public ColorRGB ingresarColorDeJugador(int numeroDeJugador) throws Exception {
		ColorRGB colorRGB = null;
		System.out.println("\nColor de jugador numero " + numeroDeJugador + "\n");
		boolean colorValido = false;
		while(!colorValido) {
			System.out.println("Seleccione uno de los siguientes colores o ingrese 0 para cargar un color RGB \n ");
			Color.imprimirTodosColores(this.listaDeColores);
			System.out.print("Numero: ");
			int numeroDeColor = IngresoPorTeclado.leerEntero();
			try {
				if(numeroDeColor == 0) {
					int[] vectorRGB = new int[3];
					System.out.print("Ingrese la cantidad de Rojo: ");
					vectorRGB[0] = IngresoPorTeclado.leerEntero();
					System.out.print("Ingrese la cantidad de Verde: ");
					vectorRGB[1] = IngresoPorTeclado.leerEntero();
					System.out.print("Ingrese la cantidad de Azul: ");
					vectorRGB[2] = IngresoPorTeclado.leerEntero();
					colorRGB = new ColorRGB(vectorRGB[0], vectorRGB[1], vectorRGB[2]);
					colorValido=true;
				}
				else{ 
					colorRGB = new ColorRGB(numeroDeColor);
					if(colorDisponible(colorRGB.getColor())){
						actualizarLista(Color.listaDeColoresSinUsar(this.listaDeColores, colorRGB.getColor()));
						colorValido = true;
					}else {
						colorValido = false;
					}
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return colorRGB;
	}	

	/**
	 * pre: -
	 * @param numeroDeJugador: numero identificador del jugador
	 * @return: devuelve el nombre elegido por el usuario para el jugador
	 */
	public static String ingresarNombreDeJugador(int numeroDeJugador) {
		System.out.print("Ingrese el nombre del jugador: ");
		String nombre = IngresoPorTeclado.leerString();
		return nombre;
	}

	/**
	 * pre: la lista de colores no debe estar vacia
	 * @param color: color a revisar
	 * @return: devuelve true si el color esta en la lista de colores
	 */
	private boolean colorDisponible(Color color) throws Exception {
		if(!listaDeColores.existe(color)){
			throw new Exception("El color " + color.getNombreColor() + " ya fue elegido por otro jugador");
		}
		return true;
	}

	/**
	 * post: actualiza la lista de colores del atributo
	 * @param listaColores: lista a actualizar
	 */
	private void actualizarLista(Lista<Color> listaDeColores) {
		this.listaDeColores = listaDeColores;
	}

	/**
	 * pre: -
	 * post: imprime las dimensiones del tablero y sus jugadores
	 * @param tablero: tablero del cual imprimir las dimensiones
	 * @param jugadores: jugador actuales del juego
	 */
	public static void imprimirComienzoDeJuego(Tablero<Ficha> tablero, ListaSimplementeEnlazada<Jugador> jugadores) {
		System.out.println("\n\nEl juego ha comenzadooooo!");
		System.out.println("Tablero de " + tablero.getAncho() + "x"+ tablero.getAlto() + "x"+ tablero.getProfundidad());
		System.out.println("Jugadores: ");
		jugadores.iniciarCursor();
		while(jugadores.avanzarCursor()) {
			System.out.println(jugadores.obtenerCursor().getNombre());
		}
	}
}

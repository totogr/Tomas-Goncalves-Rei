package Tateti;


public class Validacion {

	/**
	 * pre: -
	 * @param numero: numero a validar
	 * @return: devuelve true si el numero es mayor a cero
	 */
	public static boolean numeroMayorACero(int numero) {
		return (numero > 0);
	}

	/**
	 * pre: -
	 * @param posicion: posicion a validar
	 * @param longitudMaxima: longitud maxima de la posicion
	 * @return: devuelve true si la posicion se encuentra entre 1 y la longitud maxima
	 */
	public static boolean validarPosicion(int posicion, int longitudMaxima) {
		return (posicion >= 1 && posicion <= longitudMaxima + 1);
	}
	
	/**
	 * pre: -
	 * @param string: string a validar
	 * @return: devuelve true si el string no es nulo
	 */
	public static boolean validarString(String string) {
		return (string != null);
	}
	
	/**
	 * pre: -
	 * @param color: color a validar
	 * @return: devuelve true si el color no es nulo
	 */
	public static boolean validarColor(ColorRGB color) {
		return (color != null);
	}
	
	/**
	 * pre: -
	 * @param cantidadDeColor: cantidad de color del RGB
	 * @return: devuelve true si la cantidad de color esta entre 0 y 255
	 */
	public static boolean validarCantidadDeColorRGB(int cantidadDeColor) {
		return (0 <= cantidadDeColor && cantidadDeColor <= 255);
	}
	
	/**
	 * pre: -
	 * post: valida que el jugador no se elija a el mismo y
	 * 		que el jugador tenga fichas en el tablero para cambiar el color
	 * @param jugador: jugador a cambiar de color ficha
	 * @param turno: turno del jugador actual
	 * @throws Exception
	 */
	public static  void validarJugador(Jugador jugador, Turno turno) throws Exception {
		if(jugador.cantidadDeFichasEnTablero() < 0) {
			throw new Exception("El jugador elegido no tiene fichas en el tablero");
		}
		if(jugador.equals(turno.getJugador())){
			throw new Exception("No se puede elegir a si mismo");
		}
	}
}
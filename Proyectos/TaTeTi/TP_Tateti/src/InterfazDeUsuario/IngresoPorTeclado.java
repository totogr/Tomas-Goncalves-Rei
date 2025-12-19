package InterfazDeUsuario;

import java.util.Scanner;

public class IngresoPorTeclado {

	//ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
	//ATRIBUTOS -----------------------------------------------------------------------------------------------

	public static Scanner scanner = null;

	//CONSTRUCTORES -------------------------------------------------------------------------------------------

	/**
	 * pre: -
	 * post: inicializa el escaner
	 */
	public static void inicializar() {
		if(scanner == null) {
			scanner = new Scanner(System.in);
		}
	}

	//METODOS DE CLASE ----------------------------------------------------------------------------------------
	//METODOS GENERALES ---------------------------------------------------------------------------------------
	//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

	/**
	 * pre: -
	 * @return: lee una palabra
	 */
	public static String leerString() {
		if(scanner == null) {
			inicializar();
		}
		return scanner.next();
	}

	/**
	 * pre: -
	 * @param texto: texto a validar
	 * @return: devuelve true si el String pasado es un numero
	 */
	public static boolean esEntero(String texto) {
		try {
			Integer.parseInt(texto);
			return true;
		}catch(NumberFormatException ex) {
			return false;
		}
	}

	/**
	 * pre: -
	 * @return: lee un entero y si lo ingresado no es un entero le vuelve a pedir que ingrese un numero
	 */
	public static int leerEntero() {
		if(scanner == null) {
			inicializar();
		}
		String numero;
		do {
			numero = leerString();
			if(!esEntero(numero)) {
				System.out.println("No es un entero eso!");
			}
		} while(!esEntero(numero));
		return Integer.parseInt(numero);
	}

	/**
	 * pre: -
	 * post: finaliza el escaner
	 */
	public static void finalizar() {
		if(scanner != null) {
			scanner.close();
		}
	}

	//GETTERS SIMPLES -----------------------------------------------------------------------------------------
	//SETTERS SIMPLES -----------------------------------------------------------------------------------------
}

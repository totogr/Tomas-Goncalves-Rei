package Enums;

import Estructuras.Lista;
import Estructuras.ListaSimplementeEnlazada;

public enum Color {
	ROJO("Rojo", 1),
	VERDE("Verde", 2),
	AZUL("Azul",3),
	AMARILLO("Amarillo",4),
	CIAN("Cian",5),
	MAGENTA("Magenta",6),
	NEGRO("Negro",7),
	BLANCO("Blanco",8),
	NARANJA("Naranja",9),
	ROJO_OSCURO("Rojo Oscuro",10),
	VERDE_CLARO("Verde claro",11),
	AZUL_OSCURO("Azul Oscuro",12),
	CELESTE("Celeste",13),
	VIOLETA("Violeta",14),
	ROSA("Rosa",15),
	MARRON("Marron",16),
	GRIS("Gris",17);

	//ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
	//ATRIBUTOS -----------------------------------------------------------------------------------------------

	private String nombreColor;
	private int numeroColor;

	//CONSTRUCTORES -------------------------------------------------------------------------------------------
	
	private Color(String nombreColor, int numeroColor) {
		this.nombreColor = nombreColor;
		this.numeroColor = numeroColor;
	}

	//METODOS DE CLASE ----------------------------------------------------------------------------------------
	//METODOS GENERALES ---------------------------------------------------------------------------------------
	//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

	/**
	 * pre: -
	 * post: imprime todos los colores del enum con su indice
	 */
	public static void imprimirTodosColores() {
		for(Color color : values()) {
			System.out.println("Color: " + color.getNombreColor() + " -- Número: " + color.getNumeroColor());
		}
	}

	/**
	 * pre: -
	 * post: imprime todos los colores de la lista recibida por parametro, su color e indice 
	 * @param listaDeColores: lista de colores a mostrar
	 */
	public static void imprimirTodosColores(Lista<Color> listaDeColores) {
		listaDeColores.iniciarCursor();
		while(listaDeColores.avanzarCursor()) {
			Color color = listaDeColores.obtenerCursor();
			System.out.println("Color: " + color.getNombreColor() + " -- Número: " + color.getNumeroColor());
		}
	}

	/**
	 * pre: -
	 * @return: devuelve una lista de todos los colores del enum
	 * @throws Exception: valida la lista
	 */
	public static Lista<Color> listaTodosColores() throws Exception {
		Lista<Color> listaColores = new ListaSimplementeEnlazada<>();
		for(Color color : values()) {
			listaColores.agregar(color);
		}
		return listaColores;
	}

	/**
	 * pre: -
	 * @return: devuelve la cantidad de colores del enum
	 */
	public static int getCantidadDeColores() {
		return Color.values().length;
	}

	/**
	 * pre: la lista de colores no debe estar vacia
	 * @param listaDeColores: lista de colores
	 * @param colorAQuitar: color a sacar de la lista
	 * @return: quita el color a la lista de colores pasado por parametro y esta se devuelve al finalizar la funcion 
	 * @throws Exception: valida la lista
	 */
	public static Lista<Color> listaDeColoresSinUsar(Lista<Color> listaDeColores, Color colorAQuitar) throws Exception {
		Lista<Color> lista = new ListaSimplementeEnlazada<>();
		listaDeColores.iniciarCursor();
		while(listaDeColores.avanzarCursor()) {
			Color color = listaDeColores.obtenerCursor();
			if(!color.equals(colorAQuitar)) {
				lista.agregar(color);
			}
		}
		return lista;
	}

	//GETTERS SIMPLES -----------------------------------------------------------------------------------------

	/**
	 * pre: -
	 * @return: devuelve el nombre del color
	 */
	public String getNombreColor() {
		return this.nombreColor;
	}

	/**
	 * pre: -
	 * @return: devuelve el numero del indice del color
	 */	
	public int getNumeroColor() {
		return this.numeroColor;
	}

	//SETTERS SIMPLES -----------------------------------------------------------------------------------------	
	
	
	
}

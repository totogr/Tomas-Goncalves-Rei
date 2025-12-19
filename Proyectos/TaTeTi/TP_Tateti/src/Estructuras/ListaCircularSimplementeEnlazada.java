package Estructuras;

public class ListaCircularSimplementeEnlazada<T> extends ListaSimplementeEnlazada<T> {
	//ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
	//ATRIBUTOS -----------------------------------------------------------------------------------------------
	//CONSTRUCTORES -------------------------------------------------------------------------------------------
	//METODOS DE CLASE ----------------------------------------------------------------------------------------
	//METODOS GENERALES ---------------------------------------------------------------------------------------
	//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------
	/**
	 * pre: posición pertenece al intervalo: [1, contarElementos() + 1]
	 * pos: agrega el elemento en la posición indicada.
	 */
	public void agregar(T elemento, int posicion) throws Exception {
		if ((posicion < 1) ||
				(posicion > getTamanio()+1 )) {
			throw new Exception("La posicion debe estar entre 1 y " + getTamanio()+1);
		}
		
		Nodo<T> nuevo = new Nodo<T>(elemento);
		if (posicion == 1) {
			nuevo.setSiguiente( this.getPrimero());
			this.setPrimero( nuevo );
		} else {
			Nodo<T> anterior = this.getNodo(posicion -1);
			nuevo.setSiguiente( this.getPrimero());
			anterior.setSiguiente( nuevo );
		}
		this.aumentarTamanio();
	}
	
	//GETTERS SIMPLES -----------------------------------------------------------------------------------------
	
	//SETTERS SIMPLES -----------------------------------------------------------------------------------------	
}

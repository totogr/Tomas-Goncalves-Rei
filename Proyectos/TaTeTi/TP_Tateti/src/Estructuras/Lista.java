package Estructuras;

public abstract class Lista<T> {
	//ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
	//ATRIBUTOS -----------------------------------------------------------------------------------------------

	private Nodo<T> primero = null;
	private int tamanio = 0;
	private Nodo<T> cursor = null;
	private Nodo<T> cursorAuxiliar = null;

	//CONSTRUCTORES -------------------------------------------------------------------------------------------

	/**
	 * pre:
	 * pos: crea una lista vacia
	 */
	public Lista() {
		this.primero = null;
		this.tamanio = 0;
		this.cursor = null;
	}

	//METODOS DE CLASE ----------------------------------------------------------------------------------------
	//METODOS GENERALES ---------------------------------------------------------------------------------------
	//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

	/**
	 * pre:
	 * pos: indica si la Lista tiene algún elemento.
	 */
	public boolean estaVacia() {
		return (this.tamanio == 0);
	}

	/**
	 * pre: -
	 * pos: agrega el elemento al final de la Lista, en la posición:
	 *       contarElementos() + 1.
	 */
	public void agregar(T elemento) throws Exception {
		this.agregar(elemento, this.getTamanio() + 1);
	}

	/**
	 * pre: posición pertenece al intervalo: [1, contarElementos() + 1]
	 * pos: agrega el elemento en la posición indicada.
	 */
	public abstract void agregar(T elemento, int posicion) throws Exception;

	/*
	 * pre : posición pertenece al intervalo: [1, contarElementos()]
	 * post: remueve de la Lista el elemento en la posición indicada.
	 */
	public abstract void remover(int posicion) throws Exception;

	/**
	 * pre : posición pertenece al intervalo: [1, contarElementos()]
	 * pos: devuelve el dato de la posicion
	 */
	public T obtener(int posicion) throws Exception {
		validarPosicion(posicion);
		return this.getNodo(posicion).getDato();
	}

	/**
	 * pre : posición pertenece al intervalo: [1, contarElementos()]
	 * pos: cambia el elemento de la posicion
	 */
	public void cambiar(T elemento, int posicion) throws Exception {
		validarPosicion(posicion);
		this.getNodo(posicion).setDato(elemento);
	}

	/*
	 * post: deja el cursor de la Lista preparado para hacer un nuevo
	 *       recorrido sobre sus elementos.
	 */
	public void iniciarCursor() {
		this.cursor = null;
	}
	
	public void iniciarCursorAuxiliar() {
		this.cursorAuxiliar = null;
	}

	/*
	 * pre : se ha iniciado un recorrido (invocando el método
	 *       iniciarCursor()) y desde entonces no se han agregado o
	 *       removido elementos de la Lista.
	 * post: mueve el cursor y lo posiciona en el siguiente elemento
	 *       del recorrido.
	 *       El valor de retorno indica si el cursor quedó posicionado
	 *       sobre un elemento o no (en caso de que la Lista esté vacía o
	 *       no existan más elementos por recorrer.)
	 */
	public boolean avanzarCursor() {
		if (this.cursor == null) {
			this.cursor = this.primero;
		} else {
			this.cursor = this.cursor.getSiguiente();
		}

		/* pudo avanzar si el cursor ahora apunta a un nodo */
		return (this.cursor != null);
	}
	
	public boolean avanzarCursorAuxiliar() {
		if (this.cursorAuxiliar == null) {
			this.cursorAuxiliar = this.primero;
		} else {
			this.cursorAuxiliar = this.cursorAuxiliar.getSiguiente();
		}

		/* pudo avanzar si el cursor ahora apunta a un nodo */
		return (this.cursorAuxiliar != null);
	}

	/*
	 * pre : el cursor está posicionado sobre un elemento de la Lista,
	 *       (fue invocado el método avanzarCursor() y devolvió true)
	 * post: devuelve el elemento en la posición del cursor.
	 *
	 */
	public T obtenerCursor() {
		T elemento = null;
		if (this.cursor != null) {
			elemento = this.cursor.getDato();
		}
		return elemento;
	}
	public T obtenerCursorAuxiliar() {
		T elemento = null;
		if (this.cursorAuxiliar != null) {
			elemento = this.cursorAuxiliar.getDato();
		}
		return elemento;
	}

	protected void aumentarTamanio() {
		this.tamanio++;
	}
	
	protected void disminuirTamanio() {
		this.tamanio--;
	}
	
	/**
	 * pre: se inicio el cursor
	 * @param valor: valor a comprobar si existe
	 * @return: devuelve si el valor existe en la lista
	 */
	public boolean existe(T valor) {
		Nodo<T> cursor = this.primero;
		while (cursor != null) {
			if (cursor.getDato().equals(valor)) {
				return true;
			}
			cursor = cursor.getSiguiente();
		}
		return false;
	}
	
	//GETTERS SIMPLES -----------------------------------------------------------------------------------------

	/*
	 * post: devuelve la cantidad de elementos que tiene la Lista.
	 */
	public int getTamanio() {
		return this.tamanio;
	}

	/*
	 * pre : posición pertenece al intervalo: [1, contarElementos()]
	 * post: devuelve el nodo en la posición indicada.
	 */
	protected Nodo<T> getNodo(int posicion) throws Exception {
		validarPosicion(posicion);
		Nodo<T> actual = this.primero;
		for(int i = 1; i < posicion; i++) {
			actual = actual.getSiguiente();
		}
		return actual;
	}

	protected void validarPosicion(int posicion) throws Exception {
		if ((posicion < 1) ||
				(posicion > getTamanio() )) {
			throw new Exception("La posicion debe estar entre 1 y " + getTamanio());
		}
	}

	protected Nodo<T> getPrimero() {
		return this.primero;
	}
	
	//SETTERS SIMPLES -----------------------------------------------------------------------------------------
	
	protected void setPrimero(Nodo<T> primero) {
		this.primero = primero;
	}
}























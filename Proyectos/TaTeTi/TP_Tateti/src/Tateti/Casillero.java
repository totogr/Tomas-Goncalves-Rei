package Tateti;

import Enums.Direccion;
import Enums.EstadoDeBloqueo;
import Enums.EstadoDeCasillero;
import Enums.Movimiento;

public class Casillero<T> {

	//ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
	//ATRIBUTOS -----------------------------------------------------------------------------------------------

	private int x = 0;
	private int y = 0;
	private int z = 0;
	private T dato = null;
	private EstadoDeBloqueo estado = null;
	private EstadoDeCasillero estadoDeCasillero = null;

	//CONSTRUCTORES -------------------------------------------------------------------------------------------

	/**
	 * pre: -
	 * post: crea un casillero en una posicion X, Y, Z
	 * @param x: posicion X
	 * @param y: posicion Y
	 * @param z: posicion Z
	 * @throws Exception: validacion de posicion del casillero
	 */
	public Casillero(int x, int y, int z) throws Exception {
		if (!Validacion.numeroMayorACero(x) ||
				!Validacion.numeroMayorACero(y) ||
				!Validacion.numeroMayorACero(z)) {
			throw new Exception("Las coordenadas del casillero deben ser mayor a 0");
		}
		this.x = x;
		this.y = y;
		this.z = z;
		this.estado = EstadoDeBloqueo.HABILITADO;
		this.estadoDeCasillero = EstadoDeCasillero.VACIO;
	}

	//METODOS DE CLASE ----------------------------------------------------------------------------------------
	//METODOS GENERALES ---------------------------------------------------------------------------------------

	@Override
	public String toString() {	
		return "Casillero (" + this.x + ", " + this.y + ", " + this.z + ")";
	}

	//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

	/**
	 * pre:
	 * @return: devuelve true si el casillero esta ocupado
	 */
	public boolean estaOcupado() {
		return this.estadoDeCasillero == EstadoDeCasillero.OCUPADO;
	}

	/**
	 * pre:
	 * @return: devuelve true si el casillero esta vacio
	 */
	public boolean estaVacio() {
		return this.estadoDeCasillero == EstadoDeCasillero.VACIO;
	}

	/**
	 * pre: -
	 * @return: devuelve true si el casillero esta bloqueado
	 */
	public boolean estaBloqueado() {
		return (getEstado()==EstadoDeBloqueo.BLOQUEADO);
	}

	/**
	 * pre: -
	 * post: cambia el estado del casillero a bloqueado
	 * @throws Exception: valida que el casillero no este ocupado ni bloqueado
	 */
	public void anularCasillero() throws Exception {
		if(estaOcupado()) {
			throw new Exception("El casillero no se puede anular porque esta ocupado");
		}
		if(estaBloqueado()) {
			throw new Exception("El casillero ya esta anulado");
		}
		this.estado = EstadoDeBloqueo.BLOQUEADO;
	}

	/**
	 * pre: -
	 * post: cambia el estado del casillero a habilitado
	 * @throws Exception: valida que el casillero no esta habilitado
	 */
	public void habilitarCasillero() throws Exception {
		if(!estaBloqueado()) {
			throw new Exception("El casillero ya esta habilitado");
		}
		this.estado = EstadoDeBloqueo.HABILITADO;
	}

	/**
	 * pre: -
	 * post: saca el dato del casillero
	 * @throws Exception: valida que el casillero este ocupado
	 */
	public void desocupar() throws Exception {
		if(!estaOcupado()) {
			throw new Exception("El casillero esta vacio");
		}
		this.dato = null;
		this.estadoDeCasillero = EstadoDeCasillero.VACIO;
	}

	/**
	 * pre: -
	 * @param movimiento: recibe el movimiento que indica la direccion de la ficha que sera devuelta
	 * @param tablero: recibe el tablero en el cual se movera la ficha
	 * @return: devuelve la ficha que esta en la direccion del movimiento pasado por parametro
	 * @throws Exception: lanza una excepcion en caso de que el movimiento no sea valido
	 */
	public Casillero<Ficha> getCasilleroVecino(Movimiento movimiento, Tablero<Ficha> tablero) throws Exception {
		int nuevaX = this.x;
		int nuevaY = this.y;
		int nuevaZ = this.z;
		switch (movimiento) {
		case ARRIBA: nuevaY--; break;
		case ABAJO: nuevaY++; break;
		case IZQUIERDA: nuevaX--; break;
		case DERECHA: nuevaX++; break;
		case ADELANTE: nuevaZ--; break;
		case ATRAS: nuevaZ++; break;
		default:
			throw new Exception("Movimiento no válido");
		}
		return tablero.getCasillero(nuevaX, nuevaY, nuevaZ);
	}

	/**
	 * pre: -
	 * @param movimiento: recibe el movimiento que indica la direccion 
	 * @param tablero: recibe el tablero en el cual se realizara la comprobacion
	 * @return: devuelve true en caso de que el vecino exista y false si el casillero vecino esta vacio
	 * @throws Exception: lanza una excepcion en caso de que el movimiento no sea valido
	 */
	public boolean existeElVecino(Movimiento movimiento, Tablero<Ficha> tablero) throws Exception {
		int nuevaX = this.x;
		int nuevaY = this.y;
		int nuevaZ = this.z;
		switch (movimiento) {
		case ARRIBA: nuevaY--; break;
		case ABAJO: nuevaY++; break;
		case IZQUIERDA: nuevaX--; break;
		case DERECHA: nuevaX++; break;
		case ADELANTE: nuevaZ--; break;
		case ATRAS: nuevaZ++; break;
		default:
			throw new Exception("Movimiento no válido");
		}
		if(tablero.validarPosicion(nuevaX, nuevaY, nuevaZ)) {
			return true;
		}
		
		return false;
	}

	/**
	 * 
	 * @param direccion: recibe la direccion en la cual se devolvera el casillero
	 * @param tablero: recibe el tablero
	 * @return: devuelve el casillero que esta en la direccion pasada por parametro
	 * @throws Exception: lanza una excepcion en caso de que la direccion no sea valida
	 */
	public Casillero<Ficha> getCasilleroVecino(Direccion direccion, Tablero<Ficha> tablero) throws Exception {
		int nuevaX = this.x;
		int nuevaY = this.y;
		int nuevaZ = this.z;
		switch (direccion) {
		case ARRIBA: nuevaY--; break;
		case ABAJO: nuevaY++; break;
		case IZQUIERDA: nuevaX--; break;
		case DERECHA: nuevaX++;  break;
		case IZQUIERDA_ARRIBA: nuevaX--; nuevaY--; break;
		case IZQUIERDA_ABAJO:  nuevaX--; nuevaY++; break;
		case DERECHA_ARRIBA: nuevaX++; nuevaY--; break;
		case DERECHA_ABAJO: nuevaX++; nuevaY++; break;
		case ADELANTE: nuevaZ--;  break;
		case ADELANTE_ARRIBA: nuevaZ--;  nuevaY--; break;
		case ADELANTE_ABAJO: nuevaZ--;  nuevaY++; break;
		case ADELANTE_IZQUIERDA: nuevaZ--; nuevaX--; break;
		case ADELANTE_DERECHA: nuevaZ--; nuevaX++; break;
		case ADELANTE_ARRIBA_IZQUIERDA: nuevaZ--; nuevaY--; nuevaX--; break;
		case ADELANTE_ARRIBA_DERECHA: nuevaZ--; nuevaY--; nuevaX++;  break;
		case ADELANTE_ABAJO_IZQUIERDA: nuevaZ--; nuevaY++; nuevaX--; break;
		case ADELANTE_ABAJO_DERECHA: nuevaZ--; nuevaY++; nuevaX++;  break;
		case ATRAS: nuevaZ++; break;
		case ATRAS_ARRIBA: nuevaZ++; nuevaY--; break;
		case ATRAS_ABAJO: nuevaZ++; nuevaY++; break;
		case ATRAS_IZQUIERDA: nuevaZ++; nuevaX--; break;
		case ATRAS_DERECHA: nuevaZ++; nuevaX++; break;
		case ATRAS_ARRIBA_IZQUIERDA: nuevaZ++; nuevaY--; nuevaX--; break;
		case ATRAS_ARRIBA_DERECHA: nuevaZ++; nuevaY--; nuevaX++; break;
		case ATRAS_ABAJO_IZQUIERDA: nuevaZ++; nuevaY++; nuevaX--; break;
		case ATRAS_ABAJO_DERECHA: nuevaZ++; nuevaY++; nuevaX++; break;
		default:
			throw new Exception("Dirección no válida");
		}
		if((nuevaX > 0 && nuevaX <= tablero.getAncho()) &&
				(nuevaY > 0 && nuevaY <= tablero.getAlto()) &&
				(nuevaZ > 0 && nuevaZ <= tablero.getProfundidad())) {
			return tablero.getCasillero(nuevaX, nuevaY, nuevaZ);
		}
		return null;
	}

	//GETTERS SIMPLES -----------------------------------------------------------------------------------------

	/**
	 * pre: -
	 * post: devuelve el estado de bloqueo del casillero
	 */
	public EstadoDeBloqueo getEstado() {
		return this.estado;
	}

	/**
	 * pre: -
	 * @return: devuelve si el casillero esta ocupado o vacio
	 */
	public EstadoDeCasillero getEstadoCasillero() {
		return this.estadoDeCasillero;
	}

	/**
	 * pre: -
	 * @return: devuelve la coordenada X del casillero
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * pre: -
	 * @return: devuelve la coordenada Y del casillero
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * pre: -
	 * @return: devuelve la coordenada Z del casillero
	 */
	public int getZ() {
		return this.z;
	}

	/**
	 * pre: -
	 * @return: devuelve el dato del casillero
	 */
	public T getDato() {
		return this.dato;
	}

	//SETTERS SIMPLES -----------------------------------------------------------------------------------------	

	/**
	 * pre: -
	 * post: se ingresa un dato al casillero
	 * @param dato: dato a ingresar en casillero
	 * @throws Exception: valida que el casillero este vacio
	 */
	public void setDato(T dato) throws Exception {
		if(estaOcupado()) {
			throw new Exception("El casillero esta ocupado");
		}
		this.dato = dato;
		this.estadoDeCasillero = EstadoDeCasillero.OCUPADO;
	}
}


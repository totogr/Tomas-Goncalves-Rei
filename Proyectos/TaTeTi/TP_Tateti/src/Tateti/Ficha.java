package Tateti;

import java.util.Objects;

import Enums.EstadoDeBloqueo;
import Enums.Ubicacion;

public class Ficha {
	
	//ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
	//ATRIBUTOS -----------------------------------------------------------------------------------------------

	private ColorRGB colorDeFicha = null;
	private EstadoDeBloqueo estado = null;
	private Ubicacion ubicacion = null;
	
	//CONSTRUCTORES -------------------------------------------------------------------------------------------
	
	/**
	 * pre: -
	 * post: crea una ficha de un color
	 * @param colorDeFicha: color de la ficha
	 * @throws Exception: si el color de la ficha es nulo
	 */
	public Ficha(ColorRGB color) throws Exception {
		if (!Validacion.validarColor(color)) {
			throw new Exception("Se debe ingresar un color de ficha");
		}
		this.colorDeFicha = color;
		this.estado = EstadoDeBloqueo.HABILITADO;
		this.ubicacion = Ubicacion.JUGADOR;
	}

	//METODOS DE CLASE ----------------------------------------------------------------------------------------
	//METODOS GENERALES ---------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "Ficha " + this.colorDeFicha;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(colorDeFicha, estado, ubicacion);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ficha other = (Ficha) obj;
		return Objects.equals(colorDeFicha, other.colorDeFicha) && estado == other.estado
				&& ubicacion == other.ubicacion;
	}

	//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------
	
	/**
	 * pre: -
	 * @param ficha: ficha a verificar color
	 * @return: devuelve si la ficha es el mismo color
	 * @throws Exception: valida que la ficha no sea nula
	 */
	public boolean esMismoColor(Ficha ficha)throws Exception {
		if(ficha==null) {
			throw new Exception("La ficha no puede ser vacia");
		}
		if(this.getColorDeFicha().getRojo() == ficha.getColorDeFicha().getRojo() &&
		   this.getColorDeFicha().getVerde() == ficha.getColorDeFicha().getVerde() &&
		   this.getColorDeFicha().getAzul() == ficha.getColorDeFicha().getAzul()) {
			return true;
		}
		return false;
	}
	
	/**
	 * pre: -
	 * post: cambia el estado de la ficha a bloqueada
	 * @throws Exception: valida que ya la ficha no este bloqueada
	 */
	public void bloquearFicha() throws Exception {
		if (this.estado == EstadoDeBloqueo.BLOQUEADO) {
			throw new Exception("La ficha ya se encuentra bloqueada");
		}
		this.estado = EstadoDeBloqueo.BLOQUEADO;
	}
	
	/**
	 * pre: -
	 * post: cambia el estado de la ficha a habilitada
	 * @throws Exception: valida que ya la ficha no este habilitada
	 */
	public void habilitarFicha() throws Exception {
		if (this.estado != EstadoDeBloqueo.HABILITADO) {
			this.estado = EstadoDeBloqueo.HABILITADO;
		}
	}
	
	/**
	 * pre: -
	 * post: cambia el color de una ficha
	 * @param colorNuevo: color nuevo a asignar
	 * @throws Exception: valida que el color no sea nulo y que la ficha no contenga ese color antes
	 */
	public void cambiarColor(ColorRGB colorNuevo) throws Exception {
		if(!Validacion.validarColor(colorNuevo)) {
			throw new Exception("El color de ficha no puede ser nulo");
		}
		if(colorNuevo == getColorDeFicha()) {
			throw new Exception("La ficha ya tiene el color " + colorNuevo);
		}
		this.colorDeFicha = colorNuevo;
	}
	
	/**
	 * pre: -
	 * post: cambia la ubicacion de la ficha al tablero
	 * @throws Exception: valida que la ficha ya no se encuentre en el tablero
	 */
	public void ubicarEnTablero() throws Exception {
		if(this.ubicacion == Ubicacion.JUGADOR) {
			this.ubicacion = Ubicacion.TABLERO;
		}
	}
	
	/**
	 * pre: -
	 * post: cambia la ubicacion de la ficha al tablero
	 * @throws Exception: valida que la ficha ya no se encuentre en el tablero
	 */
	public void ubicarEnJugador() throws Exception {
		if(this.ubicacion == Ubicacion.TABLERO) {
			this.ubicacion = Ubicacion.JUGADOR;
		}
	}
	
	/**
	 * pre: -
	 * @return: devuelve si la ficha esta en el tablero
	 */
	public boolean estaEnTablero() {
		return this.ubicacion == Ubicacion.TABLERO;
	}
	
	/**
	 * pre: -
	 * @return: devuelve si la ficha esta en el tablero
	 */
	public boolean estaEnJugador() {
		return this.ubicacion == Ubicacion.JUGADOR;
	}
	
	/**
	 * pre: -
	 * @return: devuelve si la ficha esta bloqueada
	 */
	public boolean estaBloqueado() {
		return this.estado == EstadoDeBloqueo.BLOQUEADO;
	}
	
	//GETTERS SIMPLES -----------------------------------------------------------------------------------------

	/**
	 * pre: -
	 * @return: devuelve el color de la ficha
	 */
	public ColorRGB getColorDeFicha() {
		return this.colorDeFicha;
	}

	/**
	 * pre: -
	 * @return: devuelve el estado de la ficha
	 */
	public EstadoDeBloqueo getEstado() {
		return this.estado;
	}
	
	/**
	 * pre: -
	 * @return: devuelve la ubicacion de la ficha
	 */
	public Ubicacion getUbicacion() {
		return this.ubicacion;
	}

	//SETTERS SIMPLES -----------------------------------------------------------------------------------------	
}

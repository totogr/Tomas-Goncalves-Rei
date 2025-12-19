package Cartas;

import java.util.Objects;
import Jugadas.Jugada;

public abstract class Carta {
	//ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------

	private static Long IdActual = 1L;

	//ATRIBUTOS -----------------------------------------------------------------------------------------------

	private Long id = null;
	private String titulo = null;

	//CONSTRUCTORES -------------------------------------------------------------------------------------------

	/**
	 * pre: -
	 * post: crea la carta
	 */
	protected Carta() {
		this.titulo = getTituloPorDefecto();
		this.id = Carta.getIdActual();
	}

	/**
	 * pre: -
	 * post: crea la carta con un titulo
	 * @param titulo: titulo de la carta
	 */
	protected Carta(String titulo) {
		this.titulo = titulo;
		this.id = Carta.getIdActual();
	}

	//METODOS DE CLASE ----------------------------------------------------------------------------------------

	/**
	 * pre: -
	 * @return: aumenta el id cada vez que se crea una carta
	 */
	private static Long getIdActual() {
		return Carta.IdActual++;
	}

	//METODOS GENERALES ---------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return this.getTitulo();
	}

	@Override 
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}
		Carta other = (Carta) obj;
		return Objects.equals(id, other.id);
	}

	//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

	/**
	 * pre: -
	 * @return: devuelve el titulo de las cartas heredadas
	 */
	protected abstract String getTituloPorDefecto();

	/**
	 * pre: -
	 * @return: devuelve la jugada de las cartas heredadas
	 */
	public abstract Jugada getJugada();

	//GETTERS SIMPLES -----------------------------------------------------------------------------------------

	/**
	 * pre: -
	 * @return: devuelve el titulo de la carta
	 */
	public String getTitulo() {
		return this.titulo;
	}

	/**
	 * pre: -
	 * @return: devuelve el id de la carta
	 */
	public Long getId() {
		return this.id;
	}

	//SETTERS SIMPLES -----------------------------------------------------------------------------------------
}

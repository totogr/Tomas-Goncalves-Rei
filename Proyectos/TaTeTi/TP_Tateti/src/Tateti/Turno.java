package Tateti;

public class Turno {

	//ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
	//ATRIBUTOS -----------------------------------------------------------------------------------------------

	private Jugador jugador = null;
	private int cantidadDeSubturnos = 0;
	private int bloqueosRestantes = 0;

	//CONSTRUCTORES -------------------------------------------------------------------------------------------

	/**
	 * pre:-
	 * pos: crea un turno con el jugador dado
	 * @param jugador no puede ser vacio
	 * @throws Exception lanza una exepcion si el jugador es vacio
	 */
	public Turno(Jugador jugador) throws Exception {
		if(jugador == null) {
			throw new Exception("El jugador no puede ser vacio");
		}
		this.jugador=jugador;
	}

	//METODOS DE CLASE ----------------------------------------------------------------------------------------
	//METODOS GENERALES ---------------------------------------------------------------------------------------
	//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

	/**
	 * pre: -
	 * post: incrementa los bloqueos restantes
	 * @param cantidadDeBloqueos: cantidad de bloqueos a incrementar
	 */
	public void incrementarBloqueosRestantes(int cantidadDeBloqueos) {
		this.bloqueosRestantes += cantidadDeBloqueos;
	}

	/**
	 * pre: -
	 * post: inicia el turno
	 */
	public void iniciarTurno() {
		this.cantidadDeSubturnos = 1;
	}

	/**
	 * pre: -
	 * post: termina el turno
	 */
	public void terminarTurno() {
		if(this.bloqueosRestantes > 0) {
			this.bloqueosRestantes--;
		}
	}

	/**
	 * pre: -
	 * @return: devuelve true si hay mas de 0 subturnos
	 */
	public boolean haySubturnos() {
		return (this.cantidadDeSubturnos > 0);
	}

	/**
	 * pre: -  
	 * post: agrega 1 subturno
	 */
	public void agregarSubturno() {
		this.cantidadDeSubturnos++;
	}

	/**
	 * pre: -
	 * post: utiliza y resta un subturno
	 */
	public void utilizarSubturno() {
		this.cantidadDeSubturnos--;
	}

	/**
	 * pre: -
	 * @return: devuelve true si tiene mas de 0 bloqueos restantes
	 */
	public boolean estaBloqueado() {
		return this.bloqueosRestantes > 0;
	}

	//GETTERS SIMPLES -----------------------------------------------------------------------------------------

	/**
	 * pre: -
	 * @return: devuelve la cantidad de subturnos
	 */
	public int getCantidadDeSubturnos() {
		return this.cantidadDeSubturnos;
	}

	/**
	 * pre: -
	 * @return: devuelve el jugador del turno
	 */
	public Jugador getJugador() {
		return this.jugador;
	}

	/**
	 * pre: -
	 * @return: devuelve el nombre del jugador del turno
	 */
	public String getNombreDeJugador() {
		return this.jugador.getNombre();
	}

	/**
	 * pre: -
	 * @return:  devuelve la cantidad de bloqueos restantes
	 */
	public int getBloqueosRestantes() {
		return this.bloqueosRestantes;
	}

	//SETTERS SIMPLES -----------------------------------------------------------------------------------------
}

package Enums;

public enum Direccion {
	ARRIBA("Arriba", 0,1,0), 
	ABAJO("Abajo",0,-1,0), 
	IZQUIERDA("Izquierda", -1,0,0), 
	DERECHA("Derecha", 1,0,0),
	ADELANTE("Adelante", 0,0,1),
	ATRAS("Atras", 0,0,-1),
	IZQUIERDA_ARRIBA("Izquierda arriba", -1,1,0),
	IZQUIERDA_ABAJO("Izquierda abajo", -1,-1,0),
	DERECHA_ARRIBA("Izquierda arriba", 1,1,0),
	DERECHA_ABAJO("Derecha abajo", 1,-1,0),
	ADELANTE_ARRIBA("Adelante arriba", 0,1,1),
	ADELANTE_ABAJO("Adelante abajo", 0,-1,1),
	ADELANTE_IZQUIERDA("Adelante Izquierda", -1,0,1),
	ADELANTE_DERECHA("Adelante Derecha", 1,0,1),
	ADELANTE_ARRIBA_IZQUIERDA("Adelante arriba izquierda", -1,1,1),
	ADELANTE_ARRIBA_DERECHA("Adelante arriba derecha", 1,1,1),
	ADELANTE_ABAJO_IZQUIERDA("Adelante abajo izquierda", -1,-1,1),
	ADELANTE_ABAJO_DERECHA("Adelante abajo derecha", 1,-1,1),
	ATRAS_ARRIBA("Atras arriba", 0,1,-1),
	ATRAS_ABAJO("Atras abajo", 0,-1,-1),
	ATRAS_IZQUIERDA("Atras izquierda", -1,0,-1),
	ATRAS_DERECHA("Atras derecha", 1,0,-1),
	ATRAS_ARRIBA_IZQUIERDA("Atras arriba izquierda", -1,1,-1),
	ATRAS_ARRIBA_DERECHA("Atras arriba derecha", 1,1,-1),
	ATRAS_ABAJO_IZQUIERDA("Atras abajo izquierda", -1,-1,-1),
	ATRAS_ABAJO_DERECHA("Atras abajo derecha", 1,-1,-1);
	
	//ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
	//ATRIBUTOS -----------------------------------------------------------------------------------------------	

	private String direccion;
	private int direccionX;
	private int direccionY;
	private int direccionZ;

	//CONSTRUCTORES -------------------------------------------------------------------------------------------

	private Direccion(String direccion, int direccionX, int direccionY, int direccionZ){
		this.direccion=direccion;
		this.direccionX=direccionX;
		this.direccionY=direccionY;
		this.direccionZ=direccionZ;
	}

	//METODOS DE CLASE ----------------------------------------------------------------------------------------
	//METODOS GENERALES ---------------------------------------------------------------------------------------
	//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------	
	//GETTERS SIMPLES -----------------------------------------------------------------------------------------

	/**
	 * pre: -
	 * @return: devuelve el nombre de la direccion
	 */
	public String getNombre() {
		return this.direccion;
	}

	/**
	 * pre: -
	 * @return: devuelve el movimiento en la direccion X
	 */
	public int getMovimientoX() {
		return this.direccionX;
	}

	/**
	 * pre: -
	 * @return: devuelve el movimiento en la direccion Y
	 */
	public int getMovimientoY() {
		return this.direccionY;
	}

	/**
	 * pre: -
	 * @return: devuelve el movimiento en la direccion Z
	 */
	public int getMovimientoZ() {
		return this.direccionZ;
	}

	//SETTERS SIMPLES -----------------------------------------------------------------------------------------	
}

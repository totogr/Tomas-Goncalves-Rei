package Enums;

public enum Movimiento {
	ARRIBA("Arriba",1, 0,-1,0), 
	ABAJO("Abajo",2,0,1,0), 
	IZQUIERDA("Izquierda",3, -1,0,0), 
	DERECHA("Derecha",4, 1,0,0),
	ADELANTE("Adelante",5, 0,0,-1),
	ATRAS("Atras",6, 0,0,1);
	
	//ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
	//ATRIBUTOS -----------------------------------------------------------------------------------------------

	private String movimiento;
	private int numeroMovimiento;
	private int direccionX;
	private int direccionY;
	private int direccionZ;

	//CONSTRUCTORES -------------------------------------------------------------------------------------------

	private Movimiento(String movimiento, int numeroMovimiento, int direccionX, int direccionY, int direccionZ){
		this.movimiento=movimiento;
		this.numeroMovimiento=numeroMovimiento;
		this.direccionX=direccionX;
		this.direccionY=direccionY;
		this.direccionZ=direccionZ;
	}

	//METODOS DE CLASE ----------------------------------------------------------------------------------------
	//METODOS GENERALES ---------------------------------------------------------------------------------------
	//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

	/**
	 * pre: -
	 * post: imprime los movimiento disponibles
	 */
	public static void imprimirMovimientos() {
		for(Movimiento movimiento : Movimiento.values()) {
			System.out.println(movimiento.getNumeroMovimiento() + ") " + movimiento.getNombre());
		}
	}

	/**
	 * pre: -
	 * @param numeroNumeroMovimiento: indice que indica el movimiento
	 * @return: devuelve el movimiento elegido
	 * @throws Exception: valida la eleccion del indice
	 */
	public static Movimiento seleccionarMovimiento(int numeroNumeroMovimiento)throws Exception{
		switch (numeroNumeroMovimiento) {
		case 1:
			return Movimiento.ARRIBA;
		case 2:
			return Movimiento.ABAJO;
		case 3:
			return Movimiento.IZQUIERDA;
		case 4:
			return Movimiento.DERECHA;
		case 5:
			return Movimiento.ADELANTE;
		case 6:
			return Movimiento.ATRAS;
		default:
			throw new Exception("Eleccion de movimiento fuera de rango");
		}
	}

	//GETTERS SIMPLES -----------------------------------------------------------------------------------------

	/**
	 * pre: -
	 * @return: devuelve el nombre del movimiento
	 */
	public String getNombre() {
		return this.movimiento;
	}

	/**
	 * pre: -
	 * @return: devuelve el numero del indice del movmimiento
	 */
	public int getNumeroMovimiento() {
		return this.numeroMovimiento;
	}

	/**
	 * pre: -
	 * @return: devuelve la direccion del movimiento X
	 */
	public int getMovimientoX() {
		return this.direccionX;
	}

	/**
	 * pre: -
	 * @return: devuelve la direccion del movimiento Y
	 */
	public int getMovimientoY() {
		return this.direccionY;
	}

	/**
	 * pre: -
	 * @return: devuelve la direccion del movimiento Z
	 */
	public int getMovimientoZ() {
		return this.direccionZ;
	}

	//SETTERS SIMPLES -----------------------------------------------------------------------------------------	
}

package Tateti;

import Cartas.Carta;
import Enums.Direccion;
import Enums.EstadoDeJuego;
import Enums.Movimiento;
import Estructuras.Lista;
import Estructuras.ListaCircularSimplementeEnlazada;
import Estructuras.ListaSimplementeEnlazada;
import InterfazDeUsuario.InterfazDeJugada;
import InterfazDeUsuario.InterfazDeTurno;
import InterfazDeUsuario.InterfazPrincipal;
import Tateti.InterfazGrafica.InterfazGrafica;

public class Tateti{
//ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
	public static final int CANTIDAD_DE_CARTAS_POR_JUGADOR = 7;
//ATRIBUTOS -----------------------------------------------------------------------------------------------
	
	private Tablero<Ficha> tablero = null;
	private ListaSimplementeEnlazada<Jugador> jugadores = null;
	private EstadoDeJuego estadoDeJuego = null;
	private String ganador = null;
	private InterfazGrafica InterfazGraficaDelTablero = null;
	private Mazo<Carta> mazo = null;
	private int cantidadDeFichasAlineadasParaGanar = 0;

//CONSTRUCTORES -------------------------------------------------------------------------------------------
	
	/**
	 * pre: -
 	 * post: crea el juego tateti con los jugadores y el tablero
	 * @param jugadores: lista de los jugadores
	 * @param tablero: tablero de juego
	 * @throws Exception: valida que tablero y jugadores no sea nulo, y ademas que hayan por lo menos dos jugadores
	 */
	public Tateti(ListaSimplementeEnlazada<Jugador> jugadores, Tablero<Ficha> tablero)throws Exception{
		if(tablero == null) {
			throw new Exception("EL tablero no puede ser vacio");
		}
		if(jugadores == null ) {
			throw new Exception("Los jugadores no pueden ser vacio");
		}
		if(jugadores.getTamanio()<2) {
			throw new Exception("Se necesita por lo menos dos jugadores para jugar");
		}
		
		this.estadoDeJuego = EstadoDeJuego.INICIADO;
		this.tablero = tablero;
		this.cantidadDeFichasAlineadasParaGanar = this.tablero.obtenerLadoMasCorto();
		this.jugadores = jugadores;
		this.InterfazGraficaDelTablero = new InterfazGrafica(tablero);
		this.mazo=new Mazo<Carta>();
		
	}
	
//METODOS DE CLASE ----------------------------------------------------------------------------------------
//METODOS GENERALES ---------------------------------------------------------------------------------------
//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------
	
	/**
	 * pre: -
	 * pos:-
	 * @return: Devuelve true si el juego esta finalizado y false si no lo esta.
	 */
	public boolean juegoTerminado() {
		return this.estadoDeJuego == EstadoDeJuego.FINALIZADO;
	}
	
	
	/**
	 * pre: -
	 * post: Comienza el flujo del juego del tateti si este no esta finalizado: pasar turno,
	 * 		 tirar dado antes de poner o mover ficha,levantar cartas si se puede,
	 * 		 poner fichas si el jugador tiene fichas en mano, mover la ficha cuando el jugador
	 * 		 ya no tenga fichas en mano y sus fichas en el tablero no esten bloqueadas,
	 * 		 tirar un carta si se desea y pasar al siguiente turno.
	 * 		 El juego para cuando haya un ganador.
	 * @throws Exception: 
	 */
	public void jugar() throws Exception {
		if(this.estadoDeJuego != EstadoDeJuego.FINALIZADO) {
		
			ListaCircularSimplementeEnlazada<Turno> turnos = new ListaCircularSimplementeEnlazada<Turno>();
			
			this.jugadores.iniciarCursor();
			while(jugadores.avanzarCursor()) {
				turnos.agregar(new Turno(jugadores.obtenerCursor()));
			}
			InterfazPrincipal.imprimirComienzoDeJuego(this.tablero, this.jugadores);
			
			turnos.iniciarCursor();
			while(turnos.avanzarCursor() && !this.juegoTerminado()){
				Casillero<Ficha> casilleroDestino = null;
				Casillero<Ficha> casilleroOrigen = null;
				Turno turnoActual = turnos.obtenerCursor();
				turnoActual.iniciarTurno();
				InterfazDeTurno.imprimirInformacionDelTurno(turnoActual);
				if(!turnoActual.estaBloqueado() ) {
					
					while(turnoActual.haySubturnos()) {
						turnoActual.utilizarSubturno();
						int cantidadDeCartasALevantar = InterfazDeTurno.tirarDado(turnoActual);
						try {
							this.levantarCartas(turnoActual.getJugador(),cantidadDeCartasALevantar);										
						}catch(Exception e) {
							System.out.println(e.getMessage());
						}
						if(!turnoActual.getJugador().tieneTodasLasFichasBloqueadas()) {
							
							if(turnoActual.getJugador().tieneFichasEnMano()) {
								casilleroDestino = jugadaInicial(this.tablero,turnoActual.getJugador());
							}else {
								
								ListaSimplementeEnlazada<Casillero<Ficha>> casilleros = mover(turnoActual.getJugador()); 
								casilleroOrigen = casilleros.obtener(1);
								casilleroDestino = casilleros.obtener(2);
							}
														
						}
						
						boolean juegaCarta = InterfazDeTurno.quiereJugarCarta();
						Carta cartaAJugar = null;
						if(juegaCarta) {
							cartaAJugar = InterfazDeTurno.elegirCartaAJugar(turnoActual.getJugador());
							if(cartaAJugar!=null) {
								cartaAJugar.getJugada().jugar(this,turnoActual,turnos,casilleroOrigen,casilleroDestino);
								turnoActual.getJugador().getCartas().remover(cartaAJugar);
								this.mazo.dejarUnaCarta(cartaAJugar);								
							}
						}
						if(casilleroDestino !=null) {
							if(casilleroDestino.estaOcupado()) {
								verificarGanador(casilleroDestino, turnoActual.getJugador());							
							}							
						}
						this.InterfazGraficaDelTablero.actualizarTablero();
					}
				}
				turnoActual.terminarTurno();
				if(this.ganador != null) {
					System.out.println("El ganador es... es... es... "+this.ganador+"!!!!");
				}
				
			}
		}else {
			System.out.println("El juego ya ha finalizado!Hora de crear otro juego y seguir jugando :) ");
		}
	}
	
	/**
	 * pre:-
	 * pos: levanta cartas si la cantidad a levantar mas la que tiene el jugador no supera el maximo de cartas en mano,
	 * 		sino imprime un mensaje
	 * @param jugador no puede ser vacio
	 * @param cantidad no puede ser menor o igual a 0
	 * @throws Exception 
	 */
	private void levantarCartas(Jugador jugador, int cantidadDeCartasALevantar) throws Exception {
		if(jugador == null) {
			throw new Exception("El jugador no puede ser vacio");
		}
		if(cantidadDeCartasALevantar < 1) {
			throw new Exception("La cantidad de cartas a levantar no es un numero positivo");
		}
		if(jugador.getCantidadDeCartas()+ cantidadDeCartasALevantar > Tateti.CANTIDAD_DE_CARTAS_POR_JUGADOR) {
			throw new Exception("UPS! No se puede levantar "+cantidadDeCartasALevantar+" porque el jugador superaria la cantidad maxima de cartas en mano que es "+ Tateti.CANTIDAD_DE_CARTAS_POR_JUGADOR);
		}
		this.mazo.sacarCarta();
		for(int i = 0 ; i < cantidadDeCartasALevantar; i++) {
			jugador.agregarCarta(this.mazo.sacarCarta());
		}
	}
	
	/**
	 * pre: el jugador debe tener una ficha por lo menos para mover
	 * @param jugador: jugador que mueve ficha
	 * @return: devuelve una lista de casilleros, el primer elemento de la lista es el casillero donde estaba la ficha a moverse,
	 * 			y el segundo elemento es el casillero destino, a donde se movio la ficha
	 * pos: mueve una ficha del jugador al casillero elegido
	 * @throws Exception: valida que el jugador no sea nulo y que exista y no este ocupado el casillero
	 * 						donde se quiere mover 
	 */
	private ListaSimplementeEnlazada<Casillero<Ficha>> mover(Jugador jugador) throws Exception {
		if(jugador == null) {
			throw new Exception("El jugador no puede ser vacio");
		}
		boolean salir = false;
		while(!salir) {
			try {
				ListaSimplementeEnlazada<Casillero<Ficha>> casilleros = new ListaSimplementeEnlazada<Casillero<Ficha>>();
				Ficha ficha = InterfazDeJugada.elegirUnaFichaDeJugadorEnElTablero(this.tablero, jugador.getFichas()); 
				if(!ficha.estaBloqueado()) {
					
					Movimiento movimiento = InterfazDeTurno.dondeMoverFicha();
					
					Casillero<Ficha> casillero = this.tablero.getCasillero(ficha);
					casilleros.agregar(casillero);
					
					if(casillero.existeElVecino(movimiento, this.tablero)) {
						this.tablero.mover(casillero, casillero.getCasilleroVecino(movimiento, this.tablero), ficha);
						salir=true;
						casilleros.agregar(casillero.getCasilleroVecino(movimiento, this.tablero));
						return casilleros;
					}else {
						throw new Exception("No se puede mover en esa direccion porque excede el tablero");
					}
				}else {
					throw new Exception("La ficha esta bloqueada");
				}
				
			}catch (Exception e){
				System.out.println(e.getMessage());
			}
		}
		return null;
}

	/**
	 * pre: el jugador debe tener por lo menos una ficha con ubicacion en Jugador
	 * @param tablero: tablero donde se ingresa la ficha
	 * @param jugador: jugador que ingresa la ficha
	 * @return: devuelve el casillero donde se ingreso la ficha del jugador
	 * @throws Exception: valida el tablero y el jugador
	 */
	private Casillero<Ficha> jugadaInicial(Tablero<Ficha> tablero, Jugador jugador) throws Exception {
		if(tablero==null) {
			throw new Exception("EL tablero no puede ser vacio");
		}
		if(jugador==null) {
			throw new Exception("El jugador no puede ser vacio");
		}
		Ficha ficha =null;
		ListaSimplementeEnlazada<Ficha> fichas = jugador.getFichas();
		boolean seEncontroFichaRestante = false;
		while(!seEncontroFichaRestante && fichas.avanzarCursor()) {
			if(fichas.obtenerCursor().estaEnJugador()){
				ficha=fichas.obtenerCursor();
				seEncontroFichaRestante=true;
			}
		}
		Casillero<Ficha> casillero = null;
		boolean posicionValida = false;
		while(!posicionValida) {
			try {
				int[] posicionCasillero = InterfazDeTurno.ponerFichaEnCasillero();
				casillero = tablero.getCasillero(posicionCasillero[0], posicionCasillero[1], posicionCasillero[2]);
				this.tablero.ponerFicha(casillero, ficha);
				posicionValida = true;
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return casillero;
	}
		
	/**
	 * pre:-
	 * @param casillero el casillero no debe ser vacio y debe contener una ficha
	 * @throws Exception lanza un exepcion si el casillero es nulo o no tiene ficha
	 * pos:verifica si hay un ganador
	 */
	private void verificarGanador(Casillero<Ficha> casillero, Jugador jugador) throws Exception {
		if(casillero == null) {
			throw new Exception("EL casillero no puede ser nulo");
		}
		
		int cantidadDeFichasSeguidas =  1 + contarFichasSeguidas(casillero.getCasilleroVecino(Direccion.ARRIBA,this.tablero),Direccion.ARRIBA,casillero.getDato()) +
									        contarFichasSeguidas(casillero.getCasilleroVecino(Direccion.ABAJO,this.tablero),Direccion.ABAJO,casillero.getDato());
		if( cantidadDeFichasSeguidas >= this.cantidadDeFichasAlineadasParaGanar) {
			terminarJuego(jugador);
		}
		
		cantidadDeFichasSeguidas = 1 + contarFichasSeguidas(casillero.getCasilleroVecino(Direccion.IZQUIERDA,this.tablero),Direccion.IZQUIERDA,casillero.getDato()) +
				                       contarFichasSeguidas(casillero.getCasilleroVecino(Direccion.DERECHA,this.tablero),Direccion.DERECHA,casillero.getDato());
		if( cantidadDeFichasSeguidas >= this.cantidadDeFichasAlineadasParaGanar) {
			terminarJuego(jugador);
		}
		
		cantidadDeFichasSeguidas = 1 + contarFichasSeguidas(casillero.getCasilleroVecino(Direccion.IZQUIERDA_ARRIBA,this.tablero),Direccion.IZQUIERDA_ARRIBA,casillero.getDato()) +
                                       contarFichasSeguidas(casillero.getCasilleroVecino(Direccion.DERECHA_ABAJO,this.tablero),Direccion.DERECHA_ABAJO,casillero.getDato());
		if( cantidadDeFichasSeguidas >= this.cantidadDeFichasAlineadasParaGanar) {
			terminarJuego(jugador);
		}
		
		cantidadDeFichasSeguidas = 1 + contarFichasSeguidas(casillero.getCasilleroVecino(Direccion.IZQUIERDA_ABAJO,this.tablero),Direccion.IZQUIERDA_ABAJO,casillero.getDato()) +
                                       contarFichasSeguidas(casillero.getCasilleroVecino(Direccion.DERECHA_ARRIBA,this.tablero),Direccion.DERECHA_ARRIBA,casillero.getDato());
		if( cantidadDeFichasSeguidas >= this.cantidadDeFichasAlineadasParaGanar) {
			terminarJuego(jugador);
		}		
		
		cantidadDeFichasSeguidas = 1 + contarFichasSeguidas(casillero.getCasilleroVecino(Direccion.ADELANTE,this.tablero),Direccion.ADELANTE,casillero.getDato()) +
									   contarFichasSeguidas(casillero.getCasilleroVecino(Direccion.ATRAS,this.tablero),Direccion.ATRAS,casillero.getDato());
		if( cantidadDeFichasSeguidas >= this.cantidadDeFichasAlineadasParaGanar) {
			terminarJuego(jugador);
		}
		
		cantidadDeFichasSeguidas = 1 + contarFichasSeguidas(casillero.getCasilleroVecino(Direccion.ADELANTE_ARRIBA,this.tablero),Direccion.ADELANTE_ARRIBA,casillero.getDato()) +
				    			       contarFichasSeguidas(casillero.getCasilleroVecino(Direccion.ATRAS_ABAJO,this.tablero),Direccion.ATRAS_ABAJO,casillero.getDato());
		if( cantidadDeFichasSeguidas >= this.cantidadDeFichasAlineadasParaGanar) {
			terminarJuego(jugador);
		}		
		
		cantidadDeFichasSeguidas = 1 + contarFichasSeguidas(casillero.getCasilleroVecino(Direccion.ADELANTE_ABAJO,this.tablero),Direccion.ADELANTE_ABAJO,casillero.getDato()) +
				   					   contarFichasSeguidas(casillero.getCasilleroVecino(Direccion.ATRAS_ARRIBA,this.tablero),Direccion.ATRAS_ARRIBA,casillero.getDato());
		if( cantidadDeFichasSeguidas >= this.cantidadDeFichasAlineadasParaGanar) {
			terminarJuego(jugador);
		}		
		cantidadDeFichasSeguidas = 1 + contarFichasSeguidas(casillero.getCasilleroVecino(Direccion.ADELANTE_IZQUIERDA,this.tablero),Direccion.ADELANTE_IZQUIERDA,casillero.getDato()) +
				   contarFichasSeguidas(casillero.getCasilleroVecino(Direccion.ATRAS_DERECHA,this.tablero),Direccion.ATRAS_DERECHA,casillero.getDato());
		if( cantidadDeFichasSeguidas >= this.cantidadDeFichasAlineadasParaGanar) {
		terminarJuego(jugador);
		}	
		cantidadDeFichasSeguidas = 1 + contarFichasSeguidas(casillero.getCasilleroVecino(Direccion.ADELANTE_DERECHA,this.tablero),Direccion.ADELANTE_DERECHA,casillero.getDato()) +
				   contarFichasSeguidas(casillero.getCasilleroVecino(Direccion.ATRAS_IZQUIERDA,this.tablero),Direccion.ATRAS_IZQUIERDA,casillero.getDato());
		if( cantidadDeFichasSeguidas >= this.cantidadDeFichasAlineadasParaGanar) {
		terminarJuego(jugador);
		}	
		
		
		cantidadDeFichasSeguidas = 1 + contarFichasSeguidas(casillero.getCasilleroVecino(Direccion.ADELANTE_ARRIBA_DERECHA,this.tablero),Direccion.ADELANTE_ARRIBA_DERECHA,casillero.getDato()) +
				                       contarFichasSeguidas(casillero.getCasilleroVecino(Direccion.ATRAS_ABAJO_IZQUIERDA,this.tablero),Direccion.ATRAS_ABAJO_IZQUIERDA,casillero.getDato());
		if( cantidadDeFichasSeguidas >= this.cantidadDeFichasAlineadasParaGanar) {
			terminarJuego(jugador);
		}	
		cantidadDeFichasSeguidas = 1 + contarFichasSeguidas(casillero.getCasilleroVecino(Direccion.ADELANTE_ARRIBA_IZQUIERDA,this.tablero),Direccion.ADELANTE_ARRIBA_IZQUIERDA,casillero.getDato()) +
				                       contarFichasSeguidas(casillero.getCasilleroVecino(Direccion.ATRAS_ABAJO_DERECHA,this.tablero),Direccion.ATRAS_ABAJO_DERECHA,casillero.getDato());
		if( cantidadDeFichasSeguidas >= this.cantidadDeFichasAlineadasParaGanar) {
			terminarJuego(jugador);
		}		
		cantidadDeFichasSeguidas = 1 + contarFichasSeguidas(casillero.getCasilleroVecino(Direccion.ADELANTE_ABAJO_IZQUIERDA,this.tablero),Direccion.ADELANTE_ABAJO_IZQUIERDA,casillero.getDato()) +
									   contarFichasSeguidas(casillero.getCasilleroVecino(Direccion.ATRAS_ARRIBA_DERECHA,this.tablero),Direccion.ATRAS_ARRIBA_DERECHA,casillero.getDato());
		if( cantidadDeFichasSeguidas >= this.cantidadDeFichasAlineadasParaGanar) {
			terminarJuego(jugador);
		}		
		cantidadDeFichasSeguidas = 1 + contarFichasSeguidas(casillero.getCasilleroVecino(Direccion.ADELANTE_ABAJO_DERECHA,this.tablero),Direccion.ADELANTE_ABAJO_DERECHA,casillero.getDato()) +
									   contarFichasSeguidas(casillero.getCasilleroVecino(Direccion.ATRAS_ARRIBA_IZQUIERDA,this.tablero),Direccion.ATRAS_ARRIBA_IZQUIERDA,casillero.getDato());
		if( cantidadDeFichasSeguidas >= this.cantidadDeFichasAlineadasParaGanar) {
			terminarJuego(jugador);
		}		
	}
	
	
	/**
	 * pre:-
	 * pos:devuelve la cantidad de fichas seguidas que hay en la direccion dada
	 * @throws Exception
	 */
	private int contarFichasSeguidas(Casillero<Ficha> casillero,Direccion direccion, Ficha ficha) throws Exception {
		
		if(casillero == null ||
				casillero.getDato() == null) {
			return 0;
		}
		if(casillero.getDato().equals(ficha)){
			return 1 + contarFichasSeguidas(casillero.getCasilleroVecino(direccion,this.tablero),direccion,ficha);
		}
		return 0;
	}
	
	/**
	 * pre: -
	 * post: imprime la lista de jugadores
	 */
	public void imprimirListaDejugadores() {
		Lista<Jugador> listaDeJugadores = getJugadores(); 
		listaDeJugadores.iniciarCursor();
		while(listaDeJugadores.avanzarCursor()) {
			System.out.println(listaDeJugadores.obtenerCursor().getNumeroDejugador()
					+") "+listaDeJugadores.obtenerCursor().getNombre());
		}
		return;
	}
	
	/**
	 * pre: -
	 * @param idJugador: id del jugador
	 * @return: devuelve el jugador elegido por id
	 * @throws Exception: valida el id ingresado
	 */
	public Jugador obtenerJugadores(int idJugador) throws Exception {
		if(idJugador>getJugadores().getTamanio() || idJugador<0) {
			throw new Exception("el id esta fuera de rango");
		}
		Lista<Jugador> listaDeJugadores = getJugadores(); 
		listaDeJugadores.iniciarCursor();
		while(listaDeJugadores.avanzarCursor()) {
			if(listaDeJugadores.obtenerCursor().getNumeroDejugador()==idJugador) {
				return listaDeJugadores.obtenerCursor();
			}
		}
		return null;
	}

//GETTERS SIMPLES -----------------------------------------------------------------------------------------
	
	/**
	 * pre: -
	 * @return: devuelve el tablero 
	 */
	public Tablero<Ficha> getTablero() {
		return this.tablero;
	}

	/**
	 * pre: -
	 * @return: devuelve la cantidad de fichas que se deben alinear para ganar 
	 */
	public int getCantidadDeFichasAlineadasParaGanar() {
		return this.cantidadDeFichasAlineadasParaGanar;
	}

	/**
	 * pre: -
	 * @return: devuelve la lista de jugadores 
	 */
	public ListaSimplementeEnlazada<Jugador> getJugadores() {
		return this.jugadores;
	}
	

	/**
	 * pre: -
	 * @return: devuelve el mazo 
	 */
	public Mazo<Carta> getMazo() {
		return this.mazo;
	}

	/**
	 * pre: -
	 * @return: devuelve el estado de juego 
	 */
	public EstadoDeJuego getEstadoDeJuego() {
		return this.estadoDeJuego;
	}

	public void terminarJuego(Jugador jugador) {
		this.ganador = jugador.getNombre(); 
		this.estadoDeJuego=EstadoDeJuego.FINALIZADO;
	}

	/**
	 * pre: -	
	 * @return: devuelve el nombre del jugador ganador
	 */
	public String getGanador() {
		return this.ganador ;
	}
	
//SETTERS SIMPLES -----------------------------------------------------------------------------------------	
}


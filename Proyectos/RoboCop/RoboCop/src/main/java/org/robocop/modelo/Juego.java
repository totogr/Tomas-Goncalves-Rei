package org.robocop.modelo;

import org.robocop.modelo.Robot.Robot;
import org.robocop.modelo.Robot.RobotLento;
import org.robocop.modelo.Robot.RobotRapido;

import java.util.ArrayList;
import java.util.Random;

public class Juego {

    private final int TAMAÑO_DE_TABLERO;
    private final int RECURSOS = 6;
    private final int TELETRANSPORTES_SEGUROS = 3;

    private Tablero tablero;
    private Jugador jugador;
    private ArrayList<Robot> robots;

    private int nivel = 1;
    private int recursosRecolectados = 0;
    private int cantidadDeRobots = 3;

    public Juego(int tamanioTablero) {
        TAMAÑO_DE_TABLERO = tamanioTablero;
        crearJugador();
        crearTableroDeJuego();
    }

    public boolean nivelGanado() {
        return jugador.getRecursosRecolectados() == RECURSOS;
    }

    private void crearTableroDeJuego(){
        crearTablero(TAMAÑO_DE_TABLERO, TAMAÑO_DE_TABLERO);
        ubicarRecursos(RECURSOS);
        crearRobots();
    }

    public void avanzarNivel() {
        guardarPuntuacion();
        jugador.vaciarRecursos();
        jugador.agregarTeletransportesSeguro(3);
        cantidadDeRobots++;
        Posicion posicionInicial = new Posicion(this.tablero.getFilas()/2, this.tablero.getColumnas()/2);
        jugador.teletransportar(posicionInicial);
        crearTableroDeJuego();
        nivel++;
    }

    private void guardarPuntuacion() {
        recursosRecolectados += jugador.getRecursosRecolectados();
    }

    public void crearRobots(){
        robots = new ArrayList<>();
        Robot robot;
        for (int i = 0; i < cantidadDeRobots; i++) {
            Random rand = new Random();
            Posicion posicion = new Posicion(rand.nextInt(tablero.getFilas()), rand.nextInt(tablero.getColumnas()));
            if (!tablero.getPosicionesBloqueadas().contains(posicion)) {
                if (Math.random() < 0.5) {
                    robot = new RobotLento(posicion);
                } else {
                    robot = new RobotRapido(posicion);
                }
                robots.add(robot);
            }
        }
    }

    public ArrayList<Robot> getRobots(){
        return robots;
    }

    public void crearTablero(int filas, int columnas) {
        this.tablero = new Tablero(filas, columnas);
    }

    public void crearJugador() {
        Posicion posicionInicial = new Posicion(TAMAÑO_DE_TABLERO/2, TAMAÑO_DE_TABLERO/2);
        this.jugador = new Jugador(posicionInicial);
        jugador.agregarTeletransportesSeguro(TELETRANSPORTES_SEGUROS);
    }

    public void moverJugador(Jugador jugador, int dx, int dy) {
        this.tablero.moverJugador(jugador, dx, dy);
    }
    public void moverRobots() {
        this.tablero.moverRobots(this.robots,this.jugador);
    }

    public void ubicarRecursos(int n){
        tablero.ubicarRecursos(n);
    }

    public int getRecursosRecolectados(){
        return recursosRecolectados;
    }
    private int getRecursosRecolectadosJugador(){
        return this.jugador.getRecursosRecolectados();
    }
    public int getRecursosTotales(){
        return getRecursosRecolectados()+getRecursosRecolectadosJugador();
    }

    public Tablero getTablero() {
        return tablero;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public int getFilaDeJugador() {
        return jugador.getPosicion().getFila();
    }

    public int getColumnaDeJugador() {
        return jugador.getPosicion().getColumna();
    }

    public int getFilaDeRobot(Robot robot) {
        return robot.getPosicion().getFila();
    }

    public int getColumnaDeRobot(Robot robot) {
        return robot.getPosicion().getColumna();
    }

    public int getNivel() {
        return nivel;
    }

    public void usarTeletransportesSeguros(Posicion posicion){
        this.tablero.teletransportarSeguroJugador(this.jugador, posicion);
    }

    public void usarTeletrasnportesAleatorios(){
        Random rand = new Random();
        Posicion posicion = new Posicion(rand.nextInt(tablero.getFilas()), rand.nextInt(tablero.getColumnas()));
        this.tablero.teletransportarAleatorioJugador(this.jugador, posicion);
    }
}
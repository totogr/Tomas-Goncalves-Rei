package org.robocop.modelo;


public class Jugador {
    private Posicion posicion;
    private int teletransportesSeguros;
    private int recursosRecolectados;
    private EstadoJugador estado;


    public Jugador(Posicion posicionInicial) {
        this.posicion = posicionInicial;
        this.teletransportesSeguros = 0;
        this.recursosRecolectados = 0;
        this.estado = EstadoJugador.VIVO;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public EstadoJugador  getEstado() {
        return estado;
    }

    public boolean estaVivo() {
        return estado == EstadoJugador.VIVO;
    }

    public void recolectarRecurso() {
        recursosRecolectados++;
    }

    public int getRecursosRecolectados() {
        return recursosRecolectados;
    }

    public void vaciarRecursos(){ recursosRecolectados=0; }

    public void mover(int deltaFila, int deltaColumna) {
        if (estado == EstadoJugador.VIVO) {
            posicion = posicion.mover(deltaFila, deltaColumna);
        }
    }

    public void teletransportar(Posicion nuevaPosicion) {
        this.posicion = nuevaPosicion;
    }

    public void agregarTeletransportesSeguro(int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            teletransportesSeguros++;
        }
    }

    public boolean puedeTeletransportarse() {
        return teletransportesSeguros > 0;
    }

    public void usarTeletransporteSeguro(Posicion nuevaPosicion) {
        if (teletransportesSeguros > 0) {
            teletransportar(nuevaPosicion);
            teletransportesSeguros--;
        }
    }

    public int getTeletransportesSeguros(){ return teletransportesSeguros; }

    public void morir() {
        this.estado = EstadoJugador.MUERTO;
    }

    @Override
    public String toString() {
        return "Jugador{" +
                "posicion=" + posicion +
                ", estado=" + estado +
                ", recursos=" + recursosRecolectados +
                ", teletransportesSeguros=" + teletransportesSeguros +
                '}';
    }

}
package org.robocop.modelo;

import java.util.List;

public class Posicion {
    private int fila;
    private int columna;

    public Posicion(int fila, int columna){
        this.fila = fila;
        this.columna = columna;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Posicion posicion = (Posicion) obj;
        return fila == posicion.fila && columna == posicion.columna;
    }

    @Override
    public int hashCode() {
        return 31 * fila + columna;
    }

    @Override
    public String toString() {
        return "[" + fila + ", " + columna + "]";
    }

    public double distancia(Posicion pos) {
        int deltaX = this.fila - pos.fila;
        int deltaY = this.columna - pos.columna;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public Posicion  mover(int deltaFila, int deltaColumna) {
        return new Posicion(this.fila + deltaFila, this.columna + deltaColumna);
    }

    public List<Posicion> vecinos() {
        return List.of(
                mover(-1, 0),  // norte
                mover(-1, 1),  // noreste
                mover(0, 1),   // este
                mover(1, 1),   // sureste
                mover(1, 0),   // sur
                mover(1, -1),  // suroeste
                mover(0, -1),  // oeste
                mover(-1, -1)  // noroeste
        );
    }
}

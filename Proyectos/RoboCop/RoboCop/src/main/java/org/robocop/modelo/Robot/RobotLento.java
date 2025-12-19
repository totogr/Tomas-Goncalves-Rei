package org.robocop.modelo.Robot;


import org.robocop.modelo.Posicion;

import java.util.*;


public class RobotLento implements Robot {
    private Posicion posicion;
    private EstadoRobot estado;

    public RobotLento(Posicion posicionInicial) {
        this.posicion = posicionInicial;
        this.estado = EstadoRobot.INTACTO;
    }

    @Override
    public Posicion getPosicion() {
        return posicion;
    }

    @Override
    public EstadoRobot getEstado() {
        return estado;
    }

    @Override
    public void destruir() {
        this.estado = EstadoRobot.INCENDIADO;
    }

    @Override
    public List<Posicion> calcularTrayectoria(Posicion objetivo, List<Posicion> bloqueadas) {
        if (estado != EstadoRobot.INTACTO) return Collections.emptyList();
        if (posicion.equals(objetivo)) return Collections.emptyList();
        List<Posicion> candidatas = new ArrayList<>();

        for (int df = -1; df <= 1; df++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (df == 0 && dc == 0) continue;
                Posicion nueva = posicion.mover(df, dc);
                if (!bloqueadas.contains(nueva)) {
                    candidatas.add(nueva);
                }
            }
        }

        candidatas.sort(Comparator.comparingDouble(p -> p.distancia(objetivo)));

        if (!candidatas.isEmpty()) {
            return List.of(candidatas.get(0));
        }

        return Collections.emptyList();
    }

    @Override
    public void moverA(Posicion nuevaPosicion) {
        if (estado == EstadoRobot.INTACTO) {
            this.posicion = nuevaPosicion;
        }
    }

    @Override
    public String toString() {
        return "RobotLento{" +
                "posicion=" + posicion +
                ", estado=" + estado +
                '}';
    }
    @Override
    public TipoRobot getTipoRobot(){
        return TipoRobot.LENTO;
    }
}
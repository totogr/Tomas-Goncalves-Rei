package org.robocop.modelo;

import org.robocop.modelo.Celda.Celda;
import org.robocop.modelo.Celda.CeldaConRecurso;
import org.robocop.modelo.Celda.CeldaIncendiada;
import org.robocop.modelo.Celda.CeldaLibre;
import org.robocop.modelo.Robot.EstadoRobot;
import org.robocop.modelo.Robot.Robot;

import java.util.*;


public class Tablero {
    private final int filas;
    private final int columnas;
    private final Celda[][] celdas;

    public Tablero(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.celdas = new Celda[filas][columnas];
        inicializarCeldas();
    }

    private void inicializarCeldas() {
        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                celdas[fila][columna] = new CeldaLibre(); // Todo empieza libre
            }
        }
    }
    public boolean esValida(Posicion posicion) {
        return posicion.getFila() >= 0 && posicion.getFila() < filas
                && posicion.getColumna() >= 0 && posicion.getColumna() < columnas;
    }

    public Celda getCelda(Posicion posicion) {
        if (!esValida(posicion)) {
            throw new IllegalArgumentException("Posición fuera del tablero");
        }
        return celdas[posicion.getFila()][posicion.getColumna()];
    }

    public void setCelda(Posicion posicion, Celda celda) {
        if (!esValida(posicion)) {
            throw new IllegalArgumentException("Posición fuera del tablero");
        }
        celdas[posicion.getFila()][posicion.getColumna()] = celda;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                sb.append(celdas[fila][columna].toString()+"\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public void moverJugador(Jugador jugador, int deltaFila, int deltaColumna) {
        if (!jugador.estaVivo()) return;

        Posicion nuevaPosicion = jugador.getPosicion().mover(deltaFila, deltaColumna);
        if (!esMovimientoValidoParaJugador(nuevaPosicion)) return;

        ejecutarMovimientoJugador(jugador, nuevaPosicion, deltaFila, deltaColumna);
    }

    private boolean esMovimientoValidoParaJugador(Posicion pos) {
        return esValida(pos) && getCelda(pos).esTransitablePorJugador();
    }

    private void ejecutarMovimientoJugador(Jugador jugador, Posicion nuevaPos, int df, int dc) {
        jugador.mover(df, dc);
        Celda celdaDestino = getCelda(nuevaPos);
        Celda nuevaCelda = celdaDestino.interactuarConJugador(jugador);
        setCelda(nuevaPos, nuevaCelda);
    }

    public void teletransportarSeguroJugador(Jugador jugador, Posicion destino) {
        if (!jugador.estaVivo()) return;
        if (!jugador.puedeTeletransportarse()) return;

        try {
            if (!esDestinoValidoParaTeletransporte(destino)) return;
            ejecutarTeletransporteSeguro(jugador, destino);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private boolean esDestinoValidoParaTeletransporte(Posicion destino) throws Exception {
        if (!esValida(destino)) {
            throw new Exception("Destino fuera del tablero. Teletransportación fallida.");
        }
        if (!getCelda(destino).esTransitablePorJugador()) {
            throw new Exception("Destino no transitable para el jugador. Teletransportación fallida.");
        }
        return true;
    }

    private void ejecutarTeletransporteSeguro(Jugador jugador, Posicion destino) {
        jugador.usarTeletransporteSeguro(destino);
        Celda nuevaCelda = getCelda(destino).interactuarConJugador(jugador);
        setCelda(destino, nuevaCelda);
    }

    public void teletransportarAleatorioJugador(Jugador jugador, Posicion destino) {
        if (!jugador.estaVivo()) return;
        if (!esDestinoValido(destino)) return;

        ejecutarTeletransporteAleatorio(jugador, destino);
    }
    private boolean esDestinoValido(Posicion destino) {
        if (!esValida(destino)) {return false;}
        return true;
    }

    private void ejecutarTeletransporteAleatorio(Jugador jugador, Posicion destino) {
        jugador.teletransportar(destino);
        Celda celdaDestino = getCelda(destino);
        Celda nuevaCelda = celdaDestino.interactuarConJugador(jugador);
        setCelda(destino, nuevaCelda);
    }

    public List<Posicion> getPosicionesBloqueadas() {
        return getPosicionesBloqueadasConBordes();
    }

    private List<Posicion> getPosicionesBloqueadasConBordes() {
        List<Posicion> bloqueadas = new ArrayList<>();

        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < columnas; col++) {
                Posicion pos = new Posicion(fila, col);
                if (!getCelda(pos).esTransitablePorRobot()) {
                    bloqueadas.add(pos);
                }
            }
        }
        for (int fila = -1; fila <= filas; fila++) {
            bloqueadas.add(new Posicion(fila, -1));
            bloqueadas.add(new Posicion(fila, columnas));
        }
        for (int col = -1; col <= columnas; col++) {
            bloqueadas.add(new Posicion(-1, col));
            bloqueadas.add(new Posicion(filas, col));
        }
        return bloqueadas;
    }

    public void moverRobot(Robot robot, Jugador jugador) {
        if (robot.getEstado() != EstadoRobot.INTACTO) return;

        List<Posicion> bloqueadas = getPosicionesBloqueadas();
        List<Posicion> trayectoria = robot.calcularTrayectoria(jugador.getPosicion(), bloqueadas);

        avanzarPorTrayectoria(robot, trayectoria);
    }

    private void avanzarPorTrayectoria(Robot robot, List<Posicion> trayectoria) {
        for (Posicion paso : trayectoria) {
            if (!esValida(paso)) break;

            Celda celda = getCelda(paso);
            if (!celda.esTransitablePorRobot()) break;

            robot.moverA(paso);

            Celda nueva = celda.interactuarConRobot(robot);
            setCelda(paso, nueva);

            if (robot.getEstado() != EstadoRobot.INTACTO) break;
        }
    }

    public void moverRobots(List<Robot> robots, Jugador jugador) {
        for (Robot robot : robots) {
            moverRobot(robot, jugador);
        }
        gestionarColisionesEntreRobots(robots);
        verificarMuerteJugador(robots, jugador);
    }

    private void gestionarColisionesEntreRobots(List<Robot> robots) {
        Map<Posicion, List<Robot>> mapaColisiones = new HashMap<>();

        for (Robot robot : robots) {
            if (robot.getEstado() != EstadoRobot.INTACTO) continue;
            mapaColisiones
                    .computeIfAbsent(robot.getPosicion(), k -> new ArrayList<>())
                    .add(robot);
        }

        for (Map.Entry<Posicion, List<Robot>> entry : mapaColisiones.entrySet()) {
            List<Robot> enMismaPos = entry.getValue();
            if (enMismaPos.size() > 1) {
                for (Robot r : enMismaPos) {
                    r.destruir();
                }
                setCelda(entry.getKey(), new CeldaIncendiada());
            }
        }
    }
    private void verificarMuerteJugador(List<Robot> robots, Jugador jugador) {
        for (Robot robot : robots) {
            if (robot.getEstado() == EstadoRobot.INTACTO &&
                    robot.getPosicion().equals(jugador.getPosicion())) {
                jugador.morir();
            }
        }
    }

    public int getFilas() {
        return this.filas;
    }
    public int getColumnas() {
        return this.columnas;
    }

    public void ubicarRecursos(int cantidad) {
        Random random = new Random();
        List<Posicion> ubicaciones = new ArrayList<>();

        while (ubicaciones.size() < cantidad) {
            Posicion nueva = generarPosicionAleatoria(random);

            if (!sePuedeUbicarRecursoEn(nueva)) continue;
            if (!estaSuficientementeLejos(nueva, ubicaciones)) continue;

            setCelda(nueva, new CeldaConRecurso());
            ubicaciones.add(nueva);
        }
    }
    private Posicion generarPosicionAleatoria(Random random) {
        int fila = random.nextInt(filas);
        int columna = random.nextInt(columnas);
        return new Posicion(fila, columna);
    }

    private boolean sePuedeUbicarRecursoEn(Posicion pos) {
        return getCelda(pos).sePuedeUbicarRecurso();
    }

    private boolean estaSuficientementeLejos(Posicion nueva, List<Posicion> ubicaciones) {
        for (Posicion otra : ubicaciones) {
            int distancia = Math.abs(nueva.getFila() - otra.getFila()) +
                    Math.abs(nueva.getColumna() - otra.getColumna());
            if (distancia < 3) {
                return false;
            }
        }
        return true;
    }


}

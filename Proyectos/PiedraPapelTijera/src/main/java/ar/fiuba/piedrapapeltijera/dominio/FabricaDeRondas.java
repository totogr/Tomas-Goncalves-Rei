package ar.fiuba.piedrapapeltijera.dominio;

public class FabricaDeRondas {
    private final TablaDeResultados tablaDeResultados;

    public FabricaDeRondas(TablaDeResultados tablaDeResultados) { this.tablaDeResultados = tablaDeResultados; }
    public Ronda crearRonda(Jugada jugadaDelUsuario, Jugada jugadaDeLaComputadora) { return new Ronda(jugadaDelUsuario, jugadaDeLaComputadora, tablaDeResultados.obtenerResultado(jugadaDelUsuario, jugadaDeLaComputadora)); }
}

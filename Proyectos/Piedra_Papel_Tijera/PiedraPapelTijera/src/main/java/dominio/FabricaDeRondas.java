package dominio;

public class FabricaDeRondas {
    public Ronda crearRonda(Jugada jugadaDelUsuario, Jugada jugadaDeLaComputadora) { return new Ronda(jugadaDelUsuario, jugadaDeLaComputadora, jugadaDelUsuario.resultadoContra(jugadaDeLaComputadora), jugadaDelUsuario.vs(jugadaDeLaComputadora)); }
}

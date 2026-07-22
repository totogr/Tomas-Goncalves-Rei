package dominio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MarcadorTest {
    @Test void deberiaSumarUnaVictoria() { assertEquals(new Marcador(1, 0, 0), Marcador.inicial().actualizarCon(Resultado.GANADA)); }
    @Test void deberiaSumarUnaDerrota() { assertEquals(new Marcador(0, 1, 0), Marcador.inicial().actualizarCon(Resultado.PERDIDA)); }
    @Test void deberiaSumarUnEmpate() { assertEquals(new Marcador(0, 0, 1), Marcador.inicial().actualizarCon(Resultado.EMPATE)); }
}

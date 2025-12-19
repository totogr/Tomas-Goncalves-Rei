package Jugadas;

import Cartas.Carta;
import Estructuras.ListaCircularSimplementeEnlazada;
import InterfazDeUsuario.InterfazDeJugada;
import Tateti.Casillero;
import Tateti.Ficha;
import Tateti.Tateti;
import Tateti.Turno;

public class JugadaAnularCasillero extends Jugada{
	
	public JugadaAnularCasillero(Carta carta) {
		super(carta);
	}
	
	public void jugar(Tateti tateti,
			Turno turnoActual,
			ListaCircularSimplementeEnlazada<Turno> turnos,
			Casillero<Ficha> casilleroOrigen,
			Casillero<Ficha> casilleroDestino) throws Exception {
		Casillero<Ficha> casilleroAAnular = null;
		boolean casilleroAnulado = false;
		while(!casilleroAnulado) {
			try {
				casilleroAAnular=InterfazDeJugada.preguntarCasilleroAAnular(tateti.getTablero());
				casilleroAAnular.anularCasillero();
				System.out.println("El " + casilleroAAnular + " fue anulado");
				casilleroAnulado = true;
			}catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
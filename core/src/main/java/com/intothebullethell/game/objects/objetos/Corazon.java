package com.intothebullethell.game.objects.objetos;

import com.intothebullethell.game.entidades.Jugador;
import com.intothebullethell.game.globales.RecursoRuta;
import com.intothebullethell.game.globales.SonidoRuta;

public class Corazon extends Agarrable {

    public Corazon() {
        super(RecursoRuta.CAJA_VIDA, SonidoRuta.CAJA_VIDA);
    }

    @Override
    public void aplicarEfecto(Jugador jugador) {
        jugador.aumentarVida(2); 
        efectoSonido.reproducirSonido();
        this.recogido = true; 
    }

	@Override
	public String getTipo() {
		return "corazon";
	}
}

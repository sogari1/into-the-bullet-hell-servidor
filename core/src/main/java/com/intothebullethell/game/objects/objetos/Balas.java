package com.intothebullethell.game.objects.objetos;

import com.intothebullethell.game.entidades.Jugador;
import com.intothebullethell.game.globales.RecursoRuta;
import com.intothebullethell.game.globales.SonidoRuta;

public class Balas extends Agarrable {

	public Balas() {
		super(RecursoRuta.CAJA_MUNICION, SonidoRuta.CAJA_MUNICION);
	}
    @Override
    public void aplicarEfecto(Jugador jugador) {
        jugador.getArmaEquipada().aumentarBalasEnReserva(30);
        efectoSonido.reproducirSonido();
        this.recogido = true; 
        
    }
	@Override
	public String getTipo() {
		return "balas";
	}
}

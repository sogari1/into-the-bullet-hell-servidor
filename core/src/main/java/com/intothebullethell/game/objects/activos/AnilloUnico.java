package com.intothebullethell.game.objects.activos;

import com.intothebullethell.game.entidades.Jugador;
import com.intothebullethell.game.globales.RecursoRuta;
import com.intothebullethell.game.globales.SonidoRuta;

public class AnilloUnico extends Activo {

	public AnilloUnico() {
		   super("Anillo Unico", 20.0f, RecursoRuta.ANILLO_UNICO, SonidoRuta.ANILLO_UNICO);
	}

	@Override
	protected void aplicarEfecto(Jugador jugador) {
		jugador.setVida(-8);
		jugador.getArmaEquipada().aumentarDaño(12);  
	}

	@Override
	protected void revertirEfecto(Jugador jugador) {
		jugador.getArmaEquipada().aumentarDaño(-12);  	
	}

}

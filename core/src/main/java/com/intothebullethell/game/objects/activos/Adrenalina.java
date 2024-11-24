package com.intothebullethell.game.objects.activos;

import com.intothebullethell.game.entidades.Jugador;
import com.intothebullethell.game.globales.RecursoRuta;
import com.intothebullethell.game.globales.SonidoRuta;

public class Adrenalina extends Activo {

    private int incrementoVelocidad = 50;

    public Adrenalina() {
        super("Adrenalina", 7.0f, RecursoRuta.ADRENALINA, SonidoRuta.ADRENALINA);
    }

	@Override
	protected void aplicarEfecto(Jugador jugador) {
		jugador.aumentarVelocidad(incrementoVelocidad);
		System.out.println("Velocidad aumentada en " + incrementoVelocidad + " por " + getTiempoDeUso() + " segundos.");
	}

	@Override
	protected void revertirEfecto(Jugador jugador) {
		jugador.aumentarVelocidad(-incrementoVelocidad);
		System.out.println("Velocidad restaurada.");
	}
}

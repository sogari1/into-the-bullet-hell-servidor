package com.intothebullethell.game.objects.activos;

import com.intothebullethell.game.entidades.Jugador;
import com.intothebullethell.game.globales.RecursoRuta;
import com.intothebullethell.game.globales.SonidoRuta;

public class Sanguche extends Activo {

    private int incrementoDaño = 10;

    public Sanguche() {
        super("Sanguche", 7.0f, RecursoRuta.SANGUCHE, SonidoRuta.SANGUCHE);
    }

    @Override
    protected void aplicarEfecto(Jugador jugador) {
        jugador.getArmaEquipada().aumentarDaño(incrementoDaño);  
        System.out.println("Daño aumentado en " + incrementoDaño + " por " + getTiempoDeUso() + " segundos.");
    }

	@Override
	protected void revertirEfecto(Jugador jugador) {
		jugador.getArmaEquipada().aumentarDaño(-incrementoDaño);  
		System.out.println("Daño restaurado.");
	}
}

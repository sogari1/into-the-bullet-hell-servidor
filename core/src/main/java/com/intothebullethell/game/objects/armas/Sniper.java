package com.intothebullethell.game.objects.armas;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.intothebullethell.game.entidades.Proyectil;
import com.intothebullethell.game.globales.NetworkData;
import com.intothebullethell.game.globales.RecursoRuta;
import com.intothebullethell.game.globales.SonidoRuta;

public class Sniper extends Arma {

	public Sniper() {
		super("Sniper", 400, 4, 1.0f, 10, 30,  RecursoRuta.PROYECTIL_SNIPER, RecursoRuta.PROYECTIL_SNIPER, RecursoRuta.ARMA_SNIPER, SonidoRuta.DISPARO_SNIPER);
	}
	@Override
	public void disparar(Vector2 position, Vector2 target, List<Proyectil> proyectiles) {
		   proyectiles.add(new Proyectil(proyectilTexturaUno, proyectilTexturaDos, position, target, proyectilVelocidad, daño, true, 11, 11));
	        NetworkData.serverThread.enviarMensajeATodos("proyectil!crear!" +  getNombre());
	}

}

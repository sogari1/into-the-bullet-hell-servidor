package com.intothebullethell.game.objects.armas;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.intothebullethell.game.entidades.Proyectil;
import com.intothebullethell.game.globales.NetworkData;
import com.intothebullethell.game.globales.RecursoRuta;
import com.intothebullethell.game.globales.SonidoRuta;

public class Blaster extends Arma {

	public Blaster() {
		super("Blaster", 300, 6, 0.6f, 10, 40,  RecursoRuta.PROYECTIL_BLASTER_1, RecursoRuta.PROYECTIL_BLASTER_2, RecursoRuta.ARMA_BLASTER, SonidoRuta.DISPARO_BLASTER);
	}
	@Override
	public void disparar(Vector2 position, Vector2 target, List<Proyectil> proyectiles) {
		   proyectiles.add(new Proyectil(proyectilTexturaUno, proyectilTexturaDos, position, target, proyectilVelocidad, daño, true, 10, 10));
	        NetworkData.serverThread.enviarMensajeATodos("proyectil!crear!" +  getNombre());
	}

}

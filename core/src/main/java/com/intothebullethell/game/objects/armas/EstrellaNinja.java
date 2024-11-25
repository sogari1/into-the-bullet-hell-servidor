package com.intothebullethell.game.objects.armas;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.intothebullethell.game.entidades.Proyectil;
import com.intothebullethell.game.globales.NetworkData;
import com.intothebullethell.game.globales.RecursoRuta;
import com.intothebullethell.game.globales.SonidoRuta;

public class EstrellaNinja extends Arma {

	public EstrellaNinja() {
		super("Estrella ninja", 300, 2, 0.5f, 6, 32,  RecursoRuta.PROYECTIL_ESTRELLA_1, RecursoRuta.PROYECTIL_ESTRELLA_2, RecursoRuta.ARMA_ESTRELLA, SonidoRuta.DISPARO_ESTRELLA);
	}
	@Override
	public void disparar(Vector2 position, Vector2 target, List<Proyectil> proyectiles) {
		   proyectiles.add(new Proyectil(proyectilTexturaUno, proyectilTexturaDos, position, target, proyectilVelocidad, daño, true, 2, 2));
	        NetworkData.serverThread.enviarMensajeATodos("proyectil!crear!" +  getNombre());
	}

}

package com.intothebullethell.game.objects.armas;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.intothebullethell.game.entidades.Proyectil;
import com.intothebullethell.game.globales.NetworkData;
import com.intothebullethell.game.globales.RecursoRuta;
import com.intothebullethell.game.globales.SonidoRuta;

public class BFG9000 extends Arma {

	public BFG9000() {
		super("BFG 9000", 200, 20, 4.0f, 1, 4,  RecursoRuta.PROYECTIL_BFG9000_1, RecursoRuta.PROYECTIL_BFG9000_2, RecursoRuta.ARMA_BGF9000, SonidoRuta.DISPARO_BFG9000);
	}
	@Override
	public void disparar(Vector2 position, Vector2 target, List<Proyectil> proyectiles) {
		   proyectiles.add(new Proyectil(proyectilTexturaUno, proyectilTexturaDos, position, target, proyectilVelocidad, daño, true, 2, 2));
	        NetworkData.serverThread.enviarMensajeATodos("proyectil!crear!" +  getNombre());
	}

}

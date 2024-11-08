package com.intothebullethell.game.entidades;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.intothebullethell.game.globales.NetworkData;
import com.intothebullethell.game.globales.RecursoRuta;
import com.intothebullethell.game.managers.EntidadManager;
import com.intothebullethell.game.managers.ProyectilManager;

public class EnemigoFuerte extends Enemigo {
	public EnemigoFuerte(Jugador[] jugadores,  List<Enemigo> enemigos, EntidadManager entidadManager) {
		super(RecursoRuta.ENEMIGO, 10, 18, 14f, 2, 50, RecursoRuta.PROYECTIL_ESCOPETA, jugadores, enemigos, entidadManager);
	}
	@Override
	public void atacar() {
	    Jugador jugadorObjetivo = obtenerJugadorMasCercano();
	    if (jugadorObjetivo != null) {
	        Vector2 position = new Vector2(getX() + getWidth() / 2, getY() + getHeight() / 2);
	        Vector2 target = new Vector2(jugadorObjetivo.getX() + jugadorObjetivo.getWidth() / 2, jugadorObjetivo.getY() + jugadorObjetivo.getHeight() / 2);
	        Vector2 direction = target.cpy().sub(position).nor();

	        for (int i = -2; i <= 2; i++) {
	            Vector2 spreadDirection = new Vector2(direction).rotateDeg(i * 10);
	            Vector2 spreadTarget = new Vector2(position).add(spreadDirection.scl(1000));
	            
	            entidadManager.grupoProyectiles.agregarProyectil(new Proyectil(getProyectilTextura(), position, spreadTarget, proyectilVelocidad, daño, false)); 
	            NetworkData.serverThread.enviarMensajeATodos("proyectil!crear!" + getTipoProyectil() + "!" + position.x + "!" + position.y + "!" + proyectilVelocidad + "!" + daño + "!" + "false");
	        }
	    }
	}
	@Override
    public String getTipoEnemigo() {
        return "Fuerte";
    }

}

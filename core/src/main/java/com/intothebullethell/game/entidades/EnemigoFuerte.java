package com.intothebullethell.game.entidades;


import com.badlogic.gdx.math.Vector2;
import com.intothebullethell.game.globales.NetworkData;
import com.intothebullethell.game.globales.RecursoRuta;
import com.intothebullethell.game.managers.EntidadManager;

public class EnemigoFuerte extends Enemigo {
	public EnemigoFuerte(Jugador[] jugadores, EntidadManager entidadManager) {
		super(RecursoRuta.SPRITE_SLIME_1, RecursoRuta.SPRITE_SLIME_2, 20, 18, 14f, 1, 100, jugadores, entidadManager);
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
	            
	            entidadManager.getGrupoProyectiles().agregarProyectil(new Proyectil(getProyectilTexturaUno(), getProyectilTexturaDos(), position, spreadTarget, proyectilVelocidad, daño, false, 3, 3)); 
	            NetworkData.serverThread.enviarMensajeATodos("proyectil!crear!" + getTipoProyectil());
	        }
	    }
	}
	@Override
    public String getTipoEnemigo() {
        return "Fuerte";
    }

}

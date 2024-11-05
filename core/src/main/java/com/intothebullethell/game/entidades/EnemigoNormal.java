package com.intothebullethell.game.entidades;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.intothebullethell.game.globales.NetworkData;
import com.intothebullethell.game.globales.RecursoRuta;
import com.intothebullethell.game.managers.EntidadManager;
import com.intothebullethell.game.managers.ProyectilManager;

public class EnemigoNormal extends Enemigo {
    public EnemigoNormal(Jugador[] jugadores, List<Enemigo> enemigos, EntidadManager entidadManager) {
        super(RecursoRuta.ENEMIGO, 15, 10, 12f, 1, 60, RecursoRuta.PROYECTIL_ESCOPETA, jugadores, enemigos, entidadManager);
    }
    @Override
    public void atacar() {
        Jugador jugadorObjetivo = obtenerJugadorMasCercano();
        if (jugadorObjetivo != null) {
            Vector2 position = new Vector2(getX() + getWidth() / 2, getY() + getHeight() / 2);
            Vector2 target = new Vector2(jugadorObjetivo.getX() + jugadorObjetivo.getWidth() / 2, jugadorObjetivo.getY() + jugadorObjetivo.getHeight() / 2);
            
            entidadManager.grupoProyectiles.agregarProyectil(new Proyectil(getProyectilTextura(), position, target, proyectilVelocidad, daño, false)); 
            NetworkData.serverThread.enviarMensajeATodos("proyectil!crear!" + getTipoProyectil() + "!" + position.x + "!" + position.y + "!" + proyectilVelocidad + "!" + daño + "!" + "false");
        }
    }
    @Override
    public String getTipoEnemigo() {
        return "Normal";
    }
}


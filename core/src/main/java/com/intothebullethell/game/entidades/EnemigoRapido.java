package com.intothebullethell.game.entidades;


import com.badlogic.gdx.math.Vector2;
import com.intothebullethell.game.globales.NetworkData;
import com.intothebullethell.game.globales.RecursoRuta;
import com.intothebullethell.game.managers.EntidadManager;

public class EnemigoRapido extends Enemigo {
    public EnemigoRapido(Jugador[] jugadores, EntidadManager entidadManager) {
        super(RecursoRuta.SPRITE_MEDUSA_1, RecursoRuta.SPRITE_MEDUSA_2, 10, 30, 8f, 1, 350, jugadores, entidadManager);
    }
    @Override
    public void atacar() {
        Jugador jugadorObjetivo = obtenerJugadorMasCercano();
        if (jugadorObjetivo != null) {
            Vector2 position = new Vector2(getX() + getWidth() / 2, getY() + getHeight() / 2);
            Vector2 target = new Vector2(jugadorObjetivo.getX() + jugadorObjetivo.getWidth() / 2, jugadorObjetivo.getY() + jugadorObjetivo.getHeight() / 2);

            entidadManager.getGrupoProyectiles().agregarProyectil(new Proyectil(getProyectilTexturaUno(), getProyectilTexturaDos(), position, target, proyectilVelocidad, daño, false, 3, 3)); 
            NetworkData.serverThread.enviarMensajeATodos("proyectil!crear!" + getTipoProyectil());
        }
    }
    @Override
    public String getTipoEnemigo() {
        return "Rapido";
    }

}

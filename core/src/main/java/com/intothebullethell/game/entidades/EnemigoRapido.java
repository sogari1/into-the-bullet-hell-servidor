package com.intothebullethell.game.entidades;


import com.badlogic.gdx.math.Vector2;
import com.intothebullethell.game.globales.NetworkData;
import com.intothebullethell.game.globales.RecursoRuta;
import com.intothebullethell.game.managers.EntidadManager;

public class EnemigoRapido extends Enemigo {
    public EnemigoRapido(Jugador[] jugadores, EntidadManager entidadManager) {
        super(RecursoRuta.ENEMIGO, 10, 15, 10f, 1, 100, RecursoRuta.PROYECTIL_ESCOPETA, jugadores, entidadManager);
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
        return "Rapido";
    }

}

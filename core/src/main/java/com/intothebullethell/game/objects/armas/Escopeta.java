package com.intothebullethell.game.objects.armas;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.intothebullethell.game.entidades.Proyectil;
import com.intothebullethell.game.globales.NetworkData;
import com.intothebullethell.game.globales.RecursoRuta;
import com.intothebullethell.game.globales.SonidoRuta;

public class Escopeta extends Arma {
    public Escopeta() {
        super("Escopeta", 280, 3, 0.8f, 8, 32, RecursoRuta.PROYECTIL_ESCOPETA, RecursoRuta.PROYECTIL_ESCOPETA, RecursoRuta.ARMA_ESCOPETA, SonidoRuta.DISPARO_ESCOPETA);
    }

    @Override
    public void disparar(Vector2 position, Vector2 target, List<Proyectil> proyectiles) {
        Vector2 direction = new Vector2(target).sub(position).nor();

        for (int i = -2; i <= 2; i++) {
            Vector2 spreadDirection = new Vector2(direction).rotateDeg(i * 5); 
            Vector2 spreadTarget = new Vector2(position).add(spreadDirection.scl(1000));
            
            proyectiles.add(new Proyectil(proyectilTexturaUno, proyectilTexturaDos, position, spreadTarget, proyectilVelocidad, daño, true, 3, 3));
            NetworkData.serverThread.enviarMensajeATodos("proyectil!crear!" + getNombre());
        }
    }
}

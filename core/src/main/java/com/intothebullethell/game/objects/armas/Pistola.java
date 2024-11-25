package com.intothebullethell.game.objects.armas;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.intothebullethell.game.entidades.Proyectil;
import com.intothebullethell.game.globales.NetworkData;
import com.intothebullethell.game.globales.RecursoRuta;
import com.intothebullethell.game.globales.SonidoRuta;

public class Pistola extends Arma {
    public Pistola() {
        super("Pistola", 300, 1, 0.2f, 10, 80, RecursoRuta.PROYECTIL_PISTOLA, RecursoRuta.PROYECTIL_PISTOLA, RecursoRuta.ARMA_PISTOLA, SonidoRuta.DISPARO_PISTOLA);
    }

    @Override
    public void disparar(Vector2 position, Vector2 target, List<Proyectil> proyectiles) {
        proyectiles.add(new Proyectil(proyectilTexturaUno, proyectilTexturaDos, position, target, proyectilVelocidad, daño, true, 10, 10));
        NetworkData.serverThread.enviarMensajeATodos("proyectil!crear!" +  getNombre());
    }
    
}

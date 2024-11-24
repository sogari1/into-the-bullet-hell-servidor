package com.intothebullethell.game.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.intothebullethell.game.entidades.Jugador;
import com.intothebullethell.game.globales.NetworkData;
import com.intothebullethell.game.objects.objetos.Agarrable;
public class ObjetoManager {
	
	private List<Agarrable> agarrables = new ArrayList<>();

    public void agregarObjeto(Agarrable objeto) {
    	agarrables.add(objeto);
    }

    public void update(Jugador[] jugadores, float delta) {
        for (Iterator<Agarrable> iter = agarrables.iterator(); iter.hasNext();) {
        	Agarrable objeto = iter.next();
            for (Jugador jugador : jugadores) {
                if (jugador.getBoundingRectangle().overlaps(objeto.getBoundingRectangle())) {
                    objeto.aplicarEfecto(jugador);
                    int i = agarrables.indexOf(objeto); 
                    NetworkData.serverThread.enviarMensajeATodos("objeto!remover!" + i);
                    iter.remove();
                }
            }
        }
    }
    public void draw() {
        for (Agarrable objeto : agarrables) {
            objeto.draw(RenderManager.batchRender);
        }
    }

    public void reset() {
    	agarrables.clear();
    }
}

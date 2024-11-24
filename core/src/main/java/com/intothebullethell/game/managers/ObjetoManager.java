package com.intothebullethell.game.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.intothebullethell.game.entidades.Jugador;
import com.intothebullethell.game.globales.NetworkData;
import com.intothebullethell.game.objects.objetos.Objeto;

public class ObjetoManager {
	
	private List<Objeto> objetos= new ArrayList<>();

    public void agregarObjeto(Objeto objeto) {
    	objetos.add(objeto);
    }

    public void update(Jugador[] jugadores, float delta) {
        for (Iterator<Objeto> iter = objetos.iterator(); iter.hasNext();) {
            Objeto objeto = iter.next();
            for (Jugador jugador : jugadores) {
                if (jugador.getBoundingRectangle().overlaps(objeto.getBoundingRectangle())) {
                    objeto.aplicarEfecto(jugador);
                    int i = objetos.indexOf(objeto); 
                    NetworkData.serverThread.enviarMensajeATodos("objeto!remover!" + i);
                    iter.remove();
                }
            }
        }
    }
    public void draw() {
        for (Objeto objeto : objetos) {
            objeto.draw(RenderManager.batchRender);
        }
    }

    public void reset() {
    	objetos.clear();
    }
}

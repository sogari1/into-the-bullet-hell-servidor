package com.intothebullethell.game.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.intothebullethell.game.entidades.Enemigo;
import com.intothebullethell.game.globales.NetworkData;
import com.intothebullethell.game.objects.agarrables.Agarrable;
public class EnemigoManager {

    private List<Enemigo> enemigos = new ArrayList<>();
    private List<Integer> enemigosAEliminar = new ArrayList<>();
    private EntidadManager entidadManager; 
    
    public EnemigoManager(EntidadManager entidadManager) {
        this.entidadManager = entidadManager;
    }
    
    public void añadirEnemigo(Enemigo enemigo) {
        enemigos.add(enemigo);
    }

    public void update(float delta) {
        Iterator<Enemigo> iterator = enemigos.iterator();
        int i = 0;
        
        while (iterator.hasNext()) {
            Enemigo enemigo = iterator.next();
            enemigo.update(delta);
            NetworkData.serverThread.enviarMensajeATodos("enemigo!mover!" + i + "!" + enemigo.getX() + "!" + enemigo.getY());

            if (enemigo.isMuerto()) {
            	Agarrable objeto = enemigo.dropearObjeto(); 
            	if (objeto != null) {
            		objeto.setPosition(enemigo.getX(), enemigo.getY()); 
            		entidadManager.getObjetoManager().agregarObjeto(objeto); 
            		
            		NetworkData.serverThread.enviarMensajeATodos("objeto!crear!" + objeto.getTipo() + "!" + objeto.getX() + "!" + objeto.getY());
            	}
            	enemigosAEliminar.add(i); 
            }
            i++;
        }

        eliminarEnemigos();
    }

    private void eliminarEnemigos() {
        Collections.sort(enemigosAEliminar, Collections.reverseOrder());
        for (Integer index : enemigosAEliminar) {
            if (index >= 0 && index < enemigos.size()) {
                enemigos.remove((int)index);
                NetworkData.serverThread.enviarMensajeATodos("enemigo!remover!" + index);
            }
        }
        enemigosAEliminar.clear();
    }
    
    public void draw() {
        for (Enemigo enemigo : enemigos) {
            enemigo.draw(RenderManager.batchRender);
        }
    }

    public List<Enemigo> getEnemigos() {
        return enemigos;
    }

    public int getCantidad() {
        return enemigos.size();
    }
    public void reset() {
        enemigos.clear();
        enemigosAEliminar.clear();
    }

}

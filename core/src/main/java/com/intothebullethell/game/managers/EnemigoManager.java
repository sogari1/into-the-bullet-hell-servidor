package com.intothebullethell.game.managers;

import java.util.ArrayList;
import java.util.List;

import com.intothebullethell.game.entidades.Enemigo;
import com.intothebullethell.game.globales.NetworkData;
public class EnemigoManager {

    private List<Enemigo> enemigos = new ArrayList<>();
    
    public void añadirEntidad(Enemigo entidad) {
        enemigos.add(entidad);
    }

    public void update(float delta) {
        List<Enemigo> enemigosAEliminar = new ArrayList<>();
        
        for (Enemigo enemigo : enemigos) {
            enemigo.update(delta); 
            int enemigoId = enemigos.indexOf(enemigo);
            NetworkData.serverThread.enviarMensajeATodos("enemigo!mover!" + enemigoId + "!" + enemigo.getX() + "!" + enemigo.getY());

            if (enemigo.estaMuerto()) {
                enemigosAEliminar.add(enemigo);
            }
        }

        // Eliminamos los enemigos muertos
        for (Enemigo enemigo : enemigosAEliminar) {
            int enemigoId = enemigos.indexOf(enemigo);
            removerEnemigo(enemigoId);
        }
    }

    public void draw() {
        for (Enemigo enemigo : enemigos) {
            enemigo.draw(RenderManager.batchRender);
        }
    }

    private void removerEnemigo(int index) {
        if (index >= 0 && index < enemigos.size()) {
            enemigos.remove(index);
            NetworkData.serverThread.enviarMensajeATodos("enemigo!remover!" + index);
        }
    }

    public List<Enemigo> getEntidades() {
        return enemigos;
    }

    public void reset(){
        enemigos.clear();
    }
}

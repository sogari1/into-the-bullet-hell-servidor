package com.intothebullethell.game.managers;

import java.util.ArrayList;
import java.util.List;

import com.intothebullethell.game.entidades.Enemigo;

public class EnemigoManager {

	private List<Enemigo> enemigos = new ArrayList<>();
	private boolean entityRemoved = false;
	    
	public void añadirEntidad(Enemigo entidad) {
		enemigos.add(entidad);
	}
	public void update(float delta) {
		for (Enemigo enemigo : enemigos) {
			enemigo.update(delta); 
//			NetworkData.serverThread.enviarMensajeATodos(null);
			if (enemigo.estaMuerto()) {
				removerEntidad(enemigo);
			}
		}
	}
	public void draw() {
		for (Enemigo enemigo : enemigos) {
			enemigo.draw(RenderManager.batchRender);
		}
	}
	private void removerEntidad(Enemigo entidad) {
		enemigos.remove(entidad);
		entityRemoved = true;
//		NetworkData.serverThread.enviarMensajeATodos(null);
	}
	public List<Enemigo> getEntidades() {
		return enemigos;
	}
	public void reset(){
		enemigos.clear();
	}
}

package com.intothebullethell.game.managers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.intothebullethell.game.entidades.Jugador;
import com.intothebullethell.game.mecanicas.GenerarEnemigos;

public class EntidadManager {

	private EnemigoManager grupoEnemigos;
	public ProyectilManager grupoProyectiles;
	
	private GenerarEnemigos generadorEnemigos;
	
	public EntidadManager(OrthographicCamera camara, TiledMap map, Jugador[] jugadores, TileColisionManager tileCollisionManager) {
		crearGrupo();
		this.generadorEnemigos = new GenerarEnemigos(camara, RenderManager.mapa, grupoEnemigos.getEntidades(), jugadores, tileCollisionManager, this);
	}
	private void crearGrupo() {
		this.grupoEnemigos = new EnemigoManager();
		this.grupoProyectiles = new ProyectilManager();
	}
	public void update(float delta, Jugador[] jugadores) {
		if (grupoEnemigos.getEntidades().isEmpty()) {
	        generadorEnemigos.generarEnemigos();
	    }
		grupoEnemigos.update(delta);
		grupoProyectiles.update(delta, grupoEnemigos.getEntidades(), jugadores);
	}
	public void draw() {
		grupoEnemigos.draw();
		grupoProyectiles.draw();
	}
	public void reset() {
		grupoEnemigos.reset();
		grupoProyectiles.reset();
	}
	public EnemigoManager getgrupoEnemigos(){
        return grupoEnemigos;
    }
	 public ProyectilManager getGrupoProyectiles() {
	        return grupoProyectiles;
	}
}

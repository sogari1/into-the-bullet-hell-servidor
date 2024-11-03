package com.intothebullethell.game.managers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.intothebullethell.game.entidades.Jugador;
import com.intothebullethell.game.mecanicas.GenerarEnemigos;

public class MapManager {

	private EnemigoManager grupoEnemigos;
	private ProyectilManager grupoProyectiles;
	
	private GenerarEnemigos generadorEnemigos;
	
	public MapManager(OrthographicCamera camara, TiledMap map, Jugador[] jugadores, TileColisionManager tileCollisionManager, ProyectilManager proyectilManager) {
		crearGrupo();
		this.generadorEnemigos = new GenerarEnemigos(camara, RenderManager.mapa, grupoEnemigos.getEntidades(), jugadores, tileCollisionManager, proyectilManager);
	}
	private void crearGrupo() {
		this.grupoEnemigos = new EnemigoManager();
		this.grupoProyectiles = new ProyectilManager();
	}
	public void update(float delta, Jugador[] jugadores) {
		if (grupoEnemigos.getEntidades().isEmpty()) {
//	        generadorEnemigos.generarEnemigos();
	    }
		grupoEnemigos.update(delta);
		grupoProyectiles.actualizarProyectiles(delta, grupoEnemigos.getEntidades(), jugadores);
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

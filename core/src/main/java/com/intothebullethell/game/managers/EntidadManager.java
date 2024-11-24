package com.intothebullethell.game.managers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.intothebullethell.game.entidades.Jugador;
import com.intothebullethell.game.mecanicas.GenerarEnemigos;
import com.intothebullethell.game.pantallas.MultiplayerPantalla;

public class EntidadManager {
	
	private EnemigoManager grupoEnemigos;
	private ProyectilManager grupoProyectiles;
	private ObjetoManager grupoObjetos;
	
	private GenerarEnemigos generadorEnemigos;
	private MultiplayerPantalla multiplayerPantalla;
	
	 private float temporizadorGeneracion = 0f; 
	 private static final float TIEMPO_ESPERA = 5f;
	
	public EntidadManager(OrthographicCamera camara, TiledMap map, Jugador[] jugadores, TileColisionManager tileCollisionManager, MultiplayerPantalla multiplayerPantalla) {
		crearGrupo();
		this.generadorEnemigos = new GenerarEnemigos(camara, RenderManager.mapa, jugadores, tileCollisionManager, this);
		this.multiplayerPantalla = multiplayerPantalla;
	}
	private void crearGrupo() {
		this.grupoProyectiles = new ProyectilManager();
		this.grupoObjetos = new ObjetoManager();
		this.grupoEnemigos = new EnemigoManager(this);
	}
	public void update(float delta, Jugador[] jugadores) {
		 if (grupoEnemigos.getEnemigos().isEmpty()) {
	            temporizadorGeneracion += delta;

	            if (temporizadorGeneracion > TIEMPO_ESPERA) {
	                generadorEnemigos.generarEnemigos();
	                multiplayerPantalla.incrementarRonda(); 
	                temporizadorGeneracion = 0f; 
	            }
	        } else {
	            grupoEnemigos.update(delta);
	        }
		grupoProyectiles.update(delta, grupoEnemigos.getEnemigos(), jugadores);
		grupoObjetos.update(jugadores, delta);
	}
	public void draw() {
		grupoEnemigos.draw();
		grupoProyectiles.draw();
		grupoObjetos.draw();
	}
	public void reset() {
		grupoEnemigos.reset();
		grupoProyectiles.reset();
		grupoObjetos.reset();
		generadorEnemigos.reiniciarContador();
	}
	public EnemigoManager getGrupoEnemigos(){
        return grupoEnemigos;
    }
	public ProyectilManager getGrupoProyectiles() {
		return grupoProyectiles;
	}
	public ObjetoManager getObjetoManager() {
		return grupoObjetos;
	}
	public GenerarEnemigos getGeneradorEnemigos() {
		return generadorEnemigos;
	}
}

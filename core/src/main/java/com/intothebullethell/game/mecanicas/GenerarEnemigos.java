package com.intothebullethell.game.mecanicas;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.intothebullethell.game.entidades.Enemigo;
import com.intothebullethell.game.entidades.EnemigoFuerte;
import com.intothebullethell.game.entidades.EnemigoNormal;
import com.intothebullethell.game.entidades.EnemigoRapido;
import com.intothebullethell.game.entidades.Jugador;
import com.intothebullethell.game.globales.NetworkData;
import com.intothebullethell.game.managers.EntidadManager;
import com.intothebullethell.game.managers.TileColisionManager;

public class GenerarEnemigos {
    private TiledMap map;
    private ArrayList<Enemigo> listaEnemigos = new ArrayList<>();
    private Set<Vector2> posicionesOcupadas = new HashSet<>();
    private Jugador[] jugadores; 
    private TileColisionManager tileCollisionManager;
    private Random random =  new Random();
    private EntidadManager entidadManager;
    
    private int numeroDeEnemigos = 10;

    public GenerarEnemigos(OrthographicCamera camara, TiledMap map, Jugador[] jugadores, TileColisionManager tileCollisionManager, EntidadManager entidadManager) {
        this.map = map;
        this.jugadores = jugadores;
        this.tileCollisionManager = tileCollisionManager;
        this.entidadManager = entidadManager;
    }
    public void generarEnemigos() {
    	posicionesOcupadas.clear();
        entidadManager.getGrupoEnemigos().reset();
        for (int i = 0; i < this.numeroDeEnemigos; i++) {
            Vector2 spawnPosition = crearSpawnUnico();

            Enemigo enemigo = crearEnemigoAleatorio(spawnPosition);
            enemigo.setPosition(spawnPosition.x, spawnPosition.y);
            enemigo.updateBoundingBox();
            entidadManager.getGrupoEnemigos().añadirEnemigo(enemigo);
            NetworkData.serverThread.enviarMensajeATodos("enemigo!crear!" + enemigo.getTipoEnemigo());
        }
        sumarNumeroDeEnemigos();
    }

    private Vector2 crearSpawnUnico() {
        int mapaAncho = 100;
        int mapaAlto = 100; 
        int tileAncho= 32; 
        int tileAlto = 32; 

        Vector2 posicion;
        do {
        	posicion = new Vector2(
                (float) (Math.random() * (mapaAncho - 1) * tileAncho),
                (float) (Math.random() * (mapaAlto - 1) * tileAlto)
            );
        } while (posicionesOcupadas.contains(posicion) || !isPosicionValida(posicion));

        posicionesOcupadas.add(posicion);
//        System.out.println(occupiedPositions);
        return posicion;
    }

    private boolean isPosicionValida(Vector2 position) {
        int tileTamaño = 32; 
        int x = (int) position.x / tileTamaño;
        int y = (int) position.y / tileTamaño;

        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        if (x >= 0 && x < layer.getWidth() && y >= 0 && y < layer.getHeight()) {
            Rectangle boundingBox = new Rectangle(position.x, position.y, tileTamaño, tileTamaño);
            return !tileCollisionManager.esColision(boundingBox);
        }
        return false;
    }

    private Enemigo crearEnemigoAleatorio(Vector2 spawnPosition) {
        inicializarListaEnemigos();
        Enemigo enemigo;
        do {
            enemigo = listaEnemigos.get(random.nextInt(listaEnemigos.size()));
            enemigo.setPosition(spawnPosition.x, spawnPosition.y);
            enemigo.updateBoundingBox(); 
        } while (enemigo.getVidaActual() <= 0); 
        
        return enemigo;
    }

    private int sumarNumeroDeEnemigos() {
        this.numeroDeEnemigos += 1;
        return this.numeroDeEnemigos;
    }
    public void reiniciarContador() {
		this.numeroDeEnemigos = 10;
	}
    private void inicializarListaEnemigos() {
    	 listaEnemigos.add(new EnemigoNormal(jugadores, entidadManager));
         listaEnemigos.add(new EnemigoRapido(jugadores, entidadManager));
         listaEnemigos.add(new EnemigoFuerte(jugadores, entidadManager));
    }
}

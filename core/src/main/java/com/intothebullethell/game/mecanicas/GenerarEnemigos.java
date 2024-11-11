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
    private Set<Vector2> occupiedPositions = new HashSet<>();
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
        occupiedPositions.clear();
        entidadManager.grupoEnemigos.reset();
        for (int i = 0; i < numeroDeEnemigos; i++) {
            Vector2 spawnPosition = crearSpawnUnico();

            Enemigo enemigo = crearEnemigoAleatorio(spawnPosition);
            enemigo.setPosition(spawnPosition.x, spawnPosition.y);
            enemigo.updateBoundingBox();
            entidadManager.grupoEnemigos.añadirEntidad(enemigo);
            NetworkData.serverThread.enviarMensajeATodos("enemigo!crear!" + enemigo.getTipoEnemigo() +"!" + enemigo.getX() + "!" + enemigo.getY());
        }
        sumarNumeroDeEnemigos();
    }

    private Vector2 crearSpawnUnico() {
        int mapWidth = 50;
        int mapHeight = 50; 
        int tileWidth = 32; 
        int tileHeight = 32; 

        Vector2 position;
        do {
            position = new Vector2(
                (float) (Math.random() * (mapWidth - 1) * tileWidth),
                (float) (Math.random() * (mapHeight - 1) * tileHeight)
            );
        } while (occupiedPositions.contains(position) || !isPosicionValida(position));

        occupiedPositions.add(position);
//        System.out.println(occupiedPositions);
        return position;
    }

    private boolean isPosicionValida(Vector2 position) {
        int tileSize = 32; 
        int x = (int) position.x / tileSize;
        int y = (int) position.y / tileSize;

        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        if (x >= 0 && x < layer.getWidth() && y >= 0 && y < layer.getHeight()) {
            Rectangle boundingBox = new Rectangle(position.x, position.y, tileSize, tileSize);
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
        numeroDeEnemigos += 1;
        return numeroDeEnemigos;
    }
    private void inicializarListaEnemigos() {
    	 listaEnemigos.add(new EnemigoNormal(jugadores, entidadManager));
         listaEnemigos.add(new EnemigoRapido(jugadores, entidadManager));
         listaEnemigos.add(new EnemigoFuerte(jugadores, entidadManager));
    }
}

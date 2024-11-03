package com.intothebullethell.game.pantallas;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.intothebullethell.game.IntoTheBulletHell;
import com.intothebullethell.game.entidades.Jugador;
import com.intothebullethell.game.globales.GameData;
import com.intothebullethell.game.globales.JuegoEstado;
import com.intothebullethell.game.globales.NetworkData;
import com.intothebullethell.game.globales.RecursoRuta;
import com.intothebullethell.game.inputs.InputManager;
import com.intothebullethell.game.managers.MapManager;
import com.intothebullethell.game.managers.ProyectilManager;
import com.intothebullethell.game.managers.RenderManager;
import com.intothebullethell.game.managers.TileColisionManager;
import com.intothebullethell.game.mecanicas.Tiempo;
import com.intothebullethell.game.network.NetworkActionsListener;
import com.intothebullethell.game.network.ServerThread;
import com.intothebullethell.game.ui.Hud;

public class MultiplayerPantalla implements Screen, NetworkActionsListener {

	private final int NUM_JUGADORES = 2;
	private Jugador[] jugadores = new Jugador[NUM_JUGADORES];
	private Hud[] huds = new Hud[NUM_JUGADORES]; 

	private MapManager mapManager;
    private OrthographicCamera camara;
    private IntoTheBulletHell game;
    private Stage stage;
    private Tiempo tiempo;
    private ProyectilManager proyectilManager;
    private InputManager inputManager;
    private TileColisionManager tileCollisionManager;
    
    private boolean pausado = false;
    private int ronda = 1;
    
    public MultiplayerPantalla(IntoTheBulletHell game) {
    	GameData.networkListener = this;
    	NetworkData.serverThread = new ServerThread();
    	NetworkData.serverThread.start();
        this.game = game;
        this.proyectilManager = new ProyectilManager();
        this.tileCollisionManager = new TileColisionManager();
        this.camara = new OrthographicCamera();
        this.inputManager = new InputManager();
        this.mapManager = new MapManager(camara, RenderManager.mapa, jugadores, tileCollisionManager, proyectilManager);
    	Gdx.input.setInputProcessor(inputManager);
           
        
        crearJugadores();  
        crearHudJugadores();
        
        this.tiempo = new Tiempo(jugadores);
        
        this.stage = new Stage(new ScreenViewport());
        
    }
    private void crearJugadores() {
    	for (int i = 0; i < NUM_JUGADORES; i++) {
			jugadores[i] = new Jugador(i, RecursoRuta.SPRITE_ABAJO, RecursoRuta.SPRITE_ARRIBA,  RecursoRuta.SPRITE_ABAJO, RecursoRuta.SPRITE_IZQUIERDA,  RecursoRuta.SPRITE_DERECHA, camara, inputManager, mapManager, proyectilManager);
			 this.jugadores[i].setPosition((15 + (i*2)) * tileCollisionManager.collisionLayer.getTileWidth(), 15 * tileCollisionManager.collisionLayer.getTileHeight());
		}
    }
    private void crearHudJugadores() {
        for (int i = 0; i < NUM_JUGADORES; i++) {
            huds[i] = new Hud(RenderManager.batch, jugadores[i]);
            jugadores[i].setHud(huds[i]);
        }
    }
    
	@Override
	public void show() {
		
	}
	@Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        RenderManager.renderizarCamara(camara);
        draw();
        update(delta);  
    }
	
	@Override
    public void resize(int width, int height) {
        camara.viewportWidth = width;
        camara.viewportHeight = height;
        stage.getViewport().update(width, height, true);
    }
	private void update(float delta) {
//	    manejarJuegoInputs();
		if (GameData.juegoEstado.equals(JuegoEstado.JUGANDO)) {
			for (Jugador jugador : jugadores) {
		        jugador.update(delta);
		    }
			mapManager.update(delta, jugadores);
//		    if (enemigos.isEmpty()) {
//		        ronda++;
//		        for (Hud hud : huds) {
//		            hud.actualizarRonda(ronda);
//		        }
//		        generadorEnemigos.generarEnemigos();
//		    }
//
//		    for (Hud hud : huds) {
//		        hud.actualizarEnemigosRestantes(enemigos.size());
//		        hud.actualizarTemporizador(Tiempo.getTiempo());
//		    }
//		    // Actualiza los proyectiles
//		    proyectilManager.actualizarProyectiles(delta, enemigos, jugadores);
			NetworkData.serverThread.enviarMensajeATodos("tiempo!" + Tiempo.getTiempo());
        }
	}


	private void draw() {
		RenderManager.batchRender.begin();
			for (Jugador jugador : jugadores) {
				jugador.draw(RenderManager.batchRender);
			}
			mapManager.draw();
		RenderManager.batchRender.end();
		stage.draw();
	    }
	@Override
	public void pause() {
	}
	@Override
	public void resume() {
	}
	@Override
	public void hide() {
	}
	@Override
	 public void dispose() {
        NetworkData.serverThread.end();
    }
	@Override
	public void empezarJuego() {
		GameData.juegoEstado = JuegoEstado.JUGANDO;
	}
	@Override
	public void recargar(int jugadorId) {
		jugadores[jugadorId].recargarArma();
	}
	@Override
	public void disparar(int jugadorId) {
		
	}
	@Override
	public void dispararRelease(int jugadorId) {
		
	}
	@Override
	public void moverJugadorArriba(int jugadorId) {
		jugadores[jugadorId].moverArriba();
	}
	@Override
	public void moverJugadorAbajo(int jugadorId) {
		jugadores[jugadorId].moverAbajo();
	}
	@Override
	public void moverJugadorIzquierda(int jugadorId) {
		jugadores[jugadorId].moverIzquierda();
	}
	@Override
	public void moverJugadorDerecha(int jugadorId) {
		jugadores[jugadorId].moverDerecha();
	}
	@Override
	public void moverJugadorArribaRelease(int jugadorId) {
		jugadores[jugadorId].detenerMovimientoArriba();
	}
	@Override
	public void moverJugadorAbajoRelease(int jugadorId) {
		jugadores[jugadorId].detenerMovimientoAbajo();
	}
	@Override
	public void moverJugadorIzquierdaRelease(int jugadorId) {
		jugadores[jugadorId].detenerMovimientoIzquierda();
	}
	@Override
	public void moverJugadorDerechaRelease(int jugadorId) {
		jugadores[jugadorId].detenerMovimientoDerecha();
		
	}
 }

package com.intothebullethell.game.pantallas;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.intothebullethell.game.entidades.Jugador;
import com.intothebullethell.game.globales.GameData;
import com.intothebullethell.game.globales.JuegoEstado;
import com.intothebullethell.game.globales.NetworkData;
import com.intothebullethell.game.globales.RecursoRuta;
import com.intothebullethell.game.inputs.InputManager;
import com.intothebullethell.game.managers.EntidadManager;
import com.intothebullethell.game.managers.RenderManager;
import com.intothebullethell.game.managers.TileColisionManager;
import com.intothebullethell.game.mecanicas.Tiempo;
import com.intothebullethell.game.network.NetworkActionsListener;
import com.intothebullethell.game.network.ServerThread;
import com.intothebullethell.game.ui.Boton;
import com.intothebullethell.game.ui.Hud;
import com.intothebullethell.game.ui.Texto;

public class MultiplayerPantalla implements Screen, NetworkActionsListener {

	private final int NUM_JUGADORES = 2;
	private Jugador[] jugadores = new Jugador[NUM_JUGADORES];

	private EntidadManager entidadManager;
    private OrthographicCamera camara;
    private Stage stage;
    private Tiempo tiempo;
    private InputManager inputManager;
    private TileColisionManager tileCollisionManager;
    private Hud hud;
    
    private Texto esperandoJugadorTexto;
    private Boton reiniciarBoton, salirBoton;
    private int ronda = 0;
    
    @Override
	public void show() {
		GameData.networkListener = this;
 
        this.tileCollisionManager = new TileColisionManager();
        this.camara = new OrthographicCamera();
        this.inputManager = new InputManager();
        this.entidadManager = new EntidadManager(camara, RenderManager.mapa, jugadores, tileCollisionManager, this);
    	Gdx.input.setInputProcessor(inputManager);
           
        crearJugadores();  
        inicializarTextos();
        crearBotones();
        this.hud = new Hud();
        this.tiempo = new Tiempo(jugadores);
        tiempo.start();
        this.stage = new Stage(new ScreenViewport());
        
    	NetworkData.serverThread = new ServerThread();
    	NetworkData.serverThread.start();
	}
    private void crearJugadores() {
    	for (int i = 0; i < NUM_JUGADORES; i++) {
			jugadores[i] = new Jugador(i, RecursoRuta.SPRITE_ABAJO, RecursoRuta.SPRITE_ARRIBA,  RecursoRuta.SPRITE_ABAJO, RecursoRuta.SPRITE_IZQUIERDA,  RecursoRuta.SPRITE_DERECHA, camara, inputManager, entidadManager);
			 this.jugadores[i].setPosition((15 + (i*2)) * tileCollisionManager.collisionLayer.getTileWidth(), 15 * tileCollisionManager.collisionLayer.getTileHeight());
		}
    }
    private void inicializarTextos() {
    	esperandoJugadorTexto = new Texto("Esperando jugadores..", 24, Color.RED, 0, Gdx.graphics.getHeight() - 400);
    	esperandoJugadorTexto.setShadow(4, 4, Color.BLACK);
    	esperandoJugadorTexto.centerX();
    }
    private void crearBotones() {
    	reiniciarBoton = new Boton(new Texto("Reiniciar", 24, Color.WHITE, 0, 200));
    	reiniciarBoton.centrarX();
    	
    	salirBoton = new Boton(new Texto("SALIR", 24, Color.WHITE, 0, 150));
    	salirBoton.centrarX();
    }
	@Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        RenderManager.renderizarCamara(camara);
        update(delta);  
        draw();
    }
	
	@Override
    public void resize(int width, int height) {
		camara.viewportWidth = width - 320;
		camara.viewportHeight = height - 300;
        stage.getViewport().update(width, height, true);
    }
	private void update(float delta) {
		if (GameData.juegoEstado.equals(JuegoEstado.JUGANDO)) {
			tiempo.reanudar(); 
			for (Jugador jugador : jugadores) {
		        jugador.update(delta);
		    }
			entidadManager.update(delta, jugadores);
			
			hud.actualizarTemporizador(Tiempo.getTiempo());
			hud.actualizarEnemigosRestantes(entidadManager.getGrupoEnemigos().getCantidad());
			chequearMuerteJugadores();
        } else if(GameData.juegoEstado.equals(JuegoEstado.GAME_OVER)) {
        	for (Jugador jugador : jugadores) {
		        jugador.update(delta);
		    }
	    	if(reiniciarBoton.isClicked()) {
	    		reiniciarJuego();
	    	}
        } else if(GameData.juegoEstado.equals(JuegoEstado.ESPERANDO)) {
        	for (Jugador jugador : jugadores) {
		        jugador.update(delta);
		    }
        	tiempo.pausar(); 
        }
	}


	private void draw() {
		RenderManager.batchRender.begin();
			for (Jugador jugador : jugadores) {
				jugador.draw(RenderManager.batchRender);
			}
			entidadManager.draw();
			
		RenderManager.batchRender.end();
		
		RenderManager.batch.begin();
			if (GameData.juegoEstado.equals(JuegoEstado.GAME_OVER)) {
				reiniciarBoton.draw();
		    }
			if(GameData.juegoEstado.equals(JuegoEstado.ESPERANDO)){
				esperandoJugadorTexto.draw();
            }
			hud.draw();
		RenderManager.batch.end();
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
		NetworkData.serverThread.enviarMensajeATodos("servidorapagado!El servidor se ha apagado");
        NetworkData.serverThread.end();
        tiempo.detener();
    }
	public void incrementarRonda() {
		this.ronda++;
		hud.actualizarRonda(ronda);
	}
	private void chequearMuerteJugadores() {
	    boolean todosMuertos = true;

	    for (Jugador jugador : jugadores) {
	        if (!jugador.chequearMuerte()) {
	            todosMuertos = false;
	        }
	    }

	    if (todosMuertos) {
	        terminarJuego();
	    }
	}

	@Override
	public void empezarJuego() {
		GameData.juegoEstado = JuegoEstado.JUGANDO;
	}
	@Override
	public void terminarJuego() {
		if(GameData.juegoEstado.equals(JuegoEstado.JUGANDO)) {

			GameData.juegoEstado = JuegoEstado.GAME_OVER;
			NetworkData.serverThread.enviarMensajeATodos("gameover");
			NetworkData.serverThread.limpiarClientes();
			reiniciarJuego();
		}
	}
	private void reiniciarJuego() {
		GameData.juegoEstado = JuegoEstado.ESPERANDO;
		for (Jugador jugador : jugadores) {
			jugador.reiniciar();
	    }
		tiempo.reiniciar();
		crearJugadores();
		entidadManager.reset(); 
		this.ronda = 0;
	}
	@Override
	public void recargar(int jugadorId) {
		jugadores[jugadorId].recargarArma();
	}
	@Override
	public void usarBengala(int jugadorId) {
		jugadores[jugadorId].usarBengala();
	}
	@Override
	public void usarActivo(int jugadorId) {
		jugadores[jugadorId].usarActivo();
	}
	@Override
	public void disparar(int jugadorId, int mouseX, int mouseY) {
		jugadores[jugadorId].setMouseX(mouseX);
		jugadores[jugadorId].setMouseY(mouseY);
		jugadores[jugadorId].setDisparando(true);
	}
	@Override
	public void dispararRelease(int jugadorId) {
		jugadores[jugadorId].setDisparando(false);
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
		jugadores[jugadorId].moverArribaRelease();
	}
	@Override
	public void moverJugadorAbajoRelease(int jugadorId) {
		jugadores[jugadorId].moverAbajoRelease();
	}
	@Override
	public void moverJugadorIzquierdaRelease(int jugadorId) {
		jugadores[jugadorId].moverIzquierdaRelease();
	}
	@Override
	public void moverJugadorDerechaRelease(int jugadorId) {
		jugadores[jugadorId].moverDerechaRelease();
	}
	@Override
	public void actualizarDireccionJugador(int jugadorId, String region) {
		jugadores[jugadorId].actualizarDireccion(region);
	}
 }

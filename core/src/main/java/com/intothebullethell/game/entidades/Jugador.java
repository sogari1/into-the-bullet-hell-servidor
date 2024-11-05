package com.intothebullethell.game.entidades;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.intothebullethell.game.globales.GameData;
import com.intothebullethell.game.globales.JuegoEstado;
import com.intothebullethell.game.globales.NetworkData;
import com.intothebullethell.game.inputs.InputManager;
import com.intothebullethell.game.managers.EntidadManager;
import com.intothebullethell.game.managers.ProyectilManager;
import com.intothebullethell.game.mecanicas.ArmaAleatoria;
import com.intothebullethell.game.objects.armas.Arma;
import com.intothebullethell.game.ui.Hud;

public class Jugador extends Entidad {
    private Vector2 mousePosition = new Vector2();
    private Arma armaEquipada;
    private ArmaAleatoria armaAleatoria = new ArmaAleatoria();
    public OrthographicCamera camara;
    private TextureRegion upSprite, downSprite, leftSprite, rightSprite;
    private Hud hud;
    private InputManager inputManager;
    private EntidadManager entidadManager;
    
    private float shootTimer = 0;
    private float opacidad = 1.0f;
    private float escudoCoolDown = 0;
    private final float escudoCoolDownMaximo = 2.5f; 
    private int vidaActual;
    private boolean disparando = false;
    private int mouseX, mouseY;
    
    private int numeroJugador;

    public Jugador(int numeroJugador, TextureRegion sprite, TextureRegion upSprite, TextureRegion downSprite, TextureRegion leftSprite, TextureRegion rightSprite, OrthographicCamera camara, InputManager inputManager, EntidadManager entidadManager) {
    	super(sprite.getTexture(), 20, 100, null);
        this.upSprite = upSprite;
        this.downSprite = downSprite;
        this.leftSprite = leftSprite;
        this.rightSprite = rightSprite;
        this.camara = camara;
        this.vidaActual = vidaMaxima;
        this.armaEquipada = armaAleatoria.obtenerArmaAleatoria();
        this.inputManager = inputManager;
        this.inputManager.setJugador(this);
        this.entidadManager = entidadManager;
        this.numeroJugador = numeroJugador;
    }

    @Override
    public void draw(Batch batch) {
        update(Gdx.graphics.getDeltaTime());
        super.draw(batch); 
        entidadManager.grupoProyectiles.draw();

    }

    @Override
    public void update(float delta) {
    	Jugador[] jugadores = new Jugador[]{this};
    	 if (escudoCoolDown > 0) {
    		 escudoCoolDown -= delta; 
    		 opacidad = 0.5f;
         }
    	 else {
             opacidad = 1.0f; 
    	 }
    	 setColor(1.0f, 1.0f, 1.0f, opacidad); 
    	 actualizarMovimiento();
         manejarDisparos(delta);
         actualizarCamara();
         entidadManager.grupoProyectiles.update(delta, entidadManager.getgrupoEnemigos().getEntidades(), jugadores);
    }

    private void actualizarMovimiento() {
        mover(velocity);
        if(GameData.juegoEstado.equals(JuegoEstado.JUGANDO)){
        	NetworkData.serverThread.enviarMensajeATodos("jugador!mover!" + this.numeroJugador + "!" + getX() + "!" + getY());
        }
    }
    public void actualizarDireccion(String region) {
    	switch(region) {
    	case "arriba":
    		setRegion(upSprite);
    		break;
    	case "abajo":
    		setRegion(downSprite);
    		break;
    	case "izquierda":
    		setRegion(leftSprite);
    		break;
    	case "derecha":
    		setRegion(rightSprite);
    		break;
    	}
    	NetworkData.serverThread.enviarMensajeATodos("jugador!direccion!" + this.numeroJugador + "!" + region);
    }
    public void moverArriba() {
        velocity.y = velocidad;
    }

    public void moverAbajo() {
        velocity.y = -velocidad;
    }

    public void moverIzquierda() {
        velocity.x = -velocidad;
    }

    public void moverDerecha() {
        velocity.x = velocidad;
    }
    public void moverArribaRelease() {
        if (velocity.y > 0) { 
            velocity.y = 0;
        }
    }
    public void moverAbajoRelease() {
        if (velocity.y < 0) { 
            velocity.y = 0;
        }
    }
    public void moverIzquierdaRelease() {
        if (velocity.x < 0) {
            velocity.x = 0;
        }
    }
    public void moverDerechaRelease() {
        if (velocity.x > 0) { 
            velocity.x = 0;
        }
    }
    private void manejarDisparos(float delta) {
        if (isDisparando() && GameData.juegoEstado.equals(JuegoEstado.JUGANDO)) {
            shootTimer -= delta;
            if (shootTimer <= 0) {
            	entidadManager.grupoProyectiles.dispararProyectil(camara, armaEquipada, getX() + getWidth() / 2, getY() + getHeight() / 2, mouseX, mouseY);
                shootTimer = armaEquipada.getRatioFuego(); 
            }
        }
    }
    private void actualizarCamara() {
        camara.position.set(getX() + getWidth() / 2, getY() + getHeight() / 2, 0);
        camara.update();
    }
    public void recargarArma() {
    	armaEquipada.reload();
    }
    public int getVidaActual() {
        return vidaActual;
    }
    public boolean chequearMuerte() {
        if (vidaActual == 0) {
            return true; 
        }
        return false; 
    }
    @Override
    public void recibirDaño(int daño) {
        if (escudoCoolDown <= 0) {
            vidaActual -= daño;
            if (vidaActual < 0) {
                vidaActual = 0; 
            }
            escudoCoolDown = escudoCoolDownMaximo; 
        }
    }
    public float getShieldCooldown() {
        return escudoCoolDown;
    }
	public void setHud(Hud hud) { 
    	this.hud = hud; 
    }
    public void cambiarArma() {
        this.armaEquipada = armaAleatoria.obtenerArmaAleatoria();
        hud.updateWeaponSprite(); 
    }
    public void setArma(Arma arma) {
        this.armaEquipada = arma;
        hud.updateWeaponSprite();
    }
    public void setDisparando(boolean disparando) { 
    	this.disparando = disparando; 
    }
    public boolean isDisparando() {
        return disparando;
    }
    public Arma getArmaEquipada() { 
    	return armaEquipada; 
    }
    public float getShootTimer() {
        return shootTimer;
    }
    public void setShootTimer(float shootTimer) {
        this.shootTimer = shootTimer;
    }

    public Texture getArmaTextura() {
    	return armaEquipada.getArmaTextura();
    }
    public void setMouseX(int mouseX) {
		this.mouseX = mouseX;
	}
    public void setMouseY(int mouseY) {
		this.mouseY = mouseY;
	}
}

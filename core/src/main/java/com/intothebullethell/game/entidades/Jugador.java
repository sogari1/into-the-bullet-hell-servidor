package com.intothebullethell.game.entidades;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.intothebullethell.game.globales.GameData;
import com.intothebullethell.game.globales.JuegoEstado;
import com.intothebullethell.game.globales.NetworkData;
import com.intothebullethell.game.inputs.InputManager;
import com.intothebullethell.game.managers.EntidadManager;
import com.intothebullethell.game.mecanicas.ActivoAleatorio;
import com.intothebullethell.game.mecanicas.ArmaAleatoria;
import com.intothebullethell.game.objects.activos.Activo;
import com.intothebullethell.game.objects.armas.Arma;
import com.intothebullethell.game.objects.armas.Bengala;

public class Jugador extends Entidad {
	private Bengala bengala = new Bengala();
    private ArmaAleatoria armaAleatoria = new ArmaAleatoria();
    private ActivoAleatorio activoAleatorio = new ActivoAleatorio();
    private Arma armaEquipada;
    private Activo activoEquipado;
    private TextureRegion upSprite, downSprite, leftSprite, rightSprite;
    private EntidadManager entidadManager;
    public OrthographicCamera camara;
    
    private float shootTimer = 0;
    private float opacidad = 1.0f;
    private float escudoCoolDown = 0;
    private final float escudoCoolDownMaximo = 2.5f; 
    private boolean disparando = false;
    private int mouseX, mouseY;
    
    private int numeroJugador;

    public Jugador(int numeroJugador, TextureRegion sprite, TextureRegion upSprite, TextureRegion downSprite, TextureRegion leftSprite, TextureRegion rightSprite, OrthographicCamera camara, InputManager inputManager, EntidadManager entidadManager) {
    	super(sprite.getTexture(), 20, 150, null);
        this.upSprite = upSprite;
        this.downSprite = downSprite;
        this.leftSprite = leftSprite;
        this.rightSprite = rightSprite;
        this.camara = camara;
        this.entidadManager = entidadManager;
        this.numeroJugador = numeroJugador;
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch); 
    }

    @Override
    public void update(float delta) {
    	if(!chequearMuerte()) {
	    	manejarEscudoCoolDown(delta);
	    	actualizarMovimiento();
	    	manejarDisparos(delta);
	    	actualizarCamara();
	    	bengala.update(delta);
	    	NetworkData.serverThread.enviarMensajeATodos("jugador!vida!" + this.numeroJugador + "!" + this.vidaMaxima + "!" + this.vidaActual);
	    	NetworkData.serverThread.enviarMensajeATodos("jugador!bengala!" + this.numeroJugador + "!" + this.bengala.getUsosRestantes());
	    	if(armaEquipada != null && activoEquipado != null) {
	    		NetworkData.serverThread.enviarMensajeATodos("jugador!arma!" + this.numeroJugador + "!" + this.armaEquipada.getNombre());
	    		NetworkData.serverThread.enviarMensajeATodos("jugador!activo!" + this.numeroJugador + "!" + this.activoEquipado.getNombre());
	    		NetworkData.serverThread.enviarMensajeATodos("jugador!armamunicion!" + this.numeroJugador + "!" + this.armaEquipada.getBalasEnReserva() + "!" +  this.armaEquipada.getBalasEnMunicion());
	    	}
    	}
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

    private void manejarEscudoCoolDown(float delta) {
    	if (escudoCoolDown > 0) {
    		escudoCoolDown -= delta;
    		opacidad = 0.5f; 
    	} else {
    		opacidad = 1.0f;
    	}
    	setColor(1.0f, 1.0f, 1.0f, opacidad); 
    }
    private void manejarDisparos(float delta) {
        if (isDisparando() && GameData.juegoEstado.equals(JuegoEstado.JUGANDO) && armaEquipada != null) {
            shootTimer -= delta;
            if (shootTimer <= 0) {
            	entidadManager.getGrupoProyectiles().dispararProyectil(camara, armaEquipada, getX() + getWidth() / 2, getY() + getHeight() / 2, mouseX, mouseY);
                shootTimer = armaEquipada.getRatioFuego(); 
            }
        }
    }
    private void actualizarCamara() {
        camara.position.set(getX() + getWidth() / 2, getY() + getHeight() / 2, 0);
        camara.update();
    }
    public void recargarArma() {
    	if(armaEquipada != null) {
    		armaEquipada.recargar();
    	}
    }
    public void usarBengala() {
    	bengala.usar(entidadManager);
    }
    public void usarActivo() {
    	if(activoEquipado != null) {
    		activoEquipado.usar(this);
    		NetworkData.serverThread.enviarMensajeATodos("jugador!activousado!" + this.numeroJugador + "!" + "true");
    	}
    }
    public int getVidaActual() {
        return vidaActual;
    }
    public boolean chequearMuerte() {
        if (this.vidaActual <= 0) {
        	NetworkData.serverThread.enviarMensajeATodos("jugador!muerto!" + this.numeroJugador);
        	return true; 
        }
        return false; 
    }
    @Override
    public void recibirDaño(int daño) {
    	if (escudoCoolDown <= 0 && !chequearMuerte()) {
    		this.vidaActual -= daño;
    		escudoCoolDown = escudoCoolDownMaximo; 
    		if(this.vidaActual <= 0) {
            	this.vidaActual = 0;
    		}
        }
    }

    public void reiniciar() {
    	disparando = false;
    	velocity.x = 0;
    	velocity.y = 0;
    }
    public float getShieldCooldown() {
        return escudoCoolDown;
    }
    public void aumentarVida(int vida) {
    	this.vidaActual += vida;
    	if(this.vidaActual > this.vidaMaxima) {
    		this.vidaActual = this.vidaMaxima;
    	}
    }
    public void cambiarArma() {
        this.armaEquipada = armaAleatoria.obtenerArmaAleatoria();
    }
    public void cambiarActivo() {
        this.activoEquipado = activoAleatorio.obtenerActivoAleatorio();
        NetworkData.serverThread.enviarMensajeATodos("jugador!activousado!" + this.numeroJugador + "!" + "false");
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
    public void aumentarVelocidad(int velocidad) {
    	this.velocidad += velocidad;
    }
}

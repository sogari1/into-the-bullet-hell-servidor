package com.intothebullethell.game.entidades;



import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.intothebullethell.game.managers.EntidadManager;
import com.intothebullethell.game.objects.objetos.Agarrable;
import com.intothebullethell.game.objects.objetos.Balas;
import com.intothebullethell.game.objects.objetos.Corazon;

public abstract class Enemigo extends Entidad {
    protected Jugador[] jugadores;
    protected EntidadManager entidadManager;
    
    protected float intervaloAtaque;
    protected float tiempoAtaque;
    protected float proyectilVelocidad;
    protected int daño;
    private final float PROBABILIDAD_DROP = 0.2f; 

    public Enemigo(Texture texture, int vida, int velocidad, float intervaloAtaque, int daño, float proyectilVelocidad, Texture proyectilTextura, Jugador[] jugadores, EntidadManager entidadManager) {
        super(texture, vida, velocidad, proyectilTextura);
        this.jugadores = jugadores;
        this.intervaloAtaque = intervaloAtaque;
        this.tiempoAtaque = intervaloAtaque;
        this.daño = daño;
        this.proyectilVelocidad = proyectilVelocidad;
        this.entidadManager = entidadManager; 
    }

    @Override
    public void update(float delta) {
        moverHaciaJugador(delta);
 
        tiempoAtaque -= delta;
        if (tiempoAtaque <= 0) {
            atacar();
            tiempoAtaque = intervaloAtaque;
        }
    }

    private void moverHaciaJugador(float delta) {
        Jugador jugadorObjetivo = obtenerJugadorMasCercano();
        if (jugadorObjetivo != null) {
            Vector2 position = new Vector2(getX(), getY());
            Vector2 target = new Vector2(jugadorObjetivo.getX(), jugadorObjetivo.getY());
            Vector2 direction = target.sub(position).nor();

            velocity.set(direction).scl(velocidad);

            mover(velocity);
        }
    }
    protected Jugador obtenerJugadorMasCercano() {
        Jugador jugadorCercano = null;
        float distanciaMinima = Float.MAX_VALUE;

        for (Jugador jugador : jugadores) {
        	if(!jugador.chequearMuerte()) {
        		float distancia = Vector2.dst(getX(), getY(), jugador.getX(), jugador.getY());
        		if (distancia < distanciaMinima) {
        			distanciaMinima = distancia;
        			jugadorCercano = jugador;
        		}
        	}
        }
        return jugadorCercano;
    }

    @Override
    public void atacar() {}

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        boundingBox.setPosition(x, y); 
    }

    @Override
    public void recibirDaño(int daño) {
        vidaActual -= daño;
        if(vidaActual < 0) {
            vidaActual = 0;
        }
    }

    public boolean isMuerto() {
        if (vidaActual <= 0) {
            return true;
        }
        return false;
    }
    public Agarrable dropearObjeto() {
        if (Math.random() < PROBABILIDAD_DROP) {
          
            if (Math.random() < 0.5) { 
                return new Corazon();
            } else { 
                return new Balas();
            }
        }
        return null;
    }

    public abstract String getTipoEnemigo();
	public String getTipoProyectil() {
		return "Escopeta";
	}
}

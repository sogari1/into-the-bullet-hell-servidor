package com.intothebullethell.game.entidades;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.intothebullethell.game.globales.RecursoRuta;
import com.intothebullethell.game.managers.EntidadManager;
import com.intothebullethell.game.objects.agarrables.Agarrable;
import com.intothebullethell.game.objects.agarrables.CajaMunicion;
import com.intothebullethell.game.objects.agarrables.CajaVida;

public abstract class Enemigo extends Entidad {
	private static final Texture[] PROYECTIL_TEXTURA = new Texture[]{RecursoRuta.PROYECTIL_ENEMIGO_1, RecursoRuta.PROYECTIL_ENEMIGO_2};
    protected Jugador[] jugadores;
    protected EntidadManager entidadManager;
    protected float intervaloAtaque;
    protected float tiempoAtaque;
    protected float proyectilVelocidad;
    protected int daño;
    private final float PROBABILIDAD_DROP = 0.2f; 
    private AnimacionEntidad animacionEnemigo;

    public Enemigo(Texture sprite1, Texture sprite2, int vida, int velocidad, float intervaloAtaque, int daño, float proyectilVelocidad, Jugador[] jugadores, EntidadManager entidadManager) {
        super(sprite1, vida, velocidad, PROYECTIL_TEXTURA[0]);
        this.jugadores = jugadores;
        this.intervaloAtaque = intervaloAtaque;
        this.tiempoAtaque = intervaloAtaque;
        this.daño = daño;
        this.proyectilVelocidad = proyectilVelocidad;
        this.entidadManager = entidadManager; 
        
        this.animacionEnemigo = new AnimacionEntidad(new Texture[]{sprite1, sprite2});
    }
    @Override
    public void draw(Batch batch) {
        batch.draw(animacionEnemigo.actualizarAnimacionEntidad(Gdx.graphics.getDeltaTime()), getX(), getY(), getWidth(), getHeight());
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
                return new CajaVida();
            } else { 
                return new CajaMunicion();
            }
        }
        return null;
    }
    public static Texture getProyectilTexturaUno() {
		return PROYECTIL_TEXTURA[0];
	}
    public static Texture getProyectilTexturaDos() {
		return PROYECTIL_TEXTURA[1];
	}
    public abstract String getTipoEnemigo();
	public String getTipoProyectil() {
		return "Enemigo";
	}
}

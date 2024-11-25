package com.intothebullethell.game.objects.activos;
import com.badlogic.gdx.graphics.Texture;
import com.intothebullethell.game.entidades.Jugador;
import com.intothebullethell.sonido.EfectoSonido;

public abstract class Activo {
    private String nombre; 
    private float tiempoDeUso; 
    private boolean usado;
    private Texture texturaObjeto; 
    private EfectoSonido efectoSonido; 

    public Activo(String nombre, float tiempoDeUso, Texture texturaObjeto, EfectoSonido efectoSonido) {
        this.nombre = nombre;
        this.tiempoDeUso = tiempoDeUso;
        this.usado = false;
        this.texturaObjeto = texturaObjeto;
        this.efectoSonido = efectoSonido;
    }

    public String getNombre() {
        return nombre;
    }

    public float getTiempoDeUso() {
        return tiempoDeUso;
    }

    public boolean isUsado() {
        return usado;
    }

    public Texture getTexturaObjeto() {
        return texturaObjeto;
    }

    public EfectoSonido getEfectoSonido() {
        return efectoSonido;
    }

    public void usar(Jugador jugador) {
        if (!usado) {
            aplicarEfecto(jugador);
            usado = true;

            if (efectoSonido != null) {
                efectoSonido.reproducirSonido();
            }
            new Thread(() -> {
                try {
                    Thread.sleep((long) (tiempoDeUso * 1000)); 
                    revertirEfecto(jugador); 
                    usado = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
        }
    }

    protected abstract void aplicarEfecto(Jugador jugador);
    protected abstract void revertirEfecto(Jugador jugador);
}

package com.intothebullethell.game.objects.armas;

import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.intothebullethell.game.entidades.Proyectil;
import com.intothebullethell.sonido.EfectoSonido;

public abstract class Arma {
    protected String nombre;
    protected float proyectilVelocidad;
    protected int daño;
    protected float ratioFuego;
    protected Texture proyectilTexturaUno, proyectilTexturaDos;
    protected Texture armaTextura;
    protected int capacidadMunicion;
    protected int balasEnReserva;
    protected int balasEnMunicion;
    private EfectoSonido efectosSonido;
    

    public Arma(String nombre, float proyectilVelocidad, int daño, float ratioFuego, int capacidadMunicion, int balasEnReserva, Texture proyectilTexturaUno, Texture proyectilTexturaDos, Texture armaTextura, EfectoSonido efectosSonido) {
        this.nombre = nombre;
        this.proyectilVelocidad = proyectilVelocidad;
        this.daño = daño;
        this.ratioFuego = ratioFuego;
        this.capacidadMunicion = capacidadMunicion;
        this.balasEnReserva = balasEnReserva;
        this.balasEnMunicion = capacidadMunicion; 
        this.proyectilTexturaUno = proyectilTexturaUno;
        this.proyectilTexturaDos = proyectilTexturaDos;
        this.armaTextura = armaTextura;
        this.efectosSonido = efectosSonido;
    }

    public abstract void disparar(Vector2 position, Vector2 target, List<Proyectil> proyectiles);

    public boolean puedeDisparar() {
        return balasEnMunicion > 0;
    }

    public void recargar() {
        if (balasEnReserva > 0) {
            int balasNecesitadas = capacidadMunicion - balasEnMunicion;
            int balasARecargar = Math.min(balasNecesitadas, balasEnReserva);

            balasEnMunicion += balasARecargar;
            balasEnReserva -= balasARecargar;
        }
    }


    public void dispararProyectil(Vector2 position, Vector2 target, List<Proyectil> proyectiles) {
        if (puedeDisparar()) {
        	disparar(position, target, proyectiles);  
        	balasEnMunicion--;  
            efectosSonido.reproducirSonido();
        }
    }


    public int getBalasEnMunicion() {
        return balasEnMunicion;
    }

    public int getBalasEnReserva() {
        return balasEnReserva;
    }

    public String getNombre() {
        return nombre;
    }

    public Texture getProyectilTexturaUno() {
        return proyectilTexturaUno;
    }

    public float getRatioFuego() {
		return ratioFuego;
	}
	public Texture getArmaTextura() {
        return armaTextura;
    }
	public void aumentarBalasEnReserva(int balasEnReserva) {
		this.balasEnReserva += balasEnReserva;
	}
	public void aumentarDaño(int daño){
		this.daño += daño;
	}
}

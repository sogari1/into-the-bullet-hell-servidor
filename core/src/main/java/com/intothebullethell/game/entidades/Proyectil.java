package com.intothebullethell.game.entidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Proyectil extends Sprite {
    private Vector2 posicion;
    private Vector2 direccion;
    private float velocidad;
    private int daño;
    private boolean disparadoPorJugador;
    private Rectangle boundingBox;
    private float margenX, margenY; 
    private AnimacionEntidad animacionEntidad;

    public Proyectil(Texture sprite1, Texture sprite2, Vector2 posicion, Vector2 target, float velocidad, int daño, boolean disparadoPorJugador, float margenX, float margenY) {
        super(sprite1);
        this.posicion = new Vector2(posicion);
        this.direccion = new Vector2(target).sub(posicion).nor();
        this.velocidad = velocidad;
        this.daño = daño;
        this.disparadoPorJugador = disparadoPorJugador;
        this.margenX = margenX;
        this.margenY = margenY;
        setOrigin(getWidth() / 2, getHeight() / 2);
        setPosition(posicion.x - getWidth() / 2, posicion.y - getHeight() / 2);
        
        this.boundingBox = new Rectangle( getX() + margenX, getY() + margenY, getWidth() - 2 * margenX, getHeight() - 2 * margenY);
        this.animacionEntidad = new AnimacionEntidad(new Texture[]{sprite1, sprite2});
        
    }

    public void update(float delta) {
        Vector2 velocity = new Vector2(direccion).scl(velocidad * delta);
        posicion.add(velocity);
        setPosition(posicion.x - getWidth() / 2, posicion.y - getHeight() / 2);

        boundingBox.setPosition(getX() + margenX, getY() + margenY);
    }

    public boolean collidesWith(Entidad Entidad) {
        return boundingBox.overlaps(Entidad.getBoundingRectangle());
    }

    public int getDaño() {
        return daño;
    }

    public boolean isDisparadoPorJugador() {
        return disparadoPorJugador;
    }

    @Override
    public void draw(Batch batch) {
    	batch.draw(animacionEntidad.actualizarAnimacionEntidad(Gdx.graphics.getDeltaTime()), getX(), getY(), getWidth(), getHeight());
    }
}


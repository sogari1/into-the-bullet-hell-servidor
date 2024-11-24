package com.intothebullethell.game.globales;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class RecursoRuta {
    
    //JUGADOR
    public static final TextureRegion SPRITE_ARRIBA = new TextureRegion(new Texture("imagenes/personaje/kurumiUp.png"));
    public static final TextureRegion SPRITE_ABAJO = new TextureRegion(new Texture("imagenes/personaje/kurumiDown.png"));
    public static final TextureRegion SPRITE_IZQUIERDA = new TextureRegion(new Texture("imagenes/personaje/kurumiLeft.png"));
    public static final TextureRegion SPRITE_DERECHA = new TextureRegion(new Texture("imagenes/personaje/kurumiRight.png"));
    
    //BENGALA
    public static final Texture BENGALA = new Texture("imagenes/objetos/objetosAgarrables/caja_municion.png");
    
    //ENEMIGOS
    public static final Texture ENEMIGO = new Texture("imagenes/enemigos/ENEMIGO.png");    
    public static final Texture PROYECTIL_ENEMIGO = new Texture("imagenes/objetos/armaProyectil/bala_enemigo.png");
    
    //ARMAS
    public static final Texture ARMA_PISTOLA = new Texture("imagenes/objetos/armas/pistola.png");
    public static final Texture ARMA_ESCOPETA = new Texture("imagenes/objetos/armas/escopeta.png");
    
    //OBJETOS AGARRABLES
    public static final Texture CAJA_VIDA= new Texture("imagenes/objetos/objetosAgarrables/caja_vida.png");
    public static final Texture CAJA_MUNICION= new Texture("imagenes/objetos/objetosAgarrables/caja_municion.png");
    
    //OBJETOS ACTIVOS
    public static final Texture ADRENALINA = new Texture("imagenes/objetos/objetosActivos/adrenalina.png");
    public static final Texture SANGUCHE = new Texture("imagenes/objetos/objetosActivos/sanguche.png");
    
    //PROYECTILES
    public static final Texture PROYECTIL_PISTOLA = new Texture("imagenes/objetos/armaProyectil/bala.png");
    public static final Texture PROYECTIL_ESCOPETA = new Texture("imagenes/objetos/armaProyectil/bala_escopeta.png");
        
	//HUD
    public static final Texture CORAZON_LLENO = new Texture("imagenes/otros/hud/fullHeart.png");
    public static final Texture CORAZON_MITAD = new Texture("imagenes/otros/hud/halfHeart.png");
    public static final Texture CORAZON_VACIO = new Texture("imagenes/otros/hud/blankHeart.png");
    
    
    public static void dispose() {
        CORAZON_LLENO.dispose();
        CORAZON_MITAD.dispose();
        CORAZON_VACIO.dispose();
    }
}

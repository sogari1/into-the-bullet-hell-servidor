package com.intothebullethell.game.ui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.intothebullethell.game.globales.NetworkData;
import com.intothebullethell.game.managers.RenderManager;

public class Hud {
    private Stage stage;
    private Texto textoRonda, textoTiempo, textoMunicion, textoEnemigosRestantes;
    private int ronda;

    public Hud(SpriteBatch spriteBatch) {
        this.stage = new Stage(new ScreenViewport(), spriteBatch);

        textoRonda = new Texto("Ronda: ", 20, Color.WHITE, 10, Gdx.graphics.getHeight() - 20);
        textoRonda.setShadow(4, 4, Color.BLACK);
        
        textoTiempo = new Texto("Aleatorizando en: ", 20, Color.WHITE, 10, Gdx.graphics.getHeight() - 20);
        textoTiempo.setShadow(4, 4, Color.BLACK);
        textoTiempo.centerX();
        
        textoMunicion = new Texto("", 20, Color.WHITE, 10, Gdx.graphics.getHeight() - 850);
        textoMunicion.setShadow(4, 4, Color.BLACK);
        
        textoEnemigosRestantes = new Texto("Enemigos restantes: ", 20, Color.WHITE, 10, Gdx.graphics.getHeight() - 60);
        textoEnemigosRestantes.setShadow(4, 4, Color.BLACK);
    }

    public void draw() {
        textoRonda.draw(RenderManager.batch);
        textoTiempo.draw(RenderManager.batch);
        textoEnemigosRestantes.draw(RenderManager.batch);

        stage.draw();
    }

    public void actualizarRonda(int nuevaRonda) {
        ronda = nuevaRonda;
        textoRonda.setText("Ronda: " + ronda);
        NetworkData.serverThread.enviarMensajeATodos("ronda!" + ronda);
    }

    public void actualizarTemporizador(int tiempo) {
        textoTiempo.setText("Aleatorizando en: " + tiempo + "s");
        NetworkData.serverThread.enviarMensajeATodos("tiempo!" + tiempo);
    }
    public void actualizarEnemigosRestantes(int cantidad) {
    	textoEnemigosRestantes.setText("Enemigos restantes: " + cantidad);
    	NetworkData.serverThread.enviarMensajeATodos("enemigo!cantidad!" + cantidad);
    }

    public void dispose() {
        stage.dispose();
        textoRonda.dispose();
        textoTiempo.dispose();
        textoMunicion.dispose();
        textoEnemigosRestantes.dispose();
    }
}

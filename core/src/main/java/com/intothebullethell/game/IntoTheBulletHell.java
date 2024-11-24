package com.intothebullethell.game;

import com.badlogic.gdx.Game;
import com.intothebullethell.game.managers.ScreenManager;
import com.intothebullethell.game.pantallas.MultiplayerPantalla;
import com.intothebullethell.sonido.Musica;

public class IntoTheBulletHell extends Game {
    
    private Musica musica;
    @Override
    public void create() {
        musica = new Musica();
        ScreenManager.gameApp = this;
        ScreenManager.setScreen(new MultiplayerPantalla());
    }
    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
    	getScreen().dispose();
    }

    public Musica getMusica() {
        return musica;
    }
}

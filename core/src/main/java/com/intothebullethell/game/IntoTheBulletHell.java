package com.intothebullethell.game;

import com.badlogic.gdx.Game;
import com.intothebullethell.game.pantallas.MultiplayerPantalla;
import com.intothebullethell.sonido.Musica;

public class IntoTheBulletHell extends Game {
    
    private Musica musica;
    @Override
    public void create() {
        musica = new Musica();
        setScreen(new MultiplayerPantalla(this));
    }
    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public Musica getMusica() {
        return musica;
    }
}

package com.intothebullethell.game.objects.armas;

import com.badlogic.gdx.graphics.Texture;
import com.intothebullethell.game.globales.RecursoRuta;
import com.intothebullethell.game.globales.SonidoRuta;
import com.intothebullethell.game.managers.EntidadManager;
import com.intothebullethell.sonido.EfectoSonido;

public class Bengala {
	private EfectoSonido efectosSonido = SonidoRuta.BENGALA;
	private Texture spriteBengala = RecursoRuta.BENGALA;
			
    private int usosMaximos = 2;
    private int usosRestantes = usosMaximos;
    private float cooldown = 5.0f; 
    private float tiempoDesdeUltimoUso = 0;

    public void usar(EntidadManager entidadManager) {
        if (puedeUsarse()) {
        	 entidadManager.getGrupoProyectiles().eliminarProyectilesEnemigos();

            usosRestantes--;
            tiempoDesdeUltimoUso = 0; 
            efectosSonido.reproducirSonido();
        }
    }

    public void update(float delta) {
        if (tiempoDesdeUltimoUso < cooldown) {
            tiempoDesdeUltimoUso += delta; 
        }
    }
    public boolean puedeUsarse() {
        return usosRestantes > 0 && tiempoDesdeUltimoUso >= cooldown;
    }
    public int getUsosRestantes() {
        return usosRestantes;
    }

    public float getCooldownRestante() {
        return Math.max(0, cooldown - tiempoDesdeUltimoUso);
    }
}

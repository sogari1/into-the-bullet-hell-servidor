package com.intothebullethell.game.mecanicas;

import java.util.ArrayList;
import java.util.Random;

import com.intothebullethell.game.objects.activos.Activo;
import com.intothebullethell.game.objects.activos.Adrenalina;
import com.intothebullethell.game.objects.activos.AnilloUnico;
import com.intothebullethell.game.objects.activos.Sanguche;

public class ActivoAleatorio {
	private ArrayList<Activo> listaObjetos = new ArrayList<>();
	private Random random = new Random();
	
    public ActivoAleatorio() {
        inicializarObjetos();
    }

    private void inicializarObjetos() {
    	listaObjetos.add(new Adrenalina());
    	listaObjetos.add(new Sanguche());
    	listaObjetos.add(new AnilloUnico());
    }

    public Activo obtenerActivoAleatorio() {
        return listaObjetos.get(random.nextInt(listaObjetos.size()));
    }
}

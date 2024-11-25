package com.intothebullethell.game.mecanicas;

import java.util.ArrayList;
import java.util.Random;

import com.intothebullethell.game.objects.armas.AWP;
import com.intothebullethell.game.objects.armas.Arma;
import com.intothebullethell.game.objects.armas.BFG9000;
import com.intothebullethell.game.objects.armas.Blaster;
import com.intothebullethell.game.objects.armas.Escopeta;
import com.intothebullethell.game.objects.armas.EstrellaNinja;
import com.intothebullethell.game.objects.armas.Pistola;
import com.intothebullethell.game.objects.armas.Sniper;

public class ArmaAleatoria {
    private ArrayList<Arma> listaArmas;
    private Random random;

    public ArmaAleatoria() {
        this.listaArmas = new ArrayList<>();
        this.random = new Random();
        inicializarArmas();
    }

    private void inicializarArmas() {
    	listaArmas.add(new Pistola());
    	listaArmas.add(new Escopeta());
    	listaArmas.add(new BFG9000());
    	listaArmas.add(new EstrellaNinja());
    	listaArmas.add(new Sniper());
    	listaArmas.add(new AWP());
    	listaArmas.add(new Blaster());
    }

    public Arma obtenerArmaAleatoria() {
        return listaArmas.get(random.nextInt(listaArmas.size()));
    }
}

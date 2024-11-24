package com.intothebullethell.game.mecanicas;

import com.intothebullethell.game.entidades.Jugador;
public class Tiempo extends Thread {
    private static int tiempo;
    private Jugador[] jugadores;
    private boolean running;
    private boolean paused;

    public Tiempo(Jugador[] jugadores) {
        Tiempo.tiempo = 5;
        this.jugadores = jugadores;
        this.running = true;
        this.paused = true;  
    }

    @Override
    public void run() {
        while (running) {
            if (!paused) {
                try {
                    Thread.sleep(1000);
                    tiempo--;
//                    System.out.println("Tiempo: " + tiempo);
                    if (tiempo < 0) {
                        for (Jugador jugador : jugadores) {
                            jugador.cambiarArma();
                        }
                        tiempo = 30;
                    }
                } catch (InterruptedException e) {
                	e.printStackTrace();
                	running = false;
                }
            } else {
                try {
                	Thread.sleep(100);
                } catch (InterruptedException e) {
                	e.printStackTrace();
                    
                }
            }
        }
    }

    public static int getTiempo() {
        return tiempo;
    }

    public synchronized void pausar() {
        paused = true;
    }

    public void reanudar() {
        paused = false;
    }

    public void reiniciar() {
        pausar();
        tiempo = 6;
        System.out.println("Tiempo reiniciado a: " + tiempo);
    }


    public void detener() {
        running = false;
        System.out.println("Hilo de tiempo terminado.");
    }

	public boolean isPausado() {
		return paused;
	}
}

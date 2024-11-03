package com.intothebullethell.game.network;

public interface NetworkActionsListener {

	void empezarJuego();
	
	void moverJugadorArriba(int jugadorId);
	void moverJugadorAbajo(int jugadorId);
	void moverJugadorIzquierda(int jugadorId);
	void moverJugadorDerecha(int jugadorId);
	
	void moverJugadorArribaRelease(int jugadorId);
	void moverJugadorAbajoRelease(int jugadorId);
	void moverJugadorIzquierdaRelease(int jugadorId);
	void moverJugadorDerechaRelease(int jugadorId);
	
	void disparar(int jugadorId);
	void dispararRelease(int jugadorId);
	void recargar(int jugadorId);
}

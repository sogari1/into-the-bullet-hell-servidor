package com.intothebullethell.game.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.intothebullethell.game.globales.GameData;

public class ServerThread extends Thread{

	  private final int PORT = 5555;
	  private DatagramSocket socket;
	  private boolean end = false;
	  private String specialChar = "!";
	  private final int MAX_CLIENTES = 2;
	  private int clientesConectados = 0;
	  private Cliente[] clientes = new Cliente[MAX_CLIENTES];

	  public ServerThread() {
		  try {
			  socket = new DatagramSocket(PORT);
		  } catch (SocketException e) {
			  throw new RuntimeException(e);
		  }
	  }

	  @Override
	  public void run() {
		  while(!end){
			  DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
			  try {
				  socket.receive(packet);
				  procesarMensajeDeCliente(packet);
			  } catch (IOException e) {
				  throw new RuntimeException(e);
			  }
		  }
	  }
	  private void procesarMensajeDeCliente(DatagramPacket packet) {
		  String message = new String(packet.getData()).trim();
//		  System.out.println("SERVIDOR: Mensaje recibido: " + message);
		  String[] parts = message.split(specialChar);
		  
		  switch(parts[0]) {
          case "connect":
        	  boolean coneccionExitosa = conectarCliente(packet);
        	  if(coneccionExitosa && clientesConectados == MAX_CLIENTES) {
        		  enviarMensajeATodos("startgame");
        		  GameData.networkListener.empezarJuego();
        	  }
              break;
          case "disconnect":
              break;
      }	  
		  if(clientesConectados == MAX_CLIENTES) {
			  switch (parts[0]) {
			  case "mover":
				  manejarJugadorMovimiento(parts);
				  break;
			  case "recargar":
				  GameData.networkListener.recargar(Integer.parseInt(parts[1]));
				  break;
			  }
		  }
	  }
	  private void manejarJugadorMovimiento(String[] parts) {
		  switch(parts[1]) {
		  case "arriba":
			  GameData.networkListener.moverJugadorArriba(Integer.parseInt(parts[2]));
			  break;
		  case "abajo":
			  GameData.networkListener.moverJugadorAbajo(Integer.parseInt(parts[2]));
			  break;
		  case "izquierda":
			  GameData.networkListener.moverJugadorIzquierda(Integer.parseInt(parts[2]));
			  break;
		  case "derecha":
			  GameData.networkListener.moverJugadorDerecha(Integer.parseInt(parts[2]));
			  break;
		  case "arribarelease":
			  GameData.networkListener.moverJugadorArribaRelease(Integer.parseInt(parts[2]));
			  break;
		  case "abajorelease":
			  GameData.networkListener.moverJugadorAbajoRelease(Integer.parseInt(parts[2]));
			  break;
		  case "izquierdarelease":
			  GameData.networkListener.moverJugadorIzquierdaRelease(Integer.parseInt(parts[2]));
			  break;
		  case "derecharelease":
			  GameData.networkListener.moverJugadorDerechaRelease(Integer.parseInt(parts[2]));
			  break;
		  }
	}

	private boolean conectarCliente(DatagramPacket packet) {
	        if(clientesConectados < MAX_CLIENTES){
	            if(!clienteExiste(packet.getAddress(), packet.getPort())){
	            	añadirCliente(packet);
	            	enviarMensajeAlCliente("connection" + specialChar + "successful" + specialChar + (clientesConectados-1),  packet.getAddress(), packet.getPort());
	                return true;
	            } else {
	                // EL CLIENTE YA EXISTE
	            }
	        } else {
	            //PODRIA MANDARLE UN MENSAJE AL CLIENTE DICIENDO QUE SE RECHAZO LA CONEXION PQ EL SERVER ESTA LLENO
	        }
	        return false;
	  }
	  private boolean clienteExiste(InetAddress address, int port) {
	        for (int i = 0; i < clientesConectados; i++) {
	            if(clientes[i].getIp().equals(address) && clientes[i].getPort() == port){
	                return true;
	            }
	        }
	        return false;
	    }
	  private void añadirCliente(DatagramPacket packet) {
	        clientes[clientesConectados] = new Cliente(packet.getAddress(), packet.getPort(), clientesConectados);
	        clientesConectados++;
	        System.out.println("SERVIDOR: Cliente conectado");
	    }

	    public void enviarMensajeAlCliente(String msg, InetAddress ip, int port){
	        byte[] data = msg.getBytes();
	        DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
	        try {
	            socket.send(packet);
//	            System.out.println("SERVIDOR: Mensaje enviado: " + msg);
	        } catch (IOException e) {
	        }
	    }

	    public void enviarMensajeATodos(String msg){
	        if(clientesConectados == 0){
	            return;
	        }
	        for (int i = 0; i < clientesConectados; i++) {
	        	enviarMensajeAlCliente(msg, clientes[i].getIp(), clientes[i].getPort());
	        }
	    }

	    public void limpiarClientes() {
	        clientes = new Cliente[MAX_CLIENTES];
	        clientesConectados = 0;
	    }

	    public void end(){
	        this.end = true;
	        this.socket.close();
	    }
}

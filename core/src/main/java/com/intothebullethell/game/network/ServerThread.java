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
			if (e.getMessage().contains("Address already in use")) {
				System.err.println("Error: El puerto " + PORT + " ya está en uso. Por favor, cierra el servidor anterior o utiliza otro puerto.");
			} else {
				System.err.println("Error al inicializar el servidor: " + e.getMessage());
			}
			throw new RuntimeException("No se pudo iniciar el servidor debido a un problema con el socket.");
		}
	}
	
	@Override
	public void run() {
		while (!end) {
			DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
			try {
				socket.receive(packet);
				procesarMensajeDeCliente(packet);
			} catch (SocketException e) {
				if (socket.isClosed()) {
					System.out.println("Servidor cerrado correctamente.");
				} else {
					throw new RuntimeException("Error inesperado en el socket: " + e.getMessage(), e);
				}
			} catch (IOException e) {
				System.err.println("Error al recibir paquete: " + e.getMessage());
			}
		}
	}
	 private void procesarMensajeDeCliente(DatagramPacket packet) {
		  String message = new String(packet.getData()).trim();
//		  System.out.println("SERVIDOR: Mensaje recibido: " + message); 
		  String[] parts = message.split(specialChar);
		  
		  switch(parts[0]) {
          case "conectar":
        	  boolean coneccionExitosa = conectarCliente(packet);
        	  if(coneccionExitosa && clientesConectados == MAX_CLIENTES) {
        		  enviarMensajeATodos("empezarjuego");
        		  GameData.networkListener.empezarJuego();
        	  }
              break;
          case "desconectar":
        	  int numeroCliente = Integer.parseInt(parts[1]);
        	  numeroCliente = (numeroCliente==1)?0:1;
        	  clientesConectados--;
        	  if(clientesConectados>0) {
        		  this.enviarMensajeAlCliente("clientedesconectado!Un jugador se ha desconectado.", this.clientes[numeroCliente].getIp(), this.clientes[numeroCliente].getPort());
                  System.out.println("SERVIDOR: Un cliente se desconectó");
              }
        	  this.limpiarClientes();
        	  GameData.networkListener.terminarJuego();
              break;
      }	  
		  if(clientesConectados == MAX_CLIENTES) {
			  switch (parts[0]) {
			  case "mover":
				  manejarJugadorMovimiento(parts);
				  break;
			  case "disparo":
				  manejarDisparosJugador(parts);
				  break;
			  case "recargar":
				  GameData.networkListener.recargar(Integer.parseInt(parts[1]));
				  break;
			  case "bengala":
				  GameData.networkListener.usarBengala(Integer.parseInt(parts[1]));
				  break;
			  case "activo":
				  GameData.networkListener.usarActivo(Integer.parseInt(parts[1]));
				  break;
			  }
		  }
	  }
	  private void manejarDisparosJugador(String[] parts) {
		  switch(parts[1]) {
		  case "disparar":
			  GameData.networkListener.disparar(Integer.parseInt(parts[2]),  Integer.parseInt(parts[3]), Integer.parseInt(parts[4]));
			  break;
		  case "dispararrelease":
			  GameData.networkListener.dispararRelease(Integer.parseInt(parts[2]));
			  break;
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
		  case "direccion":
			  GameData.networkListener.actualizarDireccionJugador(Integer.parseInt(parts[2]), parts[3]);
			  break;
		  }
	  }

	  private boolean conectarCliente(DatagramPacket packet) {
		  if(clientesConectados < MAX_CLIENTES){
			  if(!clienteExiste(packet.getAddress(), packet.getPort())){
				  añadirCliente(packet);
				  enviarMensajeAlCliente("conexion" + specialChar + "exitosa" + specialChar + (clientesConectados-1),  packet.getAddress(), packet.getPort());
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

	  public void enviarMensajeAlCliente(String mensaje, InetAddress ip, int puerto){
		  byte[] data = mensaje.getBytes();
		  DatagramPacket paquete = new DatagramPacket(data, data.length, ip, puerto);
		  try {
			  socket.send(paquete);
//	  	          System.out.println("SERVIDOR: Mensaje enviado: " + mensaje);
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
		  System.out.println("Hilo servidor detenido.");
	  }
}

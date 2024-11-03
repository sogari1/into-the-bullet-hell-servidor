package com.intothebullethell.game.network;

import java.net.InetAddress;

public class Cliente {

    private InetAddress ip;
    private int port;
    private int number;

    public Cliente(InetAddress ip, int port, int number) {
        this.ip = ip;
        this.port = port;
        this.number = number;
    }

    public InetAddress getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public int getNumber() {
        return number;
    }

}
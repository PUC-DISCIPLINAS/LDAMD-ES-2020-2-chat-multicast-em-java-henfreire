package redes;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

public class Group {
    int port = 6789;
    MulticastSocket mSocket = null;
    InetAddress groupIp;
    boolean isChatting = true;
    String userName = "";

    public void enterGroup(String name, String address) {
        String msg = "*** " + name + " entrou no grupo... ***";
        this.userName = name;
        try {
            this.groupIp = InetAddress.getByName(address);
            this.mSocket = new MulticastSocket(this.port);
            this.mSocket.joinGroup(groupIp);
            this.sendMessage(msg);
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }

    public void sendMessage(String msg) {
        try {
            byte[] message = msg.getBytes();
            DatagramPacket messageOut = new DatagramPacket(message, message.length, this.groupIp, this.port);
            this.mSocket.send(messageOut);
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }

    public void showMessages() {
        //  Starts receiving messages thread
        Thread thread = new Thread(() -> {
            try {
                while (this.isChatting) {
                    byte[] buffer = new byte[1000];
                    DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length, this.groupIp, this.port);
                    this.mSocket.receive(messageIn);
                    String msg = new String(messageIn.getData()).trim();
                    System.out.println( new String(messageIn.getData()).trim());
                }
            } catch (IOException e) {
                System.out.println("IO: " + e.getMessage());
            }
        });
        thread.start();
    }

    public void exitGroup() {
        try {
            mSocket.leaveGroup(this.groupIp);
            this.isChatting = false;
            String msgExitGroup = "*** " + this.userName + " saiu do grupo... ***";
            this.sendMessage(msgExitGroup);
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }
}

package redes;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

public class GroupActions {
    int port = 6789;
    MulticastSocket mSocket = null;
    InetAddress groupIp;
    static boolean isFinished = false;

    public void enterGroup(String msg, String address) {
        try {
            this.groupIp = InetAddress.getByName(address);

            this.mSocket = new MulticastSocket(this.port);
            this.mSocket.joinGroup(groupIp);
            byte[] message = msg.getBytes();
            DatagramPacket messageOut = new DatagramPacket(message, message.length, groupIp, this.port);
            this.mSocket.send(messageOut);
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
                while (!isFinished) {
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
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }
}

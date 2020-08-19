package redes;
import jdk.nashorn.internal.parser.JSONParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	public static void main(String args[]) {

		ServerSocket listenSocket = null;


		try {
			// Porta do servidor
			int serverPort = 7896;
			
			// Fica ouvindo a porta do servidor esperando uma conexao.
			listenSocket = new ServerSocket(serverPort);
			System.out.println("Servidor: ouvindo porta TCP/7896.");

			while (true) {
				Socket clientSocket = listenSocket.accept();
				new Connection(clientSocket);
			}
		} catch (IOException e) {
			System.out.println("Listen socket:" + e.getMessage());
		} finally {
			if (listenSocket != null)
				try {
					listenSocket.close();
					System.out.println("Servidor: liberando porta TCP/7896.");
				} catch (IOException e) {
					/* close falhou */
				}
		}
	}

}

class Connection extends Thread {
	DataInputStream in;
	DataOutputStream out;
	Socket clientSocket;

	public Connection(Socket aClientSocket) {
		try {
			clientSocket = aClientSocket;
			in = new DataInputStream(clientSocket.getInputStream());
			out = new DataOutputStream(clientSocket.getOutputStream());
			this.start();
		} catch (IOException e) {
			System.out.println("Conex�o:" + e.getMessage());
		}
	}

	public void run() {
		try {
			ArrayList <String> groupList = new ArrayList<String>();
			while (true){
				String data = in.readUTF();
				System.out.println("Recebido: " + data);

				String params [] = data.split(",");

				if(params[0].equals("enter")){
					groupList.add(params[1]);
					String groupAddress = "228.5.6.7";
					out.writeUTF(groupAddress);
				} else if (params[0].equals("exit")){
					groupList.remove(params[1]);
				}

				System.out.println("Group List");
				groupList.forEach(System.out::println);
			}

		} catch (EOFException e) {
			System.out.println("EOF:" + e.getMessage());
		} catch (IOException e) {
			System.out.println("readline:" + e.getMessage());
		} finally {
			try {
				clientSocket.close();
				System.out.println("Servidor: fechando conex�o com cliente.");
			} catch (IOException e) {
				/* close falhou */
			}
		}

	}
}

package redes;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TCPServer {
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

	public void showGroups(ArrayList<Group> groups){
		groups.forEach(group -> System.out.println("Group:" + group.getName()));
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
		try { // servidor de repeti��o

			while (true){
				String data = in.readUTF(); // le a linha da entrada

				if(data.equals("mostrar grupos"))
				System.out.println("Recebido: " + data);
				out.writeUTF(data);
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

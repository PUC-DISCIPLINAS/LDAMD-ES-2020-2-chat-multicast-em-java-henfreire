package redes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TCPClient {
	public static void main(String args[]) {
		// arguments supply message and hostname
		Socket s = null;
		Scanner scan = new Scanner(System.in);
		try {
			int serverPort = 7896;
			s = new Socket("localhost", serverPort);

			DataInputStream in = new DataInputStream(s.getInputStream());
			DataOutputStream out = new DataOutputStream(s.getOutputStream());

			do{
				System.out.println("Digite:");
				String msg = scan.nextLine();
				out.writeUTF(msg);
				String data = in.readUTF(); // � uma linha do fluxo de dados
				System.out.println("Recebido: " + data);
			}while (true);


		} catch (UnknownHostException e) {
			System.out.println("Socket:" + e.getMessage());
		} catch (EOFException e) {
			System.out.println("EOF:" + e.getMessage());
		} catch (IOException e) {
			System.out.println("readline:" + e.getMessage());
		} finally {
			if (s != null)
				try {
					s.close();
				} catch (IOException e) {
					System.out.println("close:" + e.getMessage());
				}
		}
	}
}

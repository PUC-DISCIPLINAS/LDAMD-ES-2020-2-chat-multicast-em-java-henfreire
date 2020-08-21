package redes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Client {

	public static void main(String args[]) {
		Socket s = null;
		Scanner scan = new Scanner(System.in);
		String groupAddress;
		Group group =  new Group();
		try {
			int serverPort = 7896;
			s = new Socket("localhost", serverPort);

			DataInputStream in = new DataInputStream(s.getInputStream());
			DataOutputStream out = new DataOutputStream(s.getOutputStream());
			System.out.println("Qual seu nome: ");
			String name = scan.nextLine();
			out.writeUTF("enter," + name);
			String data = in.readUTF();
			groupAddress = data;
			System.out.println("Group: " + groupAddress);

			group.enterGroup(name, groupAddress);

			group.showMessages();
			do{
				String msg= scan.nextLine();
				if(msg.equals("exit group")){
					group.exitGroup();
					out.writeUTF("exit," + name);
					System.out.println("Até mais...");
					break;
				}else{
					group.sendMessage("[ "+ name + " ] " + msg);
				}

			}while (group.isChatting);

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
					System.exit(0);
				} catch (IOException e) {
					System.out.println("close:" + e.getMessage());
				}
		}
	}
}

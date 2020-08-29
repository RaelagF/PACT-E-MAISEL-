package level1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server
{
	public static void main(String[] args)
	{
		ServerSocket serversocket;
		Socket socket;
		
		Scanner sc = new Scanner(System.in);	// reads what the user writes in the console
		BufferedReader in;						// will read the socket input stream
		PrintWriter out;						// will write into the socket output stream
		
		try {
			InetAddress address = InetAddress.getLocalHost(); // the local address
			
			serversocket = new ServerSocket(
				5000,		// the port to listen to
				10,			// the max nb of simultaneous client connections
				address);	// the address of the server
			
			System.out.println("Server address: " + address + ":" + serversocket.getLocalPort());
			
			socket = serversocket.accept();	// waiting for a client connection
			
			System.out.println("Client connected: " + socket.getLocalAddress() + ":" + socket.getPort());
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream())) ;
			out = new PrintWriter(socket.getOutputStream());
			
			out.println("You are connected to the server at the address " + address + ":" + serversocket.getLocalPort());
			out.flush();
			
			String clientQuestion = "";
			String userInput = "";
			while(!userInput.equals("/close") && (clientQuestion = in.readLine()) != null)
			{
				if(!clientQuestion.isEmpty())
				{
					System.out.print("Client question: ");
					System.out.println(clientQuestion);
					
					System.out.print("Answer: ");
					userInput = sc.nextLine();
					if(!userInput.equals("/close"))
					{
						out.println(userInput);
						out.flush();
					}
					else
						System.out.println("You closed the connection");
				}
			}
			if(clientQuestion == null)
				System.out.println("The client closed the connection");
			
			sc.close();
			socket.close();
			serversocket.close();
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}

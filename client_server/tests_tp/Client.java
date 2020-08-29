package level1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client
{
	public static void main(String[] args)
	{
		Socket socket;
		
		Scanner sc = new Scanner(System.in);	// reads what the user writes in the console
		BufferedReader in;						// will read the socket input stream
		PrintWriter out;						// will write into the socket output stream
		
		try {
			InetAddress serverAddress = InetAddress.getLocalHost(); // the local address
			
			socket = new Socket(
					serverAddress,	// the address of the server
					5000);			// the port of the server we try to reach
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream())) ;
			System.out.println(in.readLine());
			out = new PrintWriter(socket.getOutputStream());
			
			String userInput;
			String serverAnswer = "";
			do {
				System.out.print("Question: ");
				userInput = sc.nextLine();
				if(!userInput.equals("/close"))
				{
					out.println(userInput);
					out.flush();
					serverAnswer = in.readLine();
					if(serverAnswer != null)
					{
						System.out.print("Server answer: ");
						System.out.println(serverAnswer);
					}
					else
						System.out.println("The server closed the connection");
				}
				else
					System.out.println("You closed the connection");
			} while(!userInput.equals("/close") && serverAnswer != null);
			
			sc.close();
			socket.close();
			
		} catch(ConnectException e) {
			System.out.println("Error: server not found at specified address.");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}

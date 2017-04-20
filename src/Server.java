import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server
{
	public static void main(String[] args) throws IOException
	{
		int number, temp;
		ServerSocket s1 = new ServerSocket(1342);
		
		Socket ss = s1.accept();	 //accept incoming request to server in this socket
		
		Scanner sc = new Scanner(ss.getInputStream());
		while(sc.hasNextInt())
		{
			 number = sc.nextInt();
			 temp = number * 2;
			
			//now pass this temp to client by using printstream object
			PrintStream p = new PrintStream(ss.getOutputStream());
			p.println(temp);
			
			//first run the server then client
		}
		
		
		
	}

}

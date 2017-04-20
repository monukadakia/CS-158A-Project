import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client 
{
	public static void main(String[] args) throws UnknownHostException, IOException
	{
		int num, temp;
		Scanner sc  = new Scanner(System.in);
		//specify ip and port number
		
		Socket socket = new Socket("127.0.0.1", 1342);
		Scanner in = new Scanner(socket.getInputStream());
		System.out.println("Enter any number");
		while(true)
		{
			num = sc.nextInt();
			PrintStream ps = new PrintStream(socket.getOutputStream());
			ps.println(num);
			 temp = in.nextInt();
			System.out.println(temp);
		}
		 
		
	}
 
}

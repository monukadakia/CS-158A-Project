import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client 
{
	int num, temp;
	Environment env;

	public Client(Environment e){
		env = e;
	}


	public void connect(String name){
		Scanner sc  = new Scanner(System.in);
		Socket socket = null;
		try {
			socket = new Socket("127.0.0.1", 1342);
			Scanner in = new Scanner(socket.getInputStream());
			PrintStream ps = new PrintStream(socket.getOutputStream());
			ps.println(name + " Connected");
			if(in.hasNextLine()){
				//TODO: It is not coming in here..Check this out
				String x = in.nextLine();
				env.displayWaitingScreen(x);
			}
			while(socket.isConnected())
			{
				if(in.hasNextLine()){
					//TODO: It is not coming in here..Check this out
					String x = in.nextLine();
					env.displayWaitingScreen(x);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
}
/*
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client
{
	public static void main(String[] args) throws UnknownHostException, IOException
	{
		Socket socket = new Socket("127.0.0.1", 1342);
		Scanner in = new Scanner(socket.getInputStream());
		PrintStream ps = new PrintStream(socket.getOutputStream());
		ps.println("Kirtan Connected");
		while(socket.isConnected())
		{
			if(in.hasNextLine()){
				String temp = in.nextLine();
				System.out.println(temp);
			}
		}


	}

}
 */

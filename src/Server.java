import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server
{
	public static void main(String[] args) throws IOException
	{
        System.out.println("Server Starting");
		ServerSocket s1 = new ServerSocket(1342);
		Socket ss = s1.accept();
		ArrayList<String> players = new ArrayList<>();
		Scanner sc = new Scanner(ss.getInputStream());
		System.out.println("Server Started!");
		while(!s1.isClosed())
		{
		    if(sc.hasNextLine()) {
		        String line = sc.nextLine();
                System.out.println(line);
                String name = "";
                if(line.contains("Connected")){
                    name = line.substring(0, players.indexOf(" Connected"));
                    players.add(name);
                }
                int i = 0;
                for(String s: players){
                    System.out.println("Player " + (++i) + ": " + s);
                }
                PrintStream p = new PrintStream(ss.getOutputStream());
                p.println("Hello " + name + "! Waiting for another opponent");
            }
		}
	}

}

/*
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
		while(!s1.isClosed())
		{
			if(sc.hasNextLine()){
				String line = sc.nextLine();
				System.out.println(line);
				String name = "";
				if(line.contains("Connected")){
					name = line.substring(0, line.indexOf(" Connected"));
				}
				PrintStream p = new PrintStream(ss.getOutputStream());
				p.println("Hello " + name + "!");
			}

			//first run the server then client
		}



	}

}
 */

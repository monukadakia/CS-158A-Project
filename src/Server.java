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
		ArrayList<String> players = new ArrayList<>();
		System.out.println("Server Started!");

		while(!s1.isClosed())
		{
            Socket ss = s1.accept();
            ss = s1.accept();
            Scanner sc = new Scanner(ss.getInputStream());
            PrintStream p = new PrintStream(ss.getOutputStream());
		    if(sc.hasNextLine()) {
		        String line = sc.nextLine();
                System.out.println(line);
                String name = "";
                if(line.startsWith("New Connection:")){
                    name = line.substring(15);
                    players.add(name);
                    p.println(name + " Connected");
                    p.println("Chat:" + name + " Connected");
                    int i = 0;
                    for(String s: players){
                        System.out.println("Player " + (++i) + ": " + s);
                    }
                }
                else if (line.startsWith("Chat:")){
                    p.println(line);
                }
                else if(line.startsWith("Connection Quit:")){
                    name = line.substring(16);
                    System.out.println(name + " Left");
                    p.println("Chat:" + name + " Left");
                }
                else{
                    System.out.println("Hi");
                }
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

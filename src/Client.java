import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
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
		try {
			final Socket socket = new Socket("127.0.0.1", 1342);
			Scanner in = new Scanner(socket.getInputStream());
			PrintStream ps = new PrintStream(socket.getOutputStream());
			ps.println("New Connection:" + name);
			if(in.hasNextLine() && in.nextLine().equals(name + " Connected")){
				env.displayWaitingScreen("Welcome " + name + "\n");
			}
			env.sendButton.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("It came in here");
					if(!env.chatArea.getText().trim().equals("")){
						System.out.println("It came in here as well");
						String message = env.chatArea.getText().trim();
						ps.println("Chat:" + name + ": " + message);
						env.chatArea.setText("");
					}
				}
			});
			env.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					ps.println("Connection Quit:" + name);
				}
			});
			while(socket.isConnected())
			{
				System.out.println("It is connected");
				if(in.hasNextLine()){
					String line = in.nextLine();
					System.out.println(line);
					if(line.startsWith("Chat:")){
						String parsed = line.replaceFirst("Chat:", "");
						System.out.println(parsed);
						env.chatDisplay.append(parsed + "\n");
					}
					else if(line.equals(name + " Connected")){
						env.displayWaitingScreen("Welcome " + name + "\n");
					}
				}
				else {
					ps.println("Connection from " + name + " is still established");
				}
			}
			System.out.println("It ended");
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

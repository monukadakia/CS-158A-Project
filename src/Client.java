import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.*;

/**
 * @author mohnishkadakia
 * This is where Client connect to the same Port that the Server is running on
 */
public class Client {
	
	/**
	 * These variables represent the creation of the board and information for the
	 * Client to connect using a socket
	 */
	private static JFrame frame = new JFrame("Connect 4");
	private JLabel mPassed = new JLabel("");
	private Square[][] gameEnv = new Square[6][7];
	private Square square;
	private ImageIcon img;
	private ImageIcon oppImg;
	private Socket socket;
	private BufferedReader br;
	private static PrintWriter pw;
	private static String name, serverAddress;
	private static TextArea displayChat;
	
	/**
	 * Setup constructor for setting the socket to the right port number.
	 * @param serverAddress is localhost
	 * @throws Exception
	 */
	public Client(String serverAddress) throws Exception {
		socket = new Socket(serverAddress, 1342);
		br = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		pw = new PrintWriter(socket.getOutputStream(), true);
		mPassed.setBackground(Color.lightGray);

		/**
		 * Here on the code actually creates the layout for the board and chatting window
		 * This is where the main 3 panel layout is made where 1st panel is for the game
		 * second panel is for the chatting window and the third panel is for the approppiate call
		 */
		JPanel boardPanel = new JPanel();
		boardPanel.setBackground(Color.blue);
		boardPanel.setLayout(new GridLayout(6, 7, 10, 10));
		for (int i = 0; i < gameEnv.length; i++) {
			for(int j = 0; j < gameEnv[i].length; j++) {
				gameEnv[i][j] = new Square();
				int finalI = i;
				int finalJ = j;
				gameEnv[i][j].addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
						int row = checkNextOpen(finalI, finalJ);
						square = gameEnv[row][finalJ];
						String temp = row + "," + finalJ;
//						System.out.println(temp);
						pw.println("MOVE " + temp);
					}

					public int checkNextOpen(int r, int c){
						for(int x = gameEnv.length-1; x >= 0; x--){
							if(!gameEnv[x][c].hasIcon()){
								return x;
							}
						}
						return r;
					}
				});
				boardPanel.add(gameEnv[i][j]);
			}
		}
		frame.getContentPane().add(boardPanel, "Center");
		JPanel finalPanel = new JPanel();
		finalPanel.setLayout(new BorderLayout());
		JPanel chatPanel = new JPanel();
		chatPanel.setLayout(new BorderLayout());
		displayChat = new TextArea();
		displayChat.setEditable(false);
		displayChat.setBackground(Color.LIGHT_GRAY);
		TextField chatWindow = new TextField();
		JButton sendButton = new JButton("Send");
		chatPanel.add(displayChat, BorderLayout.NORTH);
		chatPanel.add(chatWindow, BorderLayout.CENTER);
		chatPanel.add(sendButton, BorderLayout.SOUTH);
		finalPanel.add(chatPanel, BorderLayout.CENTER);
		finalPanel.add(mPassed, BorderLayout.SOUTH);
		frame.getContentPane().add(finalPanel, "South");
		sendButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!chatWindow.getText().trim().equals("")){
					String mess = "CHAT: " + name + ": " + chatWindow.getText().trim();
					pw.println(mess);
					chatWindow.setText("");
				}
			}
		});
		
		chatWindow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!chatWindow.getText().trim().equals("")){
					String mess = "CHAT: " + name + ": " + chatWindow.getText().trim();
					pw.println(mess);
					chatWindow.setText("");
				}
			}
		});
	}
	
	/**
	 * The client checks what the server has sent and compares it with one of the following commands
	 * and executes the one that matches. 
	 * @throws Exception
	 */
	public void play() throws Exception {
		String response;
		try {
			response = br.readLine();
			if (response.startsWith("WELCOME")) {
				char color = response.charAt(8);
				img = new ImageIcon(color == 'R' ? "src/r.png" : "src/y.png");
				oppImg  = new ImageIcon(color == 'R' ? "src/y.png" : "src/r.png");
				frame.setTitle("Connect 4: " + name + ": " + color);
			}
			while (true) {
				response = br.readLine();
				if (response.startsWith("VALID")) {
					mPassed.setText("Valid move, please wait");
					square.setIcon(img);
					square.repaint();
				} else if (response.startsWith("OPPONENT_MOVED")) {
					int locationa = Integer.parseInt(response.substring(15,16));
					int locationb = Integer.parseInt(response.substring(17,18));
					gameEnv[locationa][locationb].setIcon(oppImg);
					gameEnv[locationa][locationb].repaint();
					mPassed.setText("Opponent moved, your turn");
				} else if (response.startsWith("VICTORY")) {
					mPassed.setText("You win");
					break;
				} else if (response.startsWith("DEFEAT")) {
					mPassed.setText("You lose");
					break;
				} else if (response.startsWith("TIE")) {
					mPassed.setText("You tied");
					break;
				} else if (response.startsWith("DISPLAY")) {
					mPassed.setText(response.substring(8));
				} else if (response.startsWith("UNVALID MOVE")) {
					mPassed.setText("Unvalid Move");
				} else if(response.startsWith("CHAT")){
					String mess = response.substring(6);
					displayChat.append(mess + "\n");
				}
				else if(response.startsWith("CLEAR")){
					displayChat.setText("");
				}
			}
			pw.println("QUIT");
		}
		finally {
			socket.close();
		}
	}

	/**
	 * handles the scenario where it asks the users if they want to play again at the end of the game
	 * @return the option yes or no.
	 */
	private boolean wantsToPlayAgain() {
		int response = JOptionPane.showConfirmDialog(frame,
				"Do you want to play again?",
				"Connect 4",
				JOptionPane.YES_NO_OPTION);
		frame.dispose();
		return response == JOptionPane.YES_OPTION;
	}

	/*
	 * starts the actual program and runs the client by connecting to the server address localhost
	 */
	public static void main(String[] args) throws Exception {
		int x = 0, y = 0;
		String prev = "";
		while (true) {
			Client client;
			if(name == null){
				JFrame mainFrame = new JFrame();
				mainFrame.setLayout(new FlowLayout());
				mainFrame.setBounds(300,150,500,100);
				mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				mainFrame.setResizable(false);
				mainFrame.setLayout(new BorderLayout());
				JPanel panel = new JPanel();
				JLabel label = new JLabel("Your Name: ");
				JTextField textField = new JTextField();
				textField.setPreferredSize(new Dimension(200, 30));
				JButton submit = new JButton("Submit");
				panel.add(label);
				panel.add(textField);
				panel.add(submit);
				mainFrame.add(panel, BorderLayout.CENTER);
				mainFrame.setVisible(true);
				JTextField finalTextField = textField;
				JPanel finalPanel1 = panel;
				JLabel finalLabel1 = label;
				submit.addActionListener(new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (!finalTextField.getText().trim().equals("")) {
							finalPanel1.remove(finalLabel1);
							finalPanel1.remove(submit);
							finalPanel1.remove(finalTextField);
							name = finalTextField.getText().trim();
							mainFrame.remove(finalPanel1);
						}
					}
				});
				finalTextField.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (!finalTextField.getText().trim().equals("")) {
							finalPanel1.remove(finalLabel1);
							finalPanel1.remove(submit);
							finalPanel1.remove(finalTextField);
							name = finalTextField.getText().trim();
							mainFrame.remove(finalPanel1);
						}
						
					}
				});
				while(name==null){
					frame.setVisible(false);
				}
				panel = new JPanel();
				label = new JLabel("Server Address: ");
				textField = new JTextField();
				textField.setPreferredSize(new Dimension(200, 30));
				JButton submitIP = new JButton("Submit");
				panel.add(label);
				panel.add(textField);
				panel.add(submitIP);
				mainFrame.add(panel, BorderLayout.CENTER);
				mainFrame.setVisible(true);
				JTextField finalTextField1 = textField;
				JPanel finalPanel = panel;
				JLabel finalLabel = label;
				submitIP.addActionListener(new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (!finalTextField1.getText().trim().equals("")) {
							finalPanel.remove(finalLabel);
							finalPanel.remove(submit);
							finalPanel.remove(finalTextField1);
							serverAddress = finalTextField1.getText().trim();
							mainFrame.dispose();
						}
					}
				});
				

				finalTextField1.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (!finalTextField1.getText().trim().equals("")) {
							finalPanel.remove(finalLabel);
							finalPanel.remove(submit);
							finalPanel.remove(finalTextField1);
							serverAddress = finalTextField1.getText().trim();
							mainFrame.dispose();
						}
					}
				});
				while(serverAddress==null){
					frame.setVisible(false);
				}
			}
			frame = new JFrame("Connect 4");
			client = new Client(serverAddress);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setBounds(x,y,500, 700);
			frame.setVisible(true);
			frame.setResizable(false);
			frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent windowEvent) {
					System.exit(0);
				}
			});
			displayChat.setText(prev);
			client.play();
			if (!client.wantsToPlayAgain()) {
				break;
			}
			x = client.frame.getX();
			y = client.frame.getY();
			prev = displayChat.getText();
		}
	}
}
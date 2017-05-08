import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.*;

public class Client {

	private JFrame frame = new JFrame("Tic-Tac-Toe");
	private JLabel mPassed = new JLabel("");
	private Square[][] gameEnv = new Square[3][3];
	private Square square;
	private ImageIcon img;
	private ImageIcon oppImg;
	private static int PORT = 1342;
	private Socket socket;
	private BufferedReader br;
	private static PrintWriter pw;
	private static String name;
	private static TextArea displayChat;

	public Client(String serverAddress) throws Exception {
		socket = new Socket(serverAddress, PORT);
		br = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		pw = new PrintWriter(socket.getOutputStream(), true);
		mPassed.setBackground(Color.lightGray);

		JPanel boardPanel = new JPanel();
		boardPanel.setBackground(Color.black);
		boardPanel.setLayout(new GridLayout(3, 3, 2, 2));
		for (int i = 0; i < gameEnv.length; i++) {
			for(int j = 0; j < gameEnv[i].length; j++) {
				final String temp = i + "," +j;
				gameEnv[i][j] = new Square();
				int finalI = i;
				int finalJ = j;
				gameEnv[i][j].addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
						square = gameEnv[finalI][finalJ];
						pw.println("MOVE " + temp);
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
		TextArea chatWindow = new TextArea();
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
	}

	public void play() throws Exception {
		String response;
		try {
			response = br.readLine();
			if (response.startsWith("WELCOME")) {
				char mark = response.charAt(8);
				img = new ImageIcon(mark == 'X' ? "x.png" : "o.png");
				oppImg  = new ImageIcon(mark == 'X' ? "o.png" : "x.png");
				frame.setTitle("Tic-Tac-Toe " + name + ": " + mark);
			}
			while (true) {
				response = br.readLine();
				if (response.startsWith("VALID_MOVE")) {
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
				} else if (response.startsWith("MESSAGE")) {
					mPassed.setText(response.substring(8));
					if(mPassed.getText().contains("Waiting for opponent to connect")){
						displayChat.setText("");
					}
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

	private boolean wantsToPlayAgain() {
		int response = JOptionPane.showConfirmDialog(frame,
				"Do you want to play again?",
				"Tic Tac Toe is Fun Fun Fun",
				JOptionPane.YES_NO_OPTION);
		frame.dispose();
		return response == JOptionPane.YES_OPTION;
	}

	public static void main(String[] args) throws Exception {
		int x = 0, y = 0;
		String prev = "";
		while (true) {
			Client client = new Client("localhost");
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
				submit.addActionListener(new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (!textField.getText().trim().equals("")) {
							panel.remove(label);
							panel.remove(submit);
							panel.remove(textField);
							name = textField.getText().trim();
							mainFrame.dispose();
						}
					}
				});
				while(name==null){
					client.frame.setVisible(false);
				}
			}
			client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			client.frame.setBounds(x,y,500, 700);
			client.frame.setVisible(true);
			client.frame.setResizable(false);
			client.frame.addWindowListener(new WindowAdapter() {
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
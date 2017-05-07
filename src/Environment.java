import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Kirtan on 4/19/17.
 */
public class Environment extends JFrame{

    public static JButton tl, tm, tr, ml, mm, mr, bl, bm, br, sendButton;
    public static JTextArea chatDisplay, chatArea;

    public Environment(){
        /*JWindow window = new JWindow();
        JLabel label = new JLabel("Welcome to Tic-Tac-Toe", SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(30.0f));
        window.getContentPane().add(label);
        window.setBounds(300, 150, 500, 500);
        window.setVisible(true);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        window.setVisible(false);
        window.dispose();*/
        this.setVisible(true);
        this.setLayout(new FlowLayout());
        this.setBounds(300,150,500,100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
    }

    public String getUsername(){
        setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Your Name: ");
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(200, 30));
        JButton submit = new JButton("Submit");
        panel.add(label);
        panel.add(textField);
        panel.add(submit);
        add(panel, BorderLayout.CENTER);
        setVisible(true);
        final String[] name = {""};

        submit.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!textField.getText().trim().equals("")) {
                    panel.remove(label);
                    panel.remove(submit);
                    panel.remove(textField);
                    remove(panel);
                    name[0] = textField.getText().trim();
                    dispose();
                }
            }
        });

        return name[0];
    }

    public void displayWaitingScreen(String name){
        setLayout(new BorderLayout());
        JPanel window = new JPanel(new GridLayout(3,3));
        tl = new JButton();
        tm = new JButton();
        tr = new JButton();
        ml = new JButton();
        mm = new JButton();
        mr = new JButton();
        bl = new JButton();
        bm = new JButton();
        br = new JButton();
        window.add(tl);
        window.add(tm);
        window.add(tr);
        window.add(ml);
        window.add(mm);
        window.add(mr);
        window.add(bl);
        window.add(bm);
        window.add(br);
        add(window, BorderLayout.CENTER);
        JPanel chat = new JPanel(new BorderLayout());
        chatDisplay = new JTextArea(name);
        chatDisplay.setEditable(false);
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        chatDisplay.setBorder(BorderFactory.createCompoundBorder(border,
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        chatDisplay.setLineWrap(true);
        chatDisplay.setAutoscrolls(true);
        chatArea = new JTextArea(4,10);
        chatArea.setLineWrap(true);
        sendButton = new JButton("Send");
        chat.add(chatDisplay, BorderLayout.NORTH);
        chat.add(chatArea, BorderLayout.CENTER);
        chat.add(sendButton, BorderLayout.SOUTH);
        add(chat, BorderLayout.WEST);
        //JLabel label = new JLabel(name);
        //add(label, BorderLayout.SOUTH);
        setVisible(true);

    }

    public void requestServer(){
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Please run the server class first!\n\nWaiting for the server to start...");
        add(label, BorderLayout.CENTER);
        setVisible(true);
        while(available()){
        }
        dispose();

    }

    public static boolean available(){
        try (Socket ignored = new Socket("localhost", 8901)) {
            return false;
        } catch (IOException ignored) {
            return true;
        }
    }

}

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Created by Kirtan on 4/19/17.
 */
public class Environment extends JFrame{

    public Environment(){
        JWindow window = new JWindow();
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
        window.dispose();
        this.setVisible(true);
        this.setBounds(300,150,500,500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void displayWaitingScreen(){
        this.add(new JLabel("You are the first one here..waiting for more players"));
    }

}

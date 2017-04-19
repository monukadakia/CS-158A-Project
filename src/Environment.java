import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;

/**
 * Created by Kirtan on 4/19/17.
 */
public class Environment extends JFrame{

    JButton play, exit;

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
        this.setLayout(new FlowLayout());
        this.setBounds(300,150,500,500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
    }

    public void getUsername(){
        JTextField textField = new JTextField("Your Name");
        //textField.setToolTipText("Your Name");
        textField.setMinimumSize(new Dimension(200, 50));
        JButton submit = new JButton("Submit");
        add(textField);
        add(submit);
        setVisible(true);
        final String[] name = {""};
        submit.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!textField.getText().trim().equals("")){
                    remove(textField);
                    remove(submit);
                    displayWaitingScreen();
                }
            }
        });
    }

    public void displayWaitingScreen(){
        play = new JButton("Multiplayer Mode");
        play.setSize(50, 50);
        exit = new JButton("Quit");
        exit.setSize(50,50);
        this.add(play);
        this.add(exit);
        setVisible(true);
        //this.add(new JLabel("You are the first one here..waiting for more players", 0));
    }

}

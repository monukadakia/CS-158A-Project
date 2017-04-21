import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by Kirtan on 4/19/17.
 */
public class Environment extends JFrame{

    JButton play, exit, tl, tm, tr, ml, mm, mr, bl, bm, br;

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
        this.setBounds(300,150,500,500);
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
                }
            }
        });
        while(name[0].trim().equals("")){

        }
        return name[0];
    }

    public void displayWaitingScreen(String name){
        /*play = new JButton("Multiplayer Mode");
        play.setSize(50, 50);
        exit = new JButton("Quit");
        exit.setSize(50,50);
        this.add(play);
        this.add(exit);
        setVisible(true);
        //this.add(new JLabel("You are the first one here..waiting for more players", 0));*/
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
        JLabel label = new JLabel(name);
        add(label, BorderLayout.SOUTH);
        setVisible(true);

    }

    public void requestServer(){
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Please run the server class first!\n\nWaiting for the server to start...");
        add(label, BorderLayout.CENTER);
        setVisible(true);
        while(Application.available()){
        }
        remove(label);
    }

}

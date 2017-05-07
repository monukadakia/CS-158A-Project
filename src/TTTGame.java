import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This is where it sets up the game for both the players
 */
public class TTTGame {

    //This represents the board in form of a 2D array which has 9 positions. 
    private Player[][] gameEnv = new Player[3][3];
    Player currPlayer;

    /**
    * This method checks after each move whether one of the player is a winner or not.
    * It has a blueprint of all the possible wins that must be checked. 
    */
    public boolean checkWin() {
        return (gameEnv[0][0] != null && gameEnv[0][0] == gameEnv[0][1] && gameEnv[0][0] == gameEnv[0][2])
                        ||(gameEnv[1][0] != null && gameEnv[1][0] == gameEnv[1][1] && gameEnv[1][0] == gameEnv[1][2])
                        ||(gameEnv[2][0] != null && gameEnv[2][0] == gameEnv[2][1] && gameEnv[2][0] == gameEnv[2][2])
                        ||(gameEnv[0][0] != null && gameEnv[0][0] == gameEnv[1][0] && gameEnv[0][0] == gameEnv[2][0])
                        ||(gameEnv[0][1] != null && gameEnv[0][1] == gameEnv[1][1] && gameEnv[0][1] == gameEnv[2][1])
                        ||(gameEnv[0][2] != null && gameEnv[0][2] == gameEnv[1][2] && gameEnv[0][2] == gameEnv[2][2])
                        ||(gameEnv[0][0] != null && gameEnv[0][0] == gameEnv[1][1] && gameEnv[0][0] == gameEnv[2][2])
                        ||(gameEnv[0][2] != null && gameEnv[0][2] == gameEnv[1][1] && gameEnv[0][2] == gameEnv[2][0]);
    }

    /**
    * This method checks if the player move counts or not. It makes sure that 
    * it is not the same position as some pervious location.
    */
    public synchronized boolean checkMove(int locationa, int locationb, Player player) {
        if (player == currPlayer && gameEnv[locationa][locationb] == null) {
            gameEnv[locationa][locationb] = currPlayer;
            currPlayer = currPlayer.opp;
            currPlayer.oppMove(locationa, locationb);
            return true;
        }
        return false;
    }

    /**
    * This method checks if the board has any more space for player to make a move
    */
    public boolean hasSpace() {
        for (int i = 0; i < gameEnv.length; i++) {
            for(int j = 0; j < gameEnv[i].length; j++) {
                if (gameEnv[i][j] == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
    * This class has sockets and output streams for players
    */
    class Player extends Thread {
        char style;
        Player opp;
        Socket socket;
        BufferedReader br;
        PrintWriter pw;

        /**
        * Contructor for the socket and also the style represents 'X' or 'O'
        */
        public Player(Socket socket, char style) {
            this.socket = socket;
            this.style = style;
            try {
                br = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                pw = new PrintWriter(socket.getOutputStream(), true);
                pw.println("WELCOME " + this.style);
                pw.println("MESSAGE Waiting for opponent to connect");
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            }
        }

        /**
        * It always keeps track of what the opposite player's situation is.
        */
        public void oppMove(int locationa, int locationb) {
            pw.println("OPPONENT_MOVED " + locationa + "," + locationb);
            if(checkWin()){
                pw.println("DEFEAT");
            }
            else if(!hasSpace()){
                pw.println("TIE");
            }
            else{
                pw.println("");
            }
        }

        /**
        * This sets which player is the opponent
        */
        public void setOpponent(Player opp) {
            this.opp = opp;
        }

        /**
        * Based on all the messages, it runs according to the message it starts with. 
        */
        public void run() {
            try {
                pw.println("MESSAGE All players connected");
                if (style == 'X') {
                    pw.println("MESSAGE Your move");
                }
                while (true) {
                    String message = br.readLine();
                    if (message.startsWith("MOVE")) {
                        int locationa = Integer.parseInt(message.substring(5,6));
                        int locationb = Integer.parseInt(message.substring(7,8));
                        if (checkMove(locationa, locationb, this)) {
                            pw.println("VALID_MOVE");
                            pw.println(checkWin() ? "VICTORY"
                                    : !hasSpace() ? "TIE"
                                    : "");
                        } else {
                            pw.println("UNVALID MOVE");
                        }
                    } else if (message.startsWith("QUIT")) {
                        return;
                    }
                    else if(message.startsWith("CHAT:")){
                        currPlayer.pw.println(message);
                        currPlayer.opp.pw.println(message);
                    }
                    else if(message.startsWith("CLEAR")){
                        currPlayer.opp.pw.println(message);
                    }
                }
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This is where it sets up the game for both the players
 */
public class ConnectFourGame {

    //This represents the board in form of a 2D array which has 9 positions. 
    private Player[][] playerMoves = new Player[6][7];
    Player currPlayer;

    /**
    * This method checks after each move whether one of the player is a winner or not.
    * It has a blueprint of all the possible wins that must be checked. 
    */
    public boolean checkWin() {

        return checkHorizontal() || checkVertical() || checkDiagonal() || checkDiagonal2();
    }

    public boolean checkHorizontal(){
        for(int i = 0; i < playerMoves.length; i++){
            for(int j = 0; j < playerMoves[i].length-3;j++){
                if(playerMoves[i][j] != null &&
                        (playerMoves[i][j] == playerMoves [i][j+1] &&
                        playerMoves[i][j] == playerMoves [i][j+2] &&
                        playerMoves[i][j] == playerMoves [i][j+3])){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkVertical(){
        for(int i = 0; i < playerMoves.length-3; i++){
            for(int j = 0; j < playerMoves[i].length;j++){
                if(playerMoves[i][j] != null &&
                        (playerMoves[i][j] == playerMoves [i+1][j] &&
                                playerMoves[i][j] == playerMoves [i+2][j] &&
                                playerMoves[i][j] == playerMoves [i+3][j])){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkDiagonal(){
        for(int i = 0; i < playerMoves.length-3; i++){
            for(int j = 0; j < playerMoves[i].length-3;j++){
                if(playerMoves[i][j] != null &&
                        (playerMoves[i][j] == playerMoves [i+1][j+1] &&
                                playerMoves[i][j] == playerMoves [i+2][j+2] &&
                                playerMoves[i][j] == playerMoves [i+3][j+3])){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkDiagonal2(){
        for(int i = 0; i < playerMoves.length-3; i++){
            for(int j = playerMoves[i].length-1; j >= 3;j--){
                if(playerMoves[i][j] != null &&
                        (playerMoves[i][j] == playerMoves [i+1][j-1] &&
                                playerMoves[i][j] == playerMoves [i+2][j-2] &&
                                playerMoves[i][j] == playerMoves [i+3][j-3])){
                    return true;
                }
            }
        }
        return false;
    }

    /**
    * This method checks if the player move counts or not. It makes sure that 
    * it is not the same position as some pervious location.
    */
    public synchronized boolean checkMove(int locationa, int locationb, Player player) {
        if (player == currPlayer && playerMoves[locationa][locationb] == null) {
            playerMoves[locationa][locationb] = currPlayer;
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
        for (int i = 0; i < playerMoves.length; i++) {
            for(int j = 0; j < playerMoves[i].length; j++) {
                if (playerMoves[i][j] == null) {
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
        char color;
        Player opp;
        Socket socket;
        BufferedReader br;
        PrintWriter pw;

        /**
        * Contructor for the socket and also the style represents 'X' or 'O'
        */
        public Player(Socket socket, char color) {
            this.socket = socket;
            this.color = color;
            try {
                br = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                pw = new PrintWriter(socket.getOutputStream(), true);
                pw.println("WELCOME " + this.color);
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
                if (color == 'X') {
                    pw.println("MESSAGE Your move");
                }
                while (true) {
                    String message = br.readLine();
                    if (message.startsWith("MOVE")) {
                        int locationa = Integer.parseInt(message.substring(5,6));
                        int locationb = Integer.parseInt(message.substring(7,8));
                        if (checkMove(locationa, locationb, this)) {
                            pw.println("VALID");
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

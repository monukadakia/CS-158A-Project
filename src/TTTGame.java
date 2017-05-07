import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Kirtan on 5/6/17.
 */
public class TTTGame {

    private Player[][] gameEnv = new Player[3][3];

    Player currentPlayer;

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

    public synchronized boolean checkMove(int locationa, int locationb, Player player) {
        if (player == currentPlayer && gameEnv[locationa][locationb] == null) {
            gameEnv[locationa][locationb] = currentPlayer;
            currentPlayer = currentPlayer.opp;
            currentPlayer.oppMove(locationa, locationb);
            return true;
        }
        return false;
    }

    class Player extends Thread {
        char style;
        Player opp;
        Socket socket;
        BufferedReader br;
        PrintWriter pw;

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

        public void setOpponent(Player opp) {
            this.opp = opp;
        }

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
                        pw.println(message);
                    }
                }
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            } finally {
                try {socket.close();} catch (IOException e) {}
            }
        }
    }
}

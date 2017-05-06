import java.net.ServerSocket;

public class Server {

    public static void main(String[] args) throws Exception {
        System.out.println("Starting the Sever");
        ServerSocket socket = new ServerSocket(1342);
        System.out.println("Server started");
        try {
            while (true) {
                TTTGame game = new TTTGame();
                TTTGame.Player player1 = game.new Player(socket.accept(), 'X');
                TTTGame.Player player2 = game.new Player(socket.accept(), 'O');
                player1.setOpponent(player2);
                player2.setOpponent(player1);
                game.currentPlayer = player1;
                player1.start();
                player2.start();
            }
        } finally {
            socket.close();
        }
    }
}
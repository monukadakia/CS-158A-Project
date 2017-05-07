import java.net.ServerSocket;

public class Server {

    /**
    * This is where the game starts and connects the players on the same server. 
    */
    public static void main(String[] args) throws Exception {
        System.out.println("Attempting to start the Sever.");
        ServerSocket socket = new ServerSocket(1342);
        System.out.println("The Server is running now.");
        try {
            while (true) {
                TTTGame game = new TTTGame();
                TTTGame.Player playerOne = game.new Player(socket.accept(), 'X');
                TTTGame.Player playerTwo = game.new Player(socket.accept(), 'O');
                playerOne.setOpponent(playerTwo);
                playerTwo.setOpponent(playerOne);
                game.currPlayer = playerOne;
                playerOne.start();
                playerTwo.start();
            }
        } finally {
            socket.close();
        }
    }
}
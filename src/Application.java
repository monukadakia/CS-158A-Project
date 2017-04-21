import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;

public class Application {

    public static void main(String[] args) {
        Environment env = new Environment();
        if(!available()){
            String name = env.getUsername();
            Client client = new Client(env);
            client.connect(name);
        }
        else{
            env.requestServer();
            String name = env.getUsername();
            Client client = new Client(env);
            client.connect(name);
        }
    }

    public static boolean available(){
        try (Socket ignored = new Socket("localhost", 1342)) {
            return false;
        } catch (IOException ignored) {
            return true;
        }
    }
}

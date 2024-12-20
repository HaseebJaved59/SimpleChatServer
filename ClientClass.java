import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientClass {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java Client <ID> <Server_IP> <Server_Port>");
            System.exit(1);
        }

        String clientId = args[0];
        String serverIP = args[1];
        int serverPort = Integer.parseInt(args[2]);

        try {
            Socket socket = new Socket(serverIP, serverPort);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // Send client ID to the server
            writer.println(clientId);

            // Start a separate thread to receive messages from the server
            new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = serverReader.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Read user input and send messages to the server
            String userInput;
            while ((userInput = reader.readLine()) != null) {
                writer.println(userInput);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

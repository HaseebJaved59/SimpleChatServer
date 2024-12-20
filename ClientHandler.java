import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ServerClass server;
    private BufferedReader reader;
    private PrintWriter writer;
    private String clientId;

    public ClientHandler(Socket clientSocket, ServerClass server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream(), true);

            // Read client ID
            clientId = reader.readLine();

            // Add client to the server
            server.addClient(clientId, this);

            // Listen for messages from the client
            String message;
            while ((message = reader.readLine()) != null) {
                server.broadcastMessage(clientId, message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Handle client disconnection
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
    }
}

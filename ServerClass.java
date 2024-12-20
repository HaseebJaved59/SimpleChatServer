import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerClass {
    private ServerSocket serverSocket;
    private Map<String, ClientHandler> clients;

    public ServerClass(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        clients = new HashMap<>();
        System.out.println("Server started on port " + port);
    }

    public void start() {
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addClient(String clientId, ClientHandler clientHandler) {
        clients.put(clientId, clientHandler);
        System.out.println("Client " + clientId + " connected.");
    }

    public void broadcastMessage(String senderId, String message) {
        for (Map.Entry<String, ClientHandler> entry : clients.entrySet()) {
            if (!entry.getKey().equals(senderId)) {
                entry.getValue().sendMessage(senderId + ": " + message);
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Server <port>");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);

        try {
            ServerClass server = new ServerClass(port);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

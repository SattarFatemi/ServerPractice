package server;

import client.Client;
import com.sun.java.accessibility.util.AccessibilityListenerList;

import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private final List<ClientHandler> clientHandlers;
    private ServerStatus serverStatus;
    private final OrderHandler orderHandler;

    public Server() {
        clientHandlers = new ArrayList<>();
        this.orderHandler = new OrderHandler(clientHandlers);
        this.serverStatus = ServerStatus.WAITING;
    }

    public void init() {

        System.out.println("Server is running...");

        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            while (true) {

                System.out.println("Waiting for a connection...");
                Socket socket = serverSocket.accept();

                addNewClientHandler(socket);
                System.out.println("===== There are " + clientHandlers.size() + " online! =====");
            }

        } catch (IOException e) {

        }
    }

    private void addNewClientHandler(Socket socket) throws IOException {

        ClientHandler clientHandler = new ClientHandler(clientHandlers.size(), socket, orderHandler);

        if (serverStatus == ServerStatus.WAITING) {

            System.out.println("New connection established.");

            clientHandlers.add(clientHandler);
            new Thread(clientHandler).start();

            if (clientHandlers.size() == 5) {
                startGame();
            }
        } else {
            clientHandler.sendMessage("Server is full!");
            clientHandler.kill();
            clientHandlers.remove(clientHandler);
        }

    }

    private void startGame() {
        System.out.println("Game is started!");
        serverStatus = ServerStatus.STARTED;
        // start the game
    }
}

class OrderHandler {
    private final List<ClientHandler> clientHandlers;

    public OrderHandler(List<ClientHandler> clientHandlers) {
        this.clientHandlers = clientHandlers;
    }

    public void sendMessageFromClientToAnotherClient(int from, int to, String message) {
        clientHandlers.get(to).sendMessage("New message from client " + from + ": " + message);
    }
}

enum ServerStatus {
    WAITING,
    STARTED
}
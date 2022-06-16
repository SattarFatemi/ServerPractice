package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final PrintWriter out;
    private final int id;
    private final OrderHandler orderHandler;

    ClientHandler(int id, Socket socket, OrderHandler orderHandler) throws IOException {
        this.id = id;
        this.socket = socket;
        this.orderHandler = orderHandler;
        out = new PrintWriter(socket.getOutputStream());
    }

    @Override
    public void run() {

        System.out.println("New ClientHandler is running...");
        try {
            System.out.println("ClientHandler is waiting for a message...");

            Scanner in = new Scanner(socket.getInputStream());

            while (true) {
                String messageFromClient = in.nextLine();
                System.out.println("Message from client: " + messageFromClient);
                String[] order = messageFromClient.split("-");
                if (order[0].equals("SEND_MESSAGE_TO_A_CLIENT")) {
                    int to = Integer.parseInt(order[1]);
                    String message = order[2];
                    orderHandler.sendMessageFromClientToAnotherClient(id, to, message);
                } else {
                    sendMessage("You are client number " + id);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendMessage(String message) {
        out.println(message);
        out.flush();
    }

    public void kill() throws IOException {
        socket.close();
    }
}

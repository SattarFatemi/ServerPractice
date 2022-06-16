package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {

    private final int kind;

    public Client(int kind) {
        this.kind = kind;
    }

    private void init(int kind) throws IOException {
        Socket socket = new Socket("localhost", 8080);
        Scanner in = new Scanner(socket.getInputStream());
        PrintWriter out = new PrintWriter(socket.getOutputStream());

        if (kind == -1) {
            out.println("SEND_MESSAGE_TO_A_CLIENT-0-Hello client. My name is Sattar.");
        } else {
            out.println("This is a message from client");
        }
        out.flush();

        while (true) {
            String input = in.nextLine();
            System.out.println("Message from server: " + input);
        }
    }

    @Override
    public void run() {
        try {
            init(kind);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

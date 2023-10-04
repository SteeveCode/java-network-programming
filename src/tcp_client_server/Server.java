package tcp_client_server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private static ServerSocket serverSocket;
    private static final int PORT = 1234;
    public static void main(String[] args) {
        System.out.println("OPENING PORT: " + PORT + "\n");
        try {
            serverSocket = new ServerSocket(PORT); // STEP 1
        } catch (IOException e){
            System.out.println("UNABLE TO ATTACHE PORT ! EXCEPTION : " + e.getMessage());
            System.exit(1);
        }
        do {
            handleClient();
        }while(true);
    }
    private static void handleClient(){
        Socket link = new Socket(); // STEP 1

        try{
            link = serverSocket.accept(); // STEP 2
            Scanner input = new Scanner(link.getInputStream()); // STEP 3
            PrintWriter output = new PrintWriter(link.getOutputStream(),true); // STEP 3

            int numMessages = 0;
            String message = input.nextLine(); // STEP 4

            while (!message.equals("CLOSE")){
                System.out.println("MESSAGE RECEIVED: " + message);
                numMessages++;
                output.println("MESSAGE NUM " + numMessages + " : " + message); // STEP 4
                message = input.nextLine();
            }
            output.println(numMessages + " messages received"); // STEP 4

        }catch (IOException e){
            e.printStackTrace();
            System.out.println(e.getMessage());

        }
        finally {
            try{
                System.out.println("\n *CLOSING SERVER CONNECTION...*");
                link.close(); // STEP 5
                System.exit(1);
            }catch (IOException e){
                System.out.println("UNABLE TO DISCONNECT ! : " + e.getMessage());
                System.exit(1);
            }
        }
    }

}

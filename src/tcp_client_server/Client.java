package tcp_client_server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private static InetAddress host;
    private static final int PORT = 1234;

    public static void main(String[] args)  {
        try{
            host = InetAddress.getLocalHost();
        } catch (UnknownHostException unknownHostException){
            System.out.println("ERROR CAUSED : " + unknownHostException.getMessage());
            System.exit(1);
        }
        assessServer();
    }
    public static void assessServer(){
        Socket link = new Socket(); // STEP 1
        try{
            link = new Socket(host, PORT); // STEP 1
            Scanner input = new Scanner(link.getInputStream()); // STEP 2
            PrintWriter output = new PrintWriter(link.getOutputStream(), true); // STEP 2
            Scanner userEntry = new Scanner(System.in);
            String message, response;

            do{
                System.out.println("ENTER MESSAGE : ");
                message = userEntry.nextLine();
                output.println(message); // STEP 3
                response = input.nextLine(); // STEP 3
                System.out.println("\nSERVER > " + response);
            } while (!message.equals("CLOSE"));

        }catch (IOException e){
            e.printStackTrace();
        }
        finally {
            try{
                System.out.println("\n *CLOSING CLIENT CONNECTION...*");
                link.close(); // STEP 4
            }catch (IOException e){
                System.out.println("UNABLE TO DISCONNECT : " + e.getMessage());
                System.exit(1);
            }
        }
    }
}

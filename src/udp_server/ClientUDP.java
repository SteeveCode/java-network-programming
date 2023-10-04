package udp_server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ClientUDP {
    private static final int PORT = 1234;
    private static InetAddress host;
    private static DatagramSocket datagramSocket;
    private static DatagramPacket inPacket, outPacket;
    private static byte[] buffer;
    private static String username;

    public static void main(String[] args) {
        try {
        host = InetAddress.getLocalHost();
    }catch (IOException e){
        System.out.println("ERROR : " + e.getMessage());
        System.exit(1);
    }
    accessServer();
    }
    public static void accessServer(){
        try {
            datagramSocket = new DatagramSocket(); // STEP 1
            Scanner userEntry = new Scanner(System.in);
            String clientUsername;
            String message = "", response = "";

            System.out.println("Enter Username : ");
            username = userEntry.nextLine();
            outPacket = new DatagramPacket(username.getBytes(),username.length(), host, PORT);
            datagramSocket.send(outPacket);

            buffer = new byte[256];
            inPacket = new DatagramPacket(buffer, buffer.length);
            datagramSocket.receive(inPacket);
            clientUsername = new String(inPacket.getData(), 0, inPacket.getLength());


            do{
                System.out.println("Enter Message : ");
                message = userEntry.nextLine();
                outPacket = new DatagramPacket(message.getBytes(),message.length(), host, PORT); // STEP 2
                datagramSocket.send(outPacket); // STEP 3

                buffer = new byte[256]; //STEP 4
                inPacket = new DatagramPacket(buffer, buffer.length); // STEP 5
                datagramSocket.receive(inPacket); // STEP 6
                response = new String(inPacket.getData(), 0, inPacket.getLength()); // STEP 7
                System.out.println(clientUsername + " Response : " + response);
//                System.out.println("\n SERVER RESPONSE > " + response);
//                System.out.println("MESSAGE : " + username);

            }while (!message.equals("CLOSE") || !response.equals("CLOSE"));

        }catch (IOException e){
            e.printStackTrace();
            System.out.println("ERROR : " + e.getMessage());
        }
        finally {
            System.out.println("\n Connection for client " + username +" is being disconnected");
            datagramSocket.close(); // STEP 8
        }
    }
}

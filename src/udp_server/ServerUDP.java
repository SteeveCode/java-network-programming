package udp_server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;
/*
See the steps require to set up a UDP connection after the code below
 */

public class ServerUDP {
    private static final int PORT = 1234;
    private static DatagramSocket datagramSocket;
    private static DatagramPacket inPacket, outPacket;
    private static byte[] buffer;
    private static String username;

    public static void main(String[] args) throws IOException {
        System.out.println("Opening port on " + PORT + "...");
        try {
            datagramSocket = new DatagramSocket(PORT);
        }catch (IOException e){
            System.out.println("UNABLE TO OPEN PORT");
            System.out.println(e.getMessage());
            System.exit(1);
        }
        handleClient();
    }
    public static void handleClient(){
        try {
            String clientUsername, messageIn, messageOut;
//            int numMessages = 0;
            InetAddress clientAddress = null;
            int clientPort;

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter username");



            buffer = new byte[256];
            inPacket = new DatagramPacket(buffer, buffer.length);
            datagramSocket.receive(inPacket);
            clientUsername = new String(inPacket.getData(), 0, inPacket.getLength());
            clientAddress = inPacket.getAddress();
            clientPort = inPacket.getPort();

            username = scanner.nextLine();
            outPacket = new DatagramPacket(username.getBytes(),username.length(),clientAddress, clientPort);
            datagramSocket.send(outPacket);


            do{
                buffer = new byte[256]; // STEP 2
                inPacket = new DatagramPacket(buffer, buffer.length); // STEP 3
                datagramSocket.receive(inPacket); // STEP 4

                clientAddress = inPacket.getAddress(); // STEP 5
                clientPort = inPacket.getPort();

                messageIn = new String(inPacket.getData(), 0, inPacket.getLength()); // STEP 6
                System.out.println(clientUsername + " response: " + messageIn);
//                numMessages++;
                messageOut = scanner.nextLine();
//                messageOut = "Message " + numMessages + " : " + messageIn;

                outPacket = new DatagramPacket(messageOut.getBytes(),messageOut.length(),clientAddress, clientPort); // STEP 7
                datagramSocket.send(outPacket); // STEP 8
//                System.out.println("MESSAGE : " + messageOut);
            }while (!messageIn.equals("CLOSE") || !messageOut.equals("CLOSE"));
        }catch (IOException e){
            e.printStackTrace();
        }
        finally {
            System.out.println("\n Connection for client " + username +" is being disconnected");
            datagramSocket.close();
        }
    }
}
/*
*       // STEP 1 create datagram socket object
        DatagramSocket datagramSocket = new DatagramSocket(1234);
        // STEP 2 create buffer for incoming datagrams
        byte[] buffer = new byte[256];
        // STEP 3 create datagram packet object for incoming datagrams
        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
        // STEP 4 accept an incoming datagram
        datagramSocket.receive(datagramPacket);
        // STEP 5 accept the sender's address and port from the packet
        InetAddress clientAddress = datagramSocket.getInetAddress();
        // STEP 6 retrieve the data from the buffer using an overloaded form of the String constructor with 3 arguments
        String message = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
        * datagramPacket.getData() = byte array, 0 = start position, datagramPacket.getLength() = full size of the buffer
        // STEP 7  create the response datagram
        DatagramPacket outPacket = new DatagramPacket(message.getBytes(), message.length(), clientAddress, datagramSocket.getPort());
        // STEP 8 send the response datagram
        datagramSocket.send(outPacket);
        // STEP 9 close the datagram socket
        datagramSocket.close();
* */

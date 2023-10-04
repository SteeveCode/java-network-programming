package single_client_server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class NewServer {
    private ServerSocket serverSocket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public NewServer() throws IOException {
        serverSocket = new ServerSocket(456);
        System.out.println("Server running on port 456");
    }
    public void startServer() throws IOException {
        Socket socket = serverSocket.accept();
        System.out.println("Connection established!");
        System.out.println("To exit chat, type \"end\" ");
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        Scanner keyboard = new Scanner(System.in);

        String messageToSend = "";
        String  messageReceived = "";

        while (socket.isConnected()) {
           try {
               messageToSend = keyboard.nextLine();
               if(!messageToSend.equals("end")){
                   bufferedWriter.write(messageToSend);
                   bufferedWriter.newLine();
                   bufferedWriter.flush();
               } else{
                   bufferedWriter.write(messageToSend);
                   bufferedWriter.newLine();
                   bufferedWriter.flush();
                   System.exit(1);
               }

               messageReceived = bufferedReader.readLine();
              if(!messageReceived.equals("end")){
                  System.out.println(messageReceived);
              } else{
                  System.out.println(messageReceived);
                  System.exit(1);
              }
           }catch (IOException e){
               e.printStackTrace();
               closeAll(bufferedWriter, bufferedReader, socket);
               break;
           }
        }
    }
    public void closeAll(BufferedWriter bufferedWriter, BufferedReader bufferedReader,  Socket socket) throws IOException {
        if(bufferedWriter != null){
            bufferedWriter.close();
        }
        if(bufferedReader != null){
            bufferedReader.close();
        }
        if(socket != null){
            socket.close();
        }
    }

    public static void main(String[] args) throws IOException {
        NewServer server = new NewServer();
        server.startServer();
    }
}

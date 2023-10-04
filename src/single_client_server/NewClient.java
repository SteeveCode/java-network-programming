package single_client_server;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class NewClient {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    public NewClient(Socket socket) throws IOException {
        this.socket = socket;
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }
    public void startClient() throws IOException {
        String messageFromServer;
        String messageToSend;

           try {
               do{
                   messageFromServer = bufferedReader.readLine();
                   if(!messageFromServer.equals("end")){
                       System.out.println(messageFromServer);
                   } else{
                       System.out.println(messageFromServer);
                       System.exit(1);

                   }

                   Scanner keyboard = new Scanner(System.in);
                   messageToSend = keyboard.nextLine();
                  if(!messageToSend.equals("end")){
                      bufferedWriter.write(messageToSend);
                      bufferedWriter.newLine();
                      bufferedWriter.flush();
                  }
                  else{
                      bufferedWriter.write(messageToSend);
                      bufferedWriter.newLine();
                      bufferedWriter.flush();
                      System.exit(1);
                  }
               } while (socket.isConnected());
           } catch (IOException e){
               e.printStackTrace();
               closeAll(bufferedWriter, bufferedReader, socket);
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
        Socket socket = new Socket("localhost", 456);
        System.out.println("Server connection is successful");
        System.out.println("To exit chat, type \"end\" ");
        NewClient client = new NewClient(socket);
        client.startClient();
    }
}

package group_chat_app;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class MyChatClient {
    private static final int PORT = 1234;
    Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    public MyChatClient(Socket socket, String  username) throws IOException {
       try {
           this.socket = socket;
           this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
           this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
           this.username = username;

       }catch (IOException e){
           System.out.println("There was a problem creating the client object");
           closeEverything(socket,bufferedReader, bufferedWriter);

       }
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try{
            if(socket != null){
                socket.close();
            }
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    public void sendMessage() throws IOException {
        bufferedWriter.write(username);
        bufferedWriter.newLine();
        bufferedWriter.flush();

        Scanner scanner = new Scanner(System.in);

        while(socket.isConnected()){
            String messageToSend = scanner.nextLine();
            bufferedWriter.write(username + " : " + messageToSend);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }
    }
    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run(){
                String messageFromGroupChat;

                while (socket.isConnected()){
                    try {
                        messageFromGroupChat = bufferedReader.readLine();
                        System.out.println(messageFromGroupChat);
                    }catch (IOException e){
                        closeEverything(socket,bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", PORT);
        System.out.println("Successfully connected to the server");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username for the group chat: ");
        String username = scanner.nextLine();

        MyChatClient myChatClient = new MyChatClient(socket, username);
        myChatClient.listenForMessage();
        myChatClient.sendMessage();
    }
}

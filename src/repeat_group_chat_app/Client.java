package repeat_group_chat_app;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    public Client(Socket socket,String username )throws IOException {
        this.socket = socket;
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.username = username;
    }
    public void sendMessage(){
        String messageToSend;
        Scanner scanner = new Scanner(System.in);
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            while (socket.isConnected()){
                messageToSend = scanner.nextLine();
                bufferedWriter.write(messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }

        }catch (IOException e){
            closeEverything( socket, bufferedReader, bufferedWriter);
        }
    }
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter)  {
        try {
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromGroupChat;
              while(socket.isConnected()){
                  try {
                      messageFromGroupChat = bufferedReader.readLine();
                      System.out.println(messageFromGroupChat);

                  } catch (IOException e) {
                      closeEverything( socket, bufferedReader, bufferedWriter);
                  }
              }
            }
        }).start();
    }

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 123);
        System.out.println("Connection established!");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username");
        String username = scanner.nextLine();

        Client client = new Client(socket, username);
        client.listenForMessage();
        client.sendMessage();
    }
}

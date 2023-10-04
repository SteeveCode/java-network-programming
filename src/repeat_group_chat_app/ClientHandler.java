package repeat_group_chat_app;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable{
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;
    public static List<ClientHandler> clientHandlers = new ArrayList<>();
    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.bufferedReader = new BufferedReader( new InputStreamReader(socket.getInputStream()));
        this.bufferedWriter= new  BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.clientUsername = bufferedReader.readLine();
        clientHandlers.add(this);
        broadcastMessage("SERVER : " + clientUsername + " has joined the chat!");

    }
    @Override
    public void run(){
        String messageFromClient;
        while (socket.isConnected()){
            try {
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(clientUsername + " : " + messageFromClient);
            }catch (IOException e){
                closeEverything( socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    private void broadcastMessage(String messageFromClient)  {
       try {
           for (ClientHandler clientHandler: clientHandlers){
               if(!clientHandler.clientUsername.equals(clientUsername)){
                   clientHandler.bufferedWriter.write(messageFromClient);
                   clientHandler.bufferedWriter.newLine();
                   clientHandler.bufferedWriter.flush();
               }
           }
       }catch (IOException e){
           closeEverything( socket, bufferedReader, bufferedWriter);
       }
    }
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter)  {
        removeClient();
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
    public void removeClient(){
        clientHandlers.remove(this);
        broadcastMessage("SERVER : " + clientUsername + " has let the chat!");
    }
}

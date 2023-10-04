package group_chat_app;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {
    public Socket socket;
    public static List<ClientHandler> clientHandlers = new ArrayList<>();
    String clientUsername;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    public ClientHandler(Socket socket) throws IOException {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            clientUsername = bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMessage("SERVER : " + clientUsername + " has entered the chat!");
        }catch (IOException e){
            closeEverything(this.socket,bufferedReader, bufferedWriter);
        }
    }
    @Override
    public void run(){
        String messageFromClient;

        while (socket.isConnected()){
        try {
            messageFromClient = bufferedReader.readLine();
             broadcastMessage(messageFromClient);
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
            break;
            }
        }
    }
    public void broadcastMessage(String messageToSend)  {
        for(ClientHandler client: clientHandlers){
            try {
                if (!client.clientUsername.equals(clientUsername))
                {
                    client.bufferedWriter.write(messageToSend);
                    client.bufferedWriter.newLine();
                    client.bufferedWriter.flush();
                }
            } catch (IOException e){
                closeEverything(this.socket,bufferedReader, bufferedWriter);
            }
        }
    }
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter)  {
        removeClientHandler();
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
    public void removeClientHandler()  {
        clientHandlers.remove(this);
        broadcastMessage("SERVER : " + clientUsername + " has left the chat!");
    }
}

package group_chat_app;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MyChatServer {
    ServerSocket serverSocket;
    public MyChatServer() throws IOException {
       serverSocket = new ServerSocket(1234);
    }
    public void startServer(){
        try{
            while (!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected!");

                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }

        }catch (IOException e){
            closeServerSocket();
        }
    }
    public void closeServerSocket(){
        try{
            if(serverSocket != null){serverSocket.close();}
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        MyChatServer myChatServer = new MyChatServer();
        myChatServer.startServer();
    }
}

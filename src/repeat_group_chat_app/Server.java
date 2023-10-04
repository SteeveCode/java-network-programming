package repeat_group_chat_app;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final ServerSocket serverSocket;

    public Server()throws IOException {
        serverSocket = new ServerSocket(123);
    }
    public void startServer()throws IOException{
       while (!serverSocket.isClosed()){
           Socket socket = serverSocket.accept();
           System.out.println("A client has connected!");
           ClientHandler clientHandler = new ClientHandler(socket);
           Thread thread = new Thread(clientHandler);
           thread.start();
       }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.startServer();
    }
}

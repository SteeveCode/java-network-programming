package gui_chat_app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class GUIChatServer {
    Vector<String> users = new Vector<String>();
    Vector<HandleClient> clients = new Vector<HandleClient>();
    public void process() throws Exception{
        ServerSocket server = new ServerSocket(1234, 10);
        System.out.println("Server Started : ");
        while(true){
            Socket client = server.accept();
            HandleClient c = new HandleClient(client);
            clients.add(c);
        }
    }
    public static void main(String[] args) throws Exception {
        new GUIChatServer().process();
    }
    public void broadcast(String user, String message){
        //send message to all users
        for(HandleClient c : clients){
            if(!c.getUsername().equals(user)){
                c.sendMessage(user, message);
            }
        }
    }
    class HandleClient extends Thread{
        String name = "";
        BufferedReader input;
        PrintWriter output;
        public HandleClient(Socket client) throws IOException {
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            output = new PrintWriter(client.getOutputStream(), true);
            name = input.readLine();
            users.add(name);
            start();
        }

        public void run(){
            String line;
            try{
                while(true){
                    line = input.readLine();
                    if(line.equals("end")){
                        clients.remove(this);
                        users.remove(name);
                        break;
                    }
                    broadcast(name, line);
                }
            }catch (IOException e){
                System.out.println(e.getMessage());
            }
        }
        public void sendMessage(String uname, String msg){
            System.out.println(uname + " - " + msg);
        }
        public String getUsername(){
            return name;
        }
    }
}
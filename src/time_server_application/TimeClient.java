package time_server_application;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class TimeClient {
    public static void main(String[] args) {
        SocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 5000);
        try(SocketChannel socketChannel = SocketChannel.open(socketAddress)){
            ByteBuffer byteBuffer = ByteBuffer.allocate(64);
            int bytesRead = socketChannel.read(byteBuffer);
            while(bytesRead > 0){
                byteBuffer.flip();
                while(byteBuffer.hasRemaining()){
                    System.out.print((char)byteBuffer.get());
                }
                System.out.println("\n");
                bytesRead = socketChannel.read(byteBuffer);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

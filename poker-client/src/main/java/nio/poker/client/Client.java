package nio.poker.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {
    private InetAddress inetAddress;
    private int port;
    private SocketChannel socketChannel;

    Client(InetAddress inetAddress, int port) throws IOException {
        this.inetAddress = inetAddress;
        this.port = port;
        socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(this.inetAddress, this.port));
    }

    private void send() throws IOException {
        String str = "Message from client";
        socketChannel.write(ByteBuffer.wrap(str.getBytes()));
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client(null, 8080);
        client.send();
    }
}

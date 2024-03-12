package nio.poker.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class Server {
    private Selector selector;
    private ServerSocketChannel socketChannel;
    private InetAddress inetAddress;
    private int port;
    private ByteBuffer byteBuffer;

    Server(InetAddress inetAddress, int port) throws IOException {
        this.inetAddress = inetAddress;
        this.port = port;
        selector = Selector.open();
        socketChannel = ServerSocketChannel.open();
        socketChannel.bind(new InetSocketAddress(this.inetAddress, this.port));
        socketChannel.configureBlocking(false);
        socketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        byteBuffer = ByteBuffer.allocate(2048);
        run();
    }

    private void run() throws IOException {
        while (true) {
            this.selector.select();
            Iterator<SelectionKey> selectionKeyIterator = this.selector.selectedKeys().iterator();
            while (selectionKeyIterator.hasNext()) {
                SelectionKey selectionKey = selectionKeyIterator.next();
                selectionKeyIterator.remove();
                if (selectionKey.isValid() && selectionKey.isAcceptable()) {
                    accept();
                } else if (selectionKey.isValid() && selectionKey.isReadable()) {
                    read(selectionKey);
                }
            }
        }
    }

    private void accept() throws IOException {
        SocketChannel socketChannel = this.socketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        try {
            int read = socketChannel.read(byteBuffer);
            if (read == -1) {
                socketChannel.close();
            } else {
                byteBuffer.flip();
                Charset charset = StandardCharsets.UTF_8;
                CharsetDecoder decoder = charset.newDecoder();
                CharBuffer charBuffer = decoder.decode(byteBuffer);
                System.out.println(charBuffer);
                byteBuffer.clear();
            }
        }
        catch (IOException exception){
            System.out.println("Client disconnected");
            socketChannel.close();
        }
    }

    public static void main(String[] args) throws IOException {
        new Server(null, 8080);
    }
}

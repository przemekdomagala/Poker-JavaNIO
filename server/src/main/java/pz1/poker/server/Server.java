package pz1.poker.server;

import lombok.Setter;
import pz1.poker.common.*;
import pz1.poker.model.Game;

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
import java.util.HashMap;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**Class representing Server.**/
public class Server{
    /**HashMap used to map IDs to channels.**/
    @Setter
    private static HashMap<SocketChannel, Integer> map = new HashMap<>();
    /**Java NIO selector.**/
    private Selector selector;
    /**Server's socket channel.**/
    private ServerSocketChannel socketChannel;
    /**Server's inetAddress.**/

    private final InetAddress address;
    /**Port that Server is on.**/
    private final int port;
    /**Buffer holding received from Client tokens.**/
    protected ByteBuffer buffer;
    /**Poker Game that is run on the server.**/
    protected Game game;
    /**Int containing number of Clients that used method ServerManager.onConnect.**/
    protected int connections;
    /**Boolean tracking whether game is finished (All players but one folded.).**/
    protected boolean finished;
    /**Int containing number of Clients that user method ServerManager.onFinished.**/
    protected int finishedCount;
    /**For While Loop.**/
    private boolean isRunning;
    /**Logger used when catching Exception.**/
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    /**Number of currently connected clients.**/
    protected int connectedClients;

    /**Constructor.
     * @param port Port that Server is running on localhost.
     * @param inetAddress Server's inet address.
     * @throws IOException =Exception constructor throws when I/O operations are failed/interrupted**/
    Server(InetAddress inetAddress, int port) throws IOException {
        this.address = inetAddress;
        this.port = port;
        init();
        run();
    }

    /**Method that initialize Server starting parameters.
     * @throws IOException =Exception constructor throws when I/O operations are failed/interrupted**/
    private void init() throws IOException {
        isRunning = true;
        selector = Selector.open();
        socketChannel = ServerSocketChannel.open();
        socketChannel.bind(new InetSocketAddress(this.address, this.port));
        socketChannel.configureBlocking(false);
        socketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        buffer = ByteBuffer.allocate(2048);
        System.out.println("Game server started IP: localhost:8080");
        game = new Game();
        game.setNext(-1);
        connections = 0;
        finished = false;
        finishedCount = 0;
        connectedClients = 0;
    }

    /**Method that runs the server.**/
    protected void run(){
        while (isRunning) {
            try {
                this.selector.select();
                Iterator<SelectionKey> selectedKeys = this.selector.selectedKeys().iterator();
                while (selectedKeys.hasNext()) {
                    SelectionKey key = selectedKeys.next();
                    selectedKeys.remove();
                    if (!key.isValid()) {
                        continue;
                    }
                    if (key.isAcceptable()) {
                        this.accept();
                    }
                    if (key.isReadable()) {
                        this.read(key);
                    }
                }
            } catch (IOException e) {
                logger.error("An IO exception occurred", e);
                isRunning = false;
            }
        }
    }

    /**Method used to accept new Clients.
     * @throws IOException =Exception constructor throws when I/O operations are failed/interrupted**/
    private void accept() throws IOException {
        SocketChannel client = socketChannel.accept();
        map.put(client, this.connectedClients + 1);
        game.getPlayers().get(this.connectedClients).setId(this.connectedClients + 1);
        this.connectedClients++;
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }

    /**Method used to read Clients Message.
     * @throws IOException Exception constructor throws when I/O operations are failed/interrupted
     * @param key Key assigned with certain channel.**/
    private void read(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        try{
            int read = client.read(buffer);
            if(read == -1){
                client.close();
                System.out.println("Client ID: "+ map.get(key.channel()) +" disconnected.");
            }
            else {
                buffer.flip();
                Charset charset = StandardCharsets.UTF_8;
                CharsetDecoder decoder = charset.newDecoder();
                CharBuffer charBuffer = decoder.decode(buffer);
                messageHandler(charBuffer, key);
                buffer.clear();
            }
        } catch (IOException e){
            System.out.println("Client disconnected.");
            client.close();
        }
    }

    /**Method handling received message.
     * @throws IOException Exception constructor throws when I/O operations are failed/interrupted
     * @param buffer buffer containing receiver message.
     * @param key key assigned to certain channel.**/
    private void messageHandler(CharBuffer buffer, SelectionKey key) throws IOException {
        PassedTokens passedTokens = MyJSONParser.parse(buffer.toString());
        String responseStr = new String(buffer.array()).trim();
        int id = map.get(key.channel());
        if(this.finished && this.finishedCount!=0 && passedTokens.getActionType()!=ActionType.BET && passedTokens.getActionType()!=ActionType.CHECK){
            passedTokens.setActionType(ActionType.FINISHED);
        }
        if(id!=game.getNext() && game.getNext()!=-1){
            passedTokens.setActionType(ActionType.NOT_YOUR_TURN);
        }
        switch (passedTokens.getActionType()){
            case CONNECT -> ServerManager.onConnect(key, id, this);
            case DEAL -> ServerManager.onDeal(key, id, this);
            case FIRST_CHECK_HAND -> ServerManager.onFirstCheckHand(key, id, this);
            case CHECK_HAND -> ServerManager.onCheckHand(key, id, this);
            case NOT_YOUR_TURN -> ServerManager.onIncorrectTurn(key, id, this);
            case DISCONNECT -> ServerManager.onDisconnect(key, id, this);
            case BET -> ServerManager.onBet(key, id, this, MyJSONParser.extractParameters(responseStr));
            case NEXT_ROUND -> ServerManager.onNextRound(key, id, this);
            case CHECK -> ServerManager.onCheck(key, id, this);
            case ANTE -> ServerManager.onAnte(key, id, this);
            case TRADE -> ServerManager.onTrade(key, id, this, MyJSONParser.extractParameters(responseStr));
            case STAND_POT -> ServerManager.onStandPot(key, id, this);
            case SHOW_HAND -> ServerManager.onShowHand(key, id, this);
            case SHOW_RESULT -> ServerManager.onShowResult(key, id, this);
            case FOLD -> ServerManager.onFold(key, id, this);
            case WAIT -> ServerManager.onWait(key, id, this);
            case NEXT_ROUND_FOLDED -> ServerManager.onNextRoundFolded(key, id, this, MyJSONParser.extractParameters(responseStr));
            case FINISHED -> ServerManager.onFinished(key, id, this);
        }
    }

    /**Method used to send response to Client.
     * @throws IOException Exception constructor throws when I/O operations are failed/interrupted
     * @param key key assigned to channel.
     * @param passedTokens Tokens that Server sends to Client.**/
    protected void send(SelectionKey key, PassedTokens passedTokens) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        client.write(MyJSONParser.stringify(passedTokens));
    }

    public static void main(String[] args) throws IOException {
        new Server(null, 8080);
    }
}

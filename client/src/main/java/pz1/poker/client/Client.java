package pz1.poker.client;

import pz1.poker.common.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**Client connecting to the Server.**/
public class Client extends Player{
    /**Client's inet address.**/
    private final InetAddress address;
    /**Port that Client is connecting to.**/
    private final int port;
    /**Channel that Client uses to communicate with Server.**/
    private SocketChannel socketChannel;
    /**Int used to manipulate through game.**/
    private int moveNumber = 1;
    /**String printed out when Client writes invalid command in the terminal.**/
    private final String INVALID_INPUT = "INVALID_INPUT";
    /**String used to compare whether user inputs correct command for hand check.**/
    private final String CHECK_HAND = "check hand";
    /**Logger helping with catching Exception.**/
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    /**Class constructor.
     * @param port Port that Client connects to.
     * @param inetAddress inetAddress Client connects to.
     * @throws IOException Exception constructor throws when I/O operations are failed/interrupted.**/
    Client(InetAddress inetAddress, int port) throws IOException {
        this.address = inetAddress;
        this.port = port;
        init();
        run();
    }

    /**Method initializing Client's channel.
     * @throws IOException Exception constructor throws when I/O operations are failed/interrupted.**/
    private void init() throws IOException {
        socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(this.address, this.port));
    }

    /**Method used to run Client and handle moves in game.**/
    protected void run() {
        System.out.println("Connect to the game using 'connect' command.\n");
        while (moveNumber==1){
            connectHandler();
        }
        while(moveNumber==2){
            if(this.getRole()!=PlayerType.DEALER){
                System.out.println("Ante is 50 coins. Bet ante using bet 50 command.");
                anteHandler();
            }
            if(this.getRole().equals(PlayerType.DEALER)){
                System.out.println("Bet 50 and Deal Cards using betANDdeal command.");
                anteHandler();
            }
        }
        while (moveNumber==3){
            System.out.println("Check your cards using check hand command.");
            firstCheckHandler();
        }
        while(moveNumber==4){
            bettingRoundHandler();
        }
        while(moveNumber==5){
            System.out.println("Trade up to three cards with trade or stand pot.");
            tradeRoundHandler();
        }
        while (moveNumber==6){
            bettingRoundHandler();
        }
        while (moveNumber==7){
            showHandHandler();
        }
        while (moveNumber==8){
            gameResultHandler();
        }
        while (moveNumber==9){
            disconnectHandler();
        }
    }

    /**Same as run method but for players that have folded.**/
    private void foldedRun() {
        while (moveNumber==4){
            foldedBetHandler();
        }
        while (moveNumber==5){
            String command = MyScanner.nextLine();
            nextRoundFoldedRunHandler(command, "");
        }
        while (moveNumber==6){
            foldedBetHandler();
        }
        while (moveNumber==7){
            String command = MyScanner.nextLine();
            nextRoundFoldedRunHandler(command, "");
        }
        while (moveNumber==8){
            String command = MyScanner.nextLine();
            nextRoundFoldedRunHandler(command, "");

        }
        while (moveNumber==9){
            disconnectHandler();
        }
    }

    /**Method handling betting round for Clients that have folded.**/
    private void foldedBetHandler() {
        String command = MyScanner.nextLine();
        if(command.equals("next")){
            nextRoundFoldedRunHandler(command, "null");
        }
        if(command.equals("wait")){
            waitHandler(command);
        }
    }

    /**Method handling folded Player's waiting where other Players are in betting round.**/
    private void waitHandler(String command) {
        if(command.equals("wait")){
            send(new PassedTokens(this.getId(), ActionType.WAIT, ""), false);
        }
        else{
            invalidCommandHandler();
        }
    }

    /**Method handling situation where Client inputs invalid command.**/
    private void invalidCommandHandler(){
        String INVALID_COMMAND = "Invalid command.";
        System.out.println(INVALID_COMMAND);
    }

    /**Method handling going to next move for Player that folded.**/
    private void nextRoundFoldedRunHandler(String command, String parameters){
        if(command.equals("next")){
            send(new PassedTokens(this.getId(), ActionType.NEXT_ROUND_FOLDED, parameters), true);
        }
        else{
            invalidCommandHandler();
        }
    }

    /**Method handling call for result to the Server from the Client.**/
    private void gameResultHandler() {
        System.out.println("Check game result using show result command.");
        String command = MyScanner.nextLine();
        if(command.equals("show result")){
            send(new PassedTokens(this.getId(), ActionType.SHOW_RESULT, ""), true);
        }
        else{
            invalidCommandHandler();
        }
    }

    /**Method handling showing your hand on the table after the game.**/
    private void showHandHandler() {
        System.out.println("You can show what's on your hand using show hand command, so it will be evaluated.");
        String command = MyScanner.nextLine();
        if(command.equals("show hand")){
            send(new PassedTokens(this.getId(), ActionType.SHOW_HAND, ""), true);
        }
        else{
            invalidCommandHandler();
        }
    }

    /**Method handling betting round.**/
    private void bettingRoundHandler(){
        String command = MyScanner.nextLine();
        switch (command) {
            case "bet" -> {
                int amount;
                while (true) {
                    System.out.print("Amount of Coins you want to bet: ");
                    if (MyScanner.hasNextInt()) {
                        amount = MyScanner.nextInt();
                        break;
                    } else {
                        System.out.println(INVALID_INPUT);
                        MyScanner.nextLine();
                    }
                }
                betHandler(amount);
            }
            case "check" -> checkHandler();
            case "next" -> nextRoundHandler();
            case "fold" -> foldHandler();
            default -> invalidCommandHandler();
        }
    }

    /**Method handling Player's fold.**/
    private void foldHandler() {
        send(new PassedTokens(this.getId(), ActionType.FOLD, ""), false);
        this.folded=true;
        System.out.println("Now you have to wait until the end of the game using wait and next commands.");
        foldedRun();
    }

    /**Method handling trading round.**/
    private void tradeRoundHandler() {
        String command = MyScanner.nextLine();
        switch (command) {
            case "trade" -> {
                int amount = validateInt();
                List<Integer> used = new ArrayList<>();
                StringBuilder buffer = new StringBuilder();
                validateIntTrade(used, buffer, amount);
                send(new PassedTokens(this.getId(), ActionType.TRADE, buffer), true);
            }
            case "stand pot" -> {
                send(new PassedTokens(this.getId(), ActionType.STAND_POT, ""), true);
                System.out.println("Round of bets started.");
            }
            case CHECK_HAND -> send(new PassedTokens(this.getId(), ActionType.CHECK_HAND, ""), false);
            default -> invalidCommandHandler();
        }
    }

    /**Method handling valid int input for trade and bet.**/
    private int validateInt(){
        int amount;
        while (true) {
            System.out.print("Amount of Cards you want to trade: ");
            if (MyScanner.hasNextInt()) {
                amount = MyScanner.nextInt();
                return amount;
            } else {
                System.out.println(INVALID_INPUT);
                MyScanner.nextLine();
            }
        }
    }

    /**Method validating ints after Player decided to Trade.**/
    private void validateIntTrade(List<Integer> used, StringBuilder buffer, int amount){
        for (int i = 0; i < amount; i++) {
            System.out.println("Pick from a list:");
            int tmp;
            while (true) {
                if (MyScanner.hasNextInt()) {
                    tmp = Integer.parseInt(MyScanner.nextLine());
                    while (used.contains(tmp) || tmp < 1 || tmp > 5) {
                        System.out.println("invalid choice");
                        tmp = MyScanner.nextInt();
                    }
                    buffer.append(tmp);
                    used.add(tmp);
                    break;
                } else {
                    System.out.println(INVALID_INPUT);
                    MyScanner.nextLine();
                }
            }
        }
    }

    /**Method handling betting ante.**/
    private void anteHandler() {
        String command = MyScanner.nextLine();
        if(ClientManager.extract(command).equals("bet") && ClientManager.amount(command)==50 && this.role!=PlayerType.DEALER){
            send(new PassedTokens(this.getId(), ActionType.ANTE, ""), true);
        }
        else if(command.equals("betANDdeal")){
            send(new PassedTokens(this.getId(), ActionType.DEAL, ""), true);
        }
        else{
            invalidCommandHandler();
        }
    }

    /**Method handling going to next round of Game after betting round is finished.**/
    private void nextRoundHandler() {
        send(new PassedTokens(this.getId(), ActionType.NEXT_ROUND, ""), true);
    }

    /**Method handling Player's ask for check.**/
    private void checkHandler() {
        send(new PassedTokens(this.getId(), ActionType.CHECK, ""), false);
    }

    /**Method handling Player's request to bet.**/
    private void betHandler(int amount) {
        send(new PassedTokens(this.getId(), ActionType.BET, String.valueOf(amount)), false);
    }

    /**Method handling request to check your cards after dealing.**/
    private void firstCheckHandler() {
        String command = MyScanner.nextLine();
        if(command.equals(CHECK_HAND)){
            send(new PassedTokens(this.getId(), ActionType.FIRST_CHECK_HAND, ""), true);
            System.out.println("Round of bets started.");
            System.out.println("Operate through betting round with bet check fold and next commands.");
        }
        else{
            invalidCommandHandler();
        }
    }

    /**Method handling request to disconnect.**/
    private void disconnectHandler() {
        String command = MyScanner.nextLine();
        if(command.equals("disconnect")){
            send(new PassedTokens(this.getId(), ActionType.DISCONNECT, ""), true);
        }
        else if(command.equals(CHECK_HAND)){
            send(new PassedTokens(this.getId(), ActionType.CHECK_HAND, ""), false);
        }
        else{
            invalidCommandHandler();
        }
    }

    /**Method handling request to connect.**/
    private void connectHandler() {
        String command = MyScanner.nextLine();
        if(command.equals("connect")){
            send(new PassedTokens(this.getId(), ActionType.CONNECT, ""), true);
        }
        else{
            invalidCommandHandler();
        }
    }

    /**Method handling sending and receiving Signals from Server.**/
    private void send(PassedTokens passedTokens, boolean nextRound) {
        try {
            ByteBuffer buffer = MyJSONParser.stringify(passedTokens);
            socketChannel.write(buffer);
            buffer.clear();
            buffer = ByteBuffer.allocate(2048);
            String responseStr;
            socketChannel.read(buffer);
            responseStr = new String(buffer.array()).trim();
            if(!MyJSONParser.getAction(responseStr).equals("NOT_YOUR_TURN") && nextRound){
                moveNumber++;
            }
            if(MyJSONParser.getAction(responseStr).equals("CONNECT")) this.setRole(ClientManager.extractPlayerRole(responseStr));
            if(MyJSONParser.extractParameters(responseStr).equals("Game Finished")){
                moveNumber=7;
            }
            System.out.println(MyJSONParser.extractParameters(responseStr));
            buffer.clear();
            this.setId(MyJSONParser.extractID(responseStr));
        } catch (IOException e){
            logger.error("An IO exception occurred", e);
        }
    }

    public static void main(String[] args) throws IOException {
        new Client(null, 8080);
    }
}

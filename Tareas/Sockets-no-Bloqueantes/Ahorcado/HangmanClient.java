import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.channels.*;
import java.nio.*;
import java.net.*;
import java.util.Iterator;

public class EcoC {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int pto = 7;
        int msgNumber = 0;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            SocketChannel cl = SocketChannel.open();
            cl.configureBlocking(false);
            Selector sel = Selector.open();
            cl.connect(new InetSocketAddress(host, pto));
            cl.register(sel, SelectionKey.OP_CONNECT);

            while (true) {
                sel.select();
                Iterator<SelectionKey> it = sel.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey k = (SelectionKey) it.next();
                    it.remove();
                    if (k.isConnectable()) {
                        SocketChannel ch = (SocketChannel) k.channel();
                        if (ch.isConnectionPending()) {
                            try {
                                ch.finishConnect();
                                showWelcomeMessages();
                            } catch (Exception e) {
                                e.printStackTrace();
                            } // catch
                        } // if_conectionpending
                    // ch.configureBlocking(false);
                        ch.register(sel, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        continue;
                    } // if
                    if (k.isWritable()) {
                        SocketChannel ch2 = (SocketChannel) k.channel();
                        String msj;
                        if(msgNumber == 0){
                            msgNumber = 1;
                        }
                        while((msj = br.readLine()).isEmpty()){
                            System.out.println("Please type a caracter and hit ENTER.");
                        }
                        ByteBuffer b = ByteBuffer.wrap(msj.getBytes());
                        ch2.write(b);
                        if (msj.equalsIgnoreCase("EXIT")) {
                            System.out.println("Game Over");
                            ch2.close();
                            System.exit(0);
                        } else {
                            k.interestOps(SelectionKey.OP_READ);
                            continue;
                        } // else
                    } else if (k.isReadable()) {
                        SocketChannel ch2 = (SocketChannel) k.channel();
                        ByteBuffer b = ByteBuffer.allocate(1024);
                        b.clear();
                        int n = ch2.read(b);
                        b.flip();
                        String eco = new String(b.array(),0,n);
                        if(msgNumber == 1){
                            System.out.println("Guess the string:");
                            msgNumber = 2;
                        }
                        System.out.println(eco);
                        if(n > 8){
                            if(eco.substring(n-8,n).equals("YOU LOSE")){
                                System.out.println("Game Over :C"); 
                                ch2.close();
                                System.exit(0);
                            }else if(eco.substring(n-7,n).equals("YOU WIN")){
                                System.out.println("Congrats :D!!!");
                                ch2.close();
                                System.exit(0);
                            }
                        }
                        k.interestOps(SelectionKey.OP_WRITE);
                        continue;
                    } // if
                } // while
            } // while
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// main

    static void showWelcomeMessages(){
        System.out.println("Welcome to the Hangman game.");
        System.out.println("Type 'EXIT' if you want to leave the game.");
        System.out.println("Select your difficult");
        System.out.println("1 - Easy");
        System.out.println("2 - Intermediate");
        System.out.println("3 - Hard");
        System.out.print("Type your choice: ");
        System.out.println("Then type a caracter and press ENTER to play.");
    }
}
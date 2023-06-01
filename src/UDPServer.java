import java.io.IOException;
import java.net.*;

public class UDPServer {

    private static final int LOCAL_PORT = 6969;
    private static final String LOCAL_IP = "localhost";

    public static void main(String[] args) {


        try {
            // socket aufmachen
            System.out.println("horche auf port: " + LOCAL_PORT);
            DatagramSocket socket= new DatagramSocket(LOCAL_PORT,InetAddress.getByName(LOCAL_IP));
            byte[] buffer = new byte[1024];
            DatagramPacket postkarte = new DatagramPacket(buffer, buffer.length);
            socket.receive(postkarte);

            // postkarte ausgeben
            String data= new String(postkarte.getData(), 0, postkarte.getLength());
            System.out.println("bekommen: " + data);

            // socket schließen
            System.out.println("beende das Horchen!!!");
            socket.close();

        } catch (IOException e) {
            System.out.println("FEHLER: " + e.getMessage());
        }


    }

}

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class UDPServer {
    private static final int LOCAL_PORT = 6969;
    private static final String LOCAL_IP = "localhost";
    private Map<String, Room> rooms;
    private Random random;

    public UDPServer(){
        rooms = new HashMap<>();
        random = new Random();
    }

    public void start() {
        try {
            System.out.println("horche auf port: " + LOCAL_PORT);
            DatagramSocket socket = new DatagramSocket(LOCAL_PORT, InetAddress.getByName(LOCAL_IP));

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);

                new Thread(() -> handleRequest(request)).start();
            }

        } catch (IOException e) {
            System.out.println("FEHLER: " + e.getMessage());
        }
    }

    public String createRoomId(){
        String roomId;
        do{
            roomId = String.valueOf(random.nextInt(1000));
        }while(rooms.containsKey(roomId));
        return roomId;
    }

    public String createRoom(String roomId){
        Room newRoom = new Room();
        rooms.put(roomId,newRoom);
        return roomId;
    }

    public void joinRoom(String roomId, InetSocketAddress client){
        Room room = rooms.get(roomId);
        if(room != null){
            room.addClient(client);
        }else{
            System.out.println("Raum nicht gefunden!\nID: "+roomId);
        }
    }

    public void leaveRoom(String roomId, InetSocketAddress client){
        Room room = rooms.get(roomId);
        if(room != null){
            room.getClients().remove(client);
            if(room.isEmpty()){
                rooms.remove(roomId);
            }
        }
    }

    public void handleRequest(DatagramPacket request) {
        String message = new String(request.getData(), 0, request.getLength());
        if (message.equals("CREATE_ROOM")) {
            System.out.println("CREATE_ROOM von "+ request.getAddress() +":"+ request.getPort());
            String roomId = createRoom(createRoomId());
            sendResponse(roomId, request.getAddress(), request.getPort());
        } else if (message.startsWith("JOIN_ROOM:")) {
            String roomId = message.substring("JOIN_ROOM:".length());
            System.out.println("JOIN_ROOM von "+ request.getAddress() +":"+ request.getPort());
            if(rooms.containsKey(roomId)){
                joinRoom(roomId, new InetSocketAddress(request.getAddress(), request.getPort()));
                // checken ob mehr als 2 spieler im raum sind
                sendResponse("Raum gefunden!", request.getAddress(), request.getPort());
            }

        }
    }

    public void sendResponse(String message, InetAddress address, int port) {
        try {
            byte[] buffer = message.getBytes();
            DatagramPacket response = new DatagramPacket(buffer, buffer.length, address, port);
            DatagramSocket socket = new DatagramSocket();
            socket.send(response);
            socket.close();
        } catch (IOException e) {
            System.out.println("FEHLER: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        new UDPServer().start();
    }

}

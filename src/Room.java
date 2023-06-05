import java.net.*;
import java.util.*;

class Room {
    private List<InetSocketAddress> clients;

    public Room() {
        clients = new ArrayList<>();
    }

    public void addClient(InetSocketAddress client) {
        clients.add(client);
    }

    public List<InetSocketAddress> getClients() {
        return clients;
    }

    // Überprüfen, ob der Raum leer ist (keine Clients)
    public boolean isEmpty() {
        return clients.isEmpty();
    }
}


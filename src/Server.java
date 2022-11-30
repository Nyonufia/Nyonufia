import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    public void startServer() throws IOException {
        while (!serverSocket.isClosed()){
            Socket socket = serverSocket.accept();
            System.out.println("Un nouveau client s'est connecté ");
            ClientConnect clientConnect = new ClientConnect(socket);

            Thread thread = new Thread(clientConnect);
            thread.start();
        }
    }
    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}

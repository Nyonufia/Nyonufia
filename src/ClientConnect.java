import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientConnect implements Runnable{

    public static ArrayList<ClientConnect> clientConnect = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    //Pour connecter un nouveau client
    public ClientConnect(Socket socket){
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            clientConnect.add(this);
            broadcastMessage("SERVER : " +clientUsername + " a rejoint le chat; ");
        } catch (IOException e) {
            closeEverything (socket, bufferedReader, bufferedWriter);
        }
    }

    //Diffusion du message d'un client connecté
    @Override
    public void run() {
        String messageFromClient;

        while (socket.isConnected()){
            try {
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }

        }
    }
    private void broadcastMessage(String message) {
        for (ClientConnect clientConnect : clientConnect){
            try {
                    if (!clientConnect.clientUsername.equals(clientUsername)) {
                        clientConnect.bufferedWriter.write(message);
                        clientConnect.bufferedWriter.newLine();
                        clientConnect.bufferedWriter.flush();
                    }
                } catch (IOException e) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
        }
    }

    public void removeClientConnect(){
        clientConnect.remove(this);
        broadcastMessage("SERVER : " + clientUsername + " a quitté le chat;");
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClientConnect();
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null){
                bufferedWriter.close();
            }
            if (socket!=null){
                socket.close();
            }
            } catch (IOException e) {
                e.printStackTrace();

            }
    }
}

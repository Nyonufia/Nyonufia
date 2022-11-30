import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client(Socket socket, String username){
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        } catch (IOException e) {
            closeEverything (socket, bufferedReader, bufferedWriter);
        }
    }
    public void sendMessage(){
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            // Pour récupérer et afficher les données entrées
            //System.in signifie que les données seront fourni via le clavier
            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()){
                String message = scanner.nextLine();
                //Afficher le message envoyé
                bufferedWriter.write(username + " : " + message);
                bufferedWriter.newLine();//Séparation des lignes
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            closeEverything (socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromChatGroup;
                while (socket.isConnected()){
                    try {
                        msgFromChatGroup = bufferedReader.readLine();
                        System.out.println(msgFromChatGroup);
                    } catch (IOException e) {
                        closeEverything (socket, bufferedReader, bufferedWriter);
                    }
                }
            }

        }).start();
    }

    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
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

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Entrez votre nom d'utilisateur : ");
        String username = scanner.nextLine();
        Socket socket = new Socket("localhost", 1234);
        Client client = new Client(socket, username);
        client.listenMessage();
        client.sendMessage();
    }

}

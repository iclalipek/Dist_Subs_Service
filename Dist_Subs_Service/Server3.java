import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class Server3 {
    private static final int PORT = 5003;
    private static Aboneler aboneler = new Aboneler();
    private static String serverName = "Server3"; 

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);

        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } finally {
            serverSocket.close();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            long startTime = System.currentTimeMillis(); 

            try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
            ) {
                String message = in.readLine();
                System.out.println("Received message from client: " + message);

                String response = handleRequest(message);
                out.println(response);

                long endTime = System.currentTimeMillis(); 
                long elapsedTime = endTime - startTime;
                System.out.println("Response time: " + elapsedTime + " milliseconds");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        private String handleRequest(String message) {
            List<String> tokens = Arrays.asList(message.split(" "));
            String command = tokens.get(0);

            if (command.equals("ABONOL")) {
                if (tokens.size() >= 2) {
                    int clientID = Integer.parseInt(tokens.get(1));
                    aboneler.aboneOl(clientID);
                    sendUpdatedListToOtherServers();
                    return "55 TAMM " + serverName + ": Abone oldu. Güncel liste: " + aboneler.getAnaAboneListesi(); // Başarılı yanıt
                } else {
                    return "50 HATA " + serverName + ": Eksik parametre hatası";
                }
            } else if (command.equals("ABONIPTAL")) {
                if (tokens.size() >= 2) {
                    int clientID = Integer.parseInt(tokens.get(1));
                    aboneler.aboneligiIptalEt(clientID);
                    sendUpdatedListToOtherServers();
                    return "55 TAMM " + serverName + ": Abonelik iptal edildi. Güncel liste: " + aboneler.getAnaAboneListesi(); // Başarılı yanıt
                } else {
                    return "50 HATA " + serverName + ": Eksik parametre hatası";
                }
            } else if (command.equals("GIRIS")) {
                return "GIRIS işlemi";
                
            } else if (command.equals("CIKIS")) {
                return "CIKIS işlemi";
               
            } else if (command.equals("SERILESTIRILMIS_NESNE")) {
                
                return "55 TAMM " + serverName + ": Başarılı yanıt";
            } else {
                return "50 HATA " + serverName + ": Geçersiz istek";
            }
        }

        private void sendUpdatedListToOtherServers() {
            System.out.println(serverName + ": Güncellenmiş Abone Listesi Gönderildi: " + aboneler.getAnaAboneListesi());
            
        }
    }
}

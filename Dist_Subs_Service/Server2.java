import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.List;

public class Server2 {
    private static final int PORT = 5002;
    private static Aboneler aboneler = new Aboneler();
    private static String serverName = "Server2"; 

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);

        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                InetAddress clientAddress = clientSocket.getInetAddress();
                int clientPort = clientSocket.getPort();

                System.out.println("Connected to " + serverName + " from " + clientAddress + ":" + clientPort);

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
                    return "55 TAMM Abone oldu. Güncel liste: " + aboneler.getAnaAboneListesi();
                } else {
                    return "50 HATA Eksik parametre hatası";
                }
            } else if (command.equals("ABONIPTAL")) {
                if (tokens.size() >= 2) {
                    int clientID = Integer.parseInt(tokens.get(1));
                    aboneler.aboneligiIptalEt(clientID);
                    sendUpdatedListToOtherServers();
                    return "55 TAMM Abonelik iptal edildi. Güncel liste: " + aboneler.getAnaAboneListesi(); 
                } else {
                    return "50 HATA Eksik parametre hatası";
                }
            } else if (command.equals("GIRIS")) {
                return "GIRIS işlemi";
                
            } else if (command.equals("CIKIS")) {
                return "CIKIS işlemi";
                
            } else if (command.equals("SERILESTIRILMIS_NESNE")) {
                
                return "55 TAMM"; 
            } else {
                return "50 HATA Geçersiz istek";
            }
        }

        private void sendUpdatedListToOtherServers() {
            System.out.println("Server2: Güncellenmiş Abone Listesi Gönderildi: " + aboneler.getAnaAboneListesi());
            
        }
    }
}

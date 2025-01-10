import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MultiServerClient {
    public static void main(String[] args) {
        connectToServers();
    }

    private static void connectToServers() {
        Thread server1Thread = new Thread(() -> connectToServer("localhost", 5001));
        Thread server2Thread = new Thread(() -> connectToServer("localhost", 5002));
        Thread server3Thread = new Thread(() -> connectToServer("localhost", 5003));

        server1Thread.start();
        server2Thread.start();
        server3Thread.start();

        try {
            server1Thread.join();
            server2Thread.join();
            server3Thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void connectToServer(String serverHost, int serverPort) {
        try (Socket socket = new Socket(serverHost, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
    
            
            out.println("KAYITOL 11");
    
           
            String response = in.readLine();
            System.out.println("Response from server on port " + serverPort + ": " + response);
    
            //System.out.println("Sending ABONIPTAL request");
            //out.println("KAYITOL 4"); 
            //System.out.println("ABONIPTAL request sent");
          
            //String response = in.readLine();
            //System.out.println("Response from server on port " + serverPort + ": " + response);
    
           
            String updatedList = getUpdatedList();
            out.println("GUNCEL_LISTE " + updatedList);
    
            
            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                System.out.println("Message from server on port " + serverPort + ": " + serverMessage);
            }
        } catch (IOException e) {
            
        }
    }
    

    private static String getUpdatedList() {
        // Bu metodun içerisinde güncellenmiş abone listesini oluşturun
        // Örneğin: "1,2,3,4"
        return "1,2,3,4";
    }
}

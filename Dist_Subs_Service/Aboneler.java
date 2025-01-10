import java.util.ArrayList;
import java.util.List;

public class Aboneler {
    private List<Integer> anaAboneListesi = new ArrayList<>();

    public Aboneler(){
        
        for (int i = 1; i <= 5; i++) {
            anaAboneListesi.add(i);
        }
    }

    public synchronized void aboneOl(int clientID) {
        anaAboneListesi.add(clientID);
        notifyAllServers();
    }

    public synchronized void aboneligiIptalEt(int clientID) {
        anaAboneListesi.remove(Integer.valueOf(clientID));
        System.out.println("Abonelik iptal edildi. Güncel liste: " + anaAboneListesi);
        sendUpdatedListToAllServers();
        notifyAllServers();
    }
    
    

    public synchronized List<Integer> getAnaAboneListesi() {
        return new ArrayList<>(anaAboneListesi);
    }

    private void notifyAllServers() {
        System.out.println("Ana Abone Listesi Güncellendi: " + anaAboneListesi);

       
        sendUpdatedListToAllServers();
    }

    private void sendUpdatedListToAllServers() {
       
        System.out.println("Güncellenmiş Abone Listesi Tüm Sunuculara Gönderildi: " + anaAboneListesi);
    }
   
}


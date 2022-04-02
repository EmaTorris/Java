import java.util.concurrent.BlockingQueue;

public class EstraiUtente implements Runnable{

private BlockingQueue<Utente> queue;
    
    public EstraiUtente(BlockingQueue<Utente> q){
        this.queue=q;
    }

 
	@Override
    public void run() {
        try{
            Utente utente;
            Thread t;
            while((utente = queue.take()) != null){
            		t = new Thread(utente);
            		t.start();
            		t.join();
            		if(utente.getAccessiAttuali() < (utente.getAccessiTotali() -1 )) { 
            			utente.IncrementaAccessiAttuali();
            			queue.put(utente);
            		}
            }
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}

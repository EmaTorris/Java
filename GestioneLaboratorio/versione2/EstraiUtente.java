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
            while((utente = queue.take()) != null){
            	if(utente.getpriorita() == 3) {
            		System.out.println("Nuovo Professore: "+ utente.getIdentita());
            		utente.run();
            	}
            	if(utente.getpriorita() == 2) {
            		System.out.println("Nuovo Tesista: "+ utente.getIdentita());
            		utente.run();

            	}
            	if(utente.getpriorita() == 1) {
            		System.out.println("Nuovo Studente: "+ utente.getIdentita());
            		utente.run();

            	}
            }
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}

import java.util.concurrent.BlockingQueue;

public class InserisciUtenti implements Runnable {

    private BlockingQueue<Utente> queue;
    private Object globallock = new Object();

    public InserisciUtenti(BlockingQueue<Utente> q){
        this.queue=q;
    }
    @Override
    public void run() {
        //produce messages
        for(int i=0; i<10; i++){
            Studente studente = new Studente(i,globallock);
            try {

                queue.put(studente);
                }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<5; i++){
            Tesista tesista = new Tesista(i, i+3,globallock);
            try {

                queue.put(tesista);
                }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for(int i=0; i<2; i++){
            Professore professore = new Professore(i,globallock);
            try {

                queue.put(professore);
                }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


}

}

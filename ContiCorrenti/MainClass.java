import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class MainClass {

	//@SuppressWarnings("unchecked")
	public static BlockingQueue<Utente> codaProfessori;
	public static BlockingQueue<Utente> codaTesisti;
	public static BlockingQueue<Utente> codaStudenti;

	public static void main(String args[]) throws InterruptedException {
		codaProfessori = new LinkedBlockingQueue<Utente>();
		codaTesisti= new LinkedBlockingQueue<Utente>();
		codaStudenti = new LinkedBlockingQueue<Utente>();
		Monitor m = new Monitor();
		for (int i = 0; i< 3; i++) {
			codaStudenti.add(new Studente(i,m));
		}
		for (int i = 0; i< 3; i++) {
			codaTesisti.add(new Tesista(i, new Random().nextInt(20),m));
		}
		for (int i = 0; i< 3; i++) {
			codaProfessori.add(new Professore(i,m));
		}
		ExecutorService studenti = Executors.newFixedThreadPool(3);
		ExecutorService tesisti = Executors.newFixedThreadPool(3);
		ExecutorService professori = Executors.newFixedThreadPool(3);

		if(!codaProfessori.isEmpty()) {
			while(!codaProfessori.isEmpty())
				professori.execute(codaProfessori.take());
				if(!codaTesisti.isEmpty()) {
					while(!codaTesisti.isEmpty())
						tesisti.execute(codaTesisti.take());
						if(!codaStudenti.isEmpty()) {
							while(!codaStudenti.isEmpty())
								studenti.execute(codaStudenti.take());
						}
				}
		}

		studenti.shutdown();
		tesisti.shutdown();
		professori.shutdown();
	}
}

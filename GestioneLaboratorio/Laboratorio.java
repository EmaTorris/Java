import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class Laboratorio {
	
	/*
	 * Utilizzo un ArrayBlockingQueue per i posti del laboratorio, scelgo proprio questa struttura
	 * in quanto ha una coda limitata e non espandibile con nuove inserzioni. Inoltre la scelgo 
	 * bloccante in modo tale che se qualche nuovo utente volesse entrare all'interno della i-esima posizione
	 * ma la trovasse bloccata, tale inserimento non andrebbe a buon fine.
	 * Per gli studenti, i professori e i tesisti, invece, ho utilizzato una PriorityBlockingQueue. Ho scelto
	 * questa struttura in quanto il numero dei professori/utenti/tesisti può variare nel tempo e la coda si 
	 * modifica di conseguenza. L'ho scelta prioritaria perchè, facendo riferimento al testo, i professori hanno
	 * sempre la priorità massima, seguiti dai tesisti e infine dagli studenti.
	 */
	@SuppressWarnings("unchecked")
	public static BlockingQueue<Utente> laboratorio[] = new ArrayBlockingQueue[20];
	
	
	public static void main(String args[]) throws InterruptedException {
		 BlockingQueue<Utente> codaUtenti = new PriorityBlockingQueue<>(10, new ConfrontaPriorita());
		 for (int i = 0; i < laboratorio.length; i++) {
			 laboratorio[i] = new ArrayBlockingQueue<Utente>(1);
		 }
		
		 InserisciUtenti produttore = new InserisciUtenti(codaUtenti);
		 EstraiUtente consumatore = new EstraiUtente(codaUtenti);
		
		 new Thread(produttore).start();
		 new Thread(produttore).start();
		 new Thread(produttore).start();
		 new Thread(consumatore).start();
		 new Thread(consumatore).start();
		 new Thread(consumatore).start();
		 
		/*
		 for( int i = 0; i < 10; i++) {
			Studente studente = new Studente(i);
			codaUtenti.add(studente);
		}
		
		for( int i = 0 ; i < 5 ; i++ ) {
			Tesista tesista = new Tesista(i,i+3);
			codaUtenti.add(tesista);
		}
		
		for( int i = 0 ; i < 2; i++) {
			Professore professore = new Professore(i);
			codaUtenti.add(professore);
		}
		
		
		Thread thread = new Thread(() -> {
		    System.out.println("Estrazione...");
		    while (true) {
		        try {
		            Utente poll = codaUtenti.take();
		            poll.run();
		            System.out.println("Estratto: " + poll.identificativo);
		        } 
		        catch (InterruptedException e) { 
		            e.printStackTrace();
		        }
		    }
		});
		 
		thread.start();*/
		/*
		ExecutorService studenti = Executors.newFixedThreadPool(10);
		ExecutorService tesisti = Executors.newFixedThreadPool(5);
		ExecutorService professori =Executors.newFixedThreadPool(2);
	
		for(int i = 0; i < 10; i++) {
			codaUtenti.put(new Studente(i));
			studenti.execute(codaUtenti.take());
		}
			for(int i = 0; i < 5; i++)
			codaUtenti.put(new Tesista(i,i+3));
			tesisti.execute(codaUtenti.take());
		for(int i = 0; i < 2; i++) {
			codaUtenti.put(new Professore(i));
			professori.execute(codaUtenti.take());
		}
			studenti.shutdown();
			tesisti.shutdown();
			professori.shutdown();
		*/
		
	}
}

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class MainClass {
	/*
	 * Utilizzo un ArrayBlockingQueue per i posti del laboratorio, scelgo proprio questa struttura
	 * in quanto ha una coda limitata e non espandibile con nuove inserzioni. Inoltre la scelgo 
	 * bloccante in modo tale che se qualche nuovo utente volesse entrare all'interno della i-esima posizione
	 * ma la trovasse bloccata, tale inserimento non andrebbe a buon fine.
	 * Per gli studenti, i professori e i tesisti, invece, ho utilizzato una PriorityBlockingQueue. Ho scelto
	 * questa struttura in quanto il numero dei professori/utenti/tesisti può variare nel tempo e la coda si 
	 * modifica di conseguenza. L'ho scelta prioritaria perchè, facendo riferimento al testo, i professori hanno
	 * sempre la priorità massima, seguiti dai tesisti e infine dagli studenti.
	 * il tutto è gestito seguendo il modello dei produttori/consumatori
	 */
	@SuppressWarnings("unchecked")
	public static BlockingQueue<Utente> laboratorio[] = new ArrayBlockingQueue[20];
	public static int occupati = 0;	
	
	public static void main(String args[]) throws InterruptedException {
		 BlockingQueue<Utente> codaUtenti = new PriorityBlockingQueue<>(10, new ConfrontaPriorita());
		 for (int i = 0; i < laboratorio.length; i++) {
			 laboratorio[i] = new ArrayBlockingQueue<Utente>(1);
		 }
		
		 InserisciUtenti produttore = new InserisciUtenti(codaUtenti);
		 
		 EstraiUtente consumatore = new EstraiUtente(codaUtenti);
		
		 Thread p = new Thread(produttore);
		 p.start();
		 
		 Thread c = new Thread(consumatore);
		 c.start();
		 
		 
		 p.join();
		 c.join();
	}
}

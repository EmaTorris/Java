/*
Emanuele Torrisi 	CORSO:A 	M:503058
Per eseguire: "java MainClass weblog.txt"

VERSIONE SINGLETHREAD
L'idea utilizzata per questo esercizio è la seguente:
	1.Controllare se è stato passato un argomento da linea di comando
	2.Verificiare l'estenzione di tale file. Se l'estenzione è di tipo ".txt" vado avanti altrimenti mi fermo, sollevo un'eccezione
		e termino l'esecuzione del programma
	3.Prelevare ogni riga presente nel File e per ciascuna di essa la suddividere in più tokens salvandola in un array di stringhe
	4.Prendere il primo tokens (corrispondente all'indirizzo IP) e cercare di ottenere l'host corrispondente
	5.Stampare tutto il contenuto del file e sostituire ogni indirizzo IP con il corrispettivo Host
	6.Stampare il tempo per l'esecuzione

VERSIONE MULTITHREAD
Per la versione multithread i cambiamenti apportarti sono i seguenti:
	1.È stata creata una LinkedBlockingQueue di Worker dove ogni Worker prende come parametro una stringa. Tale stringa
		corrisponde a ciascuna linea del file "weblog.txt"
	2.I punti 3-4-5 della versione singlethread vengono eseguiti da un ThreadPoolExecutor
Gli altri comportamenti rimangono invariati

RISULTATI:
Ho allegato due immagini dove mostro la differenza in tempo misurata in millisecondi tra le due versioni
*/
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainClass {

	public static void main(String[] args) {
		if(args[0].length() > 0 ) {
			String check = getFileExtension(args[0]);
			if(check.equals("txt")) {
				ThreadPoolExecutor pool = new ThreadPoolExecutor(4,4,0, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
				//nel caso in cui un task non viene eseguito lo reinserisco nella coda del pool di thread
				pool.setRejectedExecutionHandler(new RejectedExecutionHandler() {
					public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
						try {
							executor.getQueue().put(r);
						}
						catch(InterruptedException e) {
							e.printStackTrace();
						}
					}
				});
				LinkedBlockingQueue<Worker> q = new LinkedBlockingQueue<Worker>();
				File file = new File(args[0]);
				try(BufferedReader br = new BufferedReader(new FileReader(file))){
					String line = br.readLine();
					while(line != null) {
						q.add(new Worker(line));
						line = br.readLine();
					}
				} catch (FileNotFoundException e) {
					System.out.println("Usare java MainClass weblog.txt");
				} catch (IOException e) {
					e.printStackTrace();
				}
				long startTime = System.currentTimeMillis();
				while(!q.isEmpty()) {
					try {
						pool.execute(q.take());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				pool.shutdown();
				//aspetto che effettivamente tutti i task siano terminati altrimenti può darsi che il tempo venga stampato prima del dovuto
				try {
					while (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
						System.out.println("Attendo la terminazione....");
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Terminato. Il tempo impiegato è di " + (System.currentTimeMillis() - startTime) + " millisecondi.");
			}
			else {
				System.out.println("usare l'estenzione .txt");
			}
		}
		else {
			System.out.println("Usare java MainClass weblog.txt");
		}
	}

	private static String getFileExtension(String fileName) {
		if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
}

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainClass {


	public static void main(String[] args) throws IOException {
		String nomefile = null;
		System.out.println("eseguire con: Java nomeprogramma fileinput");
		//controllo che ci sia un argomento passato come input
		if(args != null && args.length==1) {
			nomefile = args[0];
		}
		//altrimenti stampo un messaggio di errore ed esco
		else{
			System.err.println("il numero di argomenti è errato; eseguire con: Java nomeprogramma fileinput");
			System.exit(1);
		}

		//leggo il file passato da input
		FileInputStream filetest = new FileInputStream(nomefile);
		String linea;
		BufferedReader br = new BufferedReader(new InputStreamReader(filetest));
		//leggo la prima linea che è un intero
		linea = br.readLine();
		linea.trim();
		int k = Integer.parseInt(linea);
		//scansiono il file passato da input e inserisco tutti i nomi all'interno della prima coda
		ArrayBlockingQueue<Worker> codadipersone = new ArrayBlockingQueue<Worker>(k);
		Worker persona;
		while((linea = br.readLine()) != null){
			persona = new Worker(linea);
			codadipersone.add(persona);
		}
		br.close();
		int j = k - 2;
		//ThreadPoolExecutor pool = new ThreadPoolExecutor(4,4,10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(k));
		ThreadPoolExecutor pool = new ThreadPoolExecutor(4,4,0, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(j));
		//utilizzo il pool.setRejectedExecutionHandler nel caso in cui il pool non riesca ad eseguire un task. In questo modo
		//vado a reinserirlo all'interno della coda
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
		for( int i = 0; i < k; i++) {
			pool.execute(codadipersone.remove());
		}
		pool.shutdown();
	}

}

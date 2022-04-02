import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Lettore implements Runnable {

	private File f;
	private ConcurrentHashMap<Causale,Integer> hash;
	private ThreadPoolExecutor pool;
	
	public void Stampa(ConcurrentHashMap<Causale,Integer> h) {
		int sum = 0;
		Set<Causale> settmp = h.keySet();
		Iterator<Causale> itemp = settmp.iterator();
		while(itemp.hasNext()) {
			Causale c = itemp.next();
			System.out.println("la causale " + c + " ha le seguenti occorrenze: " + h.get(c));
			sum = sum + h.get(c);
		}
		//System.out.println("il numero totale è: " + sum);
	}
	
	public Lettore(File f1) {
		this.f= f1;
		pool = new ThreadPoolExecutor(4,4,10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(4));
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
		hash = new ConcurrentHashMap<Causale,Integer>();
	}
	@Override
	public void run() {
		ContoCorrente tempconto;
		try( 
				FileInputStream fis = new FileInputStream(this.f);
				ObjectInputStream ois = new ObjectInputStream(fis);
			)
		{
			boolean check = true;
			//finche ci sono conti nel file, la variabile check viene messa a false quando sono alla fine del file
			while(check) {
				try {
					tempconto = (ContoCorrente) ois.readObject();
					//vado a prendere un conto all'interno del file che contiene tutti i conti
					ContaConti conta = new ContaConti(this.hash, tempconto);
					pool.execute(conta);
				}
				 catch(EOFException e) {
					 check = false;
				 }
			}
			System.out.println("sono prima della terminazione del pool");
			pool.shutdown();
			ois.close();
		}
		 catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		}
		System.out.println("sto stampando le statistiche");
		Stampa(this.hash);
	}

}

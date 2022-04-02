import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MainClass {
	
	public static void main(String[] args)  {
		// creo una lista di conti correnti...
		List<ContoCorrente> lista = new ArrayList<ContoCorrente>();
		String file = "conticorrenti.txt";
		Causale[] c = Causale.values();
		for(int i = 0; i < 10; i++ ) {
			//... ogni contocorrente ha il nome del correntista e la lista dei movimenti(random)
			ContoCorrente contcorr = new ContoCorrente("correntista " + i);
		    //metto +1 così che mi assicuro che almeno un movimento esiste
			int numeromovimenti = (new Random().nextInt(10) + 1 );
			for (int j = 0; j < numeromovimenti ; j++) {
				
				LocalDate startDate = LocalDate.of(2016, 1, 1); // data di inizio
			    long start = startDate.toEpochDay();
			    LocalDate endDate = LocalDate.now(); //data di fine
			    long end = endDate.toEpochDay();

			    long randomEpochDay = ThreadLocalRandom.current().longs(start, end).findAny().getAsLong();
			    System.out.println(LocalDate.ofEpochDay(randomEpochDay)); // data random tra i due range
			    Movimento m = new Movimento(randomEpochDay,c[ new Random().nextInt(c.length)]);
				//inserisco il movimento appena creato nel conto corrente...
			    contcorr.InserisciNuovoMovimento(m);
				}
			//e lo aggiungo nella lista dei conti correnti
			lista.add(contcorr);
			}
			try(
					FileOutputStream fos = new FileOutputStream(file);
					ObjectOutputStream oos = new ObjectOutputStream(fos);)
					{
				ContoCorrente ctmp;
				Iterator<ContoCorrente> iter = lista.iterator();
				while(iter.hasNext()) {
					ctmp = iter.next();
					if(ctmp != null ) oos.writeObject(ctmp);
				}
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			System.out.println("Serializzazione completata");
		
		File f1 = new File(file);
		Lettore l = new Lettore(f1);
		Thread lettoreconti = new Thread(l);
		lettoreconti.start();
		try {
			lettoreconti.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Fine");
	}
}

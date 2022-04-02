import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ContaConti implements Runnable{

	private ConcurrentHashMap<Causale,Integer> hash;
	private ContoCorrente c;
	//uso una concurrenthashmap temporanea per contare le occorrenze
	private ConcurrentHashMap <Causale, Integer> hashtmp;
	
	public ContaConti(ConcurrentHashMap<Causale,Integer> hash, ContoCorrente c) {
		this.hash = hash;
		this.c = c;
		hashtmp = new ConcurrentHashMap<Causale, Integer>();
	}

	@Override
	public void run() {
		List<Movimento> tmp = c.PrendiMovimenti();
		for(int i = 0; i < tmp.size(); i++) {
			// nel caso in cui ho già registrato la causale incremento solo il numero di occorrenze
			if(hashtmp.containsKey(tmp.get(i).OttieniCausale()))
				hashtmp.put(tmp.get(i).OttieniCausale(), (hashtmp.get(tmp.get(i).OttieniCausale())+1));
			// altrimenti la memorizzo nella tabella hash momentanea attribuendo 1 come valore di occorrenza
			else {
				hashtmp.put(tmp.get(i).OttieniCausale(), 1);	
				}
			}
		Set<Causale> settmp = hashtmp.keySet();
		Iterator<Causale> itemp = settmp.iterator();
		while(itemp.hasNext()) {
			Causale ctmp = itemp.next();
			// se la tabella hash originale contiene la causale incremento solo il numero di occorrenze
			if(hash.containsKey(ctmp)){
				hash.put(ctmp, hash.get(ctmp) + hashtmp.get(ctmp));
			}
			//altrimenti la inserisco con il numero di occorrenze appena calcolato
			else {
				hash.put(ctmp,hashtmp.get(ctmp));
			}
		}
	}
}


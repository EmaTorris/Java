import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Monitor {
	
	@SuppressWarnings("unchecked")
	private BlockingQueue<Utente> laboratorio[] = new ArrayBlockingQueue[20];
	private int occupati = 0;
	private int professoriInAttesa = 0;
		 
	public Monitor() {
		for (int i = 0; i < laboratorio.length; i++) {
			 laboratorio[i] = new ArrayBlockingQueue<Utente>(1);
		 }
	}
	
	public synchronized void UsaTuttiComputer(Professore p) {
		professoriInAttesa++;
		while(occupati > 0) {
			try {
				//System.out.println("professore dentro la wait " + Laboratorio.occupati);
				wait();
			}
		catch(InterruptedException e) {
			e.printStackTrace();
			}
		}
		for(int i = 0 ; i < 20 ; i++) {
			laboratorio[i].offer(p);
		}
		occupati = 20;
		System.out.println("professore "  + p.identificativo + " + " + Thread.currentThread().getName() + " sta prendendo la sala ");
		
	}
	
	
	public synchronized void LasciaTuttiComputer(Professore p) {
		for(int i = 0; i < 20; i++) {
			laboratorio[i].poll();
			}
		occupati = 0;
		System.out.println("professore " + p.identificativo + " + " + Thread.currentThread().getName() + " sta abbandonando la sala ");
		professoriInAttesa--;
		notifyAll();
}

	
	public synchronized int UsaComputer(Studente s) {
		boolean preso = false;
		int i = -1;
		while(!preso) {
			while(professoriInAttesa > 0  || occupati == 20)
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			for (i = 0;!preso &&  i < 20 ; i++) {	
				if(laboratorio[i].offer(s) == true) {
					occupati++;
					preso = true;
					System.out.println("studente " + s.identificativo + " + " + Thread.currentThread().getName() + " prende il computer "+ i);
					return i;
					}
				}
				if(!preso) {
					try {
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		}
				return -1;
}
	
	public synchronized void LasciaComputer(int i ,Studente s ) {
		
		System.out.println("studente " + s.identificativo + " + " + Thread.currentThread().getName() + " sta abbandonando il computer "+ i);
		laboratorio[i].poll();
		occupati--;
		notifyAll();
	}
	
	public synchronized void UsaComputerEsclusivo(int computer, Tesista t) {
		//Utente u = codaTesisti.poll();
		//while((Laboratorio.codaProfessori.size() > 0) && (!laboratorio[computer].offer(t)))
		while((professoriInAttesa > 0) && (!laboratorio[computer].offer(t)))
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		occupati++;
		System.out.println("tesista " + t.identificativo+ " + " + Thread.currentThread().getName() + " prende il computer "+ computer);
		
	}
	
	public synchronized void LasciaComputerEsclusivo(int computer, Tesista t) {
		laboratorio[computer].poll();
		occupati--;
		//System.out.println(Laboratorio.occupati);
		notifyAll();
		System.out.println("tesista " + t.identificativo + " + " + Thread.currentThread().getName() + " lascia il computer "+ computer);
	}
	
}
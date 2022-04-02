
public class Professore extends Utente {
	private Object lock;
	
	
	public Professore(int i, Object lock) {
		this.identificativo = i;
		this.priorita = 3;
		this.lock = lock;
	}
	
	public void UsaComputer() {
		synchronized(lock) {
		int check = 0;
		for (int i = 0; i < 20; i++) {
			while(!Laboratorio.laboratorio[i].offer(this))
				try {
					lock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			if(Laboratorio.laboratorio[i].contains(this)) check++;
		}
		if (check == 20)
			System.out.println("professore "  + this.identificativo + " + " + Thread.currentThread().getName() + " sta prendendo la sala ");
		else {
			System.out.println("non sono riuscito ad occupare tutte le posizioni " + Thread.currentThread().getName());
			}
		}
	}
	
	
	public int getIdentita() {
		return this.identificativo;
	}
	
	public void LasciaComputer() {
		synchronized(lock) {
		for(int i = 0; i < 20; i++) {
			Laboratorio.laboratorio[i].poll();
			}
		System.out.println("professore " + this.identificativo + " + " + Thread.currentThread().getName() + " sta abbandonando la sala ");
		lock.notifyAll();
		}
	}

}

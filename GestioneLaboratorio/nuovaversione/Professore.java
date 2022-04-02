
public class Professore extends Utente {
	private Object lock;
	
	
	public Professore(int i, Object lock) {
		this.identificativo = i;
		this.priorita = 3;
		this.lock = lock;
	}
	
	public void UsaComputer() {
		synchronized(lock) {
			while(MainClass.occupati > 0)
				try {
					lock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			for (int i = 0; i < 20; i++) {
			MainClass.laboratorio[i].offer(this);
		}
			MainClass.occupati = 20;
			System.out.println("professore "  + this.identificativo + " + " + Thread.currentThread().getName() + " sta prendendo la sala ");
		}
	}
	
	
	public int getIdentita() {
		return this.identificativo;
	}
	
	public void LasciaComputer() {
		synchronized(lock) {
		for(int i = 0; i < 20; i++) {
			MainClass.laboratorio[i].poll();
			}
		MainClass.occupati = 0;
		System.out.println("professore " + this.identificativo + " + " + Thread.currentThread().getName() + " lascia la sala ");
		lock.notifyAll();
		}
	}

}

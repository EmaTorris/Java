
public class Tesista extends Utente{
	
	private int computer;
	private Object lock;
	
	public Tesista(int i, int computer, Object lock) {
		this.identificativo = i;
		this.computer = computer;
		this.priorita = 2;
		this.lock = lock;
	}
	
	public int getIdentita() {
		return this.identificativo;
	}
	
	public void UsaComputer() {
		synchronized(lock) {
		while(!Laboratorio.laboratorio[this.computer].offer(this))
			try {
				lock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		System.out.println("tesista " + this.identificativo+ " + " + Thread.currentThread().getName() + " prende il computer "+ this.computer);
		}
	}
	
	public void LasciaComputer() {
		synchronized(lock) {
		Laboratorio.laboratorio[this.computer].poll();
		lock.notifyAll();
		System.out.println("tesista " + this.identificativo + " + " + Thread.currentThread().getName() + " lascia il computer "+ this.computer);
		}
	}
}

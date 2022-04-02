
public class Studente extends Utente{
		
	private int computer;
	private Object lock;
	
	public Studente(int i, Object lock) {
		this.identificativo = i;
		this.priorita = 1;
		this.lock = lock;
	}
	
	public int getIdentita() {
		return this.identificativo;
	}
	
	public void UsaComputer() {
		 synchronized(lock) {
			/*boolean preso = false;
			int i = 0;
			boolean preso1 = false;
			while (!preso) {
					if(Laboratorio.laboratorio[i].offer(this) == true ) {
						this.computer = i;
						preso = true;
						System.out.println("studente " + this.identificativo + " + " + Thread.currentThread().getName() + " prende il computer "+ this.computer);
						break;
					}
					else if (i<20){
						i++;
						continue;
					}
					i=0;
					try {
						lock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
				
		}*/
			boolean preso = false;
			while(!preso) {
				for(int i = 0 ; !preso && i < 20; i++) {
					if(MainClass.laboratorio[i].offer(this) == true) {
						this.computer = i;
						preso = true;
						MainClass.occupati++;
						System.out.println("studente " + this.identificativo + " + " + Thread.currentThread().getName() + " prende il computer "+ this.computer);
					}
				}
					if (!preso) {
						try {
							lock.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}

	
	public void LasciaComputer() {
		synchronized(lock) {
		System.out.println("studente " + this.identificativo + " + " + Thread.currentThread().getName() + " lascia il computer "+ this.computer);
		MainClass.laboratorio[this.computer].poll();
		MainClass.occupati--;
		lock.notifyAll();
		}
	}
}

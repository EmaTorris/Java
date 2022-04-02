import java.util.Random;

public abstract class Utente implements Runnable{
	
	protected int priorita = 0;
	protected int accessiattuali = 0 ;
	// il + 1 mi assicura che almeno un accesso verrà sempre fatto 
	protected int accessitotali = (new Random().nextInt(5)) + 1 ;
	protected int identificativo = 0;

	public int getAccessiAttuali() {
		return this.accessiattuali;
	}
	
	public int getAccessiTotali() {
		return this.accessitotali;
	}
	
	public void IncrementaAccessiAttuali() {
		this.accessiattuali++;
	}

	public int getpriorita() {
		return priorita;
	}
	
	public abstract void UsaComputer();
	
	public abstract void LasciaComputer();
	
	public abstract int getIdentita();
	
	public void run() {
			
		//for(int i = this.accessiattuali; i < this.accessitotali; i++) {
			// tempo di attesa che intercorre tra un accesso e il successivo
			try {
				Thread.sleep(1000 * (new Random().nextInt(2)));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//l'utente x usa il computer...
			this.UsaComputer();
			//... per un tempo random
			try {
				Thread.sleep(1000 * (new Random().nextInt(5)));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//finita l'operazione l'utente x lascia il computer
			this.LasciaComputer();
			
			//}
		}	
	}


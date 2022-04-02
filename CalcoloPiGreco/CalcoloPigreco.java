//NOME: Emanuele
//COGNOME: Torrisi
//MATRICOLA: 503058
//CORSO: A

public class CalcoloPigreco implements Runnable{
	private double accuratezza;
	private double pi = 4;
	private double den = 1;
	private int segno = 1;
	private double ris = 0;

	public CalcoloPigreco(double accuratezza) {
		this.accuratezza = accuratezza;
	}


	public void run() {
			while(true) {
			ris =ris + ((pi/den)*segno);
			den = den + 2;
			segno = segno * (-1);
			//System.out.println("il risultato è:" + ris);
			if(Thread.currentThread().isInterrupted() || Math.abs(ris - Math.PI) < accuratezza) {
				System.out.println("Thread: sono stato interrotto");
				break;
			}
			System.out.println("Thread: valore calcolato: "+ ris);
		}
	}
}

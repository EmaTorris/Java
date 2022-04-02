//NOME: Emanuele
//COGNOME: Torrisi
//MATRICOLA: 503058
//CORSO: A


import java.util.Scanner;
import java.lang.Thread;
import java.lang.InterruptedException;

public class CalcoloPi{

	public static void main(String[] args) {
		long tempo;
		double accuratezza;
		Scanner sc = new Scanner(System.in);
		System.out.println("Inserire il livello di accuratezza");
		accuratezza = sc.nextDouble();
		System.out.println("Inserire il tempo massimo di attesa");
		tempo = sc.nextLong();
		System.out.println("Ho inserito il livello:"+accuratezza +"con il seguente tempo:"+ tempo);
		sc.close();
		CalcoloPigreco calcolatore = new CalcoloPigreco(accuratezza);
		Thread thread = new Thread(calcolatore);
		thread.start();
		try {
			thread.join(tempo);
			if (thread.isAlive()) {
				thread.interrupt();
				System.out.println("Main: superato il limite di tempo");
			}
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

}

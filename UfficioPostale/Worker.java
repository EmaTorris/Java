import java.util.Random;

public class Worker implements Runnable{

	private String s;

	public Worker(String s) {
	this.s = s;
	}

	public String toStringa() {
		return s;
	}

	public void run() {
		// TODO Auto-generated method stub
		System.out.println(s + " " + Thread.currentThread().getName());
		try {
			// moltiplico un numero random da 1 a 10 per 1000 in modo da avere secondi e non millisecondi
			Thread.sleep(1000 * (new Random().nextInt(10)));
			// simulo il tempo di servizio di uno sportello
			System.out.println(s + " abbandona lo sportello " + Thread.currentThread().getName());
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

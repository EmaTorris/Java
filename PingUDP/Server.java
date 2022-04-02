/*
Emanuele Torrisi
Corso:A
Matricola: 503058
*/

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Random;



public class Server {

	private void processo(int porta) {
		try {
			DatagramSocket socket = new DatagramSocket(porta);
			// il server rimane sempre attivo
			while(true) {
				byte[] ric = new byte[1024];
				DatagramPacket richiesta = new DatagramPacket(ric,1024);
				// visto che il server farà la receive ed essendo la receive bloccante setto un timeout di 30 secondi per evitare attese indefinite
				socket.setSoTimeout(30000);
				// ricevo il messaggio dal client in bytes e lo trasformo in stringa se non lo ricevo entro 30 secondi chiudo il server
				try{
					socket.receive(richiesta);
				}
				catch(SocketTimeoutException e) {
					System.out.println("il timer è scaduto");
					System.exit(0);
				}
				byte[] auxbuf = richiesta.getData();
				ByteArrayInputStream bais = new ByteArrayInputStream(auxbuf);
				InputStreamReader isr = new InputStreamReader(bais);
				BufferedReader br = new BufferedReader(isr);
				String linea = br.readLine();
				byte[] auxbufrisposta = richiesta.getData();
				//prendo la porta del client
				int portaclient = richiesta.getPort();
				//prendo l'indirizzo del client
				InetAddress client = richiesta.getAddress();
				// per simulare la probabilità di perdita di pacchetti del 25% ho usato un numero random che viene preso da 0 a 3
				// in modo tale da avere 1/4 di possibilità di perdere il pacchetto
				int rand = new Random().nextInt(4);
				if(rand == 3) {
					//il pacchetto viene perso
					System.out.println(client + ":" + portaclient + ">" + linea + " ACTION: not sent");
					continue;
				}
				//simulo l'attesa
				int attesa = new Random().nextInt(300);
				Thread.sleep((int) (attesa));
				//invio la risposta al client
				DatagramPacket risposta = new DatagramPacket(auxbufrisposta,auxbufrisposta.length,client,portaclient);
				socket.send(risposta);

				System.out.println(client + ":" + portaclient + ">" + linea + " ACTION: delayed " + attesa + "ms" );
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
}


	public static void main(String[] args) {
		int porta = 0;
		if (args.length != 1) {
	         System.out.println("Eseguire con Java Server [int]porta");
	         return;
	      }
				//verifico se effettivamente è un intero
				try{
					porta = Integer.parseInt(args[0]);
				}
				catch( NumberFormatException e){
					System.out.println("ERRORE: arg 0 la porta deve essere un intero");
					System.exit(1);
				}
		Server p = new Server();
		p.processo(porta);
		}
}

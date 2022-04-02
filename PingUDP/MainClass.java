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
import java.net.UnknownHostException;
import java.util.Date;

public class MainClass {

	public static void main(String[] args) throws IOException {
		int porta = 0;
		InetAddress nome = null;
		// faccio tutti i controlli sui parametri d'ingresso
		if(args.length == 0){
			System.out.println("ERRORE: [InetAddress:String]nome e [int]portaserver non inseriti");
		}
		else if(args.length == 1){
			System.out.println("ERRORE: [int]portaserver non inserita");
		}
		else if(args.length == 2) {
			//verifico se effettivamente è un intero
			try{
				porta = Integer.parseInt(args[1]);
			}
			catch( NumberFormatException e){
				System.out.println("ERRORE: arg 1 la porta deve essere un intero");
				System.exit(1);
			}
			//verifico se effettivamente è una stringa
			try{
				nome = InetAddress.getByName(args[0]);
			}
			catch(UnknownHostException e){
				System.out.println("ERRORE: arg 0 nome non corretto, deve essere una stringa");
				System.exit(1);
			}
			//creo un datagramSocket
			DatagramSocket client = new DatagramSocket();
			// visto che il client farà la receive ed essendo la receive bloccante setto un timeout di 1 secondo per evitare attese indefinite
			client.setSoTimeout(1000);
			// informazioni che mi serviranno per stampare le statistiche finali
			long minimo = Integer.MAX_VALUE;
			long massimo = 0;
			double media = 0;
			double pacchetti = 0;
			double percentuale = 0;
			//dichiaro due byte array per riceve ed inviare dati che saranno passati come payload del datagramma
			byte[] senddata = new byte[1024];
			byte[] receivedata = new byte[1024];
			double i;
			for (i = 0; i < 10; i++) {
				// salvo il tempo iniziale in cui invio il primo "ping"
				long msinvio = new Date().getTime();
				//contenuto che manderò come payload del datagramma
				String aux = "Ping " + String.format("%.0f",i) + " " + msinvio;
				senddata = aux.getBytes();
				// invio il messaggio di ping al server dove porta rappresenta la porta del server a cui mi voglio collegare
				DatagramPacket ping = new DatagramPacket(senddata,senddata.length,nome,porta);
				client.send(ping);
				try {
					//cerco di ottenere la risposta dal server
					DatagramPacket risposta = new DatagramPacket(receivedata,1024);
					client.receive(risposta);
					// salvo anche il tempo in cui ho ricevuto la risposta da parte del server
					long msricevo = new Date().getTime();
					//il contenuto verrà ricevuto in byte quindi devo trasformarlo in stringa
					byte[] buffaux = risposta.getData();
					ByteArrayInputStream bais = new ByteArrayInputStream(buffaux);
					InputStreamReader isr = new InputStreamReader(bais);
					BufferedReader br = new BufferedReader(isr);
					String linea = br.readLine();
					// verifico i dati da stampare poi nelle statistiche
					if((msricevo - msinvio) < minimo) minimo = (msricevo - msinvio);
					if((msricevo - msinvio) > massimo) massimo = (msricevo - msinvio);
					pacchetti++;
					media = media + (msricevo - msinvio);
					System.out.println(linea + " " + "RTT: " + ( msricevo - msinvio));
				}
				// se non mi arriva la risposta da parte del server
				catch(IOException e) {
					System.out.println(aux + " RTT: *");
				}
			}
			media = media / pacchetti;
			percentuale = (i - pacchetti)/i;
			percentuale = percentuale * 100;
			System.out.println("\t\t\t----PING STATISTICS----");
			System.out.println("pacchetti inviati: " + String.format("%.0f",i) + " pacchetti ricevuti: " + String.format("%.0f",pacchetti) + " percentuale persi: " + String.format("%.0f",percentuale) + "%");
			System.out.println("round-trip (ms) minimo: " + minimo + " massimo: " + massimo + " media: " + String.format("%.2f",media));
		}
		else {
			System.out.println("Usare 'java MainClass nome portaserver' ");
		}
	}
}

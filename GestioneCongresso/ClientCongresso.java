/*
Emanuele Torrisi
503058
Corso A
*/

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.Remote;

/*
Il client prende come parametro d'ingresso un hostid e un file (controllando se entrambi vengono passati come argomento a linea di comando)
per quanto riguarda la porta su cui è in esecuzione il servizio di Registry uso la porta di default 1099. Scansiono tutto il file passato
a riga di comando e per ogni riga creo un array di stringhe dove ad ogni posizione memorizzo:
1)operazione-giorno-sessione-speaker (caso di registrazione)
2)operazione-giorno (caso di stampa del programma relativo ad un giorno)
A seconda dell'operazioni richieste ('r' oppure 'p'), se corrette, verranno chiamati rispettivamente il metodo "registrazione"
oppure il metodo "programma" definiti nell'interfaccia remota (ServerCongresso).
*/
public class ClientCongresso{

	public static void main(String[] args){
		ServerCongresso server;
		Remote RemoteObject;
		String sessione = null;
		String speaker = null;
		String nomefile = null;
		try{
			if (args.length != 2){
			System.out.println("ERRORE: eseguire ClientCongresso localhost input1.txt");
			System.exit(1);
			}
			Registry r = LocateRegistry.getRegistry(args[0],1099);
			// interrogo il registro RMI. Ricevo una copia serializzata dello stub associato al server remoto.
			RemoteObject = r.lookup("ciao");
			server = (ServerCongresso) RemoteObject;
			nomefile = args[1];
			FileInputStream filetest = new FileInputStream(nomefile);
			BufferedReader br = new BufferedReader(new InputStreamReader(filetest));
			String linea;
			while((linea = br.readLine()) != null){
				String[] arraux = linea.split(" ");
				char operazione = arraux[0].charAt(0);
				int giorno = Integer.parseInt(arraux[1]);
				if(operazione=='r') {
					sessione = arraux[2];
					speaker = arraux[3];
					System.out.println("operazione: " + operazione + " giorno: " + giorno + " sessione: " + sessione + " speaker: " + speaker);
				}
				if (operazione=='p')
					System.out.println("operazione: " + operazione + " giorno: " + giorno);
					if(operazione=='r') {
						System.out.println("Registrazione per quale giornata? (scegliere in questo range 1-3) " + giorno);
						if (giorno < 1 || giorno > 3){
							System.out.println("Giornata non valida, inserire una giornata in questo range(1-3)");
							continue;
						}
						System.out.println("scegliere una sessione in questo range(S1 - S12)? " + sessione);
						if ( !sessione.equals("S1") && !sessione.equals("S2") && !sessione.equals("S3") && !sessione.equals("S4") && !sessione.equals("S5") && !sessione.equals("S6") && !sessione.equals("S7") && !sessione.equals("S8") && !sessione.equals("S9") && !sessione.equals("S10") && !sessione.equals("S11") && !sessione.equals("S12")) {
							System.out.println("Sessione errata");
							continue;
						}
						System.out.println("Nome dello speaker: " + speaker);
						if (server.registrazione(giorno, speaker, sessione)==true)
						System.out.println("Registrazione di " + speaker + " effettuata con successo");
						else System.out.println("Registrazione non effettuata, troppe registrazioni");
					}
					else if (operazione=='p'){
						System.out.println("Seleziona una giornata in questo range (1-3)? " + giorno);
						if (giorno < 1 || giorno > 3){
							System.out.println("Giornata non valida, inserire una giornata in questo range(1-3)");
							continue;
						}
						Classeaux prog = server.programma(giorno);
						System.out.println("Programma giornata "+giorno);
						prog.stampa();
					}
					else System.out.println("Servizio non disponibile");
				}
				br.close();
			}
			catch (Exception e){
				e.printStackTrace();
			}
	}
}

/*
Studente:Emanuele Torrisi
Matricola:503058
Corso:A
*/

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class MainClass {

	//il client è bloccante
	static int porta = 5555;
	public static void main (String[] args) throws IOException {
		ByteBuffer bytebuffer = ByteBuffer.allocate(1024);
		byte[] input = "input.txt".getBytes();
		bytebuffer.put(input);
		SocketAddress address = new InetSocketAddress(InetAddress.getLocalHost(),porta);
		SocketChannel socketchannel;
		socketchannel = SocketChannel.open();
		//mi connetto al server
		socketchannel.connect(address);
		// creo una stringa contenente il nome del file passato
		String nomefile = new String(bytebuffer.array(),0,bytebuffer.position());
		//il puntatore position si sarà fermato nell'esatto punto in cui ho finito di scrivere il nome del file
		int lunghezzanome = bytebuffer.position();
		// mi serve sapere quanto è grande il file quindi invio la lunghezza del nome ed insieme ad essa metto il nome del file
		// all'interno di un bytebuffer
		ByteBuffer infofile = ByteBuffer.allocate(1024);
		infofile.putInt(lunghezzanome);
		infofile.put(nomefile.getBytes());
		infofile.flip();
		//invio al server le informazioni sul nome e lunghezza del nome
		socketchannel.write(infofile);

		bytebuffer.clear();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		FileChannel out= null;
		char esitorisposta = 'n';
		//vado a leggere sul canale e scrivo il contenuto in buffer(risposta)
		int numero;
		while(( numero = socketchannel.read(buffer))!=-1) {
			// la prima volta sarà sicuramente 'n' e in questo caso prelevo la risposta inviata dal server
			if(esitorisposta == 'n'){
				System.out.println("numero è " + numero);
				buffer.flip();
				esitorisposta = buffer.getChar();
				System.out.println("esitorisposta " + esitorisposta);
			}
			// è andato tutto bene
			if (esitorisposta == 'y'){
				// apro il file...
				if (out == null){
					System.out.println("il nome del file è il seguente: " + Paths.get(nomefile));
					out= FileChannel.open(Paths.get(nomefile),StandardOpenOption.CREATE,StandardOpenOption.WRITE);
				}
				//devo "pulire" la risposta 'y' prima di scrivere il contenuto del file
				buffer.clear();
				buffer.flip();
				// vado a scrivere nel file andando a leggere nel buffer passato dal server
				out.write(buffer);
				buffer.compact();
			}
			// non ho trovato il file cercato
			if (esitorisposta == 'n'){
				throw new NoSuchFileException("Il server non ha trovato il file");
			}
		}
		out.close();
		System.out.println("File trasferito");
	}
}

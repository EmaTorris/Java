/*
Studente:Emanuele Torrisi
Matricola:503058
Corso:A
*/

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

public class Server {
	//il server è non bloccante
	static int porta = 5555;
	public static void main(String[] args) throws IOException {
		String nomefile = null;
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().bind(new InetSocketAddress(InetAddress.getLocalHost(),porta));
		serverSocketChannel.configureBlocking(false);
		Selector selector = Selector.open();
		// registro un serversocketchannel ad un selettore per le operazioni OP_ACCEPT
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		while(true) {
			System.out.println("attendo per nuove connessioni");
			selector.select();
			Set <SelectionKey> readyKeys = selector.selectedKeys();
			Iterator <SelectionKey> iterator = readyKeys.iterator();
			while(iterator.hasNext()) {
				SelectionKey key = iterator.next();
				iterator.remove();
				// accetto la connessione e registro il serversocketchannel per le operazioni OP_READ
				if((key.readyOps( ) & SelectionKey.OP_ACCEPT) != 0) {
					//ho il canale con il quale questa chiave è stata creata
					ServerSocketChannel channel1 = (ServerSocketChannel) key.channel();
					SocketChannel client = channel1.accept();
					System.out.println("Ho accettato la connessione da: " + client);
					client.configureBlocking(false);
					ByteBuffer output = ByteBuffer.allocate(1024);

					SelectionKey key2 = client.register(selector, SelectionKey.OP_READ,output);
				}
				//se ricevo una selectionkey con OP_READ
				else if((key.readyOps( ) & SelectionKey.OP_READ) != 0) {
					try {
					SocketChannel client = (SocketChannel) key.channel();
					//prendo il buffer passato dal client
					ByteBuffer output = (ByteBuffer) key.attachment();
					//scrivo su output il contenuto letto da client
					client.read(output);
					output.flip();
					// leggo la dimensione del nome del file
					int value = output.getInt();
					// controllo se ho ricevuto tutto il nome
					if(value == (output.limit() - output.position())) {
						// prendo il nome del file passato dal client
						nomefile = new String(output.array(),4,output.limit()-4);
						System.out.println(nomefile);
						ByteBuffer buff = ByteBuffer.allocate(Character.BYTES);
						try {
							//cerco di aprile il file passato dal client e se lo trovo metto 'y' nel buffer di risposta
							FileChannel file = FileChannel.open(Paths.get(nomefile), StandardOpenOption.READ);

							//nel caso in cui il file esiste metto 'y' all'interno di un bytebuffer
							buff.putChar('y');

							//setto la SelectionKey ora in scrittura perchè voglio mandare la risposta e gli allego il bytebuffer
							client.register(selector, SelectionKey.OP_WRITE, buff);
						}
						catch(NoSuchFileException e) {
							//se non trovo il file che mi ha chiesto il client sollevo una NoSuchFileException e metto "n" come identificativo
							buff.putChar('n');
							//setto la SelectionKey ora in scrittura perchè voglio mandare la risposta
							client.register(selector, SelectionKey.OP_WRITE,buff);
						}
						buff.flip();
						}
					} catch (IOException e){
						System.out.println("ERRORE: "+e.getMessage());
						key.cancel();
					}
				}
				//se ricevo una selectionkey con OP_WRITE
				else if ((key.readyOps( ) & SelectionKey.OP_WRITE) != 0){
					try{
						SocketChannel client=(SocketChannel)key.channel();

						//buffer conterrà o 'y' nel caso in cui il file esista o 'n' nel caso in cui il file non esista
						ByteBuffer buffer = (ByteBuffer) key.attachment();
						char esito = buffer.getChar();
						// riporto il puntatore position indietro per inviare la risposta al client
						buffer.position(buffer.position()-2);
						client.write(buffer);
						//se l'esito è positivo
						if(esito == 'y') {
								// visto che l'arraylist aggiunge in coda dovrò andare a prelevare il 2 elemento dell'arraylist per avere il file
								FileChannel file = FileChannel.open(Paths.get(nomefile), StandardOpenOption.READ, StandardOpenOption.WRITE);

								// uso il metodo transferTo per trasferire il file in quanto metodo più rapido rispetto al trasferimento mediante buffer
								boolean tutti = false;
								boolean prossimopasso = false;
								while((tutti==false) && (prossimopasso == false)) {
									if(prossimopasso == false){
										try {
												// appendo questra stringa alla fine del file per dire che effettivamente il trasferimento è stato eseguito
									    	Files.write(Paths.get(nomefile), "\n[SERVER]: La copia è avvenuta con successo".getBytes(), StandardOpenOption.APPEND);
												prossimopasso = true;
										}catch (IOException e) {
									    	System.out.println("non sono riuscito ad inserire a fine file");
										}
									}
									long transfered=file.transferTo(file.position(),file.size()-file.position(), client);
									//se ho trasferito meno bytes di quanto sia effettivamente la dimensione del file devo continuare a
									//spedire i rimanenti bytes.
									file.position(file.position()+transfered);
									if(file.position()==file.size()){
										tutti = true;
										System.out.println("sono stati spediti: " + file.position() + " byte su: " + file.size() + " del file");
										file.close();
										client.close();
									}
								}
							//se l'esito è negativo
							}else {
								//Il file non esiste
								System.out.println("il file non esiste");
								client.close();
							}
					} catch (IOException e){
						System.out.println("ERRORE: "+e.getMessage());
						key.cancel();
					}
				}
			}
		}
	}
}

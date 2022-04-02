/*
Emanuele Torrisi
503058
Corso A
*/

import java.rmi.Remote;
import java.rmi.RemoteException;

/*
interfaccia remota che dichiara i metodi di accessibili da remoto da parte del client. Nello specifico il client
ha la possibilità sia di registrare un nuovo speaker sia la possibilità di stampare il programma per un determinato
giorno
*/

public interface ServerCongresso extends Remote{
	boolean registrazione(int giorno, String speaker, String sessione) throws RemoteException;

	Classeaux programma(int giorno) throws RemoteException;
}

/*
Emanuele Torrisi
503058
Corso A
*/


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.RemoteServer;

/*
implementazione dell'interfaccia remota ServerCongresso. In questa classe vengono definiti i vari comportamenti che
dovranno assumere i metodi quando invocati dal client (e verranno eseguiti sul server).
estendendo la classe RemoteServer e non UnicastRemoteObject devo gestire l'esportazione esplicita dell'oggetto remoto
*/

public class ServerCongressoImpl extends RemoteServer implements ServerCongresso {

	private static final long serialVersionUID = 1L;
	// array che utilizzo per le 3 giornate del congresso
	static Classeaux programma[];

	public ServerCongressoImpl()throws RemoteException {
		//costruisco un server Remoto
		super();
		}


	public boolean registrazione (int giorno, String speaker, String sessione) throws RemoteException {
		int ns = -1;
		System.out.println("Server: richiesta di registrazione per: " + speaker);
		switch(sessione) {
			case "S1":
				ns = 0;
			break;
			case "S2":
				ns = 1;
			break;
			case "S3":
				ns = 2;
			break;
			case "S4":
				ns = 3;
			break;
			case "S5":
				ns = 4;
			break;
			case "S6":
				ns = 5;
			break;
			case "S7":
				ns = 6;
			break;
			case "S8":
				ns = 7;
			break;
			case "S9":
				ns = 8;
			break;
			case "S10":
				ns = 9;
			break;
			case "S11":
				ns = 10;
			break;
			case "S12":
				ns = 11;
			break;
			default: throw new RemoteException();
		}
		if (giorno < 1 || giorno > 3) throw new RemoteException();
		// registro lo speaker all'interno di una matrice sfruttando un metodo di una classe ausiliaria
		return ((programma[giorno-1].registra(speaker,ns))==true);
	}


	public Classeaux programma (int giorno)throws RemoteException{
		System.out.println("Server: programma del giorno "+giorno);
		if (giorno < 1 || giorno > 3) throw new RemoteException();
		return programma[giorno-1];
	}


	public static void main (String[] args){
		programma = new Classeaux[3];
		for (int i=0; i<3; i++) programma[i]= new Classeaux();
		try{
			//creo un'istanza dell'oggetto ServerCongresso
			ServerCongressoImpl server = new ServerCongressoImpl();
			//esporto dinamicamente l'oggetto sulla porta standard di RMI.
			ServerCongresso stub = (ServerCongresso) UnicastRemoteObject.exportObject(server,0);
			//creo un registry sulla porta 1099 (default)
		 	LocateRegistry.createRegistry(1099);
			Registry r = LocateRegistry.getRegistry(1099);
			//pubblico lo stub nel registry
			r.rebind ("ciao", stub);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
}

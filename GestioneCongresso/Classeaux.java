/*
Emanuele Torrisi
503058
Corso A
*/

import java.io.Serializable;

/*
Utilizzo una classe ausiliaria sfruttata da ServerCongressoImpl (e in seguito da ClientCongresso quando invocati i metodi remoti)
in cui attraverso il metodo costruttore creo una matrice di elementi "null" con tante linee quanto sono le sessioni e tante colonne quanti sono gli interventi.
Con il metodo "stampa" stampo tutti gli speaker registrati fino a quel momento per una specifica giornata mentre con il metodo "registra"
salvo all'interno della matrice il nome dello speaker fino ad un massimo di 5.
*/

public class Classeaux implements Serializable {

	private static final long serialVersionUID = 1L;
	public String speaker[][] = new String[12][5];

	public Classeaux(){
		for(int j = 0; j < 5; j++)
			for (int i = 0; i < 12; i++)
				speaker[i][j] = null;
	}

	public void stampa (){
		System.out.println("\t\tIntervento1\tIntervento2\tIntervento3\tIntervento4\tIntervento5");
		for (int k=0; k<12; k++){
			String linea = new String("S"+(k+1));
			for (int j=0;j<5;j++){
				if(speaker[k][j] != null)
				 linea = linea + "\t\t"+speaker[k][j];
				else linea = linea + "\t ";
			}
			System.out.println(linea);
		}
	}

	public boolean registra(String nomespeaker, int sessione){
		for(int i = 0; i < 5; i++){
			if(speaker[sessione][i] == null){
				speaker[sessione][i] = nomespeaker;
				return true;
			}
		}
		return false;
	}

}

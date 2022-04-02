import java.util.Comparator;

public class ConfrontaPriorita implements Comparator<Utente> {

	public int compare(Utente utente1, Utente utente2) {
    int priorita1 = utente1.getpriorita();
    int priorita2 = utente2.getpriorita();

    if (priorita1 > priorita2) {
        return -1;
    } 
    else {
        return 1;
    }
	}
}


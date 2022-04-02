
public class Studente extends Utente{
		
	private int computer;
	private Monitor m;
	
	public Studente(int i, Monitor m) {
		this.identificativo = i;
		this.m = m;
	}
	
	public void UsaComputer() {
		if((this.computer = this.m.UsaComputer(this)) != -1);
	}
	
	public void LasciaComputer() {
		this.m.LasciaComputer(this.computer, this);
		}
}

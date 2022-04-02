
public class Professore extends Utente {
	
	private Monitor m;
	
	public Professore(int i, Monitor m) {
		this.identificativo = i;
		this.m = m;
	}
	
	public void UsaComputer() {
		this.m.UsaTuttiComputer(this);
	}
	
	
	public void LasciaComputer() {
		this.m.LasciaTuttiComputer(this);
	}
}

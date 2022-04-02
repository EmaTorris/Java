
public class Tesista extends Utente{
	
	private int computer;
	private Monitor m;
	
	public Tesista(int i, int computer, Monitor m) {
		this.identificativo = i;
		this.computer = computer;
		this.m = m;
	}
	
	
	public void UsaComputer() {
		this.m.UsaComputerEsclusivo(this.computer,this);
	}
	
	public void LasciaComputer() {
		this.m.LasciaComputerEsclusivo(this.computer, this);
	}
}

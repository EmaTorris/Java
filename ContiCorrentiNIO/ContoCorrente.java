import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ContoCorrente implements Serializable {
	
	private static final long serialVersionUID  = 7L;
	private String correntista;
	private List<Movimento> movimento;
	
	public ContoCorrente(String c ) {
		this.correntista = c;
		movimento = new ArrayList<Movimento>();
	}
	
	public String OttieniCorrentista() {
		return this.correntista;
	}
	
	public void InserisciNuovoMovimento(Movimento m) {
		this.movimento.add(m);
	}
	
	public List<Movimento> PrendiMovimenti(){
		return this.movimento;
	}
}

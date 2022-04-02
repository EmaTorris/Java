import java.io.Serializable;

public class Movimento implements Serializable{

	private static final long serialVersionUID  = 7L;
	private long data;
	private Causale c;
	
	
	public Movimento(long data, Causale c) {
		this.data = data;
		this.c = c;
	}
	
	public Causale OttieniCausale() {
		return this.c;
	}
	
	/*public long OttieniData() {
		return this.data;
	}*/
	
	
}

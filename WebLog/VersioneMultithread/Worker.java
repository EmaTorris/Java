import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Worker implements Runnable {
	
	private String stringa;
	
	
	public Worker(String s) {
		this.stringa = s;
	}
	
	
	@Override
	public void run() {
		String[] tokens = this.stringa.split("\\s+");
		String var0 = tokens[0];
		InetAddress addr = null;
		try {
			addr = InetAddress.getByName(var0);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		String host = addr.getHostName();
		String linea2 = this.stringa.replace(var0, host);
		System.out.println(linea2);
	}
}

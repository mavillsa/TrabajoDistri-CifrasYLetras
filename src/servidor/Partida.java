package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Partida implements Runnable {
	private List<Socket> l;
	public Partida(List<Socket> s) {
		this.l=s;
	}
	public void run() {
		List<BufferedReader> lbr = new ArrayList<>();
		List<PrintWriter> lpw = new ArrayList<>();
		List<String> listanombres = new ArrayList<>();
		try {
			for (Socket s : l) {
		        lbr.add(new BufferedReader(new InputStreamReader(s.getInputStream())));
		        lpw.add(new PrintWriter(s.getOutputStream(), true));
		    }
			for(int i = 0; i< lbr.size(); i++) {
				String nombre = lbr.get(i).readLine().substring(7);
				listanombres.add(nombre);
			}
			for(int k =0; k<lpw.size(); k++) {
				lpw.get(k).println("EMPIEZA "  + String.join(",", listanombres));
			}
			
			
			
			
			
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			for (Socket s : l) {
				try {
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}

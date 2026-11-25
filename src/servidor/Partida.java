package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Partida implements Runnable {
	private List<Socket> l;
	public Partida(List<Socket> s) {
		this.l=s;
	}
	public void run() {
		List<BufferedReader> lbr = new ArrayList<>();
		List<PrintWriter> lpw = new ArrayList<>();
		List<String> listanombres = new ArrayList<>();
		List<String> listapalabras = new ArrayList<>();
		List<Integer> listapuntos = new ArrayList<>();
		List<String> listaresultado = new ArrayList<>();
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
			String letras = generaLetras();
			for(int k =0; k<lpw.size(); k++) {
				lpw.get(k).println("LETRAS " + letras);
			}
			for(int i = 0; i< lbr.size(); i++) {
				String palabra = lbr.get(i).readLine().substring(8);
				listapalabras.add(palabra);
			}
			for(int i = 0; i< lbr.size(); i++) {
				listapuntos.add(listapalabras.get(i).length());
			}
			for(int k =0; k<lpw.size(); k++) {
				listaresultado.add(listanombres.get(k) + ":" + listapalabras.get(k) + ":" + listapuntos.get(k));
			}
			String resultado = String.join(" ", listaresultado);
			for(int k =0; k<lpw.size(); k++) {
				lpw.get(k).println("RESULTADO  " + resultado);
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
	public String generaLetras() {
		String vocales = "AEIOU";
		String consonantes = "BCDFGHJLMNPQRSTVZ"; //He quitado algunas para facilitar el juego 
		List<String> lista = new ArrayList<>();
		Random r = new Random();
		for (int i = 0; i<4; i++) {
			int a = r.nextInt(vocales.length());
			char vocal = vocales.charAt(a);
			lista.add(String.valueOf(vocal));
		}
		for (int i = 0; i<5; i++) {
			int k = r.nextInt(consonantes.length());
			char cons = consonantes.charAt(k);
			lista.add(String.valueOf(cons));
		}
		String letras = lista.get(0);
		for(int i = 1; i<lista.size(); i++) {
			letras = letras + ", " + lista.get(i);
		}
		return letras;
	}
}

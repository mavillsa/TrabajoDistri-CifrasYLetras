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
	    try {
	        for (Socket s : l) {
	            lbr.add(new BufferedReader(new InputStreamReader(s.getInputStream())));
	            lpw.add(new PrintWriter(s.getOutputStream(), true));
	        }

	        for (int i = 0; i < lbr.size(); i++) {
	            String nombre = lbr.get(i).readLine().substring(7);
	            listanombres.add(nombre);
	        }

	        for (int k = 0; k < lpw.size(); k++) {
	            lpw.get(k).println("EMPIEZA " + String.join(",", listanombres));
	        }

	        List<Integer> puntosLetras = rondaLetras(lbr, lpw, listanombres);
	        List<Integer> puntosCifras = rondaCifras(lbr, lpw, listanombres);

	        List<String> totales = new ArrayList<>();
	        for (int i = 0; i < listanombres.size(); i++) {
	            int total = puntosLetras.get(i) + puntosCifras.get(i);
	            totales.add(listanombres.get(i) + ":" + total);
	        }

	        String fin = String.join(" ", totales);
	        for (int k = 0; k < lpw.size(); k++) {
	            lpw.get(k).println("FINPARTIDA " + fin);
	        }

	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
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
	public Boolean compruebaPalabra(String palabra, String letras) {
		String[] listaletras;
		listaletras = letras.split(",");
		List<String> lista = new ArrayList<>();
		for(int i = 0; i<listaletras.length; i++) {
			lista.add(listaletras[i].trim());
		}
		String palabraMayus = palabra.toUpperCase();
		for(int k=0; k<palabraMayus.length(); k++) {
			String letra = String.valueOf(palabraMayus.charAt(k));
			if(lista.contains(letra)) {
					lista.remove(letra);
			}
			else {
				return false;
			}		
		}
		return true;
	}
	public int[] generaNumeros() {
		int[] listaNum = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 25, 50, 75, 100};
		Random r = new Random();
		List<Integer> listaFin = new ArrayList<>();
		Integer objetivo= r.nextInt(899)+101;
		for(int i=0; i<6; i++) {
			int k = r.nextInt(listaNum.length);
			Integer numero = Integer.valueOf(listaNum[k]);
			listaFin.add(numero);
		}
		int[] lista=new int[7];
		for(int k =0; k<listaFin.size(); k++) {
			lista[k]=listaFin.get(k);
		}
		lista[listaFin.size()]=objetivo;
		return lista;
	}
	public List<Integer> rondaLetras(List<BufferedReader> lbr, List<PrintWriter> lpw, List<String> listanombres) throws IOException{
		String letras = generaLetras();
		List<String> listapalabras = new ArrayList<>();
		List<Integer> listapuntos = new ArrayList<>();
		List<String> listaresultado = new ArrayList<>();
		
		for(int k =0; k<lpw.size(); k++) {
			lpw.get(k).println("LETRAS " + letras);
		}
		for(int i = 0; i< lbr.size(); i++) {
			String palabra = lbr.get(i).readLine().substring(8);
			if(compruebaPalabra(palabra, letras)) {
				listapalabras.add(palabra);
			}else {
				listapalabras.add("");
				lpw.get(i).println("Palabra erronea");
			}
				
		}
		for(int i = 0; i< lbr.size(); i++) {
			listapuntos.add(listapalabras.get(i).length());
		}
		for(int k =0; k<lpw.size(); k++) {
			listaresultado.add(listanombres.get(k) + ":" + listapalabras.get(k) + ":" + listapuntos.get(k));
		}
		String resultado = String.join(" ", listaresultado);
		for(int k =0; k<lpw.size(); k++) {
			lpw.get(k).println("RESULTADO " + resultado);
		}
	return listapuntos;
	}
	public List<Integer> rondaCifras(List<BufferedReader> lbr, List<PrintWriter> lpw, List<String> listanombres) throws IOException{
		
		
		int[] cifras = generaNumeros();
		List<Integer> listanumeros = new ArrayList<>();
		List<String> listacifras = new ArrayList<>();
		List<Integer> listapuntos = new ArrayList<>();
		List<String> listaresultado = new ArrayList<>();
		
		for (int i =0; i<cifras.length-1; i++) {
				listacifras.add(String.valueOf(cifras[i]));
		}
		Integer obj = cifras[cifras.length-1];
		for(int k =0; k<lpw.size(); k++) {
			lpw.get(k).println("CIFRAS "  + String.join(",", listacifras) + " OBJETIVO " + obj);
		}
		for (int i = 0; i < lbr.size(); i++) {
			String numero = lbr.get(i).readLine().substring(7);
			int num = Integer.parseInt(numero);
			listanumeros.add(num);                 

			int distancia = Math.abs(obj - num);
			int puntos = 0;                        
			if (distancia == 0) {
			     puntos = 10;
			} else if (distancia <= 5) {
			     puntos = 7;
			} else if (distancia <= 10) {
			     puntos = 5;
			}
			listapuntos.add(puntos);               
		}
		for(int k =0; k<lpw.size(); k++) {
			listaresultado.add(listanombres.get(k) + ":" + listanumeros.get(k) + ":" + listapuntos.get(k));
		}
		String resultado = String.join(" ", listaresultado);
		for(int k =0; k<lpw.size(); k++) {
			lpw.get(k).println("RESULTADO " + resultado);
		}
		return listapuntos;
		
	}
}

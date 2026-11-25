package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		try(Socket s = new Socket("localhost", 6666);
				BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
				PrintWriter pw = new PrintWriter(s.getOutputStream(), true)){
			System.out.println("Dame tu nombre");
			String nombre = sc.nextLine();
			pw.println("NOMBRE "+ nombre);
			String linea = br.readLine();
			while(linea!=null) {
				System.out.println(linea);
				if(linea.startsWith("LETRAS")) {
					System.out.println("Introduce tu palabra usando el máximo de letras una sola vez");
					String palabra = sc.nextLine();
					pw.println("PALABRA " + palabra);
				}
				linea = br.readLine();
			}
			
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

}

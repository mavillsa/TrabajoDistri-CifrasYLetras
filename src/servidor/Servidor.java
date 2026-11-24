package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {

	public static void main(String[] args) {
		int N = 2; //son los jugadores por partida
		int puerto = 6666;
		ExecutorService pool = Executors.newCachedThreadPool();
		try(ServerSocket ss = new ServerSocket(puerto)){
			
			while(true) {
				List<Socket> lista = new ArrayList<>();
				for(int i=0; i<N; i++) {
					lista.add(ss.accept());
					System.out.println("Jugador" + i + " conectado");
				}
				pool.execute(new Partida(lista));
			}
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			pool.shutdown();
		}

	}

}

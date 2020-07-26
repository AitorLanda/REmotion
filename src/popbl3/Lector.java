package popbl3;

import java.io.IOException;

import gnu.io.CommPortIdentifier;

public class Lector extends Thread {
	SerialComm lineaSerie;
	CommPortIdentifier puerto;
	volatile boolean parar = false;
	public boolean terminado;
	boolean agotado;
	
	public Lector(SerialComm lineaSerie, CommPortIdentifier puerto) {
		this.lineaSerie = lineaSerie;
		this.puerto = puerto;
	}

	@Override
	public void run() {
		String mensaje = null;
		
		try {
			do {
				  mensaje = lineaSerie.leer();
				  System.out.println(mensaje);
				  if(mensaje.equals("3"))
				  {
					  terminado=true;
					  agotado = true;
				  }
			}while (!mensaje.contains("fin"));
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println("fin hilo lector");
	}
	public void parar() {
		parar = true;
	}

	public boolean isTerminado() {
		return terminado;
	}
	
	public void setTerminado(boolean terminado) {
		this.terminado = terminado;
	}

	public boolean isAgotado() {
		return agotado;
	}
	
}

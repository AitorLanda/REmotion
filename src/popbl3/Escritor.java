package popbl3;

import gnu.io.CommPortIdentifier;

public class Escritor extends Thread {
	
	final String TEXTO = "Hola, soy yo";
	SerialComm lineaSerie;
	CommPortIdentifier puerto;
	volatile boolean parar = false;
	
	public Escritor (SerialComm lineaSerie, CommPortIdentifier puerto) {
		this.lineaSerie = lineaSerie;
		this.puerto = puerto;
	}

	@Override
	public void run() {
		while (!parar) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				lineaSerie.escribir(TEXTO+SerialComm.CARACTERFIN);
		}
		System.out.println("Fin hilo escritor");
	}
	public void parar() {
		parar = true;
	}
}

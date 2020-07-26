package popbl3;

import java.util.Observable;

import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Vector;

public class GestorMano extends Observable {
	public enum RESULTADO {
		BIEN, CASI, MAL;
	}

	public enum DEDOS {
		GORDO, INDICE, MEDIO, ANULAR, MEÑIQUE;
	}

	private final float RADIO_MANO_CASI_CERRADA = 50;
	private final float RADIO_MANO_CERRADA = 40;
	private final int MANO_SIN_INICIALIZAR = -1;
	private final double DISTANCIA_NO_VALIDA = -1;
	private final double DISTANCIA_TOCANDO = 30;
	private final double DISTANCIA_CASI_TOCANDO = 40;
	private final double GIRO_MANO_GIRADA = 0;
	private final double GIRO_MANO_CASI_GIRADA = 0;
	
	private Hand handFrame = null;
	float radioMax = MANO_SIN_INICIALIZAR;
	float radioMin;
	float radioActual;
	boolean cerrada, dedoTocando, conectado, inicializado,girada;

	public void setDedoTocando(boolean dedoTocando) {
		this.dedoTocando = dedoTocando;
	}

	public synchronized float getRadioMax() {
		return radioMax;
	}

	public synchronized float getRadioMin() {
		return radioMin;
	}

	public synchronized float getRadioActual() {
		return radioActual;
	}

	public synchronized boolean isConectado() {
		return conectado;
	}

	public synchronized void setConectado(boolean conectado) {
		this.conectado = conectado;
		this.setChanged();
		this.notifyObservers();
	}

	public synchronized boolean isInicializado() {
		return inicializado;
	}

	public synchronized void setInicializado(boolean inicializado) {
		this.inicializado = inicializado;
		this.setChanged();
		this.notifyObservers();
	}

	public void setHand(Hand nuevaMano) {

		if (nuevaMano.isValid()) {

			handFrame = nuevaMano;
			setRadios();
			this.setChanged();
			this.notifyObservers();
		} else {
			handFrame = null;
		}

	}

	public double getDistDedo(DEDOS tipoDedo) {
		Vector vPulgar = null;
		Vector vDedo = null;
		double distancia = DISTANCIA_NO_VALIDA;
		boolean error = false;
		Finger.Type tipoDedoLeap = null;

		if (handFrame != null) {

			switch (tipoDedo) {
			case INDICE:
				tipoDedoLeap = Finger.Type.TYPE_INDEX;
				break;

			case MEDIO:
				tipoDedoLeap = Finger.Type.TYPE_MIDDLE;
				break;

			case ANULAR:
				tipoDedoLeap = Finger.Type.TYPE_RING;
				break;

			case MEÑIQUE:
				tipoDedoLeap = Finger.Type.TYPE_PINKY;
				break;

			default:
				error = true;
				break;
			}

			if (!error) {

				for (Finger dedoAux : handFrame.fingers()) {
					if (dedoAux.type() == Finger.Type.TYPE_THUMB) {
						vPulgar = dedoAux.tipPosition();
						break;
					}
				}

				for (Finger dedoAux : handFrame.fingers()) {
					if (dedoAux.type() == tipoDedoLeap) {
						vDedo = dedoAux.tipPosition();
						break;
					}
				}

				distancia = Math.sqrt(Math.pow(vPulgar.getX() - vDedo.getX(), 2)
						+ Math.pow(vPulgar.getY() - vDedo.getY(), 2) + Math.pow(vPulgar.getZ() - vDedo.getZ(), 2));
			}

		}
		return distancia;
	}

	private void setRadios() {

		radioActual = handFrame.sphereRadius();
		if (radioMax == MANO_SIN_INICIALIZAR) {
			radioMax = radioActual;
			radioMin = radioActual;
		} else if (radioMax < radioActual) {
			radioMax = radioActual;
		} else if (radioMin > radioActual) {
			radioMin = radioActual;
		}

	}

	public Hand getHand() {
		return handFrame;
	}

	public RESULTADO isCerrada() {
		RESULTADO resultado;

		if (radioActual < RADIO_MANO_CERRADA) {
			cerrada = true;
			resultado = RESULTADO.BIEN;
		} else if (radioActual < RADIO_MANO_CASI_CERRADA) {
			if (cerrada)
				resultado = RESULTADO.BIEN;
			else
				resultado = RESULTADO.CASI;
		} else {
			cerrada = false;
			resultado = RESULTADO.MAL;
		}

		return resultado;

	}

	public RESULTADO isGirada() {
		RESULTADO resultado;
		float Y = handFrame.palmNormal().getY();
		if (0.901 <= Y && Y <= 1.000) {
			girada = true;
			resultado = RESULTADO.BIEN;
		} else if (0.800 <= Y && Y <= 0.900) {
			if (girada)
				resultado = RESULTADO.BIEN;
			else
				resultado = RESULTADO.CASI;
		} else {
			girada = false;
			resultado = RESULTADO.MAL;
		}
		return resultado;
	}

	public float getRadio() {
		return radioActual;
	}

	public RESULTADO estaTocandoConDedoPulgar(DEDOS tipoDedo) {
		RESULTADO resultado;
		double distancia = getDistDedo(tipoDedo);
		if (distancia != DISTANCIA_NO_VALIDA && distancia < DISTANCIA_TOCANDO) {
			if (isDedoMasCercano())
				resultado = RESULTADO.BIEN;
			else
				resultado = RESULTADO.CASI;
			dedoTocando = true;
		} else if (distancia != DISTANCIA_NO_VALIDA && distancia < DISTANCIA_CASI_TOCANDO) {
			if (dedoTocando)
				resultado = RESULTADO.BIEN;
			else
				resultado = RESULTADO.CASI;
		} else {
			resultado = RESULTADO.MAL;
			dedoTocando = false;
		}
		return resultado;
	}

	private boolean isDedoMasCercano() {
		// TODO Auto-generated method stub
		return true;
	}

	public RESULTADO isDown() {
		RESULTADO resultado;
		float Z = handFrame.palmNormal().getZ();
		if (0.901 <= Z && Z <= 1.000) {
			girada = true;
			resultado = RESULTADO.BIEN;
		} else if (0.800 <= Z && Z <= 0.900) {
			if (girada)
				resultado = RESULTADO.BIEN;
			else
				resultado = RESULTADO.CASI;
		} else {
			girada = false;
			resultado = RESULTADO.MAL;
		}
		return resultado;
	}
}



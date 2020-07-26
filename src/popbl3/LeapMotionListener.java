package popbl3;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Listener;

public class LeapMotionListener extends Listener {

	private Frame frameActual;
	private GestorMano gestorMano;

	public LeapMotionListener(GestorMano gestorMano) {
		this.gestorMano = gestorMano;
		
	}

	@Override
	public void onInit(Controller controlador) {
	
		gestorMano.setInicializado(true);
	
		super.onInit(controlador);
	}

	@Override
	public void onConnect(Controller controlador) {
	
		gestorMano.setConectado(true);
		super.onConnect(controlador);
	}

	@Override
	public void onFrame(Controller controlador) {
		frameActual = controlador.frame();

		if ((frameActual.hands().count()) == 1) {
			gestorMano.setHand(frameActual.hands().get(0));
		}
		super.onFrame(controlador);
	}

	@Override
	public void onDisconnect(Controller arg0) {
		gestorMano.setConectado(false);
		super.onDisconnect(arg0);
	}

	@Override
	public void onExit(Controller arg0) {
		gestorMano.setInicializado(false);
		super.onExit(arg0);

	}
}

package popbl3;

import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import popbl3.GestorMano.DEDOS;



public class LeapMotionFrame implements Observer {

	private GestorMano gestorMano;
	private Controller controlador;
	private LeapMotionListener leapListener;

	private Frame frameActual = null;
	private boolean abierta = true;

	private JFrame ventana;
	private JLabel lblDatoRadioActual;
	private JLabel lblDatoRadioMax;
	private JLabel lblDatoRadioMin;
	private JLabel lblDatoInfo;
	private JLabel lblInicializado;
	private JLabel lblEstado;
	private JLabel lblDatoIndice;
	private JLabel lblDatoMedio;
	private JLabel lblDatoAnular;
	private JLabel lblDatoMeñique;
	private boolean creado = false;

	public LeapMotionFrame() {

		ventana = new JFrame("DATOS");
		ventana.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				quitarControlador();
				abierta = false;
				ventana.dispose();
			}
		});

		ventana.setContentPane(crearPanelPrincipal());
		ventana.setSize(585, 380);
		ventana.setVisible(true);
		ventana.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		gestorMano = new GestorMano();
		gestorMano.addObserver(this);
		
		leapListener = new LeapMotionListener(gestorMano);
		controlador = new Controller();
		controlador.addListener(leapListener);

	}

	private Container crearPanelPrincipal() {
		JPanel pPrincipal = new JPanel();
		pPrincipal.setLayout(null);
		pPrincipal.setBorder(new EmptyBorder(5, 5, 5, 5));

		lblEstado = new JLabel("Desconectado");
		lblEstado.setBounds(10, 10, 68, 14);
		pPrincipal.add(lblEstado);

		lblInicializado = new JLabel("Inicializado");
		lblInicializado.setBounds(88, 10, 52, 14);
		pPrincipal.add(lblInicializado);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 35, 156, 265);
		pPrincipal.add(scrollPane);

		JPanel pnDatos = new JPanel();
		pnDatos.setLayout(null);
		pnDatos.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Mano Cerrada", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(0, 0, 0)));
		pnDatos.setBounds(176, 10, 136, 138);
		pPrincipal.add(pnDatos);

		JLabel lblRadio = new JLabel("Radio Actual:");
		lblRadio.setBounds(10, 25, 64, 14);
		pnDatos.add(lblRadio);

		JLabel lblRadioMax = new JLabel("Radio Max:");
		lblRadioMax.setBounds(10, 50, 64, 14);
		pnDatos.add(lblRadioMax);

		JLabel lblRadioMin = new JLabel("Radio Min:");
		lblRadioMin.setBounds(10, 75, 64, 14);
		pnDatos.add(lblRadioMin);

		JLabel lblInfo_1 = new JLabel("Estado de\r\n");
		lblInfo_1.setBounds(10, 100, 64, 14);
		pnDatos.add(lblInfo_1);

		lblDatoRadioActual = new JLabel("0.0");
		lblDatoRadioActual.setBounds(84, 25, 46, 14);
		pnDatos.add(lblDatoRadioActual);

		lblDatoRadioMax = new JLabel("0.0");
		lblDatoRadioMax.setBounds(84, 50, 46, 14);
		pnDatos.add(lblDatoRadioMax);

		lblDatoRadioMin = new JLabel("0.0");
		lblDatoRadioMin.setBounds(84, 75, 46, 14);
		pnDatos.add(lblDatoRadioMin);

		lblDatoInfo = new JLabel("UnDefined");
		lblDatoInfo.setBounds(77, 100, 49, 14);
		pnDatos.add(lblDatoInfo);

		JLabel lblInfo2 = new JLabel("la mano:");
		lblInfo2.setBounds(10, 113, 57, 14);
		pnDatos.add(lblInfo2);

		Button button = new Button("Guardar");
		button.setBounds(10, 309, 70, 22);
		pPrincipal.add(button);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Distancia entre el dedo GORDO y ....",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(322, 10, 237, 138);
		pPrincipal.add(panel);
		panel.setLayout(null);

		JLabel lblMedio = new JLabel("Dedo medio:");
		lblMedio.setBounds(10, 50, 78, 14);
		panel.add(lblMedio);

		JLabel lblAnular = new JLabel("Dedo anular:");
		lblAnular.setBounds(10, 75, 78, 14);
		panel.add(lblAnular);

		JLabel lblMeñique = new JLabel("Dedo me\u00F1ique:");
		lblMeñique.setBounds(10, 100, 78, 14);
		panel.add(lblMeñique);

		JLabel lblInidice = new JLabel("Dedo indice:");
		lblInidice.setBounds(10, 25, 78, 14);
		panel.add(lblInidice);

		lblDatoMeñique = new JLabel("-1");
		lblDatoMeñique.setBounds(98, 100, 129, 14);
		panel.add(lblDatoMeñique);

		lblDatoAnular = new JLabel("-1");
		lblDatoAnular.setBounds(98, 75, 129, 14);
		panel.add(lblDatoAnular);

		lblDatoMedio = new JLabel("-1");
		lblDatoMedio.setBounds(98, 50, 129, 14);
		panel.add(lblDatoMedio);

		lblDatoIndice = new JLabel("-1");
		lblDatoIndice.setBounds(98, 25, 129, 14);
		panel.add(lblDatoIndice);
		creado = true;
		return pPrincipal;
	}

	public void quitarControlador() {
		controlador.removeListener(leapListener);

	}

	public boolean estaAbierta() {
		return abierta;
	}

	public GestorMano getGestor() {
		return gestorMano;
	}

	public Frame getFrame() {
		return frameActual;
	}

	@Override
	public void update(Observable o, Object arg) {
		
		lblEstado.setText(gestorMano.isConectado() ? "Conectado" : "Desconectado");
		lblInicializado.setText(gestorMano.isInicializado() ? "Inicializado" : "Sin inicializar");

		lblDatoRadioActual.setText(Float.toString(gestorMano.getRadio()));
		lblDatoRadioMax.setText(Float.toString(gestorMano.getRadioMax()));
		lblDatoRadioMin.setText(Float.toString(gestorMano.getRadioMin()));
		
		//lblDatoInfo.setText(gestorMano.isCerrada() ? "Cerrada" : "Abierta");
		
	//	lblDatoIndice.setText(gestorMano.estaTacandoDedo(DEDOS.INDICE)? "Tocando" : "Separado");
		/*
		lblDatoIndice.setText(new Double(gestorMano.getDistDedo(DEDOS.INDICE)).toString());	 */
		lblDatoMedio.setText(new Double(gestorMano.getDistDedo(DEDOS.MEDIO)).toString());
		lblDatoAnular.setText(new Double(gestorMano.getDistDedo(DEDOS.ANULAR)).toString());
		lblDatoMeñique.setText(new Double(gestorMano.getDistDedo(DEDOS.MEÑIQUE)).toString());
	
		ventana.repaint();

	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					//frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public boolean isAbierto() {
		return abierta;
	}
}

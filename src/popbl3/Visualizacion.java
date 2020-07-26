package popbl3;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;

public class Visualizacion{

	private final static String ICONO_SALIR = "/iconos/exit.png";
	private final static String ICONO_TABLA = "/iconos/tabla.png";
	private final static String ICONO_GRAFICO = "/iconos/gráfico.png";
	private final static String TABLA_STRING = "Tabla";
	private final static String GRAFICO_STRING = "Grafico";
	private final static String SALIR_STRING = "Salir";

	
	private MiAction accSalir;
	private MiAction accGrafico;
	private MiAction accTabla;

	private Paciente paciente;
	JTabbedPane tabPane;
	JFrame ventana;

	public Visualizacion(Paciente paciente) {
		this.paciente = paciente;
		ventana = new JFrame();
	}
	
	public JFrame crearVentana() {
		ventana.setTitle("Resultado de ejercicios");
		this.crearAcciones();
		ventana.setJMenuBar(crearMenuBar());
		ventana.setSize(800, 600);
		ventana.add(crearPanelPestanas(), BorderLayout.CENTER);
		ventana.setMinimumSize(new Dimension(800, 600));
		ventana.setVisible(true);
		ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		return ventana;
	}
	
	private void crearAcciones() {
		accGrafico = new MiAction(GRAFICO_STRING, new ImageIcon(this.getClass().getResource(ICONO_GRAFICO)), GRAFICO_STRING,
				KeyEvent.VK_G);
		accTabla = new MiAction(TABLA_STRING, new ImageIcon(this.getClass().getResource(ICONO_TABLA)), TABLA_STRING,
				KeyEvent.VK_T);
		accSalir = new MiAction(SALIR_STRING, new ImageIcon(this.getClass().getResource(ICONO_SALIR)), SALIR_STRING,
				KeyEvent.VK_S);
	}

	public Component crearPanelPestanas() {
		tabPane = new JTabbedPane();
		tabPane.addTab(TABLA_STRING, new ImageIcon(this.getClass().getResource(ICONO_TABLA)),
				new VisualizadorTabla().visualizadorTabla(paciente));
		tabPane.addTab(GRAFICO_STRING, new ImageIcon(this.getClass().getResource(ICONO_GRAFICO)),
				new Calendar().calendar(paciente));
		return tabPane;
	}

	private JMenuBar crearMenuBar() {
		JMenuBar barra = new JMenuBar();
		JMenu vista = new JMenu("Vista");
		vista.add(accGrafico);
		vista.addSeparator();
		vista.add(accTabla);
		barra.add(vista);
		barra.add(Box.createHorizontalGlue());
		JMenu salir = new JMenu(SALIR_STRING);
		salir.add(accSalir);
		barra.add(salir);
		return barra;

	}

	private class MiAction extends AbstractAction {
		String texto;

		public MiAction(String texto, Icon imagen, String descrip, Integer nemonic) {
			super(texto, imagen);
			this.texto = texto;
			this.putValue(Action.SHORT_DESCRIPTION, descrip);
			this.putValue(Action.MNEMONIC_KEY, nemonic);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (texto.equals(TABLA_STRING)) {
				tabPane.setSelectedIndex(0);
			}
			if (texto.equals(GRAFICO_STRING)) {
				tabPane.setSelectedIndex(1);
			}
			if (texto.equals(SALIR_STRING)) {
				ventana.dispose();
			}
		}
	}

}



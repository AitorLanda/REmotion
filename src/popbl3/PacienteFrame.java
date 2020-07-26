package popbl3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

import javax.media.j3d.Canvas3D;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.leapmotion.leap.Controller;
import com.sun.j3d.utils.universe.SimpleUniverse;

import gnu.io.CommPortIdentifier;
import popbl3.GestorMano.DEDOS;
import popbl3.GestorMano.RESULTADO;

public class PacienteFrame extends JFrame implements ListSelectionListener, Observer {

	private final String ICONO_RESULTADO = "/iconos/verResultados.png";
	private final String ICONO_EJERCICIO_EMPEZAR = "/iconos/hacerEjercicio.png";
	private final String ICONO_EJERCICIO_PARAR = "/iconos/pararEjercicio.png";

	private Paciente paciente;

	private MiAction accVerResultados;
	private MiAction accHacerEjercicio;
	private MiAction accPararEjercicio;
	private MiAction accLogOut;
	private ImageIcon icono;

	private JLabel textoDescripcion;
	private JLabel textoResultadoInSitu;
	private JLabel pantallaImagen;
	private JPanel panatallaLeap;
	private Canvas3D visualizador;
	private GestorMano gestor;
	private Mano3D mano3d;

	SerialComm lineaSerie;
	Lector hiloLectura;
	Escritor hiloEscritura;
	CommPortIdentifier puerto;
	private ModeloListaEjercicios modelo;
	private EjercicioRenderer cellRenderer;
	private JList<Ejercicio> listaEjercicios;

	private RESULTADO ultimoResultado = RESULTADO.MAL;
	private final int EJERCICIO_MANO_CERRADA = 1;
	private final int EJERCICIO_DEDO_INDICE = 2;

	private final int EJERCICIO_DEDO_MEDIO = 3;
	private final int EJERCICIO_DEDO_ANULAR = 4;
	private final int EJERCICIO_DEDO_MEÑIQUE = 5;
	private final int EJERCICIO_GIRAR_MANO = 6;
	private final int EJERCICIO_DOBLAR_MUÑECA_ADELANTE = 7;

	private LeapMotionListener leapListener;

	private Controller controlador;

	private String textoResultadoOut;

	public boolean iniciarEjercicio = false;

	private JButton botonHacerEjercicio;
	private JButton botonVerEjercicio;
	private JProgressBar progresoEjercicio;

	public PacienteFrame(Paciente paciente) {
		super(paciente.getNombre() + " " + paciente.getApellido1() + " " + paciente.getApellido2() + " (Paciente)");
		this.paciente = paciente;
		this.crearAcciones();
		this.setMinimumSize(new Dimension(800, 600));
		this.setLocation(0, 0);
		this.setJMenuBar(crearBarraMenu());
		this.getContentPane().add(crearToolBar(), BorderLayout.NORTH);
		this.getContentPane().add(crearPanelVentana(), BorderLayout.CENTER);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (PacienteFrame.this.iniciarEjercicio) {
					PacienteFrame.this.iniciarPararEjercicio();
				}
				e.getWindow().dispose();
			}
		});
		this.setVisible(true);
		listaEjercicios.setSelectedIndex(0);

		gestor = new GestorMano();
		gestor.addObserver(this);

		leapListener = new LeapMotionListener(gestor);
		controlador = new Controller();

		controlador.addListener(leapListener);
		lineaSerie = new SerialComm();

		puerto = lineaSerie.encontrarPuerto();

		if (puerto == null) {
			System.out.println("No se ha encontrado una linea serie");
		} else {
			try {
				lineaSerie.conectar(puerto);
			} catch (Exception e) {

				e.printStackTrace();
			}
			System.out.println("Linea serie encontrada en: " + puerto.getName());
			hiloLectura = new Lector(lineaSerie, puerto);
			hiloEscritura = new Escritor(lineaSerie, puerto);
			hiloLectura.start();
		}
	}

	private void crearAcciones() {
		accVerResultados = new MiAction("Ver resultados", new ImageIcon(this.getClass().getResource(ICONO_RESULTADO)),
				"Ver resultados del ejercicio", KeyEvent.VK_V);
		accHacerEjercicio = new MiAction("Hacer ejercicio",
				new ImageIcon(this.getClass().getResource(ICONO_EJERCICIO_EMPEZAR)), "Realizar ejercicio selecionado",
				KeyEvent.VK_H);
		accPararEjercicio = new MiAction("Parar ejercicio",
				new ImageIcon(this.getClass().getResource(ICONO_EJERCICIO_PARAR)), "Parar ejercicio en marcha",
				KeyEvent.VK_P);
		accLogOut = new MiAction("Log out", new ImageIcon(this.getClass().getResource(AdminFrame.ICONO_LOGOUT)),
				"Cerrar la sesión actual", KeyEvent.VK_O);
	}

	private JMenuBar crearBarraMenu() {
		JMenuBar barra = new JMenuBar();
		JMenu editar = new JMenu("Editar");
		JMenu salir = new JMenu("Salir");

		editar.add(accVerResultados);
		editar.addSeparator();
		editar.add(accHacerEjercicio);

		barra.add(editar);
		barra.add(Box.createHorizontalGlue());

		salir.add(accLogOut);
		barra.add(salir);

		return barra;
	}

	private Component crearToolBar() {
		JToolBar toolBar = new JToolBar();
		JButton boton;

		toolBar.setFloatable(false);

		toolBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		botonVerEjercicio = (JButton) toolBar.add(new JButton(accVerResultados));
		toolBar.addSeparator(new Dimension(20, 0));
		botonHacerEjercicio = (JButton) toolBar.add(new JButton(accHacerEjercicio));

		toolBar.add(Box.createHorizontalGlue());

		boton = (JButton) toolBar.add(new JButton(accLogOut));

		return toolBar;
	}

	private Component crearPanelVentana() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));

		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		panel.add(crearPanelListaEjercicios(), BorderLayout.WEST);
		panel.add(crearPanelEjercicio(), BorderLayout.CENTER);

		return panel;
	}

	private Component crearPanelListaEjercicios() {
		JScrollPane panelScroll = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panelScroll.setPreferredSize(new Dimension(200, 0));

		cellRenderer = new EjercicioRenderer();
		modelo = new ModeloListaEjercicios(paciente);
		listaEjercicios = new JList<>();

		listaEjercicios.setModel(modelo);
		listaEjercicios.setCellRenderer(cellRenderer);
		listaEjercicios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		listaEjercicios.addListSelectionListener(this);

		panelScroll.setViewportView(listaEjercicios);

		return panelScroll;
	}

	private Component crearPanelEjercicio() {
		JPanel panel = new JPanel(new BorderLayout(20, 20));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		panel.add(crearPanelDescripcion(), BorderLayout.NORTH);
		panel.add(crearPanelCentro(), BorderLayout.CENTER);
		panel.add(crearPanelResultadoInSitu(), BorderLayout.SOUTH);

		return panel;
	}

	private Component crearPanelDescripcion() {
		JPanel panel = new JPanel(new GridLayout(1, 1));
		panel.setPreferredSize(new Dimension(0, 50));
		panel.setBorder(BorderFactory.createLoweredBevelBorder());
		textoDescripcion = new JLabel(" ");
		textoDescripcion.setHorizontalAlignment(SwingConstants.CENTER);
		textoDescripcion.setVerticalAlignment(SwingConstants.CENTER);

		panel.add(textoDescripcion);

		return panel;
	}

	private Component crearPanelCentro() {
		JPanel panel = new JPanel(new GridLayout(1, 2, 5, 5));

		panel.add(crearPanelImagen());
		panel.add(crearPanelWebcam());

		return panel;
	}

	private Component crearPanelImagen() {
		JPanel panel = new JPanel(new GridLayout(1, 1));
		panel.setBorder(BorderFactory.createLoweredBevelBorder());

		pantallaImagen = new JLabel();
		pantallaImagen.setHorizontalAlignment(JLabel.CENTER);
		pantallaImagen.setVerticalAlignment(JLabel.CENTER);

		panel.add(pantallaImagen);
		panel.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				pantallaImagen.setIcon(escalarImagen(icono));
			}
		});
		return panel;
	}

	private Component crearPanelWebcam() {
		panatallaLeap = new JPanel(new BorderLayout());
		panatallaLeap.setBorder(BorderFactory.createLoweredBevelBorder());

		GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
		visualizador = new Canvas3D(gc);

		panatallaLeap.add(visualizador, BorderLayout.CENTER);

		return panatallaLeap;
	}

	private Component crearPanelResultadoInSitu() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(0, 50));
		panel.setBorder(BorderFactory.createLoweredBevelBorder());
		panel.setLayout(new BorderLayout(0, 0));

		textoResultadoInSitu = new JLabel(" ");
		textoResultadoInSitu.setOpaque(true);
		textoResultadoInSitu.setHorizontalAlignment(SwingConstants.CENTER);
		textoResultadoInSitu.setVerticalAlignment(SwingConstants.CENTER);

		panel.add(textoResultadoInSitu, BorderLayout.CENTER);

		progresoEjercicio = new JProgressBar();

		panel.add(progresoEjercicio, BorderLayout.SOUTH);

		return panel;
	}

	private class MiAction extends AbstractAction {
		String texto;
		Visualizacion vista;

		public MiAction(String texto, Icon imagen, String descrip, Integer nemonic) {
			super(texto, imagen);
			this.texto = texto;
			this.putValue(Action.SHORT_DESCRIPTION, descrip);
			this.putValue(Action.MNEMONIC_KEY, nemonic);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			switch (texto) {
			case "Ver resultados":
				vista = new Visualizacion(paciente);
				vista.crearVentana();
				break;

			case "Hacer ejercicio":
				PacienteFrame.this.iniciarPararEjercicio();
				break;

			case "Log out":
				MainFrame frame = new MainFrame();
				PacienteFrame.this.iniciarEjercicio = false;
				PacienteFrame.this.dispose();
				break;

			default:
				JOptionPane.showMessageDialog(PacienteFrame.this, "Error en el switch case", "Error",
						JOptionPane.ERROR_MESSAGE);
				break;

			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {

		if (!e.getValueIsAdjusting()) {
			textoDescripcion.setText(listaEjercicios.getSelectedValue().getDescripcion());

			textoResultadoInSitu.setText("Mano no encontrada");

			icono = new ImageIcon(this.getClass().getResource(listaEjercicios.getSelectedValue().getDirectorioGIF()));
			pantallaImagen.setIcon(escalarImagen(icono));

		}

	}

	@Override
	public void update(Observable arg0, Object arg1) {
		RESULTADO resultado = RESULTADO.MAL;

		if (iniciarEjercicio && gestor.getHand() != null && gestor.getHand().isValid()) {
			if (mano3d == null) {
				mano3d = new Mano3D(visualizador, gestor.getHand());
			} else {
				mano3d.setHand(gestor.getHand());
			}

			switch (Integer.valueOf(listaEjercicios.getSelectedValue().getId())) {
			case EJERCICIO_MANO_CERRADA:
				resultado = gestor.isCerrada();
				mostrarResultado(resultado, "Cerrada", "Cierra mas la mano", "Abierta");
				break;
			case EJERCICIO_DEDO_INDICE:
				resultado = gestor.estaTocandoConDedoPulgar(DEDOS.INDICE);
				mostrarResultado(resultado, "Tocando", "Acerca solo dos dedos, separa los demas", "Separados");
				break;
			case EJERCICIO_DEDO_ANULAR:
				resultado = gestor.estaTocandoConDedoPulgar(DEDOS.ANULAR);
				mostrarResultado(resultado, "Tocando", "Acerca solo dos dedos, separa los demas", "Separados");
				break;
			case EJERCICIO_DEDO_MEDIO:
				resultado = gestor.estaTocandoConDedoPulgar(DEDOS.MEDIO);
				mostrarResultado(resultado, "Tocando", "Acerca solo dos dedos, separa los demas", "Separados");
				break;
			case EJERCICIO_DEDO_MEÑIQUE:
				resultado = gestor.estaTocandoConDedoPulgar(DEDOS.MEÑIQUE);
				mostrarResultado(resultado, "Tocando", "Acerca solo dos dedos, separa los demas", "Separados");
				break;
			case EJERCICIO_GIRAR_MANO:
				resultado = gestor.isGirada();
				mostrarResultado(resultado, "Girada", "Gira más la mano", "sin girar");
				break;
			case EJERCICIO_DOBLAR_MUÑECA_ADELANTE:
				resultado = gestor.isDown();
				mostrarResultado(resultado, "Doblada hacia adelante", "Dobla hacia adelante", "Sin Doblar");
				break;
			default:
				break;
			}

		}
	}

	private boolean mostrarResultado(RESULTADO resultado, String bien, String casi, String mal) {

		if (ultimoResultado != RESULTADO.BIEN && resultado == RESULTADO.BIEN) {
			textoResultadoOut = bien;
			progresoEjercicio.setValue(progresoEjercicio.getValue() + 1);
			textoResultadoInSitu.setBackground(Color.GREEN);
			((Ejercicio) listaEjercicios.getSelectedValue()).modificarResultado(resultado);
			if (progresoEjercicio.getValue() >= progresoEjercicio.getMaximum()) {
				hiloLectura.setTerminado(true);
			}
		} else if (ultimoResultado == RESULTADO.CASI && resultado == RESULTADO.MAL) {
			textoResultadoOut = casi;
			textoResultadoInSitu.setBackground(Color.YELLOW);
			((Ejercicio) listaEjercicios.getSelectedValue()).modificarResultado(ultimoResultado);

		} else if (ultimoResultado == RESULTADO.MAL && resultado == RESULTADO.MAL) {
			if (textoResultadoOut != casi) {
				textoResultadoInSitu.setBackground(Color.RED);
				textoResultadoOut = mal;
			}
		}
		ultimoResultado = resultado;
		textoResultadoInSitu.setText(textoResultadoOut);
		if (hiloLectura.isTerminado()) {
			iniciarPararEjercicio();
		}
		
		if(hiloLectura.isAgotado()) { //posible error
			listaEjercicios.getSelectedValue().setAgotado(true);
		}
		return hiloLectura.isTerminado();
	}

	public void iniciarPararEjercicio() {
		int i = 0;
		int b;
		if (iniciarEjercicio == false) {
			iniciarEjercicio = true;
			botonHacerEjercicio.setText("Parar ejercicio");
			listaEjercicios.setEnabled(false);
			botonVerEjercicio.setEnabled(false);
			progresoEjercicio
					.setValue(((Ejercicio) listaEjercicios.getSelectedValue()).getValorResultado(Ejercicio.BIEN));
			((Ejercicio) listaEjercicios.getSelectedValue()).aSegundos();
			try {
				while (i < 4) {
					b = (((Ejercicio) listaEjercicios.getSelectedValue()).segundos[i]);
					lineaSerie.escribir(Integer.toString(b));
					i++;
				}
				JOptionPane.showMessageDialog(PacienteFrame.this, "Tienes "+listaEjercicios.getSelectedValue().getTiempo()+" segundos", "Información", JOptionPane.INFORMATION_MESSAGE);
				lineaSerie.escribir("}");
				progresoEjercicio.setMaximum(((Ejercicio) listaEjercicios.getSelectedValue()).getMaxRepeticiones());
				hiloLectura.setTerminado(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			iniciarEjercicio = false;
			botonHacerEjercicio.setText("Hacer ejercicio");
			textoResultadoInSitu.setBackground(null);
			((Ejercicio) listaEjercicios.getSelectedValue()).modificarResultado(RESULTADO.MAL);
			progresoEjercicio.setValue(0);
			lineaSerie.escribir("]");
			while (i < 4) {
				lineaSerie.escribir(Integer.toString(0));
				i++;
			}
			lineaSerie.escribir("]");
			botonVerEjercicio.setEnabled(true);
			listaEjercicios.setEnabled(true);
			GestorSockets.modificarDatosFichero(paciente, paciente.getUserName());
		}
	}

	public void hacerEjercicio() {
		botonHacerEjercicio.doClick();
	}

	public void listaUp() {
		if (listaEjercicios.isEnabled()) {
			try {
				listaEjercicios.setSelectedIndex(listaEjercicios.getSelectedIndex() - 1);
			} catch (IndexOutOfBoundsException e) {
				System.out.println("La opcion elegida no es la correcta");
			}
		}
	}

	public void listaDown() {
		if (listaEjercicios.isEnabled()) {
			try {
				listaEjercicios.setSelectedIndex(listaEjercicios.getSelectedIndex() + 1);
			} catch (IndexOutOfBoundsException e) {
				System.out.println("La opcion elegida no es la correcta");
			}
		}
	}

	public Icon escalarImagen(ImageIcon imagen) {
		Icon icon = new ImageIcon(imagen.getImage().getScaledInstance(pantallaImagen.getWidth(),
				pantallaImagen.getHeight(), Image.SCALE_DEFAULT));
		return icon;
	}

}

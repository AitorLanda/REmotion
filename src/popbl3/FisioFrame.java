package popbl3;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FisioFrame extends JFrame implements ListSelectionListener {

	private final int PACIENTE = 1;

	private Fisio fisio;

	private JList<Usuario> listaPacientes;
	private ModeloListaUsuario modeloLista;
	private UsuarioRenderer cellRendererUsuario;

	private MiAction accAnadir;
	private MiAction accEliminar;
	private MiAction accEditar;
	private MiAction accGestionarEjercicios;
	private MiAction accLogOut;

	private ModeloTablaEjercicio modeloTabla;
	private JScrollPane scrollPaneTabla;

	public FisioFrame(Fisio fisio) {
		this.setTitle(
				fisio.getNombre() + " " + fisio.getApellido1() + " " + fisio.getApellido2() + " (Fisioterapeuta)");

		this.fisio = fisio;
		this.crearAcciones();
		this.setSize(800, 600);
		this.setLocation(100, 100);
		this.setJMenuBar(crearBarraMenu());
		this.getContentPane().add(crearToolBar(), BorderLayout.NORTH);
		this.getContentPane().add(crearPanelCentro(), BorderLayout.CENTER);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		// this.setResizable(false);
		this.setVisible(true);
	}

	private void crearAcciones() {
		accAnadir = new MiAction(this, "Anadir usuario",
				new ImageIcon(this.getClass().getResource(AdminFrame.ICONO_ANADIR)), "Anadir un paciente",
				KeyEvent.VK_A);
		accEditar = new MiAction(this, "Editar", new ImageIcon(this.getClass().getResource(AdminFrame.ICONO_EDITAR)),
				"Editar un paciente", KeyEvent.VK_M);
		accEliminar = new MiAction(this, "Eliminar",
				new ImageIcon(this.getClass().getResource(AdminFrame.ICONO_ELIMINAR)), "Eliminar un paciente",
				KeyEvent.VK_E);
		accGestionarEjercicios = new MiAction(this, "Gestionar Ejercicios",
				new ImageIcon(this.getClass().getResource(AdminFrame.ICONO_ANADIRSESION)), "Gestionar ejercicios de un paciente",
				KeyEvent.VK_G);
		accLogOut = new MiAction(this, "Log out", new ImageIcon(this.getClass().getResource(AdminFrame.ICONO_LOGOUT)),
				"Cerrar la sesión actual", KeyEvent.VK_O);
	}

	private JMenuBar crearBarraMenu() {
		JMenuBar barra = new JMenuBar();
		JMenu administrar = new JMenu("Administrar");
		JMenu salir = new JMenu("Salir");
		
		administrar.add(accAnadir);
		administrar.addSeparator();
		administrar.add(accEditar);
		administrar.addSeparator();
		administrar.addSeparator();
		administrar.add(accGestionarEjercicios);
		administrar.add(accEliminar);

		accEditar.setEnabled(false);
		accGestionarEjercicios.setEnabled(false);
		accEliminar.setEnabled(false);

		barra.add(administrar);
		barra.add(Box.createHorizontalGlue());

		salir.add(accLogOut);
		barra.add(salir);

		return barra;
	}

	private Component crearToolBar() {
		JToolBar toolBar = new JToolBar();
		Dimension separador = new Dimension(40, 0);

		toolBar.setFloatable(false);

		toolBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		toolBar.add(new JButton(accAnadir));
		toolBar.addSeparator(separador);
		toolBar.add(new JButton(accEditar));
		toolBar.addSeparator(separador);
		toolBar.add(new JButton(accGestionarEjercicios));
		toolBar.addSeparator(separador);
		toolBar.add(new JButton(accEliminar));

		toolBar.add(Box.createHorizontalGlue());
		
		toolBar.add(new JButton(accLogOut));

		return toolBar;
	}

	private Component crearPanelCentro() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));

		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		panel.add(crearPanelListaUsuarios(), BorderLayout.WEST);
		panel.add(crearPanelTablaEjercicios(), BorderLayout.CENTER);

		return panel;
	}

	private Component crearPanelListaUsuarios() {
		JScrollPane panelScroll = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panelScroll.setPreferredSize(new Dimension(200, 0));

		cellRendererUsuario = new UsuarioRenderer();
		modeloLista = new ModeloListaUsuario(PACIENTE, this.fisio);
		listaPacientes = new JList<Usuario>();
		listaPacientes.setModel(modeloLista);
		listaPacientes.setCellRenderer(cellRendererUsuario);
		listaPacientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaPacientes.addListSelectionListener(this);
		panelScroll.setViewportView(listaPacientes);

		return panelScroll;
	}

	private Component crearPanelTablaEjercicios() {
		scrollPaneTabla = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// modeloColumnasTabla = new ();

		return scrollPaneTabla;
	}

	private void anade() {
		int tipoUsuarioPaciente = 1;
		Paciente nuevoPaciente = null;
		DialogoUsuario dialogoPaciente = new DialogoUsuario(this, nuevoPaciente, tipoUsuarioPaciente, fisio);
		nuevoPaciente = (Paciente) dialogoPaciente.getUsuario();
		if (nuevoPaciente != null)
			modeloLista.addElement(nuevoPaciente);
	}

	private void gestiona() {
		VentanaSesion ventana = new VentanaSesion(this, (Paciente) listaPacientes.getSelectedValue());
		Paciente paciente = (Paciente) listaPacientes.getSelectedValue();
		paciente.setEjercicios(ventana.getEjercicios());
		if (ventana.getEjercicios() != null) {
			GestorSockets.modificarDatosFichero(paciente, paciente.getUserName());
			crearTabla(scrollPaneTabla, (Paciente) listaPacientes.getSelectedValue(), modeloTabla);
		}
	}

	private void modifica() {
		int tipoUsuarioPaciente = 1;
		Paciente pacienteNuevo;

		DialogoUsuario dialogoPaciente = new DialogoUsuario(this, listaPacientes.getSelectedValue(),
				tipoUsuarioPaciente, fisio);
		pacienteNuevo = (Paciente) dialogoPaciente.getUsuario();
		if (pacienteNuevo != null) {
			ArrayList<Ejercicio> e;
			modeloLista.setElementAt(pacienteNuevo, listaPacientes.getSelectedIndex());
			e = pacienteNuevo.getEjercicios();
			try {
				modeloTabla.insertarListaEjercicios(null);
			} catch (Exception e1) {

			}
			this.repaint();
		}
	}

	private void elimina() {

		int reply = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar al paciente?",
				"Eliminar paciente", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (reply == JOptionPane.YES_OPTION) {
			GestorSockets.eliminarUsuarioDelFichero(listaPacientes.getSelectedValue());
			GestorSockets.eliminarDatosLoginDelUsuario(listaPacientes.getSelectedValue());
			modeloLista.remove(listaPacientes.getSelectedIndex());

			modeloTabla.eliminarListaEjercicios();
			scrollPaneTabla.setViewportView(null);
			accEditar.setEnabled(false);
			accGestionarEjercicios.setEnabled(false);
			accEliminar.setEnabled(false);
		}
	}

	private void cerrarSesion() {
		MainFrame frame = new MainFrame();
		this.dispose();
	}

	private class MiAction extends AbstractAction {
		String texto;

		public MiAction(FisioFrame frame, String texto, Icon imagen, String descrip, Integer nemonic) {
			super(texto, imagen);
			this.texto = texto;
			this.putValue(Action.SHORT_DESCRIPTION, descrip);
			this.putValue(Action.MNEMONIC_KEY, nemonic);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			switch (texto) {
			case "Anadir usuario":
				anade();
				break;
			case "Editar":
				modifica();
				break;
			case "Gestionar Ejercicios":
				gestiona();
				break;
			case "Eliminar":
				elimina();
				break;
			case "Log out":
				cerrarSesion();
				break;
			default:
				JOptionPane.showMessageDialog(FisioFrame.this, "Error en el switch case", "Error",
						JOptionPane.ERROR_MESSAGE);
				break;

			}
		}
	}

	public static void crearTabla(JScrollPane scrollPaneTabla, Paciente paciente,
			ModeloTablaEjercicio modeloTablaExterno) {
		Visualizacion vista = new Visualizacion(paciente);
		scrollPaneTabla.setViewportView(vista.crearPanelPestanas());
	}

	@Override
	public void valueChanged(ListSelectionEvent evento) {
		if (!evento.getValueIsAdjusting()) {
			accEditar.setEnabled(true);
			accGestionarEjercicios.setEnabled(true);
			accEliminar.setEnabled(true);

			if (listaPacientes.getSelectedValue() != null) {
				modeloTabla = new ModeloTablaEjercicio(new ModeloColumnasTablaEjercicios(),
						(Paciente) listaPacientes.getSelectedValue(), Ejercicio.fechaDelSistema());
				crearTabla(scrollPaneTabla, (Paciente) listaPacientes.getSelectedValue(), modeloTabla);
			}
		}

	}

}

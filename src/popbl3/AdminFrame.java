package popbl3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

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
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class AdminFrame extends JFrame implements ListSelectionListener, ChangeListener {
	
	
	private static final int ANCHO_PANTALLA = 650;
	private static final int LARGO_PANTALLA = 650;
	private static final int POS_X_PANTALLA = 50;
	private static final int POS_Y_PANTALLA = 25;
	
	private static final int FISIOTERAPEUTA = 0;
	private static final int PACIENTE = 1;
	private static final int ADMINISTRADOR = 2;
	
	
	public static final String ICONO_ANADIR = "/iconos/anadir.png";
	public static final String ICONO_EDITAR = "/iconos/editar.png";
	public static final String ICONO_ELIMINAR = "/iconos/eliminar.png";
	public static final String ICONO_ANADIRSESION = "/iconos/addSesion.jpeg";
	public static final String ICONO_LOGOUT = "/iconos/logOut.png";
	public static final String STRING_ANADIR_USUARIO = "Anadir un usuario";
	public static final String STRING_EDITAR_USUARIO = "Editar un usuario";
	public static final String STRING_ELIMINAR_USUARIO ="Eliminar un usuario";
	public static final String STRING_CERRAR_SESION = "Cerrar la sesión actual";
	public static final String ICONO_FISIO = "/iconos/fisio.png";
	public static final String ICONO_PACIENTE = "/iconos/paciente.png";
	public static final String ICONO_ADMIN = "/iconos/admin.png";
	public static final String STRING_ANADIR = "Anadir";
	public static final String STRING_EDITAR = "Editar";
	public static final String STRING_ELIMINAR = "Eliminar";
	public static final String STRING_LOG_OUT = "Log out";
	public static final String STRING_SALIR = "Salir";
	
	private MiAction accAnadir;
	private MiAction accEliminar;
	private MiAction accEditar;
	private MiAction accLogOut;
	
	private JTabbedPane pestanas;
	private UsuarioRenderer cellRendererUsuario;	
	private ModeloListaUsuario modeloFisios;
	private JList<Usuario> listaFisios;
	private ModeloListaUsuario modeloPaciente;
	private JList<Usuario> listaPacientes;
	private ModeloListaUsuario modeloAdmin;
	private JList<Usuario> listaAdministradores;
	private Logger logger;
	private JPanel panelDatos;
	
	public AdminFrame(Administrador admin){
		super (admin.getNombre()+" "+admin.getApellido1()+" "+admin.getApellido2()+" (Admin)");
		
		this.crearAcciones();
		this.setSize(ANCHO_PANTALLA,LARGO_PANTALLA);
		this.setLocation(POS_X_PANTALLA,POS_Y_PANTALLA);
		this.setJMenuBar(crearBarraMenu());
		this.getContentPane().add(crearToolBar(),BorderLayout.NORTH);
		this.getContentPane().add(crearPanelVentana(),BorderLayout.CENTER);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
	}

	private void crearAcciones() {
		accAnadir = new MiAction(STRING_ANADIR, new ImageIcon(this.getClass().getResource(ICONO_ANADIR)), STRING_ANADIR_USUARIO, KeyEvent.VK_A);
		accEditar = new MiAction(STRING_EDITAR, new ImageIcon(this.getClass().getResource(ICONO_EDITAR)),STRING_EDITAR_USUARIO, KeyEvent.VK_M);
		accEliminar = new MiAction(STRING_ELIMINAR, new ImageIcon(this.getClass().getResource(ICONO_ELIMINAR)), STRING_ELIMINAR_USUARIO, KeyEvent.VK_E);
		accLogOut = new MiAction(STRING_LOG_OUT, new ImageIcon(this.getClass().getResource(ICONO_LOGOUT)), STRING_CERRAR_SESION, KeyEvent.VK_O);
	}
	
	private JMenuBar crearBarraMenu() {
		JMenuBar barra =  new JMenuBar();
		JMenu editar = new JMenu(STRING_EDITAR);
		JMenu salir = new JMenu(STRING_SALIR);
		
		editar.add(accAnadir);
		editar.addSeparator();
		editar.add(accEditar);
		editar.addSeparator();
		editar.add(accEliminar);
		
		accEditar.setEnabled(false);
		accEliminar.setEnabled(false);
		
		barra.add(editar);
		barra.add(Box.createHorizontalGlue());
		
		salir.add(accLogOut);
		barra.add (salir);
		
		return barra;	
	}
	
	private Component crearToolBar() {
		JToolBar toolBar = new JToolBar();
		Dimension separador = new Dimension(40,0);
		
		toolBar.setFloatable(false);
		
		toolBar.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
	
		toolBar.add(new JButton (accAnadir));
		toolBar.addSeparator(separador);
		toolBar.add(new JButton (accEditar));
		toolBar.addSeparator(separador);
		toolBar.add(new JButton (accEliminar));
		
		toolBar.add(Box.createHorizontalGlue());
		
		toolBar.add(new JButton (accLogOut));
		
		return toolBar;
	}


	private Component crearPanelVentana() {
		int distanciaBorder = 10;
		JPanel panel = new JPanel(new BorderLayout(distanciaBorder,distanciaBorder));
		
		panel.add(crearPanelListasUsuarios(), BorderLayout.WEST);
		panel.add(crearPanelDatos(), BorderLayout.CENTER);
		
		panel.setBorder(BorderFactory.createEmptyBorder(distanciaBorder,distanciaBorder,
														distanciaBorder,distanciaBorder));
		return panel;
	}

	private Component crearPanelListasUsuarios() {
		pestanas = new JTabbedPane();
		pestanas.setPreferredSize(new Dimension(349,0));
		
		this.inicializarModelosListasYRenderer();
		
		pestanas.addTab("Fisioterapeutas", new ImageIcon(this.getClass().getResource(ICONO_FISIO)),
						crearScrollUsuario(modeloFisios, listaFisios, cellRendererUsuario));
        
		pestanas.addTab("Pacientes", new ImageIcon(this.getClass().getResource(ICONO_PACIENTE)),
						crearScrollUsuario(modeloPaciente, listaPacientes, cellRendererUsuario));
        
		pestanas.addTab("Administradores", new ImageIcon(this.getClass().getResource(ICONO_ADMIN)),
						crearScrollUsuario(modeloAdmin, listaAdministradores, cellRendererUsuario));
        
        pestanas.addChangeListener(this);
        
		return pestanas;
	}
	
	private void inicializarModelosListasYRenderer() {
		cellRendererUsuario = new UsuarioRenderer();
		
		modeloFisios = new ModeloListaUsuario(FISIOTERAPEUTA);
		listaFisios = new JList<>();
		
		modeloPaciente = new ModeloListaUsuario(PACIENTE);
		listaPacientes = new JList<>();
		
		modeloAdmin = new ModeloListaUsuario(ADMINISTRADOR);
		listaAdministradores = new JList<>();
	}

	private Component crearScrollUsuario(ModeloListaUsuario modeloUsuario, JList<Usuario> listaUsuarios, UsuarioRenderer cellRendererUsuario) {
		JScrollPane panelScrollFisio = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		listaUsuarios.setModel(modeloUsuario);
		listaUsuarios.setCellRenderer(cellRendererUsuario);
		listaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaUsuarios.addListSelectionListener(this);
		panelScrollFisio.setViewportView(listaUsuarios);
		return panelScrollFisio;
	}
	
	private Component crearPanelDatos() {
		int columnas = 1;
		int filas = 10;
		int separacionX = 0;
		int separacionY = 10;
		int distanciaBorde = 31;
		int sinDistancia = 0;
		panelDatos = new JPanel(new GridLayout(filas,columnas,separacionX,separacionY));
		panelDatos.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(distanciaBorde,
																								sinDistancia,
																								sinDistancia,
																								sinDistancia),
							 BorderFactory.createLoweredBevelBorder()));
		return panelDatos;
	}

	

	private class MiAction extends AbstractAction {
		String texto;
		
		public MiAction (String texto, Icon imagen, String descrip, Integer nemonic){
			super(texto,imagen);
			this.texto = texto;
			this.putValue(Action.SHORT_DESCRIPTION ,descrip);
			this.putValue(Action.MNEMONIC_KEY, nemonic);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			switch(texto){
			case "Anadir":
				anadeUsuario();
				break;
				
			case "Editar":
				editaUsuario();
				break;
				
			case "Eliminar":
				eliminaUsuario();
				break;
				
			case "Log out":
				cerrarSesion();
				break;
				
			default:
				JOptionPane.showMessageDialog(AdminFrame.this, "Error en el switch case",
											  "Error", JOptionPane.ERROR_MESSAGE);
				break;	
			}
		}
	}
	
	private void anadeUsuario(){
		int tipoUsuario;
		
		tipoUsuario = pestanas.getSelectedIndex();
		
		switch (tipoUsuario) {
			case FISIOTERAPEUTA:
				Fisio nuevoFisio = null;
				DialogoUsuario dialogoFisio = new DialogoUsuario(this, nuevoFisio, tipoUsuario, null);
				nuevoFisio = (Fisio)dialogoFisio.getUsuario();
				if(nuevoFisio != null)
					modeloFisios.addElement(nuevoFisio);
				break;
				
			case PACIENTE:
				Paciente nuevoPaciente = null;
				DialogoUsuario dialogoPaciente = new DialogoUsuario(this, nuevoPaciente, tipoUsuario, null);
				nuevoPaciente = (Paciente)dialogoPaciente.getUsuario();
				if(nuevoPaciente != null)
					modeloPaciente.addElement(nuevoPaciente);
				break;
				
			case ADMINISTRADOR:
				Administrador nuevoAdmin = null;
				DialogoUsuario dialogoAdmin = new DialogoUsuario(this, nuevoAdmin, tipoUsuario, null);
				nuevoAdmin = (Administrador)dialogoAdmin.getUsuario();
				if(nuevoAdmin != null)
					modeloAdmin.addElement(nuevoAdmin);
				break;
				
			default:
				break;
		}
		
	}

	private void editaUsuario(){
		int tipoUsuario;
		
		tipoUsuario = pestanas.getSelectedIndex();
		
		switch (tipoUsuario) {
			case FISIOTERAPEUTA:
				Fisio fisioNuevo;
				DialogoUsuario dialogoFisio = new DialogoUsuario(this, listaFisios.getSelectedValue(), tipoUsuario, null);
				fisioNuevo = (Fisio) dialogoFisio.getUsuario();
				if (fisioNuevo != null) {
					modeloFisios.setElementAt(fisioNuevo, listaFisios.getSelectedIndex());
					crearLabelsDatos(fisioNuevo);
					this.repaint();
				}
				break;
				
			case PACIENTE:
				Paciente pacienteNuevo;
				DialogoUsuario dialogoPaciente = new DialogoUsuario(this, listaPacientes.getSelectedValue(), tipoUsuario, null);
				pacienteNuevo = (Paciente) dialogoPaciente.getUsuario();
				if (pacienteNuevo != null) {
					modeloPaciente.setElementAt(pacienteNuevo, listaPacientes.getSelectedIndex());
					crearLabelsDatos(pacienteNuevo);
					this.repaint();
				}
				break;
				
			case ADMINISTRADOR:
				Administrador adminNuevo;
				DialogoUsuario dialogoAdmin = new DialogoUsuario(this, listaAdministradores.getSelectedValue(), tipoUsuario, null);
				adminNuevo = (Administrador) dialogoAdmin.getUsuario();
				if (adminNuevo != null) {
					modeloAdmin.setElementAt(adminNuevo, listaAdministradores.getSelectedIndex());
					crearLabelsDatos(adminNuevo);
					this.repaint();
				}
				break;
				
			default:
				break;
		}
	}

	private void eliminaUsuario(){
		int resultado = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar el usuario?",
															"Eliminar usuario", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		
		if (resultado == JOptionPane.YES_OPTION){
			int tipoUsuario = pestanas.getSelectedIndex();
			
			switch(tipoUsuario){
				case FISIOTERAPEUTA:
					GestorSockets.eliminarUsuarioDelFichero(listaFisios.getSelectedValue());
					GestorSockets.eliminarDatosLoginDelUsuario(listaFisios.getSelectedValue());
					modeloFisios.remove(listaFisios.getSelectedIndex());	
					break;
				case PACIENTE:
					GestorSockets.eliminarUsuarioDelFichero(listaPacientes.getSelectedValue());
					GestorSockets.eliminarDatosLoginDelUsuario(listaPacientes.getSelectedValue());
					modeloPaciente.remove(listaPacientes.getSelectedIndex());		
					break;
				case ADMINISTRADOR:
					GestorSockets.eliminarUsuarioDelFichero(listaAdministradores.getSelectedValue());
					GestorSockets.eliminarDatosLoginDelUsuario(listaAdministradores.getSelectedValue());
					modeloAdmin.remove(listaAdministradores.getSelectedIndex());
					break;
				default:
					break;
			}
			
			estadoAcciones(false);
		}
	}

	private void cerrarSesion(){
		MainFrame frame = new MainFrame();
		AdminFrame.this.dispose();
	}
	
	private void estadoAcciones(boolean estado){
		accEditar.setEnabled(estado);
		accEliminar.setEnabled(estado);
	}
	
	private void crearLabelsDatos(Usuario usuario) {
		
		limpiarPanelDatos();
		
		if(usuario != null){
			
			crearLabel("Nombre", usuario.getNombre());
			crearLabel("Apellido1", usuario.getApellido1());
			crearLabel("Apellido2", usuario.getApellido2());
			crearLabel("Username", usuario.getUserName());
			crearLabel("Centro", usuario.getCentro());
			
			if(usuario instanceof Paciente){
				Paciente p = (Paciente) usuario;
				crearLabel("Tipo de lesión", p.getTipoLesion());
			}
		}		
	}

	private void crearLabel(String tituloBorder, String datoMostrado) {
		JLabel label = new JLabel();
		
		panelDatos.add(label);
		label.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), tituloBorder));
		label.setText(datoMostrado);
	}

	private void limpiarPanelDatos(){
		panelDatos.removeAll();
		panelDatos.repaint();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		
		if (!e.getValueIsAdjusting()) {
			
			estadoAcciones(true);
			int numPestana = pestanas.getSelectedIndex();
			
			switch(numPestana){
				case FISIOTERAPEUTA:
					crearLabelsDatos(listaFisios.getSelectedValue());
					break;
				case PACIENTE:
					crearLabelsDatos(listaPacientes.getSelectedValue());
					break;
				case ADMINISTRADOR:
					crearLabelsDatos(listaAdministradores.getSelectedValue());
					if(modeloAdmin.getSize()<= 1)
						accEliminar.setEnabled(false);
					break;
				default:
					System.out.println("Ventana equivocada");
					break;
			}
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		listaAdministradores.clearSelection();
		listaPacientes.clearSelection();
		listaFisios.clearSelection();
		estadoAcciones(false);
	}	
}

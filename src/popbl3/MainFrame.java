package popbl3;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.button.StandardButtonShaper;

public class MainFrame implements ActionListener {
	
	private final String IMAGEN_LOGIN = "/imagenes/mano.jpg";
	public final String IMAGEN_VENTANA = "/imagenes/icono_prigmotion.png";
	private final String FICHERO_SOCKET = "/files/configuracionSockets.txt";
	private final String PROPERTY_DIRECCION_IP = "DireccionIP";
	private final String PROPERTY_PUERTO = "Puerto";
	private final String TITLE_IDENTIFICACION = "Identificacion";
	private final String STRING_LOGING = "Login";
	private final String STRING_USERNAME = "Username:";
	private final String STRING_PASSWORD = "Password :";
	private final String STRING_ERROR = "Error";
	private final String STRING_ERROR_CARGAR_USUARIO = "Error al cargar datos de usuario. Contacte con el administrador.";
	private final String STRING_ERROR_DATOS_INCORRECTOS = "Usuario o contraseña incorrectos.";
	private final String STRING_ERROR_FALTAN_DATOS = "Rellene todos los campos.";
	private final String NOMBRE_VENTANA_LOGING = "Control de acceso";
	private JFrame ventana;
	private JButton bLogin;
	private JTextField textoUsuario;
	private JPasswordField textoPass;
	
	public MainFrame() {
		ventana = new JFrame(NOMBRE_VENTANA_LOGING);
		ventana.setSize(400, 260);
		ventana.setLocation(100, 100);
		ventana.setContentPane(crearPanelVentana());
		ventana.setResizable(false);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana.setVisible(true);
		this.cargarConfiguracionSockets();
	}

	private void cargarConfiguracionSockets() {
	
		Properties properties = new Properties();
		InputStream in = null;
		String direccion;
		int puerto;

		try {
			
			in = new FileInputStream(new File(this.getClass().getResource(FICHERO_SOCKET).toURI()));
			properties.load(in);

			direccion = properties.getProperty(PROPERTY_DIRECCION_IP);
			puerto = Integer.valueOf(properties.getProperty(PROPERTY_PUERTO));

			GestorSockets.cargarConfiguracion(direccion, puerto);
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(ventana,
					"ERROR FICHERO", STRING_ERROR,
					JOptionPane.ERROR_MESSAGE);
			System.out.println(e.toString());
		} catch (URISyntaxException e) {
			JOptionPane.showMessageDialog(ventana,
					"ERROR URI", STRING_ERROR,
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	private Container crearPanelVentana() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(40, 20, 20, 20));
		panel.setLayout(null);
		panel.add(crearPanelTextos());
		panel.add(crearPanelBotones());

		JLabel lblImagen = new JLabel("");	
		
		
		lblImagen.setIcon(new ImageIcon(MainFrame.class.getResource(IMAGEN_LOGIN)));

		lblImagen.setBounds(0, 0, 394, 231);
		panel.add(lblImagen);

		return panel;
	}

	private Component crearPanelTextos() {
		JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
		panel.setOpaque(false);
		panel.setBounds(70, 40, 254, 98);
		textoUsuario = new JTextField();
		textoPass = new JPasswordField();
		panel.setBorder(BorderFactory.createTitledBorder(null, TITLE_IDENTIFICACION, TitledBorder.CENTER, 0,
				new Font("Arial", Font.PLAIN, 20),Color.BLACK.darker()));

		textoUsuario.addActionListener(this);
		textoUsuario.setActionCommand(STRING_LOGING);
		textoPass.addActionListener(this);
		textoPass.setActionCommand(STRING_LOGING);

		JLabel username = new JLabel(STRING_USERNAME);
		JLabel password = new JLabel(STRING_PASSWORD);

		username.setOpaque(true);
		username.setHorizontalAlignment(JLabel.CENTER);
		username.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		password.setOpaque(true);
		password.setHorizontalAlignment(JLabel.CENTER);
		password.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		panel.add(username);
		panel.add(textoUsuario);
		panel.add(password);
		panel.add(textoPass);

		return panel;
	}

	private Component crearPanelBotones() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setOpaque(false);
		panel.setBounds(70, 176, 254, 33);
		panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

		bLogin = new JButton(STRING_LOGING);
		bLogin.addActionListener(this);
		bLogin.setActionCommand(STRING_LOGING);
		bLogin.putClientProperty(SubstanceLookAndFeel.BUTTON_SHAPER_PROPERTY, new StandardButtonShaper());
		panel.add(bLogin);

		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String username;
		String password;

		if (e.getActionCommand().equals(STRING_LOGING)) {
			username = textoUsuario.getText();
			password = String.valueOf(textoPass.getPassword());
			if (!username.isEmpty() && !password.isEmpty()){
				if (GestorSockets.verificarLogin(username, password)) {
					Usuario user;

					if ((user = GestorSockets.cargarUsuario(username)) != null) {
						if (user instanceof Paciente) {
							EventQueue.invokeLater(new Runnable() {
								public void run() {
									try {
										PacienteFrame frame = new PacienteFrame((Paciente) user);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});

						}
						if (user instanceof Fisio) {
							FisioFrame frame = new FisioFrame((Fisio) user);
							frame.setVisible(true);
						}
						if (user instanceof Administrador) {
							AdminFrame frame = new AdminFrame((Administrador) user);
							frame.setVisible(true);
						}

						ventana.dispose();

					} else {
						JOptionPane.showMessageDialog(ventana,
								STRING_ERROR_CARGAR_USUARIO, STRING_ERROR,
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(ventana, STRING_ERROR_DATOS_INCORRECTOS, STRING_ERROR,
							JOptionPane.ERROR_MESSAGE);
					textoUsuario.setText(null);
					textoPass.setText(null);
					textoUsuario.requestFocus();
				}
			}
			else{
				JOptionPane.showMessageDialog(ventana, STRING_ERROR_FALTAN_DATOS, STRING_ERROR,
						JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}

	public static void main(String[] args) {
		String skin = "org.jvnet.substance.skin.BusinessBlackSteelSkin";
		String marca = "org.jvnet.substance.watermark.SubstanceFabricWatermark";
		JFrame.setDefaultLookAndFeelDecorated(true);
		SubstanceLookAndFeel.setSkin(skin);
		SubstanceLookAndFeel.setCurrentWatermark(marca);
		MainFrame mainframe = new MainFrame();

	}
}

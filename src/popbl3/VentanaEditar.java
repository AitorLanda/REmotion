package popbl3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.text.DateFormatter;

import com.toedter.calendar.JDateChooser;
/**
 * This is a class which allows to the physiotherapist user to edit the dates and rehabilitation exercises with a patient. 
 * VentanaEditar extends JDialog so it inherit some attributes and methods from the parent. 
 * It also implements ActionListener interfaces which is used to interact with the differents buttons. 
 * 
 * 
 * @author Aitor Landa
 * @author Ane Sajeras
 * @version %I%, %G%
 * @since 1.0
 * */
public class VentanaEditar extends JDialog implements ActionListener {

	private JPanel pVentana, pBotonA, pSuperior, pSuperior1, pSuperior2, pCombo;
	private JDateChooser dateChooser;
	private JLabel user;
	private JButton bOk, bCancel;
	private final String ICONO_OK = "/iconos/ok.png";
	private final String ICONO_CANCEL = "/iconos/cancel.png";
	private Paciente paciente;
	JComboBox<String> cEjercicios;
	JList<EjercicioSesion> lista;
	Modelo modelo;
	JSpinner repeticiones, tiempo;
	Date date, date2;
	Calendar calendar;
	JSpinner.DateEditor editor;
	ArrayList<Ejercicio> ejercicios;
	private int index;

	EjercicioSesion newEjercicioSesion = null;
/**
 * This is the constructor that requires some parameters to create a VentanaEditar object.
 * It creates a object which is based in JDialog, so it creates a new Dialog which blocks others interfaces that are below it.
 * @param dialog the dialog who is going to be below VentanaEditar object, this VentanaSesion JDialog is going to be blocked until the VentanaEditar is open.
 * @param ejercicioSesion the exercise which is wanted to be edited.
 * @param paciente the physiotherapist patient.
 * @param index the position number of the exercise which is will be edited in the exercises list.
 * @param modelo a JList custom model.
 */
	public VentanaEditar(VentanaSesion dialog, EjercicioSesion ejercicioSesion, Paciente paciente, int index,
			Modelo modelo) {
		super(dialog, true);
		this.setTitle("Editar Sesión");
		this.modelo = modelo;
		this.index = index;
		this.paciente = paciente;
		this.setSize(800, 300);
		this.setLocation(100, 100);
		this.getContentPane().add(crearPanelVentana(ejercicioSesion), BorderLayout.CENTER);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
/**
 * This method creates the main panel. It will have inside others panels with different components.
 * @param ejercicioSesion the ejercicioSesion is the exercise which is wanted to be edited.
 * @return pVentana the JPanel which has inside other panel, it will be added to the ContentPane.
 */
	private Component crearPanelVentana(EjercicioSesion ejercicioSesion) {
		pVentana = new JPanel(new BorderLayout());
		pVentana.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		pVentana.add(crearPanelSuperior(ejercicioSesion), BorderLayout.NORTH);
		pVentana.add(crearPanelBotones(), BorderLayout.SOUTH);
		return pVentana;
	}
/**
 * This method returns the top panel of the main panel. Besides, it creates two sub-panels inside it.
 * @param ejercicioSesion the ejercicioSesion is the exercise which is wanted to be edited.
 * @return pSuperior the top panel of the main panel.
 */
	private JPanel crearPanelSuperior(EjercicioSesion ejercicioSesion) {
		pSuperior = new JPanel(new GridLayout(2, 1));

		pSuperior.add(crearPanelSuperior1(ejercicioSesion));
		pSuperior.add(crearPanelSuperior2(ejercicioSesion));

		return pSuperior;

	}
/**
 * This method creates a JPanel which has the user name, and the exercise date.
 * @param ejercicioSesion the ejercicioSesion is the exercise which is wanted to be edited.
 * @return pSuperior1 the fist panel of the top panel. 
 */
	private JPanel crearPanelSuperior1(EjercicioSesion ejercicioSesion) {
		pSuperior1 = new JPanel(new GridLayout(1, 1));
		user = new JLabel();
		user.setText(ejercicioSesion.getNombre() + " " + ejercicioSesion.getApellido1() + " "
				+ ejercicioSesion.getApellido2());
		user.setHorizontalAlignment(JLabel.HORIZONTAL);
		user.setHorizontalTextPosition(JLabel.CENTER);
		user.setBorder(BorderFactory.createTitledBorder("Paciente"));

		pSuperior1.add(user);
		SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
		String strFecha = ejercicioSesion.getFecha();
		try {

			date = formatoDelTexto.parse(strFecha);

		} catch (ParseException ex) {

			ex.printStackTrace();

		}

		dateChooser = new JDateChooser();
		dateChooser.setDateFormatString("dd/MM/yyyy");
		dateChooser.setDate(date);
		dateChooser.setBorder(new TitledBorder("Fecha"));
		pSuperior1.add(dateChooser);

		return pSuperior1;
	}
/**
 * This method creates a JPanel which has the repetitions number of the exercises.
 * @param ejercicioSesion the ejercicioSesion is the exercise which is wanted to be edited.
 * @return pSuperior2 the second panel of the top panel.
 */
	private JPanel crearPanelSuperior2(EjercicioSesion ejercicioSesion) {
		pSuperior2 = new JPanel(new GridLayout(1, 1));
		pSuperior2.add(crearText(crearPanelCombo(ejercicioSesion), "Ejercicios"));

		SpinnerModel value = new SpinnerNumberModel(0, 0, 10, 1);
		repeticiones = new JSpinner(value);
		repeticiones.setBounds(100, 100, 50, 30);

		String repeticionesS = ejercicioSesion.getRepeticiones();
		int repeticionesI = Integer.parseInt(repeticionesS);

		SpinnerNumberModel model = new SpinnerNumberModel(repeticionesI, 1, 100, 1);
		repeticiones.setModel(model);

		pSuperior2.add(crearText(repeticiones, "Repeticiones"));

		pSuperior2.add(crearText(time(ejercicioSesion), "Tiempo (mm:ss)"));
		return pSuperior2;
	}
/**
 * This method creates a panel with a titled border.
 * @param component a element to be decorated with a border.
 * @param titulo the border title.
 * @return panel a JPanel with border.
 */
	private Component crearText(Component component, String titulo) {
		JPanel panel = new JPanel(new GridLayout(1, 1));
		panel.add(component);
		panel.setBorder(
				new TitledBorder(null, titulo, TitledBorder.LEFT, TitledBorder.ABOVE_TOP, null, new Color(0, 0, 0)));
		return panel;
	}
/**
 * This method creates a combobox with all the available exercises.
 * @param ejercicioSesion the ejercicioSesion is the exercise which is wanted to be edited.
 * @return pCombo the resulting combobox.
 */
	private Component crearPanelCombo(EjercicioSesion ejercicioSesion) {

		cEjercicios = new JComboBox<String>();

		cEjercicios.setModel(new DefaultComboBoxModel<String>(GestorSockets.cargarEjercicios()));

		pCombo = new JPanel(new FlowLayout());
		pCombo.add(cEjercicios);
		cEjercicios.setSelectedItem(ejercicioSesion.getEjercicios());

		return pCombo;
	}
/**
 * This method gets the time which a exercise has defined to be done. It generate a spinner to change the time value.
 * @param ejercicioSesion the ejercicioSesion is the exercise which is wanted to be edited.
 * @return content a JPanel with a time spinner.
 */
	public Component time(EjercicioSesion ejercicioSesion) {
		calendar = Calendar.getInstance();

		SimpleDateFormat formatoDelTiempo2 = new SimpleDateFormat("mm:ss");
		String strTiempo2 = ejercicioSesion.getTiempo();
		try {

			date2 = formatoDelTiempo2.parse(strTiempo2);

		} catch (ParseException ex) {

			ex.printStackTrace();

		}

		calendar.setTime(date2);

		SpinnerDateModel model = new SpinnerDateModel();
		model.setValue(calendar.getTime());
		tiempo = new JSpinner(model);

		editor = new JSpinner.DateEditor(tiempo, "mm:ss");
		DateFormatter formatter = (DateFormatter) editor.getTextField().getFormatter();
		formatter.setAllowsInvalid(false);
		formatter.setOverwriteMode(true);
		tiempo.setEditor(editor);
		JPanel content = new JPanel();
		return content.add(tiempo);

	}
/**
 * This method creates a panel with two buttons, "Ok" button and "Cancel" button.
 * @return pBotonA a panel with two buttons.
 */
	public Component crearPanelBotones() {
		pBotonA = new JPanel(new FlowLayout());
		pBotonA.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		bOk = new JButton("Ok");
		bOk.setIcon(new ImageIcon(this.getClass().getResource(ICONO_OK)));
		bOk.addActionListener(this);
		bOk.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		pBotonA.add(bOk);

		bCancel = new JButton("Cancel");
		bCancel.setIcon(new ImageIcon(this.getClass().getResource(ICONO_CANCEL)));
		bCancel.addActionListener(this);
		bCancel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		pBotonA.add(bCancel);

		return pBotonA;
	}
/**
 * This method returns the changed exercise.
 * @return newEjercicioSesion a EjercicioSesion object, a exercise which has been changed.
 */
	EjercicioSesion getEjercicioSesion() {
		return newEjercicioSesion;
	}
/**
 * This method gets the changed exercises list.
 * @return lista a JList with changed exercises.
 */
	public JList<EjercicioSesion> getLista() {
		return lista;
	}
/**
 * This method gets actual date.
 * @return fecha string with actual date
 */
	public String conseguirFecha() {
		String fecha = dateChooser.getCalendar().get(java.util.Calendar.DAY_OF_MONTH) + "/"
				+ (dateChooser.getCalendar().get(java.util.Calendar.MONTH) + 1) + "/"
				+ (dateChooser.getCalendar().get(java.util.Calendar.YEAR));
		return fecha;
	}
/**
 * This method overrides the actionPerformed method to define the buttons behaviour.
 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Cancel")) {
			dispose();
		}
		if (e.getActionCommand().equals("Ok")) {
			newEjercicioSesion = new EjercicioSesion(paciente.getNombre(), paciente.getApellido1(),
					paciente.getApellido2(), conseguirFecha(), (String) cEjercicios.getSelectedItem(),
					repeticiones.getValue().toString(), editor.getFormat().format(tiempo.getValue()));
			if (newEjercicioSesion != null) {
				modelo.add(index, newEjercicioSesion);
			}
			dispose();
		}

	}
}

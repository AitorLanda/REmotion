package popbl3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DateFormatter;

import com.toedter.calendar.JDateChooser;

public class VentanaSesion extends JDialog implements ActionListener, ItemListener, ListSelectionListener {

	private JPanel pVentana, pBotonA, pBotonS, pMedio, pSuperior, pSuperior1, pSuperior2, pCombo;
	private JDateChooser dateChooser;
	private JLabel user;
	private JButton bAdd, bEdit, bRemove, bSave, bCancel;
	private final String ICONO_ADD = "/iconos/add.png";
	private final String ICONO_EDIT = "/iconos/edit.png";
	private final String ICONO_REMOVE = "/iconos/remove.png";
	private final String ICONO_SAVE = "/iconos/save.png";
	private final String ICONO_CANCEL = "/iconos/cancel.png";
	private Paciente paciente;
	JComboBox<String> cEjercicios;
	JList<EjercicioSesion> lista;
	ArrayList<Ejercicio> ejercicios;
	Modelo modelo;
	Modelo modeloCopia;
	JSpinner repeticiones, tiempo;
	Date date;
	Calendar calendar;
	JSpinner.DateEditor editor;
	DateFormatter formatter;
	int index;

	EjercicioSesion newEjercicioSesion = null;

	public VentanaSesion(FisioFrame frame, Paciente paciente) {
		super(frame, true);
		this.setTitle("Gestionar Ejercicios");
		modelo = new Modelo();
		modeloCopia = new Modelo();
		this.paciente = paciente;
		this.setSize(800, 600);
		this.setLocation(200, 100);
		this.getContentPane().add(crearPanelVentana(), BorderLayout.CENTER);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

	private Component crearPanelVentana() {
		pVentana = new JPanel(new BorderLayout());
		pVentana.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		pVentana.add(crearPanelSuperior(), BorderLayout.NORTH);
		pVentana.add(crearPanelMedio(), BorderLayout.CENTER);
		pVentana.add(crearPanelBotones2(), BorderLayout.SOUTH);
		return pVentana;
	}

	private Component crearPanelSuperior() {
		pSuperior = new JPanel(new GridLayout(2, 1));

		pSuperior.add(crearPanelSuperior1());
		pSuperior.add(crearPanelSuperior2());

		return pSuperior;

	}

	private Component crearPanelSuperior1() {
		pSuperior1 = new JPanel(new GridLayout(1, 1));
		user = new JLabel();
		user.setText(paciente.getNombre() + " " + paciente.getApellido1() + " " + paciente.getApellido2());
		user.setHorizontalAlignment(JLabel.HORIZONTAL);
		user.setBorder(BorderFactory.createTitledBorder("Paciente"));
		pSuperior1.add(user);
		dateChooser = new JDateChooser();
		dateChooser.setDateFormatString("dd/MM/yyyy");
		dateChooser.setBorder(new TitledBorder("Fecha"));
		dateChooser.setMinSelectableDate(new Date());
		pSuperior1.add(dateChooser);

		return pSuperior1;
	}

	private Component crearPanelSuperior2() {
		pSuperior2 = new JPanel(new GridLayout(1, 1));
		pSuperior2.add(crearText(crearPanelCombo(), "Ejercicios"));

		SpinnerModel value = new SpinnerNumberModel(0, 0, 10, 1);
		repeticiones = new JSpinner(value);
		repeticiones.setBounds(100, 100, 50, 30);
		SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 100, 1);
		repeticiones.setModel(model);
		pSuperior2.add(crearText(repeticiones, "Repeticiones"));

		pSuperior2.add(crearText(time(), "Tiempo (mm:ss)"));
		return pSuperior2;
	}

	public Component time() {

		calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, 1);
		calendar.set(Calendar.SECOND, 0);
		SpinnerDateModel model = new SpinnerDateModel();
		model.setValue(calendar.getTime());
		tiempo = new JSpinner(model);
		editor = new JSpinner.DateEditor(tiempo, "mm:ss");
		formatter = (DateFormatter) editor.getTextField().getFormatter();

		formatter.setAllowsInvalid(false);
		formatter.setOverwriteMode(true);
		tiempo.setEditor(editor);

		JPanel content = new JPanel();
		return content.add(tiempo);

	}

	private Component crearText(Component component, String titulo) {
		JPanel panel = new JPanel(new GridLayout(1, 1));
		panel.add(component);
		panel.setBorder(
				new TitledBorder(null, titulo, TitledBorder.LEFT, TitledBorder.ABOVE_TOP, null, new Color(0, 0, 0)));
		return panel;
	}

	private Component crearPanelCombo() {

		cEjercicios = new JComboBox<String>();

		cEjercicios.setModel(new DefaultComboBoxModel<String>(GestorSockets.cargarEjercicios()));

		pCombo = new JPanel(new FlowLayout());
		pCombo.add(cEjercicios);

		return pCombo;
	}

	private JPanel crearPanelMedio() {
		pMedio = new JPanel(new BorderLayout());
		pMedio.add(crearPanelBotones(), BorderLayout.NORTH);
		pMedio.add(crearPanelCentral(), BorderLayout.CENTER);
		return pMedio;

	}

	private JScrollPane crearPanelCentral() {
		JScrollPane panel = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		lista = new JList<>();
		lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lista.setModel(modelo);
		lista.addListSelectionListener(this);
		if (paciente.getEjercicios() != null) {
			for (int i = 0; i < paciente.getEjercicios().size(); i++) {
				EjercicioSesion ejerSesion = new EjercicioSesion(paciente.getNombre(), paciente.getApellido1(),
						paciente.getApellido2(), paciente.getEjercicios().get(i).getFecha(),
						paciente.getEjercicios().get(i).getNombre(),
						String.valueOf(paciente.getEjercicios().get(i).getMaxRepeticiones()),
						paciente.getEjercicios().get(i).getTiempo());
				modelo.add(ejerSesion);
				modeloCopia.add(ejerSesion);
			}
		}
		panel.setViewportView(lista);
		return panel;
	}

	public Component crearPanelBotones() {
		pBotonA = new JPanel(new FlowLayout());
		pBotonA.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		bAdd = new JButton("Add");
		bAdd.setIcon(new ImageIcon(this.getClass().getResource(ICONO_ADD)));
		bAdd.addActionListener(this);
		bAdd.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		pBotonA.add(bAdd);

		bEdit = new JButton("Edit");
		bEdit.setIcon(new ImageIcon(this.getClass().getResource(ICONO_EDIT)));
		bEdit.addActionListener(this);
		bEdit.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		pBotonA.add(bEdit);

		bRemove = new JButton("Remove");
		bRemove.setIcon(new ImageIcon(this.getClass().getResource(ICONO_REMOVE)));
		bRemove.addActionListener(this);
		bRemove.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		pBotonA.add(bRemove);
		return pBotonA;
	}

	public Component crearPanelBotones2() {

		pBotonS = new JPanel(new FlowLayout());
		pBotonS.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		bSave = new JButton("Save");
		bSave.setIcon(new ImageIcon(this.getClass().getResource(ICONO_SAVE)));
		bSave.addActionListener(this);
		bSave.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		pBotonS.add(bSave);
		bCancel = new JButton("Cancel");
		bCancel.setIcon(new ImageIcon(this.getClass().getResource(ICONO_CANCEL)));
		bCancel.addActionListener(this);
		bCancel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		pBotonS.add(bCancel);
		return pBotonS;
	}

	public JList<EjercicioSesion> getLista() {
		return lista;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public ArrayList<Ejercicio> getEjercicios() {
		return ejercicios;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {

	}

	public String conseguirFecha() {
		String fecha = dateChooser.getCalendar().get(java.util.Calendar.DAY_OF_MONTH) + "/"
				+ (dateChooser.getCalendar().get(java.util.Calendar.MONTH) + 1) + "/"
				+ (dateChooser.getCalendar().get(java.util.Calendar.YEAR));
		return fecha;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		try {
			if (e.getActionCommand().equals("Add")) {
				newEjercicioSesion = new EjercicioSesion(paciente.getNombre(), paciente.getApellido1(),
						paciente.getApellido2(), conseguirFecha(), (String) cEjercicios.getSelectedItem(),
						repeticiones.getValue().toString(), editor.getFormat().format(tiempo.getValue()));
				if (newEjercicioSesion != null) {
					modelo.add(newEjercicioSesion);
				}
			}
		} catch (Exception e2) {
			if (dateChooser.getDate() == null) {
				JOptionPane.showMessageDialog(this, "Fecha no seleccionada", "Aviso", JOptionPane.WARNING_MESSAGE);
			}
		}
		try {
			if (e.getActionCommand().equals("Remove")) {
				modelo.remove(getLista().getSelectedIndex());
			}
		} catch (IndexOutOfBoundsException e1) {
			JOptionPane.showMessageDialog(this, "No hay elemento ningún elemento seleccionado", "Aviso",
					JOptionPane.WARNING_MESSAGE);
		}
		if (e.getActionCommand().equals("Cancel")) {
			guardar(modeloCopia);
			dispose();
		}
		if (e.getActionCommand().equals("Edit")) {
			if (lista.getSelectedValue() == null) {
				JOptionPane.showMessageDialog(this, "No hay elemento ningún elemento seleccionado", "Aviso",
						JOptionPane.WARNING_MESSAGE);
			} else {

				index = lista.getSelectedIndex();
				VentanaEditar ventana = new VentanaEditar(this, lista.getSelectedValue(), paciente, index, modelo);
				if (e.getActionCommand().equals("Ok")) {
					modelo.add(index, ventana.getEjercicioSesion());
				}

			}
		}
		if (e.getActionCommand().equals("Save")) {
			guardar(modelo);
			dispose();
		}
	}

	public void guardar(Modelo modelo) {
		ejercicios = new ArrayList<Ejercicio>();

		for (int i = 0; i < modelo.getSize(); i++) {
			EjercicioSesion ejer = modelo.getElementAt(i);
			ejercicios.add(new Ejercicio(GestorSockets.buscarIdEjercicio(ejer.getEjercicios()), ejer.getEjercicios(),
					ejer.getFecha(), Integer.parseInt(ejer.getRepeticiones()), ejer.getTiempo()));
		}
	}

	EjercicioSesion getEjercicioSesion() {
		return newEjercicioSesion;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// System.out.println(lista.getSelectedIndex());

	}

}

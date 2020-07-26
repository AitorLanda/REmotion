package popbl3;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;

public class Calendar extends JFrame implements ActionListener, ItemListener {

	JPanel pDateChooser;
	JPanel pBoton;
	JPanel pVentana;
	JPanel pSeleccion; 
	JPanel pMonthChooser; 
	JPanel pCombo; 
	JPanel pSuperior; 
	JPanel pCentral; 
	JPanel pCentral2; 
	JPanel pDia;
	JPanel pMes; 
	JPanel pCentro;
	JDateChooser dateChooser;
	JYearChooser yearChooser;
	JMonthChooser monthChooser;
	JButton bBuscar;
	JRadioButton rMes, rDia;
	ButtonGroup grupo;
	JLabel texto;
	JComboBox<String> cMeses;
	JComboBox<String> cEjercicios;

	private final String ICONO_LUPA = "/iconos/lupa.png";
	private Paciente paciente;

	public Component calendar(Paciente paciente) {
		this.paciente = paciente;
		crearPanelVentana();
		return pVentana;

	}

	public void crearPanelVentana() {
		pVentana = new JPanel(new BorderLayout());
		pVentana.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		pVentana.add(crearPanelSuperior(), BorderLayout.NORTH);
		pCentro = new JPanel(new BorderLayout());
		pVentana.add(pCentro, BorderLayout.CENTER);
	}

	public JPanel crearPanelSuperior() {
		TitledBorder border = new TitledBorder("Criterio de búsqueda");
		border.setTitleFont(new Font("arial", Font.BOLD, 20));
		border.setTitleJustification(TitledBorder.CENTER);
		border.setTitlePosition(TitledBorder.TOP);

		pSuperior = new JPanel(new BoxLayout(pSuperior, BoxLayout.Y_AXIS));
		pSuperior.setLayout(new BoxLayout(pSuperior, BoxLayout.Y_AXIS));
		pSuperior.setBorder(border);
		crearPanelSeleccion();
		crearPanelCombo();
		crearPanelDayChooser();
		crearPanelBoton();

		pSuperior.add(pSeleccion);
		pSuperior.add(pDateChooser);
		pSuperior.add(pCombo);
		pCombo.setVisible(false);
		pSuperior.add(pBoton);

		return pSuperior;

	}

	public void crearPanelDayChooser() {
		pDateChooser = new JPanel();
		pDateChooser.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		dateChooser = new JDateChooser();
		dateChooser.setDateFormatString("dd/MM/yyyy");
		pDateChooser.add(dateChooser);
	}

	public JPanel crearPanelSeleccion() {
		pSeleccion = new JPanel();

		texto = new JLabel("Gráfico:");
		grupo = new ButtonGroup();

		rDia = new JRadioButton("Día");
		rDia.setSelected(true);
		rDia.addItemListener(this);

		rMes = new JRadioButton("Mes");
		rMes.setSelected(false);
		rMes.addItemListener(this);

		grupo.add(rDia);
		grupo.add(rMes);

		pSeleccion.add(texto);
		pSeleccion.add(rDia);
		pSeleccion.add(rMes);
		return pSeleccion;
	}

	public void crearPanelBoton() {

		pBoton = new JPanel();

		bBuscar = new JButton("Buscar");
		bBuscar.setIcon(new ImageIcon(this.getClass().getResource(ICONO_LUPA)));
		bBuscar.addActionListener(this);

		pBoton.add(bBuscar);

	}

	private void crearPanelCombo() {
		cEjercicios = new JComboBox<>();
		ArrayList<Ejercicio> ejercicios = paciente.getEjercicios();

		for (int i = 0; i < ejercicios.size(); i++) {
			cEjercicios.addItem(ejercicios.get(i).toString());
		}
		pCombo = new JPanel(new FlowLayout());

		monthChooser = new JMonthChooser();
		yearChooser = new JYearChooser();

		pCombo.add(monthChooser);
		pCombo.add(yearChooser);
		pCombo.add(cEjercicios);

	}

	public JPanel crearPanelCentral1() {
		pCentral = new JPanel(new BorderLayout());

		pDia = new JPanel(new BorderLayout());
		List<Ejercicio>lista=Ejercicio.buscarEjerciciosDia(paciente, fechaActualCompleta());
		pDia.add(new GraficoResultados(lista).crearPanelGraficoDia());

		pCentral.add(pDia);
		pCentral.setVisible(false);

		return pCentral;
	}

	public JPanel crearPanelCentral2() {
		pCentral2 = new JPanel(new BorderLayout());

		pMes = new JPanel(new BorderLayout());
		List<Ejercicio>lista=Ejercicio.buscarEjerciciosMes(paciente, fechaActualMes());
		pMes.add(new GraficoResultados(lista).crearPanelGraficoMes(cEjercicios.getSelectedItem().toString()));

		pCentral2.add(pMes);
		pCentral2.setVisible(false);

		return pCentral2;
	}
	
	public String fechaActualCompleta() {
		String fecha;
		int dia= dateChooser.getCalendar().get(java.util.Calendar.DAY_OF_MONTH);
		int mes= dateChooser.getCalendar().get(java.util.Calendar.MONTH);
		int año= dateChooser.getCalendar().get(java.util.Calendar.YEAR);
		fecha=dia+"/"+(mes+1)+"/"+año;
		return fecha;
	}
	
	public String fechaActualMes() {
		String fecha;
		int mes= monthChooser.getMonth();
		int año= yearChooser.getYear();
		fecha=(mes+1)+"/"+año;
		return fecha;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String fecha;
		if (e.getActionCommand().equals("Buscar")) {
			pCentro.removeAll();
			pCentro.setVisible(true);
			if(rDia.isSelected()) {
				pCentro.add(crearPanelCentral1(), BorderLayout.CENTER);
				pCentral.setVisible(true);	
			
			}
			
			if(rMes.isSelected()) {
				String seleccion = cEjercicios.getSelectedItem().toString();
				fecha= (monthChooser.getMonth() + 1)+"/"
						+yearChooser.getYear();
				pCentro.add(crearPanelCentral2(), BorderLayout.CENTER);
				pCentral2.setVisible(true);
				System.out.println(fecha+seleccion);
			}
		}



	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			pCentro.setVisible(false);
			if ((JRadioButton) e.getSource() == rDia) {
				pDateChooser.setVisible(true);
				pCombo.setVisible(false);
				pSuperior.add(pDateChooser);
			}
			if ((JRadioButton) e.getSource() == rMes) {
				pDateChooser.setVisible(false);
				pCombo.setVisible(true);
				pSuperior.add(pCombo);
			}
			pSuperior.add(pBoton);

		}
	}
}



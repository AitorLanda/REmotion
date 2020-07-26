package popbl3;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JYearChooser;

public class VisualizadorTabla extends JFrame implements ActionListener, ItemListener {

	JPanel pDateChooser, pBoton, pVentana, pSuperior, pCentro, pTabla;
	JDateChooser dateChooser;
	JYearChooser yearChooser;
	JButton bBuscar;
	JLabel texto;

	private final String ICONO_LUPA = "/iconos/lupa.png";
	private Paciente paciente;

	public Component visualizadorTabla(Paciente paciente) {
		this.paciente = paciente;
		crearPanelVentana();
		return pVentana;

	}

	public Component crearPanelVentana() {
		pVentana = new JPanel(new BorderLayout());
		pVentana.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		pVentana.add(crearPanelSuperior(), BorderLayout.NORTH);
		pCentro = new JPanel(new BorderLayout());
		pVentana.add(pCentro, BorderLayout.CENTER);

		pCentro.setVisible(false);
		return pVentana;
	}

	public JPanel crearPanelSuperior() {
		TitledBorder border = new TitledBorder("Criterio de búsqueda");
		border.setTitleFont(new Font("arial", Font.BOLD, 20));
		border.setTitleJustification(TitledBorder.CENTER);
		border.setTitlePosition(TitledBorder.TOP);

		pSuperior = new JPanel(new BoxLayout(pSuperior, BoxLayout.Y_AXIS));
		pSuperior.setLayout(new BoxLayout(pSuperior, BoxLayout.Y_AXIS));
		pSuperior.setBorder(border);
		texto = new JLabel("Tabla: Día");
		texto.setHorizontalAlignment(JLabel.HORIZONTAL);
		pSuperior.add(texto);

		crearPanelDayChooser();
		crearPanelBoton();

		pSuperior.add(pDateChooser);
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

	public void crearPanelBoton() {

		pBoton = new JPanel();

		bBuscar = new JButton("Buscar");
		bBuscar.setIcon(new ImageIcon(this.getClass().getResource(ICONO_LUPA)));
		bBuscar.addActionListener(this);

		pBoton.add(bBuscar);

	}

	public String conseguirFecha() {
		String fecha = dateChooser.getCalendar().get(java.util.Calendar.DAY_OF_MONTH) + "/"
				+ (dateChooser.getCalendar().get(java.util.Calendar.MONTH) + 1) + "/"
				+ (dateChooser.getCalendar().get(java.util.Calendar.YEAR));
		return fecha;
	}

	public JPanel crearPanelTabla() {
		pTabla = new JPanel(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(pTabla, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(200,0));
		pTabla.add(new TablaResultados().crearPanelTabla(paciente, null, conseguirFecha()));
		pTabla.setVisible(false);

		return pTabla;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Buscar")) {
			if (dateChooser.getDate() == null) {
				JOptionPane.showMessageDialog(this, "Fecha no seleccionada", "Aviso", JOptionPane.WARNING_MESSAGE);
			} else {
				pCentro.removeAll();
				pCentro.setVisible(true);
				pCentro.add(crearPanelTabla());
				pTabla.setVisible(true);

			}

		}

	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
		}

	}
}

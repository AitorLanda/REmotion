package popbl3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

public class EjercicioListRender implements ListCellRenderer<Ejercicio> {

	@Override
	public Component getListCellRendererComponent(JList<? extends Ejercicio> list, Ejercicio value, int index,
			boolean isSelected, boolean cellHasFocus) {
		JLabel lRepeticiones = value.getLabel();
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		panel.setBackground(Color.WHITE);
		JCheckBox check = new JCheckBox();
		check.setComponentOrientation(list.getComponentOrientation());
		check.setFont(list.getFont());
		check.setBackground(list.getBackground());
		check.setForeground(list.getForeground());
		check.setSelected(value.isSeleccionado());
		check.setText(value.getId() + " " + value.getNombre());
		panel.add(check, BorderLayout.WEST);

		lRepeticiones.setPreferredSize(new Dimension(50, 12));
		panel.add(lRepeticiones, BorderLayout.EAST);
		// panel.setSize(12, list.getWidth());
		return panel;
	}

}

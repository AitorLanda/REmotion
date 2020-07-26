package popbl3;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

public class UsuarioRenderer  extends JLabel implements ListCellRenderer<Usuario> {

	private final int TAMAÑO_LETRA = 16;
	private final int DELGADEZ = 1;
	
	@Override
	public Component getListCellRendererComponent(JList<? extends Usuario> list,
			 Usuario e,
	         int index,
	         boolean isSelected,
	         boolean cellHasFocus)
	     {
		Border border = BorderFactory.createLineBorder(Color.BLUE, DELGADEZ);
		setFont(new Font("Arial", Font.BOLD, TAMAÑO_LETRA));

		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		if (isSelected && cellHasFocus) {
			setBorder(border);
		} else {
			setBorder(null);
		}

		this.setText(e.toString());
		this.setOpaque(true);
		return this;
	}

}

package popbl3;

import java.awt.Component;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class TablaResultados {

	JScrollPane scrollPaneTabla;

	public Component crearPanelTabla(Paciente paciente, ModeloTablaEjercicio modeloTablaExterno, String data) {

		scrollPaneTabla = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JTable tablaEjercicios;
		ModeloTablaEjercicio modeloTabla;

		ModeloColumnasTablaEjercicios modeloColumnasTabla = new ModeloColumnasTablaEjercicios();

		if (modeloTablaExterno == null)
			modeloTabla = new ModeloTablaEjercicio(modeloColumnasTabla, paciente, data);
		else
			modeloTabla = modeloTablaExterno;

		tablaEjercicios = new JTable(modeloTabla, modeloColumnasTabla);
		tablaEjercicios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablaEjercicios.setFillsViewportHeight(true);
		tablaEjercicios.getTableHeader().setReorderingAllowed(false);
		tablaEjercicios.setRowHeight(30);
		tablaEjercicios.getBorder();

		scrollPaneTabla.setViewportView(tablaEjercicios);

		return scrollPaneTabla;
	}
}

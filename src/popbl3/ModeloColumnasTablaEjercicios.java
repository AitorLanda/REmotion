package popbl3;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

public class ModeloColumnasTablaEjercicios extends DefaultTableColumnModel {

	public ModeloColumnasTablaEjercicios() {
		super();

		this.addColumn(crearColumna("ID", 0, 5));
		this.addColumn(crearColumna("Nombre", 1, 100));
		this.addColumn(crearColumna("Fecha", 2, 10));
		this.addColumn(crearColumna("Repeticiones", 3, 20));
		this.addColumn(crearColumna("Resultado", 4, 150));
		this.addColumn(crearColumna("Tiempo", 5, 10));
		
	}

	private TableColumn crearColumna(String texto, int indice, int ancho) {
		TableColumn columna = new TableColumn(indice, ancho);

		columna.setHeaderValue(texto);
		columna.setPreferredWidth(ancho);
		columna.setHeaderRenderer(new DefaultTableCellRenderer() {

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {

				setBackground(Color.black);
				setForeground(Color.white);
				setHorizontalAlignment(CENTER);

				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
		});
		columna.setCellRenderer(new DefaultTableCellRenderer() {

			@Override
			public Component getTableCellRendererComponent(JTable table, Object valor, boolean isSelected,
					boolean hasFocus, int fila, int columna) {
		
				super.getTableCellRendererComponent(table, valor, isSelected, hasFocus, fila, columna);

				super.setHorizontalAlignment(CENTER);

				return this;
			}

		});
		return columna;
	}

}

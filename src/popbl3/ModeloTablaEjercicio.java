package popbl3;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class ModeloTablaEjercicio extends AbstractTableModel {

	ModeloColumnasTablaEjercicios columnas;
	ArrayList<Ejercicio> ejerciciosFecha = new ArrayList<>();

	public ModeloTablaEjercicio(ModeloColumnasTablaEjercicios columnas, Paciente paciente, String data) {
		super();

		for (Ejercicio ejercicio : paciente.getEjercicios()) {
			if (ejercicio.getFecha().equals(data)) {
				ejerciciosFecha.add(ejercicio);
			}

		}

		this.ejerciciosFecha = getEjerciciosFecha();
		this.columnas = columnas;
	}

	@Override
	public int getColumnCount() {
		return columnas.getColumnCount();
	}

	@Override
	public int getRowCount() {
		return ejerciciosFecha.size();
	}

	@Override
	public Object getValueAt(int fila, int columna) {
		Ejercicio a = ejerciciosFecha.get(fila);
		return a.getFieldAt(columna);

	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {

		return getValueAt(0, columnIndex).getClass();
	}

	public Ejercicio getEjercicio(int indice) {
		return ejerciciosFecha.get(indice);
	}

	public void insertarListaEjercicios(ArrayList<Ejercicio> ejercicios) {
		this.ejerciciosFecha = ejercicios;
		this.fireTableDataChanged();
	}

	public void eliminarListaEjercicios() {
		this.ejerciciosFecha = null;
	}

	public ArrayList<Ejercicio> getEjerciciosFecha() {
		return ejerciciosFecha;
	}

}

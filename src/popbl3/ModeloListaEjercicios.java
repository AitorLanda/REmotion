package popbl3;


import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.DefaultListModel;

public class ModeloListaEjercicios extends DefaultListModel<Ejercicio> {
	final String SEPARADOR = "$";	
	
	public ModeloListaEjercicios (Paciente paciente){
		super();
		inicializar(paciente);
	}

	public void inicializar(Paciente paciente) {
		Calendar fecha = new GregorianCalendar();
		int dia = fecha.get(Calendar.DAY_OF_MONTH);
		int mes = fecha.get((Calendar.MONTH));
		int año = fecha.get(Calendar.YEAR);
		String data = dia + "/" + (mes + 1) + "/" + año;
		for(Ejercicio ejercicio : paciente.getEjercicios()){
			if(ejercicio.getFecha().equals(data)) {
				this.addElement(ejercicio);
			}
		}
		
	}
	
	
}






package popbl3;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class Modelo implements ListModel<EjercicioSesion> {

	List<EjercicioSesion> agenda;
	List<ListDataListener> listeners;
	
	public Modelo() {
		agenda = new ArrayList<>();
		listeners = new ArrayList<>();
	}
	public void add(EjercicioSesion ejercicioSesion) {
		agenda.add (ejercicioSesion);
		notifyListeners();
	}
	
	public void add(int index, EjercicioSesion ejercicioSesion) {
		
		agenda.remove(index);
		agenda.add(index, ejercicioSesion);
		notifyListeners();
	}
	
	public void remove (EjercicioSesion ejercicioSesion) {
		agenda.remove(ejercicioSesion);
		notifyListeners();
	}
	public void remove (int indice) {
		agenda.remove(indice);
		notifyListeners();
	}
	
	private void notifyListeners() {
		for (ListDataListener listener : listeners) {
			listener.contentsChanged(new ListDataEvent (agenda,
					ListDataEvent.CONTENTS_CHANGED,0,agenda.size()));
		}
		
	}
	@Override
	public int getSize() {
		
		return agenda.size();
	}

	@Override
	public EjercicioSesion getElementAt(int index) {
		
		return agenda.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListDataListener(ListDataListener listener) {
		listeners.remove(listener);
		
	}

}


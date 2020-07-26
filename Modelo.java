package listadatosdinámicos;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class Modelo implements ListModel<Persona> {
	
	List<Persona> agenda;
	List<ListDataListener> listeners;
	
	public Modelo() {
		agenda = new ArrayList<>();
		listeners = new ArrayList<>();
	}
	public void add(Persona persona) {
		agenda.add (persona);
		notifyListeners();
	}
	public void remove (Persona persona) {
		agenda.remove(persona);
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
	public Persona getElementAt(int index) {
		
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

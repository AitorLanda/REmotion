package popbl3;

import java.util.ArrayList;

public class Paciente extends Usuario {

	String tipoLesion;
	ArrayList<Ejercicio> ejercicios;
	String fisioAsociado;

	public Paciente(String userName, String nombre, String apellido1, String apellido2, String centro,
			String tipoLesion, String fisioAsociado) {

		super(userName, nombre, apellido1, apellido2, centro);
		this.tipoLesion = tipoLesion;
		this.ejercicios = new ArrayList<>();
		this.fisioAsociado = fisioAsociado;
		this.setTipoUsuario(PACIENTE);
	}

	public Paciente(String userName, String nombre, String apellido1, String apellido2, String centro,
			String tipoLesion, String lesion, ArrayList<Ejercicio> ejercicios, String fisioAsociado) {

		super(userName, nombre, apellido1, apellido2, centro);
		this.tipoLesion = lesion;
		this.ejercicios = ejercicios;
		this.tipoLesion = tipoLesion;
		this.fisioAsociado = fisioAsociado;
		this.setTipoUsuario(PACIENTE);
	}

	public void setEjercicios(ArrayList<Ejercicio> ejercicios) {
		this.ejercicios = ejercicios;
	}

	public ArrayList<Ejercicio> getEjercicios() {
		return ejercicios;
	}

	public String getTipoLesion() {
		return tipoLesion;
	}

	public String getFisioAsociado() {
		return fisioAsociado;
	}

	@Override
	public String guardar() {
		return super.guardar() + SEPARADOR + tipoLesion + SEPARADOR + fisioAsociado + SEPARADOR + ejercicios.size();
	}
}
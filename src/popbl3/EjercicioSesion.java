package popbl3;

public class EjercicioSesion {
	String nombre;
	String apellido1;
	String apellido2;
	String fecha;
	String ejercicios;
	String repeticiones;
	String tiempo;
	
	public EjercicioSesion (String nombre, String apellido1, String apellido2, String fecha, String ejercicios, String repeticiones, String tiempo) {
		this.nombre = nombre;
		this.apellido1 = apellido1;
		this.apellido2 = apellido2;
		this.fecha = fecha;
		this.ejercicios = ejercicios;
		this.repeticiones = repeticiones;
		this.tiempo = tiempo;
	}

	public String getTiempo() {
		return tiempo;
	}

	public String getNombre() {
		return nombre;
	}

		public String getApellido1() {
	return apellido1;
	}

	public String getApellido2() {
		return apellido2;
	}

	public String getFecha() {
		return fecha;
	}

	public String getEjercicios() {
		return ejercicios;
	}

	public String getRepeticiones() {
		return repeticiones;
	}
	
	@Override
	public String toString() {
		return this.nombre+" "+this.apellido1+" "+this.apellido2+" "+this.fecha+" "+this.ejercicios+" "+this.repeticiones+" "+this.tiempo;
	}
}

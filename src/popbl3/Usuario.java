package popbl3;

public abstract class Usuario {
	final static String SEPARADOR = "$";
	final static int FISIO = 0;
	final static int ADMIN = 1;
	final static int PACIENTE = 2;
	
	private int tipoUsuario;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String userName;
	private String centro;

	public Usuario(String userName, String nombre, String apellido1, String apellido2, String centro) {
		this.nombre = nombre;
		this.apellido1 = apellido1;
		this.apellido2 = apellido2;
		this.userName = userName;
		this.centro = centro;
	}	

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido1() {
		return apellido1;
	}

	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}

	public String getApellido2() {
		return apellido2;
	}

	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCentro() {
		return centro;
	}

	public void setCentro(String centro) {
		this.centro = centro;
	}
	
	public int getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(int tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	@Override
	public String toString() {
		return  nombre+ " " + apellido1 +" "+ apellido2;
	}

	public String guardar() {		
		return userName+SEPARADOR+nombre+SEPARADOR+apellido1+SEPARADOR+apellido2+SEPARADOR+centro;
	}

}

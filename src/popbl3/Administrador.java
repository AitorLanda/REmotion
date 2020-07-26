package popbl3;

public class Administrador extends Usuario {

	public Administrador(String userName, String nombre, String apellido1, String apellido2, String centro) {
		super(userName, nombre, apellido1, apellido2, centro);
		this.setTipoUsuario(ADMIN);
	}	
}

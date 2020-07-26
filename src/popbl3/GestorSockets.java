package popbl3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class GestorSockets {
	
	static int PUERTO;
	static String DIRECCION;
	
	final static int USERNAME = 0;
	final static int NOMBRE = 1;
	final static int APELLIDO1 = 2;
	final static int APELLIDO2 = 3;
	final static int CENTRO = 4;
	final static int TIPO_LESION = 5;
	final static int FISIO_ASOCIADO = 6;
	final static int NUM_EJERCICIOS = 7;
	final static int PASSWORD = 1;
	final static int NOMBRE_LESION = 0;
	private final static String PACIENTE_STRING = "Paciente";
	private final static String FISIO_STRING = "Fisio";
	private final static String ADMIN_STRING = "Admin";
	
	final static String SEPARADOR_DOLAR = "[$]";
	final static String SEPARADOR_IGUAL = "[=]";
	
	public static void cargarConfiguracion(String direccion, int puerto){
		 DIRECCION = direccion;
	     PUERTO = puerto;
	}
	
	public static boolean verificarLogin(String username, String password){
		Socket socketDatos = null;
		boolean verificado = false;
		DataInputStream in;
		DataOutputStream out;
		String strIn;
		
		try {
			socketDatos =  new Socket(DIRECCION, PUERTO);
			in = new DataInputStream(socketDatos.getInputStream());
			out = new DataOutputStream(socketDatos.getOutputStream());
			
			out.writeUTF("Login");
			out.writeUTF(username+"$"+password);
			
			strIn = in.readUTF();
			
			if(strIn.equals("Succesfull")){
				in.close();
				out.close();
				socketDatos.close();
				verificado = true;
			}else{
				in.close();
				out.close();
				socketDatos.close();
				verificado = false;
			}		

		} catch (UnknownHostException e) {
			System.out.println("Host no encontrado");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "No se ha podido establecer la conexion. Contacte con el administrador", "Error", JOptionPane.ERROR_MESSAGE);
		} finally {
			try {
				socketDatos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return verificado;
	}
	
	public static Usuario cargarUsuario(String username){
		Socket socketDatos = null;
		DataInputStream in;
		DataOutputStream out;
		String strIn;
		
		try {
			socketDatos =  new Socket(DIRECCION, PUERTO);
			in = new DataInputStream(socketDatos.getInputStream());
			out = new DataOutputStream(socketDatos.getOutputStream());
			
			out.writeUTF("Cargar Usuario");
			out.writeUTF(username);
			
			strIn = in.readUTF();
			
			switch(strIn){
				case PACIENTE_STRING:
					strIn = in.readUTF();
					Paciente paciente = ModeloListaUsuario.crearPaciente(strIn.split(SEPARADOR_DOLAR));
					strIn = in.readUTF();
					int numEjercicios = Integer.valueOf(strIn);
					ArrayList<Ejercicio> ejercicios = new ArrayList<>();
					
					for (int i = 0; i < numEjercicios; i++){
						strIn = in.readUTF();
						ejercicios.add(ModeloListaUsuario.crearEjercicio(strIn.split(SEPARADOR_DOLAR)));
					}
					paciente.setEjercicios(ejercicios);
					
					socketDatos.close();
					return paciente;
					
				case FISIO_STRING:
					strIn = in.readUTF();
					Fisio fisio = ModeloListaUsuario.crearFisio(strIn.split(SEPARADOR_DOLAR));
					
					socketDatos.close();
					return fisio;
					
				case ADMIN_STRING:
					strIn = in.readUTF();
					Administrador admin = ModeloListaUsuario.crearAdministrador(strIn.split(SEPARADOR_DOLAR));
					
					socketDatos.close();
					return admin;
					
				case "Null":
					socketDatos.close();
					return null;
					
				default:
					break;
			}
			socketDatos.close();
		} catch (UnknownHostException e) {
			System.out.println("Host no encontrado");
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				socketDatos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
		
	}

	public static void añadirUserYPasswordEnFicheroLogin(String usernameNuevo, String passwordNueva){
		Socket socketDatos = null;
		DataInputStream in;
		DataOutputStream out;
		
		
			try {
				socketDatos =  new Socket(DIRECCION, PUERTO);
				in = new DataInputStream(socketDatos.getInputStream());
				out = new DataOutputStream(socketDatos.getOutputStream());
			
				out.writeUTF("Añadir Usuario Fichero Login");
				out.writeUTF(usernameNuevo+"$"+passwordNueva);
				
				in.readUTF();
			
			
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				try {
					socketDatos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
	
	public static void añadirAlFichero(Usuario userNuevo) {
		Socket socketDatos = null;
		DataInputStream in;
		DataOutputStream out;		
		
		try {
			socketDatos =  new Socket(DIRECCION, PUERTO);
			in = new DataInputStream(socketDatos.getInputStream());
			out = new DataOutputStream(socketDatos.getOutputStream());
			
			out.writeUTF("Añadir Al Fichero Usuario");
			
			if(userNuevo != null){
				
				out.writeUTF(userNuevo.guardar());
				
				if(userNuevo instanceof Paciente){
					out.writeUTF(PACIENTE_STRING);
				}
				if(userNuevo instanceof Fisio){
					out.writeUTF(FISIO_STRING);
				}
				if(userNuevo instanceof Administrador){
					out.writeUTF(ADMIN_STRING);
				}
				if(userNuevo instanceof Paciente){
					Paciente paciente = (Paciente)userNuevo;
					out.writeUTF(String.valueOf(paciente.getEjercicios().size()));
					for(Ejercicio ejercicio : paciente.getEjercicios()){
						out.writeUTF(ejercicio.guardar());
					}
				}
			}
			
			in.readUTF();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				socketDatos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	public static void modificarFicheroLogin(String nombreUsuario, String contraseñaNueva, String usernameDelFichero) {
		Socket socketDatos = null;
		DataInputStream in;
		DataOutputStream out;
		
		try {
			socketDatos =  new Socket(DIRECCION, PUERTO);
			in = new DataInputStream(socketDatos.getInputStream());
			out = new DataOutputStream(socketDatos.getOutputStream());
		
			out.writeUTF("Modificar Usuario Fichero Login");
			out.writeUTF(nombreUsuario+"$"+contraseñaNueva+"$"+usernameDelFichero);
		
			in.readUTF();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				socketDatos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void modificarDatosFichero(Usuario usuarioModificado, String usernameDelFichero) {
		Socket socketDatos = null;
		DataInputStream in;
		DataOutputStream out;		
		
		try {
			socketDatos =  new Socket(DIRECCION, PUERTO);
			in = new DataInputStream(socketDatos.getInputStream());
			out = new DataOutputStream(socketDatos.getOutputStream());
			
			out.writeUTF("Modificar Fichero Usuario");
			
			if(usuarioModificado != null){
				
				out.writeUTF(usuarioModificado.guardar());
				out.writeUTF(usernameDelFichero);
				
				if(usuarioModificado instanceof Paciente){
					out.writeUTF(PACIENTE_STRING);
				}
				if(usuarioModificado instanceof Fisio){
					out.writeUTF(FISIO_STRING);
				}
				if(usuarioModificado instanceof Administrador){
					out.writeUTF(ADMIN_STRING);
				}
				
				if(usuarioModificado instanceof Paciente){
					Paciente paciente = (Paciente)usuarioModificado;
					out.writeUTF(String.valueOf(paciente.getEjercicios().size()));
					for(Ejercicio ejercicio : paciente.getEjercicios()){
						out.writeUTF(ejercicio.guardar());
					}
				}
			}
			in.readUTF();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				socketDatos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void eliminarDatosLoginDelUsuario(Usuario usuarioABorrar){
		Socket socketDatos = null;
		DataInputStream in;
		DataOutputStream out;		
		
		
		try {
			socketDatos =  new Socket(DIRECCION, PUERTO);
			in = new DataInputStream(socketDatos.getInputStream());
			out = new DataOutputStream(socketDatos.getOutputStream());
		
			out.writeUTF("Eliminar Usuario Fichero Login");
			if(usuarioABorrar != null){
				
				out.writeUTF(usuarioABorrar.guardar());
				
				if(usuarioABorrar instanceof Paciente){
					out.writeUTF(PACIENTE_STRING);
				}
				if(usuarioABorrar instanceof Fisio){
					out.writeUTF(FISIO_STRING);
				}
				if(usuarioABorrar instanceof Administrador){
					out.writeUTF(ADMIN_STRING);
				}
				
				if(usuarioABorrar instanceof Paciente){
					Paciente paciente = (Paciente)usuarioABorrar;
					out.writeUTF(String.valueOf(paciente.getEjercicios().size()));
					for(Ejercicio ejercicio : paciente.getEjercicios()){
						out.writeUTF(ejercicio.guardar());
					}
				}
			}
			in.readUTF();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socketDatos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void eliminarUsuarioDelFichero(Usuario usuarioABorrar) {
		Socket socketDatos = null;
		DataInputStream in;
		DataOutputStream out;		
		
		try {
			socketDatos =  new Socket(DIRECCION, PUERTO);
			in = new DataInputStream(socketDatos.getInputStream());
			out = new DataOutputStream(socketDatos.getOutputStream());
		
			out.writeUTF("Eliminar Del Fichero Usuario");
			if(usuarioABorrar != null){
				
				out.writeUTF(usuarioABorrar.guardar());
				
				if(usuarioABorrar instanceof Paciente){
					out.writeUTF(PACIENTE_STRING);
				}
				if(usuarioABorrar instanceof Fisio){
					out.writeUTF(FISIO_STRING);
				}
				if(usuarioABorrar instanceof Administrador){
					out.writeUTF(ADMIN_STRING);
				}
				
				if(usuarioABorrar instanceof Paciente){
					Paciente paciente = (Paciente)usuarioABorrar;
					out.writeUTF(String.valueOf(paciente.getEjercicios().size()));
					for(Ejercicio ejercicio : paciente.getEjercicios()){
						out.writeUTF(ejercicio.guardar());
					}
				}
			}
			in.readUTF();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socketDatos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static String[] cargarLesiones() {
		Socket socketDatos = null;
		DataInputStream in;
		DataOutputStream out;	
		
		String lesiones[];
		ArrayList<String> listaLesiones = new ArrayList<>();
		
		
		try {
			socketDatos =  new Socket(DIRECCION, PUERTO);
			in = new DataInputStream(socketDatos.getInputStream());
			out = new DataOutputStream(socketDatos.getOutputStream());
			
			out.writeUTF("Cargar Lesiones");
			out.writeUTF("");
			
			int numLesiones = Integer.valueOf(in.readUTF());
			
			for (int i = 0; i < numLesiones; i++){
				listaLesiones.add(in.readUTF());
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socketDatos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		lesiones = new String[listaLesiones.size()];
		
		int i = 0;
		for(String lesion : listaLesiones){
			lesiones[i] = lesion;
			i++;
		}
		
		return lesiones;
	}
	
	public static String[] cargarEjercicios() {
		Socket socketDatos = null;
		DataInputStream in;
		DataOutputStream out;
		
		String ejercicios[];
		ArrayList<String> listaEjercicios = new ArrayList<>();

		try {
			socketDatos =  new Socket(DIRECCION, PUERTO);
			in = new DataInputStream(socketDatos.getInputStream());
			out = new DataOutputStream(socketDatos.getOutputStream());
			
			out.writeUTF("Cargar Ejercicios");
			out.writeUTF("");
			
			int numEjercicios = Integer.valueOf(in.readUTF());
			
			for (int i = 0; i < numEjercicios; i++){
				listaEjercicios.add(in.readUTF());
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socketDatos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ejercicios = new String[listaEjercicios.size()];
		
		int i = 0;
		for(String ejercicio : listaEjercicios){
			ejercicios[i] = ejercicio;
			i++;
		}
		return ejercicios;
	}
	
	public static boolean isUsernameInLoginFile(String username) {
		Socket socketDatos = null;
		boolean itIs = false;
		DataInputStream in;
		DataOutputStream out;
		String strIn;
		
		try {
			socketDatos =  new Socket(DIRECCION, PUERTO);
			in = new DataInputStream(socketDatos.getInputStream());
			out = new DataOutputStream(socketDatos.getOutputStream());
			
			out.writeUTF("Verificar Username");
			out.writeUTF(username);
			
			strIn = in.readUTF();
			
			if(strIn.equals("Succesfull")){
				in.close();
				out.close();
				itIs = true;
			}else{
				in.close();
				out.close();
				itIs = false;
			}
			
		} catch (UnknownHostException e) {
			System.out.println("Host no encontrado");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "No se ha podido establecer la conexion. Contacte con el administrador", "Error", JOptionPane.ERROR_MESSAGE);
		} finally {
			try {
				socketDatos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return itIs;
	}
	
	public static boolean fisioUsernameDoesNotExist(String usernameFisio){
		Socket socketDatos = null;
		DataInputStream in;
		DataOutputStream out;
		String strIn;
		
		try {
			socketDatos =  new Socket(DIRECCION, PUERTO);
			in = new DataInputStream(socketDatos.getInputStream());
			out = new DataOutputStream(socketDatos.getOutputStream());
			
			out.writeUTF("Verificar Username Fisio");
			out.writeUTF(usernameFisio);
			
			strIn = in.readUTF();
			
			if(strIn.equals("Succesfull")){
				in.close();
				out.close();
				socketDatos.close();
				return true;
			}else{
				in.close();
				out.close();
				socketDatos.close();
				return false;
			}
			
			
		} catch (UnknownHostException e) {
			System.out.println("Host no encontrado");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "No se ha podido establecer la conexion. Contacte con el administrador", "Error", JOptionPane.ERROR_MESSAGE);
		} finally {
			try {
				socketDatos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static String buscarIdEjercicio(String nombre) {
		String id = null;
		Socket socketDatos = null;
		DataInputStream in;
		DataOutputStream out;

		try {
			socketDatos = new Socket(GestorSockets.DIRECCION, GestorSockets.PUERTO);
			in = new DataInputStream(socketDatos.getInputStream());
			out = new DataOutputStream(socketDatos.getOutputStream());

			out.writeUTF("Buscar ID Ejercicio");
			out.writeUTF(nombre);

			id = in.readUTF();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socketDatos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		
		return id;
	}
}

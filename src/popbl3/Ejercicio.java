package popbl3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JLabel;

import popbl3.GestorMano.RESULTADO;

public class Ejercicio {

	final String SEPARADOR = "$";
	final String SEPARADOR_TIEMPO = ":";
	static public final int BIEN = 0;
	static public final int CASI = 1;
	static public final int INACABADO = 2;

	final int ID_EJERCICIO = 0;
	final int NOMBRE_EJERCICIO = 5;
	final int DESCRIPCION_EJERCICIO = 2;
	final int DIRECTORIO_GIF = 3;

	String id;
	String nombre;
	String descripcion;
	String directorioGIF;
	String resultado;
	String fecha;
	String tiempo;
	int[] segundos;
	int repeticiones;
	int repeticionesRealizadas;
	boolean seleccionado;
	boolean agotado;
	
	public boolean isAgotado() {
		return agotado;
	}

	public void setAgotado(boolean agotado) {
		this.agotado = agotado;
	}

	JLabel lRepeticiones;

	public Ejercicio(Ejercicio e) {
		this.id = e.getId();
		this.nombre = e.getNombre();
		this.descripcion = e.getDescripcion();
		this.directorioGIF = e.getDirectorioGIF();
		this.resultado = e.getResultado();
		this.fecha = e.fechaDelSistema();
		this.lRepeticiones = new JLabel();
		this.setRepeticiones(e.getMaxRepeticiones());
		this.seleccionado = e.isSeleccionado();
		this.tiempo = e.getTiempo();

	}

	public Ejercicio(String id, String nombre, String descripcion, String directorioGIF) {
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.directorioGIF = directorioGIF;
		this.resultado = "";
		this.fecha = fechaDelSistema();
		this.lRepeticiones = new JLabel();
		this.setRepeticiones(0);
		this.seleccionado = false;
		this.tiempo = "01:00";
	}

	public Ejercicio(String id, String resultado, String fecha, int repeticiones, boolean seleccionado, String tiempo) {
		this.id = id;
		this.resultado = resultado;
		this.fecha = fecha;
		this.lRepeticiones = new JLabel();
		this.setRepeticiones(repeticiones);
		this.seleccionado = seleccionado;
		this.cargarNombreDescripcionYGIF(id);
		this.tiempo = tiempo;
	}

	public Ejercicio(String id, String nombre, String fecha, int repeticiones, String tiempo) {
		this.id = id;
		this.nombre = nombre;
		this.fecha = fecha;
		this.lRepeticiones = new JLabel();
		this.setRepeticiones(repeticiones);
		this.tiempo = tiempo;
		this.seleccionado = false;
		this.cargarNombreDescripcionYGIF(id);
		this.resultado = "BIEN=0@CASI=0@INACABADO=0";
	}

	private void cargarNombreDescripcionYGIF(String id) {

		Socket socketDatos = null;
		DataInputStream in;
		DataOutputStream out;

		try {
			socketDatos = new Socket(GestorSockets.DIRECCION, GestorSockets.PUERTO);
			in = new DataInputStream(socketDatos.getInputStream());
			out = new DataOutputStream(socketDatos.getOutputStream());

			out.writeUTF("Cargar Datos Ejercicio");
			out.writeUTF(id);

			this.nombre = in.readUTF();
			this.descripcion = in.readUTF();
			this.directorioGIF = in.readUTF();
			
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

	public static String fechaDelSistema() {
		String fecha;
		Calendar calendario = new GregorianCalendar();

		int año = calendario.get(Calendar.YEAR);
		int mes = calendario.get(Calendar.MONTH);
		int dia = calendario.get(Calendar.DAY_OF_MONTH);

		fecha = dia + "/" + (mes + 1) + "/" + año;

		return fecha;
	}

	public String getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public String getTiempo() {
		return tiempo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public String getDirectorioGIF() {
		return directorioGIF;
	}

	public String getFecha() {
		return fecha;
	}

	public void changeSelection() {
		this.seleccionado = !this.seleccionado;
	}

	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}

	public boolean isSeleccionado() {
		return seleccionado;
	}

	public String getResultado() {
		if (resultado.isEmpty()) {
			resultado = "BIEN=0@CASI=0@INACABADO=0";
		}
		return resultado;
	}

	public void setResultado(int bien, int casi, int mal) {
		resultado = "BIEN=" + bien + "@CASI=" + casi + "@INACABADO=" + mal;
	}

	public Object getFieldAt(int columna) {
		switch (columna) {
		case 0:
			return id;
		case 1:
			return nombre;
		case 2:
			return fecha;
		case 3:
			return repeticiones;
		case 4:
			return "Bien = " + getValorResultado(BIEN) + " Casi =  " + getValorResultado(CASI) + " Inacabado =  "
					+ getValorResultado(INACABADO);
		case 5:
			return tiempo;
		default:
			return null;
		}
	}

	public String guardar() {
		String linea;

		linea = id + SEPARADOR + resultado + SEPARADOR + fecha + SEPARADOR + repeticiones + SEPARADOR + seleccionado
				+ SEPARADOR + tiempo;

		return linea;
	}

	public JLabel getLabel() {
		return lRepeticiones;
	}

	public void setRepeticiones(int repeticiones) {
		this.repeticiones = repeticiones;
		if (repeticiones != 0)
			lRepeticiones.setText(Integer.toString(repeticiones));
		else
			lRepeticiones.setText("");

	}

	public int getMaxRepeticiones() {
		return repeticiones;
	}

	public boolean isCompletado() {
		boolean completado = false;
		if (getValorResultado(BIEN) >= getMaxRepeticiones())
			completado = true;
		return completado;
	}

	public int getValorResultado(int calificacion) {
		int cantidad = 0;
		String[] splitResult = new String[3];
		String[] splitValor = new String[2];
		splitResult = getResultado().split("[@]");
		splitValor = splitResult[calificacion].split("[=]");
		cantidad = Integer.valueOf(splitValor[1]);
		return cantidad;
	}

	public boolean modificarResultado(RESULTADO resultado) {
		boolean correcto = true;
		int bien = 0;
		int casi;
		int inacabados;
		try {
			if (!this.isCompletado()) {
				bien = getValorResultado(BIEN);
				casi = getValorResultado(CASI);
				inacabados = getValorResultado(INACABADO);
				switch (resultado) {
				case BIEN:
					bien++;
					break;
				case CASI:
					casi++;
					break;
				case MAL:
					inacabados = repeticiones - getValorResultado(BIEN);

					break;
				default:
					break;
				}
				this.setResultado(bien, casi, inacabados);
			} else {
				this.setResultado(this.getMaxRepeticiones(), getValorResultado(CASI), 0);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			correcto = false;
		}

		return correcto;

	}

	public static List<Ejercicio> buscarEjerciciosDia(Paciente paciente, String fechaDeEjercicios) {
		List<Ejercicio> listaEjerciciosFecha = new ArrayList<Ejercicio>();

		for (int i = 0; i < paciente.getEjercicios().size(); i++) {
			if (paciente.getEjercicios().get(i).getFecha().equals(fechaDeEjercicios))
				listaEjerciciosFecha.add(paciente.getEjercicios().get(i));
		}
		return listaEjerciciosFecha;
	}

	public static List<Ejercicio> buscarEjerciciosMes(Paciente paciente, String fechaDeEjercicios) {
		List<Ejercicio> listaEjerciciosFecha = new ArrayList<Ejercicio>();

		for (int i = 0; i < paciente.getEjercicios().size(); i++) {
			if (paciente.getEjercicios().get(i).getFecha().contains(fechaDeEjercicios))
				listaEjerciciosFecha.add(paciente.getEjercicios().get(i));
		}
		return listaEjerciciosFecha;
	}

	public void aSegundos() {
		this.segundos = new int[4];
		String valoresTiempo[];
		int segundos = 0;
		int multiplicador = 60;
		valoresTiempo = tiempo.split(SEPARADOR_TIEMPO);
		multiplicador = (int) Math.pow(multiplicador, (valoresTiempo.length - 1));
		for (int i = 0; i < valoresTiempo.length; i++) {
			segundos += Integer.parseInt(valoresTiempo[i]) * multiplicador;
			multiplicador /= 60;
		}
		int dig;
		int j = this.segundos.length;
		while (segundos > 0) {
			dig = segundos % 10;
			segundos = segundos / 10;
			this.segundos[--j] = dig;
		}
	}

	@Override
	public String toString() {
		return nombre;
	}
}

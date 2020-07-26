package popbl3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.DefaultListModel;

public class ModeloEjercicioEstandar extends DefaultListModel<Ejercicio> {

	final int NOMBRE_LESION = 0;
	final int ID_DE_EJERCICIOS = 1;
	final int ID = 0;
	final int NOMBRE = 1;
	final int DESCRIPCION = 2;
	final int DIRECTORIO_GIF = 3;
	final String SEPARADOR = "[$]";
	final String SEPARADOR_NUMEROS = "[#]";
	boolean inicializado;

	public ModeloEjercicioEstandar(String tipoLesion) {
		super();
		if (tipoLesion != null) {
			inicializarListaEjercicios(tipoLesion);
		} else {
			inicializado = false;
		}

	}

	public void inicializarListaEjercicios(String nombreEjer) {
		Socket socketDatos = null;
		DataInputStream in;
		DataOutputStream out;
		String strIn = null;

		try {
			socketDatos = new Socket(GestorSockets.DIRECCION, GestorSockets.PUERTO);
			in = new DataInputStream(socketDatos.getInputStream());
			out = new DataOutputStream(socketDatos.getOutputStream());

			out.writeUTF("Cargar Lista Ejercicios");
			out.writeUTF(nombreEjer);

			do {
				strIn = in.readUTF();

				if (!strIn.equals("Fin")) {
					Ejercicio nuevoEjercicio = this.crearEjercicio(strIn.split(SEPARADOR));
					this.addElement(nuevoEjercicio);
				}

			} while (!strIn.equals("Fin"));

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
		inicializado = true;
	}

	private Ejercicio crearEjercicio(String[] valoresEjercicio) {
		Ejercicio nuevoEjercicio = new Ejercicio(valoresEjercicio[ID], valoresEjercicio[NOMBRE],
				valoresEjercicio[DESCRIPCION], valoresEjercicio[DIRECTORIO_GIF]);
		return nuevoEjercicio;
	}

	public void marcarEjercicios(Paciente paciente) {
		if (paciente.getEjercicios() != null) {
			for (Ejercicio ejercicio : paciente.getEjercicios()) {
				for (int i = 0; i < this.getSize(); i++) {

					if (ejercicio.getId().equals(this.get(i).getId())) {
						this.setElementAt(ejercicio, i);
					}

				}
			}
		}
	}

	public boolean isListaEjerciciosEmpty() {
		for (int i = 0; i < this.getSize(); i++) {
			if (this.get(i).isSeleccionado()) {
				return false;
			}
		}
		return true;
	}

	public boolean isInicializado() {
		return inicializado;
	}
}

package popbl3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

public class DialogoContraseñaNueva extends JDialog implements ActionListener{

	JPasswordField contraseñaNueva;
	JPasswordField contraseñaRepetida;
	
	String newContraseña = null;
	
	public DialogoContraseñaNueva(JDialog panelAnterior){
		super(panelAnterior,"Nueva contraseña");
		super.setModal(true);
		
		this.setSize(300,225);
		this.setLocation(200,50);
		this.setContentPane(crearPanelVentana());
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	private Container crearPanelVentana() {
		JPanel panel = new JPanel (new BorderLayout(0,10));
		
		panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		panel.add(crearPanelPasswordFields(),BorderLayout.CENTER);
		panel.add(crearPanelBotones(),BorderLayout.SOUTH);
		
		return panel;
	}

	private Component crearPanelPasswordFields() {
		JPanel panel = new JPanel (new GridLayout(2,1));
		
		contraseñaNueva = new JPasswordField();
		contraseñaRepetida= new JPasswordField();
		
		panel.add(crearPasswordField(contraseñaNueva,"Contraseña nueva"));
		panel.add(crearPasswordField(contraseñaRepetida,"Repita la contraseña"));		
		
		return panel;
	}

	private Component crearPasswordField(JPasswordField text, String titulo) {
		JPanel panel = new JPanel(new GridLayout(1,1));
		panel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.cyan), titulo));
		
		panel.add(text);
		return panel;
	}

	private Component crearPanelBotones() {
		
		JPanel panel = new JPanel (new GridLayout(1,2,20,0));
		JButton bOk = new JButton ("OK");
		bOk.setActionCommand("ok");
		bOk.addActionListener(this);
		
		JButton bCancel = new JButton ("Cancelar");
		bCancel.setActionCommand("cancel");
		bCancel.addActionListener(this);
		
		panel.add(bOk);
		panel.add(bCancel);
		return panel;
	}

	private void comprobarContraseñas() throws Excepciones.HayCamposIncompletos, Excepciones.ContraseñasNoIguales{
		if(String.valueOf(contraseñaNueva.getPassword()).equals("")) throw new Excepciones.HayCamposIncompletos();
		if(String.valueOf(contraseñaRepetida.getPassword()).equals("")) throw new Excepciones.HayCamposIncompletos();
		if (String.valueOf(contraseñaRepetida.getPassword())
				.equals(String.valueOf(contraseñaNueva.getPassword())) == false) {
			throw new Excepciones.ContraseñasNoIguales();
		}
	}	
	
	public String getNewContraseña(){
		return newContraseña;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("ok")){
			try{
			this.comprobarContraseñas();
			
			newContraseña = String.valueOf(contraseñaNueva.getPassword());
			
			this.dispose();
		
			}catch(Excepciones.HayCamposIncompletos error){
				JOptionPane.showMessageDialog(this,"Rellene todos los campos", "Error",JOptionPane.ERROR_MESSAGE);
			}catch(Excepciones.ContraseñasNoIguales error){
				JOptionPane.showMessageDialog(this,"Las contraseñas no se escribieron igual", "Error",JOptionPane.ERROR_MESSAGE);
			}
		}
		
		if(e.getActionCommand().equals("cancel")){
			this.dispose();
		}
		
	}

}

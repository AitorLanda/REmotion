package popbl3;

public class Excepciones {
	
	public static class HayCamposIncompletos extends Exception{
		
		public HayCamposIncompletos(){
			super();
		}
	}
	
	public static class UsernameRepetido extends Exception{
		
		public UsernameRepetido(){
			super();
		}
	}
	
	public static class UsernameDeFisioIncorrecto extends Exception{
		
		public UsernameDeFisioIncorrecto(){
			super();
		}
	}
	
	public static class ContraseñasNoIguales extends Exception{
		
		public ContraseñasNoIguales(){
			super();
		}
		
	}

}

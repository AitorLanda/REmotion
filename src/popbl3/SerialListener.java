/*package popbl3;

import java.io.IOException;
import java.io.InputStream;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class SerialListener implements SerialPortEventListener {
	
	int END;
	InputStream in;
	PacienteFrame frame = new PacienteFrame();

	public SerialListener (InputStream in) {
		this.in = in;
	}
	@Override
	public void serialEvent(SerialPortEvent arg0) {
		int data;

		byte[] buffer = new byte[1024];
        try
        {
            int len = 0;
            while ( ( data = in.read()) > -1 )
            {
            	System.out.println('\n'+ data);

                if ( data == 61 ) {
                	frame.setTimeout(true);
                	}
                buffer[len++] = (byte) data;
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            System.exit(-1);
        }     
	}

}
*/
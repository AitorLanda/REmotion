package popbl3;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.PointArray;
import javax.media.j3d.PointAttributes;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Vector;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class Mano3D implements Runnable {

	final float COLOR_ROJO[] = { 1, 0, 0 };
	final float COLOR_BLANCO[] = { 1, 1, 1 };
	final float COLOR_VERDE[] = { 0, 1, 0 };
	final float COLOR_AZUL[] = { 0, 0, 1 };
	final float COLOR_AMARILLO[] = { 1, 1, 0 };
	final float COLOR_CELESTE[] = { 0, 1, 1 };
	final float COLOR_MORADO[] = { 1, 0.6f, 1 };
	final float COLOR_NARANJA[] = {1, 0.4f, 0 };
	
	final int NUMERO_PUNTOS_LINEAS = 42;
	final int NUMERO_PUNTOS_PUNTOS = 21;

	final float TAMAÑO_PUNTOS = 12.5f;
	final float TAMAÑO_LINEAS = 7.0f;

	final double RADIANES_90_GRADOS = 1.5708;

	final int TIEMPO_DE_ESPERA = 20;

	final int NUMERO_PUNTOS_POR_LINEA = 2;
	final int NUMERO_LINEAS_POR_DEDO = 3;

	final float FACTOR_DE_REDUCCION = 200.0f;

	Hand manoActual = null;

	SimpleUniverse universo;

	BranchGroup grupo;

	TransformGroup GT = new TransformGroup();
	Transform3D transformar = new Transform3D();

	Thread hilo1 = new Thread(this);

	private PointArray ptsMano;
	private LineArray lineasMano;

	public Mano3D(Canvas3D canvas, Hand mano) {
		manoActual = mano;

		grupo = crearMano(manoActual);
		grupo.compile();

		universo = new SimpleUniverse(canvas);

		hilo1.start();
		GT.setTransform(transformar);

		universo.getViewingPlatform().setNominalViewingTransform();
		universo.addBranchGraph(grupo);
	}

	public BranchGroup crearMano(Hand mano) {
		BranchGroup lineGroup = null;
		try {
			lineGroup = new BranchGroup();
			Appearance aparienciaPulgar = new Appearance();
			Appearance aparienciaLineas = new Appearance();

			aparienciaPulgar.setColoringAttributes(
					new ColoringAttributes(new Color3f(COLOR_VERDE), ColoringAttributes.SHADE_FLAT));
			aparienciaLineas.setColoringAttributes(
					new ColoringAttributes(new Color3f(COLOR_BLANCO), ColoringAttributes.SHADE_FLAT));
			

			lineasMano = new LineArray(NUMERO_PUNTOS_LINEAS,LineArray.COORDINATES);
			ptsMano = new PointArray(NUMERO_PUNTOS_PUNTOS,  GeometryArray.COORDINATES);
			
			lineasMano.setCapability(QuadArray.ALLOW_COLOR_WRITE);
			lineasMano.setCapability(QuadArray.ALLOW_COORDINATE_WRITE);
			lineasMano.setCapability(QuadArray.ALLOW_NORMAL_WRITE);

			
			ptsMano.setCapability(PointArray.ALLOW_COORDINATE_WRITE);
			ptsMano.setCapability(PointArray.ALLOW_COLOR_WRITE);


			PointAttributes a_point_just_bigger = new PointAttributes();
			a_point_just_bigger.setPointSize(TAMAÑO_PUNTOS);
			a_point_just_bigger.setPointAntialiasingEnable(true);

			LineAttributes a_line_just_bigger = new LineAttributes();
			a_line_just_bigger.setLineWidth(TAMAÑO_LINEAS);
			a_line_just_bigger.setLineAntialiasingEnable(true);

			aparienciaPulgar.setPointAttributes(a_point_just_bigger);
			aparienciaLineas.setLineAttributes(a_line_just_bigger);
			
			Shape3D lineas3D = new Shape3D(lineasMano, aparienciaLineas);
			Shape3D puntos3D = new Shape3D(ptsMano, aparienciaPulgar);
	

			GT = new TransformGroup();
			GT.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);			
			
			
			GT.addChild(puntos3D);		
			GT.addChild(lineas3D);
			
			lineGroup.addChild(GT);;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lineGroup;

	}

	

	private void crearNuevaForma() {
		Vector vectorHueso;
		Point3f[] puntos3Dfloat = new Point3f[NUMERO_PUNTOS_PUNTOS];
		int posArrayPuntos = 0;

		crearNuevasLineas();
		if (manoActual != null && manoActual.isValid()) {
			for (Finger dedo : manoActual.fingers()) {

				for (Bone.Type tipoHueso : Bone.Type.values()) {
					Bone hueso = dedo.bone(tipoHueso);
					vectorHueso = hueso.nextJoint();

					puntos3Dfloat[posArrayPuntos] = getPuntosVector(vectorHueso);
					posArrayPuntos++;
				}
			}
			puntos3Dfloat[posArrayPuntos] = getPuntosHueso(Finger.Type.TYPE_PINKY, Bone.Type.TYPE_METACARPAL, false);
		} 
		if(puntos3Dfloat != null && ptsMano!= null){
			ptsMano.setCoordinates(0, puntos3Dfloat);
		}

	}

	private void crearNuevasLineas() {
		Vector vectorHueso;
		Bone hueso;
		Point3f[] puntos3Dfloat = new Point3f[NUMERO_PUNTOS_LINEAS];

		int repeticiones;
		int lineas;
		int posArrayLineas = 0;
	
		if (manoActual != null && manoActual.isValid()) {
			for (Finger dedo : manoActual.fingers()) {
				repeticiones = 1;
				lineas = 0;
				for (Bone.Type tipoHueso : Bone.Type.values()) {					
					do {
						repeticiones++;
						hueso = dedo.bone(tipoHueso);
						vectorHueso = hueso.nextJoint();		
						puntos3Dfloat[posArrayLineas] = getPuntosVector(vectorHueso);
						posArrayLineas++;
					} while (repeticiones < NUMERO_PUNTOS_POR_LINEA && lineas <NUMERO_LINEAS_POR_DEDO);
					lineas ++;
					repeticiones = 0;
				}
			}
			repeticiones = 1;
			for(Finger dedo : manoActual.fingers()){
				do{
				repeticiones++;
				hueso = dedo.bone(Bone.Type.TYPE_METACARPAL);
				vectorHueso = hueso.nextJoint();		
				puntos3Dfloat[posArrayLineas] = getPuntosVector(vectorHueso);
				posArrayLineas++;
				
				}while(repeticiones<NUMERO_PUNTOS_POR_LINEA);
				repeticiones =0;
			}
			puntos3Dfloat[posArrayLineas++] = getPuntosHueso(Finger.Type.TYPE_PINKY, Bone.Type.TYPE_METACARPAL, false);
			puntos3Dfloat[posArrayLineas++] = getPuntosHueso(Finger.Type.TYPE_PINKY, Bone.Type.TYPE_METACARPAL, false);
			puntos3Dfloat[posArrayLineas++] = getPuntosHueso(Finger.Type.TYPE_THUMB, Bone.Type.TYPE_METACARPAL, true);
			
			
		}	
		lineasMano.setCoordinates(0, puntos3Dfloat);
	}
	private Point3f getPuntosHueso(Finger.Type tipoDedo,Bone.Type tipoHueso,boolean nextJoint){
		Point3f nuevoPunto = null;
		Vector vectorHueso = null;
		for(Finger dedo : manoActual.fingers()){
			if(dedo.type() == tipoDedo){
				if(nextJoint)
					vectorHueso = dedo.bone(tipoHueso).nextJoint();
				else
					vectorHueso = dedo.bone(tipoHueso).prevJoint();
				nuevoPunto = getPuntosVector(vectorHueso);	
				}
		}
		return nuevoPunto;
	}
	private Point3f getPuntosVector(Vector vector){
		Point3f nuevoPunto = new Point3f(	vector.getX() /FACTOR_DE_REDUCCION,
				vector.getY() /FACTOR_DE_REDUCCION,
				vector.getZ() /FACTOR_DE_REDUCCION);
		
		return nuevoPunto;
	}
	public void setHand(Hand mano) {
		manoActual = mano;
	}
	
	@Override
	public void run() {
		Thread ct = Thread.currentThread();
		while (ct == hilo1) {
			try {				
				transformar.rotX(RADIANES_90_GRADOS);			

				crearNuevaForma();		

				GT.setTransform(transformar);			

				Thread.sleep(TIEMPO_DE_ESPERA);
					
			} catch (InterruptedException ex) {
				Logger.getLogger(Mano3D.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}


}



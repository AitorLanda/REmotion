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
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class Puntos implements Runnable {
	
	final float COLOR_ROJO[] = { 1, 0, 0 };
	final float COLOR_BLANCO[] = { 1, 1, 1 };
	final float COLOR_VERDE[] = { 0, 1, 0 };
	final float COLOR_AZUL[] = { 0, 0, 1 };
	final float COLOR_AMARILLO[] = { 1, 1, 0 };
	final float COLOR_CELESTE[] = {0,1,1};
	final float COLOR_MORADA[] = {1,0.6f,1};
	Hand manoActual = null, manoDibujada = null;

	SimpleUniverse universo;
	BranchGroup grupo;
	ColorCube cubo = new ColorCube(0.3);
	TransformGroup GT = new TransformGroup();
	Transform3D transformar = new Transform3D();

	Thread hilo1 = new Thread(this);

	private PointArray ptsMano;

	// private Shape3D forma3D;
	private LineArray lineasMano;

	public Puntos(Canvas3D canvas, Hand mano) {
		manoActual = mano;

		grupo = crearMano(manoActual);
		
		manoDibujada = manoActual;
		
		grupo.compile();

		universo = new SimpleUniverse(canvas);

		hilo1.start();

		GT.setTransform(transformar);

		universo.getViewingPlatform().setNominalViewingTransform();

		universo.addBranchGraph(grupo);
	}

	public BranchGroup crearMano(Hand mano) {
		try {

			BranchGroup lineGroup = new BranchGroup();
			Appearance aparienciaPulgar = new Appearance();
			Appearance aparienciaLineas = new Appearance();

			aparienciaPulgar.setColoringAttributes(
					new ColoringAttributes(new Color3f(COLOR_BLANCO), ColoringAttributes.SHADE_FLAT));

			aparienciaLineas.setColoringAttributes(
					new ColoringAttributes(new Color3f(COLOR_BLANCO), ColoringAttributes.SHADE_FLAT));

			int count = 30;

			lineasMano = new LineArray(count, LineArray.COORDINATES);

			lineasMano.setCapability(QuadArray.ALLOW_COLOR_WRITE);
			lineasMano.setCapability(QuadArray.ALLOW_COORDINATE_WRITE);
			lineasMano.setCapability(QuadArray.ALLOW_NORMAL_WRITE);

			ptsMano = new PointArray(20, GeometryArray.COLOR_3 | GeometryArray.COORDINATES);
			ptsMano.setCapability(PointArray.ALLOW_COORDINATE_WRITE);
			ptsMano.setCapability(PointArray.ALLOW_COLOR_WRITE);

			for (int i = 0; i < 20; i++) {
			
					ptsMano.setColors(i, COLOR_MORADA);

			}

			PointAttributes a_point_just_bigger = new PointAttributes();
			a_point_just_bigger.setPointSize(10.0f);
			a_point_just_bigger.setPointAntialiasingEnable(true);

			LineAttributes a_line_just_bigger = new LineAttributes();
			a_line_just_bigger.setLineWidth(7.0f);
			a_line_just_bigger.setLineAntialiasingEnable(true);

			aparienciaPulgar.setPointAttributes(a_point_just_bigger);
			aparienciaLineas.setLineAttributes(a_line_just_bigger);
			Shape3D lineas3D = new Shape3D(lineasMano, aparienciaLineas);
			Shape3D forma3D = new Shape3D(ptsMano, aparienciaPulgar);
	

			GT = new TransformGroup();

			GT.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			
			GT.addChild(lineas3D);
			GT.addChild(forma3D);
		

			lineGroup.addChild(GT);
			return lineGroup;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	private double rotacionX = 1.5708;
	@Override
	public void run() {

		Thread ct = Thread.currentThread();
		while (ct == hilo1) {
			try {
				//y = y + 0.05;

				transformar.rotX(rotacionX);

				crearNuevaForma();

				GT.setTransform(transformar);

				Thread.sleep(20);
			} catch (InterruptedException ex) {
				Logger.getLogger(Puntos.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	private void crearNuevaForma() {
		Vector vectorHueso;
		Point3f[] puntos3Dfloat = new Point3f[20];
		int i = 0;

		crearNuevasLineas();
		if (manoActual != null && manoActual.isValid()) {
			for (Finger dedo : manoActual.fingers()) {

				for (Bone.Type tipoHueso : Bone.Type.values()) {

					Bone hueso = dedo.bone(tipoHueso);
					vectorHueso = hueso.nextJoint();

					puntos3Dfloat[i] = new Point3f(vectorHueso.getX() / 200.0f, vectorHueso.getY() / 200.0f,
							vectorHueso.getZ() / 200.0f);
					i++;
					// System.out.println(vectorHueso.getX()+"/"+vectorHueso.getY()+"/"+vectorHueso.getZ());
				}
			}
		} else
			for (int j = 0; i < 30; i++) {

				puntos3Dfloat[i] = new Point3f(0, 0, 0);
				j++;
			}

		ptsMano.setCoordinates(0, puntos3Dfloat);

	}

	private void crearNuevasLineas() {
		Vector vectorHueso;

		Point3f[] puntos3Dfloat = new Point3f[30];

		int i = 0;

		int repeticiones = 0;
		int lineas = 0;
		if (manoActual != null && manoActual.isValid()) {
			for (Finger dedo : manoActual.fingers()) {
				repeticiones = 1;
				lineas = 0;
				for (Bone.Type tipoHueso : Bone.Type.values()) {
					
					do {
						repeticiones++;
						Bone hueso = dedo.bone(tipoHueso);
						vectorHueso = hueso.nextJoint();

						puntos3Dfloat[i] = new Point3f(vectorHueso.getX() / 200.0f, vectorHueso.getY() / 200.0f,
								vectorHueso.getZ() / 200.0f);
						i++;
					} while (repeticiones < 2 && lineas <3);
					
					lineas ++;
					repeticiones = 0;
					//System.out.println("lineas "+ lineas +" repeticiones " +repeticiones + " i "+i);
				}
			}
		}
		lineasMano.setCoordinates(0, puntos3Dfloat);
		// lineasMano.setNormals(0, normals);
	}

	public void setHand(Hand mano) {
		manoActual = mano;
	}

}

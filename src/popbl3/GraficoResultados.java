package popbl3;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

public class GraficoResultados {
	public Calendar calendar;
	public List<Ejercicio> listEjercicios;
	static public final int BIEN = 0;
	static public final int CASI = 1;
	private static final int MINUTOS = 0;
	private DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
	private DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
	private DefaultCategoryDataset dataset3 = new DefaultCategoryDataset();
	private DefaultCategoryDataset dataset4 = new DefaultCategoryDataset();
	public CategoryPlot plot;
	public ChartPanel chartPanel;

	public GraficoResultados(List<Ejercicio> ejercicios) {
		this.listEjercicios = ejercicios;
	}

	public Component crearPanelGraficoDia() {

		int i, numEjercicios;

		numEjercicios = listEjercicios.size();

		for (i = 0; i < numEjercicios; i++) {
			dataset1.addValue(listEjercicios.get(i).getMaxRepeticiones(), "Objetivo",listEjercicios.get(i).getNombre());
			dataset2.addValue(listEjercicios.get(i).getValorResultado(CASI), "Casi", listEjercicios.get(i).getNombre());
			dataset3.addValue(listEjercicios.get(i).getValorResultado(BIEN), "Bien", listEjercicios.get(i).getNombre());
		}
		crearGraficos();
		
		return chartPanel;
	}

	public Component crearPanelGraficoMes(String ejercicio) {

		int i, numEjercicios;

		numEjercicios = listEjercicios.size();

		for (i = 0; i < numEjercicios; i++) {
			if (ejercicio.equals(listEjercicios.get(i).getNombre())) {
				dataset1.addValue(listEjercicios.get(i).getMaxRepeticiones(), "Objetivo", listEjercicios.get(i).getFecha());
				dataset2.addValue(listEjercicios.get(i).getValorResultado(CASI), "Casi", listEjercicios.get(i).getFecha());
				dataset3.addValue(listEjercicios.get(i).getValorResultado(BIEN), "Bien", listEjercicios.get(i).getFecha());
				String tiempo[] = listEjercicios.get(i).getTiempo().split(":");
				dataset4.addValue(Integer.parseInt(tiempo[MINUTOS]), "Tiempo", listEjercicios.get(i).getFecha());
			}
		}
		crearGraficos();
	
		final ValueAxis rangeAxis2 = new NumberAxis("Tiempo (minutos)");
		plot.setRangeAxis(1, rangeAxis2);

		plot.setDataset(3, dataset4);
		final CategoryItemRenderer renderer4 = new LineAndShapeRenderer();
		renderer4.setSeriesPaint(0, new Color(139, 69, 19));
		plot.setRenderer(3, renderer4);

		return chartPanel;
	}

	public void crearGraficos() {
		final CategoryItemRenderer renderer = new BarRenderer();
		renderer.setItemLabelsVisible(true);

		plot = new CategoryPlot();
		plot.setDataset(dataset1);
		plot.setRenderer(renderer);

		plot.setDomainAxis(new CategoryAxis("Ejercicios"));
		plot.setRangeAxis(new NumberAxis("Número de intentos"));

		plot.setOrientation(PlotOrientation.VERTICAL);
		plot.setRangeGridlinesVisible(true);
		plot.setDomainGridlinesVisible(true);

		final CategoryItemRenderer renderer2 = new LineAndShapeRenderer();
		plot.setDataset(1, dataset2);
		plot.setRenderer(1, renderer2);

		plot.setDataset(2, dataset3);
		final CategoryItemRenderer renderer3 = new LineAndShapeRenderer();
		plot.setRenderer(2, renderer3);

		plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
		
		plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		final JFreeChart chart = new JFreeChart(plot);

		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
	}
}
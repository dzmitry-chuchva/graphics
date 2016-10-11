package giis.labs.base.impl;

import giis.labs.base.api.IWorkArea;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JToolBar;

public abstract class AbstractArea extends JComponent implements IWorkArea {
	
	private static final int reportAreaHeight = 30;
	private static final int serifSize = 5;
	private static final int arrowSize = 10;
	private static final int axisNumberCount = 5;
	private static final int numbersLineThicness = 15; // pad for numbers about maximum number string width
	
	private static final Color compColorUp = new Color(129,182,248);
	private static final Color compColorDown = new Color(201,224,252);
	private static final Color areaColor = Color.WHITE;
	private static final Color xAxisColor = Color.BLACK;
	private static final Color yAxisColor = Color.BLACK;
	private static final Color gridColor = Color.LIGHT_GRAY;
	private static final Color contourColor = Color.BLACK;
	
	private int cellSize;

	private int sideSize;

	private int originX, originY;

	private Graphics2D cachedGraphics;
	private Graphics componentCachedGraphics;
	private String currentMessage = "";
	
	protected JMenu[] menuBar = null;
	protected JToolBar toolBar = null;
	
	private String shortDescription = "Abstract";
	private String longDescription = "AbstractArea";
	
	private BufferedImage image = null;
	
	public AbstractArea() {
		setFocusable(true);
	}
	
	public JComponent getComponent() {
		return this;
	}

	public JMenu[] getMenuBar() {
		return menuBar;
	}
	
	public JToolBar getToolBar() {
		return toolBar;
	}
	
	protected JComboBox createActionsCombo(LabsAction[] actions) {
		JComboBox box = new JComboBox(actions);
		ActionComboRenderer renderer = new ActionComboRenderer();
		renderer.setPreferredSize(new Dimension(100,26));
		box.setRenderer(renderer);		
		box.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				JComboBox cb = (JComboBox)arg0.getSource();
		        LabsAction action = (LabsAction)cb.getSelectedItem();
		        action.putValue(LabsAction.SELECTED_KEY, true);
		        action.actionPerformed(arg0);
			}
			
		});
		
		return box;
	}
	
	public void paintComponent(Graphics componentGraphics) {
		// save component graphics for using in endPaint()
		componentCachedGraphics = componentGraphics;
		
		Graphics g = image.createGraphics();
		Graphics2D g2 = (Graphics2D)g;
		cachedGraphics = g2;
		
		Stroke defaultStroke = g2.getStroke();
		BasicStroke thick = new BasicStroke(2.0f);
		Font defaultFont = g.getFont();
		Font thickFont = new Font("Fixedsys",Font.BOLD,14);
		Font thinFont = new Font("Courier",Font.PLAIN,12);

		// фон
		Paint oldPaint = g2.getPaint();
		GradientPaint gp = new GradientPaint(0f,0f,compColorUp,0f,getHeight(),compColorDown);
		g2.setPaint(gp);
		g2.fillRect(0, 0, getWidth(), getHeight());
		g2.setPaint(oldPaint);

		// заливаем рабочую область
		g.setColor(areaColor);
		g.fillRect(originX, originY, sideSize, sideSize);

		// рисуем букву Y
		g.setColor(yAxisColor);
		g.setFont(thickFont);
		g.drawString("Y", originX + serifSize, originY + sideSize + g.getFontMetrics().getAscent());
				
		// рисуем ось Y
		g.setColor(contourColor);
		g2.setStroke(thick);
		g2.drawLine(originX, originY - serifSize, originX, originY + sideSize + serifSize + arrowSize);
				
		// рисуем стрелку оси Y
		g.drawLine(originX, originY + sideSize + serifSize + arrowSize,originX - serifSize, originY + sideSize + serifSize);
		g.drawLine(originX, originY + sideSize + serifSize + arrowSize,originX + serifSize, originY + sideSize + serifSize);
		g2.setStroke(defaultStroke);
		g.setFont(thinFont);
		
		// рисуем "0" в начале координат
		g.setColor(contourColor);
		g.drawString("0", originX - serifSize - g.getFontMetrics().stringWidth("0"), originY - serifSize);
		
		// рисуем вертикальные линии сетки 
		int interval = axisNumberCount;
		int intervalCounter = 0;
		for (int cx = originX + cellSize; cx <= originX + sideSize; cx += cellSize, intervalCounter++) {
			// собственно лини€
			if (cx != originX + sideSize) {
				g.setColor(gridColor);
			} else {
				g.setColor(contourColor);
			}
			g.drawLine(cx, originY, cx, originY + sideSize - 1);
			// засечка
			g.setColor(Color.BLACK);
			g.drawLine(cx, originY - serifSize, cx, originY);
			if (intervalCounter != 0 && intervalCounter % interval == 0) {
				String output = String.valueOf(intervalCounter);
				g.drawString(output, cx - cellSize, originY - serifSize);
				g2.setStroke(thick);
				g.drawLine(cx - cellSize, originY - serifSize, cx - cellSize, originY);
				g2.setStroke(defaultStroke);
			}
		}

		// рисуем букву "’"
		g2.setStroke(thick);
		g.setFont(thickFont);
		g.setColor(xAxisColor);
		g.drawString("X", originX + sideSize + serifSize, originY + g.getFontMetrics().getAscent());
		// рисуем ось ’
		g.setColor(contourColor);
		g.drawLine(originX - serifSize, originY, originX + sideSize + serifSize + arrowSize, originY);
	
		// рисуем стрелку оси ’
		g.drawLine(originX + sideSize + serifSize + arrowSize, originY, originX + sideSize + serifSize, originY + serifSize);
		g.drawLine(originX + sideSize + serifSize + arrowSize, originY, originX + sideSize + serifSize, originY - serifSize);
		
		// рисуем горизонтальные линии сетки
		g2.setStroke(defaultStroke);
		g.setFont(thinFont);
		intervalCounter = 0;
		for (int cy = originY + cellSize; cy <= originY + sideSize; cy += cellSize, intervalCounter++) {
			// собственно лини€
			if (cy != originY + sideSize) {
				g.setColor(gridColor);
			} else {
				g.setColor(contourColor);
			}
			g.drawLine(originX, cy, originX + sideSize - 1, cy);
			// засечка			
			g.setColor(Color.BLACK);
			g.drawLine(originX - serifSize, cy, originX, cy);
			if (intervalCounter != 0 && intervalCounter % interval == 0) {
				String output = String.valueOf(intervalCounter);
				g.drawString(output, originX - serifSize - g.getFontMetrics().stringWidth(output), cy - cellSize);
				g2.setStroke(thick);
				g.drawLine(originX - serifSize, cy - cellSize, originX, cy - cellSize);
				g2.setStroke(defaultStroke);
			}
		}
		g.setFont(defaultFont);
	}
	
	protected void endPaintComponent() {
		cachedGraphics.setColor(Color.BLACK);
		cachedGraphics.drawString(currentMessage, originX, 2 * reportAreaHeight / 3);
		cachedGraphics = null;
		
		// flip page
		componentCachedGraphics.drawImage(image, 0, 0, null);
		componentCachedGraphics = null;
	}

	public void plot(Point p, Color c) {
		plot(p.x, p.y,c);
	}
	
	public void plot(int x, int y, Color c) {
		if (cachedGraphics != null) {
			int cells = getSideSize() / getCellSize();
			if (x >= 0 && x < cells && y >= 0 && y < cells) {
				int cellX = x * cellSize;
				int cellY = y * cellSize;
				
				cachedGraphics.setColor(c);
				cachedGraphics.fillRect(originX + cellX + 1, originY + cellY + 1, cellSize - 1, cellSize - 1);
			}
		}
	}

	protected Point translate(Point screenPoint) {
		Point res = new Point(screenPoint);
		res.x = screenPoint.x - originX;
		res.y = screenPoint.y - originY;

		if (res.x >= sideSize) {
			res.x = sideSize - 1;
		} else if (res.x < 0) {
			res.x = 0;
		}
		
		if (res.y >= sideSize) {
			res.y = sideSize - 1;
		} else if (res.y < 0) {
			res.y = 0;
		}
		
		res.x /= cellSize;
		res.y /= cellSize;
		return res;
	}
	
	public int getCellSize() {
		return cellSize;
	}

	public void setCellSize(int cellSize) {
		this.cellSize = cellSize;
	}
	
	protected void recalcSize() {
		Insets insets = getInsets();
		int currentWidth = getWidth() - insets.left - insets.right - 1;
		int currentHeight = getHeight() - insets.top - insets.bottom - 1;
		int x = insets.left;
		int y = insets.top;

		// make a square area
		sideSize = currentHeight - reportAreaHeight - 2 * serifSize - arrowSize - numbersLineThicness;
		if (sideSize > currentWidth - 2 * serifSize - arrowSize - numbersLineThicness) {
			sideSize = currentWidth - 2 * serifSize - arrowSize - numbersLineThicness;
		}

		sideSize = sideSize - sideSize % cellSize;

		originX = (currentWidth - sideSize - 2 * serifSize - arrowSize - numbersLineThicness) / 2 + serifSize + numbersLineThicness;
		originY = (currentHeight - sideSize - reportAreaHeight - 2 * serifSize - arrowSize - numbersLineThicness) / 2 + reportAreaHeight + serifSize + numbersLineThicness;

		image = getGraphicsConfiguration().createCompatibleImage(getWidth(), getHeight());
	}

	public void componentHidden(ComponentEvent arg0) {
	}

	public void componentMoved(ComponentEvent arg0) {
	}

	public void componentResized(ComponentEvent arg0) {
		recalcSize();
		repaint();
	}

	public void componentShown(ComponentEvent arg0) {
		recalcSize();
	}

	public void mouseClicked(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {
	}

	public void mouseDragged(MouseEvent arg0) {
	}

	public void mouseMoved(MouseEvent arg0) {
	}

	protected int getOriginX() {
		return originX;
	}

	protected void setOriginX(int originX) {
		this.originX = originX;
	}

	protected int getOriginY() {
		return originY;
	}

	protected void setOriginY(int originY) {
		this.originY = originY;
	}

	public int getSideSize() {
		return sideSize;
	}

	public String getCurrentMessage() {
		return currentMessage;
	}

	public void setCurrentMessage(String currentMessage) {
		this.currentMessage = currentMessage;
	}
	
	public String getLongDescription() {
		return longDescription;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	protected void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	protected void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public void line(int x1, int y1, int x2, int y2, Color c) {
		if (cachedGraphics != null) {
			privateBresenhamLine(x1, y1, x2, y2, c);
		}
	}

	public void line(Point p1, Point p2, Color c) {
		line(p1.x,p1.y,p2.x,p2.y,c);		
	}
	
	private void privateBresenhamLine(int x1, int y1, int x2, int y2, Color c) {
		// локальна€ реализаци€ алгоритма Ѕрезенхама
		boolean steep = Math.abs(y2 - y1) > Math.abs(x2 - x1);
		if (steep) {
			int tmp = x1;
			x1 = y1;
			y1 = tmp;
			
			tmp = x2;
			x2 = y2;
			y2 = tmp;
		}
		if (x1 > x2) {
			int tmp = x1;
			x1 = x2;
			x2 = tmp;
			
			tmp = y1;
			y1 = y2;
			y2 = tmp;
		}
		int dx = x2 - x1;
		int dy = Math.abs(y2 - y1);
		int error = - (dx >> 1);
		int ystep;
		if (y1 < y2) {
			ystep = 1;
		} else {
			ystep = -1;
		}
		
		int y = y1;
		for (int x = x1; x <= x2; x++) {
			if (steep) {
				plot(y, x, c);
			} else {
				plot(x, y, c);
			}
			error += dy;
			if (error > 0) {
				y += ystep;
				error -= dx;
			}
		}
	}
	
	public Color getPixel(Point p) {
		return getPixel(p.x,p.y);
	}
	
	public Color getPixel(int x, int y) {
		if (image != null) {
			int cells = getSideSize() / getCellSize();
			if (x >= 0 && x < cells && y >= 0 && y < cells) {
				int cellX = x * cellSize;
				int cellY = y * cellSize;
				
				return new Color(image.getRGB(originX + cellX + 1, originY + cellY + 1));
			}
		}
		return Color.WHITE;
	}
	
}

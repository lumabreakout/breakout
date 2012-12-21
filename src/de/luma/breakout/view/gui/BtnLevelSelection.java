package de.luma.breakout.view.gui;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class BtnLevelSelection extends JButton {

	private String filePath;
	private IGuiManager guiManager;
	private Rectangle2D stringDimension;
	
	public BtnLevelSelection(String filepath, IGuiManager guiMgr) {
		super();
		this.filePath = filepath;
		this.guiManager = guiMgr;
		this.setPreferredSize(new Dimension(80, 80));
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setFont(IGuiManager.BUTTON_FONT.deriveFont(40.0F));
		FontMetrics fm = g2d.getFontMetrics();
		if (stringDimension == null) {			
			stringDimension = fm.getStringBounds(this.getText(), g2d);
		}
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(this.getBackground());	
		g2d.drawImage(guiManager.getGameImage("resources/levelbutton.png"), 0, 0, getWidth(), getHeight(), this);
		g2d.setColor(IGuiManager.BUTTON_COLOR);
		
		
		g2d.drawString(this.getText(), 
				(int) (this.getWidth() - stringDimension.getWidth()) / 2, 
				(int) (this.getHeight() - stringDimension.getHeight()) / 2 + fm.getAscent());	
	}
	
	public String getFilePath() {
		return this.filePath;
	}
	
}

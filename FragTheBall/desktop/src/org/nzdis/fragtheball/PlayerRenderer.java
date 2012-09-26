package org.nzdis.fragtheball;

import org.nzdis.fragme.ControlCenter;
import org.nzdis.fragtheball.Player;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Vector;

import javax.swing.JPanel;



public class PlayerRenderer extends JPanel {

	private static final long serialVersionUID = 4305993006244918732L;
	
	public boolean isRunning = true;
	
	private float screenWidth = 500.0f;
	private float screenHeight = 500.0f;
	private static float borderThickness = 5.0f;
	private float arenaWidth = screenWidth - borderThickness * 2.0f;
	private float arenaHeight = screenHeight - borderThickness * 2.0f;
	private static int sphereSize = 20;
	private static float sphereSizeHalf = (float)sphereSize * 0.5f;
	
	private FragMePlayer myFmPlayer;
	
	public PlayerRenderer(Player player) {
		myFmPlayer = player.fmPlayer;
		this.setBackground(Color.YELLOW);
	}
	
	
	public void paint(Graphics g) {
		if (!isRunning)
			return;
		
	    super.paintComponent(g);

	    // Render all balls
		Vector<FragMePlayer> fragmePlayers = (Vector<FragMePlayer>)ControlCenter.getAllObjects(FragMePlayer.class).clone();
		
		for (FragMePlayer fmPlayer : fragmePlayers) {
		    if (fmPlayer == myFmPlayer)
		    	g.setColor(Color.BLUE);
		    else 
		    	g.setColor(Color.RED);
		    g.fillOval((int)(fmPlayer.getPositionX() * arenaWidth + borderThickness + sphereSizeHalf), 
		    		(int)(fmPlayer.getPositionY() * arenaHeight + borderThickness + sphereSizeHalf), 
		    		sphereSize, sphereSize);
		}
	}

}

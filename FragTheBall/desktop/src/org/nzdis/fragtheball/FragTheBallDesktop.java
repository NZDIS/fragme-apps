package org.nzdis.fragtheball;

import java.util.Random;

import org.nzdis.fragme.ControlCenter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;



public class FragTheBallDesktop {

	// GUI
	private JFrame frame = new JFrame();
	private JPanel actionPanel = new JPanel();
	private JButton randomiseButton = new JButton("Randomise");
	private JButton quitButton = new JButton("Quit");
	
	// Stuff
	private boolean isRunning = true;
	Random rng = new Random();
	private PlayerRenderer playerRenderer;
	
	
	
	// Startup
	public static void main(String args[]){
		new FragTheBallDesktop();
	}
	
	

	//constructor
	public FragTheBallDesktop() {
		System.out.println("Starting");

		// Setup FragMe
		ControlCenter.setUpConnections("testGroupNathan", String.format("testDesktop%d", rng.nextInt(1000)));
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// !!! Need callback when setup complete
		
		// Setup GUI
		buildTheGUI();

		while(isRunning) {
			frame.repaint();
        	try {
				Thread.sleep(1000/30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		ControlCenter.closeUpConnections();
		System.out.println("Finished");
		System.exit(0);
	}

	
	
	public void buildTheGUI() {
	    frame.setLayout(new BorderLayout());
	    randomiseButton.addActionListener(new RandomiseButtonClickHandler());
	    quitButton.addActionListener(new QuitButtonClickHandler());
	    actionPanel.add(randomiseButton);
	    actionPanel.add(quitButton);
	    frame.add(BorderLayout.NORTH, actionPanel);
	    frame.setSize(600, 600);
	    frame.setLocation(100, 100);
	    frame.setVisible(true);
	    actionPanel.setBackground(Color.CYAN);
	    frame.setBackground(Color.RED);
	    playerRenderer = new PlayerRenderer();
	    frame.add(playerRenderer);
	    frame.validate();
	}
	
	class RandomiseButtonClickHandler implements ActionListener {
	    public void actionPerformed(ActionEvent e) { 
	    	playerRenderer.player.randomiseAcceleration();
	    }
	}

	class QuitButtonClickHandler implements ActionListener {
	    public void actionPerformed(ActionEvent e) { 
	        // Signal shutdown
	    	isRunning = false;
	    	playerRenderer.isRunning = false;
	    }
	}

}

package org.nzdis.fragtheball;

import java.util.Random;

import org.nzdis.fragme.ControlCenter;
import org.nzdis.fragme.util.NetworkUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;



public class FragTheBallDesktop {

	// Parameters
	private boolean guiIsEnabled;
	private int updatesPerSecond = 30;
	private int secondsPerRandomise = 20;
	
	// GUI
	private JFrame frame = new JFrame();
	private JPanel actionPanel = new JPanel();
	private JButton randomiseButton = new JButton("Randomise");
	private JButton quitButton = new JButton("Quit");
	
	// The Player
	public Player player;

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

		String address = NetworkUtils.getNonLoopBackAddressByProtocol(NetworkUtils.IPV4);
		System.out.println("Using address: " + address);
		
		String peerName = String.format("testDesktop%d", rng.nextInt(1000));
		
		// Setup FragMe
		ControlCenter.setUpConnections("testGroup3", peerName, address);
		
		// Initialize this player
		player = new Player();
		player.randomiseAcceleration();
		
		if (ControlCenter.getNoOfPeers() == 0) {
			guiIsEnabled = true;
		} else {
			guiIsEnabled = false;
		}
		
		if (guiIsEnabled) {
			// Setup GUI
			buildTheGUI();

			while(isRunning) {
			    // Update myself
				player.update();

				// Tell java that we want the screen repainted (it will be done at some point in the near future)
				frame.repaint();
				
	        	try {
					Thread.sleep(1000/updatesPerSecond);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else {
			// Console only - automate randomisation every few seconds
			int c = secondsPerRandomise*updatesPerSecond;
			
			while(isRunning) {
				c--;
			    // Update myself
				if (ControlCenter.getNoOfPeers() > 0) {
					if (player.fmPlayer.getOwnerAddr().equals(ControlCenter.getMyAddress())) {
						System.out.println("I own my object");
						for (Object otherObject : ControlCenter.getAllObjects(FragMePlayer.class)) {
							FragMePlayer otherPlayer = (FragMePlayer)otherObject;
							if (!otherPlayer.getOwnerAddr().equals(ControlCenter.getMyAddress())) {
								System.out.println("I found someone to give it to");
								player.fmPlayer.delegateOwnership(otherPlayer.getOwnerName());
								break;
							}
						}
					}
				}
				player.update();

				if (c < 0) {
					player.randomiseAcceleration();
					c = secondsPerRandomise*updatesPerSecond;
				}
				
	        	try {
					Thread.sleep(1000/updatesPerSecond);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
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
	    playerRenderer = new PlayerRenderer(player);
	    frame.add(playerRenderer);
	    frame.validate();
	}
	
	
	
	class RandomiseButtonClickHandler implements ActionListener {
	    public void actionPerformed(ActionEvent e) { 
	    	player.randomiseAcceleration();
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

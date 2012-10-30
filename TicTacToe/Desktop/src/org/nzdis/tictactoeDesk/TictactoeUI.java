/**
 * 
 */
package org.nzdis.tictactoeDesk;

import java.awt.Button;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import org.nzdis.fragme.ControlCenter;
import org.nzdis.fragme.helpers.StartupWaitForObjects;
import org.nzdis.fragme.util.NetworkUtils;
import org.nzdis.tictactoeDesk.TicTacToeModel;

/**
 *  TicTacToe game interface
 *
 */
public class TictactoeUI extends Frame implements Observer {

	private TicTacToeModel tModel;
	private int player;
	private Panel jContentPane = null;
	private Button btn1 = null;
	private Button btn2 = null;
	private Button btn3 = null;
	private Button btn4 = null;
	private Button btn5 = null;
	private Button btn6 = null;
	private Button btn7 = null;
	private Button btn8 = null;
	private Button btn9 = null;
	private Button[] buttons = new Button[10];
	private Label lblPlayer = null;
	private Label lblWinMsg = null;
	private Button btnRestart = null;

	/**
	 * This is the default constructor
	 */
	public TictactoeUI() {
		//initialize interface
		initialize();
		initialConnection();
	}
	
	public void initialConnection(){
		//set connection address
		String address = NetworkUtils.getNonLoopBackAddressByProtocol(NetworkUtils.IPV4);
		if (address == null) {
			System.out.println("Could not find a local ip address");
			return;
		}
		//set peer name
		String username = System.getProperty("user.name");
		//set up connection
		ControlCenter.setUpConnectionsWithHelper("TicTacToe", username, address, new StartupWaitForObjects(1));
		//get shared object
		Vector shareObjects = ControlCenter.getAllObjects();
		if (shareObjects.size() == 0) {
			tModel=new TicTacToeModel();
			this.tModel = (TicTacToeModel) ControlCenter
					.createNewObject(TicTacToeModel.class);
			System.out.println("Create new tictactoemodel:"	+ this.tModel.toString());
			this.player = 1;
			System.out.println("I am player 1 (0)");
			lblPlayer.setText("Player 1 (0)");
		} else {
			this.tModel = (TicTacToeModel) shareObjects.get(0);
			System.out.println("Joining the existing game:"
					+ shareObjects.get(0).toString());
			this.player = 2;
			System.out.println("I am player 2 (X).");
			lblPlayer.setText("Player 2 (X)");
		}
		this.tModel.addObserver(this);
		this.update(tModel, null);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.add(getJContentPane());
		this.setTitle("TicTacToe Example");
		this.setBounds(new Rectangle(20, 20, 253, 428));
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return java.awt.Panel
	 */
	private Panel getJContentPane() {
		if (jContentPane == null) {
			lblWinMsg = new Label();
			lblWinMsg.setBounds(new Rectangle(15, 281, 209, 19));
			lblWinMsg.setText("");
			lblPlayer = new Label();
			lblPlayer.setBounds(new Rectangle(15, 20, 163, 28));
			lblPlayer.setText("");
			jContentPane = new Panel();
			jContentPane.setLayout(null);
			jContentPane.add(getBtn1(), null);
			jContentPane.add(getBtn2(), null);
			jContentPane.add(getBtn3(), null);
			jContentPane.add(getBtn4(), null);
			jContentPane.add(getBtn5(), null);
			jContentPane.add(getBtn6(), null);
			jContentPane.add(getBtn7(), null);
			jContentPane.add(getBtn8(), null);
			jContentPane.add(getBtn9(), null);
			jContentPane.add(lblPlayer, null);
			jContentPane.add(lblWinMsg, null);
			jContentPane.add(getBtnRestart(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes btn1
	 * 
	 * @return java.awt.Button
	 */
	private Button getBtn1() {
		if (btn1 == null) {
			btn1 = new Button();
			btn1.setBounds(new Rectangle(14, 60, 61, 60));
			btn1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					btnPressed(1);
				}
			});
		}
		buttons[1] = btn1;
		return btn1;
	}

	/**
	 * This method initializes btn2
	 * 
	 * @return java.awt.Button
	 */
	private Button getBtn2() {
		if (btn2 == null) {
			btn2 = new Button();
			btn2.setBounds(new Rectangle(89, 59, 62, 62));
			btn2.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					btnPressed(2);
				}
			});
		}
		buttons[2] = btn2;
		return btn2;
	}

	/**
	 * This method initializes btn3
	 * 
	 * @return java.awt.Button
	 */
	private Button getBtn3() {
		if (btn3 == null) {
			btn3 = new Button();
			btn3.setBounds(new Rectangle(163, 60, 62, 61));
			btn3.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					btnPressed(3);
				}
			});
		}
		buttons[3] = btn3;
		return btn3;
	}

	/**
	 * This method initializes btn4
	 * 
	 * @return java.awt.Button
	 */
	private Button getBtn4() {
		if (btn4 == null) {
			btn4 = new Button();
			btn4.setBounds(new Rectangle(15, 134, 61, 61));
			btn4.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					btnPressed(4);
				}
			});
		}
		buttons[4] = btn4;
		return btn4;
	}

	/**
	 * This method initializes btn5
	 * 
	 * @return java.awt.Button
	 */
	private Button getBtn5() {
		if (btn5 == null) {
			btn5 = new Button();
			btn5.setBounds(new Rectangle(89, 134, 62, 61));
			btn5.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					btnPressed(5);
				}
			});
		}
		buttons[5] = btn5;
		return btn5;
	}

	/**
	 * This method initializes btn6
	 * 
	 * @return java.awt.Button
	 */
	private Button getBtn6() {
		if (btn6 == null) {
			btn6 = new Button();
			btn6.setBounds(new Rectangle(164, 134, 62, 61));
			btn6.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					btnPressed(6);
				}
			});
		}
		buttons[6] = btn6;
		return btn6;
	}

	/**
	 * This method initializes btn7
	 * 
	 * @return java.awt.Button
	 */
	private Button getBtn7() {
		if (btn7 == null) {
			btn7 = new Button();
			btn7.setBounds(new Rectangle(15, 206, 60, 62));
			btn7.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					btnPressed(7);
				}
			});
		}
		buttons[7] = btn7;
		return btn7;
	}

	/**
	 * This method initializes btn8
	 * 
	 * @return java.awt.Button
	 */
	private Button getBtn8() {
		if (btn8 == null) {
			btn8 = new Button();
			btn8.setBounds(new Rectangle(90, 207, 62, 63));
			btn8.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					btnPressed(8);
				}
			});
		}
		buttons[8] = btn8;
		return btn8;
	}

	/**
	 * This method initializes btn9
	 * 
	 * @return java.awt.Button
	 */
	private Button getBtn9() {
		if (btn9 == null) {
			btn9 = new Button();
			btn9.setBounds(new Rectangle(164, 207, 62, 64));
			btn9.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					btnPressed(9);
				}
			});
		}
		buttons[9] = btn9;
		return btn9;
	}

	private void btnPressed(int aPosition) {
		// System.out.println(position);
		boolean legal = false;
		if (player == 1) {
			legal = tModel.OPlays(aPosition);
			tModel.setLastMove("O");
		} else {
			legal = tModel.XPlays(aPosition);
			tModel.setLastMove("X");
		}
		if (legal) {
			tModel.change();
			
		} else {
			System.out.println("Illegal move!");
			
		}
		this.update(tModel, null);
	}

	//implement update method of Observer interface
	public void update(Observable arg0, Object arg1) {
		String[] positions = tModel.getPositions();
		if (tModel.isNew_game()) {
			lblWinMsg.setText("");
			btnRestart.setLabel("Restart");
		}
		for (int i = 1; i <= 9; i++) {
			if (positions[i].equals("X")) {
				buttons[i].setLabel("X");
				buttons[i].setEnabled(false);
			} else if (positions[i].equals("O")) {
				buttons[i].setLabel("O");
				buttons[i].setEnabled(false);
			} else {
				buttons[i].setLabel("");
				buttons[i].setEnabled(true);
			}
			if (tModel.isGameOver()) {
				buttons[i].setEnabled(false);
				if (tModel.hasWon("X")) {
					lblWinMsg.setText("Player 2 won!");
				} else if (tModel.hasWon("O")) {
					lblWinMsg.setText("Player 1 won!");
				} else {
					lblWinMsg.setText("The game is tied.");
				}
				btnRestart.setLabel("New Game?");
			}
		}
	}

	/**
	 * This method initializes btnRestart
	 * 
	 * @return java.awt.Button
	 */
	private Button getBtnRestart() {
		if (btnRestart == null) {
			btnRestart = new Button();
			btnRestart.setBounds(new Rectangle(61, 313, 137, 29));
			btnRestart.setLabel("Restart");
			btnRestart.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					btnRestartPressed();
				}
			});
		}
		return btnRestart;
	}

	private void btnRestartPressed() {
		System.out.println("restart pressed");
		tModel.setNew_game(true);
		tModel.restart();
		tModel.change();
		this.update(tModel, null);
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		new TictactoeUI().setVisible(true);
	}
}

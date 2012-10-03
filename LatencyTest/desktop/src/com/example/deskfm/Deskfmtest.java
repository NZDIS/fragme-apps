package com.example.deskfm;

import java.awt.Button;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import org.nzdis.fragme.ControlCenter;
import org.nzdis.fragme.helpers.StartupWaitForObjects;


public class Deskfmtest extends Frame implements Observer {
	
	private Panel jContentPanel=null;
	private TextField tf=null;
	private Button submit=null;
	private Label lb=null;
	private Label lbinfo=null;
	
	private TestOb tob=new TestOb();
	
	int changeNum;
	
	public Deskfmtest(){
		initialize();
		System.setProperty("JGroups.bind_addr", "IPV4");
		String username=System.getProperty("user.name");
		ControlCenter.setUpConnectionsWithHelper("Fragme Desktop test", username, new StartupWaitForObjects(1));

		Vector shareObs = ControlCenter.getAllObjects();
		
		System.err.println("Peers: " + ControlCenter.getNoOfPeers() + " Objects: " + shareObs.size());
		if (shareObs.size() == 0) {
			this.tob = (TestOb) ControlCenter.createNewObject(TestOb.class);
			System.out.println("Create a new TestOb");

		} else {
			this.tob = (TestOb) shareObs.get(0);
			System.out.println("join and share the TestOb");
		}
		this.tob.addObserver(this);
		this.update(tob, null);
		
		
	}
	
	private void initialize(){
		if (jContentPanel==null){
			 tf=new TextField();
			 tf.setBounds(14, 60,209, 22);
			 
			 tf.setText("");
			// submit=new Button("Submit");
			 lb= new Label();
			 lb.setBounds(14, 90,209, 22);
			 lb.setText("lb");
			 lbinfo=new Label();
			 lbinfo.setBounds(14, 110,209, 22);
			 lbinfo.setText("lbinfo");
			 jContentPanel=new Panel();
			 jContentPanel.setLayout(null);
			 jContentPanel.add(tf, null);
			 jContentPanel.add(setButton(),null);
			 jContentPanel.add(lb, null);
			 jContentPanel.add(lbinfo, null);
			 
		}
		this.add(jContentPanel);
		this.setTitle("Fragme desktop test");
		this.setBounds(20,20, 253, 428);
	}
	
	private Button setButton(){
		if (submit==null){
			submit=new Button("Submit");
			submit.setBounds(15, 281,61, 60) ;
			submit.addActionListener(new java.awt.event.ActionListener(){
				public void actionPerformed(java.awt.event.ActionEvent e) {
					submitPressed();
				}
			});
		}
		return submit;
	}
	
	private void submitPressed(){
		
		if (tf.getText().length() == 0) {
			lbinfo.setText("Please type in the text area");
			
			return;

		}
		
		if(tob.getRecord()==null){
			changeNum=0;
		}else{
		changeNum=Integer.valueOf(tob.getRecord());
		}
		changeNum++;
		tob.setChange(tf.getText().toString());
		tob.setRecord(String.valueOf(changeNum));
		System.out.println("tob:"+tob.getChange()+"  "+tob.getRecord());
		tob.change();
		System.out.println("deskfmtest : object changed");
		this.update(tob, null);
		
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new Deskfmtest().setVisible(true);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		lb.setText(tob.getRecord());
		tf.setText(tob.getChange());
	}

}

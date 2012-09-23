package com.example.fmtest2;

import org.nzdis.fragme.factory.FactoryObject;
import org.nzdis.fragme.factory.FragMeFactory;
import org.nzdis.fragme.objects.FMeObject;

import android.util.Log;

public class TestOb extends FMeObject {

	private String change;
	private String record;

	public TestOb() {
	}

	@Override
	public void deserialize(FMeObject serObject) {
		// TODO Auto-generated method stub
		Log.i("Test Object:", "deserialized(FMeObject)");
		this.change = ((TestOb) serObject).getChange();
		this.record = ((TestOb) serObject).getRecord();
	}

	@Override
	public void changedObject() {
		// TODO Auto-generated method stub
		Log.i("Test object: ", "Received a change notification!");
		this.setChanged();
		this.notifyObservers();

	}

	@Override
	public void deletedObject() {
		// TODO Auto-generated method stub
		Log.i("Test object:", "Received a delete notification!");
		this.setChanged();
		this.notifyObservers();

	}

	/*
	 * public void changedObject() {
	 * System.out.println("Received a change notification!"); this.setChanged();
	 * this.notifyObservers(); }
	 * 
	 * public void deletedObject() {
	 * System.out.println("Received a delete notification!"); this.setChanged();
	 * this.notifyObservers(); }
	 * 
	 * public void deserialize(FMeObject serObject) { this.positions =
	 * ((TicTacToeModel) serObject).getPositions(); }
	 */

	private static class Factory extends FragMeFactory {
		protected FactoryObject create() {
			return new TestOb();
		}
	}

	static {
		FragMeFactory.addFactory(new Factory(), TestOb.class);
	}

	public String getChange() {
		return change;
	}

	public String getRecord() {
		return record;
	}

	public void setChange(String change) {
		this.change = change;
	}

	public void setRecord(String record) {
		this.record = record;
	}

}

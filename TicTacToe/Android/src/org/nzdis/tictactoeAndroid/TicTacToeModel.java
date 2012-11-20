package org.nzdis.tictactoeAndroid;

import org.nzdis.fragme.factory.FactoryObject;
import org.nzdis.fragme.factory.FragMeFactory;
import org.nzdis.fragme.objects.FMeObject;

/*
 * Shared object between peers
 */
public class TicTacToeModel extends FMeObject {
	
	private static final long serialVersionUID = 2443727758382046169L;

	// The positions of user move
	public String[] positions = { "*", "1", "2", "3", "4", "5", "6", "7", "8",
			"9" }; 
	
	private boolean new_game;
	// last move of peers
	public String lastMove = ""; 

	public TicTacToeModel() {
	}

	//Decide which player ("O" or "X") wins
	public boolean hasWon(String anXorO) {
		
		// check the rows for XXX or OOO
		if ((positions[1].equals(anXorO)) && (positions[2].equals(anXorO))
				&& positions[3].equals(anXorO))
			return true;
		else if ((positions[4].equals(anXorO)) && (positions[5].equals(anXorO))
				&& positions[6].equals(anXorO))
			return true;
		else if ((positions[7].equals(anXorO)) && (positions[8].equals(anXorO))
				&& positions[9].equals(anXorO))
			return true;
		// check the columns for XXX or OOO
		else if ((positions[1].equals(anXorO)) && (positions[4].equals(anXorO))
				&& positions[7].equals(anXorO))
			return true;
		else if ((positions[2].equals(anXorO)) && (positions[5].equals(anXorO))
				&& positions[8].equals(anXorO))
			return true;
		else if ((positions[3].equals(anXorO)) && (positions[6].equals(anXorO))
				&& positions[9].equals(anXorO))
			return true;
		// check the diagonals for XXX or OOO
		else if ((positions[1].equals(anXorO)) && (positions[5].equals(anXorO))
				&& positions[9].equals(anXorO))
			return true;
		else if ((positions[3].equals(anXorO)) && (positions[5].equals(anXorO))
				&& positions[7].equals(anXorO))
			return true;
		return false;
	}

	// Decide whether the game is over. The game is over if either "X" or "O" have a winning
	// position or there are no more free positions
	public boolean isGameOver() {
		
		if (!hasFreePositions()) {
			lastMove = "";
			return true;
		}
		if (hasWon("X")) {
			lastMove = "";
			return true;
		}
		if (hasWon("O")) {
			lastMove = "";
			return true;
		}

		return false;
	}

	// Decide whether there are free places to play on
	private boolean hasFreePositions() {
		
		boolean hasFreePlacesToPlay = false;
		for (int i = 1; i < positions.length; i++)
			if (isFree(i))
				hasFreePlacesToPlay = true;
		return hasFreePlacesToPlay;
	}

	//Decide whether the position is free to play on
	private boolean isFree(int aPosition) {
		
		if ((aPosition < 1) || (aPosition > 9))
			return false; // positions are out of range
		return !((positions[aPosition].equals("X")) || (positions[aPosition]
				.equals("O")));
	}

	//Make a move for "X" player if it is a free place and not the same player as last move
	public boolean XPlays(int aPosition) {
		
		if (isFree(aPosition) && (!lastMove.equals("X"))) {
			positions[aPosition] = "X";
			return true;
		}
		return false;
	}

	//Make a move for "O" player if it is a free place and not the same player as last move
	public boolean OPlays(int aPosition) {
		
		if (isFree(aPosition) && (!lastMove.equals("O"))) {
			positions[aPosition] = "O";
			return true;
		}
		return false;
	}

	//initial a new game
	public void restart() {
		positions[0] = "*";
		for (int i = 1; i <= 9; i++) {
			positions[i] = String.valueOf(i);
		}
		lastMove = "";
	}

	public void changed(FMeObject object) {
		//System.out.println("Received a change notification!");
	}

	public void delegatedOwnership(FMeObject object) {
		//System.out.println("Received a delegate ownership notification!");
	}

	public void deleted(FMeObject object) {
		//System.out.println("Received a delete notification!");
	}

	public void deserialize(FMeObject serObject) {
		this.positions = ((TicTacToeModel) serObject).getPositions();
		this.lastMove = ((TicTacToeModel) serObject).getLastMove();
	}

	private static class Factory extends FragMeFactory {
		protected FactoryObject create() {
			return new TicTacToeModel();
		}
	}

	static {
		FragMeFactory.addFactory(new Factory(), TicTacToeModel.class);
	}

	public String[] getPositions() {
		return positions;
	}

	public void setPositions(String[] positions) {
		this.positions = positions;
	}

	public String getLastMove() {
		return lastMove;
	}

	public void setLastMove(String lastMove) {
		this.lastMove = lastMove;
	}

	public boolean isNew_game() {
		return this.new_game;
	}

	public void setNew_game(boolean new_game) {
		this.new_game = new_game;
	}
}
import java.util.ArrayList;
import java.util.Random;

/**
	* Etude 1
	* Oliver McAleese, Alex Lake-Smith, Connor Spear, Mathew Shields
	*
	* main inspiration:
	* 	https://scientificgems.wordpress.com/2018/02/14/the-game-of-mu-torere/
	*/

class OurPlayer extends Player {

	private Random rng;

	public OurPlayer(BoardReader boardReader, Board.Piece playerID) {
		super(boardReader, playerID);
		rng = new Random();
	}

	public int getMove() {

		int checkOne, checkTwo, calculate, checkPlusOne, checkPlusTwo, checkMinus, temp, checkTwoPlus, checkTwoPlusPlus;
		int blankVal = 9, num = 0, target = 0;

		blankVal = 9;
		num = 0;
		while(blankVal == 9){
			if (boardReader.pieceAt(num) == Board.Piece.BLANK) blankVal = num;
			else num++;
		}
		if(blankVal == 0){
			checkOne = 7;
			checkTwo = 1;
		}else{
			checkOne = blankVal - 1;
			checkTwo = (blankVal + 1) % 8;
		}

		ArrayList<Integer> validMoves = new ArrayList<Integer>();
		for (int i = 0; i < 9; ++i) {
			for (int j = 0; j < 9; ++j) {
				if (isValid(i,j)) {
					validMoves.add(i);
					continue;
				}
			}
		}
		if (validMoves.isEmpty()) { // lost game
			return 0;
		}else if (validMoves.size() == 1){ // only one valid move
			return validMoves.get(0);
		}

		// Winning scenarios
		else if (isSmallTrap()){
			// perform small trap
			// also part three of big trap
			checkPlusOne = (blankVal + 1) % 8;
			checkPlusTwo = (blankVal + 2) % 8;
			if (blankVal == 0) checkMinus = 7;
			else checkMinus = blankVal - 1;
			if (boardReader.pieceAt(checkPlusOne) == playerID && boardReader.pieceAt(checkPlusTwo) == playerID) return checkPlusOne;
			else return checkMinus;
		}

		else if (isBigTrap2()){
			// perform big trap move 2
			// find white with black on either side
			while (target < 8){
				if (boardReader.pieceAt(target) != playerID){
					if(target == 0){
						checkOne = 7;
						checkTwo = 1;
					}else{
						checkOne = target - 1;
						checkTwo = (target + 1) % 8;
					}
					if (boardReader.pieceAt(checkOne) == playerID && boardReader.pieceAt(checkTwo) == playerID) return checkOne;
				}
				target++;
			}
		}

		else if (isBigTrap()){
			// perform big trap move 1
			calculate = (checkOne + 4) % 8;
			if(boardReader.pieceAt(calculate) != playerID) return checkOne;
			else return checkTwo;
		}

		// avoid falling into small trap
		else if(avoidSmallTrap()){
			checkTwoPlus = (blankVal + 2) % 8;
			checkTwoPlusPlus = (blankVal + 3) % 8;
			if(boardReader.pieceAt(checkTwoPlus) == playerID || boardReader.pieceAt(checkTwoPlusPlus) == playerID) return checkTwo;
			else return checkOne;
		}

		else{
			return validMoves.get(rng.nextInt(validMoves.size()));
		}

		return 0;
	}

	boolean isValid(int moveFrom, int moveTo) {

		if (boardReader.pieceAt(moveTo) != Board.Piece.BLANK) {
			return false;
		}

		if (boardReader.pieceAt(moveFrom) != playerID) {
			return false;
		}
		if (moveTo == 8) {
			// Move to centre, check for valid neighbour
			int prev = moveFrom - 1;
			if (prev < 0) prev = 7;
			int next = moveFrom + 1;
			if (next > 7) next = 0;
			if (boardReader.pieceAt(prev) == playerID && boardReader.pieceAt(next) == playerID) {
				return false;
			}
		} else {
			// Either move from centre to kewai...
			if (moveFrom == 8) {
				return true;
			}
			// ... or from one kewai to next, make sure they are neighbours
			int prev = moveFrom - 1;
			if (prev < 0) prev = 7;
			int next = moveFrom + 1;
			if (next > 7) next = 0;
			if (boardReader.pieceAt(prev) != Board.Piece.BLANK &&
				boardReader.pieceAt(next) != Board.Piece.BLANK) {
				return false;
			}
		}
		return true;
	}

	// check if board is in big trap winnable scenario
	boolean isBigTrap(){
		int leftTop = 7, left = 6, leftBottom = 5, topRight = 1, bottomRight = 3;
		int num = 0;
		if (boardReader.pieceAt(8) == playerID || boardReader.pieceAt(8) == Board.Piece.BLANK) return false;
		while (num < 8){
			if (boardReader.pieceAt(leftTop) != playerID && boardReader.pieceAt(left) != playerID && boardReader.pieceAt(leftBottom) != playerID){
				if (boardReader.pieceAt(topRight) == Board.Piece.BLANK || boardReader.pieceAt(bottomRight) == Board.Piece.BLANK) return true;
			}
			leftTop = (leftTop + 1) % 8;
			left = (left + 1) % 8;
			leftBottom = (leftBottom + 1) % 8;
			topRight = (topRight + 1) % 8;
			bottomRight = (bottomRight + 1) % 8;
			num++;
		}
		return false;
	}

	// check if board is in big trap part 2
	boolean isBigTrap2(){
		int top = 0, topRight = 1, bottomRight = 3, bottom = 4;
		int num = 0;
		if (boardReader.pieceAt(8) != Board.Piece.BLANK) return false;
		while (num < 8){
			if (boardReader.pieceAt(top) == playerID && boardReader.pieceAt(topRight) == playerID &&
					boardReader.pieceAt(bottomRight) == playerID && boardReader.pieceAt(bottom) == playerID) return true;
			top = (top + 1) % 8;
			topRight = (topRight + 1) % 8;
			bottomRight = (bottomRight + 1) % 8;
			bottom = (bottom + 1) % 8;
			num++;
		}
		return false;
	}

	// check if board is in small trap. AKA big trap part 3
	boolean isSmallTrap(){
		int top = 0, topRight = 1, right = 2, bottomRight = 3;
		int num = 0;
		if (boardReader.pieceAt(8) != playerID) return false;
		while (num < 8){
			if (boardReader.pieceAt(topRight) == playerID && boardReader.pieceAt(right) == playerID){
				if (boardReader.pieceAt(top) == Board.Piece.BLANK || boardReader.pieceAt(bottomRight) == Board.Piece.BLANK) return true;
			}
			top = (top + 1) % 8;
			topRight = (topRight + 1) % 8;
			right = (right + 1) % 8;
			bottomRight = (bottomRight + 1) % 8;
			num++;
		}
		return false;
	}

	// avoid player moving into a losing scenario
	boolean avoidSmallTrap(){
		Board.Piece enemy;
		if (playerID == Board.Piece.ONE) enemy = Board.Piece.TWO;
		else enemy = Board.Piece.ONE;
		int top = 0, topRight = 1, right = 2, bottomRight = 3, bottom = 4, leftTop = 7, left = 6, leftBottom = 5;
		int num = 0;
		if (boardReader.pieceAt(8) == playerID || boardReader.pieceAt(8) == Board.Piece.BLANK) return false;
		while(num < 8){
			if (boardReader.pieceAt(top) == enemy && boardReader.pieceAt(topRight) == enemy){
				if ((boardReader.pieceAt(right) == playerID && boardReader.pieceAt(bottomRight) == Board.Piece.BLANK && boardReader.pieceAt(bottom) == playerID) ||
						(boardReader.pieceAt(leftTop) == playerID && boardReader.pieceAt(left) == Board.Piece.BLANK && boardReader.pieceAt(leftBottom) == playerID)) return true;
			}
			top = (top + 1) % 8;
			topRight = (topRight + 1) % 8;
			right = (right + 1) % 8;
			bottomRight = (bottomRight + 1) % 8;
			bottom = (bottom + 1) % 8;
			leftTop = (leftTop + 1) % 8;
			left = (left + 1) % 8;
			leftBottom = (leftBottom + 1) % 8;
			num++;
		}
		return false;
	}

}

import java.util.*;

/**
 * Application class for solving Pentomino puzzles. Given a puzzle and the pieces to use, 
 * the program attempts to fill in the puzzle.
 */
public class App {

  private static ArrayList<char[]> output = new ArrayList<char[]>();
  private static boolean asterisk = false;
  private static ArrayList<Character> inputPieces = new ArrayList<Character>();

  public static ArrayList<Character> getPieces(Scanner scan) {
    String firstLine = scan.nextLine();
    ArrayList<Character> pieces = new ArrayList<Character>();
    for(int i = 0; i < firstLine.length(); i++){
      pieces.add(firstLine.charAt(i));
    }
    return pieces;
  }

  public static ArrayList<char[]> getPuzzle(Scanner scan) {
    ArrayList<char[]> puzzle = new ArrayList<char[]>();
    while (scan.hasNextLine()) {
      puzzle.add(scan.nextLine().toCharArray());
    }
    return puzzle;
  }

  public static void print(ArrayList<char[]> grid) {
    try {
      for (int i = 0; i < grid.size(); i++) {
        for (int j = 0; j < grid.get(i).length; j++) {
          System.out.print(grid.get(i)[j]);
        }
        System.out.println();
      }
    } catch (NullPointerException e) {
      System.out.println("Null");
    }
  }

  public static void printPiece(char[][] piece) {
    for (int i = 0; i < piece.length; i++) {
      for (int j = 0; j < piece[i].length; j++) {
        System.out.print(piece[i][j]);
      }
      System.out.println();
    }
    System.out.println();
  }

  public static ArrayList<char[]> copyPuzzle(ArrayList<char[]> puzzle) {
    ArrayList<char[]> copy = new ArrayList<char[]>();
    for (int i = 0; i < puzzle.size(); i++) {
      copy.add(new char[puzzle.get(i).length]);
      for (int j = 0; j < puzzle.get(i).length; j++) {
        copy.get(i)[j] = puzzle.get(i)[j];
      }
    }
    return copy;
  }

  public static ArrayList<char[]> replace(ArrayList<char[]> puzzle, Pentomino p, char[][] pShape, int row, int col) {
    for (int i = 0; i < pShape.length; i++) {
      for (int j = 0; j < pShape[i].length; j++) {
        if (pShape[i][j] == '*') {
          if (puzzle.get(i + row)[j + col - p.check] == '.') {
            puzzle.get(i + row)[j + col - p.check] = p.type;
          }
        }
      }
    }
    return puzzle;
  }

  public static boolean checkComplete(ArrayList<char[]> puzzle) {
    for (int i = 0; i < puzzle.size(); i++) {
      for (int j = 0; j < puzzle.get(i).length; j++) {
        if (puzzle.get(i)[j] == '.') {
          return false;
        }
      }
    }
    return true;
  }

  public static void output(ArrayList<char[]> puzzle, ArrayList<char[]> output, ArrayList<Character> pieces) {
    for (int i = 0; i < inputPieces.size(); i++) { // - 1 to remove the 'A'
      System.out.print(inputPieces.get(i));
    }
    System.out.println();
    if (output != null) {
      print(output);
    } else {
      print(puzzle);
      System.out.println("Impossible");
    }
  }

  public static ArrayList<char[]> pentominoFit(ArrayList<char[]> puzzle, Pentomino p, char[][] pShape, int row, int col) {
    ArrayList<char[]> copy = copyPuzzle(puzzle);
    if (row + pShape.length > puzzle.size()) {
      return null;
    }
    for (int x = 0; x < pShape.length; x++) {
      if (pShape[0][x] == '*') {
        if (col + pShape[0].length - x > puzzle.get(0).length) {
          return null;
        }
        for (int i = 0; i < pShape.length; i++) {
          for (int j = 0; j < pShape[i].length; j++) {
            if (j + col - x < 0) {
              return null;
            }
            if (pShape[i][j] == '*') {
              if (copy.get(i + row)[j + col - x] != '.') {
                return null;
              }
            }
          }
        }
        p.check = x;
        copy = replace(copy, p, pShape, row, col);
        if(checkSingleSpace(copy) == false) return null;
        if(spanOut(copy) == false) return null;
        return copy;
      }
    }
    return null;
  }

  public static boolean spanOut(ArrayList<char[]> input){
    boolean first = true;
    ArrayList<char[]> copy = copyPuzzle(input);
    for(int i = 0; i < copy.size(); i++){
      for(int j = 0; j < copy.get(i).length; j++){
        if(copy.get(i)[j] == '.' && first == true){
          first = false;
          copy.get(i)[j] = '$';
          if(spanOutSolve(copy, 1) == false){
            return false;
          }else {
            return true;
          }
        }
      }
    }
    return true;
  }

  // false if find space not a multiple of 5
  public static boolean spanOutSolve(ArrayList<char[]> inputEdited, int totalChanges){
    int changes = 0;
    ArrayList<char[]> copy = copyPuzzle(inputEdited);
    for(int ii = 0; ii < copy.size(); ii++){
      for(int jj = 0; jj < copy.get(ii).length; jj++){
        if(copy.get(ii)[jj] == '$'){
          if(ii - 1 >= 0){//above
            if(copy.get(ii - 1)[jj] == '.'){
              copy.get(ii - 1)[jj] = '$';
              changes++;
            }
          }
          if(ii + 1 < copy.size()){//below
            if(copy.get(ii + 1)[jj] == '.'){
              copy.get(ii + 1)[jj] = '$';
              changes++;
            }
          }
          if(jj - 1 >= 0){//left
            if(copy.get(ii)[jj - 1] == '.'){
              copy.get(ii)[jj - 1] = '$';
              changes++;
            }
          }
          if(jj + 1 < copy.get(ii).length){//right
            if(copy.get(ii)[jj + 1] == '.'){
              copy.get(ii)[jj + 1] = '$';
              changes++;
            }
          }
        }
      }
    }
    if(changes == 0){
      if((totalChanges + changes) % 5 == 0){
        return true;
      }else{
        return false;
      }
    }else{
      if(spanOutSolve(copy, totalChanges + changes) == false){
        return false;
      }
    }
    return true;
  }

  //avoid creating single '.' surrounded by unplacable points
  public static boolean checkSingleSpace(ArrayList<char[]> updated){
    for (int row = 0; row < updated.size(); row++){
      for (int col = 0; col < updated.get(row).length; col++){
        if(updated.get(row)[col] == '.') {
          int tally = 0;
          if(row - 1 >= 0){//above
            if(updated.get(row - 1)[col] == '.'){
              tally++;
            }
          }
          if(row + 1 < updated.size()){//below
            if(updated.get(row + 1)[col] == '.'){
              tally++;
            }
          }
          if(col - 1 >= 0){//left
            if(updated.get(row)[col - 1] == '.'){
              tally++;
            }
          }
          if(col + 1 < updated.get(row).length){//right
            if(updated.get(row)[col + 1] == '.'){
              tally++;
            }
          }
          if(tally == 0){
            return false;
          }
        }
      }
    }
    return true;
  }

  public static ArrayList<char[]> recursive(ArrayList<char[]> puzzle, ArrayList<Character> pieces, char toRemove) {
    ArrayList<char[]> remainder = new ArrayList<char[]>();
    ArrayList<char[]> restOfPuzzle = new ArrayList<char[]>();

    ArrayList<Character> newPiecesArr = new ArrayList<Character>(pieces);
    if(!asterisk){
      newPiecesArr.remove(newPiecesArr.indexOf(toRemove));
    }

    for (int row = 0; row < puzzle.size(); row++) {
      for (int col = 0; col < puzzle.get(row).length; col++){//For each position in order from top left to bottom right
        if (puzzle.get(row)[col] == '.'){//If the position is not already filled
          for (int num = 0; num < newPiecesArr.size(); num++){//For each possible piece
            Pentomino p = new Pentomino(newPiecesArr.get(num));
            for(int shape = 0; shape < p.pent.length; shape++){//and each possible rotation and flip
              if ((remainder = pentominoFit(puzzle, p, p.pent[shape], row, col)) != null){// If it can be placed in that position
                //Put it there and make a recursive call
                if (!checkComplete(remainder)) {//not solved yet
                  restOfPuzzle = recursive(remainder, newPiecesArr, newPiecesArr.get(num));
                }
                if (restOfPuzzle != null) {
                  replace(output, p, p.pent[shape], row, col);
                  return output;
                }
              }
            }
          }
          return null;
        }
      }
    }
    return null;
  }

  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);
    ArrayList<Character> pieces = getPieces(scan);
    inputPieces = new ArrayList<Character>(pieces);
    ArrayList<char[]> puzzle = getPuzzle(scan);

    output = copyPuzzle(puzzle);

    if(pieces.get(pieces.size() - 1) == '*'){
      pieces.remove(pieces.size() - 1);
      asterisk = true;
    }else{
      pieces.add('A');
    }

    output = recursive(puzzle, pieces, 'A');
    output(puzzle, output, pieces);
  }
}

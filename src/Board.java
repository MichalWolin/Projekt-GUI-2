import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Board {
    private static ArrayList<Piece>[][] board;

    public static void startTheGame(){
        board = new ArrayList[9][9];
        new Rook(Colour.WHITE, 1, 1);
        new Knight(Colour.WHITE, 1, 2);
        new Bishop(Colour.WHITE, 1, 3);
        new Queen(Colour.WHITE, 1, 4);
        new King(Colour.WHITE, 1, 5);
        new Bishop(Colour.WHITE, 1, 6);
        new Knight(Colour.WHITE, 1, 7);
        new Rook(Colour.WHITE, 1, 8);

        new Rook(Colour.BLACK, 8, 1);
        new Knight(Colour.BLACK, 8, 2);
        new Bishop(Colour.BLACK, 8, 3);
        new Queen(Colour.BLACK, 8, 4);
        new King(Colour.BLACK, 8, 5);
        new Bishop(Colour.BLACK, 8, 6);
        new Knight(Colour.BLACK, 8, 7);
        new Rook(Colour.BLACK, 8, 8);

        for(int i = 1; i <= 8; i++){
            new Pawn(Colour.WHITE, 2, i);
            new Pawn(Colour.BLACK, 7, i);
        }
    }

    public static void printBoard(){
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));

        System.out.println("  \u2009\u200AA \u200AB\u2009\u2009\u200A\u200AC \u200AD" +
                "\u2009\u2009\u200AE \u200AF\u2009\u2009\u200A\u200AG \u200AH");
        for (int i = 8; i > 0; i--) {
            System.out.print(i + " ");
            for (int j = 1; j < 9; j++) {
                if(board[i][j] == null){
                    if((i + j) % 2 != 0) {
                        System.out.print("\u001B[48;2;255;206;158m \u200A\u2009\u2009\u200A\u001B[0m");
                    }else{
                        System.out.print("\u001B[48;2;209;139;71m \u200A\u2009\u2009\u200A\u001B[0m");
                    }
                }else{
                    if((i + j) % 2 != 0) {
                        System.out.print("\u001B[48;2;255;206;158m\u200A" + board[i][j].get(0) + "\u200A\u001B[0m");
                    }else{
                        System.out.print("\u001B[48;2;209;139;71m\u200A" + board[i][j].get(0) + "\u200A\u001B[0m");
                    }
                }
            }
            System.out.print(" " + i + " ");
            if(board[0][0] != null){
                if(i == 1){
                    System.out.print("Captured pieces:");
                    for (int j = 0; j < board[0][0].size(); j++) {
                        if(board[0][0].get(j).getColour() == Colour.WHITE){
                            System.out.print(board[0][0].get(j));
                        }
                    }
                }else if(i == 8){
                    System.out.print("Captured pieces:");
                    for (int j = 0; j < board[0][0].size(); j++) {
                        if(board[0][0].get(j).getColour() == Colour.BLACK){
                            System.out.print(board[0][0].get(j) + " ");
                        }
                    }
                }
            }
            System.out.println();
        }
        System.out.println("  \u2009\u200AA \u200AB\u2009\u2009\u200A\u200AC \u200AD" +
                "\u2009\u2009\u200AE \u200AF\u2009\u2009\u200A\u200AG \u200AH\n");
    }

    public static void setPiecePos(Piece piece){
        if(board[piece.getY()][piece.getX()] == null) {
            board[piece.getY()][piece.getX()] = new ArrayList<>();
        }else{
            board[piece.getY()][piece.getX()].clear();
        }
        board[piece.getY()][piece.getX()].add(piece);
    }

    public static boolean moveProcessing(String input, Colour colour){
        if(input.length() != 5){
            System.out.println("Invalid input!");
            return false;
        }

        int sourceY = input.charAt(1) - '0';
        int sourceX = input.charAt(0) - 'a' + 1;
        int destY = input.charAt(4) - '0';
        int destX = input.charAt(3) - 'a' + 1;
        if(sourceY == destY && sourceX == destX){
            System.out.println("You cannot move to the same field!");
            return false;
        }

        if(sourceY < 1 || sourceY > 8 || sourceX < 1 || sourceX > 8
                || destY < 1 || destY > 8 || destX < 1 || destX > 8){

            System.out.println("You cannot move out of bounds!");
            return false;
        }

        if (board[sourceY][sourceX] == null) {
            System.out.println("There is no piece on this field!");
            return false;
        }

        Piece piece = board[sourceY][sourceX].get(0);

        if (piece.getColour() != colour) {
            System.out.println("This is not your piece!");
            return false;
        }

        boolean didCastle = false;
        Piece other = Board.getPiece(destY, destX);
        if(piece instanceof King && other != null){
            King king = (King) piece;
            if(King.isCastling(other, king)){
                King.castlingMove(other, king);
                didCastle = true;
            }
        }

        if(!didCastle){
            if(!piece.isMoveValidAndNotChecked(destY, destX)) {
                return false;
            }else{
                piece.movePiece(destY, destX);
            }
        }

        flushEnPassantable(colour);

        return true;
    }

    public static void capturePiece(Piece piece){
        if(board[0][0] == null) {
            board[0][0] = new ArrayList<>();
        }
        piece.setY(0);
        piece.setX(0);
        board[0][0].add(piece);
    }

    public static boolean isPathClear(int srcY, int srcX, int destY, int destX){
        int distanceY = destY - srcY, distanceX = destX - srcX;
        int length;

        int changeY = distanceY < 0 ? -1 : distanceY > 0 ? 1 : 0;
        int changeX = distanceX < 0 ? -1 : distanceX > 0 ? 1 : 0;

        length = distanceY != 0 ? Math.abs(distanceY) - 1 : Math.abs(distanceX) - 1;

        for (int i = 0; i < length; i++) {
            srcY += changeY;
            srcX += changeX;

            if(srcY == 9 || srcX == 9 || srcY == 0 || srcX == 0){
                return false;
            }

            if(getPiece(srcY, srcX) != null){
                return false;
            }
        }

        return true;
    }

    public static Piece getPiece(int y, int x){
        if(board[y][x] == null) {
            return null;
        }
        return board[y][x].get(0);
    }

    public static boolean isChecked(int[] kingPosition) {
        int kingY = kingPosition[0];
        int kingX = kingPosition[1];
        Colour kingColour = board[kingY][kingX].get(0).getColour();

        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (board[i][j] == null) {
                    continue;
                }

                Piece piece = board[i][j].get(0);
                if (piece.getColour() == kingColour) {
                    continue;
                }

                if(piece.isMoveValid(kingY, kingX)){
                    return true;
                }
            }
        }

        return false;
    }


    public static int[] findKing(Colour colour){
        int[] kingPosition = new int[2];
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if(board[i][j] == null){
                    continue;
                }

                if(board[i][j].get(0) instanceof King && board[i][j].get(0).getColour() == colour){
                    kingPosition[0] = i;
                    kingPosition[1] = j;
                    return kingPosition;
                }
            }
        }

        return null;
    }

    public static boolean isCheckedMated(int[] kingPosition){
        int kingY = kingPosition[0];
        int kingX = kingPosition[1];
        King king = (King)board[kingY][kingX].get(0);

        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if(king.isMoveValidAndNotChecked(i, j)){
                    return false;
                }
                Piece piece = getPiece(i, j);
                if(piece != null){
                    for (int k = 1; k < 9; k++) {
                        for (int l = 1; l < 9; l++) {
                            if(piece.isMoveValidAndNotChecked(k, l) && piece.getColour() == king.getColour()){
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    public static void tileClear(int y, int x){
        board[y][x].clear();
    }

    public static void tileSetNull(int y, int x){
        board[y][x] = null;
    }

    public static void flushEnPassantable(Colour colour){
        int y;
        if(colour == Colour.WHITE){
            y = 5;
        }else{
            y = 4;
        }

        for (int x = 1; x < 9; x++) {
            Piece piece = Board.getPiece(y, x);
            if(piece != null){
                if(piece instanceof Pawn){
                    ((Pawn)piece).setEnPassantable(false);
                }
            }
        }
    }

    public static void saveGame() {
        File file = new File("src/chess.bin");

        try{
            FileOutputStream fos = new FileOutputStream(file);

            if(board[0][0] != null){
                for (int i = 0; i < board[0][0].size(); i++) {
                    Piece piece = board[0][0].get(i);
                    int colour = piece.getColour() == Colour.WHITE ? 0 : 1;
                    int y = piece.getY();
                    int x = piece.getX();
                    int type = piece.getPieceTypeValue();
                    int data = colour << 11 | y << 7 | x << 3 | type;
                    fos.write(data >> 8);
                    fos.write(data);
                }
            }

            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 8; j++) {
                    if(board[i][j] != null){
                        Piece piece = board[i][j].get(0);
                        int colour = piece.getColour() == Colour.WHITE ? 0 : 1;
                        int y = piece.getY();
                        int x = piece.getX();
                        int type = piece.getPieceTypeValue();
                        int data = colour << 11 | y << 7 | x << 3 | type;
                        fos.write(data >> 8);
                        fos.write(data);
                    }
                }
            }

            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void loadGame(){
        File file = new File("src/chess.bin");

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                tileSetNull(i, j);
            }
        }

        try{
            FileInputStream fis = new FileInputStream(file);
            int data;
            while((data = fis.read() << 8 | fis.read()) != -1) {
                int colour = data >> 11 & 0b1;
                int y = data >> 7 & 0b1111;
                int x = data >> 3 & 0b1111;
                int type = data & 0b111;

                switch(type){
                    case 0 -> new Pawn(colour == 0 ? Colour.WHITE : Colour.BLACK, y, x);
                    case 1 -> new King(colour == 0 ? Colour.WHITE : Colour.BLACK, y, x);
                    case 2 -> new Queen(colour == 0 ? Colour.WHITE : Colour.BLACK, y, x);
                    case 3 -> new Rook(colour == 0 ? Colour.WHITE : Colour.BLACK, y, x);
                    case 4 -> new Bishop(colour == 0 ? Colour.WHITE : Colour.BLACK, y, x);
                    case 5 -> new Knight(colour == 0 ? Colour.WHITE : Colour.BLACK, y, x);
                }
            }
            printBoard();
            fis.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

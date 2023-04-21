import java.util.Scanner;

public class S29239Projekt02 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while(true){
            Board.startTheGame();
            Board.printBoard();
            boolean isBeingPlayed = true;
            boolean whiteDraw = false;
            boolean blackDraw = false;
            int whoseTurn = 0;
            Colour colour;

            while(isBeingPlayed){
                colour = whoseTurn % 2 == 0 ? Colour.WHITE : Colour.BLACK;
                int[] kingPosition = Board.findKing(colour);

                if(Board.isChecked(kingPosition)){
                    if(Board.isCheckedMated(kingPosition)){
                        System.out.println("-----Checkmate!-----");
                        isBeingPlayed = false;
                        continue;
                    }
                    System.out.println("Your king is checked!");
                }

                if(whiteDraw || blackDraw){
                    if(whoseTurn % 2 == 1){
                        System.out.println("WHITE offered a draw! Do you accept? (draw/decline): ");
                    }else{
                        System.out.println("BLACK offered a draw! Do you accept? (draw/decline): ");
                    }
                }


                System.out.println(colour + "'s turn!" + "\nWhat's your move? (e.g.: e2 e4): ");
                String input = scanner.nextLine();
                System.out.println();

                switch(input){
                    case "exit" -> System.exit(0);
                    case "draw" -> {
                        if(colour == Colour.WHITE){
                            whiteDraw = true;
                        }else{
                            blackDraw = true;
                        }
                        if(whiteDraw && blackDraw){
                            System.out.println("-----Draw!-----");
                            isBeingPlayed = false;
                            continue;
                        }
                    whoseTurn++;
                    }
                    case "decline" -> {
                        System.out.println(colour + " declined the draw!");
                        whiteDraw = false;
                        blackDraw = false;
                        whoseTurn++;
                    }
                    case "save" -> Board.saveGame();
                    case "load" -> {
                        whoseTurn = 0;
                        Board.loadGame();
                    }
                    default -> {
                        if(Board.moveProcessing(input, colour)){
                            whoseTurn++;
                            Board.printBoard();
                        }else{
                            System.out.println("Invalid move!");
                        }
                    }
                }
            }

            Scanner decision = new Scanner(System.in);
            System.out.println("Do you want to play again? (yes/no): ");
            String input = decision.nextLine();
            switch(input){
                case "no" -> System.exit(0);
                case "save" -> Board.saveGame();
                case "load" -> Board.loadGame();
            }
        }
    }
}

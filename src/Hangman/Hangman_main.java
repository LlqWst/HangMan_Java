package Hangman;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

public class Hangman_main {
    public static void main(String[] args) {
        while (true){
            int mistakeCount = 0;
            if (startMenu() == 2) System.exit(0);
            String seekWord = getWord();
            String maskWord = maskFill(seekWord);
            while (mistakeCount < 7 && !seekWord.equals(maskWord)) {
                printHangMan(mistakeCount);
                printMistakes(mistakeCount);
                printWord(maskWord);
                char charInput = getLowCaseChar();
                if (isWordContainsChar(seekWord, charInput)){
                    maskWord = maskBuilder(seekWord, charInput, maskWord);
                } else if (!(isIgnoreSymbols(charInput))){
                    mistakeCount++;
                }
            }
            gameResult(mistakeCount);
            printWord(seekWord);
        }
    }

    public static int startMenu(){
        int answer;
        System.out.println();
        System.out.println("|------------------HangMan---------------------|");
        System.out.println("|Write down 'start' for game or 'exit' for quit|");
        System.out.println("|----------------------------------------------|");
        do {
            answer = menuOptions(inputString());
        } while (!(answer == 2 || answer == 1));
        return answer;
    }

    public static String inputString(){
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }

    public static int  menuOptions (String entry){
        return switch (entry) {
            case "start" -> 1;
            case "exit" -> 2;
            default -> 0;
        };
    }

    public static String getWord () {
        List<String> list = new ArrayList<>();
        try {
            FileReader filePath = new FileReader("src/Hangman/WordsNouns.txt");
            BufferedReader reader = new BufferedReader(filePath);
            String line = reader.readLine();
            while (line != null) {
                list.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list.get(getRandInt(list.size()));
    }

    public static int getRandInt (int LinesNumber){
        Random rand = new Random();
        return rand.nextInt(LinesNumber);
    }

    public static String maskFill(String seekWord){
        return "*".repeat(seekWord.length());
    }

    public static void  printHangMan (int mistakeCount){
        final int rows = 8, columns = 14;
        String[][] arrHangMan = new String[rows][columns];
        for (int i = 0; i < arrHangMan.length; i++) {
            for (int j = 0; j < arrHangMan[i].length; j++) {
                if ((i == 0 && j >= 4 && j < columns - 1) ||
                    (i == rows - 1 && j <= 8 && j != 4)) System.out.print("_");
                else if ((i > 0 && j == 4)) System.out.print("|");
                else if ((i == 1 && j == 5)) System.out.print("/");
                else if ((mistakeCount > 0 && i == 1 && j == 12)) System.out.print("|");
                else if ((mistakeCount > 0 && i == 2 && j == 11)){System.out.print("(_)"); break;}
                else if ((mistakeCount > 1 && i == 3 && j == 11)) System.out.print("\\");
                else if ((mistakeCount > 2 && i == 3 && j == 12)) System.out.print("|");
                else if ((mistakeCount > 3 && i == 3 && j == 13)) System.out.print("/");
                else if ((mistakeCount > 4 && i == 4 && j == 12)) System.out.print("|");
                else if ((mistakeCount > 5 && i == 5 && j == 11)) System.out.print("/");
                else if ((mistakeCount > 6 && i == 5 && j == 13)) System.out.print("\\");
                else System.out.print(" ");
            }
            System.out.println();
        }
    }

    public static char getLowCaseChar(){
        return Character.toLowerCase(inputString().charAt(0));
    }

    public static void printMistakes (int mistakeCount){
        System.out.println("Mistakes: " + mistakeCount);
    }

    public static void printWord (String Word){
        System.out.println(Word);
    }

    public static boolean isWordContainsChar(String seekWord, char charInput){
        return seekWord.indexOf(charInput) != -1;
    }

    public static boolean isIgnoreSymbols (char charInput){
        return charInput <= 127;
    }

    public static String maskBuilder(String seekWord, char charInput, String maskWord){
        StringBuilder tempString = new StringBuilder(maskWord);
        for(int i = 0; i < seekWord.length(); i++) {
            if (seekWord.charAt(i) == charInput) {
                tempString.setCharAt(i, charInput);
            }
        }
        maskWord = tempString.toString();
        return maskWord;
    }

    public static void gameResult (int mistakeCount){
        printHangMan(mistakeCount);
        if (mistakeCount < 7) {
            System.out.println("Congratulations you win!");
        } else {
            System.out.println("Oops...someone is dead");
        }
        printMistakes(mistakeCount);
    }
}
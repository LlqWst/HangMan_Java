package Hangman;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Main {

    final static int START = 1;
    final static int EXIT = 2;

    public static void main(String[] args) {
        while (true){
            printInitialScreen();
            if (awaitInitialInput() == EXIT) System.exit(0);
            int mistakeCount = 0;
            List<Character> incorrectCharsList = new ArrayList<>();
            List<String> listWithWords = readWordsFromFile();
            String seekWord = getRandomWord(listWithWords);
            String maskWord = fillingMask(seekWord);
            do  {
                printHangMan(mistakeCount);
                printInputtedIncorrectChars(incorrectCharsList);
                printMistakesCount(mistakeCount);
                printMaskWord(maskWord);
                String entryByPlayer = getPlayerInput();
                if(!(isInputCorrect(entryByPlayer))){
                    printNotificationIncorrectInput();
                    continue;
                }
                char inputChar = getLowCaseChar(entryByPlayer);
                if (isDuplicatedIncorrectChar(inputChar, incorrectCharsList)){
                    printNotificationDuplicatedIncorrectChar();
                } else if (isDuplicatedCorrectChar(inputChar, maskWord)) {
                     printNotificationDuplicatedCorrectChar();
                } else if (isWordContainsInputChar(seekWord, inputChar)){
                     maskWord = rebuildMaskWord(seekWord, inputChar, maskWord);
                } else {
                    mistakeCount++;
                    incorrectCharsList.add(inputChar);
                }
            } while (mistakeCount < 7 && !seekWord.equals(maskWord));
            printHangMan(mistakeCount);
            printResult(mistakeCount);
            printInputtedIncorrectChars(incorrectCharsList);
            printMistakesCount(mistakeCount);
            printSeekWord(seekWord);
        }
    }

    private static int awaitInitialInput(){
        int entry = 0;
        do {
            switch (getPlayerInput()) {
                case "старт":
                    entry = START;
                    break;
                case "выход":
                    entry = EXIT;
                    break;
                default:
                    System.out.println("Уведомление: Некорректный ввод!");
                    System.out.println("Ожидается 'старт' или 'выход'");
                    System.out.println("Ввод должн быть без одинарных кавычек");
            }
        } while (entry != START && entry != EXIT);
        return entry;
    }

    private static String getPlayerInput(){
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private static List<String> readWordsFromFile(){
        List<String> listWithWords = new ArrayList<>();
        try {
            FileReader filePath = new FileReader("src/Hangman/WordsNouns.txt");
            BufferedReader reader = new BufferedReader(filePath);
            String line = reader.readLine();
            while (line != null) {
                listWithWords.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return listWithWords;
    }

    private static String getRandomWord(List<String> listWithWords) {
        int numberLine = getRandInt(listWithWords.size());
        return listWithWords.get(numberLine);
    }

    private static int getRandInt (int linesNumber){
        Random rand = new Random();
        return rand.nextInt(linesNumber);
    }

    private static String fillingMask(String seekWord){
        return "*".repeat(seekWord.length());
    }

    private static boolean isInputCorrect(String PlayerInput){
        return PlayerInput.length() == 1 && !isSymbolNotCyrillic(PlayerInput.charAt(0));
    }

    private static boolean isSymbolNotCyrillic(char inputChar){
        return Character.UnicodeBlock.of(inputChar) != Character.UnicodeBlock.CYRILLIC;
    }

    private static boolean isDuplicatedIncorrectChar(char inputChar, List<Character> incorrectCharsList){
        return incorrectCharsList.contains(inputChar);
    }

    private static boolean isDuplicatedCorrectChar(char inputChar, String maskWord){
        return maskWord.indexOf(inputChar) != -1;
    }
    private static char getLowCaseChar(String playerInput){
        return Character.toLowerCase(playerInput.charAt(0));
    }

    private static boolean isWordContainsInputChar(String seekWord, char inputChar){
        return seekWord.indexOf(inputChar) != -1;
    }

    private static String rebuildMaskWord(String seekWord, char inputChar, String maskWord){
        StringBuilder tempString = new StringBuilder(maskWord);
        for(int i = 0; i < seekWord.length(); i++) {
            if (seekWord.charAt(i) == inputChar) {
                tempString.setCharAt(i, inputChar);
            }
        }
        maskWord = tempString.toString();
        return maskWord;
    }

    private static void printInitialScreen (){
        System.out.println();
        System.out.println("|------------------------------Виселица-----------------------------|");
        System.out.println("|Для начала игры введите 'старт', для выхода из игры введите 'выход'|");
        System.out.println("|Ввод должн быть без одинарных кавычек                              |");
        System.out.println("|-------------------------------------------------------------------|");
    }

    private static void  printHangMan (int mistakeCount) {
        String[][] versionOfHangMan = {
                {"""
                     +--------+
                     |/
                     |
                     |
                     |
                     |
                     |
                 ____|____
                 """},
                {"""
                     +--------+
                     |/       |
                     |       (_)
                     |
                     |
                     |
                     |
                 ____|____
                 """},
                {"""
                     +--------+
                     |/       |
                     |       (_)
                     |       \\
                     |
                     |
                     |
                 ____|____
                 """},
                {"""
                     +--------+
                     |/       |
                     |       (_)
                     |       \\|
                     |
                     |
                     |
                 ____|____
                 """},
                {"""
                     +--------+
                     |/       |
                     |       (_)
                     |       \\|/
                     |
                     |
                     |
                 ____|____
                 """},
                {"""
                     +--------+
                     |/       |
                     |       (_)
                     |       \\|/
                     |        |
                     |
                     |
                 ____|____
                 """},
                {"""
                     +--------+
                     |/       |
                     |       (_)
                     |       \\|/
                     |        |
                     |       /
                     |
                 ____|____
                 """},
                {"""
                     +--------+
                     |/       |
                     |       (_)
                     |       \\|/
                     |        |
                     |       / \\
                     |
                 ____|____
                 """}
        };
        System.out.print(String.join("", versionOfHangMan[mistakeCount]));
    }

    private static void printNotificationIncorrectInput (){
        System.out.println("Уведомление: Некорректный ввод!");
        System.out.println("Ожидается ввод только одной буквы кириллицы");
    }

    private static void printNotificationDuplicatedIncorrectChar (){
        System.out.println("Уведомление: повторно введена некорректная буква!");
    }

    private static void printNotificationDuplicatedCorrectChar (){
        System.out.println("Уведомление: повторно введена угаданная буква!");
    }

    private static void printInputtedIncorrectChars(List<Character> incorrectCharsList){
        System.out.println("Неправильно введенные буквы:" + incorrectCharsList);
    }

    private static void printMistakesCount (int mistakeCount){
        System.out.println("Количество ошибок: " + mistakeCount);
    }

    private static void printSeekWord (String seekWord){
        System.out.println("Загаданное слово: " + seekWord);
    }

    private static void printMaskWord(String maskWord){
        System.out.println("Текущий результат: " + maskWord);
    }

    private static void printResult(int mistakeCount){
        if (mistakeCount < 7) {
            System.out.println("Поздравляю! Ты выиграл!");
        } else {
            System.out.println("Опа...кто-то не выжил. Игра окончена.");
        }
    }
}
package Hangman;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    private final static String CMD_START = "старт";
    private final static String CMD_EXIT = "выход";
    private final static int MAX_MISTAKES = 6;
    private static final Random RANDOM = new Random();
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final String[][] versionOfHangMan = {
            {"""
                     +--------+
                     |/
                     |
                     |
                     |
                     |
                     |
                 ____|____
                 """}, {"""
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
                     |        |
                     |        |
                     |
                     |
                 ____|____
                 """},
    {"""
                     +--------+
                     |/       |
                     |       (_)
                     |       \\|
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

    public static void main(String[] args) {
        while (true){
            printInitialScreen();
            if (awaitInitialInput().equals(CMD_EXIT)) System.exit(0);
            start();
        }
    }

    private static void start(){
        int mistakeCount = 0;
        boolean isGameOver = false;
        List<Character> wrongLetters = new ArrayList<>();
        List<String> words = readWords();
        String seekWord = getRandomWord(words);
        String maskWord = fillingMask(seekWord);
        do  {
            printHangMan(mistakeCount);
            showWrongLetters(wrongLetters);
            printMistakesCount(mistakeCount);
            printMaskWord(maskWord);
            String entryByPlayer = getPlayerInput();
            if(!(isOneLetter(entryByPlayer))){
                notifyWrongInput();
                continue;
            }
            char inputLetter = getLetter(entryByPlayer);
            if (isWrongDuplicated(inputLetter, wrongLetters)){
                notifyWrongsDuplicated();
            } else if (isCorrectDuplicated(inputLetter, maskWord)) {
                notifyCorrectDuplicated();
            } else if (isCorrectLetter(seekWord, inputLetter)){
                maskWord = rebuildMask(seekWord, inputLetter, maskWord);
            } else {
                mistakeCount++;
                wrongLetters.add(inputLetter);
            }
            isGameOver = mistakeCount >= MAX_MISTAKES || seekWord.equals(maskWord);
        } while (!isGameOver);
        printHangMan(mistakeCount);
        printResult(mistakeCount);
        showWrongLetters(wrongLetters);
        printMistakesCount(mistakeCount);
        printSeekWord(seekWord);
    }

    private static String awaitInitialInput(){
        while(true) {
            switch (getPlayerInput()) {
                case "старт":
                    return CMD_START;
                case "выход":
                    return CMD_EXIT;
                default:
                    System.out.println("Уведомление: Некорректный ввод!");
                    System.out.println("Ожидается 'старт' или 'выход'");
                    System.out.println("Ввод должн быть без одинарных кавычек");
            }
        }
    }

    private static String getPlayerInput(){
        return SCANNER.nextLine().toLowerCase();
    }

    private static List<String> readWords(){
        List<String> words = new ArrayList<>();
        try {
            FileReader filePath = new FileReader("src/Hangman/WordsNouns.txt");
            BufferedReader reader = new BufferedReader(filePath);
            String line = reader.readLine();
            while (line != null) {
                words.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return words;
    }

    private static String getRandomWord(List<String> words) {
        int size = words.size();
        int numberLine = RANDOM.nextInt(size);
        return words.get(numberLine);
    }

    private static String fillingMask(String seekWord){
        return "*".repeat(seekWord.length());
    }

    private static boolean isOneLetter(String s){
        return s.length() == 1 && isCyrillic(s.charAt(0));
    }

    private static boolean isCyrillic(char letter){
        return Character.UnicodeBlock.of(letter) == Character.UnicodeBlock.CYRILLIC;
    }

    private static boolean isWrongDuplicated(char inputChar, List<Character> incorrectLetters){
        return incorrectLetters.contains(inputChar);
    }

    private static boolean isCorrectDuplicated(char inputChar, String maskWord){
        return maskWord.indexOf(inputChar) != -1;
    }

    private static char getLetter(String playerInput){
        return playerInput.charAt(0);
    }

    private static boolean isCorrectLetter(String seekWord, char inputChar){
        return seekWord.indexOf(inputChar) != -1;
    }

    private static String rebuildMask(String seekWord, char inputChar, String mask){
        StringBuilder result = new StringBuilder(mask);
        for(int i = 0; i < seekWord.length(); i++) {
            if (seekWord.charAt(i) == inputChar) {
                result.setCharAt(i, inputChar);
            }
        }
        return result.toString();
    }

    private static void printInitialScreen (){
        System.out.println();
        System.out.println("|------------------------------Виселица-----------------------------|");
        System.out.printf("|Для начала игры введите '%s', для выхода из игры введите '%s'| %n", CMD_START, CMD_EXIT);
        System.out.println("|Ввод должн быть без одинарных кавычек                              |");
        System.out.println("|-------------------------------------------------------------------|");
    }

    private static void  printHangMan (int mistakeCount) {
        System.out.print(String.join("", versionOfHangMan[mistakeCount]));
    }

    private static void notifyWrongInput(){
        System.out.println("Уведомление: Некорректный ввод!");
        System.out.println("Ожидается ввод только одной буквы кириллицы");
    }

    private static void notifyWrongsDuplicated(){
        System.out.println("Уведомление: повторно введена некорректная буква!");
    }

    private static void notifyCorrectDuplicated(){
        System.out.println("Уведомление: повторно введена угаданная буква!");
    }

    private static void showWrongLetters(List<Character> wrongLetters){
        System.out.println("Неправильно введенные буквы:" + wrongLetters);
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
        if (mistakeCount < MAX_MISTAKES) {
            System.out.println("Поздравляю! Ты выиграл!");
        } else {
            System.out.println("Опа...кто-то не выжил. Отгадывающий проиграл.");
        }
    }
}
package aoc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Day3 {
    public static void main(String[] args) throws IOException {
        String fileInput = FileUtils.readFile("src/resources/aoc.Day3-Input.txt");
        List<String> inputLine = Arrays.stream(fileInput.split("\r\n")).toList();
        String[][] inputCharacters = inputLine.stream().map(line -> line.split("")).toArray(String[][]::new);

        List<List<Integer>> numbersForCharacters = new ArrayList<>();
        List<Integer> gearRatios = new ArrayList<>();


        //loop through each line
        for (int i = 0; i < inputCharacters.length; i++) {
            String[] line = inputCharacters[i];
            for (int j = 0; j < line.length; j++) {
                if (isSymbol(inputCharacters[i][j])) {
                    List<Integer> digits = scanAroundSymbol(inputCharacters, i, j);
                    numbersForCharacters.add(digits);

                    if (!inputCharacters[i][j].equals("*")) {
                        continue;
                    }
                    if (digits.size() != 2) {
                        continue;
                    }
                    gearRatios.add(digits.get(0) * digits.get(1));
                }
            }
        }

        List<Integer> allNumbers = numbersForCharacters.stream().flatMap(Collection::stream).toList();

        Integer sumOfNumbers = allNumbers.stream().reduce(Integer::sum).get();
        System.out.println(sumOfNumbers);

        Integer sumOfGearRatios = gearRatios.stream().reduce(Integer::sum).get();
        System.out.println(sumOfGearRatios);
    }

    private static List<Integer> scanAroundSymbol(String[][] inputCharacters, int y, int x) {
        char topLeft = inputCharacters[y - 1][x - 1].charAt(0);
        char top = inputCharacters[y - 1][x].charAt(0);
        char topRight = inputCharacters[y - 1][x + 1].charAt(0);
        char left = inputCharacters[y][x - 1].charAt(0);
        char right = inputCharacters[y][x + 1].charAt(0);
        char bottomLeft = inputCharacters[y + 1][x - 1].charAt(0);
        char bottom = inputCharacters[y + 1][x].charAt(0);
        char bottomRight = inputCharacters[y + 1][x + 1].charAt(0);


        List<Integer> digits = new ArrayList<>();

        //checkTopRow
        if (Character.isDigit(topLeft)) {
            String number = scanForLeftNumbers(inputCharacters, y - 1, x - 1);
            digits.add(Integer.parseInt(number));
        }
        if (Character.isDigit(top)) {
            String number = inputCharacters[y - 1][x];
            fixOverlap(topLeft, digits, number);
        }
        if (Character.isDigit(topRight)) {
            String number = scanForRightNumbers(inputCharacters, y - 1, x + 1);
            fixOverlap(top, digits, number);
        }
        //check left and right
        if (Character.isDigit(left)) {
            String number = scanForLeftNumbers(inputCharacters, y, x - 1);
            digits.add(Integer.parseInt(number));
        }
        if (Character.isDigit(right)) {
            String number = scanForRightNumbers(inputCharacters, y, x + 1);
            digits.add(Integer.parseInt(number));
        }

        //check bottom row
        if (Character.isDigit(bottomLeft)) {
            String number = scanForLeftNumbers(inputCharacters, y + 1, x - 1);
            digits.add(Integer.parseInt(number));
        }

        if (Character.isDigit(bottom)) {
            String number = inputCharacters[y + 1][x];
            fixOverlap(bottomLeft, digits, number);
        }

        if (Character.isDigit(bottomRight)) {
            String number = scanForRightNumbers(inputCharacters, y + 1, x + 1);
            fixOverlap(bottom, digits, number);
        }

        return digits;
    }

    private static void fixOverlap(Character character, List<Integer> digits, String number) {
        if (Character.isDigit(character)) {
            Integer combinedNumber = Integer.valueOf(String.valueOf(digits.get(digits.size() - 1)) + number);
            digits.remove(digits.size() - 1);
            digits.add(combinedNumber);
        } else {
            digits.add(Integer.parseInt(number));
        }
    }

    private static String scanForLeftNumbers(String[][] inputCharacters, int y, int x) {
        String startingPoint = inputCharacters[y][x];
        StringBuilder sb = new StringBuilder();
        sb.append(startingPoint);

        boolean scanningLeft = true;
        int xOffsetLeft = 1;
        while (scanningLeft) {
            if (Character.isDigit(inputCharacters[y][x - xOffsetLeft].charAt(0))) {
                sb.insert(0, inputCharacters[y][x - xOffsetLeft]);
                xOffsetLeft += 1;
            } else {
                scanningLeft = false;
            }
            if (x - xOffsetLeft == -1) {
                scanningLeft = false;
            }
        }

        return sb.toString();
    }

    private static String scanForRightNumbers(String[][] inputCharacters, int y, int x) {
        String startingPoint = inputCharacters[y][x];
        StringBuilder sb = new StringBuilder();
        sb.append(startingPoint);

        boolean scanningRight = true;
        int xOffsetRight = 1;
        while (scanningRight) {
            if (Character.isDigit(inputCharacters[y][x + xOffsetRight].charAt(0))) {
                sb.append(inputCharacters[y][x + xOffsetRight]);
                xOffsetRight += 1;
            } else {
                scanningRight = false;
            }

            if (x + xOffsetRight > 139) {
                int i = 0;
            }

            if (x + xOffsetRight == inputCharacters[y].length) {
                scanningRight = false;
            }
        }

        return sb.toString();
    }

    private static boolean isSymbol(String input) {
        Character character = input.charAt(0);
        return !(Character.isDigit(character) || character.equals('.'));
    }
}

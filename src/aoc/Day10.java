package aoc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day10 {
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        String fileInput = FileUtils.readFile("src/resources/Day10-Input.txt");
        List<String> inputLine = Arrays.stream(fileInput.split("\r\n")).toList();
        Character[][] inputCharacters = inputLine.stream().map(line -> Arrays.stream(line.split("")).map(letter -> letter.charAt(0)).toArray(Character[]::new)).toArray(Character[][]::new);

        Integer[] startingLocation = getStartingCharacter(inputCharacters);
        int startY = startingLocation[0];
        int startX = startingLocation[1];

        List<Integer[]> pathLocations = scanAroundSymbol(inputCharacters, startY, startX);

        Integer[] path1 = pathLocations.get(0);
        char currentLetter = inputCharacters[path1[0]][path1[1]];

        Integer[] newLocation = changeDirection(currentLetter, startY, startX, path1[0], path1[1]);


//        for (Integer[] path : pathLocations) {
//
//        }


    }

    private static Integer[] changeDirection(char letter, int currentY, int currentX, int nextY, int nextX) {
        if (letter == '|' && currentY - nextY == 1) {
            return new Integer[]{nextY - 1, nextX};
        }

        if (letter == '|' && currentY - nextY == -1) {
            return new Integer[]{nextY + 1, nextX};
        }

        if (letter == '-' && currentX - nextX == 1) {
            return new Integer[]{nextY - 1, nextX};
        }

        if (letter == '-' && currentX - nextX == -1) {
            return new Integer[]{nextY + 1, nextX};
        }

        if (letter == 'F' && currentX - nextX == 1) {
            return new Integer[]{nextY + 1, nextX};
        }
        if (letter == 'F' && currentX - nextX == 0) {
            return new Integer[]{nextY, nextX + 1};
        }

        if (letter == '7' && currentX - nextX == -1) {
            return new Integer[]{nextY + 1, nextX};
        }
        if (letter == '7' && currentX - nextX == 0) {
            return new Integer[]{nextY, nextX + 1};
        }

        return null;
    }

    private static List<Integer[]> scanAroundSymbol(Character[][] inputCharacters, int y, int x) {
        char top = inputCharacters[y - 1][x];
        char left = inputCharacters[y][x - 1];
        char right = inputCharacters[y][x + 1];
        char bottom = inputCharacters[y + 1][x];

        List<Integer[]> validPaths = new ArrayList<>();

        if (top == '7' || top == 'F' || top == '|') {
            validPaths.add(new Integer[]{y - 1, x});
        }
        if (bottom == 'L' || bottom == 'J' || bottom == '|') {
            validPaths.add(new Integer[]{y + 1, x});
        }

        if (left == 'L' || left == 'F' || left == '-') {
            validPaths.add(new Integer[]{y, x - 1});
        }
        if (right == '7' || right == 'J' || right == '-') {
            validPaths.add(new Integer[]{y, x + 1});
        }

        return validPaths;
    }

    private static Integer[] getStartingCharacter(Character[][] inputCharacters) {
        List<Integer> coordinates = new ArrayList<>();

        for (int i = 0; i < inputCharacters.length; i++) {
            Character[] line = inputCharacters[i];
            for (int j = 0; j < line.length; j++) {
                Character letter = line[j];
                if (letter.equals('S')) {
                    coordinates.add(i);
                    coordinates.add(j);
                }
            }
        }
        return coordinates.toArray(Integer[]::new);
    }
}

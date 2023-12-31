package aoc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day10 {
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        String fileInput = FileUtils.readFile("src/resources/Day10-Input.txt");
        List<String> inputLine = Arrays.stream(fileInput.split("\r\n")).toList();
        Character[][] inputCharacters = inputLine.stream().map(line -> Arrays.stream(line.split("")).map(letter -> letter.charAt(0)).toArray(Character[]::new)).toArray(Character[][]::new);
        Character[][] inputMask = deepCopy(inputCharacters);

        Integer[] startingLocation = getStartingCharacter(inputCharacters);
        int startY = startingLocation[0];
        int startX = startingLocation[1];
        inputCharacters[startY][startX] = 'L';
        inputMask[startY][startX] = 'X';

        List<CurrentLocation> pathLocations = scanAroundSymbol(inputCharacters, startY, startX);

        boolean isSearching = true;
        int stepsUntilIntersect = 1;

        while (isSearching) {
            for (int i = 0; i < pathLocations.size(); i++) {
                CurrentLocation currentLocation = pathLocations.get(i);
                char currentLetter = inputCharacters[currentLocation.currentY][currentLocation.currentX];
                inputMask[currentLocation.currentY][currentLocation.currentX] = 'X';
                CurrentLocation newLocation = changeDirection(currentLetter, currentLocation);
                pathLocations.add(i, newLocation);
                pathLocations.remove(currentLocation);
            }

            stepsUntilIntersect++;

            int allCurrentY = pathLocations.stream().map(pathLocation -> pathLocation.currentY).reduce((location1, location2) -> location1 - location2).get();
            int allCurrentX = pathLocations.stream().map(pathLocation -> pathLocation.currentX).reduce((location1, location2) -> location1 - location2).get();

            if (allCurrentY == 0 && allCurrentX == 0) {
                inputMask[pathLocations.get(0).currentY][pathLocations.get(0).currentX] = 'X';
                isSearching = false;
            }
        }

        int insideCount = 0;

        for (int i = 0; i < inputCharacters.length; i++) {
            for (int j = 0; j < inputCharacters[i].length; j++) {
                if (inputMask[i][j] == 'X') {
                    continue;
                }

                if (isInLoop(inputCharacters, inputMask, i, j)) {
                    inputMask[i][j] = 'I';
                    insideCount++;
                } else {
                    inputMask[i][j] = '0';
                }
            }
        }

        for (Character[] line : inputMask) {
            String values = Arrays.stream(line).map(String::valueOf).collect(Collectors.joining());

            System.out.println(values);
        }

        System.out.println(insideCount);
    }

    private static boolean isInLoop(Character[][] inputChars, Character[][] inputMask, int y, int x) {
        double lineHits = 0.0;
        char lastCharacter = '0';
        for (int i = x; i < inputChars[y].length; i++) {
            if (inputMask[y][i] == 'X') {
                char actualCharacter = inputChars[y][i];
                if (actualCharacter == '-') {
                    continue;
                } else if (actualCharacter == '|') {
                    lineHits += 1.0;
                } else if (lastCharacter == '0' && actualCharacter == 'F') {
                    lastCharacter = 'F';
                    lineHits += 0.5;
                } else if (lastCharacter == 'F' && actualCharacter == '7') {
                    lastCharacter = '0';
                    lineHits += 1.5;
                } else if (lastCharacter == 'F' && actualCharacter == 'J') {
                    lastCharacter = '0';
                    lineHits += 0.5;
                } else if (lastCharacter == '0' && actualCharacter == 'L') {
                    lastCharacter = 'L';
                    lineHits += 0.5;
                } else if (lastCharacter == 'L' && actualCharacter == 'J') {
                    lastCharacter = '0';
                    lineHits += 1.5;
                } else if (lastCharacter == 'L' && actualCharacter == '7') {
                    lastCharacter = '0';
                    lineHits += 0.5;
                }

            }
        }

        int intersectCount = (int) lineHits;

        return (intersectCount % 2) != 0;
    }

    public static Character[][] deepCopy(Character[][] original) {
        if (original == null) {
            return null;
        }

        int rows = original.length;
        int columns = original[0].length;

        // Create a new array with the same dimensions
        Character[][] copy = new Character[rows][columns];

        // Copy the values from the original array to the new array
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                copy[i][j] = original[i][j];
            }
        }

        return copy;
    }

    private static CurrentLocation changeDirection(char letter, CurrentLocation currentLocation) {
        if (letter == '|') {
            if (currentLocation.direction.equals(Direction.DOWN)) {
                return new CurrentLocation(currentLocation.currentY + 1, currentLocation.currentX, Direction.DOWN);
            }
            if (currentLocation.direction.equals(Direction.UP)) {
                return new CurrentLocation(currentLocation.currentY - 1, currentLocation.currentX, Direction.UP);
            }
        }
        if (letter == '-') {
            if (currentLocation.direction.equals(Direction.RIGHT)) {
                return new CurrentLocation(currentLocation.currentY, currentLocation.currentX + 1, Direction.RIGHT);
            }
            if (currentLocation.direction.equals(Direction.LEFT)) {
                return new CurrentLocation(currentLocation.currentY, currentLocation.currentX - 1, Direction.LEFT);
            }
        }
        if (letter == 'F') {
            if (currentLocation.direction.equals(Direction.LEFT)) {
                return new CurrentLocation(currentLocation.currentY + 1, currentLocation.currentX, Direction.DOWN);
            }
            if (currentLocation.direction.equals(Direction.UP)) {
                return new CurrentLocation(currentLocation.currentY, currentLocation.currentX + 1, Direction.RIGHT);
            }
        }
        if (letter == '7') {
            if (currentLocation.direction.equals(Direction.RIGHT)) {
                return new CurrentLocation(currentLocation.currentY + 1, currentLocation.currentX, Direction.DOWN);
            }
            if (currentLocation.direction.equals(Direction.UP)) {
                return new CurrentLocation(currentLocation.currentY, currentLocation.currentX - 1, Direction.LEFT);
            }
        }
        if (letter == 'L') {
            if (currentLocation.direction.equals(Direction.LEFT)) {
                return new CurrentLocation(currentLocation.currentY - 1, currentLocation.currentX, Direction.UP);
            }
            if (currentLocation.direction.equals(Direction.DOWN)) {
                return new CurrentLocation(currentLocation.currentY, currentLocation.currentX + 1, Direction.RIGHT);
            }
        }
        if (letter == 'J') {
            if (currentLocation.direction.equals(Direction.RIGHT)) {
                return new CurrentLocation(currentLocation.currentY - 1, currentLocation.currentX, Direction.UP);
            }
            if (currentLocation.direction.equals(Direction.DOWN)) {
                return new CurrentLocation(currentLocation.currentY, currentLocation.currentX - 1, Direction.LEFT);
            }
        }

        return null;
    }

    private static List<CurrentLocation> scanAroundSymbol(Character[][] inputCharacters, int y, int x) {
        char top = inputCharacters[y - 1][x];
        char left = inputCharacters[y][x - 1];
        char right = inputCharacters[y][x + 1];
        char bottom = inputCharacters[y + 1][x];

        List<CurrentLocation> validPaths = new ArrayList<>();

        if (top == '7' || top == 'F' || top == '|') {
            validPaths.add(new CurrentLocation(y - 1, x, Direction.UP));
        }
        if (bottom == 'L' || bottom == 'J' || bottom == '|') {
            validPaths.add(new CurrentLocation(y + 1, x, Direction.DOWN));
        }

        if (left == 'L' || left == 'F' || left == '-') {
            validPaths.add(new CurrentLocation(y, x - 1, Direction.LEFT));
        }
        if (right == '7' || right == 'J' || right == '-') {
            validPaths.add(new CurrentLocation(y, x + 1, Direction.RIGHT));
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

    private enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    private record CurrentLocation(int currentY, int currentX, Direction direction) {
    }
}

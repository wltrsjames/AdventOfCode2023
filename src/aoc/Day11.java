package aoc;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day11 {
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        String fileInput = FileUtils.readFile("src/resources/Day11-Input.txt");
        List<String> inputLine = Arrays.stream(fileInput.split("\r\n")).toList();
        List<List<Character>> inputCharacters = inputLine.stream().map(line -> Arrays.stream(line.split("")).map(letter -> letter.charAt(0)).collect(Collectors.toCollection(ArrayList::new))).collect(Collectors.toCollection(ArrayList::new));

        List<List<Integer>> missingLinesAndColumns = expandGalaxy(inputCharacters);

        Map<Integer, Integer[]> galaxyPoints = new HashMap<>();
        int galaxyId = 1;

        for (int i = 0; i < inputCharacters.size(); i++) {
            for (int j = 0; j < inputCharacters.get(i).size(); j++) {
                if (inputCharacters.get(i).get(j) == '#') {
                    galaxyPoints.put(galaxyId, new Integer[]{i, j});
                    galaxyId++;
                }
            }
        }

        List<Integer> galaxyIds = galaxyPoints.keySet().stream().toList();
        Map<String, Long> galaxyDistances = new HashMap<>();

        for (int i = 0; i < galaxyIds.size(); i++) {
            Integer currentGalaxyId = galaxyIds.get(i);
            Integer[] galaxyLocation = galaxyPoints.get(currentGalaxyId);
            for (int j = 0; j < galaxyIds.size(); j++) {
                if (j == i) {
                    continue;
                }
                Integer nextGalaxyId = galaxyIds.get(j);
                int[] galaxyIdsToCheck = new int[]{currentGalaxyId, nextGalaxyId};
                Arrays.sort(galaxyIdsToCheck);

                if (galaxyDistances.containsKey(Arrays.toString(galaxyIdsToCheck))) {
                    continue;
                }

                if (currentGalaxyId == 3 && nextGalaxyId == 6) {
                    int k = 0;
                }

                Integer[] nextGalaxyLocation = galaxyPoints.get(nextGalaxyId);
                long dist = calcDistance(galaxyLocation, nextGalaxyLocation, missingLinesAndColumns);
                galaxyDistances.put(Arrays.toString(galaxyIdsToCheck), dist);
            }
        }

        long totalDistances = galaxyDistances.values().stream().reduce(Long::sum).get();
        System.out.println(totalDistances);

    }

    private static long calcDistance(Integer[] galaxyLocation1, Integer[] galaxyLocation2, List<List<Integer>> missingLinesAndColumns) {
        List<Integer> missingLines = missingLinesAndColumns.get(0);
        List<Integer> missingColumns = missingLinesAndColumns.get(1);

        long intersectingLines = 0;
        long intersectingColumns = 0;

        if (galaxyLocation1[0] <= galaxyLocation2[0]) {
            intersectingLines = missingLines.stream().filter(missingLine -> galaxyLocation1[0] <= missingLine && missingLine <= galaxyLocation2[0]).toList().size();
        } else {
            intersectingLines = missingLines.stream().filter(missingLine -> galaxyLocation2[0] <= missingLine && missingLine <= galaxyLocation1[0]).toList().size();
        }

        if (galaxyLocation1[1] <= galaxyLocation2[1]) {
            intersectingColumns = missingColumns.stream().filter(missingLine -> galaxyLocation1[1] <= missingLine && missingLine <= galaxyLocation2[1]).toList().size();
        } else {
            intersectingColumns = missingColumns.stream().filter(missingLine -> galaxyLocation2[1] <= missingLine && missingLine <= galaxyLocation1[1]).toList().size();
        }

        return (Math.abs(galaxyLocation1[0] - galaxyLocation2[0]) + (intersectingLines * 1000000)) - intersectingLines +
                (Math.abs(galaxyLocation1[1] - galaxyLocation2[1]) + (intersectingColumns * 1000000) - intersectingColumns);
    }

    private static List<List<Integer>> expandGalaxy(List<List<Character>> galaxy) {
        List<Integer> lineWithNoHashes = new ArrayList<>();

        for (int i = 0; i < galaxy.size(); i++) {
            List<Character> characterLine = galaxy.get(i);
            Set<Character> uniqueCharacters = new HashSet<>(characterLine);

            if (uniqueCharacters.stream().count() == 1) {
                lineWithNoHashes.add(i);
            }
        }

        int charWidth = galaxy.get(0).size();

        for (int i = lineWithNoHashes.size() - 1; i >= 0; i--) {
            int lineNumber = lineWithNoHashes.get(i);
//            galaxy.add(lineNumber, getEmptyLine(charWidth));
        }

        List<Integer> rowWithNoHashes = new ArrayList<>();

        for (int j = 0; j < galaxy.get(0).size(); j++) {
            char character = galaxy.get(0).get(j);
            if (character == '.') {
                Set<Character> allVerticalChars = new HashSet<>();
                for (List<Character> inputCharacter : galaxy) {
                    allVerticalChars.add(inputCharacter.get(j));
                }

                if (allVerticalChars.size() == 1) {
                    rowWithNoHashes.add(j);
                }
            }
        }

        int charHeight = galaxy.size();

        for (int i = rowWithNoHashes.size() - 1; i >= 0; i--) {
            int rowNumber = rowWithNoHashes.get(i);
            for (int j = 0; j < charHeight; j++) {
//                galaxy.get(j).add(rowNumber, '.');
            }
        }
        List<List<Integer>> rowsAndColumnsMissing = new ArrayList<>();
        rowsAndColumnsMissing.add(lineWithNoHashes);
        rowsAndColumnsMissing.add(rowWithNoHashes);

        return rowsAndColumnsMissing;
    }

    private static List<Character> getEmptyLine(int charWidth) {
        return ".".repeat(charWidth).chars().mapToObj(c -> (char) c).collect(Collectors.toCollection(ArrayList::new));
    }
}

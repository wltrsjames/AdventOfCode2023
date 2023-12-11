package aoc;

import java.io.IOException;
import java.util.*;

public class Day4 {
    private static Map<Integer, List<Integer[][]>> memoisation = new HashMap<>();

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        String fileInput = FileUtils.readFile("src/resources/aoc.Day4-Input.txt");
        List<String> inputLines = Arrays.stream(fileInput.split("\r\n")).toList();
        List<String> inputsWithoutGame = inputLines.stream().map(inputLine -> inputLine.substring(inputLine.indexOf(": ") + 2)).toList();

        List<Integer[][]> allCardNumbers = inputsWithoutGame.stream().map(line -> {
            String[] lineSplit = line.split("\\|");
            Integer[] winningNumbers = Arrays.stream(lineSplit[0].trim().split("\\s+")).map(Integer::parseInt).toArray(Integer[]::new);
            Integer[] cardDigits = Arrays.stream(lineSplit[1].trim().split("\\s+")).map(Integer::parseInt).toArray(Integer[]::new);

            return new Integer[][]{winningNumbers, cardDigits};
        }).toList();

        List<Integer> cardPoints = allCardNumbers.stream().map(cardNumbers -> {
            long winningNumberCount = getWinningNumberCount(cardNumbers);

            return (int) (1 * Math.pow(2, winningNumberCount - 1));
        }).toList();

        Integer winningTotal = cardPoints.stream().reduce(Integer::sum).get();
        System.out.println(winningTotal);

        //part 2
        List<Integer[][]> collectedCards = new ArrayList<>();

        for (int i = 0; i < allCardNumbers.size(); i++) {
            Integer[][] card = allCardNumbers.get(i);
            collectedCards.add(card);

            long winningNumberCount = getWinningNumberCount(allCardNumbers.get(i));
            List<Integer[][]> copiesWonForCard = getCopiesFromCard(allCardNumbers, i + 1, (int) (i + 1 + winningNumberCount));
            collectedCards.addAll(copiesWonForCard);
        }

        System.out.println(collectedCards.size());

        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime) + "ms");
    }

    private static List<Integer[][]> getCopiesFromCard(List<Integer[][]> cards, Integer startingIndex, Integer endingIndex) {
        List<Integer[][]> collectedCards = new ArrayList<>();
        if (memoisation.containsKey(startingIndex - 1)) {
            collectedCards.addAll(memoisation.get(startingIndex - 1));
            return collectedCards;
        }

        for (int i = startingIndex; i < endingIndex; i++) {
            collectedCards.add(cards.get(i));
            long winningNumberCount = getWinningNumberCount(cards.get(i));

            List<Integer[][]> copyCards = getCopiesFromCard(cards, i + 1, (int) (i + 1 + winningNumberCount));
            collectedCards.addAll(copyCards);
        }

        memoisation.put(startingIndex - 1, collectedCards);

        return collectedCards;
    }

    private static long getWinningNumberCount(Integer[][] cardNumbers) {
        List<Integer> winningNumbers = Arrays.stream(cardNumbers[0]).toList();
        Integer[] cardDigits = cardNumbers[1];
        return Arrays.stream(cardDigits).filter(winningNumbers::contains).count();
    }


}

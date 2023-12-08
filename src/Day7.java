import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day7 {
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        String fileInput = FileUtils.readFile("src/resources/Day7-Input.txt");

        String[] lines = fileInput.split("\n");

        Map<Integer[][], Integer> cards = new HashMap<>();
        for (String line : lines) {
            String[] lineValues = line.replace("\r", "").split(" ");

            Integer[][] valueSplit = convertCardsToNumbers(lineValues[0].split(""));
            cards.put(valueSplit, Integer.parseInt(lineValues[1]));
        }

        Comparator<Integer[][]> handCompare = Comparator.comparing(intArray -> intArray, Comparator.comparingInt(o -> o[1][0]));
        Comparator<Integer[][]> highCard1 = handCompare.thenComparing(Comparator.comparingInt(o -> o[0][0]));
        Comparator<Integer[][]> highCard2 = highCard1.thenComparing(Comparator.comparingInt(o -> o[0][1]));
        Comparator<Integer[][]> highCard3 = highCard2.thenComparing(Comparator.comparingInt(o -> o[0][2]));
        Comparator<Integer[][]> highCard4 = highCard3.thenComparing(Comparator.comparingInt(o -> o[0][3]));
        Comparator<Integer[][]> highCard5 = highCard4.thenComparing(Comparator.comparingInt(o -> o[0][4]));

        List<Integer[][]> sortedCards = cards.keySet().stream().sorted(highCard5).toList();

        List<Integer> cardRankWinnings = new ArrayList<>();

        for (int i = 0; i < sortedCards.size(); i++) {
            Integer[][] cardNumbers = sortedCards.get(i);
            Integer cardBid = cards.get(cardNumbers);

            cardRankWinnings.add(cardBid * (i + 1));
        }

        System.out.println(cardRankWinnings.stream().reduce(Integer::sum).get());
    }

    private static Map<String, Integer> buildCardValues() {
        Map<String, Integer> cardMapper = new HashMap<>();
        cardMapper.put("J", 1);
        cardMapper.put("2", 2);
        cardMapper.put("3", 3);
        cardMapper.put("4", 4);
        cardMapper.put("5", 5);
        cardMapper.put("6", 6);
        cardMapper.put("7", 7);
        cardMapper.put("8", 8);
        cardMapper.put("9", 9);
        cardMapper.put("T", 10);
        cardMapper.put("Q", 11);
        cardMapper.put("K", 12);
        cardMapper.put("A", 13);

        return cardMapper;
    }

    private static Integer[][] convertCardsToNumbers(String[] cardValues) {

        Integer[] cardNumbers = Arrays.stream(cardValues).map(cardValue -> buildCardValues().get(cardValue)).toArray(Integer[]::new);

        return new Integer[][]{cardNumbers, getHandScore(cardNumbers)};
    }

    private static Integer findMostFrequentNumber(List<Integer> numbers) {
        // Grouping by count
        Map<Integer, Long> countByNumber = numbers.stream()
                .collect(Collectors.groupingBy(number -> number, Collectors.counting()));

        // Finding the entry with max count
        Map.Entry<Integer, Long> maxEntry = countByNumber.entrySet().stream()
                .sorted((entry1, entry2) -> Integer.compare(entry2.getKey(), entry1.getKey()))
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        // Returning the key (number) with max count
        return (maxEntry != null) ? maxEntry.getKey() : null;
    }

    private static Integer[] getHandScore(Integer[] cardValues) {
        int jackCardCount = (int) Arrays.stream(cardValues).filter(cardValue -> cardValue == 1).count();
        Integer[] cardValuesReplaced = cardValues;
        if (jackCardCount > 0) {
            Integer mostCommonNumber = findMostFrequentNumber(Arrays.stream(cardValues).toList());
            cardValuesReplaced = Arrays.stream(cardValues).map(cardValue -> cardValue.equals(1) ? mostCommonNumber : cardValue).toArray(Integer[]::new);
        }

        Set<Integer> uniqueNumbers = Arrays.stream(cardValuesReplaced).collect(Collectors.toSet());

        if (uniqueNumbers.size() == 5) {
            return new Integer[]{0};
        } else if (uniqueNumbers.size() == 4) {
            return new Integer[]{100};
        } else if (uniqueNumbers.size() == 3 && !isThreeOfAKind(uniqueNumbers, cardValuesReplaced)) {
            return new Integer[]{200};
        } else if (uniqueNumbers.size() == 3) {
            return new Integer[]{300};
        } else if (uniqueNumbers.size() == 2 && isFullHouse(uniqueNumbers, cardValuesReplaced)) {
            return new Integer[]{400};
        } else if (uniqueNumbers.size() == 2) {
            return new Integer[]{500};
        } else if (uniqueNumbers.size() == 1) {
            return new Integer[]{600};
        }

        return null;
    }

    private static boolean isFullHouse(Set<Integer> uniqueNumbers, Integer[] cardValues) {
        List<Integer> occurrenceCount = uniqueNumbers.stream().filter(uniqueNumber -> Arrays.stream(cardValues).filter(cardValue -> cardValue == uniqueNumber).count() == 2).toList();
        return (long) occurrenceCount.size() > 0;
    }

    private static boolean isThreeOfAKind(Set<Integer> uniqueNumbers, Integer[] cardValues) {
        List<Integer> occurrenceCount = uniqueNumbers.stream().filter(uniqueNumber -> Arrays.stream(cardValues).filter(cardValue -> cardValue == uniqueNumber).count() == 3).toList();
        return (long) occurrenceCount.size() > 0;
    }
}

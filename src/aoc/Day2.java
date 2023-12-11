package aoc;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day2 {
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        String fileInput = FileUtils.readFile("src/resources/aoc.Day2-Input.txt");

        List<String> games = Arrays.stream(fileInput.split("\r\n")).toList();

        List<Map<String, Integer>> gamesWithValues = games.stream().map(game -> {
            String gameWithoutGameText = game.substring(game.indexOf(": ") + 2);
            List<String> subsets = Arrays.stream(gameWithoutGameText.split("; ")).toList();

            Map<String, Integer> ballValues = new HashMap<>();
            subsets.forEach(subset -> {
                List<String> colourValues = Arrays.stream(subset.split(", ")).toList();
                colourValues.forEach(colourValue -> {
                    String[] values = colourValue.split(" ");
                    if (ballValues.containsKey(values[1])) {
                        Integer existingValue = ballValues.get(values[1]);
                        ballValues.put(values[1], Integer.max(Integer.parseInt(values[0]), existingValue));
                    } else {
                        ballValues.put(values[1], Integer.parseInt(values[0]));
                    }
                });
            });
            return ballValues;
        }).toList();

        int indexTotal = 0;

        for (int i = 0; i < gamesWithValues.size(); i++) {
            Map<String, Integer> game = gamesWithValues.get(i);
            if ((game.get("red") <= 12) &&
                    (game.get("green") <= 13) &&
                    (game.get("blue") <= 14)) {
                indexTotal += (i + 1);
            }
        }

        System.out.println(indexTotal);

        List<Integer> gamePowers = gamesWithValues.stream().map(gameWithValue -> {
            Integer greenValue = gameWithValue.get("green");
            Integer blueValue = gameWithValue.get("blue");
            Integer redValue = gameWithValue.get("red");

            return greenValue * blueValue * redValue;
        }).toList();

        Integer gamePowerTotal = gamePowers.stream().reduce(Integer::sum).get();
        System.out.println(gamePowerTotal);
        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime) + "ms");
    }
}

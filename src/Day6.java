import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day6 {
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        String fileInput = FileUtils.readFile("src/resources/Day6-Input.txt");

        String[] lines = fileInput.split("\n");
        Long[] times = Arrays.stream(lines[0].replace("Time:", "").trim().split("\\s+")).map(Long::parseLong).toArray(Long[]::new);
        Long[] distances = Arrays.stream(lines[1].replace("Distance:", "").trim().split("\\s+")).map(Long::parseLong).toArray(Long[]::new);
        Long[][] races = buildRaces(times, distances);

        // get distance for button time
        List<Long> raceWinConditions = Arrays.stream(races).map(race -> {
                long minimumButtonTime = 0;
                long maximumButtonTime = 0;

                for (long i = 1; i <= race[0]; i++) {
                    boolean didWin = doesWinRace(i, race);
                    if(didWin) {
                        minimumButtonTime = i;
                        break;
                    }
                }

                for (long i = race[0]; i >= 1; i--) {
                    boolean didWin = doesWinRace(i, race);
                    if(didWin) {
                        maximumButtonTime = i;
                        break;
                    }
                }

                return maximumButtonTime - minimumButtonTime + 1L;
        }).toList();

        Long raceConditionsMultiplied = raceWinConditions.stream().reduce(1L, (a, b) -> a * b);

        System.out.println("ans: " + raceConditionsMultiplied);
    }


    private static boolean doesWinRace(long buttonTime, Long[] race) {
        long raceTimeLimit = race[0];

        long timeToRace = raceTimeLimit - buttonTime;
        if(timeToRace <= 0) {
           return false;
        }

        long distanceTraveled = buttonTime * timeToRace;

        return distanceTraveled > race[1];
    }

    private static Long[][] buildRaces(Long[] times, Long[] distances) {
        Long[][] races = new Long[times.length][2];

        for (int i = 0; i < times.length; i++) {
            races[i][0] = times[i];
            races[i][1] = distances[i];
        }

        return races;
    }
}

package aoc;

import java.io.IOException;
import java.util.*;

public class Day8 {
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        String fileInput = FileUtils.readFile("src/resources/aoc.Day8-Input.txt");

        String[] rightLeftValues = fileInput.split("\r\n\r\n")[0].split("");
        String[] directions = fileInput.split("\r\n\r\n")[1].split("\r\n");

        Map<String, String[]> directionMap = new HashMap<>();
        for (String direction: directions) {
            String[] directionPath = direction.split(" = ");
            String key = directionPath[0];
            String[] value = directionPath[1].replace("(", "").replace(")", "").split(", ");
            directionMap.put(key, value);
        }

        List<String> endsWithA = directionMap.keySet().stream().filter(direction -> direction.endsWith("A")).toList();
        List<Long> pointsTraveledList = new ArrayList<>();

        for (String startingPoint: endsWithA) {
            boolean isSearching = true;
            String currentPoint = startingPoint;
            int directionIndex = 0;
            long pointsTraveled = 0;

            while (isSearching) {
                if(directionIndex > rightLeftValues.length -1) {
                    directionIndex = 0;
                }

                String direction = rightLeftValues[directionIndex];
                String[] newLocations = directionMap.get(currentPoint);

                currentPoint = newLocations[direction.equals("L")? 0 : 1];
                pointsTraveled++;
                if(currentPoint.endsWith("Z")) {
                    isSearching = false;
                } else {
                    directionIndex++;
                }
            }
            pointsTraveledList.add(pointsTraveled);
        }

        long LCM = lcm(pointsTraveledList.stream().toArray(Long[]::new));

        System.out.println(LCM);
    }

    private static long gcd(long a, long b)
    {
        while (b > 0)
        {
            long temp = b;
            b = a % b; // % is remainder
            a = temp;
        }
        return a;
    }

    private static long lcm(long a, long b)
    {
        return a * (b / gcd(a, b));
    }

    private static long lcm(Long[] input)
    {
        long result = input[0];
        for(int i = 1; i < input.length; i++) result = lcm(result, input[i]);
        return result;
    }
}

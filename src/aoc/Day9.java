package aoc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day9 {

    public static void main(String[] args) throws IOException {
//        part1();
        part2();
    }

    public static void part1() throws IOException {
        long startTime = System.currentTimeMillis();
        String fileInput = FileUtils.readFile("src/resources/Day9-Input.txt");
        String[] lines = fileInput.split("\r\n");
        Long[][] data = Arrays.stream(lines).map(line-> Arrays.stream(line.split(" ")).map(Long::parseLong).toArray(Long[]::new)).toArray(Long[][]::new);
        List<Long> allExtrapolatedValues = new ArrayList<>();

        for (int i = 0; i < data.length; i++) {
            Long[] historyValues = data[i];
            List<Long[]> extrapolatedValues = new ArrayList<>();
            extrapolatedValues.add(historyValues);

            getExtrapolatedValues(extrapolatedValues);

            for (int j = extrapolatedValues.size() -1; j >= 0; j--) {
                Long[] value = extrapolatedValues.get(j);
                if(Arrays.stream(value).reduce(Long::sum).get() == 0) {
                    extrapolatedValues.remove(j);
                    List<Long> replaceZero = new ArrayList<>(Arrays.stream(value).toList());
                    replaceZero.add( 0L);
                    extrapolatedValues.add(replaceZero.toArray(Long[]::new));
                } else {
                    Long[] rowBellow = extrapolatedValues.get(j + 1);
                    Long valueBellow = rowBellow[rowBellow.length -1];
                    Long nextValue = valueBellow + value[value.length -1];

                    extrapolatedValues.remove(j);
                    List<Long> replaceZero = new ArrayList<>(Arrays.stream(value).toList());
                    replaceZero.add( nextValue);
                    extrapolatedValues.add(j, replaceZero.toArray(Long[]::new));
                }
            }

            Long[] firstValues = extrapolatedValues.get(0);

            allExtrapolatedValues.add(firstValues[firstValues.length-1]);
        }

        System.out.println(allExtrapolatedValues.stream().reduce(Long::sum).get());

    }

    public static void part2() throws IOException {
        long startTime = System.currentTimeMillis();
        String fileInput = FileUtils.readFile("src/resources/Day9-Input.txt");
        String[] lines = fileInput.split("\r\n");
        Long[][] data = Arrays.stream(lines).map(line-> Arrays.stream(line.split(" ")).map(Long::parseLong).toArray(Long[]::new)).toArray(Long[][]::new);
        List<Long> allExtrapolatedValues = new ArrayList<>();

        for (int i = 0; i < data.length; i++) {
            Long[] historyValues = data[i];
            List<Long[]> extrapolatedValues = new ArrayList<>();
            extrapolatedValues.add(historyValues);

            getExtrapolatedValues(extrapolatedValues);

            for (int j = extrapolatedValues.size() -1; j >= 0; j--) {
                Long[] value = extrapolatedValues.get(j);
                if(Arrays.stream(value).reduce(Long::sum).get() == 0) {
                    extrapolatedValues.remove(j);
                    List<Long> replaceZero = new ArrayList<>(Arrays.stream(value).toList());
                    replaceZero.add(0, 0L);
                    extrapolatedValues.add(replaceZero.toArray(Long[]::new));
                } else {
                    Long[] rowBellow = extrapolatedValues.get(j + 1);
                    Long valueBellow = rowBellow[0];
                    Long nextValue = value[0] - valueBellow;

                    extrapolatedValues.remove(j);
                    List<Long> replaceZero = new ArrayList<>(Arrays.stream(value).toList());
                    replaceZero.add(0, nextValue);
                    extrapolatedValues.add(j, replaceZero.toArray(Long[]::new));
                }
            }

            Long[] firstValues = extrapolatedValues.get(0);

            allExtrapolatedValues.add(firstValues[0]);
        }

        System.out.println(allExtrapolatedValues.stream().reduce(Long::sum).get());

    }

    private static void getExtrapolatedValues(List<Long[]> extrapolatedValues) {
        List<Long> extrapolatedValueSet = new ArrayList<>();
        Long[] mostRecent = extrapolatedValues.get(extrapolatedValues.size() -1);
        for (int j = 1; j < mostRecent.length; j++) {
            extrapolatedValueSet.add(mostRecent[j] - mostRecent[j-1]);
        }

        extrapolatedValues.add(extrapolatedValueSet.toArray(Long[]::new));
        long valuesAdded = extrapolatedValueSet.stream().reduce(Long::sum).get();

        if (valuesAdded != 0) {
            getExtrapolatedValues(extrapolatedValues);
        }
    }
}

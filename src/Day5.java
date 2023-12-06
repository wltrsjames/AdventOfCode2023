import java.io.IOException;
import java.util.*;

public class Day5 {
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        String fileInput = FileUtils.readFile("src/resources/Day5-Input.txt");

        List<Long> seeds = Arrays.stream(fileInput.substring(fileInput.indexOf("seeds: ") + 7, fileInput.indexOf("\r\n")).split(" ")).map(Long::parseLong).toList();
        List<Long[]> seedRanges = new ArrayList<>();

        for (int i = 0; i < seeds.size(); i += 2) {
            Long[] seedRange = {seeds.get(i), seeds.get(i) + seeds.get(i + 1) - 1};
            seedRanges.add(seedRange);
        }

        Map<Long[], Long> seedToSoilMap = new HashMap<>();
        Map<Long[], Long> soilToFertMap = new HashMap<>();
        Map<Long[], Long> fertToWaterMap = new HashMap<>();
        Map<Long[], Long> waterToLightMap = new HashMap<>();
        Map<Long[], Long> lightToTempMap = new HashMap<>();
        Map<Long[], Long> tempToHumidityMap = new HashMap<>();
        Map<Long[], Long> humidityToLocationMap = new HashMap<>();

        populateMap(fileInput, "seed-to-soil map:", seedToSoilMap);
        populateMap(fileInput, "soil-to-fertilizer map:", soilToFertMap);
        populateMap(fileInput, "fertilizer-to-water map:", fertToWaterMap);
        populateMap(fileInput, "water-to-light map:", waterToLightMap);
        populateMap(fileInput, "light-to-temperature map:", lightToTempMap);
        populateMap(fileInput, "temperature-to-humidity map:", tempToHumidityMap);
        populateMap(fileInput, "humidity-to-location map:", humidityToLocationMap);

        long currentLowest = Long.MAX_VALUE;

        for (Long[] seedRange : seedRanges) {
            long start = seedRange[0];
            long end = seedRange[1];
            System.out.println(end - start + " loops");

            for (long seedId = start; seedId <= end; seedId++) {
                Long soilId = processRange(seedToSoilMap, seedId);
                Long fertId = processRange(soilToFertMap, soilId);
                Long waterId = processRange(fertToWaterMap, fertId);
                Long lightId = processRange(waterToLightMap, waterId);
                Long tempId = processRange(lightToTempMap, lightId);
                Long humidityId = processRange(tempToHumidityMap, tempId);
                Long locationId = processRange(humidityToLocationMap, humidityId);

                if (currentLowest > locationId) {
                    currentLowest = locationId;
                }
            }
            System.out.println("currentLowest: " + currentLowest);
        }

        System.out.println(currentLowest);

    }

    private static Long processRange(Map<Long[], Long> map, Long inputId) {
        Set<Long[]> ranges = map.keySet();

        Optional<Long> mapHit = ranges.stream().filter(range -> {
            return range[0] <= inputId && range[1] >= inputId;
        }).map(range -> {
            Long offset = inputId - range[0];
            return map.get(range) + offset;
        }).findFirst();

        return mapHit.orElse(inputId);
    }

    private static void populateMap(String fileInput, String searchTerm, Map<Long[], Long> map) {
        Arrays.stream(fileInput.split(searchTerm + "\r\n")[1].split("\r\n\r\n")[0].split("\r\n"))
                .forEach(entry -> {
                    Long[] values = Arrays.stream(entry.split(" ")).map(Long::parseLong).toArray(Long[]::new);
                    Long rangeLength = values[2] - 1;
                    Long[] range = {values[1], values[1] + rangeLength};
                    map.put(range, values[0]);
                });
    }
}

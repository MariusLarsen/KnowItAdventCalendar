package calendar2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KnowIt2 {
    private static final String FILE_PATH = "data/day2.txt";
    private static final String FLOAT_PATTERN = "(-?[0-9]*[.][0-9]+)";
    private static final double EARTH_RADIUS = 6371.0;
    private static final Pattern pattern = Pattern.compile(FLOAT_PATTERN + " " + FLOAT_PATTERN);

    public static void main(String[] args) {
        Collection<Vec3> cities = getCitiesFromFile();
        double totalDistance = findTotalDistance(cities);
        System.out.println(Math.round(totalDistance));
    }

    public static Collection<Vec3> getCitiesFromFile() {
        try (Stream<String> stream = Files.lines(Path.of(FILE_PATH))) {
            return stream.skip(1).map(KnowIt2::vec3FromString).collect(Collectors.toSet());
        } catch (IOException e) {
            return Collections.emptySet();
        }
    }

    private static Vec3 vec3FromString(String input) throws IllegalArgumentException {
        Matcher m = pattern.matcher(input);
        if (m.find())
            return Vec3.fromAngles(Double.parseDouble(m.group(1)), Double.parseDouble(m.group(2)));
        throw new IllegalArgumentException(String.format("\"%s\" does not contain the pattern: %s", input, pattern));
    }

    private static double findTotalDistance(Collection<Vec3> cities) {
        final Vec3 northPole = Vec3.fromAngles(0.0, 90.0);
        Vec3 currentCity = northPole;
        double totalDistance = 0;
        while (!cities.isEmpty()) {
            Vec3 closestCity = findClosestCity(currentCity, cities);
            totalDistance += distanceBetweenCities(currentCity, closestCity);
            currentCity = closestCity;
            cities.remove(currentCity);
        }
        totalDistance += distanceBetweenCities(currentCity, northPole);
        return totalDistance;
    }

    private static Vec3 findClosestCity(Vec3 currentCity, Collection<Vec3> cities) {
        double minDist = Double.MAX_VALUE;
        Vec3 closestCity = null;
        for (var city : cities) {
            double distance = distanceBetweenCities(currentCity, city);
            if (distance < minDist) {
                closestCity = city;
                minDist = distance;
            }
        }
        return closestCity;
    }

    private static double distanceBetweenCities(Vec3 v1, Vec3 v2) {
        return Vec3.angleBetweenUnitVectors(v1, v2) * EARTH_RADIUS;
    }

    private record Vec3(double x, double y, double z) {

        static Vec3 fromAngles(double alpha, double beta) {
            alpha = Math.toRadians(alpha);
            beta = Math.toRadians(beta);
            return new Vec3(Math.cos(alpha) * Math.cos(beta), Math.sin(alpha) * Math.cos(beta), Math.sin(beta));
        }

        static double angleBetweenUnitVectors(Vec3 v1, Vec3 v2) {
            return Math.acos(v1.x * v2.x + v1.y * v2.y + v1.z * v2.z);
        }
    }
}
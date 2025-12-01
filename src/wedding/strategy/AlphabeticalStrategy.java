package wedding.strategy;

import wedding.model.Family;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AlphabeticalStrategy implements SeatingStrategy {
    @Override
    public void arrange(List<Family> families) {
        Collections.sort(families, Comparator.comparing(Family::getName));
        System.out.println("Подредени по азбучен ред.");
    }
}

package wedding.strategy;

import wedding.model.Family;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BySizeStrategy implements SeatingStrategy {
    @Override
    public void arrange(List<Family> families) {
        Collections.sort(families, Comparator.comparingInt(Family::getGuestCount).reversed());
        System.out.println("Подредени по големина на семейството.");
    }
}

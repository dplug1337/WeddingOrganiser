package wedding.strategy;

import wedding.model.Family;
import java.util.List;

public class SeatingContext {
    private SeatingStrategy strategy;

    public void setStrategy(SeatingStrategy strategy) {
        this.strategy = strategy;
    }

    public void arrange(List<Family> families) {
        if (strategy == null) {
            System.out.println("Няма зададена стратегия!");
        } else {
            strategy.arrange(families);
        }
    }
}

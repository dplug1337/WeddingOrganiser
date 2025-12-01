package wedding.strategy;

import wedding.model.Family;
import java.util.List;

public interface SeatingStrategy {
    void arrange(List<Family> families);
}

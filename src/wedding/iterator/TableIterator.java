package wedding.iterator;

import wedding.model.GuestComponent;
import java.util.List;

public class TableIterator implements GuestIterator {
    private final List<GuestComponent> guests;
    private int position = 0;

    public TableIterator(List<GuestComponent> guests) {
        this.guests = guests;
    }

    @Override
    public boolean hasNext() {
        return position < guests.size();
    }

    @Override
    public GuestComponent next() {
        return guests.get(position++);
    }
}

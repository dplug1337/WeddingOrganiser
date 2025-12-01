package wedding.model;

public abstract class GuestComponent {
    public void add(GuestComponent guest) {
        throw new UnsupportedOperationException();
    }

    public void remove(GuestComponent guest) {
        throw new UnsupportedOperationException();
    }

    public GuestComponent getChild(int i) {
        throw new UnsupportedOperationException();
    }

    public String getName() {
        throw new UnsupportedOperationException();
    }

    public int getGuestCount() {
        throw new UnsupportedOperationException();
    }

    public void display() {
        throw new UnsupportedOperationException();
    }
}

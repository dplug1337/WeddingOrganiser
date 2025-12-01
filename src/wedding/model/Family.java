package wedding.model;

public class Family extends GuestComponent {
    private String name;
    private int guestCount;

    public Family(String name, int guestCount) {
        this.name = name;
        this.guestCount = guestCount;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getGuestCount() {
        return guestCount;
    }

    @Override
    public void display() {
        System.out.println("  Семейство: " + name + " (" + guestCount + " души)");
    }
}

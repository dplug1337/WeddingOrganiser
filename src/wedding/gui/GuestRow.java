package wedding.gui;

import javafx.beans.property.*;
import wedding.model.Family;

public class GuestRow {
    private final StringProperty tableName;
    private final StringProperty familyName;
    private final IntegerProperty guestCount;

    public GuestRow(String tableName, String familyName, int guestCount) {
        this.tableName = new SimpleStringProperty(tableName);
        this.familyName = new SimpleStringProperty(familyName);
        this.guestCount = new SimpleIntegerProperty(guestCount);
    }

    // Нова конструктор за Family
    public GuestRow(String tableName, Family family) {
        this(tableName, family.getName(), family.getGuestCount());
    }

    public StringProperty tableNameProperty() { return tableName; }
    public StringProperty familyNameProperty() { return familyName; }
    public IntegerProperty guestCountProperty() { return guestCount; }
}

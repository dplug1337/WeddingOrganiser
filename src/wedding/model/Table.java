package wedding.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import wedding.iterator.GuestIterator;
import wedding.iterator.TableIterator;

public class Table extends GuestComponent {
    private String name;
    // Вътрешно съхраняваме GuestComponent (Composite)
    private List<GuestComponent> guests = new ArrayList<>();

    public Table(String name) {
        this.name = name;
    }

    @Override
    public void add(GuestComponent guest) {
        // Ограничение: максимум 2 семейства или 10 души общо
        if (getTotalGuests() + guest.getGuestCount() <= 10 && getNumberOfFamilies() < 2) {
            guests.add(guest);
        } else {
            System.out.println("Не може да добавите още гости към " + name);
        }
    }

    @Override
    public void display() {
        System.out.println(name + " съдържа:");
        for (GuestComponent guest : guests) {
            guest.display();
        }
        System.out.println("Общо гости: " + getTotalGuests());
    }

    // Возвращаем итератор по GuestComponent
    public GuestIterator createIterator() {
        return new TableIterator(guests);
    }

    // Връща само списък от Family (за GUI/Strategy удобство)
    public List<Family> getGuests() {
        return guests.stream()
                .filter(g -> g instanceof Family)
                .map(g -> (Family) g)
                .collect(Collectors.toList());
    }

    // Общо хора на масата (сума от всички GuestComponent)
    public int getTotalGuests() {
        int total = 0;
        for (GuestComponent g : guests) {
            total += g.getGuestCount();
        }
        return total;
    }

    // Брой семейства (важно за ограничението "до 2 фамилии")
    public int getNumberOfFamilies() {
        int count = 0;
        for (GuestComponent g : guests) {
            if (g instanceof Family) count++;
        }
        return count;
    }

    public String getName() {
        return name;
    }

    // позволи премахване/изчистване
    public void clear() {
        guests.clear();
    }
}

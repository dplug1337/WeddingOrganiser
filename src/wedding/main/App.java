package wedding.main;

import wedding.model.*;

import static javax.swing.text.StyleConstants.Family;

import wedding.strategy.*;
import java.util.ArrayList;
import java.util.List;


public class App {
    public static void main(String[] args) {
        System.out.println("=== Wedding Organiser ===");

        Table table1 = new Table("Маса 1");
        Family ivanovi = new Family("Иванови", 4);
        Family petrovi = new Family("Петрови", 3);

        table1.add(ivanovi);
        table1.add(petrovi);

        table1.display();

        System.out.println("\n=== Обхождане чрез Iterator ===");
        var iterator = table1.createIterator();
        while (iterator.hasNext()) {
            GuestComponent g = iterator.next();
            System.out.println(" -> " + g.getName() + " (" + g.getGuestCount() + " души)");
        }

        System.out.println("\n=== Демонстрация на Strategy Pattern ===");
        List<Family> families = new ArrayList<>();
        families.add(new Family("Иванови", 4));
        families.add(new Family("Петрови", 3));
        families.add(new Family("Георгиеви", 6));

        SeatingContext context = new SeatingContext();

        // Подреждане по азбучен ред
        context.setStrategy(new AlphabeticalStrategy());
        context.arrange(families);
        for (Family f : families) {
            System.out.println(f.getName() + " - " + f.getGuestCount() + " души");
        }

        // Подреждане по големина
        context.setStrategy(new BySizeStrategy());
        context.arrange(families);
        for (Family f : families) {
            System.out.println(f.getName() + " - " + f.getGuestCount() + " души");
        }


    }
}

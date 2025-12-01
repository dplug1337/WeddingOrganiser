package wedding.model;

import java.util.HashSet;
import java.util.Set;

public class SeatingManager {
    private final Set<String> forbiddenPairs = new HashSet<>();

    // Добавя забрана между две фамилии (двупосочна)
    public void addForbiddenPair(String familyA, String familyB) {
        forbiddenPairs.add(key(familyA, familyB));
    }

    // Проверява дали две фамилии са забранени да седят заедно
    public boolean isForbidden(String familyA, String familyB) {
        return forbiddenPairs.contains(key(familyA, familyB));
    }

    // Помощен метод за нормализиране (независимо от реда на имената)
    private String key(String a, String b) {
        if (a.compareTo(b) < 0) {
            return a + "|" + b;
        } else {
            return b + "|" + a;
        }
    }

    //метод за изчистване
    public void clear() {
        forbiddenPairs.clear();
    }

    public Set<String> getAllForbiddenPairs() {
        return forbiddenPairs;
    }
}

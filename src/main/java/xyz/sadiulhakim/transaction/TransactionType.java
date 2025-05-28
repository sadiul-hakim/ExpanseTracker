package xyz.sadiulhakim.transaction;

public enum TransactionType {
    INCOME(1, "Income"), COST(2, "Cost"), DUE(3, "Due"), RECEIVABLE(4, "Receivable");

    private final int id;
    private final String name;

    TransactionType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static TransactionType getById(int id) {
        for (var type : values()) {
            if (type.getId() == id)
                return type;
        }
        return null;
    }

    public static TransactionType getByName(String name) {
        for (var type : values()) {
            if (type.getName().equals(name))
                return type;
        }
        return null;
    }

    public static TransactionType get(String text) {

        try {
            int id = Integer.parseInt(text);
            return getById(id);
        } catch (NumberFormatException e) {
            return getByName(text);
        }
    }
}

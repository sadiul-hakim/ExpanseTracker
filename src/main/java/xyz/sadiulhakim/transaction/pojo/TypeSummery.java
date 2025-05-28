package xyz.sadiulhakim.transaction.pojo;

import xyz.sadiulhakim.transaction.Currency;
import xyz.sadiulhakim.transaction.TransactionType;

public record TypeSummery(
        TransactionType type,
        Currency currency,
        double amount
) {
}

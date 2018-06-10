package monsterstack.io.partner.domain;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Transaction {

    private Instant transactionDate;
    private TransactionType type;
    private Double amount;
}

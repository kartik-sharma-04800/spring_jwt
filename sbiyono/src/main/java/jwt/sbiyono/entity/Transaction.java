package jwt.sbiyono.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String accountNumber;
    
    @Column(nullable = false)
    private String transactionType; // DEPOSIT, WITHDRAW, TRANSFER_IN, TRANSFER_OUT
    
    @Column(nullable = false)
    private Double amount;
    
    private String toAccountNumber; // For transfers
    
    @Column(nullable = false)
    private LocalDateTime transactionDate = LocalDateTime.now();
    
    @Column(nullable = false)
    private Double balanceAfter;
}

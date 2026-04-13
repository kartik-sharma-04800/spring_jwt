package jwt.sbiyono.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {
    
    @NotBlank(message = "To account number is required")
    private String toAccountNumber;
    
    @NotNull(message = "Amount is required")
    @Min(value = 1, message = "Amount must be at least ₹1")
    private Double amount;
}

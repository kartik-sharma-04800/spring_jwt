package jwt.sbiyono.controller;

import jakarta.validation.Valid;
import jwt.sbiyono.dto.*;
import jwt.sbiyono.service.BankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/banking")
public class BankingController {

    @Autowired
    private BankingService bankingService;

    @GetMapping("/account")
    public ResponseEntity<ApiResponse> getAccountDetails() {
        return ResponseEntity.ok(bankingService.getAccountDetails());
    }

    @GetMapping("/balance")
    public ResponseEntity<ApiResponse> getBalance() {
        return ResponseEntity.ok(bankingService.getBalance());
    }

    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse> deposit(@Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.ok(bankingService.deposit(request));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse> withdraw(@Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.ok(bankingService.withdraw(request));
    }

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse> transfer(@Valid @RequestBody TransferRequest request) {
        return ResponseEntity.ok(bankingService.transfer(request));
    }

    @PostMapping("/services/debit-card")
    public ResponseEntity<ApiResponse> optDebitCard() {
        return ResponseEntity.ok(bankingService.optDebitCard());
    }

    @PostMapping("/services/cheque-book")
    public ResponseEntity<ApiResponse> optChequeBook() {
        return ResponseEntity.ok(bankingService.optChequeBook());
    }

    @PostMapping("/services/passbook")
    public ResponseEntity<ApiResponse> optPassbook() {
        return ResponseEntity.ok(bankingService.optPassbook());
    }

    @PutMapping("/contact")
    public ResponseEntity<ApiResponse> updateContact(@Valid @RequestBody UpdateContactRequest request) {
        return ResponseEntity.ok(bankingService.updateContact(request));
    }

    @DeleteMapping("/account")
    public ResponseEntity<ApiResponse> closeAccount() {
        return ResponseEntity.ok(bankingService.closeAccount());
    }
}

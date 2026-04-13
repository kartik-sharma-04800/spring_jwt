package jwt.sbiyono.service;

import jwt.sbiyono.dto.*;
import jwt.sbiyono.entity.Transaction;
import jwt.sbiyono.entity.User;
import jwt.sbiyono.repository.TransactionRepository;
import jwt.sbiyono.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
public class BankingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private User getCurrentUser() {
        String username = getCurrentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private String generateAccountNumber() {
        Random random = new Random();
        StringBuilder accountNumber = new StringBuilder("SBI");
        for (int i = 0; i < 10; i++) {
            accountNumber.append(random.nextInt(10));
        }
        return accountNumber.toString();
    }

    @Transactional
    public ApiResponse register(RegisterRequest request) {
        // Validations
        if (request.getInitialDeposit() < 1000) {
            return new ApiResponse(false, "Can't open 0 balance account. Minimum deposit is ₹1000");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            return new ApiResponse(false, "Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return new ApiResponse(false, "Email already registered");
        }

        if (userRepository.existsByMobileNumber(request.getMobileNumber())) {
            return new ApiResponse(false, "Mobile number already registered");
        }

        // Create user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setMobileNumber(request.getMobileNumber());
        user.setEmail(request.getEmail());
        user.setBalance(request.getInitialDeposit());
        user.setAccountNumber(generateAccountNumber());

        userRepository.save(user);

        // Record transaction
        Transaction transaction = new Transaction();
        transaction.setAccountNumber(user.getAccountNumber());
        transaction.setTransactionType("DEPOSIT");
        transaction.setAmount(request.getInitialDeposit());
        transaction.setBalanceAfter(user.getBalance());
        transactionRepository.save(transaction);

        return new ApiResponse(true, "Account opened successfully", 
                "Account Number: " + user.getAccountNumber());
    }

    public ApiResponse authenticate(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            return new ApiResponse(true, "Authentication successful");
        } catch (Exception e) {
            return new ApiResponse(false, "Invalid username or password");
        }
    }

    public ApiResponse getAccountDetails() {
        User user = getCurrentUser();
        return new ApiResponse(true, "Account details retrieved",
                java.util.Map.of(
                    "accountNumber", user.getAccountNumber(),
                    "username", user.getUsername(),
                    "email", user.getEmail(),
                    "mobileNumber", user.getMobileNumber(),
                    "balance", "₹" + user.getBalance(),
                    "hasDebitCard", user.isHasDebitCard(),
                    "hasChequeBook", user.isHasChequeBook(),
                    "hasPassbook", user.isHasPassbook()
                ));
    }

    public ApiResponse getBalance() {
        User user = getCurrentUser();
        return new ApiResponse(true, "Current balance", "₹" + user.getBalance());
    }

    @Transactional
    public ApiResponse deposit(TransactionRequest request) {
        User user = getCurrentUser();
        
        user.setBalance(user.getBalance() + request.getAmount());
        userRepository.save(user);

        Transaction transaction = new Transaction();
        transaction.setAccountNumber(user.getAccountNumber());
        transaction.setTransactionType("DEPOSIT");
        transaction.setAmount(request.getAmount());
        transaction.setBalanceAfter(user.getBalance());
        transactionRepository.save(transaction);

        return new ApiResponse(true, "Amount deposited successfully", "New Balance: ₹" + user.getBalance());
    }

    @Transactional
    public ApiResponse withdraw(TransactionRequest request) {
        User user = getCurrentUser();

        if (user.getBalance() < request.getAmount()) {
            throw new RuntimeException("Insufficient funds. Available balance: ₹" + user.getBalance());
        }

        user.setBalance(user.getBalance() - request.getAmount());
        userRepository.save(user);

        Transaction transaction = new Transaction();
        transaction.setAccountNumber(user.getAccountNumber());
        transaction.setTransactionType("WITHDRAW");
        transaction.setAmount(request.getAmount());
        transaction.setBalanceAfter(user.getBalance());
        transactionRepository.save(transaction);

        return new ApiResponse(true, "Amount withdrawn successfully", "New Balance: ₹" + user.getBalance());
    }

    @Transactional
    public ApiResponse transfer(TransferRequest request) {
        User fromUser = getCurrentUser();
        
        User toUser = userRepository.findByAccountNumber(request.getToAccountNumber())
                .orElseThrow(() -> new RuntimeException("Recipient account not found"));

        if (!toUser.isActive()) {
            throw new RuntimeException("Recipient account is closed");
        }

        if (fromUser.getAccountNumber().equals(toUser.getAccountNumber())) {
            throw new RuntimeException("Cannot transfer to same account");
        }

        if (fromUser.getBalance() < request.getAmount()) {
            throw new RuntimeException("Insufficient funds. Available balance: ₹" + fromUser.getBalance());
        }

        // Debit from sender
        fromUser.setBalance(fromUser.getBalance() - request.getAmount());
        userRepository.save(fromUser);

        Transaction debitTransaction = new Transaction();
        debitTransaction.setAccountNumber(fromUser.getAccountNumber());
        debitTransaction.setTransactionType("TRANSFER_OUT");
        debitTransaction.setAmount(request.getAmount());
        debitTransaction.setToAccountNumber(toUser.getAccountNumber());
        debitTransaction.setBalanceAfter(fromUser.getBalance());
        transactionRepository.save(debitTransaction);

        // Credit to receiver
        toUser.setBalance(toUser.getBalance() + request.getAmount());
        userRepository.save(toUser);

        Transaction creditTransaction = new Transaction();
        creditTransaction.setAccountNumber(toUser.getAccountNumber());
        creditTransaction.setTransactionType("TRANSFER_IN");
        creditTransaction.setAmount(request.getAmount());
        creditTransaction.setToAccountNumber(fromUser.getAccountNumber());
        creditTransaction.setBalanceAfter(toUser.getBalance());
        transactionRepository.save(creditTransaction);

        return new ApiResponse(true, "Transfer successful", 
                "Transferred ₹" + request.getAmount() + " to " + toUser.getAccountNumber() + 
                ". New Balance: ₹" + fromUser.getBalance());
    }

    @Transactional
    public ApiResponse optDebitCard() {
        User user = getCurrentUser();
        
        if (user.isHasDebitCard()) {
            throw new RuntimeException("You already have a debit card");
        }

        user.setHasDebitCard(true);
        userRepository.save(user);

        return new ApiResponse(true, "Debit card issued successfully");
    }

    @Transactional
    public ApiResponse optChequeBook() {
        User user = getCurrentUser();
        
        if (user.isHasChequeBook()) {
            throw new RuntimeException("You already have a cheque book");
        }

        user.setHasChequeBook(true);
        userRepository.save(user);

        return new ApiResponse(true, "Cheque book issued successfully");
    }

    @Transactional
    public ApiResponse optPassbook() {
        User user = getCurrentUser();
        
        if (user.isHasPassbook()) {
            throw new RuntimeException("You already have a passbook");
        }

        user.setHasPassbook(true);
        userRepository.save(user);

        return new ApiResponse(true, "Passbook issued successfully");
    }

    @Transactional
    public ApiResponse updateContact(UpdateContactRequest request) {
        User user = getCurrentUser();

        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            if (userRepository.existsByEmail(request.getEmail()) && 
                !user.getEmail().equals(request.getEmail())) {
                throw new RuntimeException("Email already in use");
            }
            user.setEmail(request.getEmail());
        }

        if (request.getMobileNumber() != null && !request.getMobileNumber().isEmpty()) {
            if (userRepository.existsByMobileNumber(request.getMobileNumber()) && 
                !user.getMobileNumber().equals(request.getMobileNumber())) {
                throw new RuntimeException("Mobile number already in use");
            }
            user.setMobileNumber(request.getMobileNumber());
        }

        userRepository.save(user);
        return new ApiResponse(true, "Contact details updated successfully");
    }

    @Transactional
    public ApiResponse closeAccount() {
        User user = getCurrentUser();
        user.setActive(false);
        userRepository.save(user);

        return new ApiResponse(true, "Account closed successfully");
    }
}

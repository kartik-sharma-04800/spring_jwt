package jwt.sbiyono.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MainController {

    @GetMapping("/")
    public ResponseEntity<String> welcomeMessage(){
        String apiGuide = """
                <html>
                <head>
                    <style>
                        body { font-family: Arial, sans-serif; margin: 40px; background: #f5f5f5; }
                        .container { background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                        h1 { color: #0066cc; }
                        h2 { color: #333; margin-top: 30px; }
                        .endpoint { background: #f0f0f0; padding: 10px; margin: 10px 0; border-left: 4px solid #0066cc; }
                        .method { font-weight: bold; color: #0066cc; }
                        .protected { color: #ff6600; font-weight: bold; }
                        code { background: #eee; padding: 2px 6px; border-radius: 3px; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1>🏦 Welcome to State Bank of India Yono API</h1>
                        <p><strong>Base URL:</strong> http://localhost:8080</p>
                        <p><strong>JWT Token Expiry:</strong> 30 minutes</p>

                        <h2>📋 Public Endpoints (No Authentication Required)</h2>

                        <div class="endpoint">
                            <span class="method">POST</span> /api/auth/register<br>
                            <strong>Description:</strong> Open new account (Minimum deposit: ₹1000)<br>
                            <strong>Body:</strong> { "username", "password", "mobileNumber", "email", "initialDeposit" }<br>
                            <strong>Returns:</strong> Account Number
                        </div>

                        <div class="endpoint">
                            <span class="method">POST</span> /api/auth/login<br>
                            <strong>Description:</strong> Login to get JWT token<br>
                            <strong>Body:</strong> { "username", "password" }<br>
                            <strong>Returns:</strong> JWT Token (use in Authorization header)
                        </div>

                        <h2>🔒 Protected Endpoints (Requires JWT Token)</h2>
                        <p><span class="protected">⚠️ Add header:</span> <code>Authorization: Bearer &lt;your-jwt-token&gt;</code></p>

                        <h3>Account Information</h3>
                        <div class="endpoint">
                            <span class="method">GET</span> /api/banking/account<br>
                            <strong>Description:</strong> Get complete account details (account number, balance, services)
                        </div>

                        <div class="endpoint">
                            <span class="method">GET</span> /api/banking/balance<br>
                            <strong>Description:</strong> Check current balance
                        </div>

                        <h3>Transactions</h3>
                        <div class="endpoint">
                            <span class="method">POST</span> /api/banking/deposit<br>
                            <strong>Description:</strong> Deposit money<br>
                            <strong>Body:</strong> { "amount": 5000 }
                        </div>

                        <div class="endpoint">
                            <span class="method">POST</span> /api/banking/withdraw<br>
                            <strong>Description:</strong> Withdraw money (checks insufficient funds)<br>
                            <strong>Body:</strong> { "amount": 2000 }
                        </div>

                        <div class="endpoint">
                            <span class="method">POST</span> /api/banking/transfer<br>
                            <strong>Description:</strong> Transfer money to another account<br>
                            <strong>Body:</strong> { "toAccountNumber": "SBI1234567890", "amount": 1000 }
                        </div>

                        <h3>Banking Services (One per customer)</h3>
                        <div class="endpoint">
                            <span class="method">POST</span> /api/banking/services/debit-card<br>
                            <strong>Description:</strong> Opt for Debit Card (max 1)
                        </div>

                        <div class="endpoint">
                            <span class="method">POST</span> /api/banking/services/cheque-book<br>
                            <strong>Description:</strong> Opt for Cheque Book (max 1)
                        </div>

                        <div class="endpoint">
                            <span class="method">POST</span> /api/banking/services/passbook<br>
                            <strong>Description:</strong> Opt for Passbook (max 1)
                        </div>

                        <h3>Account Management</h3>
                        <div class="endpoint">
                            <span class="method">PUT</span> /api/banking/contact<br>
                            <strong>Description:</strong> Update email or mobile (username cannot be changed)<br>
                            <strong>Body:</strong> { "email": "new@email.com", "mobileNumber": "9988776655" }
                        </div>

                        <div class="endpoint">
                            <span class="method">DELETE</span> /api/banking/account<br>
                            <strong>Description:</strong> Close account permanently
                        </div>

                        <h2>💡 Quick Start Guide</h2>
                        <ol>
                            <li><strong>Register:</strong> POST to /api/auth/register with initial deposit ≥ ₹1000</li>
                            <li><strong>Login:</strong> POST to /api/auth/login to get JWT token</li>
                            <li><strong>Copy Token:</strong> Use the token in Authorization header for all protected endpoints</li>
                            <li><strong>Get Account Number:</strong> GET /api/banking/account to see your account number</li>
                            <li><strong>Start Banking:</strong> Use other endpoints with the JWT token</li>
                        </ol>

                        <h2>⚠️ Important Notes</h2>
                        <ul>
                            <li>Minimum opening deposit: <strong>₹1000</strong></li>
                            <li>Account number format: <strong>SBI + 10 digits</strong></li>
                            <li>Each service (Debit Card, Cheque Book, Passbook) can be opted only <strong>once</strong></li>
                            <li>Transfers check for <strong>insufficient funds</strong></li>
                            <li>Username <strong>cannot be changed</strong>, only email and mobile</li>
                            <li>JWT token expires in <strong>30 minutes</strong></li>
                        </ul>
                    </div>
                </body>
                </html>
                """;
        return ResponseEntity.ok(apiGuide);
    }


}

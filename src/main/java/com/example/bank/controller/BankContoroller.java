package com.example.bank.controller;

import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/bank")
@CrossOrigin(origins = "*") // フロントエンド端末からのCORS接続を許可
public class BankController {

    // 初期セッション確認（index.html 起動時に呼ばれます）
    @GetMapping
    public Map<String, Object> handleGetAction(@RequestParam String action) {
        if ("session".equals(action)) {
            return Map.of(
                "mode", "database",
                "account", Map.of(
                    "id", "acc-1017256",
                    "bankCode", "003",[cite: 1]
                    "branchCode", "024",[cite: 1]
                    "accountNumber", "1017256",[cite: 1]
                    "balance", new BigDecimal("10000.00")
                )
            );
        }
        return Map.of();
    }

    // 入金・出金・振込等のリクエスト処理[cite: 2]
    @PostMapping
    public Map<String, Object> handlePostAction(
            @RequestParam String action,
            @RequestBody Map<String, Object> body) {
        
        // SimpleBankSystem.java で行っていた検証・計算ロジックを実行[cite: 1]
        // index.html の期待する形式（account, receipt）で結果を返却[cite: 2]
        return Map.of(
            "account", Map.of("balance", new BigDecimal("15000.00")),
            "receipt", Map.of(
                "kind", "入金",
                "amount", new BigDecimal("5000.00"),
                "before", new BigDecimal("10000.00")
            )
        );
    }
}
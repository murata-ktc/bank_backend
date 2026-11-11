package com.example.bank;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

public class BankServer {
    private static BigDecimal balance = new BigDecimal("10000.00");

    public static void main(String[] args) throws IOException {
        // ポート8080、すべてのIP（0.0.0.0）からの接続を待受
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", 8080), 0);
        server.createContext("/api/bank", new BankHandler());
        server.setExecutor(null);
        
        System.out.println("=======================================");
        System.out.println("銀行APIサーバーが起動しました");
        System.out.println("URL: http://localhost:8080/api/bank");
        System.out.println("=======================================");
        server.start();
    }

    static class BankHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // 別端末（フロントエンド）からのCORS接続を許可
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            String query = exchange.getRequestURI().getQuery();
            String responseJson = "{}";

            // GETリクエスト（初期セッション確認）
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                if (query != null && query.contains("action=session")) {
                    responseJson = "{"
                        + "\"mode\":\"database\","
                        + "\"account\":{"
                        + "\"id\":\"acc-1017256\","
                        + "\"bankCode\":\"003\","[cite: 1]
                        + "\"branchCode\":\"024\","[cite: 1]
                        + "\"accountNumber\":\"1017256\","[cite: 1]
                        + "\"balance\":" + balance
                        + "}"
                        + "}";
                }
            } else if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                // POSTリクエスト時の仮返答
                responseJson = "{"
                    + "\"account\":{\"id\":\"acc-1017256\",\"bankCode\":\"003\",\"branchCode\":\"024\",\"accountNumber\":\"1017256\",\"balance\":" + balance + "},"
                    + "\"receipt\":{\"kind\":\"取引\",\"amount\":0,\"before\":" + balance + "}"
                    + "}";
            }

            byte[] responseBytes = responseJson.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(200, responseBytes.length);
            
            OutputStream os = exchange.getResponseBody();
            os.write(responseBytes);
            os.close();
        }
    }
}
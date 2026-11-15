package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Client;
import com.example.demo.service.clientService;

@RestController
public class SubsidyController {

    private final clientService service;

    public SubsidyController(clientService service) {
        this.service = service;
    }

    // 顧客一覧取得
    @GetMapping("/subsidies")
    public List<Client> getAllSubsidies() {
        return service.findAll();
    }

    // 顧客詳細取得
    @GetMapping("/subsidies/{id}")
    public Client getClient(@PathVariable Long id) {
        return service.findById(id);
    }

    // 顧客新規登録
    @PostMapping("/subsidies")
    public Client createClient(@RequestBody Client client) {
        return service.save(client);
    }

 // 顧客情報更新
    @PutMapping("/subsidies/{id}")
    public Client updateClient(
            @PathVariable Long id,
            @RequestBody Client client) {

        client.setId(id);

        return service.save(client);
    }

    // 顧客削除
    @DeleteMapping("/subsidies/{id}")
    public void deleteClient(@PathVariable Long id) {
        service.delete(id);
    }
}

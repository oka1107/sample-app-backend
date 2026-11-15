package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.Client;
import com.example.demo.repository.SubsidyRepository;

@Service
public class clientService {

    private final SubsidyRepository repository;

    public clientService(SubsidyRepository repository) {
        this.repository = repository;
    }

    // 顧客一覧取得
    public List<Client> findAll() {
        return repository.findAll();
    }

    // 顧客詳細取得
    public Client findById(Long id) {
        return repository.findById(id)
                .orElse(null);
    }

    // 顧客登録・更新
    public Client save(Client client) {
        return repository.save(client);
    }

    // 顧客削除
    public void delete(Long id) {
        repository.deleteById(id);
    }
}

package com.example.demo.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "client")
public class client {

    @Id
    private Long id;

    private String clientName;

    private String grantName;

    private Integer price;

    private LocalDate deadline;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getGrantName() {
        return grantName;
    }

    public void setGrantName(String grantName) {
        this.grantName = grantName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer subsidyAmount, Integer price) {
        this.price = price;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate applicationDeadline, LocalDate deadline) {
        this.deadline = deadline;
    }
}

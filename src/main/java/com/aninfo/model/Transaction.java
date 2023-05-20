package com.aninfo.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;
    private Double amount;

    private String type;

    @ManyToOne
    private Account account;

    // Constructor, getters y setters
    public Transaction() {
    }

    public void setAccount(Account account) {
        this.account = account;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String tipoDeTransaccion) {
        this.type = tipoDeTransaccion;
    }
}

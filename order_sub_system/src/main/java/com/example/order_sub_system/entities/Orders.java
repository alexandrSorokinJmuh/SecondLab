package com.example.order_sub_system.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "orders")
@Data
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer order_id;

    private Integer offer_id;
    private Integer customer_id;

    String name;
    Date deliveryTime;

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private Status status;
    boolean paid;
}

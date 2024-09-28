package com.example.booktickets.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {

    private String from;
    private String to;
    private User user;
    private double price;
    private String seat;
}

package com.example.booktickets.service;

import com.example.booktickets.model.Ticket;
import com.example.booktickets.model.User;

import java.util.List;

public interface BookTicketService {

    Ticket buyTicket(User user);
    Ticket getTicketDetails(String email);
    List<Ticket> getTicketsForSection(String section);
    boolean removeUser(String email);
    boolean modifyUserSeat(String email, String newSection);
}

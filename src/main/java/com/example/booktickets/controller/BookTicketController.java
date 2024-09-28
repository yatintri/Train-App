package com.example.booktickets.controller;

import com.example.booktickets.model.Response;
import com.example.booktickets.model.Ticket;
import com.example.booktickets.model.User;
import com.example.booktickets.service.BookTicketService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/tickets")
public class BookTicketController {

    private static final Logger logger = LoggerFactory.getLogger(BookTicketController.class);

    private final BookTicketService bookTicketService;

    public BookTicketController(BookTicketService bookTicketService) {
        this.bookTicketService = bookTicketService;
    }

    @PostMapping("/buy")
    public Response<Ticket> buyTicket(@Valid @RequestBody User user){
        logger.info("BookTicketController :: buyTicket :: Purchasing Ticket ");
        Ticket ticket = bookTicketService.buyTicket(user);
        if(ticket != null) {
            logger.info("BookTicketController :: buyTicket :: Purchase Successful ");
            return new Response<>(true, "Ticket bought successfully", ticket);
        }
        else {
            logger.info("BookTicketController :: buyTicket :: Purchase Failed ");
            return new Response<>(false, "Ticket purchase failed", null);
        }
    }

    @GetMapping("/details")
    public Response<Ticket> getTicketDetails(@RequestParam String email){
        logger.info("BookTicketController :: getTicketDetails :: Fetching ticket details for Email: {}", email.toLowerCase());
        Ticket ticket = bookTicketService.getTicketDetails(email.toLowerCase());
        if (ticket != null) {
            logger.info("BookTicketController :: getTicketDetails :: Ticket details retrieved successfully for Email: {}", email.toLowerCase());
            return new Response<>(true, "Ticket details retrieved successfully", ticket);
        } else {
            logger.info("BookTicketController :: getTicketDetails :: Ticket not found for Email: {}", email.toLowerCase());
            return new Response<>(false, "Ticket not found for the given email", null);
        }
    }

    @GetMapping("/section")
    public Response<List<Ticket>> getTicketsForSection(@RequestParam String section){
        logger.info("BookTicketController :: getTicketsForSection :: Fetching tickets for Section: {}", section);
        List<Ticket> tickets = bookTicketService.getTicketsForSection(section);
        if (!tickets.isEmpty()) {
            logger.info("BookTicketController :: getTicketsForSection :: Tickets retrieved successfully for Section: {}", section);
            return new Response<>(true, "Tickets for the section retrieved successfully", tickets);
        } else {
            logger.info("BookTicketController :: getTicketsForSection :: No tickets found for Section: {}", section);
            return new Response<>(false, "No tickets found for the given section", null);
        }
    }

    @DeleteMapping("/remove")
    public Response<Void> removeUser(@RequestParam String email) {
        logger.info("BookTicketController :: removeUser :: Removing user with Email: {}", email.toLowerCase());
        boolean isRemoved = bookTicketService.removeUser(email);
        if (isRemoved) {
            logger.info("BookTicketController :: removeUser :: User removed successfully for Email: {}", email.toLowerCase());
            return new Response<>(true, "User removed successfully", null);
        } else {
            logger.info("BookTicketController :: removeUser :: User not found or could not be removed for Email: {}", email.toLowerCase());
            return new Response<>(false, "User not found or could not be removed", null);
        }
    }

    @PutMapping("/modify")
    public Response<Void> modifyUserSeat(@RequestParam String email, @RequestParam String newSection) {
        logger.info("BookTicketController :: modifyUserSeat :: Modifying seat for User: {} to Section: {}", email.toLowerCase(), newSection);
        boolean isModified = bookTicketService.modifyUserSeat(email.toLowerCase(), newSection);
        if (isModified) {
            logger.info("BookTicketController :: modifyUserSeat :: User seat modified successfully for User: {} to Section: {}", email.toLowerCase(), newSection);
            return new Response<>(true, "User seat modified successfully", null);
        } else {
            logger.info("BookTicketController :: modifyUserSeat :: Modification failed for User: {}. Check user email or section", email.toLowerCase());
            return new Response<>(false, "Modification failed. Check user email or section", null);
        }
    }
}

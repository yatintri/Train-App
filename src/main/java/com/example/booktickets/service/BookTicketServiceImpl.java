package com.example.booktickets.service;

import com.example.booktickets.exception.CustomServiceException;
import com.example.booktickets.model.Ticket;
import com.example.booktickets.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookTicketServiceImpl implements BookTicketService {

    private final List<Ticket> tickets = new ArrayList<>();
    private final Map<String, List<Ticket>> sectionTickets = new HashMap<>();
    private final Map<String, Integer> seatCounter = new HashMap<>();
    private final Random random = new Random();
    private static final Logger logger = LoggerFactory.getLogger(BookTicketServiceImpl.class);

    public BookTicketServiceImpl() {
        sectionTickets.put("A", new ArrayList<>());
        sectionTickets.put("B", new ArrayList<>());
        seatCounter.put("A", 0);
        seatCounter.put("B", 0);
    }

    public String getRandomSection() {
        try {
            String section = random.nextBoolean() ? "A" : "B";
            logger.info("BookTicketServiceImpl :: getRandomSection :: Randomly selected section: {}", section);
            return section;
        } catch (Exception e) {
            logger.error("BookTicketServiceImpl :: getRandomSection :: Error while selecting random section", e);
            throw new CustomServiceException("Failed to select a random section", e);
        }
    }

    public String generateSeatNumber(String section) {
        try {
            int seatNumber = seatCounter.get(section) + 1;
            seatCounter.put(section, seatNumber);
            String seat = section + seatNumber;
            logger.info("BookTicketServiceImpl :: generateSeatNumber :: Generated seat number: {} for section: {}", seat, section);
            return seat;
        } catch (Exception e) {
            logger.error("BookTicketServiceImpl :: generateSeatNumber :: Error while generating seat number for section: {}", section, e);
            throw new CustomServiceException("Failed to generate seat number for section " + section, e);
        }
    }

    public Ticket buyTicket(User user) {
        try {
            logger.info("BookTicketServiceImpl :: buyTicket :: Purchasing ticket for user: {}", user.getEmail());
            String section = getRandomSection();
            String seatNumber = generateSeatNumber(section);
            Ticket ticket = new Ticket("London", "France", user, 20.0, seatNumber);
            tickets.add(ticket);
            sectionTickets.get(section).add(ticket);
            logger.info("BookTicketServiceImpl :: buyTicket :: Ticket purchased successfully for user: {} with seat: {}", user.getEmail(), seatNumber);
            return ticket;
        } catch (Exception e) {
            logger.error("BookTicketServiceImpl :: buyTicket :: Error while purchasing ticket for user: {}", user.getEmail(), e);
            throw new CustomServiceException("Failed to purchase ticket for user " + user.getEmail(), e);
        }
    }

    public Ticket getTicketDetails(String email) {
        try {
            logger.info("BookTicketServiceImpl :: getTicketDetails :: Fetching ticket details for email: {}", email);
            Ticket ticket = tickets.stream()
                    .filter(t -> t.getUser().getEmail().equalsIgnoreCase(email))
                    .findFirst()
                    .orElse(null);
            if (ticket != null) {
                logger.info("BookTicketServiceImpl :: getTicketDetails :: Ticket found for email: {}", email);
            } else {
                logger.info("BookTicketServiceImpl :: getTicketDetails :: No ticket found for email: {}", email);
            }
            return ticket;
        } catch (Exception e) {
            logger.error("BookTicketServiceImpl :: getTicketDetails :: Error while fetching ticket details for email: {}", email, e);
            throw new CustomServiceException("Failed to fetch ticket details for email " + email, e);
        }
    }

    public List<Ticket> getTicketsForSection(String section) {
        try {
            logger.info("BookTicketServiceImpl :: getTicketsForSection :: Fetching tickets for section: {}", section);
            List<Ticket> ticketsForSection = sectionTickets.getOrDefault(section, new ArrayList<>());
            if (!ticketsForSection.isEmpty()) {
                logger.info("BookTicketServiceImpl :: getTicketsForSection :: {} tickets found for section: {}", ticketsForSection.size(), section);
            } else {
                logger.info("BookTicketServiceImpl :: getTicketsForSection :: No tickets found for section: {}", section);
            }
            return ticketsForSection;
        } catch (Exception e) {
            logger.error("BookTicketServiceImpl :: getTicketsForSection :: Error while fetching tickets for section: {}", section, e);
            throw new CustomServiceException("Failed to fetch tickets for section " + section, e);
        }
    }

    public boolean removeUser(String email) {
        try {
            logger.info("BookTicketServiceImpl :: removeUser :: Removing user with email: {}", email);
            Ticket ticket = getTicketDetails(email.toLowerCase());
            if (ticket != null) {
                tickets.remove(ticket);

                // Extract section from the seat number
                String section = ticket.getSeat().substring(0, 1);
                sectionTickets.get(section).remove(ticket);
                logger.info("BookTicketServiceImpl :: removeUser :: User removed successfully with email: {}", email);
                return true;
            }
            logger.info("BookTicketServiceImpl :: removeUser :: User not found or could not be removed for email: {}", email);
            return false;
        } catch (Exception e) {
            logger.error("BookTicketServiceImpl :: removeUser :: Error while removing user with email: {}", email, e);
            throw new CustomServiceException("Failed to remove user with email " + email, e);
        }
    }

    public boolean modifyUserSeat(String email, String newSection) {
        try {
            logger.info("BookTicketServiceImpl :: modifyUserSeat :: Modifying seat for user with email: {} to new section: {}", email, newSection);
            Ticket ticket = getTicketDetails(email.toLowerCase());
            if (ticket != null && sectionTickets.containsKey(newSection)) {
                String oldSection = ticket.getSeat().substring(0, 1);
                sectionTickets.get(oldSection).remove(ticket);
                String newSeatNumber = generateSeatNumber(newSection);
                ticket.setSeat(newSeatNumber);
                sectionTickets.get(newSection).add(ticket);
                logger.info("BookTicketServiceImpl :: modifyUserSeat :: User seat modified successfully for email: {} to new section: {}", email, newSection);
                return true;
            }
            logger.info("BookTicketServiceImpl :: modifyUserSeat :: Modification failed for user with email: {}. Check email or section", email);
            return false;
        } catch (Exception e) {
            logger.error("BookTicketServiceImpl :: modifyUserSeat :: Error while modifying seat for user with email: {} to new section: {}", email, newSection, e);
            throw new CustomServiceException("Failed to modify seat for user with email " + email, e);
        }
    }
}

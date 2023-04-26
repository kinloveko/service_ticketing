package com.ticketing_project.Ticketing.Project;

import java.io.FileNotFoundException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.itextpdf.io.exceptions.IOException;

@Service
public class TicketService {

	@Autowired
	private TicketRepository repo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private UserService userService;

	@Autowired
	private InvoiceService invoiceService;

	public Ticket save(Ticket ticket) {
		return repo.save(ticket);
	}

	@Scheduled(cron = "0 0 0 */5 * *") // This runs every 5 days at midnight
	public void sendAgingTicketReminders() throws MessagingException {
		// Get all tickets

		List<Ticket> allTickets = repo.findAll();

		// Get current date and calculate two days before
		Date currentDate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		cal.add(Calendar.DATE, -2);
		Date twoDaysAgo = cal.getTime();

		// Loop through all tickets
		for (Ticket ticket : allTickets) {
			java.util.Optional<User> userDetails = userService.findUserById(ticket.getUser_id());
			if (ticket.getStatus().equals("pending") && ticket.getCreated_on().before(twoDaysAgo)) {
				// Check if the ticket is aging 2 days without progress
				// Send email reminder

				String emailBody = "Dear Sir/Ma'am,\n\n"
						+ "This is a reminder that your ticket with ID #" + ticket.getTicket_id()
						+ " has been pending for 2 days without progress. "
						+ "To help the sales staff understand more fully the reason of your problem, kindly submit an updated explanation. We appreciate your assisting us..\n\n"
						+ "Best regards,\n" + "info@ark.alliance";
				String emailSubject = "Reminder: Ticket ID #" + ticket.getTicket_id()
						+ " Pending for 2 Days without Progress";
				invoiceService.sendReminder(userDetails.get().getUser_email(), emailBody, emailSubject);
			} else if (ticket.getStatus().equals("ongoing") && ((ticket.getClient_signature() == null
					|| ticket.getClient_signature().equals(""))
					&& (ticket.getClient_payment_proof() == null || ticket.getClient_payment_proof().equals("")))) {
					// Check if the ticket is ongoing but client signature and payment proof are not provided
				// Send email reminder
				String emailBody = "Dear Sir/Ma'am,\n\n" + "This is a reminder that your ticket with ID #"
						+ ticket.getTicket_id()
						+ " is ongoing but we have not yet received your signature and payment proof. "
						+ "Please provide these information as soon as possible to avoid delay in processing your request. Thank you.\n\n"
						+ "Best regards,\n" + "Ark.Alliance@Support";
				String emailSubject = "Reminder: Ticket ID #" + ticket.getTicket_id()
						+ " Missing Client Signature and Payment Proof";
				invoiceService.sendReminder(userDetails.get().getUser_email(), emailBody, emailSubject);
			}
		}
	}

	public void update(int ticketId, Ticket updatedTicket) {
		// List containing the ticket that matches the ticketId
		List<Ticket> matchingTicket = getAllTickets().stream().filter(ticket -> ticket.getTicket_id() == ticketId)
				.toList();

		if (matchingTicket.size() != 0) {
			if (updatedTicket.getTitle() != null) {
				matchingTicket.get(0).setTitle(updatedTicket.getTitle());
			}
			if (updatedTicket.getDescription() != null) {
				matchingTicket.get(0).setDescription(updatedTicket.getDescription());
			}
			if (updatedTicket.getStatus() != null) {
				matchingTicket.get(0).setStatus(updatedTicket.getStatus());
			}
			if (updatedTicket.getUser_id() != 0) {
				matchingTicket.get(0).setUser_id(updatedTicket.getUser_id());
			}

			repo.save(matchingTicket.get(0));
		}
	}

	public void delete(int ticketId) {
		// List containing the ticket that matches the ticketId
		List<Ticket> matchingTicket = getAllTickets().stream().filter(ticket -> ticket.getTicket_id() == ticketId)
				.toList();

		if (matchingTicket.size() != 0) {
			repo.delete(matchingTicket.get(0));
		}
	}

	public List<Ticket> getAllTickets() {
		final List<Ticket> ticketList = repo.findAll();
		return ticketList;
	}

	public Ticket getTicketById(int ticketId) {
		List<Ticket> matchingTicket = repo.findAll().stream().filter(ticket -> ticket.getTicket_id() == ticketId)
				.toList();

		if (matchingTicket.size() == 0)
			return null;

		return matchingTicket.get(0);
	}

	public List<Ticket> getTicketsByUserId(int user_id) {
		return repo.findAll().stream().filter(ticket -> ticket.getUser_id() == user_id).toList();
	}

	public void clientOwnTicket(Model m, int userID) {

		List<Ticket> tickets = repo.findAll().stream().filter(ticket -> ticket.getUser_id() == userID).toList();

		List<Ticket> pendingTickets = new ArrayList<>();
		List<Ticket> ongoingTickets = new ArrayList<>();
		List<Ticket> completedTickets = new ArrayList<>();

		for (Ticket ticket : tickets) {
			switch (ticket.getStatus()) {
			case "pending":
				pendingTickets.add(ticket);
				break;
			case "ongoing":
				ongoingTickets.add(ticket);
				break;
			case "completed":
				completedTickets.add(ticket);
				break;
			}
		}

		Collections.sort(pendingTickets, Comparator.comparingInt(Ticket::getTicket_id).reversed());
		Collections.sort(ongoingTickets, Comparator.comparingInt(Ticket::getTicket_id).reversed());
		Collections.sort(completedTickets, Comparator.comparingInt(Ticket::getTicket_id).reversed());

		m.addAttribute("pending_tickets", pendingTickets);
		m.addAttribute("pending_ticket_count", pendingTickets.size());

		m.addAttribute("ongoing_tickets", ongoingTickets);
		m.addAttribute("ongoing_ticket_count", ongoingTickets.size());

		m.addAttribute("completed_tickets", completedTickets);
		m.addAttribute("completed_ticket_count", completedTickets.size());

	}

	public void populateTicketModel(Model m) {
		List<Ticket> tickets = getAllTickets();
		List<Ticket> pendingTickets = new ArrayList<>();
		List<Ticket> ongoingTickets = new ArrayList<>();
		List<Ticket> completedTickets = new ArrayList<>();
		List<Ticket> supportTickets = new ArrayList<>();

		for (Ticket ticket : tickets) {
			switch (ticket.getStatus()) {
			case "pending":
				pendingTickets.add(ticket);
				break;
			case "ongoing":
				ongoingTickets.add(ticket);
				break;
			case "completed":
				completedTickets.add(ticket);
				break;
			}

			if (ticket.getProgress() != null && ticket.getProgress().equals("support_team")
					&& ticket.getStatus().equals("ongoing")) {
				supportTickets.add(ticket);
			}
		}

		Collections.sort(pendingTickets, Comparator.comparingInt(Ticket::getTicket_id).reversed());
		Collections.sort(ongoingTickets, Comparator.comparingInt(Ticket::getTicket_id).reversed());
		Collections.sort(completedTickets, Comparator.comparingInt(Ticket::getTicket_id).reversed());
		Collections.sort(supportTickets, Comparator.comparingInt(Ticket::getTicket_id).reversed());

		m.addAttribute("pending_tickets", pendingTickets);
		m.addAttribute("pending_ticket_count", pendingTickets.size());

		m.addAttribute("ongoing_tickets", ongoingTickets);
		m.addAttribute("ongoing_ticket_count", ongoingTickets.size());

		m.addAttribute("completed_tickets", completedTickets);
		m.addAttribute("completed_ticket_count", completedTickets.size());

		m.addAttribute("support_tickets", supportTickets);
		m.addAttribute("support_count", supportTickets.size());

	}

	// TicketService
	@Autowired
	private JavaMailSender mailSender;

	public void sendEmail(String toEmail, String body, String subject) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("ark.alliance2023@gmail.com");
		message.setTo(toEmail);
		message.setText(body);
		message.setSubject(subject);

		mailSender.send(message);
	}

	public List<String> getEmailsOfSalesTeam() {
		List<User> salesTeamUsers = userRepo.findByUserRole("sales_team_leader");
		List<String> emails = new ArrayList<>();
		for (User user : salesTeamUsers) {
			emails.add(user.getUser_email());
		}
		return emails;
	}

	public void generateCsvFileByAging(String filter, HttpServletResponse response)
			throws IOException, java.io.IOException {
		// Create a new workbook
		XSSFWorkbook workbook = new XSSFWorkbook();
		List<Ticket> allTickets = repo.findAll();

		// Retrieve filtered tickets from database
		List<Ticket> ongoingTickets = new ArrayList<>();
		List<Ticket> pendingTickets = new ArrayList<>();
		// All pending accounts has no assignee so here we don't need to add it on EXCEL
		// file
		int minimumAge = 2;
		for (Ticket i : allTickets) {

			Instant minimumAgeInstant = Instant.now().minus(minimumAge, ChronoUnit.DAYS);
			Date minimumAgeDate = Date.from(minimumAgeInstant);

			if (filter.equals("all")) {

				if (i.getStatus().equals("pending") && i.getCreated_on().before(minimumAgeDate)) {
					pendingTickets.add(i);
				} else if (i.getStatus().equals("ongoing") && i.getCreated_on().before(minimumAgeDate)) {
					if ((i.getClient_signature() == null && i.getClient_payment_proof() == null)
							|| (i.getClient_signature().equals("") && i.getClient_payment_proof().equals("")))
						ongoingTickets.add(i);
				}

			} else if (filter.equals("pending")) {
				if (i.getStatus().equals("pending") && i.getCreated_on().before(minimumAgeDate)) {
					pendingTickets.add(i);
				}
			} else if (filter.equals("ongoing")) {
				if (i.getStatus().equals("ongoing") && i.getCreated_on().before(minimumAgeDate)) {
					if ((i.getClient_signature() == null && i.getClient_payment_proof() == null)
							|| (i.getClient_signature().equals("") && i.getClient_payment_proof().equals("")))
						ongoingTickets.add(i);
				}
			}
		}

		// Write data to CSV file
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition",
				"attachment; filename=\"tickets_" + System.currentTimeMillis() + ".xlsx\"");

		// Get output stream from response object
		ServletOutputStream outputStream = response.getOutputStream();

		// Write header row to each sheet
		String[] header = { "Ticket ID", "User Name", "Title", "Description", "Status", "Created On", "User ID",
				"Conforme No.", "Amount", "Sales Signature", "Client Payment Proof", "Client Signature",
				"Conforme Date", "Progress" };

		if (filter.equals("all")) {
			writeDataToSheet(workbook.createSheet("Pending Tickets"), pendingTickets, header);
			writeDataToSheet(workbook.createSheet("Ongoing Tickets"), ongoingTickets, header);

		} else if (ongoingTickets.size() != 0) {
			writeDataToSheet(workbook.createSheet("Ongoing Tickets"), ongoingTickets, header);

		} else if (pendingTickets.size() != 0) {
			writeDataToSheet(workbook.createSheet("Pending Tickets"), pendingTickets, header);

		}
		// Write data to Excel file
		workbook.write(outputStream);

		// Flush and close output stream
		outputStream.flush();
		outputStream.close();
	}

	public void generateCsvFileByAssignee(String filter, HttpServletResponse response)
			throws IOException, java.io.IOException {
		// Create a new workbook
		XSSFWorkbook workbook = new XSSFWorkbook();
		List<Ticket> allTickets = repo.findAll();

		// Retrieve filtered tickets from database
		List<Ticket> ongoingTickets = new ArrayList<>();
		List<Ticket> completedTickets = new ArrayList<>();

		// All pending accounts has no assignee so here we don't need to add it on EXCEL
		// file
		for (Ticket i : allTickets) {

			if (filter.equals("Sales Team")) {
				if (i.getStatus().equals("ongoing") && i.getProgress().equals("sales_team"))
					ongoingTickets.add(i);
				if (i.getStatus().equals("completed") && i.getProgress().equals("support_team"))
					ongoingTickets.add(i);

			} else if (filter.equals("Billing Team")) {
				if (i.getStatus().equals("completed") && i.getProgress().equals("billing_team"))
					completedTickets.add(i);
			} else if (filter.equals("Support Team")) {
				if (i.getStatus().equals("ongoing") && i.getProgress().equals("support_team"))
					ongoingTickets.add(i);
			} else if (filter.equals("Collection Team")) {
				if (i.getStatus().equals("completed") && i.getProgress().equals("collection_team"))
					completedTickets.add(i);
			} else if (filter.equals("Treasury Team")) {
				if (i.getStatus().equals("completed") && i.getProgress().equals("treasury_team"))
					completedTickets.add(i);
			} else {
				if (i.getStatus().equals("completed") && i.getProgress().equals("completed"))
					completedTickets.add(i);
			}

		}

		// Write data to CSV file
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition",
				"attachment; filename=\"tickets_" + System.currentTimeMillis() + ".xlsx\"");

		// Get output stream from response object
		ServletOutputStream outputStream = response.getOutputStream();

		// Write header row to each sheet
		String[] header = { "Ticket ID", "User Name", "Title", "Description", "Status", "Created On", "User ID",
				"Conforme No.", "Amount", "Sales Signature", "Client Payment Proof", "Client Signature",
				"Conforme Date", "Progress" };

		if (filter.equals("Sales Team")) {
			writeDataToSheet(workbook.createSheet("Ongoing Tickets"), ongoingTickets, header);
		} else if (filter.equals("Billing Team")) {
			writeDataToSheet(workbook.createSheet("Completed Tickets"), completedTickets, header);
		} else if (filter.equals("Support Team")) {
			writeDataToSheet(workbook.createSheet("Ongoing Tickets"), ongoingTickets, header);
		} else if (filter.equals("Collection Team")) {
			writeDataToSheet(workbook.createSheet("Completed Tickets"), completedTickets, header);
		} else if (filter.equals("Treasury Team")) {
			writeDataToSheet(workbook.createSheet("Completed Tickets"), completedTickets, header);
		} else {
			writeDataToSheet(workbook.createSheet("Completed Tickets"), completedTickets, header);
		}

		// Write data to Excel file
		workbook.write(outputStream);

		// Flush and close output stream
		outputStream.flush();
		outputStream.close();
	}

	public void generateCsvFileByCreatedOn(String filter, HttpServletResponse response)
			throws IOException, java.io.IOException {
		// Create a new workbook
		XSSFWorkbook workbook = new XSSFWorkbook();
		List<Ticket> allTickets = repo.findAll();

		// Calculate start and end dates based on filter
		LocalDate startDate, endDate;
		if (filter.equals("This month")) {
			startDate = LocalDate.now().withDayOfMonth(1);
			endDate = LocalDate.now().plusMonths(1).withDayOfMonth(1).minusDays(1);
		} else if (filter.equals("Last month")) {
			startDate = LocalDate.now().minusMonths(1).withDayOfMonth(1);
			endDate = LocalDate.now().withDayOfMonth(1).minusDays(1);
		} else if (filter.equals("All tickets")) {
			startDate = LocalDate.of(2023, 1, 1); // Beginning of time
			endDate = LocalDate.now();
		} else {
			throw new IllegalArgumentException("Invalid filter: " + filter);
		}

		// Convert start and end dates to timestamps
		Instant startInstant = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
		Instant endInstant = endDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
		java.sql.Timestamp startTimestamp = java.sql.Timestamp.from(startInstant);
		java.sql.Timestamp endTimestamp = java.sql.Timestamp.from(endInstant);

		// Retrieve filtered tickets from database
		List<Ticket> pendingTickets = new ArrayList<>();
		List<Ticket> ongoingTickets = new ArrayList<>();
		List<Ticket> completedTickets = new ArrayList<>();
		for (Ticket ticket : allTickets) {
			if (ticket.getCreated_on().after(startTimestamp) && ticket.getCreated_on().before(endTimestamp)) {
				switch (ticket.getStatus()) {
				case "ongoing":
					ongoingTickets.add(ticket);
					break;
				case "completed":
					completedTickets.add(ticket);
					break;
				case "pending":
					pendingTickets.add(ticket);
					break;
				default:
					break;
				}
			}
		}

		// Write data to CSV file
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition",
				"attachment; filename=\"tickets_" + System.currentTimeMillis() + ".xlsx\"");

		// Get output stream from response object
		ServletOutputStream outputStream = response.getOutputStream();

		// Write header row to each sheet
		String[] header = { "Ticket ID", "User Name", "Title", "Description", "Status", "Created On", "User ID",
				"Conforme No.", "Amount", "Sales Signature", "Client Payment Proof", "Client Signature",
				"Conforme Date", "Progress" };

		// Write data to appropriate sheet
		writeDataToSheet(workbook.createSheet("Pending Tickets"), pendingTickets, header);
		writeDataToSheet(workbook.createSheet("Ongoing Tickets"), ongoingTickets, header);
		writeDataToSheet(workbook.createSheet("Completed Tickets"), completedTickets, header);

		// Write data to Excel file
		workbook.write(outputStream);

		// Flush and close output stream
		outputStream.flush();
		outputStream.close();
	}

	private void writeDataToSheet(XSSFSheet sheet, List<Ticket> tickets, String[] header)
			throws FileNotFoundException, IOException {
		// Create header row
		XSSFRow headerRow = sheet.createRow(0);
		int count = 0;
		for (String i : header) {

			headerRow.createCell(count).setCellValue(i);
			count++;
		}

		// Write data rows
		for (int i = 0; i < tickets.size(); i++) {
			Ticket ticket = tickets.get(i);
			XSSFRow dataRow = sheet.createRow(i + 1);
			dataRow.createCell(0).setCellValue(ticket.getTicket_id());
			dataRow.createCell(1).setCellValue(ticket.getUser_name());
			dataRow.createCell(2).setCellValue(ticket.getTitle());
			dataRow.createCell(3).setCellValue(ticket.getDescription());
			dataRow.createCell(4).setCellValue(ticket.getStatus());
			dataRow.createCell(5).setCellValue(ticket.getCreated_on().toLocalDateTime().toString());
			dataRow.createCell(6).setCellValue(ticket.getUser_id());
			dataRow.createCell(7).setCellValue(ticket.getConforme_no());
			dataRow.createCell(8).setCellValue(ticket.getAmount());
			dataRow.createCell(9).setCellValue(ticket.getSalesSignature());
			dataRow.createCell(10).setCellValue(ticket.getClient_payment_proof());
			dataRow.createCell(11).setCellValue(ticket.getClient_signature());
			dataRow.createCell(12).setCellValue(ticket.getConforme_date());
			dataRow.createCell(13).setCellValue(ticket.getProgress());
		}
	}
}
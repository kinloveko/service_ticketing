package com.ticketing_project.Ticketing.Project;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class InvoiceController {

	@Autowired
	private InvoiceService invoiceService;

	@Autowired
	private TicketService ticketService;

	@Autowired
	private UserService userService;

	@PostMapping("/invoice/save")
	public String addNewTicket(@ModelAttribute Invoice newInvoice, HttpSession session) {
		invoiceService.save(newInvoice);

		return "redirect:/admin.ark";
	}

	@GetMapping("/invoiceDetails")
	@ResponseBody
	public Invoice getFeedbacksByStatusId(@RequestParam int ticketID) {
		Invoice invoiceDetails = invoiceService.findByTicketID(ticketID);

		return invoiceDetails;
	}

	@PutMapping("/invoice/update/{ticketID}")
	@ResponseBody
	public String updateInvoice(@PathVariable int ticketID, HttpSession session, @RequestParam MultipartFile img1,
			RedirectAttributes redirectAttributes) {

		Invoice newInvoice = invoiceService.findByTicketID(ticketID);

		newInvoice.setPaymentConfirmation(img1.getOriginalFilename());

		Ticket updateTicket = ticketService.getTicketById(newInvoice.getTicketID());
		updateTicket.setProgress("completed");

		ticketService.save(updateTicket);
		if (newInvoice != null) {
			try {
				// Save confirmation of payment file
				File saveFile = new ClassPathResource("static/img").getFile();
				Path proofPath = Paths.get(saveFile.getAbsolutePath() + File.separator + img1.getOriginalFilename());
				System.out.println(proofPath);
				System.out.println(img1.getOriginalFilename());
				Files.copy(img1.getInputStream(), proofPath, StandardCopyOption.REPLACE_EXISTING);
				String name = (String) session.getAttribute("user_name");

				String emailBody = "Dear Collection In-Charge Team,\n\n"
						+ "The online transaction of the ticket has been verified with the following information:\n\n"
						+ "Invoice ID: " + newInvoice.getInvoice_id() + "\n" + "Date: " + newInvoice.getInvoice_date()
						+ "\n" + "Client Name: " + newInvoice.getClient_name() + "\n" + "Email Address: "
						+ newInvoice.getClient_email() + "\n" + "Ticket ID:: " + newInvoice.getTicketID() + "\n"
						+ "Title: " + newInvoice.getTicket_issue_title() + "\n" + "Paid Amount: "
						+ newInvoice.getInvoice_amount() + "\n\n"
						+ "This file attachment serves as verification of the client's payment made online.\n\n"
						+ "Best regards, \n" + name + "\n" + "Treasury";
				String emailSubject = "Payment Confirmation: Ticket ID #" + newInvoice.getTicketID();
				String attachment = proofPath.toString();
				// Send email to sales team
				List<String> collectionTeamEmails = invoiceService.getEmailsOfCollectionTeamLeader();
				for (String email : collectionTeamEmails) {
					invoiceService.sendEmail(email, emailBody, emailSubject, attachment);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		redirectAttributes.addFlashAttribute("paymentSuccess", "Payment Comfirmation saved successfully!");
		return "redirect:/admin.ark";
	}

	@PutMapping("/send/confirmation/client/{ticketID}")
	@ResponseBody
	public String sendBillingStatement(@PathVariable int ticketID, HttpSession session,	@RequestParam String client_email,
			@RequestParam MultipartFile img1, RedirectAttributes redirectAttributes) throws MessagingException {

		Invoice newInvoice = invoiceService.findByTicketID(ticketID);

		newInvoice.setPaymentConfirmation(img1.getOriginalFilename());

		Ticket updateTicket = ticketService.getTicketById(newInvoice.getTicketID());
		updateTicket.setProgress("completed");

		ticketService.save(updateTicket);
		if (newInvoice != null) {
			String name = (String) session.getAttribute("user_name");
			String emailBody = "Dear Sir/Ma'am,\n\n"
					+ "Please find attached the official receipt/scan copy of the payment made for Ticket ID #"
					+ newInvoice.getTicketID() + ". The following information has been verified:\n\n" + "Invoice ID: "
					+ newInvoice.getInvoice_id() + "\n" + "Date: " + newInvoice.getInvoice_date() + "\n"
					+ "Client Name: " + newInvoice.getClient_name() + "\n" + "Email Address: "
					+ newInvoice.getClient_email() + "\n" + "Ticket ID: " + newInvoice.getTicketID() + "\n" + "Title: "
					+ newInvoice.getTicket_issue_title() + "\n" + "Paid Amount: " + newInvoice.getInvoice_amount()
					+ "\n\n" + "Best regards,\n" + name + "\n" + "Treasury";
			String emailSubject = "Official Receipt/Scan Copy: Payment Confirmation for Ticket ID #"
					+ newInvoice.getTicketID();
			byte[] pdfBytes = null;
			try {
				pdfBytes = img1.getBytes();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Send email to sales team
			
			invoiceService.sendEmailClient(client_email, emailBody, emailSubject, pdfBytes);

		}
		redirectAttributes.addFlashAttribute("ClientSuccess", "Email Sent successfully!");
		return "redirect:/admin.ark";
	}
	
	
	
	

	@GetMapping("/pdf/generate/{ticketID}")
	@ResponseBody
	public void generatePDF(HttpServletResponse response, @PathVariable int ticketID) throws IOException {
		response.setContentType("application/pdf");

		// Create a formatter for the desired date format
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd:hh:mm:ss");

		// Get the current date and time
		LocalDateTime currentDateTime = LocalDateTime.now();

		// Format the date and time as a string using the formatter
		String formattedDateTime = currentDateTime.format(dateFormatter);

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=pdf_" + formattedDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);

		invoiceService.export(response, ticketID);
	}

}

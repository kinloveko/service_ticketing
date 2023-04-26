package com.ticketing_project.Ticketing.Project;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.borders.Border;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfCell;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class InvoiceService {
	@Autowired
	private InvoiceRepository repo;

	@Autowired
	private UserRepository UserRepo;

	public Invoice save(Invoice invoice) {
		return repo.save(invoice);
	}

	public Invoice findByTicketID(int ticketID) {
		return repo.findByticketID(ticketID);
	}

	@Autowired
	private JavaMailSender mailSender;

	public void sendEmail(String toEmail, String body, String subject, String attachment) throws MessagingException {

		MimeMessage mimeMessage = mailSender.createMimeMessage();

		MimeMessageHelper mineMessageHelper = new MimeMessageHelper(mimeMessage, true);

		mineMessageHelper.setFrom("ark.alliance2023@gmail.com");
		mineMessageHelper.setTo(toEmail);
		mineMessageHelper.setText(body);
		mineMessageHelper.setSubject(subject);

		FileSystemResource fileSystem = new FileSystemResource(new File(attachment));

		mineMessageHelper.addAttachment(fileSystem.getFilename(), fileSystem);

		mailSender.send(mimeMessage);
	}
	
	
	
	public void sendReminder(String toEmail, String body, String subject) throws MessagingException {

		MimeMessage mimeMessage = mailSender.createMimeMessage();

		MimeMessageHelper mineMessageHelper = new MimeMessageHelper(mimeMessage, true);

		mineMessageHelper.setFrom("ark.alliance2023@gmail.com");
		mineMessageHelper.setTo(toEmail);
		mineMessageHelper.setText(body);
		mineMessageHelper.setSubject(subject);

		mailSender.send(mimeMessage);
	}

	public List<String> getEmailsOfCollectionTeamLeader() {
		List<User> collectionTeamUsers = UserRepo.findByUserRole("collection_team_leader");
		List<String> emails = new ArrayList<>();
		for (User user : collectionTeamUsers) {
			emails.add(user.getUser_email());
		}
		return emails;
	}


	public void sendEmailClient(String toEmail, String body, String subject, byte[] attachment) throws MessagingException {

	    MimeMessage mimeMessage = mailSender.createMimeMessage();

	    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

	    mimeMessageHelper.setFrom("ark.alliance2023@gmail.com");
	    mimeMessageHelper.setTo(toEmail);
	    mimeMessageHelper.setText(body);
	    mimeMessageHelper.setSubject(subject);

	    ByteArrayResource attachmentResource = new ByteArrayResource(attachment);

	    mimeMessageHelper.addAttachment("attachment.pdf", attachmentResource);

	    mailSender.send(mimeMessage);
	}

	public void export(HttpServletResponse response, @RequestParam int ticketID) throws IOException {
		// Get invoice details from database

		Invoice invoice = repo.findByticketID(ticketID);
		String ticketIDNo = String.valueOf(invoice.getTicketID());
		String Amount = "₱ " + String.valueOf(invoice.getInvoice_amount());

		// Create new document
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());

		// Add logo to header and center header title
		PdfPTable headerTable = new PdfPTable(1);
		headerTable.setWidthPercentage(15);

		Image logo = Image.getInstance("src/main/resources/static/img/logo.png");
		logo.setAlignment(Element.ALIGN_CENTER);

		PdfPCell cell = new PdfPCell();
		cell.addElement(logo);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(PdfPCell.NO_BORDER);

		headerTable.addCell(cell);

		// Open document
		document.open();
		document.add(headerTable);

		// Add company address
		Paragraph address = new Paragraph();
		address.add(new Chunk("Official Receipt", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
		address.add(Chunk.NEWLINE);
		address.add(new Chunk("Ark.Alliance Company", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
		address.add(Chunk.NEWLINE);
		address.add(new Chunk("Gov. Cuenco Banilad, Cebu City", FontFactory.getFont(FontFactory.HELVETICA)));
		address.add(Chunk.NEWLINE);
		address.add(new Chunk("Cebu, 6000", FontFactory.getFont(FontFactory.HELVETICA)));
		address.setAlignment(Element.ALIGN_CENTER);
		address.add(Chunk.NEWLINE);
		address.add(Chunk.NEWLINE);
		address.add(Chunk.NEWLINE);

		document.add(address);

		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
		String formattedDate = currentDate.format(formatter);

		// Add invoice details
		Paragraph invoiceNo = new Paragraph("Date : " + formattedDate, FontFactory.getFont(FontFactory.HELVETICA));
		invoiceNo.setAlignment(Element.ALIGN_LEFT);
		invoiceNo.add(Chunk.NEWLINE);
		invoiceNo.add(new Chunk("INVOICE NO :" + invoice.getInvoice_id(), FontFactory.getFont(FontFactory.HELVETICA)));
		invoiceNo.setAlignment(Element.ALIGN_LEFT);
		invoiceNo.add(Chunk.NEWLINE);
		invoiceNo.add(Chunk.NEWLINE);

		document.add(invoiceNo);

		// Add bill to details
		Paragraph billTo = new Paragraph("BILLING TO:", FontFactory.getFont(FontFactory.HELVETICA));
		billTo.add(Chunk.NEWLINE);

		billTo.add(new Chunk("Name : ", FontFactory.getFont(FontFactory.HELVETICA)));
		billTo.add(new Chunk(invoice.getClient_name(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
		billTo.add(Chunk.NEWLINE);
		billTo.add(new Chunk("Email : ", FontFactory.getFont(FontFactory.HELVETICA)));

		billTo.add(new Chunk(invoice.getClient_email(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
		billTo.setAlignment(Element.ALIGN_LEFT);
		document.add(billTo);

		// Add ticket details
		PdfPTable ticketTable = new PdfPTable(3);
		ticketTable.setWidthPercentage(100);
		ticketTable.setSpacingBefore(20);
		ticketTable.setSpacingAfter(20);
		ticketTable.getDefaultCell().setBorder(0);

		// Define cell padding
		float cellPadding = 10;

		// Add header row
		PdfPCell ticketNoHeader = new PdfPCell(
				new Phrase("Ticket No:", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
		ticketNoHeader.setPadding(cellPadding);
		ticketNoHeader.setBackgroundColor(new Color(0xED, 0xEF, 0xF1));
		ticketNoHeader.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ticketNoHeader.setHorizontalAlignment(Element.ALIGN_LEFT);
		ticketNoHeader.setBorder(4);

		ticketNoHeader.setBorderColor(Color.WHITE);
		ticketTable.addCell(ticketNoHeader);

		PdfPCell descriptionHeader = new PdfPCell(
				new Phrase("Description:", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
		descriptionHeader.setPadding(cellPadding);
		descriptionHeader.setBorderColor(Color.WHITE);
		descriptionHeader.setBackgroundColor(new Color(0xED, 0xEF, 0xF1));
		descriptionHeader.setVerticalAlignment(Element.ALIGN_MIDDLE);
		descriptionHeader.setHorizontalAlignment(Element.ALIGN_LEFT);
		descriptionHeader.setBorder(4);

		ticketTable.addCell(descriptionHeader);

		PdfPCell servicePriceHeader = new PdfPCell(
				new Phrase("Service price:", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
		servicePriceHeader.setPadding(cellPadding);
		servicePriceHeader.setBorderColor(Color.WHITE);
		servicePriceHeader.setBackgroundColor(new Color(0xED, 0xEF, 0xF1));
		servicePriceHeader.setVerticalAlignment(Element.ALIGN_MIDDLE);
		servicePriceHeader.setHorizontalAlignment(Element.ALIGN_LEFT);

		servicePriceHeader.setBorder(4);

		ticketTable.addCell(servicePriceHeader);

		// Add ticket details
		PdfPCell ticketNoCell = new PdfPCell(new Phrase(ticketIDNo, FontFactory.getFont(FontFactory.HELVETICA)));
		ticketNoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ticketNoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		ticketNoCell.setBorderColor(Color.WHITE);
		ticketNoCell.setPadding(15);
		ticketNoCell.setBackgroundColor(new Color(0xF5, 0xF5, 0xF5));
		ticketNoCell.setBorder(4);

		ticketTable.addCell(ticketNoCell);

		PdfPCell descriptionCell = new PdfPCell(
				new Phrase(invoice.getTicket_issue_title(), FontFactory.getFont(FontFactory.HELVETICA)));
		descriptionCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		descriptionCell.setBackgroundColor(new Color(0xF5, 0xF5, 0xF5));
		descriptionCell.setPadding(15);
		descriptionCell.setBorderColor(Color.WHITE);
		descriptionCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		descriptionCell.setBorder(4);

		ticketTable.addCell(descriptionCell);

		PdfPCell servicePriceCell = new PdfPCell(new Phrase(Amount, FontFactory.getFont(FontFactory.HELVETICA)));
		servicePriceCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		servicePriceCell.setBorderColor(Color.WHITE);
		servicePriceCell.setBackgroundColor(new Color(0xF5, 0xF5, 0xF5));
		servicePriceCell.setPadding(15);
		servicePriceCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		servicePriceCell.setBorder(4);

		ticketTable.addCell(servicePriceCell);

		// Add empty rows
		PdfPCell emptyCell = new PdfPCell();
		emptyCell.setBackgroundColor(new Color(0xF5, 0xF5, 0xF5));
		emptyCell.setMinimumHeight(50);
		emptyCell.setBorderColor(Color.WHITE);

		emptyCell.setBorder(4);

		ticketTable.addCell(emptyCell);

		PdfPCell emptyCell2 = new PdfPCell();
		emptyCell2.setMinimumHeight(50);

		emptyCell2.setBorderColor(Color.WHITE);
		emptyCell2.setBackgroundColor(new Color(0xF5, 0xF5, 0xF5));
		emptyCell2.setBorder(4);

		ticketTable.addCell(emptyCell2);

		PdfPCell emptyCell3 = new PdfPCell();

		emptyCell3.setBorderColor(Color.WHITE);
		emptyCell3.setBackgroundColor(new Color(0xF5, 0xF5, 0xF5));
		emptyCell3.setMinimumHeight(10);
		emptyCell3.setBorder(4);

		ticketTable.addCell(emptyCell3);

		PdfPCell emptyCell4 = new PdfPCell();
		emptyCell4.setMinimumHeight(10);
		emptyCell4.setBorder(4);
		emptyCell4.setBorderColor(Color.WHITE);

		emptyCell4.setBackgroundColor(new Color(0xF5, 0xF5, 0xF5));
		ticketTable.addCell(emptyCell4);

		// Add total row
		PdfPCell totalHeader = new PdfPCell(new Phrase("Total:", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
		totalHeader.setPadding(cellPadding);
		totalHeader.setPadding(15);
		totalHeader.setBorderColor(Color.WHITE);
		totalHeader.setBackgroundColor(new Color(0xF5, 0xF5, 0xF5));
		totalHeader.setVerticalAlignment(Element.ALIGN_MIDDLE);
		totalHeader.setHorizontalAlignment(Element.ALIGN_RIGHT);
		totalHeader.setBorder(4);
		ticketTable.addCell(totalHeader);

		PdfPCell totalCell = new PdfPCell(new Phrase(Amount, FontFactory.getFont(FontFactory.HELVETICA)));
		totalCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		totalCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		totalCell.setBorderColor(Color.WHITE);
		totalCell.setPadding(15);
		totalCell.setBackgroundColor(new Color(0xF5, 0xF5, 0xF5));
		totalCell.setBorder(4);
		ticketTable.addCell(totalCell);

		document.add(ticketTable);
		document.close();
	}
}
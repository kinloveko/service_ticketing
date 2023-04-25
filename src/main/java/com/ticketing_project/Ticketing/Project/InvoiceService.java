package com.ticketing_project.Ticketing.Project;

 
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
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
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

        FileSystemResource fileSystem
                = new FileSystemResource(new File(attachment));

        mineMessageHelper.addAttachment(fileSystem.getFilename(),
                fileSystem);

	    mailSender.send(mimeMessage);
	}
	
	public List<String> getEmailsOfCollectionTeam() {
	    List<User> collectionTeamUsers = UserRepo.findByUserRole("collection_team");
	  List<String> emails = new ArrayList<>();
	    for (User user : collectionTeamUsers) {
	        emails.add(user.getUser_email());
	    }
	    return emails;
	}
	
	public void export(HttpServletResponse response, @RequestParam int ticketID) throws IOException {
	    // Get invoice details from database
	    Invoice invoice = repo.findByticketID(ticketID);

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
	    Paragraph invoiceNo = new Paragraph("Date : "+formattedDate,
	    		  FontFactory.getFont(FontFactory.HELVETICA));
	    invoiceNo.setAlignment(Element.ALIGN_LEFT);
	    invoiceNo.add(Chunk.NEWLINE);
	    invoiceNo.add(new Chunk("INVOICE NO :" + invoice.getInvoice_id(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
	    invoiceNo.setAlignment(Element.ALIGN_LEFT);
	    invoiceNo.add(Chunk.NEWLINE);
	    invoiceNo.add(Chunk.NEWLINE);

	    document.add(invoiceNo);
	    
	 // Add bill to details
	    Paragraph billTo = new Paragraph("BILLING TO:",
	        FontFactory.getFont(FontFactory.HELVETICA));
	    billTo.add(Chunk.NEWLINE);
	    billTo.add(Chunk.NEWLINE);
	    billTo.add(new Chunk("Name : "+invoice.getClient_name(), FontFactory.getFont(FontFactory.HELVETICA)));
	    billTo.add(Chunk.NEWLINE);
	    billTo.add(new Chunk("Email : "+invoice.getClient_email(), FontFactory.getFont(FontFactory.HELVETICA)));
	    billTo.setAlignment(Element.ALIGN_LEFT);
	    document.add(billTo);
	    

	    // Add ticket details
	    Paragraph ticket = new Paragraph();
	    String ticketIDNo = String.valueOf(invoice.getTicketID());
	    String Amount = String.valueOf(invoice.getInvoice_amount());
	    ticket.add(new Chunk("Ticket No: ", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
	    ticket.add(new Chunk(ticketIDNo, FontFactory.getFont(FontFactory.HELVETICA)));
	    ticket.add(Chunk.NEWLINE);
	    ticket.add(new Chunk("Ticket Title: ", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
	    ticket.add(new Chunk(invoice.getTicket_issue_title(), FontFactory.getFont(FontFactory.HELVETICA)));
	    ticket.add(Chunk.NEWLINE);
	    document.add(ticket);

	    // Add total amount
	    Paragraph total = new Paragraph();
	    total.add(new Chunk("Service Charge: ", FontFactory.getFont(FontFactory.HELVETICA)));
	    total.add(new Chunk(Amount, FontFactory.getFont(FontFactory.HELVETICA)));
	    total.add(Chunk.NEWLINE);
	    total.add(new Chunk("Total: ", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
	    total.add(new Chunk(Amount, FontFactory.getFont(FontFactory.HELVETICA)));
	    total.setAlignment(Element.ALIGN_RIGHT);
	    total.add(Chunk.NEWLINE);
	    total.add(Chunk.NEWLINE);
	    total.add(Chunk.NEWLINE);
	    total.add(Chunk.NEWLINE);
	    total.add(Chunk.NEWLINE);
	    total.add(Chunk.NEWLINE);
	    total.add(Chunk.NEWLINE);
	    document.add(total);

	    
	    // Add footer
	    Font fontFooter = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
	    fontFooter.setSize(14);
	    Paragraph footer = new Paragraph("Thank You!", fontFooter);
	    footer.setAlignment(Element.ALIGN_CENTER);
	    document.add(footer);

	    // Close document
	    document.close();
	    }
	   
}
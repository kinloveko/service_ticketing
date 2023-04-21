package com.ticketing_project.Ticketing.Project;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class InvoiceController {

	@Autowired
	private InvoiceService invoiceService;
	
	
	
	@PostMapping("/invoice/save")
	public String addNewTicket(@ModelAttribute Invoice newInvoice,HttpSession session){
	    invoiceService.save(newInvoice);
	 
	    return "redirect:/admin.ark";
	}	
	
	@GetMapping("/invoiceDetails")
	@ResponseBody
	public Invoice getFeedbacksByStatusId(@RequestParam int ticketID) {
	    Optional<Invoice> invoice = invoiceService.findByTicketID(ticketID);
	    Invoice invoiceDetails = invoice.get();
	    return invoiceDetails;
	}
	
	@PostMapping("/invoice/update")
	public String updateInvoice(@ModelAttribute Invoice newInvoice,HttpSession session){
	    invoiceService.save(newInvoice);
	 
	    return "redirect:/admin.ark";
	}	
	

	
}

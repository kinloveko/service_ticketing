package com.ticketing_project.Ticketing.Project;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class InvoiceController {

	@Autowired
	private InvoiceService invoiceService;
	
	
	
	@PostMapping("/invoice/save")
	public String addNewTicket(@ModelAttribute Invoice newInvoice,HttpSession session){
	    invoiceService.save(newInvoice);
	 
	    return "redirect:/admin.ark";
	}	
	
}

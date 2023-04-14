package com.ticketing_project.Ticketing.Project;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TicketController {
	
	/*
	 * @GetMapping("/tickets/assigned/{userId}/filter")
	 * 
	 * @ResponseBody public List<Ticket> getTicketsByUserIdAndStatus(@PathVariable
	 * int userId,
	 * 
	 * @RequestParam String status){ return
	 * ticketService.getTicketsByStatusAndUserId(userId, status); }
	 */
	
	/*
	 * @GetMapping("/tickets/all/filter")
	 * 
	 * @ResponseBody public List<Ticket> getAllTicketsByStatus(@RequestParam final
	 * String status){ return ticketService.getTicketsByStatus(status); }
	 */
	
	
	
	@Autowired
	private TicketService ticketService;
	//ticketController
	
	
	//HTTP POST method
	@PostMapping("/tickets/post-ticket")
	public String addNewTicket(@ModelAttribute Ticket newTicket, RedirectAttributes redirectAttributes) {
		ticketService.save(newTicket);
		redirectAttributes.addFlashAttribute("successMessage", "Ticket saved successfully!");
		
		 return "redirect:/dashboard";
		}
	
	//HTTP POST method
	@PostMapping("/tickets/update-ticket-ongoing")
	public String updateOngoing(@ModelAttribute Ticket newTicket, RedirectAttributes redirectAttributes) {
		ticketService.save(newTicket);
		redirectAttributes.addFlashAttribute("successMessage", "Ticket saved successfully!");
		
		 return "redirect:/admin.ark";
		}
	
	@PostMapping("/tickets/update-ticket")
		public String conformeTicket(@ModelAttribute Ticket newTicket, RedirectAttributes redirectAttributes, @RequestParam MultipartFile img) {
			
			newTicket.setSalesSignature(img.getOriginalFilename());
			
			Ticket uploadImg = ticketService.save(newTicket);
			
			if (uploadImg != null) {
				try {

					File saveFile = new ClassPathResource("static/img").getFile();
					Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + img.getOriginalFilename());
					System.out.println(path);
					System.out.println(img.getOriginalFilename());
					Files.copy(img.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			redirectAttributes.addFlashAttribute("successMessage", "Conforme slip saved successfully!");
			 return "redirect:/admin.ark";
			}
	


		
	@PostMapping("/tickets/update-tickets-client")
	public String conforme_client_update(@ModelAttribute Ticket newTicket, RedirectAttributes redirectAttributes,
	        @RequestParam MultipartFile img1, 
	        @RequestParam MultipartFile img2) {
	    
	    newTicket.setClient_payment_proof(img1.getOriginalFilename());
	    newTicket.setClient_signature(img2.getOriginalFilename());

	    Ticket savedTicket = ticketService.save(newTicket);

	    if (savedTicket != null) {
	        try {
	            // Save proof of payment file
	            File saveFile = new ClassPathResource("static/img").getFile();
	            Path proofPath = Paths.get(saveFile.getAbsolutePath() + File.separator + img1.getOriginalFilename());
	            System.out.println(proofPath);
				System.out.println(img1.getOriginalFilename());
	            Files.copy(img1.getInputStream(), proofPath, StandardCopyOption.REPLACE_EXISTING);

	            // Save client signature file
	            Path signaturePath = Paths.get(saveFile.getAbsolutePath() + File.separator + img2.getOriginalFilename());
	            System.out.println(signaturePath);
				System.out.println(img2.getOriginalFilename());
	            Files.copy(img2.getInputStream(), signaturePath, StandardCopyOption.REPLACE_EXISTING);

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    redirectAttributes.addFlashAttribute("successMessage", "Conforme slip saved successfully!");
	    return "redirect:/dashboard";
	}
	
	
	
	
	
	@PutMapping("/tickets/update-ticket/{ticketId}")
	@ResponseBody
	public void updateTicket(@PathVariable final int ticketId, @ModelAttribute Ticket updatedTicket) {
		ticketService.update(ticketId, updatedTicket);
	}
	
	
	//HTTP DELETE method
	@DeleteMapping("/tickets/delete/{ticketId}")
	@ResponseBody
	public void deleteTicket(@PathVariable final int ticketId) {
		ticketService.delete(ticketId);
	}
	
	@GetMapping("/tickets/{ticketId}")
	public Ticket getTicketById(int ticketId) {
		return ticketService.getTicketById(ticketId);
	}
	
	@GetMapping("/tickets/assigned/{userId}")
	@ResponseBody
	public List<Ticket> getTicketsByUserId(@PathVariable final int userId){
		return ticketService.getTicketsByUserId(userId);
	}
	

    @GetMapping("/dashboard")
    public ModelAndView getTicketsByUserId(HttpSession session) {
        int user_id = (int) session.getAttribute("user_id");
        List<Ticket>list=ticketService.getTicketsByUserId(user_id);
        
        List<Ticket> pendingTickets = new ArrayList<>();
        int countPending = 0;
        List<Ticket> ongoingTickets = new ArrayList<>();
        int goingPending = 0;
        List<Ticket> completedTickets = new ArrayList<>();
        int completedPending = 0;
        
        for(Ticket i:list) {
        	if(i.getStatus().equals("pending")) {
        		countPending++;
        		pendingTickets.add(i);
        	}
        	if(i.getStatus().equals("ongoing")) {
        		goingPending++;
        		ongoingTickets.add(i);
        	}
        }
        
        ModelAndView mav = new ModelAndView("dashboard");
        mav.addObject("pending_tickets", pendingTickets);
        mav.addObject("pending_count",countPending);
        mav.addObject("ongoing_tickets", ongoingTickets);
        mav.addObject("ongoing_count",goingPending);
        mav.addObject("completed_tickets", completedTickets);
        mav.addObject("completed_count",completedPending);
        
        return mav;
    }
    
    
}
package com.ticketing_project.Ticketing.Project;


import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FeedbackController {

	@Autowired
	private FeedbackService feedbackService;
	
	@PostMapping("/feedback/add")
	public String addNewTicket(@ModelAttribute Feedback newFeedback, RedirectAttributes redirectAttributes, HttpSession session){
	    feedbackService.save(newFeedback);
	    int ticketId = (int) session.getAttribute("ticketId"); 
	    redirectAttributes.addFlashAttribute("successMessage", "Your feedback is much appreciated. Thank you!");
	    return "redirect:/ticket_progress?ticketId="+ticketId;
	}
	
	
	
	
	
	@GetMapping("/feedbacks")
	@ResponseBody
	public List<Feedback> getFeedbacksByStatusId(@RequestParam int statusId) {
	    List<Feedback> feedbacks = feedbackService.getFeedbacksByStatusId(statusId);
	    return feedbacks;
	}
	
}

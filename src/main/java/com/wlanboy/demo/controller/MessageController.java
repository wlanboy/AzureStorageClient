package com.wlanboy.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wlanboy.demo.model.StorageParameters;
import com.wlanboy.demo.storage.QueueClient;

@Controller
public class MessageController {

	private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    private final StorageParameters NO_MESSAGE = new StorageParameters("","NO MESSAGES","");
    
	@Autowired
	QueueClient queue;

	@RequestMapping("/message")
	public String index(Model model) {

		String error = null;
		StorageParameters latestMessage = null;

		try {
			latestMessage = queue.readMessage();
			
			if (latestMessage == null) {
				latestMessage = NO_MESSAGE;
			}
		} catch (Exception ex) {
			logger.error("group", ex);
			error = ex.getMessage();
		}

		model.addAttribute("latestMessage", latestMessage);
		model.addAttribute("error", error);
		return "message";
	}

	@PostMapping("/newmessage")
	public String upload(@RequestParam("text") String text, Model model) {
		String error = null;
		StorageParameters latestMessage = null;

		try {
			queue.createMessage(text);
			
			if (latestMessage == null) {
				latestMessage = NO_MESSAGE;
			}
			
			//latestMessage = queue.readMessage();
		} catch (Exception ex) {
			logger.error("group", ex);
			error = ex.getMessage();
		}

		model.addAttribute("latestMessage", latestMessage);
		model.addAttribute("error", error);

		return "message";
	}
	
}

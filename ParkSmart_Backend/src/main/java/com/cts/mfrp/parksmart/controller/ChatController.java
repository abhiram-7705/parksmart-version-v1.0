package com.cts.mfrp.parksmart.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cts.mfrp.parksmart.service.ChatService;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> request) {
        String reply = chatService.chat(request.get("message"));
        return ResponseEntity.ok(Map.of("reply", reply));
    }
}
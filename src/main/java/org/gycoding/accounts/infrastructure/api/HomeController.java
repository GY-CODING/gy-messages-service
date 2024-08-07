package org.gycoding.accounts.infrastructure.api;

import org.gycoding.accounts.domain.exceptions.ChatAPIError;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
public class HomeController {
    @GetMapping("/")
    public ResponseEntity<?> home() {
        Resource resource = new ClassPathResource("static/index.html");

        try {
            return ResponseEntity.ok(new String(Files.readAllBytes(Paths.get(resource.getURI()))));
        } catch (IOException e) {
            return ResponseEntity.status(ChatAPIError.HOME_NOT_FOUND.status).body(ChatAPIError.HOME_NOT_FOUND.toString());
        }
    }
}

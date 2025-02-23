package com.listeningraccoon.pamietaj_o_zdrowiu.backend.controllers;

import com.listeningraccoon.pamietaj_o_zdrowiu.backend.data.Prescription;
import com.listeningraccoon.pamietaj_o_zdrowiu.backend.data.User;
import com.listeningraccoon.pamietaj_o_zdrowiu.backend.services.PDFExportService;
import com.listeningraccoon.pamietaj_o_zdrowiu.backend.services.PrescriptionService;
import com.listeningraccoon.pamietaj_o_zdrowiu.backend.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
@RequestMapping("/api/v1/pdf")
public class PDFExportController {
    @Autowired
    private PrescriptionService prescriptionService;
    @Autowired
    private UserService userService;

    @GetMapping("/export")
    public void export(HttpServletResponse response, @RequestParam ObjectId userId, @RequestParam ObjectId prescriptionId) throws IOException {
        Optional<User> user = userService.getUserById(userId);
        Optional<Prescription> prescription = prescriptionService.getPrescriptionById(prescriptionId);
        if (user.isEmpty() || prescription.isEmpty()) {
            return;
        }

        response.setContentType("application/pdf");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy_HH:mm");
        LocalDateTime now = LocalDateTime.now();

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=prescription_"+now.format(formatter)+".pdf";
        response.setHeader(headerKey, headerValue);

        PDFExportService.export(response, user.get(), prescription.get());
    }
}

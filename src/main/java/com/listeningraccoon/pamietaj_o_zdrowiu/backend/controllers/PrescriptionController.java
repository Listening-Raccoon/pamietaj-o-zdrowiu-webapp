package com.listeningraccoon.pamietaj_o_zdrowiu.backend.controllers;

import com.listeningraccoon.pamietaj_o_zdrowiu.backend.data.Prescription;
import com.listeningraccoon.pamietaj_o_zdrowiu.backend.services.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/prescriptions")
public class PrescriptionController {
    @Autowired
    private PrescriptionService prescriptionService;

    @PostMapping("/add")
    public ResponseEntity<Prescription> createPrescription(@RequestBody Map<String, String> payload) {
        return new ResponseEntity<>(prescriptionService.createPrescription(payload.get("name"),
                payload.get("group"),
                payload.get("startDate"),
                payload.get("endDate"),
                payload.get("time"),
                payload.get("email")), HttpStatus.CREATED);
    }

    @GetMapping("/get")
    public ResponseEntity<List<Prescription>> getAllPrescriptionsOfUser(@RequestParam String email) {
        return new ResponseEntity<>(prescriptionService.getAllPrescriptionsOfUser(email), HttpStatus.OK);
    }
}

package com.omgtu.medhelper.controller;

import com.omgtu.medhelper.model.Medicine;
import com.omgtu.medhelper.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    private final MedicineService medicineService;

    @Autowired
    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<Medicine>> searchMedicines(@RequestParam String query) {
        List<Medicine> medicines = medicineService.searchMedicines(query);
        return ResponseEntity.ok(medicines);
    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<String>> getMedicineSuggestions(@RequestParam String query) {
        List<String> suggestions = medicineService.getMedicineSuggestions(query);
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medicine> getMedicineById(@PathVariable Long id) {
        return medicineService.getMedicineById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 
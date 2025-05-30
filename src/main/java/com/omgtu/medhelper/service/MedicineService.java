package com.omgtu.medhelper.service;

import com.omgtu.medhelper.model.Medicine;
import com.omgtu.medhelper.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MedicineService {

    private final MedicineRepository medicineRepository;

    @Autowired
    public MedicineService(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    public List<Medicine> searchMedicines(String query) {
        return medicineRepository.findByNameContainingIgnoreCase(query);
    }

    public List<String> getMedicineSuggestions(String query) {
        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }
        
        String searchQuery = query.trim().toLowerCase();
        return medicineRepository.findByNameContainingIgnoreCase(searchQuery)
                .stream()
                .map(Medicine::getName)
                .distinct()
                .collect(Collectors.toList());
    }

    public Optional<Medicine> getMedicineById(Long id) {
        return medicineRepository.findById(id);
    }
} 
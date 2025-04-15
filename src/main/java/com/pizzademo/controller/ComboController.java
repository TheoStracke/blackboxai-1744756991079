package com.pizzademo.controller;

import com.pizzademo.dto.ComboDTO;
import com.pizzademo.model.Combo;
import com.pizzademo.repository.ComboRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/combos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ComboController {

    private final ComboRepository comboRepository;

    @GetMapping
    public ResponseEntity<List<ComboDTO>> getAllCombos() {
        List<ComboDTO> combos = comboRepository.findAll().stream()
                .map(ComboDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(combos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComboDTO> getComboById(@PathVariable Long id) {
        return comboRepository.findById(id)
                .map(ComboDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createCombo(@Valid @RequestBody ComboDTO comboDTO) {
        if (comboRepository.existsByName(comboDTO.getName())) {
            return ResponseEntity.badRequest()
                    .body("Um combo com este nome já existe");
        }

        Combo combo = comboDTO.toEntity();
        Combo savedCombo = comboRepository.save(combo);
        return ResponseEntity.ok(ComboDTO.fromEntity(savedCombo));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateCombo(@PathVariable Long id, @Valid @RequestBody ComboDTO comboDTO) {
        if (!comboRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        Combo existingCombo = comboRepository.findById(id).get();
        if (!existingCombo.getName().equals(comboDTO.getName()) && 
            comboRepository.existsByName(comboDTO.getName())) {
            return ResponseEntity.badRequest()
                    .body("Um combo com este nome já existe");
        }

        Combo combo = comboDTO.toEntity();
        combo.setId(id);
        Combo updatedCombo = comboRepository.save(combo);
        return ResponseEntity.ok(ComboDTO.fromEntity(updatedCombo));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCombo(@PathVariable Long id) {
        if (!comboRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        comboRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}

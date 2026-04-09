package com.skillvault.controller;

import com.skillvault.dto.CertificationDTO;
import com.skillvault.dto.MessageResponse;
import com.skillvault.service.CertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api/certifications")
public class CertificationController {

    @Autowired
    private CertificationService certificationService;

    // ✅ USER: get own certifications
    @GetMapping
    public ResponseEntity<List<CertificationDTO>> getUserCertifications() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<CertificationDTO> certs = certificationService.getUserCertifications(auth.getName());
        return ResponseEntity.ok(certs);
    }

    // ✅ ADMIN: get ALL certifications
    @GetMapping("/all")
    public ResponseEntity<List<CertificationDTO>> getAllCertifications() {
        List<CertificationDTO> certs = certificationService.getAllCertifications();
        return ResponseEntity.ok(certs);
    }

    // ✅ ADD
    @PostMapping
    public ResponseEntity<CertificationDTO> addCertification(@RequestBody CertificationDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CertificationDTO created = certificationService.createCertification(dto, auth.getName());
        return ResponseEntity.ok(created);
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<CertificationDTO> updateCertification(
            @PathVariable Long id,
            @RequestBody CertificationDTO dto
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CertificationDTO updated = certificationService.updateCertification(id, dto, auth.getName());
        return ResponseEntity.ok(updated);
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCertification(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        certificationService.deleteCertification(id, auth.getName());
        return ResponseEntity.ok(new MessageResponse("Deleted successfully"));
    }
}
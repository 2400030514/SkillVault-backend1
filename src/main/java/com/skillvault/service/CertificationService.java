package com.skillvault.service;

import com.skillvault.dto.CertificationDTO;
import com.skillvault.model.Certification;
import com.skillvault.model.User;
import com.skillvault.repository.CertificationRepository;
import com.skillvault.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CertificationService {

    @Autowired
    private CertificationRepository certificationRepository;

    @Autowired
    private UserRepository userRepository;

    public CertificationDTO createCertification(CertificationDTO dto, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        Certification cert = new Certification();
        cert.setTitle(dto.getTitle()); // populated from 'name' json property
        cert.setOrganization(dto.getOrganization()); // from 'issuer' json property
        cert.setIssueDate(dto.getIssueDate());
        cert.setExpiryDate(dto.getExpiryDate());
        cert.setUser(user);
        System.out.println("DTO Title: " + dto.getTitle());
        System.out.println("DTO Org: " + dto.getOrganization());
        Certification saved = certificationRepository.save(cert);
        return mapToDTO(saved);
    }
    public List<CertificationDTO> getAllCertifications() {
        return certificationRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<CertificationDTO> getUserCertifications(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return certificationRepository.findByUserId(user.getId())
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public void deleteCertification(Long id, String email) {
        Certification cert = certificationRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        if (!cert.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized to delete this resource");
        }
        certificationRepository.delete(cert);
    }
    
    public CertificationDTO updateCertification(Long id, CertificationDTO dto, String email) {
        Certification cert = certificationRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        if (!cert.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized to modify this resource");
        }
        cert.setTitle(dto.getTitle());
        cert.setOrganization(dto.getOrganization());
        cert.setIssueDate(dto.getIssueDate());
        cert.setExpiryDate(dto.getExpiryDate());
        Certification saved = certificationRepository.save(cert);
        return mapToDTO(saved);
    }

    private CertificationDTO mapToDTO(Certification cert) {
        CertificationDTO dto = new CertificationDTO();
        dto.setId(cert.getId());
        dto.setTitle(cert.getTitle());
        dto.setOrganization(cert.getOrganization());
        dto.setIssueDate(cert.getIssueDate());
        dto.setExpiryDate(cert.getExpiryDate());
        return dto;
    }
}

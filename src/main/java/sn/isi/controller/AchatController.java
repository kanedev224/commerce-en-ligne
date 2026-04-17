package sn.isi.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.isi.dto.AchatDTO;
import sn.isi.service.AchatService;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/achats")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AchatController {
    private final AchatService achatService;

    @GetMapping
    public ResponseEntity<List<AchatDTO>> getAllAchats() {
        return ResponseEntity.ok(achatService.getAllAchats());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AchatDTO> getAchatById(@PathVariable Long id) {
        return ResponseEntity.ok(achatService.getAchatById(id));
    }

    @GetMapping("/produit/{produitId}")
    public ResponseEntity<List<AchatDTO>> getAchatsByProduitId(@PathVariable Long produitId) {
        return ResponseEntity.ok(achatService.getAchatsByProduitId(produitId));
    }

    @PostMapping
    public ResponseEntity<AchatDTO> createAchat(@Valid @RequestBody AchatDTO achatDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(achatService.createAchat(achatDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AchatDTO> updateAchat(@PathVariable Long id, @Valid @RequestBody AchatDTO achatDTO) {
        return ResponseEntity.ok(achatService.updateAchat(id, achatDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAchat(@PathVariable Long id) {
        achatService.deleteAchat(id);
        return ResponseEntity.noContent().build();
    }
}

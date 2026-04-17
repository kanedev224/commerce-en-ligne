package sn.isi.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.isi.dto.VenteDTO;
import sn.isi.service.VenteService;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ventes")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class VenteController {
    private final VenteService venteService;

    @GetMapping
    public ResponseEntity<List<VenteDTO>> getAllVentes() {
        return ResponseEntity.ok(venteService.getAllVentes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VenteDTO> getVenteById(@PathVariable Long id) {
        return ResponseEntity.ok(venteService.getVenteById(id));
    }

    @GetMapping("/produit/{produitId}")
    public ResponseEntity<List<VenteDTO>> getVentesByProduitId(@PathVariable Long produitId) {
        return ResponseEntity.ok(venteService.getVentesByProduitId(produitId));
    }

    @PostMapping
    public ResponseEntity<VenteDTO> createVente(@Valid @RequestBody VenteDTO venteDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(venteService.createVente(venteDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VenteDTO> updateVente(@PathVariable Long id, @Valid @RequestBody VenteDTO venteDTO) {
        return ResponseEntity.ok(venteService.updateVente(id, venteDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVente(@PathVariable Long id) {
        venteService.deleteVente(id);
        return ResponseEntity.noContent().build();
    }
}

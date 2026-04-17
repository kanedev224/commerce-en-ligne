package sn.isi.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.isi.dto.ProduitDTO;
import sn.isi.service.ProduitService;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/produits")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProduitController {
    private final ProduitService produitService;

    @GetMapping
    public ResponseEntity<List<ProduitDTO>> getAllProduits() {
        return ResponseEntity.ok(produitService.getAllProduits());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProduitDTO> getProduitById(@PathVariable Long id) {
        return ResponseEntity.ok(produitService.getProduitById(id));
    }

    @PostMapping
    public ResponseEntity<ProduitDTO> createProduit(@Valid @RequestBody ProduitDTO produitDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produitService.createProduit(produitDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProduitDTO> updateProduit(@PathVariable Long id, @Valid @RequestBody ProduitDTO produitDTO) {
        return ResponseEntity.ok(produitService.updateProduit(id, produitDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduit(@PathVariable Long id) {
        produitService.deleteProduit(id);
        return ResponseEntity.noContent().build();
    }
}

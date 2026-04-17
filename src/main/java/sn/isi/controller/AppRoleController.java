package sn.isi.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.isi.dto.AppRoleDTO;
import sn.isi.service.AppRoleService;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AppRoleController {
    private final AppRoleService appRoleService;

    @GetMapping
    public ResponseEntity<List<AppRoleDTO>> getAllRoles() {
        return ResponseEntity.ok(appRoleService.getAllRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppRoleDTO> getRoleById(@PathVariable Integer id) {
        return ResponseEntity.ok(appRoleService.getRoleById(id));
    }

    @PostMapping
    public ResponseEntity<AppRoleDTO> createRole(@Valid @RequestBody AppRoleDTO roleDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appRoleService.createRole(roleDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppRoleDTO> updateRole(@PathVariable Integer id, @Valid @RequestBody AppRoleDTO roleDTO) {
        return ResponseEntity.ok(appRoleService.updateRole(id, roleDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Integer id) {
        appRoleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}

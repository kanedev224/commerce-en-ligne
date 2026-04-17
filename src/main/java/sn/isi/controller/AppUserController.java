package sn.isi.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.isi.dto.AppUserDTO;
import sn.isi.service.AppUserService;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AppUserController {
    private final AppUserService appUserService;

    @GetMapping
    public ResponseEntity<List<AppUserDTO>> getAllUsers() {
        return ResponseEntity.ok(appUserService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUserDTO> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(appUserService.getUserById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<AppUserDTO> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(appUserService.getUserByEmail(email));
    }

    @PostMapping
    public ResponseEntity<AppUserDTO> createUser(@Valid @RequestBody AppUserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appUserService.createUser(userDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppUserDTO> updateUser(@PathVariable Integer id, @Valid @RequestBody AppUserDTO userDTO) {
        return ResponseEntity.ok(appUserService.updateUser(id, userDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        appUserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

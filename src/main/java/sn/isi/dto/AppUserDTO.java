package sn.isi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUserDTO {
    private Integer id;

    @NotBlank(message = "Le nom ne doit pas être vide")
    private String nom;

    @NotBlank(message = "Le prénom ne doit pas être vide")
    private String prenom;

    @Email(message = "L'email doit être valide")
    @NotBlank(message = "L'email ne doit pas être vide")
    private String email;

    @NotBlank(message = "Le mot de passe ne doit pas être vide")
    private String password;

    @NotNull(message = "L'état ne doit pas être null")
    private Integer etat;
}

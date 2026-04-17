package sn.isi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppRoleDTO {
    private Integer id;

    @NotBlank(message = "Le nom du rôle ne doit pas être vide")
    private String nom;
}

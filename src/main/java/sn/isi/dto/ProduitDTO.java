package sn.isi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProduitDTO {
    private Long id;

    @NotBlank(message = "La référence ne doit pas être vide")
    private String ref;

    @NotBlank(message = "Le nom ne doit pas être vide")
    private String name;

    @NotNull(message = "Le stock ne doit pas être null")
    @Positive(message = "Le stock doit être positif")
    private Double stock;
}

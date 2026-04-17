package sn.isi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AchatDTO {
    private Long id;

    @NotNull(message = "La date d'achat ne doit pas être null")
    private Date dateAchat;

    @NotNull(message = "La quantité ne doit pas être null")
    @Positive(message = "La quantité doit être positive")
    private Double quantity;

    @NotNull(message = "L'ID du produit ne doit pas être null")
    private Long produitId;
}

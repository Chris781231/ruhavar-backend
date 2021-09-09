package ruhavarbackend.phonenumber.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePhoneNumberCommand {

    @Schema(example = "cell")
    @NotBlank
    private String type;

    @Schema(example = "701234567")
    @NotBlank
    private String number;
}

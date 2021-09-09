package ruhavarbackend.phonenumber.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPhoneNumberCommand {

    @Schema(description = "type of phonenumber, e.g. cell, mobil, home, private", example = "cell")
    @NotBlank
    private String type;

    @Schema(description = "number of phonenumber, e.g. +36201234567", example = "+36201234567")
    @NotBlank
    private String number;
}

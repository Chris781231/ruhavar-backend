package ruhavarbackend.phonenumber.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePhoneNumberCommand {

    @Schema(example = "2098765432")
    @NotBlank
    private String number;
}

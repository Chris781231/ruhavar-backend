package ruhavarbackend.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCustomerCommand {

    @Schema(description = "name of customer", example = "Balogh Béla")
    @NotBlank
    private String name;

    @Schema(example = "Budapest")
    @NotBlank
    private String city;

    @Schema(example = "Kossuth utca 2")
    @NotBlank
    private String address;

    @Schema(example = "701234567")
    private String phoneNumber;

    @Schema(example = "eztalanszabad@gmail.com")
    private String email;
}

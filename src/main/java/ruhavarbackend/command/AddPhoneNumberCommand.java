package ruhavarbackend.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPhoneNumberCommand {

    @NotBlank
    private String type;

    @NotBlank
    private String number;
}

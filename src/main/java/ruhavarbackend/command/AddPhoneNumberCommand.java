package ruhavarbackend.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ruhavarbackend.entity.PhoneNumber;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPhoneNumberCommand {

    @NotBlank
    private String type;

    @NotBlank
    private String number;
}

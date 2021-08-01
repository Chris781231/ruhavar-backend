package ruhavarbackend.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class PhoneNumberNotFoundException extends AbstractThrowableProblem {

    public PhoneNumberNotFoundException(long id) {
        super(URI.create("phonenumbers/phone-number-not-found"),
                "Not found",
                Status.NOT_FOUND,
                String.format("Phone number with id %d not found", id));
    }
}

package ruhavarbackend.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class CustomerNotFoundException extends AbstractThrowableProblem {

    public CustomerNotFoundException(long id) {
        super(URI.create("customers/customer-not-found"),
                "Not found",
                Status.NOT_FOUND,
                String.format("Customer with id %d not found", id));
    }
}

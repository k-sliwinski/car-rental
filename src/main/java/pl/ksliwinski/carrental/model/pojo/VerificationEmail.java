package pl.ksliwinski.carrental.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerificationEmail {
    private String subject;
    private String recipient;
    private String body;
}

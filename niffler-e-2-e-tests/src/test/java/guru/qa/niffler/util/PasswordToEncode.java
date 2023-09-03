package guru.qa.niffler.util;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordToEncode {
    public static PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();
}

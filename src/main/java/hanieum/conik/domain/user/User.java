package hanieum.conik.domain.user;

import hanieum.conik.domain.user.dto.UserSignUpRequest;

public class User {

    private String email;
    private String password;
    private String phoneNumber;
    private boolean termsOfServiceAgreed;
    private UserRole role;

    public static User signUp(UserSignUpRequest request) {
        User user = new User();

        user.email = request.email();
        user.password = request.password();
        user.phoneNumber = request.phoneNumber();
        user.termsOfServiceAgreed = request.termsOfServiceAgreed();
        user.role = request.role();

        return user;
    }
}

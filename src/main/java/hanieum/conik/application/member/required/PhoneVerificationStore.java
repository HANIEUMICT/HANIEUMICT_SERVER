package hanieum.conik.application.member.required;

public interface PhoneVerificationStore {
    boolean isVerified(String phoneNumber);
    void removeVerifiedFlag(String phoneNumber);
}

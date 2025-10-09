package hanieum.conik.adapter.member.sms;

import hanieum.conik.application.member.required.PhoneVerificationStore;
import hanieum.conik.global.application.required.MemoryMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PhoneVerificationAdapter implements PhoneVerificationStore {
    private final MemoryMap memoryMap;

    @Override
    public boolean isVerified(String phoneNumber) {
        String verified = memoryMap.getValue("verified:" + phoneNumber);
        return "true".equals(verified);
    }

    @Override
    public void removeVerifiedFlag(String phoneNumber) {
        memoryMap.deleteValue("verified:" + phoneNumber);
    }
}

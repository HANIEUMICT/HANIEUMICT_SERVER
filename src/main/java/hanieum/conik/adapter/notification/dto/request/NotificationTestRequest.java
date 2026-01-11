package hanieum.conik.adapter.notification.dto.request;

public record NotificationTestRequest(
        Long targetMemberId,
        String type,
        String title,
        String message,
        Long referenceId
) {
}

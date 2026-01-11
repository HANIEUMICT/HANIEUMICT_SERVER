package hanieum.conik.domain.notification.dto;

public record NotificationPayload(
        String type,
        String title,
        String message,
        Long referenceId
) {
}

package hanieum.conik.domain.chat.enumerate;

public enum MessageType {
    TEXT,       // 일반 텍스트 메시지
    IMAGE,      // 이미지 전송
    FILE,       // 파일 전송
    SYSTEM      // 시스템 알림 (예: 입장/퇴장 메시지)
}
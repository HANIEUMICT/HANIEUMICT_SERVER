package hanieum.conik.domain.chat.enumerate;

public enum MessageType {
    TEXT,       // 일반 텍스트 메시지
    IMAGE,      // 이미지
    FILE,       // 문서, PDF 등
    VIDEO,      // 영상
    AUDIO,      // 음성
    SYSTEM      // 시스템 메시지 (입장/퇴장 등)
}
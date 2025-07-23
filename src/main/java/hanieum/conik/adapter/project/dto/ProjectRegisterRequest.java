package hanieum.conik.adapter.project.dto;

public record ProjectRegisterRequest (
    String title,
    String description,
    String category,
    String progressStep,
    Long userId
){}

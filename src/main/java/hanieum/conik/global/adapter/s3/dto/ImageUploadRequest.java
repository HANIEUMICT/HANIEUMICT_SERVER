package hanieum.conik.global.adapter.s3.dto;

public record ImageUploadRequest(
        String prefix,

        String originalFilename
) { }

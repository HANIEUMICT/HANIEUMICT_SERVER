package hanieum.conik.global.clients.s3.dto;

import lombok.Builder;

@Builder
public record ReadPreSignedUrlResponse (
        String preSignedUrl,

        String objectUrl
){
    public static ReadPreSignedUrlResponse of(String preSignedUrl, String objectUrl) {
        return ReadPreSignedUrlResponse.builder()
                .preSignedUrl(preSignedUrl)
                .objectUrl(objectUrl)
                .build();
    }
}
package hanieum.conik.global.application.required;

import hanieum.conik.global.adapter.s3.dto.ImageUploadRequest;
import hanieum.conik.global.adapter.s3.dto.ReadPreSignedUrlResponse;

public interface BucketClient {
    ReadPreSignedUrlResponse getPreSignedUrl(ImageUploadRequest imageUploadRequest);

    String postPresignedUrl(String key);

    String getPresignedUrl(String prefix, String originalFileName);

    String getPublicUrl(String key);

    String createPath(String prefix, String originalFileName);
}


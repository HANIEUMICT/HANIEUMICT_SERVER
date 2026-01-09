package hanieum.conik.global.application.required;

import hanieum.conik.global.adapter.s3.dto.ImageUploadRequest;
import hanieum.conik.global.adapter.s3.dto.ReadPreSignedUrlResponse;

public interface BucketClient {
    ReadPreSignedUrlResponse getPreSignedUrl(ImageUploadRequest imageUploadRequest);

    String makePublic(String objectUrl);
}


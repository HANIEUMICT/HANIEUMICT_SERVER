package hanieum.conik.global.adapter.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import hanieum.conik.global.adapter.s3.dto.ImageUploadRequest;
import hanieum.conik.global.adapter.s3.dto.ReadPreSignedUrlResponse;
import hanieum.conik.global.application.required.BucketClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class S3BucketClient implements BucketClient {

    @Value("${cloud.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    @Override
    public ReadPreSignedUrlResponse getPreSignedUrl(ImageUploadRequest imageUploadRequest) {
        String key = createPath(imageUploadRequest.prefix(), imageUploadRequest.originalFilename());

        String presignedUrl = postPresignedUrl(key);
        String accessUrl =  getPublicUrl(key);

        return ReadPreSignedUrlResponse.of(presignedUrl, accessUrl);
    }

    @Override
    public String postPresignedUrl(String key) {
        Date expiration = new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(2));
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucket, key)
                .withMethod(HttpMethod.PUT)
                .withExpiration(expiration);

        return amazonS3.generatePresignedUrl(req).toString();
    }

    @Override
    public String getPresignedUrl(String prefix, String originalFileName) {
        String key = createPath(prefix, originalFileName);

        Date expiration = new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10));
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucket, key)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);

        return amazonS3.generatePresignedUrl(req).toString();
    }

    @Override
    public String getPublicUrl(String key) {
        return amazonS3.getUrl(bucket, key).toString();
    }

    @Override
    public String createPath(String prefix, String originalFileName) {
        String safeFileName = URLEncoder
                .encode(originalFileName, StandardCharsets.UTF_8)
                .replace("+", "%20");
        return String.format("%s/%s", prefix, safeFileName);
    }
}

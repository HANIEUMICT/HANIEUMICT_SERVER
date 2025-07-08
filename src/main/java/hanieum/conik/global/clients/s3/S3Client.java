package hanieum.conik.global.clients.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import hanieum.conik.global.clients.s3.dto.ReadPreSignedUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class S3Client {

    @Value("${cloud.s3.bucket}")
    private String bucket;

    private final AmazonS3 S3Client;

    public ReadPreSignedUrlResponse getPreSignedUrl(String prefix, String originalFilename) {
        String key = createPath(prefix, originalFilename);

        String presignedUrl = postPresignedUrl(key);
        String accessUrl =  getPublicUrl(key);

        return ReadPreSignedUrlResponse.of(presignedUrl, accessUrl);
    }

    private String postPresignedUrl(String key) {
        Date expiration = new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(2));
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucket, key)
                .withMethod(HttpMethod.PUT)
                .withExpiration(expiration);

        return S3Client.generatePresignedUrl(req).toString();
    }

    private String getPresignedUrl(String prefix, String originalFileName) {
        String key = createPath(prefix, originalFileName);

        Date expiration = new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10));
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucket, key)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);

        return S3Client.generatePresignedUrl(req).toString();
    }

    private String getPublicUrl(String key) {
        return S3Client.getUrl(bucket, key).toString();
    }

    private String createPath(String prefix, String originalFileName) {
        String safeFileName = URLEncoder
                .encode(originalFileName, StandardCharsets.UTF_8)
                .replace("+", "%20");
        return String.format("%s/%s", prefix, safeFileName);
    }
}

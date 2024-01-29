package com.flix.video.service;

import com.flix.aggregator.service.AggregateService;
import com.flix.video.persistence.entity.VideoDocument;
import com.flix.video.persistence.entity.VideoSearchDocument;
import com.flix.video.persistence.repository.VideoDocumentRepository;
import com.flix.video.persistence.repository.VideoSearchRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Objects;

@Service
public class VideoUploadService {

    @Value("${aws.s3.region:us-east-1}")
    private String region;

    @Value("${aws.s3.bucket-name:flix-video}")
    private String bucketName;

    @Value("${aws.access-key:ABCDESFGHIJKLMNOPQRST}")
    private String accessKey;

    @Value("${aws.secret-key:X1X2X3X4X5X6X7X8X9X0}")
    private String secretKey;

    @Autowired
    private VideoDocumentRepository videoDocumentRepository;

    @Autowired
    private VideoSearchRepository videoSearchRepository;

    @Autowired
    private VideoService videoService;

    @Autowired
    private AggregateService aggregateService;

    public void uploadVideo(MultipartFile videoFile, String idVideo) throws IOException {

        System.out.println("region: " + region);

        if (region == null || bucketName == null || accessKey == null || secretKey == null) {
            throw new IOException("Error on video upload: AWS credentials are not properly configured");
        }

        try {
            VideoDocument videoDocument = videoService.findVideo(idVideo);

            String fileName = generateUniqueFileName(Objects.requireNonNull(videoFile.getOriginalFilename()), idVideo);

            S3Client s3Client = S3Client.builder()
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                    .build();

            RequestBody requestBody = RequestBody.fromBytes(videoFile.getBytes());

            s3Client.putObject(PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build(), requestBody);

            String s3ObjectUrl = "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName;

            videoDocument.setUrl(s3ObjectUrl);
            videoDocumentRepository.save(videoDocument);
            videoSearchRepository.save(toVideoSearchDocument(videoDocument));

            aggregateService.aggregateTotalVideo();

        } catch (IOException e) {
            throw new IOException("Error on video upload", e);
        }
    }

    String generateUniqueFileName(String originalFileName, String idVideo) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        return idVideo + extension;
    }

    private VideoSearchDocument toVideoSearchDocument(VideoDocument videoDocument) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(videoDocument, VideoSearchDocument.class);
    }
}




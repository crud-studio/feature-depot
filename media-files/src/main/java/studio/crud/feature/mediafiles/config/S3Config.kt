package studio.crud.feature.mediafiles.config

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class S3Config {
    @Bean
    fun s3Client(): AmazonS3 {
        return AmazonS3ClientBuilder
            .standard()
            .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
            .build()
    }
}
package life.duanfu.community.provider;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import life.duanfu.community.exception.CustomizeErrorCode;
import life.duanfu.community.exception.CustomizeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
public class AliyunProvider {
    @Value("${aliyun.endpoint}")
    private String endpoint;
    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;
    @Value("${aliyun.bucket-name}")
    private String bucketName;

    public String upload(InputStream FileStream, String fileName) {
        OSS client = null;
        String generatedFileName;
        String[] filePaths = fileName.split("\\.");
        if (filePaths.length > 1) {
            generatedFileName = UUID.randomUUID().toString() + "." + filePaths[filePaths.length - 1];
        } else {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
        try {
            // 创建OSSClient实例。
            client = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            client.putObject(bucketName, generatedFileName, FileStream);

            if (client != null) {
                // 设置URL过期时间为10年。
                Date expiration = new Date(new Date().getTime() + 3600 * 1000 * 24 * 365 * 10);
                // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
                URL url = client.generatePresignedUrl(bucketName, generatedFileName, expiration);
                return url.toString();
            } else {
                throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        } finally {
            // 关闭OSSClient。
            client.shutdown();
        }
    }
}

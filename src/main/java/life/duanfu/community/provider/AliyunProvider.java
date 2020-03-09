package life.duanfu.community.provider;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
public class AliyunProvider {
    @Value("${aliyun.endpoint}")
    private String endpoint;
    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;

    public String upload(InputStream FileStream,String fileName) {
        OSS client = null;
        String generatedFileName;
        String[] filePaths = fileName.split("\\.");
        if (filePaths.length > 1) {
            generatedFileName = UUID.randomUUID().toString() + "." + filePaths[filePaths.length - 1];
        } else {
            return null;
        }
        try {
            // 创建OSSClient实例。
            client = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            client.putObject("duanfu", generatedFileName, FileStream);
        } finally {
            // 关闭OSSClient。
            client.shutdown();
        }
        return generatedFileName;
    }
}

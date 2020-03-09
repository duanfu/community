package life.duanfu.community.controller;

import life.duanfu.community.dto.FileDTO;
import life.duanfu.community.provider.AliyunProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class FileController {

    @Autowired
    private AliyunProvider aliyunProvider;

    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest =(MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("editormd-image-file");
        try {
            aliyunProvider.upload(file.getInputStream(),file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setUrl("/images/慢慢变老.jpg");
        return fileDTO;
    }
}

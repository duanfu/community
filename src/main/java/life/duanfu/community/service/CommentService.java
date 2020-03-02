package life.duanfu.community.service;

import life.duanfu.community.exception.CustomizeErrorCode;
import life.duanfu.community.exception.CustomizeException;
import life.duanfu.community.model.Comment;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    public void insert(Comment comment) {
        //首先comment必须得存在
        if (comment.getParentId() == null || comment.getParentId() == 0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
    }
}

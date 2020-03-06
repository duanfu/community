package life.duanfu.community.mapper;

import life.duanfu.community.model.Comment;

public interface CommentExtMapper {
    int incCommentCount(Comment comment);
}
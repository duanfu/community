package life.duanfu.community.mapper;

import life.duanfu.community.model.Question;

public interface QuestionExtMapper {
    int incView(Question record);
    int incCommentCount(Question record);
}

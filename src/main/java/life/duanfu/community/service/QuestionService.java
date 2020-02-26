package life.duanfu.community.service;

import life.duanfu.community.dto.PaginationDTO;
import life.duanfu.community.dto.QuestionDTO;
import life.duanfu.community.mapper.QuestionMapper;
import life.duanfu.community.mapper.UserMapper;
import life.duanfu.community.model.Question;
import life.duanfu.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    public PaginationDTO list(Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.count();
        paginationDTO.setPagination(totalCount,page,size);
        //保证page>totalPage，的时候数据会显示到totalPage这页，如果删掉数据不展示
        //在DTO判断只能让数字切换回来数据不显示，必须在数据库层面，判断一次。为了不复写代码，加了一个字段totalPage
        if (page < 1) {
            page = 1;
        }
        if (page > paginationDTO.getTotalPage()) {
            page = paginationDTO.getTotalPage();
        }
        //size*(page-1)
        Integer offset = size *(page-1);
        List<Question> questions = questionMapper.list(offset,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();


        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //Spring内置的可以快速的把question对象的属性 拷贝到 questionDTO对象
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }
}

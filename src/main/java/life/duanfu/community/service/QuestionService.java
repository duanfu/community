package life.duanfu.community.service;

import life.duanfu.community.dto.PaginationDTO;
import life.duanfu.community.dto.QuestionDTO;
import life.duanfu.community.dto.QuestionQueryDTO;
import life.duanfu.community.exception.CustomizeErrorCode;
import life.duanfu.community.exception.CustomizeException;
import life.duanfu.community.mapper.QuestionExtMapper;
import life.duanfu.community.mapper.QuestionMapper;
import life.duanfu.community.mapper.UserMapper;
import life.duanfu.community.model.Question;
import life.duanfu.community.model.QuestionExample;
import life.duanfu.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    //注入进来扩展的Mapper，增加阅读数
    @Autowired
    private QuestionExtMapper questionExtMapper;

    //分页
    public PaginationDTO list(String search ,Integer page, Integer size) {

        if (StringUtils.isNotBlank(search)) {
            String[] tags = StringUtils.split(search, " ");
            search=Arrays.stream(tags).collect(Collectors.joining("|"));
        }

        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalPage;

        //Integer totalCount = questionMapper.count();

        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        Integer totalCount = questionExtMapper.countBySearch(questionQueryDTO);

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        //按钮的一些设置，并且一个列表展示7页，左边三个，右边三个。
        //首页，前一页，后一页，尾页的设置
        paginationDTO.setPagination(totalPage, page);
        //计算 size*(page-1)
        Integer offset = size * (page - 1);
        questionQueryDTO.setPage(offset);
        questionQueryDTO.setSize(size);
        List<Question> questions = questionExtMapper.selectBySearch(questionQueryDTO);

        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {
            //User user = userMapper.findById(question.getCreator());
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //Spring内置的可以快速的把question对象的属性 拷贝到 questionDTO对象
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }

    //展示我的问题
    public PaginationDTO list(Long userId, Integer page, Integer size) {

        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalPage;

        //通过userId计算我的问题的总数
        //Integer totalCount = questionMapper.countByUserId(userId);
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(questionExample);

        //越界问题
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage, page);
        //size*(page-1)
        Integer offset = size * (page - 1);
        //List<Question> questions = questionMapper.listByUserId(userId, offset, size);
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(example, new RowBounds(offset, size));

        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            //User user = userMapper.findById(question.getCreator());
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();

            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }

    //问题详情页面
    public QuestionDTO getById(Long id) {
        //Question question = questionMapper.getById(id);
        Question question = questionMapper.selectByPrimaryKey(id);
        //返回异常信息
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        //question页面用到了作者
        //User user = userMapper.findById(question.getCreator());
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    //修改问题页面
    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            //questionMapper.create(question);
            questionMapper.insert(question);
        } else {
            //更新
            //question.setGmtModified(question.getGmtCreate());
            //questionMapper.update(question);
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());

            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExampleSelective(updateQuestion, example);
            //更新接口，我默认页面传过来的这个id，数据库中它已经存在，直接更新了。
            //可能数据库中就没有，我们都知道更新成功是返回1，更新失败是返回0，所以说可能数据库中就不存在这个，
            //或者说，你这个页面停留在更新页面的时候，我在另一个页面把原来的问题删除了，所以我点击发布按钮的时候可能我
            //原问题已经不存在了，所以这个地方也要加一个验证。
            if (updated != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    //阅读数累加
    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }

    //获取相关问题列表
    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if (StringUtils.isBlank(queryDTO.getTag())) {
            return new ArrayList<>();
        }

        String[] tags = StringUtils.split(queryDTO.getTag(), ",");
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexpTag);
        List<Question> questions = questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());
        return questionDTOS;
    }
}

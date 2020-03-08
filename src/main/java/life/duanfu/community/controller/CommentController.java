package life.duanfu.community.controller;

import life.duanfu.community.dto.CommentCreateDTO;
import life.duanfu.community.dto.CommentDTO;
import life.duanfu.community.dto.ResultDTO;
import life.duanfu.community.enums.CommentTypeEnum;
import life.duanfu.community.exception.CustomizeErrorCode;
import life.duanfu.community.model.Comment;
import life.duanfu.community.model.User;
import life.duanfu.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    //使用JSON的方式去做，method = RequestMethod.POST
    //前端传过来请求的时候，我们要拿到一个JSON，拿到后反序列化成自己的一个对象，然后做操作
    //回给前端的时候呢，也是返回一个Java的Object，让Spring去做，把Object转换成JSON。
    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    //@RequestBody CommentCreateDTO commentDTO自动的把key和value赋值上去了
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        //StringUtils是Commons lang3包，下面封装的一个方法
        if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())) {
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        commentService.insert(comment, user);
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id") Long id) {
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOS);
    }
}

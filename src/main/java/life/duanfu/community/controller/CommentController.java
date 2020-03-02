package life.duanfu.community.controller;

import life.duanfu.community.dto.CommentDTO;
import life.duanfu.community.dto.ResultDTO;
import life.duanfu.community.exception.CustomizeErrorCode;
import life.duanfu.community.model.Comment;
import life.duanfu.community.model.User;
import life.duanfu.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    //使用JSON的方式去做，method = RequestMethod.POST
    //前端传过来请求的时候，我们要拿到一个JSON，拿到后反序列化成自己的一个对象，然后做操作
    //回给前端的时候呢，也是返回一个Java的Object，让Spring去做，把Object转换成JSON。
    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    //@RequestBody CommentDTO commentDTO自动的把key和value赋值上去了
    public Object post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        commentService.insert(comment);
        return  ResultDTO.okOf();
    }
}

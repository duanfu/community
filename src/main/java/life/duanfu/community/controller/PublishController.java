package life.duanfu.community.controller;

import life.duanfu.community.dto.QuestionDTO;
import life.duanfu.community.model.Question;
import life.duanfu.community.model.User;
import life.duanfu.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {


    @Autowired
    private QuestionService questionService;


    //修改问题页面
    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Integer id,
                       Model model) {
        QuestionDTO question = questionService.getById(id);
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        return "publish";
    }

    //添加问题页面
    @GetMapping("/publish")
    public String publish() {
        return "publish";
    }

    //点击发布按钮，会走这个请求，post提交表单
    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "tag", required = false) String tag,
            //拿到input的隐藏属性
            @RequestParam(value = "id", required = false) Integer id,
            HttpServletRequest request,
            Model model) {

        //把值set到model里面，然后页面就可以拿到了
        //th:value="${title}"，th:text="${description}"，th:value="${tag}"
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);


        //验证失败，正常情况下，应该在前端点击发布按钮的时候，
        //通过js去校验一下，它是否为空，为空的话提示我错误信息。
        //但是我们主要的发力点是服务端，所以我们直接把逻辑写在服务端
        if (title == null || title == "") {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (description == null || description == "") {
            model.addAttribute("error", "问题补充不能为空");
            return "publish";
        }
        if (tag == null || tag == "") {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }

        //获取User
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "publish";
        }
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        //把页面的隐藏属性set进来，它是可以空的
        question.setId(id);
        //questionMapper.create(question);
        questionService.createOrUpdate(question);
        return "redirect:/";
    }
}

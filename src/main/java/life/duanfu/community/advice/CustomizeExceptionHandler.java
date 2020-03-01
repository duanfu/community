package life.duanfu.community.advice;

import life.duanfu.community.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    //@ResponseBody 我不希望它返回Json需要返回Model and View
    ModelAndView handle(Throwable e, Model model) {
        if (e instanceof CustomizeException){
            model.addAttribute("message",e.getMessage());

        }else {
            //默认的
            model.addAttribute("message","服务冒烟了，要不然你稍后试试！！！");
        }
        return new ModelAndView("error");
    }
}

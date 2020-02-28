package life.duanfu.community.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
//@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private SessionInterceptor sessionInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //请求的时候，addPathPatterns：会拦截的地址
        //excludePathPatterns：略过的地址
        //我们希望所有的路径都检验一下
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**");
    }
}
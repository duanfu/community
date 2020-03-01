package life.duanfu.community.exception;

//为什么要继承一个RuntimeException，如果不继承RuntimeException在异常的方法里面直接throw出来的话，
//必须得在上一层try catch，所以说我这个异常我不希望在调用的时候，有任何的影响，
//而仅仅在我ControllerAdvice里面去开始就好了
public class CustomizeException extends RuntimeException {
    private String message;

    public CustomizeException(ICustomizeErrorCode errorCode) {
        this.message = errorCode.getMessage();
    }

    public CustomizeException(String message) {
        this.message = message;
    }

    //外层可以拿到我的方法，重载了父类的Throwable的方法
    @Override
    public String getMessage() {
        return message;
    }
}

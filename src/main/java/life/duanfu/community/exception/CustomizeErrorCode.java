package life.duanfu.community.exception;

//为了不会后面去做类爆炸，就是说后面我可能后面不同的业务，不同的question，service，
//不同的UserService都会有自己的，我不能说我把每一个业务异常全都定义到一起，这样的话类非常庞大，
//所以为了方便，我给question定义一个通用的，然后分别去给他做一个封装。
//所以我希望外层传进去的使用的时候，是一个接口的形式。里面我可以定义成不同的业务类型的ErrorCode，系统类型的ErrorCode
public enum CustomizeErrorCode implements ICustomizeErrorCode {

    QUESTION_NOT_FOUND(2001, "你找的问题不存在了，要不要换个试试？"),
    TARGET_PARAM_NOT_FOUND(2002, "未选中任何问题或评论进行回复"),
    NO_LOGIN(2003,"当前操作需要登录，请登录后重试"),
    SYS_ERROR(2004,"服务冒烟了，要不然你稍后试试！！！"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"回复的评论不存在了，要不要换个试试？"),
    ;

    private Integer code;
    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    //调整参数ctrl+F6,好处如果其他地方依赖的话，会自动调整前后位置
    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }


}

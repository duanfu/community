package life.duanfu.community.exception;

//为了不会后面去做类爆炸，就是说后面我可能后面不同的业务，不同的question，service，
//不同的UserService都会有自己的，我不能说我把每一个业务异常全都定义到一起，这样的话类非常庞大，
//所以为了方便，我给question定义一个通用的，然后分别去给他做一个封装。
//所以我希望外层传进去的使用的时候，是一个接口的形式。里面我可以定义成不同的业务类型的ErrorCode，系统类型的ErrorCode
public enum CustomizeErrorCode implements ICustomizeErrorCode {
    QUESTION_NOT_FOUND("你找的问题不存在了，要不要换个试试？");

    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    CustomizeErrorCode(String message) {
        this.message = message;
    }
}

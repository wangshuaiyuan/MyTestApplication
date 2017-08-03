package wsy.org.mytestapplication.taskHandler.entity;

/**
 * Created by wsy on 2017/2/28.
 */
public class MessageBean<T> {
    /**
     * 所对应的taskId
     */
//    public String taskId;
    /**
     * 消息码
     */
    public int messageCode;
    /**
     * 消息相关信息
     */
    public String messageInfo;
    /**
     * 消息体
     */
    public T messageBody;
    /**
     * 任务总量
     */
    public int total;
    /**
     * 任务当前进度
     */
    public int present;
}

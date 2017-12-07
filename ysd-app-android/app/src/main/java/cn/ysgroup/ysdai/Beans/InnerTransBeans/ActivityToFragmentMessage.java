package cn.ysgroup.ysdai.Beans.InnerTransBeans;

/**
 * Created by Administrator on 2015/10/23.
 */
public class ActivityToFragmentMessage {
    private int tag;
    private String messageString;
    private int messageInt;
    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public String getMessageString() {
        return messageString;
    }

    public void setMessageString(String messageString) {
        this.messageString = messageString;
    }

    public int getMessageInt() {
        return messageInt;
    }

    public void setMessageInt(int messageInt) {
        this.messageInt = messageInt;
    }

}

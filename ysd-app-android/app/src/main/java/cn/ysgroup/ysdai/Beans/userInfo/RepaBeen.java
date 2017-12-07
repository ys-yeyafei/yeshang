package cn.ysgroup.ysdai.Beans.userInfo;

import java.util.List;

/**
 * Created by linyujie on 17/1/14.
 */

public class RepaBeen {

    /**
     * timeLimit : 30
     * createTime : 2017-01-13 09:39:58
     * borrowStatus : 3
     * apr : 8.64
     * borrowStatusVal : 还款中
     * borrowName : null
     * interest : 0.72
     * account : 100
     * userRepDetailList : [{"waitAccount":"100","repaymentDate":"2017-02-12","waitInterest":"0.72"}]
     * rcd : R0001
     * rmg :
     * hongbaoAmount : 0
     */

    private String timeLimit;
    private String createTime;
    private int borrowStatus;
    private double apr;
    private String borrowStatusVal;
    private Object borrowName;
    private String interest;
    private String account;
    private String rcd;
    private String rmg;
    private String hongbaoAmount;
    /**
     * waitAccount : 100
     * repaymentDate : 2017-02-12
     * waitInterest : 0.72
     */

    private List<UserRepDetailListBean> userRepDetailList;

    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getBorrowStatus() {
        return borrowStatus;
    }

    public void setBorrowStatus(int borrowStatus) {
        this.borrowStatus = borrowStatus;
    }

    public double getApr() {
        return apr;
    }

    public void setApr(double apr) {
        this.apr = apr;
    }

    public String getBorrowStatusVal() {
        return borrowStatusVal;
    }

    public void setBorrowStatusVal(String borrowStatusVal) {
        this.borrowStatusVal = borrowStatusVal;
    }

    public Object getBorrowName() {
        return borrowName;
    }

    public void setBorrowName(Object borrowName) {
        this.borrowName = borrowName;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRcd() {
        return rcd;
    }

    public void setRcd(String rcd) {
        this.rcd = rcd;
    }

    public String getRmg() {
        return rmg;
    }

    public void setRmg(String rmg) {
        this.rmg = rmg;
    }

    public String getHongbaoAmount() {
        return hongbaoAmount;
    }

    public void setHongbaoAmount(String hongbaoAmount) {
        this.hongbaoAmount = hongbaoAmount;
    }

    public List<UserRepDetailListBean> getUserRepDetailList() {
        return userRepDetailList;
    }

    public void setUserRepDetailList(List<UserRepDetailListBean> userRepDetailList) {
        this.userRepDetailList = userRepDetailList;
    }

    public static class UserRepDetailListBean {
        private String waitAccount;
        private String repaymentDate;
        private String waitInterest;

        public String getWaitAccount() {
            return waitAccount;
        }

        public void setWaitAccount(String waitAccount) {
            this.waitAccount = waitAccount;
        }

        public String getRepaymentDate() {
            return repaymentDate;
        }

        public void setRepaymentDate(String repaymentDate) {
            this.repaymentDate = repaymentDate;
        }

        public String getWaitInterest() {
            return waitInterest;
        }

        public void setWaitInterest(String waitInterest) {
            this.waitInterest = waitInterest;
        }
    }
}

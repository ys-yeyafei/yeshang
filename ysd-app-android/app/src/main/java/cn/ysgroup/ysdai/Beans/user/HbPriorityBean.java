package cn.ysgroup.ysdai.Beans.user;

import java.util.List;

/**
 * Created by linyujie on 16/11/12.
 */

public class HbPriorityBean {

    /**
     * rcd : R0001
     * rmg : 成功
     * userHongbaoList : [{"id":662561,"createDate":1478854496000,"modifyDate":1478929477000,"userId":323,"hbNo":"2016111107253","name":"测试","money":10,"usedMoney":null,"status":0,"startTime":1478929477000,"endTime":1486742399000,"source":5,"sourceString":null,"sourceUserId":null,"sourceBorrowId":null,"usedBorrowId":null,"expDate":90,"limitStart":1,"limitEnd":2,"isPc":1,"isApp":1,"isHfive":1,"investFullMomey":100,"isLooked":null,"on":true,"overDays":0},{"id":662572,"createDate":1478930662000,"modifyDate":1478930667000,"userId":323,"hbNo":"2016111243353","name":"测试","money":100,"usedMoney":null,"status":0,"startTime":1478930667000,"endTime":1481558399000,"source":5,"sourceString":null,"sourceUserId":null,"sourceBorrowId":null,"usedBorrowId":null,"expDate":30,"limitStart":30,"limitEnd":31,"isPc":1,"isApp":1,"isHfive":1,"investFullMomey":1000,"isLooked":null,"on":true,"overDays":0},{"id":662571,"createDate":1478927189000,"modifyDate":1478930488000,"userId":323,"hbNo":"2016111289297","name":"测试","money":100,"usedMoney":null,"status":0,"startTime":1478930488000,"endTime":1481558399000,"source":5,"sourceString":null,"sourceUserId":null,"sourceBorrowId":null,"usedBorrowId":null,"expDate":30,"limitStart":30,"limitEnd":31,"isPc":1,"isApp":1,"isHfive":1,"investFullMomey":1000,"isLooked":null,"on":false,"overDays":0},{"id":662573,"createDate":1478930785000,"modifyDate":1478930792000,"userId":323,"hbNo":"2016111280804","name":"测试","money":50,"usedMoney":null,"status":0,"startTime":1478930792000,"endTime":1479830399000,"source":5,"sourceString":null,"sourceUserId":null,"sourceBorrowId":null,"usedBorrowId":null,"expDate":10,"limitStart":10,"limitEnd":30,"isPc":1,"isApp":1,"isHfive":1,"investFullMomey":10000,"isLooked":null,"on":false,"overDays":0}]
     * bestNum : 2
     * bestHbMoney : 110.00
     */

    private String rcd;
    private String rmg;
    private String bestNum;
    private String bestHbMoney;
    /**
     * id : 662561
     * createDate : 1478854496000
     * modifyDate : 1478929477000
     * userId : 323
     * hbNo : 2016111107253
     * name : 测试
     * money : 10.0
     * usedMoney : null
     * status : 0
     * startTime : 1478929477000
     * endTime : 1486742399000
     * source : 5
     * sourceString : null
     * sourceUserId : null
     * sourceBorrowId : null
     * usedBorrowId : null
     * expDate : 90
     * limitStart : 1
     * limitEnd : 2
     * isPc : 1
     * isApp : 1
     * isHfive : 1
     * investFullMomey : 100
     * isLooked : null
     * on : true
     * overDays : 0
     */

    private List<UserHongbaoListBean> userHongbaoList;

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

    public String getBestNum() {
        return bestNum;
    }

    public void setBestNum(String bestNum) {
        this.bestNum = bestNum;
    }

    public String getBestHbMoney() {
        return bestHbMoney;
    }

    public void setBestHbMoney(String bestHbMoney) {
        this.bestHbMoney = bestHbMoney;
    }

    public List<UserHongbaoListBean> getUserHongbaoList() {
        return userHongbaoList;
    }

    public void setUserHongbaoList(List<UserHongbaoListBean> userHongbaoList) {
        this.userHongbaoList = userHongbaoList;
    }

    public static class UserHongbaoListBean {
        private int id;
        private long createDate;
        private long modifyDate;
        private int userId;
        private String hbNo;
        private String name;
        private double money;
        private Object usedMoney;
        private int status;
        private long startTime;
        private long endTime;
        private int source;
        private Object sourceString;
        private Object sourceUserId;
        private Object sourceBorrowId;
        private Object usedBorrowId;
        private int expDate;
        private int limitStart;
        private int limitEnd;
        private int isPc;
        private int isApp;
        private int isHfive;
        private int investFullMomey;
        private Object isLooked;
        private boolean on;
        private int overDays;
        public boolean isright=false;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public long getModifyDate() {
            return modifyDate;
        }

        public void setModifyDate(long modifyDate) {
            this.modifyDate = modifyDate;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getHbNo() {
            return hbNo;
        }

        public void setHbNo(String hbNo) {
            this.hbNo = hbNo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public Object getUsedMoney() {
            return usedMoney;
        }

        public void setUsedMoney(Object usedMoney) {
            this.usedMoney = usedMoney;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public int getSource() {
            return source;
        }

        public void setSource(int source) {
            this.source = source;
        }

        public Object getSourceString() {
            return sourceString;
        }

        public void setSourceString(Object sourceString) {
            this.sourceString = sourceString;
        }

        public Object getSourceUserId() {
            return sourceUserId;
        }

        public void setSourceUserId(Object sourceUserId) {
            this.sourceUserId = sourceUserId;
        }

        public Object getSourceBorrowId() {
            return sourceBorrowId;
        }

        public void setSourceBorrowId(Object sourceBorrowId) {
            this.sourceBorrowId = sourceBorrowId;
        }

        public Object getUsedBorrowId() {
            return usedBorrowId;
        }

        public void setUsedBorrowId(Object usedBorrowId) {
            this.usedBorrowId = usedBorrowId;
        }

        public int getExpDate() {
            return expDate;
        }

        public void setExpDate(int expDate) {
            this.expDate = expDate;
        }

        public int getLimitStart() {
            return limitStart;
        }

        public void setLimitStart(int limitStart) {
            this.limitStart = limitStart;
        }

        public int getLimitEnd() {
            return limitEnd;
        }

        public void setLimitEnd(int limitEnd) {
            this.limitEnd = limitEnd;
        }

        public int getIsPc() {
            return isPc;
        }

        public void setIsPc(int isPc) {
            this.isPc = isPc;
        }

        public int getIsApp() {
            return isApp;
        }

        public void setIsApp(int isApp) {
            this.isApp = isApp;
        }

        public int getIsHfive() {
            return isHfive;
        }

        public void setIsHfive(int isHfive) {
            this.isHfive = isHfive;
        }

        public int getInvestFullMomey() {
            return investFullMomey;
        }

        public void setInvestFullMomey(int investFullMomey) {
            this.investFullMomey = investFullMomey;
        }

        public Object getIsLooked() {
            return isLooked;
        }

        public void setIsLooked(Object isLooked) {
            this.isLooked = isLooked;
        }

        public boolean isOn() {
            return on;
        }

        public void setOn(boolean on) {
            this.on = on;
        }

        public int getOverDays() {
            return overDays;
        }

        public void setOverDays(int overDays) {
            this.overDays = overDays;
        }
    }
}

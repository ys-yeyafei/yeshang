package cn.ysgroup.ysdai.Beans.user;

import cn.ysgroup.ysdai.Beans.BaseBean;

/**
 * Created by Administrator on 2015/11/9.
 */
public class UserRechargeBean  extends BaseBean {

    private String ableMoney;//会员可用余额
    private String feeType;//0：按比例；1：按每笔
    private String award;//充值奖励 比例/每笔
    private String fee;//手续费比例 /每笔
    private String realName;//持卡人姓名
    private String cardId;//持卡人身份证
    private String userId;//用户ID
    private String registerTime;//注册时间

    private String cardNo;//卡号
    private Integer status;//签约状态
    private String bankName;//签约状态
    private String bankId;//签约状态
    private String phone;//签约状态
    private String bankLimit;

    public void setBankLimit(String bankLimit){
        this.bankLimit=bankLimit;
    }

    public String getBankLimit(){
        return bankLimit;
    }


    public UserRechargeBean() {
        setRcd("R0001");
    }


    public String getAbleMoney() {
        return ableMoney;
    }


    public void setAbleMoney(String ableMoney) {
        this.ableMoney = ableMoney;
    }


    public String getAward() {
        return award;
    }


    public void setAward(String award) {
        this.award = award;
    }


    public String getFee() {
        return fee;
    }


    public void setFee(String fee) {
        this.fee = fee;
    }


    public String getCardId() {
        return cardId;
    }


    public void setCardId(String cardId) {
        this.cardId = cardId;
    }


    public String getRealName() {
        return realName;
    }


    public void setRealName(String realName) {
        this.realName = realName;
    }


    public String getFeeType() {
        return feeType;
    }


    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }


    public String getUserId() {
        return userId;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getRegisterTime() {
        return registerTime;
    }


    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }


    public String getCardNo() {
        return cardNo;
    }


    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }


    public Integer getStatus() {
        return status;
    }


    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getBankName()
    {
        return bankName;
    }

    public void setBankName(String bankName)
    {
        this.bankName = bankName;
    }

    public String getBnakId()
    {
        return bankId;
    }

    public void setBnakId(String bnakId)
    {
        this.bankId = bnakId;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }
}

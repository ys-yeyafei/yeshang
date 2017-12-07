package cn.ysgroup.ysdai.Beans.userInfo;

import java.io.Serializable;

/**
 * Created by linyujie on 17/1/13.
 */

public class PrincipalBeen implements Serializable{


    /**
     * total : 399.2
     * hbNum : 10
     * unableMoneyTx : 299.2
     * investorCollectionCapital : 0.0
     * unableMoneyTz : 0.0
     * zjjlNum : 0
     * unableMoney : 299.2
     * jljlNum : 3
     * ableMoney : 100.0
     * investorCollectionInterest : 0.0
     * investorTotal : 0.0
     * username : 13758294605
     * rcd : R0001
     * rmg :
     * tzNum : 0
     */

    private double total;
    private String hbNum;
    private double unableMoneyTx;
    private double investorCollectionCapital;
    private double unableMoneyTz;
    private String zjjlNum;
    private double unableMoney;
    private String jljlNum;
    private double ableMoney;
    private double investorCollectionInterest;
    private double investorTotal;
    private String username;
    private String rcd;
    private String rmg;
    private String tzNum;
    private String dhkjlNum;

    public void setDhkjlNum(String dhkjlNum){
        this.dhkjlNum=dhkjlNum;
    }

    public String getDhkjlNum(){
        return dhkjlNum;
    }
    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getHbNum() {
        return hbNum;
    }

    public void setHbNum(String hbNum) {
        this.hbNum = hbNum;
    }

    public double getUnableMoneyTx() {
        return unableMoneyTx;
    }

    public void setUnableMoneyTx(double unableMoneyTx) {
        this.unableMoneyTx = unableMoneyTx;
    }

    public double getInvestorCollectionCapital() {
        return investorCollectionCapital;
    }

    public void setInvestorCollectionCapital(double investorCollectionCapital) {
        this.investorCollectionCapital = investorCollectionCapital;
    }

    public double getUnableMoneyTz() {
        return unableMoneyTz;
    }

    public void setUnableMoneyTz(double unableMoneyTz) {
        this.unableMoneyTz = unableMoneyTz;
    }

    public String getZjjlNum() {
        return zjjlNum;
    }

    public void setZjjlNum(String zjjlNum) {
        this.zjjlNum = zjjlNum;
    }

    public double getUnableMoney() {
        return unableMoney;
    }

    public void setUnableMoney(double unableMoney) {
        this.unableMoney = unableMoney;
    }

    public String getJljlNum() {
        return jljlNum;
    }

    public void setJljlNum(String jljlNum) {
        this.jljlNum = jljlNum;
    }

    public double getAbleMoney() {
        return ableMoney;
    }

    public void setAbleMoney(double ableMoney) {
        this.ableMoney = ableMoney;
    }

    public double getInvestorCollectionInterest() {
        return investorCollectionInterest;
    }

    public void setInvestorCollectionInterest(double investorCollectionInterest) {
        this.investorCollectionInterest = investorCollectionInterest;
    }

    public double getInvestorTotal() {
        return investorTotal;
    }

    public void setInvestorTotal(double investorTotal) {
        this.investorTotal = investorTotal;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getTzNum() {
        return tzNum;
    }

    public void setTzNum(String tzNum) {
        this.tzNum = tzNum;
    }
}

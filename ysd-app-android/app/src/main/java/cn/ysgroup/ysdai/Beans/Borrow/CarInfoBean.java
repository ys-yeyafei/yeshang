package cn.ysgroup.ysdai.Beans.Borrow;

import cn.ysgroup.ysdai.Beans.BaseBean;

import java.math.BigDecimal;


public class CarInfoBean extends BaseBean
{
	
	private static final long serialVersionUID = -7683603982464447706L;

	public CarInfoBean() {
		setRcd("R0001");
	}

	private Integer id;// 标ID
	private String borImage2; // 车辆照片
	private String borrowInfoJson;
//	private String carBrand;//车辆品牌
//	private String carSeries;//车系
//	private String carSpec;//车型
//	private String onCardTime;//上牌时间（YYYY年MM月）
//	private String certificationTime;//发证时间（YYYY年MM月）
//	private String roadHaul;//行驶里程
//	private String vin;//车辆识别号
//	private String engineNumber;//发动机号
//	private String use;//用途
//	private String modelsLink;//车型链接
//	private String idCard;//是否有身份证
//	private String household;//户口本
//	private String zhengXin;//征信报告
//	private String income;//收入证明
//	private String anCase;//在执行案件查询
//	private String cardDriving;// 车辆行驶证
//	private String gouZhi;//购置税凭证
//	private String guJia;//车辆评估报告
//	private String gcfp;//购车发票机
//	private String jdczs;//动车登记证书
	private int userFlg;
	private BigDecimal userAbleMoney;
//	public String getCarBrand() {
//		return carBrand;
//	}
//	public void setCarBrand(String carBrand) {
//		this.carBrand = carBrand;
//	}
//	public String getCarSeries() {
//		return carSeries;
//	}
//	public void setCarSeries(String carSeries) {
//		this.carSeries = carSeries;
//	}
//	public String getCarSpec() {
//		return carSpec;
//	}
//	public void setCarSpec(String carSpec) {
//		this.carSpec = carSpec;
//	}
//	public String getOnCardTime() {
//		return onCardTime;
//	}
//	public void setOnCardTime(String onCardTime) {
//		this.onCardTime = onCardTime;
//	}
//	public String getCertificationTime() {
//		return certificationTime;
//	}
//	public void setCertificationTime(String certificationTime) {
//		this.certificationTime = certificationTime;
//	}
//	public String getRoadHaul() {
//		return roadHaul;
//	}
//	public void setRoadHaul(String roadHaul) {
//		this.roadHaul = roadHaul;
//	}
//	public String getVin() {
//		return vin;
//	}
//	public void setVin(String vin) {
//		this.vin = vin;
//	}
//	public String getEngineNumber() {
//		return engineNumber;
//	}
//	public void setEngineNumber(String engineNumber) {
//		this.engineNumber = engineNumber;
//	}
//	public String getUse() {
//		return use;
//	}
//	public void setUse(String use) {
//		this.use = use;
//	}
//	public String getModelsLink() {
//		return modelsLink;
//	}
//	public void setModelsLink(String modelsLink) {
//		this.modelsLink = modelsLink;
//	}
//	public String getIdCard() {
//		return idCard;
//	}
//	public void setIdCard(String idCard) {
//		this.idCard = idCard;
//	}
//	public String getHousehold() {
//		return household;
//	}
//	public void setHousehold(String household) {
//		this.household = household;
//	}
//	public String getZhengXin() {
//		return zhengXin;
//	}
//	public void setZhengXin(String zhengXin) {
//		this.zhengXin = zhengXin;
//	}
//	public String getIncome() {
//		return income;
//	}
//	public void setIncome(String income) {
//		this.income = income;
//	}
//	public String getAnCase() {
//		return anCase;
//	}
//	public void setAnCase(String anCase) {
//		this.anCase = anCase;
//	}
//	public String getCardDriving() {
//		return cardDriving;
//	}
//	public void setCardDriving(String cardDriving) {
//		this.cardDriving = cardDriving;
//	}
//	public String getGouZhi() {
//		return gouZhi;
//	}
//	public void setGouZhi(String gouZhi) {
//		this.gouZhi = gouZhi;
//	}
//	public String getGuJia() {
//		return guJia;
//	}
//	public void setGuJia(String guJia) {
//		this.guJia = guJia;
//	}
//	public String getGcfp() {
//		return gcfp;
//	}
//	public void setGcfp(String gcfp) {
//		this.gcfp = gcfp;
//	}
//	public String getJdczs() {
//		return jdczs;
//	}
//	public void setJdczs(String jdczs) {
//		this.jdczs = jdczs;
//	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBorImage2() {
		return borImage2;
	}
	public void setBorImage2(String borImage2) {
		this.borImage2 = borImage2;
	}
	public int getUserFlg() {
		return userFlg;
	}
	public void setUserFlg(int userFlg) {
		this.userFlg = userFlg;
	}
	public BigDecimal getUserAbleMoney() {
		return userAbleMoney;
	}
	public void setUserAbleMoney(BigDecimal userAbleMoney) {
		this.userAbleMoney = userAbleMoney;
	}
	public String getBorrowInfoJson() {
		return borrowInfoJson;
	}
	public void setBorrowInfoJson(String borrowInfoJson) {
		this.borrowInfoJson = borrowInfoJson;
	}
	
	
}

package com.example.fly;

public class FlightInfo {
	private int imageId; // ListView中共显示11个参数
	private String flight_num;
	private String go;
	private String jiantou;
	private String get; // int image类型下次再编写

	private String date;
	private String depart_time;
	private String fly_time;
	private String land_time;
	private String depart_airport;
	private String land_airport;

	private int toudeng_piao; // 头等舱 经济舱信息
	private int jingji_piao;
	private int toudeng_jia;
	private int jingji_jia;

	private String account_name;
	private String passenger; // 订单中的部分信息
	private String sex;
	private String birthday;
	private String nation;
	private String idcard;
	private String baoxian;
	private String clas;
	private String total_price;
	private String order_num;

	private String jp;
	private String ycp;
	private String jiantou2;
	private String refund;

	private String ddh;

	private String condition;

	public FlightInfo() {

	}

	// 最近路线查询
	public FlightInfo(String go, String get) {
		this.go = go;
		this.get = get;
	}

	// 对应列表中显示的航班
	public FlightInfo(int imageId, String flight_num, String go, String jiantou, String get, String date,
			String depart_time, String fly_time, String land_time, String depart_airport, String land_airport,
			String condition) {
		this.imageId = imageId;
		this.flight_num = flight_num;
		this.go = go;
		this.jiantou = jiantou;
		this.get = get;
		this.date = date;
		this.depart_time = depart_time;
		this.fly_time = fly_time;
		this.land_time = land_time;
		this.depart_airport = depart_airport;
		this.land_airport = land_airport;
		this.condition = condition;
	}

	// 对应点击进入后显示的具体航班信息
	public FlightInfo(int imageId, String flight_num, String go, String jiantou, String get, String date,
			String depart_time, String fly_time, String land_time, String depart_airport, String land_airport,
			int toudeng_piao, int jingji_piao, int toudeng_jia, int jingji_jia) {
		this.imageId = imageId;
		this.flight_num = flight_num;
		this.go = go;
		this.jiantou = jiantou;
		this.get = get;
		this.date = date;
		this.depart_time = depart_time;
		this.fly_time = fly_time;
		this.land_time = land_time;
		this.depart_airport = depart_airport;
		this.land_airport = land_airport;
		this.toudeng_piao = toudeng_piao;
		this.jingji_piao = jingji_piao;
		this.toudeng_jia = toudeng_jia;
		this.jingji_jia = jingji_jia;
	}

	// 对应机票订单表中的信息。18个参数，17个String，一个Int。
	public FlightInfo(String order_num, String account_name, String passenger, String sex, String birthday,
			String nation, String idcard, String baoxian, String clas, String total_price, String date, String go,
			String get, String fly_time, String depart_airport, String land_airport, String flight_num) {
		this.order_num = order_num;
		this.account_name = account_name;
		this.passenger = passenger;
		this.sex = sex;
		this.birthday = birthday;
		this.nation = nation;
		this.idcard = idcard;
		this.baoxian = baoxian;
		this.clas = clas;
		this.total_price = total_price;
		this.date = date;
		this.go = go;
		this.get = get;
		this.fly_time = fly_time;
		this.depart_airport = depart_airport;
		this.land_airport = land_airport;
		this.flight_num = flight_num;
	}

	// 对于订单列表中的机票简易信息 14个参数
	public FlightInfo(String order_num, int imageId, String jp, String ycp, String go, String jiantou, String get,
			String total_price, String date, String depart_time, String jiantou2, String land_time, String flight_num,
			String refund) {
		this.order_num = order_num;
		this.imageId = imageId;
		this.jp = jp;
		this.ycp = ycp;
		this.go = go;
		this.jiantou = jiantou;
		this.get = get;
		this.total_price = total_price;
		this.date = date;
		this.depart_time = depart_time;
		this.jiantou2 = jiantou2;
		this.land_time = land_time;
		this.flight_num = flight_num;
		this.refund = refund;
	}

	// 对应管理员操作的退票请求界面的简单列表信息
	public FlightInfo(String ddh, String order_num, String go, String jiantou, String get, String date,
			String flight_num) {
		this.order_num = order_num;
		this.go = go;
		this.jiantou = jiantou;
		this.get = get;
		this.date = date;
		this.ddh = ddh;
		this.flight_num = flight_num;
	}

	// public String getDdh() {
	// return ddh;
	// }
	//
	// public void setDdh(String ddh) {
	// this.ddh = ddh;
	// }

	public String getRefund() {
		return refund;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public void setRefund(String refund) {
		this.refund = refund;
	}

	public String getOrder_num() {
		return order_num;
	}

	public void setOrder_num(String order_num) {
		this.order_num = order_num;
	}

	public String getTotal_price() {
		return total_price;
	}

	public void setTotal_price(String total_price) {
		this.total_price = total_price;
	}

	public String getJp() {
		return jp;
	}

	public void setJp(String jp) {
		this.jp = jp;
	}

	public String getYcp() {
		return ycp;
	}

	public void setYcp(String ycp) {
		this.ycp = ycp;
	}

	public String getJiantou2() {
		return jiantou2;
	}

	public void setJiantou2(String jiantou2) {
		this.jiantou2 = jiantou2;
	}

	public int getToudeng_piao() {
		return toudeng_piao;
	}

	public void setToudeng_piao(int toudeng_piao) {
		this.toudeng_piao = toudeng_piao;
	}

	public int getJingji_piao() {
		return jingji_piao;
	}

	public void setJingji_piao(int jingji_piao) {
		this.jingji_piao = jingji_piao;
	}

	public int getToudeng_jia() {
		return toudeng_jia;
	}

	public void setToudeng_jia(int toudeng_jia) {
		this.toudeng_jia = toudeng_jia;
	}

	public int getJingji_jia() {
		return jingji_jia;
	}

	public void setJingji_jia(int jingji_jia) {
		this.jingji_jia = jingji_jia;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public String getFlight_num() {
		return flight_num;
	}

	public void setFlight_num(String flight_num) {
		this.flight_num = flight_num;
	}

	public String getGo() {
		return go;
	}

	public void setGo(String go) {
		this.go = go;
	}

	public String getJiantou() {
		return jiantou;
	}

	public void setJiantou(String jiantou) {
		this.jiantou = jiantou;
	}

	public String getGet() {
		return get;
	}

	public void setGet(String get) {
		this.get = get;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDepart_time() {
		return depart_time;
	}

	public void setDepart_time(String depart_time) {
		this.depart_time = depart_time;
	}

	public String getFly_time() {
		return fly_time;
	}

	public void setFly_time(String fly_time) {
		this.fly_time = fly_time;
	}

	public String getLand_time() {
		return land_time;
	}

	public void setLand_time(String land_time) {
		this.land_time = land_time;
	}

	public String getDepart_airport() {
		return depart_airport;
	}

	public void setDepart_airport(String depart_airport) {
		this.depart_airport = depart_airport;
	}

	public String getLand_airport() {
		return land_airport;
	}

	public void setLand_airport(String land_airport) {
		this.land_airport = land_airport;
	}
}

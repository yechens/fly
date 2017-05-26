package pc;

public class FlightInfo {
	//android��ѯ���߽���    һ�������ݿ��ȡ9������
	private String flight_num;
	private String go;
	private String get;		//int image�����´��ٱ�д
	
	private String date;
	private String depart_time;
	private String fly_time;
	private String land_time;
	private String depart_airport;
	private String land_airport;
	
	private int toudeng_piao;	//ͷ�Ȳ� ���ò���Ϣ
	private int jingji_piao;
	private int toudeng_jia;
	private int jingji_jia;
	
	private int total_price;	//�����е���Ϣ
	private String clas;
	private String passenger;
	private String passport;
	private String nation;
	private String baoxian;
	private String order_num;	
	private String refund;
	
	private String account_name;
	
	private String condition;
	
	public static String _tableName2 = "account";	//���ݿ��еı���
	public static String _tableName = "flights";	//���ݿ��еı���
	public static String _tableName3 = "`order`";	//���ݿ��еı���
	public static String _tableName4 = "refund";	//���ݿ��еı���
	
	public FlightInfo(){		
	}
	
	public FlightInfo(String flight_num,String go,String get,
			String date,String depart_time,String fly_time,
			String land_time,String depart_airport,String land_airport,String condition){
		this.flight_num = flight_num;
		this.go = go;
		this.get = get;
		this.date = date;
		this.depart_time = depart_time;
		this.fly_time = fly_time;
		this.land_time = land_time;
		this.depart_airport = depart_airport;
		this.land_airport = land_airport;
		this.condition = condition;
	}
	
	public FlightInfo(String flight_num,String go,String get,
			String date,String depart_time,String fly_time,
			String land_time,String depart_airport,String land_airport,
			int toudeng_piao,int toudeng_jia,int jingji_piao,int jingji_jia){
		this.flight_num = flight_num;
		this.go = go;
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
	
	//���ڶ����б��еĻ�Ʊ������Ϣ	13������
	public FlightInfo(int imageId, String jp, String ycp, String go, String jiantou, String get,
			int total_price, String date, String depart_time, String jiantou2, 
			String land_time, String flight_num){
		this.go = go;
		this.get = get;
		this.total_price = total_price;
		this.date = date;
		this.depart_time = depart_time;
		this.land_time = land_time;
		this.flight_num = flight_num;
		this.refund = refund;
	}
	
	//���ڶ�����Ʊ��ϸ��Ϣ	15������
	public FlightInfo( String order_num, String date, String go, String get,
			String depart_time, String fly_time, String land_time,
			String depart_airport, String land_airport, String clas, String flight_num,
			String passenger, String passport, String nation, int total_price, String baoxian){
		this.order_num = order_num;
		this.depart_airport = depart_airport;
		this.land_airport = land_airport;
		this.go = go;
		this.get = get;
		this.total_price = total_price;
		this.date = date;
		this.depart_time = depart_time;
		this.fly_time = fly_time;
		this.land_time = land_time;
		this.clas = clas;
		this.flight_num = flight_num;
		this.passenger = passenger;
		this.passport = passport;
		this.nation = nation;
		this.baoxian = baoxian;
	}
	
	//��Ӧ��Ʊ������5��������Ϣ
	public FlightInfo( String order_num, String go, String get,
			String date, String flight_num ){
		this.order_num = order_num;
		this.go = go;
		this.get = get;
		this.date = date;
		this.flight_num = flight_num;
	}
	
	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	public String getRefund() {
		return refund;
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

	public String getClas() {
		return clas;
	}

	public void setClas(String clas) {
		this.clas = clas;
	}

	public String getPassenger() {
		return passenger;
	}

	public void setPassenger(String passenger) {
		this.passenger = passenger;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getBaoxian() {
		return baoxian;
	}

	public void setBaoxian(String baoxian) {
		this.baoxian = baoxian;
	}
	
	public int getTotal_price() {
		return total_price;
	}

	public void setTotal_price(int total_price) {
		this.total_price = total_price;
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

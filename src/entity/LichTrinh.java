package entity;

public class LichTrinh {
	private String maLichTrinh;
	private String tenLichTrinh;
	private Ga maGaDi;
	private Ga maGaDen;
	
	public LichTrinh() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public LichTrinh(String maLichTrinh, String tenLichTrinh, Ga maGaDi, Ga maGaDen) throws Exception {
		super();
	setMaGaDen(maGaDen);
	setMaGaDi(maGaDi);
	setTenLichTrinh(tenLichTrinh);
	setMaLichTrinh(maLichTrinh);
	}
	
	public String getMaLichTrinh() {
		return maLichTrinh;
	}
	public void setMaLichTrinh(String maLichTrinh) throws Exception {
		if(maLichTrinh == null)
			throw new Exception("Mã lịch trình không được rỗng");
		this.maLichTrinh = maLichTrinh;
	}
	public String getTenLichTrinh() {
		return tenLichTrinh;
	}
	public void setTenLichTrinh(String tenLichTrinh) throws Exception {
		if(tenLichTrinh == null)
			throw new Exception("Mã lịch trình không được rỗng");
		this.tenLichTrinh = tenLichTrinh;
	}
	public Ga getMaGaDi() {
		return maGaDi;
	}
	public void setMaGaDi(Ga maGaDi) throws Exception {
		if(maGaDi == null)
			throw new Exception("Mã lịch trình không được rỗng");
		this.maGaDi = maGaDi;
	}
	public Ga getMaGaDen() {
		return maGaDen;
	}
	public void setMaGaDen(Ga maGaDen) throws Exception {
		if(maGaDen == null)
			throw new Exception("Mã lịch trình không được rỗng");
		this.maGaDen = maGaDen;
	}
	@Override
	public String toString() {
	    return "LichTrinh [maLichTrinh=" + maLichTrinh 
	            + ", tenLichTrinh=" + tenLichTrinh
	            + ", maGaDi=" + (maGaDi != null ? maGaDi.toString() : "null")
	            + ", maGaDen=" + (maGaDen != null ? maGaDen.toString() : "null")
	            + "]";
	}
	
}

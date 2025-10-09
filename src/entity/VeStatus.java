package entity;

public enum VeStatus {
	KHA_DUNG ("Khả Dụng"),
	DA_HET_HAN ("Đã hết hạn"),
	DA_HUY ("Đã hủy"),
	DA_SU_DUNG ("Đã sử dụng");

	private String moTa;
	
	VeStatus(String moTa) {
		// TODO Auto-generated constructor stub
		this.moTa = moTa;
	}

	public String getMoTa() {
		return moTa;
	}
	
}

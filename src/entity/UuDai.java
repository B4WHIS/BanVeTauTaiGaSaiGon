package entity;

import java.math.BigDecimal;

public class UuDai {
	private String maUuDai;
	private String loaiUuDai; //Người già, sinh viên, trẻ em
	private BigDecimal mucGiamGia; 
	private String dieuKienApDung;
	
	public UuDai(String maUuDai, String loaiUuDai, BigDecimal mucGiamGia, String dieuKienApDung) {
		super();
		setMaUuDai(maUuDai);
		setLoaiUuDai(loaiUuDai);
		setMucGiamGia(mucGiamGia);
		setDieuKienApDung(dieuKienApDung);
	}
	public UuDai() {}
	
	
	public String getMaUuDai() {
		return maUuDai;
	}
	public void setMaUuDai(String maUuDai) {
		if(maUuDai == null || maUuDai.trim().isEmpty()) {
			throw new IllegalArgumentException("Mã ưu đãi không được rỗng");
		}
		//Ràng buộc UD-XX viết sau
		this.maUuDai = maUuDai;
	}
	public String getLoaiUuDai() {
		return loaiUuDai;
	}
	public void setLoaiUuDai(String loaiUuDai) {
		if (loaiUuDai == null || !loaiUuDai.matches(
				"^(Người cao tuổi|Sinh viên|Trẻ em|Người lớn)$")){
			throw new IllegalArgumentException("Loại ưu đãi không hợp lệ");
		}
		this.loaiUuDai = loaiUuDai;
	}
	public BigDecimal getMucGiamGia() {
		return mucGiamGia;
	}
	public void setMucGiamGia(BigDecimal mucGiamGia) {
		if(mucGiamGia == null || mucGiamGia.compareTo(BigDecimal.ZERO) < 0 || mucGiamGia.compareTo(new BigDecimal(100)) > 0) {
			throw new IllegalArgumentException("Mức giảm giá phải từ 0 đến 100");
		}
		this.mucGiamGia = mucGiamGia;
	}
	public String getDieuKienApDung() {
		return dieuKienApDung;
	}
	public void setDieuKienApDung(String dieuKienApDung) {
		this.dieuKienApDung = dieuKienApDung;
	}

}

package entity;


public class ChoNgoi {
    private String maChoNgoi;
    private int IDloaiGhe;     
    private String trangThai; 
    private ToaTau toaTau;  

    public ChoNgoi() {}
    
    public ChoNgoi(String maChoNgoi, int iDloaiGhe, String trangThai, ToaTau toaTau) {
		super();
		setMaChoNgoi(maChoNgoi);
		setIDloaiGhe(iDloaiGhe);
		setTrangThai(trangThai);
		setToaTau(toaTau);
	}

	public ChoNgoi(String maChoNgoi) {
		// TODO Auto-generated constructor stub
		setMaChoNgoi(maChoNgoi);
	}

	public String getMaChoNgoi() {
		return maChoNgoi;
	}

	public void setMaChoNgoi(String maChoNgoi) {
		this.maChoNgoi = maChoNgoi;
	}

	public String getTrangThai() {
		return trangThai;
	}

    public void setTrangThai(String trangThai) {
        
        if (trangThai == null || (!trangThai.equalsIgnoreCase("Đã đặt") 
        		&& !trangThai.equalsIgnoreCase("Trống"))) {
            throw new IllegalArgumentException("Trạng thái phải là 'Trống' hoặc 'Đã đặt'.");
        }
        this.trangThai = trangThai;
    }

	public int getIDloaiGhe() {
        return IDloaiGhe;
    }
    
    public void setIDloaiGhe(int IDloaiGhe) {
        if (IDloaiGhe <= 0) {
            throw new IllegalArgumentException("ID loại ghế phải dương.");
        }
        this.IDloaiGhe = IDloaiGhe;
    }
    
	public ToaTau getToaTau() {
		return toaTau;
	}

	public void setToaTau(ToaTau toaTau) {
		if (toaTau == null) {
			throw new IllegalArgumentException("Toa Tàu không được null!");
		}
		this.toaTau = toaTau;
	}

	@Override
	public String toString() {
		return "ChoNgoi [maChoNgoi=" + maChoNgoi + ", IDloaiGhe=" + IDloaiGhe + ", trangThai=" + trangThai + ", toaTau="
				+ toaTau + "]";
	}


	
}
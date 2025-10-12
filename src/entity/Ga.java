package entity;

public class Ga {
    private Integer IDga;   
    private String maGa;    
    private String tenGa;
    private String diaChi;

    public Ga() {
    	
    }

    public Ga(Integer IDga, String maGa, String tenGa, String diaChi) throws IllegalAccessException {
        setIDga(IDga);
        setMaGa(maGa);
        setTenGa(tenGa);
        setDiaChi(diaChi);
    }

    public Integer getIDga() {
        return IDga;
    }

    
    public void setIDga(Integer IDga) throws IllegalAccessException {
        if (IDga == null) {
            throw new IllegalArgumentException("ID ga không được để trống");
        }
        else if(IDga < 1) {
        	throw new IllegalArgumentException("ID ga không được nhỏ hơn 1");
        }
        else if(IDga > 99) {
        	throw new IllegalAccessException("ID ga không được lớp hơn 99");
        }
        this.IDga = IDga;
    }

    
    public String getMaGa() {
        return maGa;
    }

    public void setMaGa(String maGa) {
        if (maGa == null || maGa.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã ga không được để trống");
        }
        if (maGa.trim().length() > 5) {
            throw new IllegalArgumentException("Mã ga không được vượt quá 5 ký tự");
        }
        this.maGa = maGa;
    }

    public String getTenGa() {
        return tenGa;
    }

    public void setTenGa(String tenGa) {
        if (tenGa == null || tenGa.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên ga không được để trống");
        }
        if (tenGa.length() > 100) {
            throw new IllegalArgumentException("Tên ga không được vượt quá 100 ký tự");
        }
        this.tenGa = tenGa;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        if (diaChi == null || diaChi.trim().isEmpty()) {
            throw new IllegalArgumentException("Địa chỉ ga không được để trống");
        }
        if (diaChi.length() > 200) {
            throw new IllegalArgumentException("Địa chỉ ga không được vượt quá 200 ký tự");
        }
        this.diaChi = diaChi;
    }

    @Override
    public String toString() {
        return "Ga [IDga=" + IDga + ", maGa=" + maGa + ", tenGa=" + tenGa + ", diaChi=" + diaChi + "]";
    }
}

package entity;

public class LichTrinh {

    private String maLichTrinh;   
    private Ga gaDi;              
    private Ga gaDen;             
    private String tenLichTrinh;  


    public LichTrinh() {
    }

    public LichTrinh(String maLichTrinh, Ga gaDi, Ga gaDen, String tenLichTrinh) {
        setMaLichTrinh(maLichTrinh);
        setGaDi(gaDi);
        setGaDen(gaDen);
        setTenLichTrinh(tenLichTrinh);
    }


    public String getMaLichTrinh() {
        return maLichTrinh;
    }

    public void setMaLichTrinh(String maLichTrinh) {
        if (maLichTrinh == null || maLichTrinh.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã lịch trình không được để trống.");
        }
        if (maLichTrinh.length() > 6) {
            throw new IllegalArgumentException("Mã lịch trình không được vượt quá 6 ký tự.");
        }
        this.maLichTrinh = maLichTrinh.trim();
    }

    public Ga getGaDi() {
        return gaDi;
    }

    public void setGaDi(Ga gaDi) {
        if (gaDi == null) {
            throw new IllegalArgumentException("Ga đi không được để trống.");
        }
        this.gaDi = gaDi;
    }

    public Ga getGaDen() {
        return gaDen;
    }

    public void setGaDen(Ga gaDen) {
        if (gaDen == null) {
            throw new IllegalArgumentException("Ga đến không được để trống.");
        }

        if (this.gaDi != null && gaDen.getMaGa().equals(this.gaDi.getMaGa())) {
            throw new IllegalArgumentException("Ga đến không được trùng với ga đi.");
        }
        this.gaDen = gaDen;
    }

    public String getTenLichTrinh() {
        return tenLichTrinh;
    }

    public void setTenLichTrinh(String tenLichTrinh) {
        if (tenLichTrinh == null || tenLichTrinh.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên lịch trình không được để trống.");
        }
        if (tenLichTrinh.length() > 100) {
            throw new IllegalArgumentException("Tên lịch trình không được vượt quá 100 ký tự.");
        }
        this.tenLichTrinh = tenLichTrinh.trim();
    }


    @Override
    public String toString() {
        return "LichTrinh [" +
                "maLichTrinh='" + maLichTrinh + '\'' +
                ", Ga đi='" + (gaDi != null ? gaDi.getTenGa() : "null") + '\'' +
                ", Ga đến='" + (gaDen != null ? gaDen.getTenGa() : "null") + '\'' +
                ", Tên lịch trình='" + tenLichTrinh + '\'' +
                ']';
    }
}

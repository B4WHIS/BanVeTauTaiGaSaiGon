package entity;

public class LoaiGiaoDich {
    private int IDloaiGiaoDich;
    private String tenGiaoDich;
    private String moTa;

    public LoaiGiaoDich() {}

    public LoaiGiaoDich(int IDloaiGiaoDich, String tenGiaoDich, String moTa) {
        this.IDloaiGiaoDich = IDloaiGiaoDich;
        setTenGiaoDich(tenGiaoDich);
        setMoTa(moTa);
    }

    public int getIDloaiGiaoDich() {
        return IDloaiGiaoDich;
    }

    public void setIDloaiGiaoDich(int IDloaiGiaoDich) {
        this.IDloaiGiaoDich = IDloaiGiaoDich;
    }

    public String getTenGiaoDich() {
        return tenGiaoDich;
    }

    public void setTenGiaoDich(String tenGiaoDich) {
        if (tenGiaoDich == null || tenGiaoDich.trim().isEmpty())
            throw new IllegalArgumentException("Tên giao dịch không được để trống.");
        this.tenGiaoDich = tenGiaoDich.trim();
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = (moTa == null) ? "" : moTa.trim();
    }

    @Override
    public String toString() {
        return "LoaiGiaoDich [IDloaiGiaoDich=" + IDloaiGiaoDich + 
               ", tenGiaoDich=" + tenGiaoDich + 
               ", moTa=" + moTa + "]";
    }
}

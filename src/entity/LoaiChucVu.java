package entity;

public class LoaiChucVu {
    private int IDloaiChucVu;
    private String tenChucVu;
    private String moTa;

    public LoaiChucVu() {}

    public LoaiChucVu(int IDloaiChucVu, String tenChucVu, String moTa) {
        this.IDloaiChucVu = IDloaiChucVu;
        setTenChucVu(tenChucVu);
        setMoTa(moTa);
    }

    public int getIDloaiChucVu() {
        return IDloaiChucVu;
    }

    public void setIDloaiChucVu(int IDloaiChucVu) {
        this.IDloaiChucVu = IDloaiChucVu;
    }

    public String getTenChucVu() {
        return tenChucVu;
    }

    public void setTenChucVu(String tenChucVu) {
        if (tenChucVu == null || tenChucVu.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên chức vụ không được để trống.");
        }
        this.tenChucVu = tenChucVu.trim();
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = (moTa == null) ? "" : moTa.trim();
    }

    @Override
    public String toString() {
        return "LoaiChucVu [IDloaiChucVu=" + IDloaiChucVu +
               ", tenChucVu=" + tenChucVu +
               ", moTa=" + moTa + "]";
    }
}

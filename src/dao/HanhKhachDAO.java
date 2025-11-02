package dao;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import connectDB.connectDB;
import entity.HanhKhach;

public class HanhKhachDAO {

    
	private HanhKhach layHanhKhachTuResultSet(ResultSet rs) throws SQLException {
	    String maHK = rs.getString("maHanhKhach");
	    String hoTen = rs.getString("hoTen");
	    String cmndCccd = rs.getString("cmndCccd");
	    String soDT = rs.getString("soDienThoai");
	    Date sqlNgaySinh = rs.getDate("ngaySinh");
	    LocalDate ngaySinh = sqlNgaySinh != null ? sqlNgaySinh.toLocalDate() : null;

	    // ĐẢM BẢO CÓ DÒNG NÀY
	    String maUuDai = rs.getString("maUuDai");
	    
	    System.out.println(maHK + " | maUuDai: " + maUuDai);

	    
	    if (maUuDai != null) {
	        maUuDai = maUuDai.trim();
	    }
	    // Nếu null → mặc định UD-01 (hoặc để null nếu bạn muốn)
	    if (maUuDai == null || maUuDai.isEmpty()) {
	        maUuDai = "UD-01";
	    }

	    String trangThai = rs.getString("TrangThai");
	    if (trangThai == null) {
	        trangThai = "Hoạt động";
	    }

	    HanhKhach hk = new HanhKhach(maHK, hoTen, cmndCccd, soDT, ngaySinh, maUuDai, trangThai);
	    hk.setTrangThai(trangThai);
	    return hk;
	}
    
    public boolean themHanhKhach(HanhKhach hk) throws SQLException {
        String sql = "INSERT INTO HanhKhach (hoTen, cmndCccd, soDienThoai, ngaySinh, maUuDai, TrangThai) VALUES (?, ?, ?, ?, ?, ?)";
        try(
            Connection con = connectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setString(1, hk.getHoTen());
            ps.setString(2, hk.getCmndCccd());
            ps.setString(3, hk.getSoDT());
            ps.setDate(4, Date.valueOf(hk.getNgaySinh()));
            ps.setString(5, hk.getMaUuDai());
            ps.setString(6, hk.getTrangThai()); 
            
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public List<HanhKhach> layDanhSachHanhKhachHoatDong() throws SQLException {
        List<HanhKhach> danhSachHK = new ArrayList<>();
        String sql = "SELECT * FROM HanhKhach WHERE TrangThai = N'Hoạt động'"; 
        try (
            Connection con = connectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()){
                danhSachHK.add(layHanhKhachTuResultSet(rs)); 
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return danhSachHK;
    }
    
    public boolean capNhatHanhKhach(HanhKhach hk) throws SQLException {
        String sql = "UPDATE HanhKhach SET hoTen = ?, cmndCccd = ?, soDienThoai = ?, ngaySinh = ?, maUuDai = ?, TrangThai = ? WHERE maHanhKhach = ?";
        try(
            Connection con = connectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, hk.getHoTen());
            ps.setString(2, hk.getCmndCccd());
            ps.setString(3, hk.getSoDT());
            ps.setDate(4, Date.valueOf(hk.getNgaySinh()));
            ps.setString(5, hk.getMaUuDai());
            ps.setString(6, hk.getTrangThai());
            ps.setString(7, hk.getMaKH());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public boolean xoaMemHanhKhach(String maKH) throws SQLException {
        String sql = "UPDATE HanhKhach SET TrangThai = N'Đã xóa' WHERE maHanhKhach = ?";
        try (
            Connection con = connectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, maKH);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Lỗi xóa mềm hành khách: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
    
    public boolean khoiPhucHanhKhach(String maKH) throws SQLException {
        String sql = "UPDATE HanhKhach SET TrangThai = N'Hoạt động' WHERE maHanhKhach = ?";
        try (
            Connection con = connectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, maKH);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    
    public HanhKhach layHanhKhachTheoCMND(String cmndCccd) throws SQLException {
        String sql = "SELECT * FROM HanhKhach WHERE cmndCccd = ? AND TrangThai = N'Hoạt động'";
        HanhKhach hk = null;
        try (
            Connection con = connectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cmndCccd);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    hk = layHanhKhachTuResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return hk;
    }
    
    public HanhKhach layHanhKhachTheoSDT(String soDT) throws SQLException {
        String sql = "SELECT * FROM HanhKhach WHERE soDienThoai = ? AND TrangThai = N'Hoạt động'";
        HanhKhach hk = null;
        try (
            Connection con = connectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, soDT);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    hk = layHanhKhachTuResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi tìm kiếm hành khách theo SĐT: " + ex.getMessage());
            throw ex;
        }
        return hk;
    }

    public List<HanhKhach> timHanhKhachTheoDieuKien(String ten, String cmnd, String sdt, LocalDate ngaySinh, String maUuDai) throws SQLException {
        List<HanhKhach> ds = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM HanhKhach WHERE TrangThai = N'Hoạt động'");

        if (!ten.isEmpty()) sql.append(" AND hoTen LIKE ?");
        if (!cmnd.isEmpty()) sql.append(" AND cmndCccd LIKE ?");
        if (!sdt.isEmpty()) sql.append(" AND soDienThoai LIKE ?");
        if (ngaySinh != null) sql.append(" AND ngaySinh = ?");  // Exact match cho ngày sinh, vì là date
        if (maUuDai != null && !maUuDai.isEmpty() && !"UD-01".equals(maUuDai)) {  // Bỏ qua nếu default
            sql.append(" AND maUuDai = ?");
        }

        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql.toString())) {

            int index = 1;
            if (!ten.isEmpty()) stmt.setString(index++, "%" + ten + "%");
            if (!cmnd.isEmpty()) stmt.setString(index++, "%" + cmnd + "%");
            if (!sdt.isEmpty()) stmt.setString(index++, "%" + sdt + "%");
            if (ngaySinh != null) stmt.setDate(index++, java.sql.Date.valueOf(ngaySinh));  // Chuyển LocalDate sang sql.Date
            if (maUuDai != null && !maUuDai.isEmpty() && !"UD-01".equals(maUuDai)) {
                stmt.setString(index++, maUuDai);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ds.add(layHanhKhachTuResultSet(rs));
                }
            }
        }
        return ds;
    }
    
    public HanhKhach layHanhKhachTheoMa(String maHK) throws SQLException {
        String sql = "SELECT maHanhKhach, hoTen, ngaySinh, soDienThoai, cmndCccd, maUuDai FROM HanhKhach WHERE maHanhKhach = ?";
        try (   Connection conn = connectDB.getConnection();
        		PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHK);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String ma = rs.getString("maHanhKhach");
                    String hoTen = rs.getString("hoTen");
                    LocalDate ngaySinh = rs.getDate("ngaySinh").toLocalDate();
                    String sdt = rs.getString("soDienThoai");
                    String cmnd = rs.getString("cmndCccd");
                    String maUuDai = rs.getString("maUuDai");
                    return new HanhKhach(ma, hoTen, ngaySinh, sdt, cmnd, maUuDai);
                }
            }
        }
        return null;
    }
    
    public List<HanhKhach> timKiemHanhKhach(String tuKhoa) throws SQLException {
        List<HanhKhach> ds = new ArrayList<>();
        String sql = "SELECT * FROM HanhKhach WHERE TrangThai = N'Hoạt động' AND (HoTen LIKE ? OR CMND_CCCD LIKE ?)"; // [1]
        
        Connection con = connectDB.getConnection();
        PreparedStatement ps = con.prepareStatement(sql); 

        ps.setString(1, "%" + tuKhoa + "%");
        ps.setString(2, "%" + tuKhoa + "%");

        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            HanhKhach hk = new HanhKhach(
                rs.getString("MaKH"), 
                rs.getString("HoTen"), 
                rs.getString("CMND_CCCD"), 
                rs.getString("SoDT"),
                rs.getDate("NgaySinh").toLocalDate(), 
                rs.getString("maUuDai"), 
                rs.getString("TrangThai") 
            );
            ds.add(hk); 
        }
        
        rs.close();
        ps.close(); 
        return ds;
    }
}
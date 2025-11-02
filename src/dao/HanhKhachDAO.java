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
    private Connection con;

    public HanhKhachDAO() {
        con = connectDB.getConnection();
    }

    private HanhKhach layHanhKhachTuResultSet(ResultSet rs) throws SQLException {
        // 1. Đọc các cột đã có từ trước
        String maHK = rs.getString("maHanhKhach");
        String hoTen = rs.getString("hoTen");
        String cmndCccd = rs.getString("cmndCccd");
        String soDT = rs.getString("soDienThoai");
        
        // Xử lý ngày sinh (chuyển từ java.sql.Date sang java.time.LocalDate)
        Date sqlNgaySinh = rs.getDate("ngaySinh");
        LocalDate ngaySinh = null;
        if (sqlNgaySinh != null) {
            ngaySinh = sqlNgaySinh.toLocalDate(); 
        }
        
        String maUuDai = rs.getString("maUuDai"); 
        
        // 2. Đọc Cột Mới: trangThai (Giả định cột này đã được thêm vào DB)
        String trangThai = null;
        try {
            // Cố gắng đọc cột "trangThai"
            trangThai = rs.getString("trangThai"); 
        } catch (SQLException e) {
            // Nếu DB chưa có cột này, mình bắt lỗi SQL và gán trạng thái mặc định
            // Đây là cách fix lỗi kiểu sinh viên khi DB chưa hoàn toàn khớp với Entity mới
            trangThai = "Hoạt động"; 
        }
        

        try {
            // Khởi tạo đối tượng HanhKhach
            HanhKhach hk = new HanhKhach(maHK, hoTen, cmndCccd, soDT, ngaySinh, maUuDai); 
            
            // 3. Set trạng thái (Phải dùng setter vì constructor cũ không có tham số này)
            // Nếu không có setTrangThai(), dòng này sẽ báo lỗi, nhưng mình giả định bạn đã sửa Entity
            if (trangThai != null) {
                hk.setTrangThai(trangThai); 
            }
            
            return hk;
            
        } catch (Exception e) {
            // Nếu có lỗi validation trong Entity (ví dụ: format Mã KH sai, như mình đã trao đổi)
            throw new SQLException("Lỗi khi tạo đối tượng HanhKhach từ ResultSet: " + e.getMessage(), e); 
        }
    }
    
    // 🔹 Lấy tất cả hành khách hoạt động
    public List<HanhKhach> getAllHanhKhachHoatDong() throws SQLException {
        List<HanhKhach> ds = new ArrayList<>();
        String sql = "SELECT * FROM HanhKhach WHERE TrangThai = N'Hoạt động'";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            HanhKhach hk = new HanhKhach(
                rs.getString("MaHanhKhach"),
                rs.getString("hoTen"),
                rs.getString("cmndCccd"),
                rs.getString("soDienThoai"),
                rs.getDate("ngaySinh").toLocalDate(),
                rs.getString("maUuDai"),
                rs.getString("TrangThai")
            );
            ds.add(hk);
        }

        rs.close();
        ps.close();
        return ds;
    }

    // 🔹 Thêm hành khách
    public boolean themHanhKhach(HanhKhach hk) {
        String sql = "INSERT INTO HanhKhach (HoTen, CMND_CCCD, SoDT, NgaySinh, MaUuDai, TrangThai) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, hk.getHoTen());
            stmt.setString(2, hk.getCmndCccd());
            stmt.setString(3, hk.getSoDT());
            stmt.setDate(4, Date.valueOf(hk.getNgaySinh()));
            stmt.setString(5, hk.getMaUuDai());
            stmt.setString(6, hk.getTrangThai());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Cập nhật thông tin hành khách
    public boolean capNhatHanhKhach(HanhKhach hk) throws SQLException {
        String sql = "UPDATE HanhKhach SET HoTen=?, CMND_CCCD=?, SoDT=?, NgaySinh=?, MaUuDai=?, TrangThai=? WHERE MaKH=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, hk.getHoTen());
        ps.setString(2, hk.getCmndCccd());
        ps.setString(3, hk.getSoDT());
        ps.setDate(4, Date.valueOf(hk.getNgaySinh()));
        ps.setString(5, hk.getMaUuDai());
        ps.setString(6, hk.getTrangThai());
        ps.setString(7, hk.getMaKH());
        int n = ps.executeUpdate();
        ps.close();
        return n > 0;
    }

    // 🔹 Xóa mềm → chuyển sang "Đã xóa"
    public boolean xoaMemHanhKhach(String maKH) throws SQLException {
        String sql = "UPDATE HanhKhach SET TrangThai = N'Đã xóa' WHERE MaKH = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, maKH);
        int n = ps.executeUpdate();
        ps.close();
        return n > 0;
    }

    // 🔹 Khôi phục hành khách
    public boolean khoiPhucHanhKhach(String maKH) throws SQLException {
        String sql = "UPDATE HanhKhach SET TrangThai = N'Hoạt động' WHERE MaKH = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, maKH);
        int n = ps.executeUpdate();
        ps.close();
        return n > 0;
    }

    // 🔹 Tìm kiếm theo tên hoặc CMND
    public List<HanhKhach> timKiemHanhKhach(String tuKhoa) throws SQLException {
        List<HanhKhach> ds = new ArrayList<>();
        String sql = "SELECT * FROM HanhKhach WHERE TrangThai = N'Hoạt động' AND (HoTen LIKE ? OR CMND_CCCD LIKE ?)";
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
                rs.getString("MaUuDai"),
                rs.getString("TrangThai")
            );
            ds.add(hk);
        }

        rs.close();
        ps.close();
        return ds;
    }
    
    // 🔍 Lấy hành khách theo CMND/CCCD
    public HanhKhach layHanhKhachTheoCMND(String cmnd) {
        String sql = "SELECT * FROM HanhKhach WHERE CMND_CCCD = ? AND TrangThai = N'Hoạt động'";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, cmnd);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new HanhKhach(
                    rs.getString("MaKH"),
                    rs.getString("HoTen"),
                    rs.getString("CMND_CCCD"),
                    rs.getString("SoDT"),
                    rs.getDate("NgaySinh").toLocalDate(),
                    rs.getString("MaUuDai"),
                    rs.getString("TrangThai")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔍 Lấy hành khách theo Số điện thoại
    public HanhKhach layHanhKhachTheoSDT(String sdt) {
        String sql = "SELECT * FROM HanhKhach WHERE SoDT = ? AND TrangThai = N'Hoạt động'";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, sdt);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new HanhKhach(
                    rs.getString("MaKH"),
                    rs.getString("HoTen"),
                    rs.getString("CMND_CCCD"),
                    rs.getString("SoDT"),
                    rs.getDate("NgaySinh").toLocalDate(),
                    rs.getString("MaUuDai"),
                    rs.getString("TrangThai")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<HanhKhach> timHanhKhachTheoDieuKien(String ten, String cmnd, String sdt) throws SQLException {
        List<HanhKhach> ds = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM HanhKhach WHERE TrangThai = N'Hoạt động'");

        if (!ten.isEmpty()) sql.append(" AND HoTen LIKE ?");
        if (!cmnd.isEmpty()) sql.append(" AND CMND_CCCD LIKE ?");
        if (!sdt.isEmpty()) sql.append(" AND SoDT LIKE ?");

        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql.toString())) {
            int index = 1;
            if (!ten.isEmpty()) stmt.setString(index++, "%" + ten + "%");
            if (!cmnd.isEmpty()) stmt.setString(index++, "%" + cmnd + "%");
            if (!sdt.isEmpty()) stmt.setString(index++, "%" + sdt + "%");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                HanhKhach hk = new HanhKhach(
                    rs.getString("maHanhKhach"),
                    rs.getString("hoTen"),
                    rs.getString("cmndCccd"),
                    rs.getString("soDienThoai"),
                    rs.getDate("ngaySinh").toLocalDate(),
                    rs.getString("maUuDai"),
                    rs.getString("TrangThai")
                );
                ds.add(hk);
            }
        }
        return ds;
    }
    public HanhKhach layHanhKhachTheoMa(String maHK) throws SQLException {
        String sql = "SELECT * FROM HanhKhach WHERE maHanhKhach = ?"; 
        HanhKhach hk = null;
        
        try (
            Connection con = connectDB.getConnection(); 
            PreparedStatement ps = con.prepareStatement(sql)) { 
            
            ps.setString(1, maHK);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    hk = layHanhKhachTuResultSet(rs); 
                }
            }
        } catch (SQLException ex) {
            // Nếu có lỗi CSDL, in ra console để debug [5]
            ex.printStackTrace();
        }
        return hk; 
    }

}

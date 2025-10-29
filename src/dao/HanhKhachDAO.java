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
        if (hoTen != null) {
            hoTen = hoTen.trim(); // Loại bỏ khoảng trắng thừa
        }
        String cmndCccd = rs.getString("cmndCccd");
        String soDT = rs.getString("soDienThoai");
        Date sqlNgaySinh = rs.getDate("ngaySinh");
        LocalDate ngaySinh = (sqlNgaySinh != null) ? sqlNgaySinh.toLocalDate() : null;
        String maUuDai = rs.getString("maUuDai");
        try {
            HanhKhach hk = new HanhKhach(maHK, hoTen, cmndCccd, soDT, ngaySinh, maUuDai);
            return hk;
        } catch (Exception e) {
            throw new SQLException("Lỗi khi tạo đối tượng HanhKhach từ ResultSet: " + e.getMessage(), e);
        }
    }
// public boolean themHanhKhach(HanhKhach hk) throws SQLException {
// String sql = "INSERT INTO HanhKhach (hoTen, cmndCccd, soDienThoai, ngaySinh, maUuDai) VALUES (?, ?, ?, ?, ?)";
//
// try (Connection conn = connectDB.getConnection();
// PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
// stmt.setString(1, hk.getHoTen());
// stmt.setString(2, hk.getCmndCccd());
// stmt.setString(3, hk.getSoDT());
// stmt.setObject(4, hk.getNgaySinh());
// stmt.setString(5, hk.getMaUuDai());
//
// int rowsAffected = stmt.executeUpdate();
// if (rowsAffected > 0) {
//
// return true;
// }
// return false;
// }
// }
   
//    public boolean themHanhKhach(HanhKhach hk) throws SQLException {
//        // 1. Chỉ chèn các cột cơ sở (KHÔNG chèn IDhk và maHanhKhach)
//        // Tên cột cần chèn: hoTen, cmndCccd, soDienThoai, ngaySinh, maUuDai
//        String sqlInsert = "INSERT INTO HanhKhach (hoTen, cmndCccd, soDienThoai, ngaySinh, maUuDai) VALUES (?, ?, ?, ?, ?)";
//       
//        // 2. Câu truy vấn để lấy maHanhKhach vừa được sinh ra (dựa vào cmndCccd)
//        String sqlSelect = "SELECT TOP 1 maHanhKhach FROM HanhKhach WHERE cmndCccd = ? ORDER BY IDhk DESC";
//        Connection con = null;
//        PreparedStatement psInsert = null;
//        PreparedStatement psSelect = null;
//        ResultSet rs = null;
//        try {
//            con = connectDB.getConnection();
//            // A. Thực thi INSERT
//            psInsert = con.prepareStatement(sqlInsert);
//            psInsert.setString(1, hk.getHoTen());
//            psInsert.setString(2, hk.getCmndCccd());
//            psInsert.setString(3, hk.getSoDT());
//            psInsert.setDate(4, hk.getNgaySinh() != null ? Date.valueOf(hk.getNgaySinh()) : null);
//            psInsert.setString(5, hk.getMaUuDai());
//           
//            int rowsAffected = psInsert.executeUpdate();
//            if (rowsAffected > 0) {
//                // B. SELECT mã HK vừa tạo (Lấy mã HK Persisted từ DB)
//                if (hk.getCmndCccd() != null && !hk.getCmndCccd().trim().isEmpty()) {
//                   
//                    // Đóng psInsert trước khi mở psSelect
//                    psInsert.close();
//                   
//                    psSelect = con.prepareStatement(sqlSelect);
//                    psSelect.setString(1, hk.getCmndCccd());
//                    rs = psSelect.executeQuery();
//                    if (rs.next()) {
//                        String generatedMaHK = rs.getString("maHanhKhach");
//                        hk.setMaKH(generatedMaHK); // Gán mã hợp lệ (HK-XXXXX) trở lại Entity
//                    }
//                }
//                return true;
//            }
//            return false;
//        } catch (SQLException ex) {
//            // Ném lại lỗi CSDL, QuanLyHoaDonControl sẽ bắt lỗi này
//            throw ex;
//        } finally {
//            // Đóng tài nguyên sử dụng try-catch hoặc try-with-resources nếu phù hợp
//            try {
//                if (rs != null) rs.close();
//                if (psSelect != null) psSelect.close();
//                if (psInsert != null) psInsert.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
    public boolean themHanhKhach(HanhKhach hk) throws SQLException {
        // *** CHỈ CHÈN CÁC CỘT KHÔNG PHẢI TỰ ĐỘNG SINH ***
        String sqlInsert = "INSERT INTO HanhKhach (hoTen, cmndCccd, soDienThoai, ngaySinh, maUuDai) VALUES (?, ?, ?, ?, ?)";
        
        // Truy vấn mã HK vừa được sinh
        String sqlSelect = "SELECT TOP 1 maHanhKhach FROM HanhKhach WHERE cmndCccd = ? ORDER BY IDhk DESC"; 

        Connection con = null;
        PreparedStatement psInsert = null;
        PreparedStatement psSelect = null;
        ResultSet rs = null;

        try {
            con = connectDB.getConnection();
            con.setAutoCommit(false); // Bắt đầu giao dịch

            // A. Thực thi INSERT
            psInsert = con.prepareStatement(sqlInsert);
            psInsert.setString(1, hk.getHoTen());
            psInsert.setString(2, hk.getCmndCccd());
            psInsert.setString(3, hk.getSoDT());
            psInsert.setDate(4, Date.valueOf(hk.getNgaySinh())); 
            psInsert.setString(5, hk.getMaUuDai());
            
            int rowsAffected = psInsert.executeUpdate();
            psInsert.close(); 

            if (rowsAffected > 0) {
                // B. SELECT mã HK vừa tạo
                if (hk.getCmndCccd() != null && !hk.getCmndCccd().trim().isEmpty()) {
                    psSelect = con.prepareStatement(sqlSelect);
                    psSelect.setString(1, hk.getCmndCccd());
                    rs = psSelect.executeQuery();

                    if (rs.next()) {
                        String generatedMaHK = rs.getString("maHanhKhach");
                        hk.setMaKH(generatedMaHK); // Gán mã HK hợp lệ
                    } else {
                        con.rollback(); 
                        throw new SQLException("Không thể truy vấn lại mã HK sau khi chèn.");
                    }
                }
                con.commit();
                return true;
            }
            con.rollback(); 
            return false;
        } catch (SQLException ex) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException rollbackEx) { }
            }
            // Ném lại lỗi, bao gồm lỗi "duplicate key" [3]
            throw ex; 
        } finally {
            try {
                if (rs != null) rs.close();
                if (psSelect != null) psSelect.close();
                if (con != null) con.setAutoCommit(true); 
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }
    
    private String generateMaHanhKhach() {
        // Logic tạo mã, ví dụ: hk-00001, hk-00002, v.v.
        String prefix = "hk-";
        int sequence = 1; // Nên lấy từ DB hoặc cấu hình
        return String.format("%s%05d", prefix, sequence); // Định dạng hk-00001
        // Lưu ý: Nên dùng sequence hoặc counter trong DB để tránh trùng lặp
    }
    private int layIdTiepTheo() throws SQLException {
        String sql = "SELECT COUNT(*) FROM HanhKhach";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) + 1; // Tăng ID dựa trên số bản ghi hiện tại
            }
            return 1; // Nếu bảng rỗng
        }
    }
    // DOC
    public List<HanhKhach> layDanhSachHanhKhach() throws SQLException {
        List<HanhKhach> danhSachHK = new ArrayList<>();
        String sql = "SELECT * FROM HanhKhach";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                danhSachHK.add(layHanhKhachTuResultSet(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return danhSachHK;
    }
    public HanhKhach layHanhKhachTheoMa(String maHK) throws SQLException {
        String sql = "SELECT * FROM HanhKhach WHERE maHanhKhach = ?";
        HanhKhach hk = null;
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHK);
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
    // Phương thức layHanhKhachTheoCMND/capNhatHanhKhach giữ nguyên [4, 5]
    public HanhKhach layHanhKhachTheoCMND(String cmndCccd) throws SQLException {
        // [Logic tìm kiếm HK theo CMND/CCCD giữ nguyên]
        String sql = "SELECT * FROM HanhKhach WHERE cmndCccd = ?";
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
    
    public HanhKhach layHanhKhachTheoSDT(String sdt) throws SQLException {
        String sql = "SELECT * FROM HanhKhach WHERE soDienThoai = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sdt);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String hoTen = rs.getString("hoTen");
                    String cmndCccd = rs.getString("cmndCccd");
                    String soDienThoai = rs.getString("soDienThoai");
                    LocalDate ngaySinh = rs.getObject("ngaySinh", LocalDate.class);
                    String maUuDai = rs.getString("maUuDai");
                    String maKH = rs.getString("maHanhKhach");
                    System.out.println("Dữ liệu từ ResultSet - hoTen: " + hoTen + ", soDienThoai: " + soDienThoai);
                    if (hoTen == null || hoTen.trim().isEmpty()) {
                        System.err.println("Họ tên rỗng hoặc null cho SĐT: " + sdt);
                        return null;
                    }
                    hoTen = hoTen.trim().toLowerCase().replaceAll("[0-9]", "");
                    hoTen = capitalizeEachWord(hoTen);
                    if (hoTen.isEmpty()) {
                        System.err.println("Họ tên không hợp lệ sau chuẩn hóa cho SĐT: " + sdt);
                        return null;
                    }
                    try {
                        return new HanhKhach(maKH, hoTen, cmndCccd, soDienThoai, ngaySinh, maUuDai);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Lỗi tạo HanhKhach từ ResultSet cho SĐT " + sdt + ": " + e.getMessage());
                        return null;
                    }
                }
            }
        }
        return null;
    }
    public List<HanhKhach> timHanhKhachTheoHoTen(String hoTen) throws SQLException {
        List<HanhKhach> danhSachHK = new ArrayList<>();
        String sql = "SELECT * FROM HanhKhach WHERE hoTen LIKE ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + hoTen + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    danhSachHK.add(layHanhKhachTuResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Lỗi tìm kiếm hành khách theo Họ tên: " + ex.getMessage());
            throw ex;
        }
        return danhSachHK;
    }
    public boolean capNhatHanhKhach(HanhKhach hk) throws SQLException {
        // [Logic cập nhật HK giữ nguyên]
        String sql = "UPDATE HanhKhach SET hoTen = ?, cmndCccd = ?, soDienThoai = ?, ngaySinh = ?, maUuDai = ? WHERE maHanhKhach = ?";
        try(
            Connection con = connectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, hk.getHoTen());
            ps.setString(2, hk.getCmndCccd());
            ps.setString(3, hk.getSoDT());
            ps.setDate(4, Date.valueOf(hk.getNgaySinh()));
            ps.setString(5, hk.getMaUuDai());
            ps.setString(6, hk.getMaKH());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    public boolean xoaHanhKhach(String maHK) throws SQLException {
        String sql = "DELETE FROM HanhKhach WHERE maHanhKhach = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHK);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    public boolean kiemTraHanhKhachTonTai(String cccd) throws SQLException {
        String sql = "SELECT COUNT(*) FROM HanhKhach WHERE cmndCccd = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cccd);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    private String capitalizeEachWord(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        String[] words = str.split("\\s+");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                      .append(word.substring(1).toLowerCase())
                      .append(" ");
            }
        }
        return result.toString().trim();
    }
}
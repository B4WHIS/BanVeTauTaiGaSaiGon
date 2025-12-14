package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connectDB.connectDB;
import entity.ChoNgoi;
import entity.ToaTau;

public class ChoNgoiDAO {

    

    //Thêm chỗ ngồi mới
    public boolean insertChoNgoi(ChoNgoi cn) throws SQLException {
        Connection conn = connectDB.getConnection();
        String sql = "INSERT INTO ChoNgoi (IDloaiGhe, trangThai, maToa) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cn.getIDloaiGhe());
            ps.setString(2, cn.getTrangThai());
            ps.setString(3, cn.getToaTau().getMaToa());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi thêm chỗ ngồi: " + e.getMessage());
            return false;
        }
    }
    
    public String getTenLoaiGheByID(int IDloaiGhe) throws SQLException {
        String tenLoai = null;
        String sql = "SELECT tenLoai FROM LoaiGhe WHERE IDloaiGhe = ?";
        
        try (Connection conn = connectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, IDloaiGhe);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tenLoai = rs.getString("tenLoai");
                }
            }
        }
        return tenLoai != null ? tenLoai : "[Không xác định]";
    }
    
    public boolean capNhatChoNgoi(ChoNgoi choNgoi) throws SQLException {
        String sql = "UPDATE ChoNgoi SET trangThai = ? WHERE maChoNgoi = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, choNgoi.getTrangThai());
            stmt.setString(2, choNgoi.getMaChoNgoi());
            return stmt.executeUpdate() > 0;
        }
    }
 // Thêm vào class ChoNgoiDAO (nếu chưa có)
    public boolean capNhatTrangThaiCho(String maChoNgoi, String trangThai) throws SQLException {
        String sql = "UPDATE ChoNgoi SET trangThai = ? WHERE maChoNgoi = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setString(2, maChoNgoi);
            return ps.executeUpdate() > 0;
        }
    }
    //  Cập nhật thông tin chỗ ngồi
    public boolean updateChoNgoi(ChoNgoi cn) throws SQLException {
        Connection conn = connectDB.getConnection();
        String sql = "UPDATE ChoNgoi SET IDloaiGhe = ?, trangThai = ?, maToa = ? WHERE maChoNgoi = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cn.getIDloaiGhe());
            ps.setString(2, cn.getTrangThai());
            ps.setString(3, cn.getToaTau().getMaToa());
            ps.setString(4, cn.getMaChoNgoi());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(" Lỗi cập nhật chỗ ngồi: " + e.getMessage());
            return false;
        }
    }

    public List<ChoNgoi> getSeatsByMaToa(String maToa) throws SQLException {
        List<ChoNgoi> danhSachCho = new ArrayList<>();
        // Trong CSDL, cột maChoNgoi được sinh ra dựa trên ID và maToa [2]
        String sql = "SELECT maChoNgoi, IDloaiGhe, trangThai, maToa FROM ChoNgoi WHERE maToa = ? ORDER BY maChoNgoi";
        
        try (Connection conn = connectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maToa);
            try (ResultSet rs = ps.executeQuery()) {
                ToaTau toaTau = new ToaTau(maToa); 
                while (rs.next()) {
                    ChoNgoi cn = new ChoNgoi(
                        rs.getString("maChoNgoi"),
                        rs.getInt("IDloaiGhe"),
                        rs.getString("trangThai"), // LẤY TRẠNG THÁI TỪ CSDL
                        toaTau
                    );
                    danhSachCho.add(cn);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi CSDL khi tải chỗ ngồi theo toa: " + e.getMessage());
            throw e;
        }
        return danhSachCho;
    }
    
    // Xóa chỗ ngồi theo mã
    public boolean deleteChoNgoi(String maChoNgoi) throws SQLException {
        Connection conn = connectDB.getConnection();
        String sql = "DELETE FROM ChoNgoi WHERE maChoNgoi = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maChoNgoi);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(" Lỗi xóa chỗ ngồi: " + e.getMessage());
            return false;
        }
    }

    // Tìm chỗ ngồi theo mã
    public ChoNgoi getChoNgoiByMa(String maChoNgoi) throws SQLException {
        Connection conn = connectDB.getConnection();
        String sql = "SELECT * FROM ChoNgoi WHERE maChoNgoi = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maChoNgoi);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ChoNgoi cn = new ChoNgoi(
                    rs.getString("maChoNgoi"),
                    rs.getInt("IDloaiGhe"),
                    rs.getString("trangThai"),
                    new ToaTau(rs.getString("maToa"))
                );
                rs.close();
                return cn;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Cập nhật trạng thái 
    public boolean updateTrangThai(String maChoNgoi, String trangThai, Connection conn) throws SQLException {
        String sql = "UPDATE ChoNgoi SET trangThai = ? WHERE maChoNgoi = ?";
        // Dùng conn được truyền vào
        try (PreparedStatement ps = conn.prepareStatement(sql)) { 
            ps.setString(1, trangThai);
            ps.setString(2, maChoNgoi);
            return ps.executeUpdate() > 0;
        } 
    }
    public boolean updateTrangThai(String maChoNgoi, String trangThai) throws SQLException {
        // Phương thức này tự mở kết nối và COMMIT, chỉ dùng cho các cập nhật độc lập
        Connection conn = connectDB.getConnection();
        String sql = "UPDATE ChoNgoi SET trangThai = ? WHERE maChoNgoi = ?";
        boolean result = false;
        try (PreparedStatement ps = conn.prepareStatement(sql)) { 
            ps.setString(1, trangThai);
            ps.setString(2, maChoNgoi);
            result = ps.executeUpdate() > 0;
            conn.commit(); // Tự commit
        } finally {
            if (conn != null) {
                conn.close(); // Đóng kết nối
            }
        }
        return result;
    }

}

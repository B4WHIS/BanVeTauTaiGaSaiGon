package dao;

import java.sql.*;
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
    public boolean updateTrangThai(String maChoNgoi, String trangThai) throws SQLException {
        Connection conn = connectDB.getConnection();
        String sql = "UPDATE ChoNgoi SET trangThai = ? WHERE maChoNgoi = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setString(2, maChoNgoi);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(" Lỗi cập nhật trạng thái chỗ ngồi: " + e.getMessage());
            return false;
        }
    }
}

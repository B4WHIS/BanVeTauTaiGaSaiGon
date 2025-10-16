package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import connectDB.connectDB;
import entity.LoaiGiaoDich;

public class LoaiGiaoDichDAO {

//     Thêm mới một loại giao dịch
    
    public boolean insertLoaiGiaoDich(LoaiGiaoDich lgd) {
        String sql = "INSERT INTO LoaiGiaoDich (tenGiaoDich, moTa) VALUES (?, ?)";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, lgd.getTenGiaoDich());
            stmt.setString(2, lgd.getMoTa());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi thêm loại giao dịch: " + e.getMessage());
        }
        return false;
    }

//    Cập nhật thông tin loại giao dịch
    
    public boolean updateLoaiGiaoDich(LoaiGiaoDich lgd) {
        String sql = "UPDATE LoaiGiaoDich SET tenGiaoDich = ?, moTa = ? WHERE IDloaiGiaoDich = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, lgd.getTenGiaoDich());
            stmt.setString(2, lgd.getMoTa());
            stmt.setInt(3, lgd.getIDloaiGiaoDich());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật loại giao dịch: " + e.getMessage());
        }
        return false;
    }

//     Xóa loại giao dịch theo ID
    
    public boolean deleteLoaiGiaoDich(int id) {
        String sql = "DELETE FROM LoaiGiaoDich WHERE IDloaiGiaoDich = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key")) {
                System.err.println("Không thể xóa vì loại giao dịch đang được sử dụng trong bảng khác.");
            } else {
                System.err.println("Lỗi xóa loại giao dịch: " + e.getMessage());
            }
        }
        return false;
    }

//     Lấy danh sách tất cả các loại giao dịch
    
    public List<LoaiGiaoDich> getAllLoaiGiaoDich() {
        List<LoaiGiaoDich> ds = new ArrayList<>();
        String sql = "SELECT * FROM LoaiGiaoDich ORDER BY IDloaiGiaoDich ASC";

        try (Connection con = connectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                LoaiGiaoDich lgd = new LoaiGiaoDich(
                        rs.getInt("IDloaiGiaoDich"),
                        rs.getString("tenGiaoDich"),
                        rs.getString("moTa")
                );
                ds.add(lgd);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách loại giao dịch: " + e.getMessage());
        }
        return ds;
    }

//     Tìm loại giao dịch theo ID
    
    public LoaiGiaoDich findByID(int id) {
        String sql = "SELECT * FROM LoaiGiaoDich WHERE IDloaiGiaoDich = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new LoaiGiaoDich(
                        rs.getInt("IDloaiGiaoDich"),
                        rs.getString("tenGiaoDich"),
                        rs.getString("moTa")
                );
            }

        } catch (SQLException e) {
            System.err.println("Lỗi tìm loại giao dịch theo ID: " + e.getMessage());
        }
        return null;
    }
}

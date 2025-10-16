package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import connectDB.connectDB;
import entity.LoaiChucVu;

public class LoaiChucVuDAO {

//     Thêm mới một loại chức vụ
     
    public boolean insertLoaiChucVu(LoaiChucVu lcv) {
        String sql = "INSERT INTO LoaiChucVu (tenChucVu, moTa) VALUES (?, ?)";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, lcv.getTenChucVu());
            stmt.setString(2, lcv.getMoTa());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi thêm loại chức vụ: " + e.getMessage());
        }
        return false;
    }

//    Cập nhật thông tin loại chức vụ
    
    public boolean updateLoaiChucVu(LoaiChucVu lcv) {
        String sql = "UPDATE LoaiChucVu SET tenChucVu = ?, moTa = ? WHERE IDloaiChucVu = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, lcv.getTenChucVu());
            stmt.setString(2, lcv.getMoTa());
            stmt.setInt(3, lcv.getIDloaiChucVu());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật loại chức vụ: " + e.getMessage());
        }
        return false;
    }

//    Xóa loại chức vụ theo ID
     
    public boolean deleteLoaiChucVu(int id) {
        String sql = "DELETE FROM LoaiChucVu WHERE IDloaiChucVu = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            // Trường hợp ID đang được dùng trong bảng NhanVien sẽ bị lỗi FK
            if (e.getMessage().contains("foreign key")) {
                System.err.println("Không thể xóa chức vụ vì đang được nhân viên sử dụng.");
            } else {
                System.err.println("Lỗi xóa loại chức vụ: " + e.getMessage());
            }
        }
        return false;
    }

//     Lấy toàn bộ danh sách loại chức vụ
     
    public List<LoaiChucVu> getAllLoaiChucVu() {
        List<LoaiChucVu> ds = new ArrayList<>();
        String sql = "SELECT * FROM LoaiChucVu ORDER BY IDloaiChucVu";

        try (Connection con = connectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                LoaiChucVu lcv = new LoaiChucVu(
                        rs.getInt("IDloaiChucVu"),
                        rs.getString("tenChucVu"),
                        rs.getString("moTa")
                );
                ds.add(lcv);
            }

        } catch (SQLException e) {
            System.err.println(" Lỗi lấy danh sách loại chức vụ: " + e.getMessage());
        }
        return ds;
    }

//     Tìm loại chức vụ theo ID
   
    public LoaiChucVu findByID(int id) {
        String sql = "SELECT * FROM LoaiChucVu WHERE IDloaiChucVu = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new LoaiChucVu(
                        rs.getInt("IDloaiChucVu"),
                        rs.getString("tenChucVu"),
                        rs.getString("moTa")
                );
            }

        } catch (SQLException e) {
            System.err.println("Lỗi tìm loại chức vụ theo ID: " + e.getMessage());
        }
        return null;
    }
}

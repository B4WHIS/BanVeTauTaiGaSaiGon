package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import entity.LichTrinh;
import entity.Ga;
import connectDB.connectDB;

public class LichTrinhDAO {

    // Phương thức lấy tất cả lịch trình
    public List<LichTrinh> getAllLichTrinh() {
        List<LichTrinh> listLichTrinh = new ArrayList<>();
        String sql = "SELECT lt.maLichTrinh, lt.tenLichTrinh, lt.gaDi, g1.tenGa AS tenGaDi, g1.diaChi AS diaChiDi, " +
                     "lt.gaDen, g2.tenGa AS tenGaDen, g2.diaChi AS diaChiDen, lt.khoangCach " +
                     "FROM LichTrinh lt " +
                     "JOIN Ga g1 ON lt.gaDi = g1.maGa " +
                     "JOIN Ga g2 ON lt.gaDen = g2.maGa";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                try {
                    Ga gaDi = new Ga(rs.getString("gaDi"), rs.getString("tenGaDi"), rs.getString("diaChiDi"));
                    Ga gaDen = new Ga(rs.getString("gaDen"), rs.getString("tenGaDen"), rs.getString("diaChiDen"));
                    LichTrinh lt = new LichTrinh();
                    lt.setMaLichTrinh(rs.getString("maLichTrinh"));
                    lt.setTenLichTrinh(rs.getString("tenLichTrinh"));
                    lt.setMaGaDi(gaDi);
                    lt.setMaGaDen(gaDen);
                    lt.setKhoangCach(rs.getDouble("khoangCach"));
                    listLichTrinh.add(lt);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listLichTrinh;
    }

    // Phương thức lấy tất cả tên lịch trình
    public List<String> getAllTenLichTrinh() {
        List<String> listTenLichTrinh = new ArrayList<>();
        String sql = "SELECT tenLichTrinh FROM LichTrinh ORDER BY tenLichTrinh";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                listTenLichTrinh.add(rs.getString("tenLichTrinh"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listTenLichTrinh;
    }

    // Phương thức lấy lịch trình theo mã lịch trình
    public LichTrinh getLichTrinhByMaLichTrinh(String maLichTrinh) {
        String sql = "SELECT lt.maLichTrinh, lt.tenLichTrinh, lt.gaDi, g1.tenGa AS tenGaDi, g1.diaChi AS diaChiDi, " +
                     "lt.gaDen, g2.tenGa AS tenGaDen, g2.diaChi AS diaChiDen, lt.khoangCach " +
                     "FROM LichTrinh lt " +
                     "JOIN Ga g1 ON lt.gaDi = g1.maGa " +
                     "JOIN Ga g2 ON lt.gaDen = g2.maGa " +
                     "WHERE lt.maLichTrinh = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maLichTrinh);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    try {
                        Ga gaDi = new Ga(rs.getString("gaDi"), rs.getString("tenGaDi"), rs.getString("diaChiDi"));
                        Ga gaDen = new Ga(rs.getString("gaDen"), rs.getString("tenGaDen"), rs.getString("diaChiDen"));
                        LichTrinh lt = new LichTrinh();
                        lt.setMaLichTrinh(rs.getString("maLichTrinh"));
                        lt.setTenLichTrinh(rs.getString("tenLichTrinh"));
                        lt.setMaGaDi(gaDi);
                        lt.setMaGaDen(gaDen);
                        lt.setKhoangCach(rs.getDouble("khoangCach"));
                        return lt;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Phương thức lấy lịch trình theo tên lịch trình
    public LichTrinh getLichTrinhByTenLichTrinh(String tenLichTrinh) {
        String sql = "SELECT lt.maLichTrinh, lt.tenLichTrinh, lt.gaDi, g1.tenGa AS tenGaDi, g1.diaChi AS diaChiDi, " +
                     "lt.gaDen, g2.tenGa AS tenGaDen, g2.diaChi AS diaChiDen, lt.khoangCach " +
                     "FROM LichTrinh lt " +
                     "JOIN Ga g1 ON lt.gaDi = g1.maGa " +
                     "JOIN Ga g2 ON lt.gaDen = g2.maGa " +
                     "WHERE lt.tenLichTrinh = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tenLichTrinh);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    try {
                        Ga gaDi = new Ga(rs.getString("gaDi"), rs.getString("tenGaDi"), rs.getString("diaChiDi"));
                        Ga gaDen = new Ga(rs.getString("gaDen"), rs.getString("tenGaDen"), rs.getString("diaChiDen"));
                        LichTrinh lt = new LichTrinh();
                        lt.setMaLichTrinh(rs.getString("maLichTrinh"));
                        lt.setTenLichTrinh(rs.getString("tenLichTrinh"));
                        lt.setMaGaDi(gaDi);
                        lt.setMaGaDen(gaDen);
                        lt.setKhoangCach(rs.getDouble("khoangCach"));
                        return lt;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Phương thức thêm lịch trình mới (maLichTrinh sẽ được tự động tạo bởi DB)
    public boolean addLichTrinh(LichTrinh lt) {
        String sql = "INSERT INTO LichTrinh (tenLichTrinh, gaDi, gaDen, khoangCach) VALUES (?, ?, ?, ?)";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, lt.getTenLichTrinh());
            pstmt.setString(2, lt.getMaGaDi().getMaGa()); // Xử lý khóa ngoại gaDi
            pstmt.setString(3, lt.getMaGaDen().getMaGa()); // Xử lý khóa ngoại gaDen
            pstmt.setDouble(4, lt.getKhoangCach());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        String generatedMaLichTrinh = "LT-" + String.format("%03d", id);
                        lt.setMaLichTrinh(generatedMaLichTrinh);
                    }
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Phương thức cập nhật lịch trình
    public boolean updateLichTrinh(LichTrinh lt) {
        String sql = "UPDATE LichTrinh SET tenLichTrinh = ?, gaDi = ?, gaDen = ?, khoangCach = ? WHERE maLichTrinh = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, lt.getTenLichTrinh());
            pstmt.setString(2, lt.getMaGaDi().getMaGa()); // Xử lý khóa ngoại gaDi
            pstmt.setString(3, lt.getMaGaDen().getMaGa()); // Xử lý khóa ngoại gaDen
            pstmt.setDouble(4, lt.getKhoangCach());
            pstmt.setString(5, lt.getMaLichTrinh());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Phương thức xóa lịch trình theo mã lịch trình
    public boolean deleteLichTrinh(String maLichTrinh) {
        String sql = "DELETE FROM LichTrinh WHERE maLichTrinh = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maLichTrinh);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public String getMaLichTrinhByTenLichTrinh(String tenLichTrinh) {
        String sql = "SELECT maLichTrinh FROM LichTrinh WHERE tenLichTrinh = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tenLichTrinh);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("maLichTrinh");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Phương thức lấy tên lịch trình theo mã lịch trình
    public String getTenLichTrinhByMaLichTrinh(String maLichTrinh) {
        String sql = "SELECT tenLichTrinh FROM LichTrinh WHERE maLichTrinh = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maLichTrinh);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("tenLichTrinh");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
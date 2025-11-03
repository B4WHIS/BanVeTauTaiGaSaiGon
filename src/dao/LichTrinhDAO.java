package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import entity.LichTrinh;
import entity.Ga;
import connectDB.connectDB;

public class LichTrinhDAO {

    // === HÀM KIỂM TRA TRÙNG GA ĐI + GA ĐẾN ===
    private boolean isTrungGaDiDen(String gaDi, String gaDen, String maLichTrinhHienTai) {
        String sql = "SELECT COUNT(*) FROM LichTrinh " +
                     "WHERE gaDi = ? AND gaDen = ? AND TrangThai = N'Hoạt động'";
        if (maLichTrinhHienTai != null) {
            sql += " AND maLichTrinh != ?";
        }
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, gaDi);
            pstmt.setString(2, gaDen);
            if (maLichTrinhHienTai != null) {
                pstmt.setString(3, maLichTrinhHienTai);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // === THÊM LỊCH TRÌNH (KIỂM TRA TRÙNG GA) ===
    public boolean addLichTrinh(LichTrinh lt) {
        String gaDi = lt.getMaGaDi().getMaGa();
        String gaDen = lt.getMaGaDen().getMaGa();

        // Kiểm tra trùng
        if (isTrungGaDiDen(gaDi, gaDen, null)) {
            System.err.println("Lỗi: Đã tồn tại lịch trình từ ga " + gaDi + " đến " + gaDen + "!");
            return false;
        }

        String sql = "INSERT INTO LichTrinh (tenLichTrinh, gaDi, gaDen, khoangCach, TrangThai) VALUES (?, ?, ?, ?, N'Hoạt động')";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, lt.getTenLichTrinh());
            pstmt.setString(2, gaDi);
            pstmt.setString(3, gaDen);
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

    // === SỬA LỊCH TRÌNH (KIỂM TRA TRÙNG GA) ===
    public boolean updateLichTrinh(LichTrinh lt) {
        String gaDi = lt.getMaGaDi().getMaGa();
        String gaDen = lt.getMaGaDen().getMaGa();
        String maLT = lt.getMaLichTrinh();

        // Kiểm tra trùng (trừ chính nó)
        if (isTrungGaDiDen(gaDi, gaDen, maLT)) {
            System.err.println("Lỗi: Đã tồn tại lịch trình khác từ ga " + gaDi + " đến " + gaDen + "!");
            return false;
        }

        String sql = "UPDATE LichTrinh SET tenLichTrinh = ?, gaDi = ?, gaDen = ?, khoangCach = ?, TrangThai = N'Hoạt động' WHERE maLichTrinh = ? AND TrangThai = N'Hoạt động'";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, lt.getTenLichTrinh());
            pstmt.setString(2, gaDi);
            pstmt.setString(3, gaDen);
            pstmt.setDouble(4, lt.getKhoangCach());
            pstmt.setString(5, maLT);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // === CÁC HÀM KHÁC GIỮ NGUYÊN 100% ===
    public List<LichTrinh> getAllLichTrinh() {
        List<LichTrinh> listLichTrinh = new ArrayList<>();
        String sql = "SELECT lt.maLichTrinh, lt.tenLichTrinh, lt.gaDi, g1.tenGa AS tenGaDi, g1.diaChi AS diaChiDi, " +
                     "lt.gaDen, g2.tenGa AS tenGaDen, g2.diaChi AS diaChiDen, lt.khoangCach " +
                     "FROM LichTrinh lt " +
                     "JOIN Ga g1 ON lt.gaDi = g1.maGa " +
                     "JOIN Ga g2 ON lt.gaDen = g2.maGa " +
                     "WHERE lt.TrangThai = N'Hoạt động'";
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

    public List<String> getAllTenLichTrinh() {
        List<String> listTenLichTrinh = new ArrayList<>();
        String sql = "SELECT tenLichTrinh FROM LichTrinh WHERE TrangThai = N'Hoạt động' ORDER BY tenLichTrinh";
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

    public LichTrinh getLichTrinhByMaLichTrinh(String maLichTrinh) {
        String sql = "SELECT lt.maLichTrinh, lt.tenLichTrinh, lt.gaDi, g1.tenGa AS tenGaDi, g1.diaChi AS diaChiDi, " +
                     "lt.gaDen, g2.tenGa AS tenGaDen, g2.diaChi AS diaChiDen, lt.khoangCach " +
                     "FROM LichTrinh lt " +
                     "JOIN Ga g1 ON lt.gaDi = g1.maGa " +
                     "JOIN Ga g2 ON lt.gaDen = g2.maGa " +
                     "WHERE lt.maLichTrinh = ? AND lt.TrangThai = N'Hoạt động'";
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

    public LichTrinh getLichTrinhByTenLichTrinh(String tenLichTrinh) {
        String sql = "SELECT lt.maLichTrinh, lt.tenLichTrinh, lt.gaDi, g1.tenGa AS tenGaDi, g1.diaChi AS diaChiDi, " +
                     "lt.gaDen, g2.tenGa AS tenGaDen, g2.diaChi AS diaChiDen, lt.khoangCach " +
                     "FROM LichTrinh lt " +
                     "JOIN Ga g1 ON lt.gaDi = g1.maGa " +
                     "JOIN Ga g2 ON lt.gaDen = g2.maGa " +
                     "WHERE lt.tenLichTrinh = ? AND lt.TrangThai = N'Hoạt động'";
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

    public boolean deleteLichTrinh(String maLichTrinh) {
        String sql = "UPDATE LichTrinh SET TrangThai = N'Ngừng hoạt động' WHERE maLichTrinh = ? AND TrangThai = N'Hoạt động'";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maLichTrinh);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi xóa mềm lịch trình: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean khoiPhucLichTrinh(String maLichTrinh) {
        String sql = "UPDATE LichTrinh SET TrangThai = N'Hoạt động' WHERE maLichTrinh = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maLichTrinh);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khôi phục lịch trình: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public String getMaLichTrinhByTenLichTrinh(String tenLichTrinh) {
        String sql = "SELECT maLichTrinh FROM LichTrinh WHERE tenLichTrinh = ? AND TrangThai = N'Hoạt động'";
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

    public String getTenLichTrinhByMaLichTrinh(String maLichTrinh) {
        String sql = "SELECT tenLichTrinh FROM LichTrinh WHERE maLichTrinh = ? AND TrangThai = N'Hoạt động'";
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
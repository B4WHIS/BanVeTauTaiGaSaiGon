// File: src/dao/ChoNgoiDAO.java
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
    public ChoNgoi layChoNgoiTheoMa(String maChoNgoi) throws SQLException {
        if (maChoNgoi == null || maChoNgoi.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã chỗ ngồi không được rỗng!");
        }

        String sql = """
            SELECT 
                CN.maChoNgoi, 
                CN.IDloaiGhe, 
                CN.trangThai,
                TT.maToa AS maToa
            FROM ChoNgoi CN
            JOIN ToaTau TT ON CN.maToa = TT.maToa
            WHERE CN.maChoNgoi = ?
            """;

        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maChoNgoi.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // 1. Lấy dữ liệu từ ResultSet
                    String ma = rs.getString("maChoNgoi");
                    int idLoaiGhe = rs.getInt("IDloaiGhe");
                    String trangThai = rs.getString("trangThai");
                    String maToa = rs.getString("maToa");

                    // 2. Tạo đối tượng ToaTau
                    ToaTau toaTau = new ToaTau();
                    toaTau.setMaToa(maToa);

                    // 3. Tạo đối tượng ChoNgoi (dùng constructor đầy đủ)
                    ChoNgoi choNgoi = new ChoNgoi();
                    choNgoi.setMaChoNgoi(ma);
                    choNgoi.setIDloaiGhe(idLoaiGhe);
                    choNgoi.setTrangThai(trangThai);
                    choNgoi.setToaTau(toaTau);

                    return choNgoi;
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi truy vấn ChoNgoi theo mã: " + maChoNgoi);
            e.printStackTrace();
            throw e;
        }

        return null; 
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

    // Lấy danh sách chỗ ngồi theo mã toa (đồng bộ với getSeatsByMaToa, nhưng giữ nguyên method gốc nếu có)
    public List<ChoNgoi> getChoNgoiByToa(String maToa) throws SQLException {
        return getSeatsByMaToa(maToa); // Liên kết với method gốc
    }

    // Lấy tất cả chỗ ngồi
    public List<ChoNgoi> getAllChoNgoi() throws SQLException {
        List<ChoNgoi> danhSachCho = new ArrayList<>();
        String sql = "SELECT maChoNgoi, IDloaiGhe, trangThai, maToa FROM ChoNgoi ORDER BY maChoNgoi";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ToaTau toaTau = new ToaTau(rs.getString("maToa"));
                ChoNgoi cn = new ChoNgoi(
                    rs.getString("maChoNgoi"),
                    rs.getInt("IDloaiGhe"),
                    rs.getString("trangThai"),
                    toaTau
                );
                danhSachCho.add(cn);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi CSDL khi tải tất cả chỗ ngồi: " + e.getMessage());
            throw e;
        }
        return danhSachCho;
    }
	public List<ChoNgoi> timKiemChoNgoi(String maToa, String maChuyenTau, Object object, Object object2, Object object3,
			Object object4) {
		// TODO Auto-generated method stub
		return null;
	}


}
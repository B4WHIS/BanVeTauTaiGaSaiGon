package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connectDB.connectDB;
import entity.UuDai;

public class UuDaiDAO {
	
    private UuDai getUuDaiTuResultSet(ResultSet rs) throws SQLException {
        try {
            String maUuDai = rs.getString("maUuDai");
            int IDloaiUuDai = rs.getInt("IDloaiUuDai");

            BigDecimal mucGiamGia = rs.getBigDecimal("mucGiamGia");
            String dieuKienApDung = rs.getString("dieuKienApDung");

            UuDai ud = new UuDai(maUuDai, IDloaiUuDai, mucGiamGia, dieuKienApDung);
            return ud;
        } catch (Exception ex) {
            throw new SQLException("Lỗi khi tạo đối tượng UuDai từ ResultSet: " + ex.getMessage(), ex);
        }
    }

//  DOC
    public List<UuDai> layTatCaUuDai() {
        List<UuDai> danhSachUD = new ArrayList<>();
     
        String sql = "SELECT maUuDai, IDloaiUuDai, mucGiamGia, dieuKienApDung FROM UuDai";
        try (Connection con = connectDB.getConnection();
        	 PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                danhSachUD.add(getUuDaiTuResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachUD;
    }

    public UuDai timUuDaiTheoMa(String maUD) {
        String sql = "SELECT maUuDai, IDloaiUuDai, mucGiamGia, dieuKienApDung FROM UuDai WHERE maUuDai = ?";
        try (Connection con = connectDB.getConnection();
        	 PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maUD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return getUuDaiTuResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
//   them
    public boolean themUuDai(UuDai ud) throws SQLException {
        Connection con = connectDB.getConnection();
        String sql = "INSERT INTO UuDai (IDloaiUuDai, mucGiamGia, dieuKienApDung) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ud.getIDloaiUuDai());
            ps.setBigDecimal(2, ud.getMucGiamGia());
            ps.setString(3, ud.getDieuKienApDung());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
//update
    public boolean capNhatUuDai(UuDai ud) {
        String sql = "UPDATE UuDai SET IDloaiUuDai = ?, mucGiamGia = ?, dieuKienApDung = ? WHERE maUuDai = ?";
        try (Connection con = connectDB.getConnection();
        	 PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ud.getIDloaiUuDai());
            ps.setBigDecimal(2, ud.getMucGiamGia());
            ps.setString(3, ud.getDieuKienApDung());
            ps.setString(4, ud.getMaUuDai());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
// xoa
    public boolean xoaUuDai(String maUD) {
        String sql = "DELETE FROM UuDai WHERE maUuDai = ?";
        try (Connection con = connectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maUD);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
}

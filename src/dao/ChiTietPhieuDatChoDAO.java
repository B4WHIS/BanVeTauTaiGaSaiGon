package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import connectDB.connectDB;
import entity.ChiTietPDC;
import entity.ChoNgoi;
import entity.ChuyenTau;
import entity.PhieuDatCho;

public class ChiTietPhieuDatChoDAO {

	//thêm chi tiết phiếu đặt chỗ
    public boolean themChiTietPDC(ChiTietPDC chiTiet) {
        if (chiTiet == null) {
        	return false;
        	}
        Connection con = connectDB.getConnection();
        PreparedStatement stmt = null;
        String sql = "INSERT INTO ChiTietPhieuDatCho(maPhieuDatCho, maChoNgoi, maChuyenTau) VALUES (?, ?, ?)";
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, chiTiet.getMaPhieuDatCho().getMaPhieuDatCho());
            stmt.setString(2, chiTiet.getMaChoNgoi().getMaChoNgoi());
            stmt.setString(3, chiTiet.getMaChuyenTau().getMaChuyenTau());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

  //lấy tất cả chi tiết pdc 
    public ArrayList<ChiTietPDC> LayTatCaChiTietPDC() {
        ArrayList<ChiTietPDC> ds = new ArrayList<>();
        Connection con = connectDB.getConnection();
        Statement stmt = null;
        ResultSet rs = null;

        String sql = "SELECT maPhieuDatCho, maChoNgoi, maChuyenTau FROM ChiTietPhieuDatCho";
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                PhieuDatCho pdc = new PhieuDatCho(rs.getString("maPhieuDatCho"));
                ChoNgoi choNgoi = new ChoNgoi(rs.getString("maChoNgoi"));
                ChuyenTau chuyenTau = new ChuyenTau(rs.getString("maChuyenTau"));
                ChiTietPDC chiTiet = new ChiTietPDC(pdc, choNgoi, chuyenTau);
                ds.add(chiTiet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ds;
    }

    // Lấy chi tiết theo mã phiếu đặt chỗ
    public ArrayList<ChiTietPDC> LaydsChiTietTheoMaPDC(String maPDC) {
        ArrayList<ChiTietPDC> ds = new ArrayList<>();
        Connection con = connectDB.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "SELECT maPhieuDatCho, maChoNgoi, maChuyenTau FROM ChiTietPhieuDatCho WHERE maPhieuDatCho = ?";
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, maPDC);
            rs = stmt.executeQuery();
            while (rs.next()) {
                PhieuDatCho pdc = new PhieuDatCho(rs.getString("maPhieuDatCho"));
                ChoNgoi choNgoi = new ChoNgoi(rs.getString("maChoNgoi"));
                ChuyenTau chuyenTau = new ChuyenTau(rs.getString("maChuyenTau"));
                ChiTietPDC chiTiet = new ChiTietPDC(pdc, choNgoi, chuyenTau);
                ds.add(chiTiet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ds;
    }
}

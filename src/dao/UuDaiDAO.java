package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

// DOC
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
		try (Connection con = connectDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
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

// them
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
		try (Connection con = connectDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
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
		try (Connection con = connectDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maUD);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
// public Map<Integer, String> layTatCaLoaiUuDai() throws SQLException {
// Map<Integer, String> danhSachLoaiUD = new HashMap<>(); // Thay thế cho List<LoaiUuDai>
// String sql = "SELECT IDloaiUD, tenLoai FROM LoaiUuDai";
// try (Connection con = connectDB.getConnection();
// PreparedStatement ps = con.prepareStatement(sql);
// ResultSet rs = ps.executeQuery()) {
// while (rs.next()) {
// int id = rs.getInt("IDloaiUD");
// String tenLoai = rs.getString("tenLoai");
// danhSachLoaiUD.put(id, tenLoai);
// }
// } catch (SQLException e) {
// throw e;
// }
// return danhSachLoaiUD;
// }

	public Map<Integer, String> layTatCaLoaiUuDai2() throws SQLException {
		Map<Integer, String> map = new HashMap<>();
		String sql = "SELECT IDLoaiUD, tenLoai FROM LoaiUuDai";
		try (Connection conn = connectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				int id = rs.getInt("IDLoaiUD");
				String ten = rs.getString("tenLoai");
				map.put(id, ten);
			}
		}
		return map;
	}

	public UuDai layUuDaiTheoMa(String maUuDai) throws SQLException {
		String sql = "SELECT maUuDai, IDLoaiUuDai, mucGiamGia, dieuKienApDung FROM UuDai WHERE maUuDai = ?";
		try (Connection conn = connectDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);) {
			ps.setString(1, maUuDai);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					String ma = rs.getString("maUuDai");
					int idLoai = rs.getInt("IDLoaiUuDai");
					BigDecimal giam = rs.getBigDecimal("mucGiamGia");
					String dk = rs.getString("dieuKienApDung");
					return new UuDai(ma, idLoai, giam, dk);
				}
			}
		}
		return null;
	}

	public Map<String, String> layTatCaLoaiUuDai() throws SQLException {
		Map<String, String> uuDaiMap = new HashMap<>();

		String sql = """
				    SELECT
				        UD.maUuDai,
				        LG.tenLoai,
				        UD.mucGiamGia,
				        UD.dieuKienApDung
				    FROM UuDai UD
				    JOIN LoaiUuDai LG ON UD.IDloaiUuDai = LG.IDloaiUD
				""";

		try (Connection conn = connectDB.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				String maUuDai = rs.getString("maUuDai");
				String tenLoai = rs.getString("tenLoai");
				String tenHienThi = tenLoai;

				uuDaiMap.put(maUuDai, tenHienThi);
			}
		} catch (SQLException e) {
			System.err.println("Lỗi tải danh sách Ưu đãi: " + e.getMessage());
			throw e;
		}
		return uuDaiMap;
	}

	public boolean themLoaiUuDai(String tenLoai) throws SQLException {
		String sql = "INSERT INTO LoaiUuDai (tenLoai) VALUES (?)";
		try (Connection con = connectDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, tenLoai);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			throw e;
		}
	}

	public boolean capNhatLoaiUuDai(int idLoaiUD, String tenLoaiMoi) throws SQLException {
		String sql = "UPDATE LoaiUuDai SET tenLoai = ? WHERE IDloaiUD = ?";
		try (Connection con = connectDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, tenLoaiMoi);
			ps.setInt(2, idLoaiUD);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			throw e;
		}
	}

	public boolean xoaLoaiUuDai(int idLoaiUD) throws SQLException {
		String sql = "DELETE FROM LoaiUuDai WHERE IDloaiUD = ?";
		try (Connection con = connectDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, idLoaiUD);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			throw new SQLException("Không thể xóa Loại Ưu đãi do có Ưu đãi đang tham chiếu.", e);
		}
	}

//	public UuDai getUuDaiByMa(String maUuDai) throws SQLException {
//	    if (maUuDai == null || maUuDai.equals("0")) {
//	        return new UuDai("0", 0, BigDecimal.ZERO, "");
//	    } else if (maUuDai.equals("1")) {
//	        return new UuDai("1", 1, new BigDecimal("100.00"), "Dưới 6 tuổi");
//	    } else if (maUuDai.equals("2")) {
//	        return new UuDai("2", 2, new BigDecimal("20.00"), "Có thẻ ưu đãi người cao tuổi");
//	    } else if (maUuDai.equals("3")) {
//	        return new UuDai("3", 3, new BigDecimal("5.00"), "Tích lũy điểm");
//	    }
//	    return null;
//	}

	public boolean kiemTraMaUuDaiTonTai(String maUuDai) throws SQLException {
		String sql = "SELECT COUNT(*) FROM UuDai WHERE maUuDai = ?";
		try (Connection conn = connectDB.getConnection() /* Kết nối CSDL của bạn */;
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, maUuDai);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
			return false;
		}
	}

	public BigDecimal getHeSoUuDaiByTen(String tenHienThi) throws SQLException {
		if ("Không áp dụng".equals(tenHienThi)) {
			return BigDecimal.ONE;
		}
		String maUuDai = tenHienThi.replaceAll(".*ID: (.*?),.*", "$1");
		UuDai uuDai = timUuDaiTheoMa(maUuDai);
		if (uuDai != null) {
			return uuDai.getMucGiamGia().divide(new BigDecimal("100"));
		}
		throw new SQLException("Không tìm thấy ưu đãi với tên: " + tenHienThi);
	}

	public List<String> getAllTenUuDai() throws SQLException {
		List<String> danhSachTenUuDai = new ArrayList<>();

		String sql = """
				    SELECT
				        UD.maUuDai,
				        LG.tenLoai,
				        UD.mucGiamGia,
				        UD.dieuKienApDung
				    FROM UuDai UD
				    JOIN LoaiUuDai LG ON UD.IDloaiUuDai = LG.IDloaiUD
				""";

		try (Connection conn = connectDB.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				String maUuDai = rs.getString("maUuDai");
				String tenLoai = rs.getString("tenLoai");
				BigDecimal mucGiam = rs.getBigDecimal("mucGiamGia");
				String dieuKien = rs.getString("dieuKienApDung");

				String tenHienThi = String.format("%s (ID: %s, Giảm: %.0f%%, DK: %s)", tenLoai, maUuDai,
						mucGiam.doubleValue(), dieuKien);
				danhSachTenUuDai.add(tenHienThi);
			}
		} catch (SQLException e) {
			System.err.println("Lỗi tải danh sách tên ưu đãi: " + e.getMessage());
			throw new SQLException("Không thể tải danh sách tên ưu đãi: " + e.getMessage(), e);
		}

		return danhSachTenUuDai;
	}

	public String getTenLoaiByID(int idLoaiUD) throws SQLException {
		String sql = "SELECT tenLoai FROM LoaiUuDai WHERE IDloaiUD = ?";

		try (Connection conn = connectDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, idLoaiUD);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getString("tenLoai").trim();
				}
			}
		} catch (SQLException e) {
			System.err.println("Lỗi CSDL khi truy vấn tên loại ưu đãi: " + e.getMessage());
			throw e;
		}
		return "Không xác định";
	}

}
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import connectDB.connectDB;
import entity.HanhKhach;
import entity.HoaDon;
import entity.NhanVien;

public class HoaDonDAO {

    private HoaDon getHoaDonFromRS(ResultSet rs) throws SQLException {
        HoaDon hd = new HoaDon();
        hd.setMaHoaDon(rs.getString("maHoaDon"));
        hd.setNgayLap(rs.getTimestamp("ngayLap").toLocalDateTime());

        HanhKhach hk = new HanhKhach();
        hk.setMaKH(rs.getString("maHanhKhach"));
        hk.setHoTen(rs.getString("tenHanhKhach"));
        hd.setMaHanhKhach(hk);

        NhanVien nv = new NhanVien();
        nv.setMaNhanVien(rs.getString("maNhanVien"));
        nv.setHoTen(rs.getString("tenNhanVien"));
        hd.setMaNhanVien(nv);

        hd.setTongTien(rs.getBigDecimal("tongTien"));
        return hd;
    }

    public String insert(HoaDon hd) throws SQLException {
        String sql = """
            INSERT INTO HoaDon (ngayLap, tongTien, maNhanVien, maHanhKhach)
            OUTPUT INSERTED.maHoaDon
            VALUES (?, ?, ?, ?)
            """;

        try (Connection conn = connectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, hd.getNgayLap());
            stmt.setBigDecimal(2, hd.getTongTien()); // ← DÙNG setTongTien
            stmt.setString(3, hd.getMaNhanVien().getMaNhanVien());
            stmt.setString(4, hd.getMaHanhKhach().getMaKH());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String maHoaDon = rs.getString(1);
                    hd.setMaHoaDon(maHoaDon);
                    return maHoaDon;
                }
            }
        }
        throw new SQLException("Không thể tạo hóa đơn.");
    }

    public List<HoaDon> getAll() throws SQLException {
        List<HoaDon> list = new ArrayList<>();
        String sql = """
            SELECT hd.maHoaDon, hd.ngayLap, hd.tongTien,
                   hk.maHanhKhach, hk.hoTen AS tenHanhKhach,
                   nv.maNhanVien, nv.hoTen AS tenNhanVien
            FROM HoaDon hd
            JOIN HanhKhach hk ON hd.maHanhKhach = hk.maHanhKhach
            JOIN NhanVien nv ON hd.maNhanVien = nv.maNhanVien
            ORDER BY hd.ngayLap DESC
            """;

        try (Connection conn = connectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(getHoaDonFromRS(rs));
            }
        }
        return list;
    }

    public HoaDon findByMa(String maHD) throws SQLException {
        String sql = """
            SELECT hd.maHoaDon, hd.ngayLap, hd.tongTien,
                   hk.maHanhKhach, hk.hoTen AS tenHanhKhach,
                   nv.maNhanVien, nv.hoTen AS tenNhanVien
            FROM HoaDon hd
            JOIN HanhKhach hk ON hd.maHanhKhach = hk.maHanhKhach
            JOIN NhanVien nv ON hd.maNhanVien = nv.maNhanVien
            WHERE hd.maHoaDon = ?
            """;

        try (Connection conn = connectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maHD);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return getHoaDonFromRS(rs);
                }
            }
        }
        return null;
    }

    public List<HoaDon> getByHanhKhach(String maHK) throws SQLException {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT maHoaDon, ngayLap, tongTien FROM HoaDon WHERE maHanhKhach = ?";

        try (Connection conn = connectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maHK);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    HoaDon hd = new HoaDon();
                    hd.setMaHoaDon(rs.getString("maHoaDon"));
                    hd.setNgayLap(rs.getTimestamp("ngayLap").toLocalDateTime());
                    hd.setTongTien(rs.getBigDecimal("tongTien"));
                    list.add(hd);
                }
            }
        }
        return list;
    }

    public String insert(HoaDon hd, Connection conn) throws SQLException {
        // CSDL sử dụng OUTPUT INSERTED.maHoaDon để trả về mã hóa đơn tự động tạo (HD-XXXXXX)
        String sql = """
            INSERT INTO HoaDon (ngayLap, tongTien, maHanhKhach, maNhanVien)
            OUTPUT INSERTED.maHoaDon 
            VALUES (?, ?, ?, ?)
        """;
        
        String maHoaDon = null;

        // Sử dụng Connection được truyền vào (conn)
        try (PreparedStatement ps = conn.prepareStatement(sql)) { 
            ps.setTimestamp(1, Timestamp.valueOf(hd.getNgayLap())); // [2]
            ps.setBigDecimal(2, hd.getTongTien()); // [2]
            ps.setString(3, hd.getMaHanhKhach().getMaKH()); // [2]
            ps.setString(4, hd.getMaNhanVien().getMaNhanVien()); // [3]

            // Thực thi và nhận mã hóa đơn (giống như VeDAO.themVe sử dụng OUTPUT [4])
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    maHoaDon = rs.getString(1);
                    hd.setMaHoaDon(maHoaDon); // Cập nhật mã hóa đơn cho đối tượng
                }
            }
        }
        
        if (maHoaDon == null) {
            throw new SQLException("Thêm hóa đơn thất bại, không nhận được mã hóa đơn trả về.");
        }
        return maHoaDon;
    }
    
    
    
    public List<HoaDon> searchHoaDon(String maHD, String tenHanhKhach, String tenNhanVien,
            java.time.LocalDateTime ngayLap, java.math.BigDecimal tongTien) throws SQLException {
List<HoaDon> list = new ArrayList<>();

StringBuilder sql = new StringBuilder("""
SELECT hd.maHoaDon, hd.ngayLap, hd.tongTien,
hk.maHanhKhach, hk.hoTen AS tenHanhKhach,
nv.maNhanVien, nv.hoTen AS tenNhanVien
FROM HoaDon hd
JOIN HanhKhach hk ON hd.maHanhKhach = hk.maHanhKhach
JOIN NhanVien nv ON hd.maNhanVien = nv.maNhanVien
WHERE 1=1
""");

List<Object> params = new ArrayList<>();

if (maHD != null && !maHD.isEmpty()) {
sql.append(" AND hd.maHoaDon LIKE ?");
params.add("%" + maHD + "%");
}
if (tenHanhKhach != null && !tenHanhKhach.isEmpty()) {
sql.append(" AND hk.hoTen LIKE ?");
params.add("%" + tenHanhKhach + "%");
}
if (tenNhanVien != null && !tenNhanVien.isEmpty()) {
sql.append(" AND nv.hoTen LIKE ?");
params.add("%" + tenNhanVien + "%");
}
if (ngayLap != null) {
sql.append(" AND CAST(hd.ngayLap AS DATE) = ?");
params.add(java.sql.Date.valueOf(ngayLap.toLocalDate()));
}
if (tongTien != null) {
sql.append(" AND hd.tongTien = ?");
params.add(tongTien);
}

sql.append(" ORDER BY hd.ngayLap DESC");

try (Connection conn = connectDB.getConnection();
PreparedStatement ps = conn.prepareStatement(sql.toString())) {

// gán tham số
for (int i = 0; i < params.size(); i++) {
ps.setObject(i + 1, params.get(i));
}

try (ResultSet rs = ps.executeQuery()) {
while (rs.next()) {
list.add(getHoaDonFromRS(rs));
}
}
}

return list;
}


}
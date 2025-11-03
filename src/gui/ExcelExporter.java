package gui;

import java.awt.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelExporter {

    public static void exportToExcel(JTable table, Component parent) {
    	
        // mở hop thoai
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Chọn nơi lưu file Excel");
        chooser.setFileFilter(new FileNameExtensionFilter("File Excel 97-2003 (*.xls)", "xls"));

        int chon = chooser.showSaveDialog(parent);
        if (chon != JFileChooser.APPROVE_OPTION) {
            return; 
        }

        File file = chooser.getSelectedFile();
        if (file == null) {
            return; 
        }

        String duongDan = file.getAbsolutePath();
        if (!duongDan.toLowerCase().endsWith(".xls")) {
            duongDan = duongDan + ".xls";
        }
        File fileCuoi = new File(duongDan);

        if (fileCuoi.exists()) {
            int xacNhan = JOptionPane.showConfirmDialog(parent, 
                "File đã tồn tại! Ghi đè không?", 
                "Xác nhận", 
                JOptionPane.YES_NO_OPTION);
            if (xacNhan != JOptionPane.YES_OPTION) {
                return;
            }
        }

        // tạo file Excel
        Workbook workbook = null;
        FileOutputStream fos = null;
        try {
            workbook = new HSSFWorkbook(); 
            Sheet sheet = workbook.createSheet("DanhSach");

            //tiêu đề 
            Row dongTieuDe = sheet.createRow(0); // Dòng đầu tiên
            CellStyle kieuTieuDe = taoKieuTieuDe(workbook); //tạo style

            // Lặp qua từng cột trong JTable
            for (int i = 0; i < table.getColumnCount(); i++) {
                Cell o = dongTieuDe.createCell(i); // Tạo ô
                o.setCellValue(table.getColumnName(i)); // Ghi tên cột
                o.setCellStyle(kieuTieuDe); // Áp dụng style
            }

            //tạo dulieu
            CellStyle kieuDuLieu = taoKieuDuLieu(workbook); 

            for (int i = 0; i < table.getRowCount(); i++) {
                Row dong = sheet.createRow(i + 1); 

                for (int j = 0; j < table.getColumnCount(); j++) {
                    Cell o = dong.createCell(j);
                    Object giaTri = table.getValueAt(i, j); 

                    if (giaTri != null) {
                        o.setCellValue(giaTri.toString());
                    } else {
                        o.setCellValue("");
                    }
                    o.setCellStyle(kieuDuLieu);
                }
            }

            //tu dong điều chỉnh chiều rọng
            for (int i = 0; i < table.getColumnCount(); i++) {
                sheet.autoSizeColumn(i); // Làm cho cột vừa nội dung
            }

            //ghi file
            fos = new FileOutputStream(fileCuoi);
            workbook.write(fos);

            // Thông báo thành công và mở file luôn
            JOptionPane.showMessageDialog(parent, 
                "Xuất file Excel thành công!\nĐường dẫn: " + fileCuoi.getAbsolutePath(),
                "Thành công", JOptionPane.INFORMATION_MESSAGE);

            java.awt.Desktop.getDesktop().open(fileCuoi);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent, 
                "Lỗi khi xuất file: " + e.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (fos != null) fos.close();
                if (workbook != null) workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Hàm tạo style cho tiêu đề
    private static CellStyle taoKieuTieuDe(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBold(true); // Chữ in đậm
        font.setFontHeightInPoints((short) 14); // Cỡ chữ 14
        font.setColor(IndexedColors.WHITE.getIndex()); // Chữ trắng
        style.setFont(font);

        style.setFillForegroundColor(IndexedColors.DARK_GREEN.getIndex()); // Nền xanh đậm
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        style.setAlignment(HorizontalAlignment.CENTER); // Căn giữa

        // Thêm viền cho ô
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
    }

    // Hàm tạo style cho dữ liệu
    private static CellStyle taoKieuDuLieu(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 12); // Cỡ chữ 12
        style.setFont(font);

        // Thêm viền mỏng cho ô
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
    }
}
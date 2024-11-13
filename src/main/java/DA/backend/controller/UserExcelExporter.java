package DA.backend.controller;

import java.io.IOException;
import java.util.List;

import DA.backend.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class UserExcelExporter {
    private XSSFWorkbook workbook; // Workbook chứa toàn bộ nội dung của file Excel
    private Sheet sheet; // Sheet đại diện cho một bảng trong file Excel
    private List<User> listUsers; // Danh sách người dùng cần xuất ra file Excel

    // Constructor để khởi tạo UserExcelExporter với danh sách người dùng
    public UserExcelExporter(List<User> listUsers) {
        this.listUsers = listUsers; // Gán danh sách người dùng vào biến lớp
        workbook = new XSSFWorkbook(); // Tạo mới một workbook (file Excel)
        sheet = workbook.createSheet("Users"); // Tạo sheet mới với tên "Users"
    }

    // Tạo dòng tiêu đề cho bảng trong sheet
    private void writeHeader() {
        Row headerRow = sheet.createRow(0); // Tạo dòng đầu tiên trong sheet (dòng tiêu đề)
        CellStyle style = createCellStyle(true); // Tạo style dành riêng cho tiêu đề (in đậm, font lớn)

        // Danh sách các tiêu đề cột
        String[] headers = {"User ID", "E-mail", "Full Name"};
        for (int i = 0; i < headers.length; i++) { // Lặp qua từng tiêu đề
            Cell cell = headerRow.createCell(i); // Tạo ô trong dòng tiêu đề
            cell.setCellValue(headers[i]); // Gán tên tiêu đề vào ô
            cell.setCellStyle(style); // Áp dụng style cho ô
            sheet.autoSizeColumn(i); // Tự động điều chỉnh độ rộng của cột để vừa với nội dung
        }
    }

    // Ghi dữ liệu người dùng vào các dòng tiếp theo trong sheet
    private void writeData() {
        int rowCount = 1; // Bắt đầu từ dòng 1 (dòng tiếp theo sau tiêu đề)
        CellStyle style = createCellStyle(false); // Tạo style cho dữ liệu (font bình thường)

        // Lặp qua danh sách người dùng và ghi dữ liệu vào sheet
        for (User user : listUsers) {
            Row row = sheet.createRow(rowCount++); // Tạo một dòng mới cho mỗi người dùng
            createCell(row, 0, user.getId(), style); // Ghi ID người dùng vào cột 0
            createCell(row, 1, user.getEmail(), style); // Ghi email người dùng vào cột 1
            createCell(row, 2, user.getName(), style); // Ghi tên đầy đủ người dùng vào cột 2
        }
    }

    // Tạo ô và thiết lập giá trị cùng style cho ô
    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        Cell cell = row.createCell(columnCount); // Tạo ô trong dòng ở vị trí cột đã chỉ định
        // Kiểm tra kiểu dữ liệu của giá trị và thiết lập vào ô
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value); // Nếu là Integer, gán giá trị dưới dạng số nguyên
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value); // Nếu là Boolean, gán giá trị đúng/sai
        } else {
            cell.setCellValue(value.toString()); // Nếu là kiểu khác, chuyển đổi sang chuỗi và gán
        }
        cell.setCellStyle(style); // Áp dụng style cho ô
    }

    // Tạo style cho tiêu đề và dữ liệu, điều chỉnh theo isHeader
    private CellStyle createCellStyle(boolean isHeader) {
        CellStyle style = workbook.createCellStyle(); // Tạo đối tượng style mới
        XSSFFont font = workbook.createFont(); // Tạo font chữ mới
        font.setFontHeight(isHeader ? 16 : 14); // Thiết lập kích thước font: 16 cho tiêu đề, 14 cho dữ liệu
        font.setBold(isHeader); // In đậm font nếu là tiêu đề
        style.setFont(font); // Gán font vào style
        return style; // Trả về style đã tạo
    }

    // Phương thức export để ghi workbook ra HttpServletResponse
    public void export(HttpServletResponse response) throws IOException {
        writeHeader(); // Gọi phương thức ghi dòng tiêu đề
        writeData(); // Gọi phương thức ghi dữ liệu người dùng vào sheet
        workbook.write(response.getOutputStream()); // Ghi nội dung workbook ra output stream của response
        workbook.close(); // Đóng workbook sau khi ghi xong để giải phóng tài nguyên
    }
}

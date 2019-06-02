package hr.fer.zemris.java.hw14.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import hr.fer.zemris.java.hw14.dao.DAOException;
import hr.fer.zemris.java.hw14.dao.DAOProvider;
import hr.fer.zemris.java.hw14.model.PollOption;

/**
 * Generates an XLS document with the table of voting results.
 * 
 * @author Bruno Iljazović
 */
@WebServlet("/servleti/glasanje-xls")
public class GlasanjeXlsServlet extends HttpServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		long pollid;
		
		try {
			pollid = Long.parseLong(req.getParameter("pollID"));
		} catch(NumberFormatException ex) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		List<PollOption> options;

		try {
			options = DAOProvider.getDao().getOptions(pollid);
		} catch(DAOException ex) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			ex.printStackTrace();
			return;
		}
		
		if (options.isEmpty()) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		HSSFWorkbook hwb = createWorkbook(options);
		
		resp.setContentType("application/vnd.ms-excel");
		resp.setHeader("Content-Disposition", "filename=rezultati.xls");
		hwb.write(resp.getOutputStream());
	}

	/**
	 * Creates the workbook with one sheet, voting results.
	 *
	 * @param results
	 *            the voting results
	 * @return the HSSF workbook
	 */
	private HSSFWorkbook createWorkbook(List<PollOption> results) {
		HSSFWorkbook hwb = new HSSFWorkbook();
		
		CellStyle headCellStyle = hwb.createCellStyle();
		headCellStyle.setAlignment(HorizontalAlignment.CENTER);
		headCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		headCellStyle.setBorderBottom(BorderStyle.MEDIUM);
		headCellStyle.setBorderTop(BorderStyle.MEDIUM);
		headCellStyle.setBorderLeft(BorderStyle.MEDIUM);
		headCellStyle.setBorderRight(BorderStyle.MEDIUM);
		Font headFont = hwb.createFont();
		headFont.setBold(true);
		headFont.setFontHeightInPoints((short)14);
		headCellStyle.setFont(headFont);
		
		CellStyle cellStyle = hwb.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		Font cellFont = hwb.createFont();
		cellFont.setFontHeightInPoints((short)12);
		cellStyle.setFont(cellFont);
		
		HSSFSheet sheet = hwb.createSheet("rezultati glasanja");
		
		HSSFRow rowHead = sheet.createRow(1);
		rowHead.setHeightInPoints(21);

		Cell cell1 = rowHead.createCell(1);
		cell1.setCellValue("Name");
		cell1.setCellStyle(headCellStyle);

		Cell cell2 = rowHead.createCell(2);
		cell2.setCellValue("Points");
		cell2.setCellStyle(headCellStyle);
		
		results.sort(PollOption.COMPARATOR_BY_VOTES);
		
		for (int i = 0; i < results.size(); i++) {
			HSSFRow row = sheet.createRow(i + 2);
			row.setHeightInPoints(20);

			cell1 = row.createCell(1);
			cell1.setCellValue(results.get(i).getTitle());
			cell1.setCellStyle(cellStyle);
			
			cell2 = row.createCell(2);
			cell2.setCellValue(results.get(i).getVotesCount());
			cell2.setCellStyle(cellStyle);
		}
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);

		return hwb;
	}
	
}

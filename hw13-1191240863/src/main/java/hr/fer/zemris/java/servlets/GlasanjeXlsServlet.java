package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import hr.fer.zemris.java.servlets.Util.Band;

/**
 * Generates an XLS document with the table of voting results.
 * 
 * @author Bruno Iljazović
 */
@WebServlet("/glasanje-xls")
public class GlasanjeXlsServlet extends HttpServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String resultFile = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		String definitionFile = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");

		Map<Integer,Band> results = Util.readDefinitionsAndResults(definitionFile, resultFile);
		
		if (results == null) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}

		HSSFWorkbook hwb = createWorkbook(results);
		
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
	private HSSFWorkbook createWorkbook(Map<Integer, Band> results) {
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
		
		List<Band> bands = new ArrayList<>(results.values());
		bands.sort((e, f) -> Integer.compare(f.getPoints(), e.getPoints()));
		
		for (int i = 0; i < bands.size(); i++) {
			HSSFRow row = sheet.createRow(i + 2);
			row.setHeightInPoints(20);

			cell1 = row.createCell(1);
			cell1.setCellValue(bands.get(i).getName());
			cell1.setCellStyle(cellStyle);
			
			cell2 = row.createCell(2);
			cell2.setCellValue(bands.get(i).getPoints());
			cell2.setCellStyle(cellStyle);
		}
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);

		return hwb;
	}
	
}

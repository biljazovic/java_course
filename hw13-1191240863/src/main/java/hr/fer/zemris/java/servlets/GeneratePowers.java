package hr.fer.zemris.java.servlets;


import java.io.IOException;

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

/**
 * Takes values a, b and n through request parameters and generates an XLS
 * document where on each of n sheets there are values from a to b and their
 * i-th powers, where i is the number of the sheet.
 * <p>
 * If any of the parameters is invalid, appropriate message is displayed
 * <p>
 * If a is greater than b, they are swapped and document is created normally.
 * 
 * @author Bruno Iljazović
 */
@WebServlet("/powers")
public class GeneratePowers extends HttpServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		final String messagePage = "/WEB-INF/pages/message.jsp";
		
		String aString = req.getParameter("a");
		String bString = req.getParameter("b");
		String nString = req.getParameter("n");
		
		Integer aNumber = null;
		Integer bNumber = null;
		Integer nNumber = null;
		
		try {
			aNumber = Integer.parseInt(aString);
			bNumber = Integer.parseInt(bString);
			nNumber = Integer.parseInt(nString);
		} catch(NumberFormatException ex) {
			req.setAttribute("message", "a, b and n should be numbers!");
			req.getRequestDispatcher(messagePage).forward(req, resp);
			return;
		}
		
		if (aNumber < -100 || aNumber > 100) {
			req.setAttribute("message", "a must be in range [-100,100]");
			req.getRequestDispatcher(messagePage).forward(req, resp);
		}
		
		if (bNumber < -100 || bNumber > 100) {
			req.setAttribute("message", "b must be in range [-100,100]");
			req.getRequestDispatcher(messagePage).forward(req, resp);
		}
		
		if (nNumber < 1 || nNumber > 5) {
			req.setAttribute("message", "n must be in range [1,5]");
			req.getRequestDispatcher(messagePage).forward(req, resp);
		}
		
		if (aNumber > bNumber) {
			int temp = aNumber;
			aNumber = bNumber;
			bNumber = temp;
		}
		
		HSSFWorkbook hwb = createWorkbook(aNumber, bNumber, nNumber);
		
		resp.setContentType("application/vnd.ms-excel");
		resp.setHeader("Content-Disposition", "filename=powers.xls");
		hwb.write(resp.getOutputStream());
	}

	/**
	 * Creates the workbook from the given a, b and n
	 *
	 * @param a the lower bound
	 * @param b the upper bound
	 * @param n the number of sheets
	 * @return the HSSF workbook
	 */
	private HSSFWorkbook createWorkbook(Integer a, Integer b, Integer n) {
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
		headFont.setFontHeightInPoints((short)12);
		headCellStyle.setFont(headFont);

		CellStyle cellStyle = hwb.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		
		for (int i = 1; i <= n; i++) {
			HSSFSheet sheet = hwb.createSheet("n=" + i);
			HSSFRow rowHead = sheet.createRow(1);

			Cell cell0 = rowHead.createCell(1);
			cell0.setCellValue("x");
			cell0.setCellStyle(headCellStyle);

			Cell cell1 = rowHead.createCell(2);
			cell1.setCellValue("x^" + i);
			cell1.setCellStyle(headCellStyle);

			for (int j = a; j <= b; j++) {
				HSSFRow row = sheet.createRow(j-a+2);

				cell0 = row.createCell(1);
				cell0.setCellValue(j);
				cell0.setCellStyle(cellStyle);

				cell1 = row.createCell(2);
				cell1.setCellValue(Math.pow(j, i));
				cell1.setCellStyle(cellStyle);
			}
		}
		
		return hwb;
	}
}

package io.github.someja.utilsx;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ExcelUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtils.class);

	/**
	 * 导出Excel，并将文件以流的形式写入到response中
	 * 
	 * @param response
	 * @param fileName
	 *            文件名
	 * @param propNameStr
	 *            属性名列表字符串，以英文逗号分隔
	 * @param titleStr
	 *            列名列表字符串，以英文逗号分隔
	 * @param widths
	 *            列宽
	 * @param datas
	 *            填充的数据列表
	 * @throws Exception
	 */
	public static final void exportExcel(HttpServletResponse response, String fileName, String propNameStr,
			String titleStr, int[] widths, List<?> datas) throws Exception {
		String[] propNames = propNameStr.split(",");
		String[] titles = titleStr.split(",");

		if (propNames.length != titles.length) {
			LOGGER.error("propNames length[{}] is not equal to titles length[{}]", propNames.length, titles.length);
			throw new RuntimeException();
		}

		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet();

		sheet.setColumnWidth(0, 100 * 40);
		XSSFRow row = sheet.createRow(0);
		XSSFCell cell = row.createCell(0);
		for (int j = 0; j < titles.length; j++) {
			sheet.setColumnWidth(j + 1, widths[j] * 40);
			cell = row.createCell(j + 1);
			cell.setCellValue(titles[j]);
		}

		// ConvertUtils.register((type, value) -> {
		// if (value != null) {
		// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// return formatter.format(value);
		// }
		// return null;
		// }, Date.class);

		for (int i = 0; i < datas.size(); i++) {
			row = sheet.createRow(i + 1);
			Object o = datas.get(i);
			cell = row.createCell(0);
			cell.setCellValue(i + 1);
			for (int j = 0; j < propNames.length; j++) {
				cell = row.createCell(j + 1);

				Object val = PropertyUtils.getProperty(o, propNames[j]);
				if (val != null) {
					cell.setCellValue(val.toString());
				}
			}
		}

		response.setHeader("Content-disposition",
				"attachment; filename=" + java.net.URLEncoder.encode(fileName, "UTF-8") + "");
		response.setContentType("application/msexcel;charset=utf-8");

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.flush();
		out.close();
	}

	// --------------导入用的-----------------------------------------
	public static String getStringValue(HSSFCell cell) {
		return cell.getStringCellValue();
	}

	public static Integer getIntegerValue(HSSFCell cell) {
		double d = cell.getNumericCellValue();
		return (int)d;
	}

	public static Double getDoubleValue(HSSFCell cell) {
		return cell.getNumericCellValue();
	}

	public static Timestamp getTimeValue(HSSFCell cell) throws Exception {
		Date date = cell.getDateCellValue();
		if (date == null)
			return null;
		return new Timestamp(date.getTime());
	}

	// ------------导出用的-------------------------------------------------------------

	/**
	 * 导出excel
	 * 
	 * @return
	 */
	public InputStream export(List<T> list, String[] columnsName, int[] columnsWidth) {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet();
			// 设置列宽
			setSheetColumnWidth(sheet, columnsWidth);
			// 创建表头
			HSSFRow head = sheet.createRow(0);
			// 设置表头格式
			HSSFCellStyle headStyle = getHeadStyle(workbook);
			// 设置表头
			for (int i = 0; i < columnsName.length; i++) {
				formatCell(head.createCell(i), columnsName[i], headStyle);
			}
			// 设置表格正文格式
			HSSFCellStyle bodyStyle = getBodyStyle(workbook);
			for (int i = 0; i < list.size(); i++) {
				this.buildBody(list.get(i), sheet.createRow(i + 1), bodyStyle);
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			workbook.write(baos);
			return new ByteArrayInputStream(baos.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将 t 的内容，填充到一行
	 * 
	 * @param t
	 *            被填充的对象
	 * @param row
	 *            当前行
	 * @param style
	 *            样式
	 */
	protected abstract void buildBody(T t, HSSFRow row, HSSFCellStyle bodyStyle);

	/**
	 * 设置列宽
	 * 
	 * @param sheet
	 * @param columnWidths
	 *            所有列的宽度
	 */
	protected void setSheetColumnWidth(HSSFSheet sheet, int[] columnWidths) {
		for (int i = 0; i < columnWidths.length; i++) {
			sheet.setColumnWidth(i, columnWidths[i]);
		}
	}

	/**
	 * 填充一个单元格
	 * 
	 * @param cell
	 *            要填充的单元格
	 * @param value
	 *            单元格内容
	 * @param cellStyle
	 *            单元格格式
	 */
	protected void formatCell(HSSFCell cell, Object value, HSSFCellStyle cellStyle) {
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellStyle(cellStyle);
		if (value != null) {
			cell.setCellValue(String.valueOf(value));
		}
	}

	/**
	 * 设置表格正文格式
	 * 
	 * @param workbook
	 * @return
	 */
	protected HSSFCellStyle getBodyStyle(HSSFWorkbook workbook) {
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		font.setFontHeightInPoints((short) 12);
		style.setFont(font);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		return style;
	}

	/**
	 * 设置表头格式
	 * 
	 * @param workbook
	 * @return
	 */
	protected HSSFCellStyle getHeadStyle(HSSFWorkbook workbook) {
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 12);
		style.setFont(font);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		style.setFillBackgroundColor(HSSFColor.CORNFLOWER_BLUE.index);
		return style;
	}

}

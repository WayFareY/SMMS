package com.zstar.SMMS.constant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {

	private static DecimalFormat df = new DecimalFormat("0");

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static DecimalFormat nf = new DecimalFormat("0");

	public static ArrayList<ArrayList<Object>> readExcel(File file) {
		if (file == null) {
			return null;
		}
		if (file.getName().endsWith("xlsx")) {
			return readExcel2007(file);
		}
		return readExcel2003(file);
	}

	public static ArrayList<ArrayList<Object>> readExcel2003(File file) {
		try {
			ArrayList<ArrayList<Object>> rowList = new ArrayList();

			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));
			HSSFSheet sheet = wb.getSheetAt(0);

			int i = sheet.getFirstRowNum();
			for (int rowCount = 0; rowCount < sheet.getPhysicalNumberOfRows(); i++) {
				HSSFRow row = sheet.getRow(i);
				ArrayList<Object> colList = new ArrayList();
				if (row == null) {
					if (i != sheet.getPhysicalNumberOfRows()) {
						rowList.add(colList);
					}
				} else {
					rowCount++;
					for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
						HSSFCell cell = row.getCell(j);
						if ((cell == null) || (cell.getCellType() == 3)) {
							if (j != row.getLastCellNum()) {
								colList.add("");
							}
						} else {
							Object value;
							switch (cell.getCellType()) {
							case 1:
								System.out.println(i + "行" + j + " 列 is String type");
								value = cell.getStringCellValue();
								break;
							case 0:
								if ("@".equals(cell.getCellStyle().getDataFormatString())) {
									value = df.format(cell.getNumericCellValue());
								} else {
									if ("General".equals(cell.getCellStyle().getDataFormatString())) {
										value = nf.format(cell.getNumericCellValue());
									} else {
										value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
									}
								}
								System.out.println(i + "行" + j + " 列 is Number type ; DateFormt:" + value.toString());
								break;
							case 4:
								System.out.println(i + "行" + j + " 列 is Boolean type");
								value = Boolean.valueOf(cell.getBooleanCellValue());
								break;
							case 3:
								System.out.println(i + "行" + j + " 列 is Blank type");
								value = "";
								break;
							case 2:
							default:
								System.out.println(i + "行" + j + " 列 is default type");
								value = cell.toString();
							}
							colList.add(value);
						}
					}
					rowList.add(colList);
				}
			}
			return rowList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<ArrayList<Object>> readExcel2007(File file) {
		try {
			ArrayList<ArrayList<Object>> rowList = new ArrayList();

			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
			XSSFSheet sheet = wb.getSheetAt(0);

			int i = sheet.getFirstRowNum();
			for (int rowCount = 0; rowCount < sheet.getPhysicalNumberOfRows(); i++) {
				XSSFRow row = sheet.getRow(i);
				ArrayList<Object> colList = new ArrayList();
				if (row == null) {
					if (i != sheet.getPhysicalNumberOfRows()) {
						rowList.add(colList);
					}
				} else {
					rowCount++;
					for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
						XSSFCell cell = row.getCell(j);
						if ((cell == null) || (cell.getCellType() == 3)) {
							if (j != row.getLastCellNum()) {
								colList.add("");
							}
						} else {
							Object value;
							switch (cell.getCellType()) {
							case 1:
								System.out.println(i + "行" + j + " 列 is String type");
								value = cell.getStringCellValue();
								break;
							case 0:
								if ("@".equals(cell.getCellStyle().getDataFormatString())) {
									value = df.format(cell.getNumericCellValue());
								} else {
									if ("General".equals(cell.getCellStyle().getDataFormatString())) {
										value = nf.format(cell.getNumericCellValue());
									} else {
										value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
									}
								}
								System.out.println(i + "行" + j + " 列 is Number type ; DateFormt:" + value.toString());
								break;
							case 4:
								System.out.println(i + "行" + j + " 列 is Boolean type");
								value = Boolean.valueOf(cell.getBooleanCellValue());
								break;
							case 3:
								System.out.println(i + "行" + j + " 列 is Blank type");
								value = "";
								break;
							case 2:
							default:
								System.out.println(i + "行" + j + " 列 is default type");
								value = cell.toString();
							}
							colList.add(value);
						}
					}
					rowList.add(colList);
				}
			}
			return rowList;
		} catch (Exception e) {
			System.out.println("exception");
		}
		return null;
	}

	public static void writeExcel(ArrayList<ArrayList<Object>> result, String path) {
		if (result == null) {
			return;
		}
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("sheet1");
		for (int i = 0; i < result.size(); i++) {
			HSSFRow row = sheet.createRow(i);
			if (result.get(i) != null) {
				for (int j = 0; j < ((ArrayList) result.get(i)).size(); j++) {
					HSSFCell cell = row.createCell(j);
					cell.setCellValue(((ArrayList) result.get(i)).get(j).toString());
				}
			}
		}
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			wb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] content = os.toByteArray();
		File file = new File(path);
		OutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(content);
			os.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static DecimalFormat getDf() {
		return df;
	}

	public static void setDf(DecimalFormat df) {
		df = df;
	}

	public static SimpleDateFormat getSdf() {
		return sdf;
	}

	public static void setSdf(SimpleDateFormat sdf) {
		sdf = sdf;
	}

	public static DecimalFormat getNf() {
		return nf;
	}

	public static void setNf(DecimalFormat nf) {
		nf = nf;
	}

	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '	') {
				c[i] = ' ';
			} else if ((c[i] > 65280) && (c[i] < 65375)) {
				c[i] = ((char) (c[i] - 65248));
			}
		}
		return new String(c);
	}

	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	public static void main(String[] args) {
		String str = "156465.feaf54，151";
		System.out.print(str);
		System.out.print(ToDBC(str));
	}
}

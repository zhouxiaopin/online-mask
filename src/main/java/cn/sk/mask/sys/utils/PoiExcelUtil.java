package cn.sk.mask.sys.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 读取Excel
 * 
 * @author chengy
 */
@Slf4j
public class PoiExcelUtil {

	/**
	 * 读取excel
	 * @param file	文件
	 * @param titleMap	标题字段名映射
	 * @param commonMap 公共
	 * @return
	 */
	public static Map<String,Object> readExcel(MultipartFile file,Map<String,String> titleMap,Map<String,String> commonMap) {
		Map<String,Object> datas = new HashMap<>();
		try {
			boolean is03 = true;
			String fileName = file.getOriginalFilename();
			String ext = fileName.substring(fileName.lastIndexOf("."));
			if(".xlsx".equals(ext)) {
				is03 = false;
			}
			//创建Excel，读取文件内容
			Workbook wb = createWorkbookByExt(file.getInputStream(),is03);

			//读取默认第一个工作表sheet
			Sheet sheet = wb.getSheetAt(0);
			//读取excel标题
			String[] title = readExcelTitle(sheet);

			//行
			Row row;
			// 得到总行数
			int rowNum = sheet.getLastRowNum();
			// 正文内容应该从第二行开始,第一行为表头的标题
			List<Map<String,String>> list = new ArrayList<Map<String,String>>();

			String key = null;
			int j = 0;
			for (int i = 1; i <= rowNum; i++) {
				row = sheet.getRow(i);
				j = 0;
				Map<String, String> cellValue = new HashMap<>();
				//获取当前行最后单元格列号
				int lastCellNum = row.getLastCellNum();
				while (j < lastCellNum) {
					key = titleMap.get(title[j]);
					if(key == null){//只导入map有的列
						j++;
						continue;
					}
					String obj = getCellFormatValue(row.getCell(j));
					cellValue.put(key, obj);
					j++;
				}
				cellValue.putAll(commonMap);
				list.add(cellValue);
			}
			datas.put("list",list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return datas;

	}

	/**
	 * 读取excel带图片的
	 * @param file	文件
	 * @param titleMap	标题字段名映射
	 * @param commonMap 公共
	 * @return
	 */
	public static Map<String,Object> readExcelHasPic(MultipartFile file, Map<String,String> titleMap, Map<String,String> commonMap) {
		Map<String,Object> datas = new HashMap<>();
		try {
			boolean is03 = true;
			String fileName = file.getOriginalFilename();
			String ext = fileName.substring(fileName.lastIndexOf("."));
			if(".xlsx".equals(ext)) {
				is03 = false;
			}
			//创建Excel，读取文件内容
			Workbook wb = createWorkbookByExt(file.getInputStream(),is03);

			//读取默认第一个工作表sheet
			Sheet sheet = wb.getSheetAt(0);
			//读取excel标题
			String[] title = readExcelTitle(sheet);

			//行
			Row row;
			// 得到总行数
			int rowNum = sheet.getLastRowNum();
			// 正文内容应该从第二行开始,第一行为表头的标题
			List<Map<String,String>> list = new ArrayList<Map<String,String>>();

			String key = null;
			int j = 0;
			for (int i = 1; i <= rowNum; i++) {
				row = sheet.getRow(i);
				j = 0;
				Map<String, String> cellValue = new HashMap<>();
				//获取当前行最后单元格列号
				int lastCellNum = row.getLastCellNum();
				while (j < lastCellNum) {
					key = titleMap.get(title[j]);
					if(key == null){//只导入map有的列
						j++;
						continue;
					}
					String obj = getCellFormatValue(row.getCell(j));
					cellValue.put(key, obj);
					j++;
				}
				cellValue.putAll(commonMap);
				list.add(cellValue);
			}
			datas.put("list",list);

			//判断用07还是03的方法获取图片
			Map<String, PictureData>  picList=null;
			if(is03) {
				picList = getPictures1((HSSFSheet) sheet);
			}else{
				picList = getPictures2((XSSFSheet) sheet);
			}
			datas.put("picList",picList);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return datas;

	}

	/**
	 * 根据扩展名创建工作簿
	 * @param in
	 * @param is03
	 * @return
	 */
	private static Workbook createWorkbookByExt(InputStream in,boolean is03) {
		Workbook wb = null;
		try {
			if (is03) {
				wb = new HSSFWorkbook(in);
			} else{
				wb = new XSSFWorkbook(in);
			}
		} catch (FileNotFoundException e) {
			log.error("FileNotFoundException", e);
		} catch (IOException e) {
			log.error("IOException", e);
		}
		return wb;
	}


	/**
	 * 读取Excel表格表头的内容
	 * @param sheet
	 * @return String 表头内容的数组
	 */
	private static String[] readExcelTitle(Sheet sheet){
		//获取第一行
		Row row = sheet.getRow(0);
		// 标题总列数
		int colNum = row.getPhysicalNumberOfCells();
		String[] title = new String[colNum];
		for (int i = 0; i < colNum; i++) {
			title[i] = getCellFormatValue(row.getCell(i));
		}
		return title;
	}


	/**
	 * 
	 * 根据Cell类型设置数据
	 * @param cell
	 * @return
	 */
	private static String getCellFormatValue(Cell cell) {
		String cellvalue = "";
		try {
			if (cell != null) {
				// 判断当前Cell的Type
				CellType type = cell.getCellType();
				if(type == CellType.NUMERIC) {// 如果当前Cell的Type为NUMERIC
					if (DateUtil.isCellDateFormatted(cell)) {
						cellvalue = DateUtils.dateFomat.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
					} else {
						cellvalue = (new BigDecimal(cell.getNumericCellValue())).toString();
					}
				}else if(type == CellType.FORMULA){
					if (DateUtil.isCellDateFormatted(cell)) {
						cellvalue = DateUtils.dateFomat.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
					} else {// 如果是纯数字
						// 取得当前Cell的数值
						cellvalue = (new BigDecimal(cell.getNumericCellValue())).toString();
					}
				}else if(type == CellType.STRING){// 如果当前Cell的Type为STRING
					// 取得当前的Cell字符串
					cellvalue = cell.getRichStringCellValue().getString();
				}else{// 默认的Cell值
					cellvalue = "";
				}
			} else {
				cellvalue = "";
			}
		} catch (Exception e) {
		}
		return cellvalue;
	}

	/**
	 * 获取图片和位置 (xls)
	 * @param sheet
	 * @return
	 */
	private static Map<String, PictureData> getPictures1 (HSSFSheet sheet) {
		Map<String, PictureData> map = new HashMap<String, PictureData>();
		HSSFPatriarch hssfShapes = sheet.getDrawingPatriarch();
		if(null == hssfShapes) {
		    return map;
		}
		List<HSSFShape> list = sheet.getDrawingPatriarch().getChildren();
		for (HSSFShape shape : list) {
			if (shape instanceof HSSFPicture) {
				HSSFPicture picture = (HSSFPicture) shape;
				HSSFClientAnchor cAnchor = (HSSFClientAnchor) picture.getAnchor();
				PictureData pdata = picture.getPictureData();
				String key = cAnchor.getRow1() + "-" + cAnchor.getCol1(); // 行号-列号
				map.put(key, pdata);
			}
		}
		return map;
	}

	/**
	 * 获取图片和位置 (xlsx)
	 * @param sheet
	 * @return
	 */
	private static Map<String, PictureData> getPictures2 (XSSFSheet sheet) {
		Map<String, PictureData> map = new HashMap<String, PictureData>();
		List<POIXMLDocumentPart> list = sheet.getRelations();
		for (POIXMLDocumentPart part : list) {
			if (part instanceof XSSFDrawing) {
				XSSFDrawing drawing = (XSSFDrawing) part;
				List<XSSFShape> shapes = drawing.getShapes();
				for (XSSFShape shape : shapes) {
					XSSFPicture picture = (XSSFPicture) shape;
					XSSFClientAnchor anchor = picture.getPreferredSize();
					CTMarker marker = anchor.getFrom();
					String key = marker.getRow() + "-" + marker.getCol();
					map.put(key, picture.getPictureData());
				}
			}
		}
		return map;
	}

	/*//图片写出
	public static void printImg(Map<String, PictureData> sheetList,String fileUploadPath) throws IOException {

		//for (Map<String, PictureData> map : sheetList) {
		Object key[] = sheetList.keySet().toArray();
		for (int i = 0; i < sheetList.size(); i++) {
			// 获取图片流
			PictureData pic = sheetList.get(key[i]);
			// 获取图片索引
			String picName = key[i].toString();
			// 获取图片格式
			String ext = pic.suggestFileExtension();

			byte[] data = pic.getData();

			//图片保存路径
			FileOutputStream out = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\pic" + picName + "." + ext);
			out.write(data);
			out.close();
		}
		// }

	}*/


	/**************************************/

	/*************导出相关开始**************/
	/**
	 * Excel导出
	 * @param os
	 * @param title
	 * @param colWidth
	 * @param lists
	 */
	public static void writeExcel(OutputStream os, String[] title, int[] colWidth, List<List<String>> lists){
		//创建Excel工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();
		//创建一个工作表sheet
		HSSFSheet sheet = workbook.createSheet();
		//创建第一行
		HSSFRow row = sheet.createRow(0);
		//设定第一行的行高
		row.setHeightInPoints(30);

		//添加第一列标题序号
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("序号");
		sheet.setColumnWidth(0, 10*256);
		// 开始写入第一行(即标题栏)
		for (int i = 0; i < title.length; i++) {
			sheet.setColumnWidth(i+1, colWidth[i]*256);
			cell = row.createCell(i+1);
			cell.setCellValue(title[i]);
		}
		//追加数据
		for (int i = 0,len = lists.size(); i < len; i++) {
			List<String> rowData = lists.get(i);
			HSSFRow nextrow = sheet.createRow(i+1);
			for(int j = 0,length = rowData.size(); j <= length; j++) {
				cell = nextrow.createCell(j);
				if(j==0) {
					cell.setCellValue(i+"");
				}else {
					cell.setCellValue(rowData.get(j-1));
				}
			}
		}
		try {
			workbook.write(os);
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*************导出相关结束**************/

	/**************************************/

	
	public static void main(String[] args) {
		/*try {
			String filepath = "C:/Users/ticc/Desktop/新建文件夹/Archive/4.中运海珠客运站工会.xls";
			PoiExcelUtil excelReader = new PoiExcelUtil(
					"123.xls", new FileInputStream(new File(
							filepath)));
			// 对读取Excel表格标题测试
			// String[] title = excelReader.readExcelTitle();
			// System.out.println("获得Excel表格的标题:");
			// for (String s : title) {
			// System.out.print(s + " ");
			// }

			// 对读取Excel表格内容测试
			Map<String,String> staticMap = new HashMap<String,String>();
			staticMap.put("organid", "0000001");
			List<Map<String,String>> map = excelReader.readExcelContent(staticMap);
			System.out.println("获得Excel表格的内容:");
			for (int i = 0; i < map.size(); i++) {
				System.out.println(map.get(i));
			}
		} catch (FileNotFoundException e) {
			System.out.println("未找到指定路径的文件!");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}

	
}
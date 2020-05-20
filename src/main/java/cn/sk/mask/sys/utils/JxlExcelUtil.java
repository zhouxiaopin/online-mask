package cn.sk.mask.sys.utils;

import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class JxlExcelUtil {
	/**
	 * Excel读取方法
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static List<Object[]> readExcel(String path) {
		InputStream is = null;
		Workbook rwb = null;
		try {
			is = new FileInputStream(new File(path));
			rwb = Workbook.getWorkbook(is);
			Sheet st = rwb.getSheet(0);
			if (st != null) {
				int rows = st.getRows();
				int cols = st.getColumns();
				List<Object[]> list = new ArrayList<Object[]>();
				for (int i = 0; i < rows; i++) {
					if (i > 0) {
						Object[] obj = new Object[cols];
						for (int j = 0; j < cols; j++) {
							obj[j] = st.getCell(j, i).getContents();
						}
						list.add(obj);
					}
				}
				return list;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			rwb.close();
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * Excel读取方法
	 *
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static List<Object[]> readExcel(MultipartFile file) {
		InputStream is = null;
		Workbook rwb = null;
		try {
			is = file.getInputStream();
			rwb = Workbook.getWorkbook(is);
			Sheet st = rwb.getSheet(0);
			if (st != null) {
				int rows = st.getRows();
				int cols = st.getColumns();
				List<Object[]> list = new ArrayList<Object[]>();
				for (int i = 0; i < rows; i++) {
					if (i > 0) {
						Object[] obj = new Object[cols];
						for (int j = 0; j < cols; j++) {
							obj[j] = st.getCell(j, i).getContents();
						}
						list.add(obj);
					}
				}
				return list;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			rwb.close();
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Excel导出
	 * 
	 * @param os
	 * @param title
	 * @param colWidth
	 * @param lists
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	public static void writeExcel(OutputStream os, String[] title, int[] colWidth, List<List<String>> lists) throws IOException, RowsExceededException, WriteException {
		// 创建可以写入的Excel工作薄(默认运行生成的文件在tomcat/bin下 )
		WritableWorkbook wwb = Workbook.createWorkbook(os);
		// 生成工作表,(name:First Sheet,参数0表示这是第一页)
		WritableSheet sheet = wwb.createSheet("First Sheet", 0);
		
		//设定第一行的行高
		sheet.setRowView(0, 450);

		// 头部单元格字体
		WritableFont titleFont = new WritableFont(WritableFont.createFont("SimSun"), 15, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
		// 头部单元格样式
		WritableCellFormat titleFormat = new WritableCellFormat(titleFont);
		// 把水平对齐方式指定为居中
		titleFormat.setAlignment(Alignment.CENTRE);
		// 把垂直对齐方式指定为居中
		titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		// 内容自动换行
		titleFormat.setWrap(true);
		
		//添加第一列标题序号
		sheet.addCell(new Label(0, 0, "序号", titleFormat));
		//将第1列的宽度设为8
		sheet.setColumnView(0, 8);

		// 开始写入第一行(即标题栏)
		for (int i = 0; i < title.length; i++) {
			// 将定义好的单元格添加到工作表中
			sheet.addCell(new Label(i+1, 0, title[i], titleFormat));
			
			//将第i列的宽度设为colWidth[i]
			sheet.setColumnView(i+1, colWidth[i]);
		}
		
		
		// 内容单元格字体
		WritableFont contentFont = new WritableFont(WritableFont.createFont("SimSun"), 12, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
		// 内容单元格样式
		WritableCellFormat contentFormat = new WritableCellFormat(contentFont);
		// 把垂直对齐方式指定为居中
		contentFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		// 内容自动换行
		contentFormat.setWrap(true);
		
		
		// 开始写入内容
		for (int row = 0; row < lists.size(); row++) {
			
			//设置每一行的高度为30
			sheet.setRowView(row+1, 400);
			
			//添加第一列序号值
			sheet.addCell(new Label(0, row + 1, String.valueOf(row+1), contentFormat));
			
			// 获取一条(一行)记录
			List<String> list = lists.get(row);
			// 数据是文本时(用label写入到工作表中)
			for (int col = 0; col < list.size(); col++) {
				String listvalue = "";
				if (null != list.get(col)) {
					listvalue = list.get(col);
				}
				
				sheet.addCell(new Label(col+1, row + 1, listvalue, contentFormat));
			}
		}
		
		/*
		 * 生成一个保存数字的单元格,必须使用Number的完整包路径,否则有语法歧义,值为789.123 jxl.write.Number
		 * number = new jxl.write.Number(col, row, 555.12541);
		 * sheet.addCell(number);
		 */

		/*
		 * 生成一个保存日期的单元格,必须使用DateTime的完整包路径,否则有语法歧义,值为new Date()
		 * jxl.write.DateTime date = new jxl.write.DateTime(col, row, new
		 * java.util.Date()); sheet.addCell(date);
		 */
		
		// 写入数据
		wwb.write();
		// 关闭文件
		wwb.close();
		// 关闭输出流
		os.close();
	}

	/**
	 * 向客户端下载文件,弹出下载框.
	 * 
	 * @param response
	 *            (HttpServletResponse)
	 * @param file
	 *            (需要下载的文件)
	 * @param isDel
	 *            (下载完成后是否删除该文件)
	 * @throws IOException
	 */
	public static void exportFile(HttpServletResponse response, File file, boolean isDel) throws IOException {
		OutputStream out = null;
		InputStream in = null;

		// 获得文件名
		String filename = URLEncoder.encode(file.getName(), "UTF-8");
		// 定义输出类型(下载)
		response.setContentType("application/force-download");
		response.setHeader("Location", filename);
		// 定义输出文件头
		response.setHeader("Content-Disposition", "attachment;filename=" + filename);
		// 取得输出流
		out = response.getOutputStream();
		in = new FileInputStream(file.getPath());

		byte[] buffer = new byte[1024];
		int i = -1;
		while ((i = in.read(buffer)) != -1) {
			out.write(buffer, 0, i);
		}
		out.flush();
		in.close();
		out.close();
		if (isDel) {
			// 删除文件,删除前关闭所有的Stream.
			file.delete();
		}
	}
}

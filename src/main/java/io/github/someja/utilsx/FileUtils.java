package io.github.someja.utilsx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

	public static void copyFile(File oldfile, String newFilePath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldfile.getPath()); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newFilePath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param dir
	 * @return
	 */
	public static final boolean createDir(String dir) {
		return createDir(dir, false);
	}
	
	/**
	 * 
	 * @param dir
	 * @param reverse 如果为true，则会创建父文件夹
	 * @return
	 */
	public static final boolean createDir(String dir, boolean reverse) {
		File f = new File(dir);
		if (reverse) {
			return f.mkdirs();
		}
		return f.mkdir();
	}

	/**
	 * isNew为true时，会先删除已有的目录，再创建
	 * @param dir
	 * @param isNew
	 * @param reverse
	 * @return
	 * @throws IOException 
	 */
	public static boolean createNewDir(String dir, boolean isNew, boolean reverse) throws IOException {
		File d = new File(dir);
		if (isNew && d.exists()) {
			if (d.isFile()) {
				d.delete();
			} else {
				// 目录
				org.apache.commons.io.FileUtils.deleteDirectory(d);
			}
		}
		
		if (reverse) {
			return d.mkdirs();
		}
		
		return d.mkdir();
	}
	
	public static final String combinePath(String ... paths) {
		return String.join(File.separator, paths);
	}
}

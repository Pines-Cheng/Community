package com.forex.util;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.forex.common.Constants;
import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageHelper {

	/**
	 * 直接指定压缩后的宽高： (先保存原文件，再压缩、上传) 一般项目中用于二维码压缩
	 * @param dir 图片所在目录
	 * @param oldFileName 要进行压缩的文件
	 * @param scale 压缩倍数
	 * @param quality 压缩质量
	 */
	public static void zipImageFile(File oldFile,
			File outputFile, int scale, float quality) {
		try {
			/** 对服务器上的临时文件进行处理 */
			Image srcFile = ImageIO.read(oldFile);
			int width = srcFile.getWidth(null);
			int height = srcFile.getHeight(null);
			/** 宽,高设定 */
			BufferedImage tag = new BufferedImage(width / scale, height / scale,
					BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(srcFile, 0, 0, width / scale, height / scale, null);
			/** 压缩之后临时存放位置 */
			FileOutputStream out = new FileOutputStream(outputFile);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);
			/** 压缩质量 */
			jep.setQuality(quality, true);
			encoder.encode(tag, jep);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void compressPic(File srcFile, File outputFile) {
		compressPic(srcFile, outputFile, Constants.ThumbnailHeight, 0.9d);
	}

	public static void compressPic(String dir, String oldFileName,
			String outputFileName, double comBase, double scale) {
		File srcFile = new java.io.File(dir, oldFileName);
		File outputFile = new File(dir, outputFileName);
		compressPic(srcFile, outputFile, comBase, scale);
	}

	/**
	 * 等比例压缩算法： 算法思想：根据压缩基数和压缩比来压缩原图，生产一张图片效果最接近原图的缩略图
	 * @param srcFile 原图 缩略图
	 * @param comBase 压缩基数
	 * @param scale 压缩限制(宽/高)比例 一般用1： 
	 * 当scale>=1,缩略图height=comBase,width按原图宽高比例;若scale<1,缩略图width=comBase,height按原图宽高比例
	 * @throws Exception
	 * @author shenbin
	 * @createTime 2014-12-16
	 * @lastModifyTime 2014-12-16
	 */
	public static void compressPic(File srcFile, File outputFile,
			double comBase, double scale) {
		try {
			Image src = ImageIO.read(srcFile);
			int srcHeight = src.getHeight(null);
			int srcWidth = src.getWidth(null);
			int deskHeight = 0;// 缩略图高
			int deskWidth = 0;// 缩略图宽
			double srcScale = (double) srcHeight / srcWidth;
			/** 缩略图宽高算法 */
			if ((double) srcHeight > comBase || (double) srcWidth > comBase) {
				if (srcScale >= scale || 1 / srcScale > scale) {
					if (srcScale >= scale) {
						deskHeight = (int) comBase;
						deskWidth = srcWidth * deskHeight / srcHeight;
					} else {
						deskWidth = (int) comBase;
						deskHeight = srcHeight * deskWidth / srcWidth;
					}
				} else {
					if ((double) srcHeight > comBase) {
						deskHeight = (int) comBase;
						deskWidth = srcWidth * deskHeight / srcHeight;
					} else {
						deskWidth = (int) comBase;
						deskHeight = srcHeight * deskWidth / srcWidth;
					}
				}
			} else {
				deskHeight = srcHeight;
				deskWidth = srcWidth;
			}
			BufferedImage tag = new BufferedImage(deskWidth, deskHeight,
					BufferedImage.TYPE_3BYTE_BGR);
			tag.getGraphics().drawImage(src, 0, 0, deskWidth, deskHeight, null); // 绘制缩小后的图
			FileOutputStream deskImage = new FileOutputStream(outputFile); // 输出到文件流
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(deskImage);
			encoder.encode(tag); // 近JPEG编码
			deskImage.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ImageFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 根据尺寸图片居中裁剪
	 */
	public static void cutCenterImage(String src, String dest, int w, int h) {
		try {
			Iterator<ImageReader> iterator = ImageIO
					.getImageReadersByFormatName("jpg");
			ImageReader reader = (ImageReader) iterator.next();
			InputStream in = new FileInputStream(src);
			ImageInputStream iis = ImageIO.createImageInputStream(in);
			reader.setInput(iis, true);
			ImageReadParam param = reader.getDefaultReadParam();
			int imageIndex = 0;
			
			int srcWidth = reader.getWidth(imageIndex);
			int srcHeight = reader.getHeight(imageIndex);
			
			if (srcWidth < w) {
				w = srcWidth;
			} 
			if (srcHeight < h) {
				h = srcHeight;
			}
			
			Rectangle rect = new Rectangle((srcWidth - w) / 2,
					(srcHeight - h) / 2, w, h);
			param.setSourceRegion(rect);
			BufferedImage bi = reader.read(0, param);
			ImageIO.write(bi, "jpg", new File(dest));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void cutCenterImage(String src, String dest) {
		cutCenterImage(new File(src), new File(dest));
	}
	
	public static void cutCenterImage(File src, File dest) {
		try {
			Iterator<ImageReader> iterator = ImageIO.getImageReadersByFormatName("jpg");
			ImageReader reader = (ImageReader) iterator.next();
			InputStream in = new FileInputStream(src);
			ImageInputStream iis = ImageIO.createImageInputStream(in);
			reader.setInput(iis, true);
			ImageReadParam param = reader.getDefaultReadParam();
			int imageIndex = 0;
			int srcWidth = reader.getWidth(imageIndex);
			int srcHeight = reader.getHeight(imageIndex);
			
			Rectangle rect = new Rectangle();
			if (srcWidth == srcHeight) {
				return; //长高相等不需要裁剪
			} else if (srcWidth > srcHeight) {
				rect.setFrame((srcWidth - srcHeight)/2, 0, srcHeight, srcHeight);
			} else {
				rect.setFrame(0, (srcHeight - srcWidth)/2, srcWidth, srcWidth);
			}
			param.setSourceRegion(rect);
			BufferedImage bi = reader.read(0, param);
			ImageIO.write(bi, "jpg", dest);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 图片裁剪二分之一
	 */
	public static void cutHalfImage(String src, String dest) throws IOException {
		Iterator<ImageReader> iterator = ImageIO
				.getImageReadersByFormatName("jpg");
		ImageReader reader = (ImageReader) iterator.next();
		InputStream in = new FileInputStream(src);
		ImageInputStream iis = ImageIO.createImageInputStream(in);
		reader.setInput(iis, true);
		ImageReadParam param = reader.getDefaultReadParam();
		int imageIndex = 0;
		int width = reader.getWidth(imageIndex) / 2;
		int height = reader.getHeight(imageIndex) / 2;
		Rectangle rect = new Rectangle(width / 2, height / 2, width, height);
		param.setSourceRegion(rect);
		BufferedImage bi = reader.read(0, param);
		ImageIO.write(bi, "jpg", new File(dest));
	}

	/*
	 * 图片裁剪通用接口
	 */
	public static void cutImage(String src, String dest, int x, int y, int w,
			int h) throws IOException {
		Iterator<ImageReader> iterator = ImageIO
				.getImageReadersByFormatName("jpg");
		ImageReader reader = (ImageReader) iterator.next();
		InputStream in = new FileInputStream(src);
		ImageInputStream iis = ImageIO.createImageInputStream(in);
		reader.setInput(iis, true);
		ImageReadParam param = reader.getDefaultReadParam();
		Rectangle rect = new Rectangle(x, y, w, h);
		param.setSourceRegion(rect);
		BufferedImage bi = reader.read(0, param);
		ImageIO.write(bi, "jpg", new File(dest));
	}

	/*
	 * 图片缩放
	 */
	public static void zoomImage(String src, String dest, int w, int h)
			throws Exception {
		double wr = 0, hr = 0;
		File srcFile = new File(src);
		File destFile = new File(dest);
		BufferedImage bufImg = ImageIO.read(srcFile);
		Image Itemp = bufImg.getScaledInstance(w, h, BufferedImage.SCALE_SMOOTH);
		wr = w * 1.0 / bufImg.getWidth();
		hr = h * 1.0 / bufImg.getHeight();
		AffineTransformOp ato = new AffineTransformOp(
				AffineTransform.getScaleInstance(wr, hr), null);
		Itemp = ato.filter(bufImg, null);
		try {
			ImageIO.write((BufferedImage) Itemp,
					dest.substring(dest.lastIndexOf(".") + 1), destFile);
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
	}
	
	public static void main(String args[]) throws Exception {
		File dir = new File("/Users/xinger/Desktop/icon");
		for (File file : dir.listFiles()) {
			System.out.println(file.getAbsolutePath());
			if (file.getName().endsWith("jpg")) {
				System.out.println(file.getName().replace("B", "S"));
				File thumb = new File(dir, file.getName().replace("B", "S"));
				compressPic(file, thumb);
			}
		}
		
//		File srcFile = new File("/Users/xinger/Downloads/image_test/1.jpg");
//		File outputFile = new File("/Users/xinger/Downloads/image_test/1_1.jpg");
	}

}
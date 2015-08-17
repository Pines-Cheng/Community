package com.forex.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.core.Controller;

public class ImageController extends Controller {

	// 验证码图片的宽度。
	private int width = 70;
	// 验证码图片的高度。
	private int height = 25;
	// 验证码字符个数
	private int codeCount = 4;

	private int x = 0;
	// 字体高度
	private int fontHeight;
	private int codeY;

	char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	
	public ImageController(){
		x = width / (codeCount + 1);
		fontHeight = height - 2;
		codeY = height - 4;
	}

	public void get() {
		//定义图像buffer
		BufferedImage buffImg = new BufferedImage(
		width, height,BufferedImage.TYPE_INT_RGB);
		Graphics2D g = buffImg.createGraphics();

		//创建一个随机数生成器类
		Random random = new Random();

		//将图像填充为白色
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);

		//创建字体，字体的大小应该根据图片的高度来定。
		Font font = new Font("宋体", Font.PLAIN, fontHeight);
		//设置字体。
		g.setFont(font);

		//画边框。
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, width - 1, height - 1);

		//随机产生160条干扰线，使图象中的认证码不易被其它程序探测到。
		g.setColor(Color.GRAY);
		for(int i = 0; i < 30; i++)
		{
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}

		//randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
		StringBuffer randomCode = new StringBuffer();
		int red = 0, green = 0, blue = 0;

		//随机产生codeCount数字的验证码。
		for (int i = 0; i < codeCount; i++) {
		//得到随机产生的验证码数字。
		String strRand = String.valueOf(codeSequence[random.nextInt(36)]);
		//产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
		red = random.nextInt(255);
		green = random.nextInt(255);
		blue = random.nextInt(255);

		//用随机产生的颜色将验证码绘制到图像中。
		g.setColor(new Color(red, green, blue));
		g.drawString(strRand, (i + 1) * x, codeY);

		//将产生的四个随机数组合在一起。
		randomCode.append(strRand);
		}
		// 将四位数字的验证码保存到Session中。
		setSessionAttr("validateCode", randomCode.toString());

		//HttpServletRequest resp = getRequest();
		HttpServletResponse resp = getResponse();
		// 禁止图像缓存。
		resp.setHeader("Pragma", "no-cache");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setDateHeader("Expires", 0);

		resp.setContentType("image/jpeg");

		//将图像输出到Servlet输出流中。
		try {
			ServletOutputStream sos = resp.getOutputStream();
			ImageIO.write(buffImg, "jpeg", sos);
			sos.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		renderNull();
	}
}

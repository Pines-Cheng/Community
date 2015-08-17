package com.zforex.test;

import org.junit.Test;

import com.forex.common.BaseConfig;
import com.jfinal.ext.test.ControllerTestCase;

public class ExceptionControllerTest extends ControllerTestCase<BaseConfig> {

	@Test
	public void testShow() {				//测试无效，得到的全为空
		System.out.println(use("/exception/show").invoke());
	}
}

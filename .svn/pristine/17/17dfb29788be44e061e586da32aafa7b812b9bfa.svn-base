package com.forex.common;

import java.util.HashMap;
import java.util.Map;

import com.forex.controller.Status;
import com.jfinal.ext.render.exception.ExceptionRender;
import com.jfinal.render.RenderFactory;

public class ExceptionHandlerRender extends ExceptionRender {
	
	private String errText;
	
	public ExceptionHandlerRender(String errText) {
		this.errText = errText;
	}

	@Override
	public void render() {
		System.out.println("The Render is called...");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("stat", Status.SERVER_EXCEPTION);
		params.put("errText", errText);
		getException().printStackTrace();
		RenderFactory.me().getJsonRender(params).setContext(request, response).render();
	}

}

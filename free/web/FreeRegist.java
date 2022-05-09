package com.study.free.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import com.study.common.vo.ResultMessageVO;
import com.study.exception.BizNotEffectedException;
import com.study.free.service.FreeBoardServiceImpl;
import com.study.free.service.IFreeBoardService;
import com.study.free.vo.FreeBoardVO;
import com.study.servlet.Handler;

public class FreeRegist implements Handler {
	
	

	@Override
	public String process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		FreeBoardVO freeBoard = new FreeBoardVO();
		BeanUtils.populate(freeBoard, req.getParameterMap());
		IFreeBoardService freeBoardService = new FreeBoardServiceImpl();
		ResultMessageVO resultMessageVO = new ResultMessageVO();
		try {
			freeBoardService.registBoard(freeBoard);
			resultMessageVO.messageSetting(true, "등록", "등록성공", "/free/freeList.wow", "목록으로");
		} catch (BizNotEffectedException bne) {
			resultMessageVO.messageSetting(false, "글 NotEffected", "업데이트에 실패했습니다.", "/free/freeList.wow", "목록으로");
		}
		req.setAttribute("resultMessageVO", resultMessageVO);
		return "common/message";
	}

}

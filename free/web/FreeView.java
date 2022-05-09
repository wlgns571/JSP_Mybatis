package com.study.free.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.study.common.vo.ResultMessageVO;
//import com.study.common.vo.ResultMessageVO;
import com.study.exception.BizNotEffectedException;
import com.study.exception.BizNotFoundException;
import com.study.free.service.FreeBoardServiceImpl;
import com.study.free.service.IFreeBoardService;
import com.study.free.vo.FreeBoardVO;
import com.study.servlet.Handler;

public class FreeView implements Handler {

	
	
	@Override
	public String process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		int boNo = Integer.parseInt(req.getParameter("boNo"));
		IFreeBoardService freeBoardService = new FreeBoardServiceImpl();
		try { 
			FreeBoardVO free = freeBoardService.getBoard(boNo);
			freeBoardService.increaseHit(boNo);
			req.setAttribute("free", free);
		} catch (BizNotFoundException bnf) {
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			resultMessageVO.messageSetting(false, "글 NotFound", "해당 글이 없습니다", "/free/freeList.wow", "목록으로");
			req.setAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		} catch (BizNotEffectedException bne) {
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			resultMessageVO.messageSetting(false, "글 NotEffected", "업데이트에 실패했습니다.", "/free/freeList.wow", "목록으로");
			req.setAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		}
		return "free/freeView";
	}
}

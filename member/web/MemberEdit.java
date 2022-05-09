package com.study.member.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.study.code.service.CommCodeServiceImpl;
import com.study.code.service.ICommCodeService;
import com.study.code.vo.CodeVO;
import com.study.common.vo.ResultMessageVO;
import com.study.exception.BizNotFoundException;
import com.study.member.service.IMemberService;
import com.study.member.service.MemberServiceImpl;
import com.study.member.vo.MemberVO;
import com.study.servlet.Handler;

public class MemberEdit implements Handler {

	@Override
	public String process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String memId = req.getParameter("memId");
		ICommCodeService codeSerivce = new CommCodeServiceImpl();
		IMemberService memberService = new MemberServiceImpl();
		List<CodeVO> jobList = codeSerivce.getCodeListByParent("JB00");
		req.setAttribute("jobList", jobList);
		List<CodeVO> hobbyList = codeSerivce.getCodeListByParent("HB00");
		req.setAttribute("hobbyList", hobbyList);
		try {
			MemberVO member = memberService.getMember(memId);
			req.setAttribute("member", member);
		} catch (BizNotFoundException bnf) {
			ResultMessageVO resultMessageVO = new ResultMessageVO();
			resultMessageVO.messageSetting(false, "글 NotFound", "해당 글이 없습니다", "/member/memberList.wow", "목록으로");
			req.setAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		}
		return "member/memberEdit";
	}
}

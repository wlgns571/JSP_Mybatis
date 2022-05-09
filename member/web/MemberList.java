package com.study.member.web;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import com.study.code.service.CommCodeServiceImpl;
import com.study.code.service.ICommCodeService;
import com.study.code.vo.CodeVO;
import com.study.member.service.IMemberService;
import com.study.member.service.MemberServiceImpl;
import com.study.member.vo.MemberSearchVO;
import com.study.member.vo.MemberVO;
import com.study.servlet.Handler;

public class MemberList implements Handler {

	
	
	@Override
	public String process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		MemberSearchVO searchVO = new MemberSearchVO();
		try {
			BeanUtils.populate(searchVO, req.getParameterMap());
		} catch (IllegalAccessException | InvocationTargetException e) {
			System.out.println("파라미터가 세팅이 되지 않음");
			e.printStackTrace();
		}
		req.setAttribute("searchVO", searchVO);
		IMemberService memberService = new MemberServiceImpl();
		List<MemberVO> memberList = memberService.getMemberList(searchVO);
		req.setAttribute("memberList", memberList);
		ICommCodeService codeSerivce = new CommCodeServiceImpl();
		List<CodeVO> jobList = codeSerivce.getCodeListByParent("JB00");
		req.setAttribute("jobList", jobList);
		List<CodeVO> hobbyList = codeSerivce.getCodeListByParent("HB00");
		req.setAttribute("hobbyList", hobbyList);
		return "member/memberList";
	}
}

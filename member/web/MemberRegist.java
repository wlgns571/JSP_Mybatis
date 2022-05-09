package com.study.member.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import com.study.code.service.CommCodeServiceImpl;
import com.study.code.service.ICommCodeService;
import com.study.common.vo.ResultMessageVO;
import com.study.exception.BizDuplicateKeyException;
import com.study.exception.BizNotEffectedException;
import com.study.member.service.IMemberService;
import com.study.member.service.MemberServiceImpl;
import com.study.member.vo.MemberVO;
import com.study.servlet.Handler;

public class MemberRegist implements Handler {


	@Override
	public String process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		MemberVO member = new MemberVO();
		BeanUtils.populate(member, req.getParameterMap());
		IMemberService memberService = new MemberServiceImpl();
		ResultMessageVO resultMessageVO = new ResultMessageVO();
		try {
			memberService.registMember(member);
			resultMessageVO.messageSetting(true, "등록", "등록성공", "/member/memberList.wow", "목록으로");
		} catch (BizNotEffectedException bne) {
			resultMessageVO.messageSetting(false, "글 NotEffected", "업데이트에 실패했습니다.", "/member/memberList.wow", "목록으로");
		} catch (BizDuplicateKeyException bde) {
			resultMessageVO.messageSetting(false, "아이디 중복", "아이디가 중복되었습니다..", "/member/memberList.wow", "목록으로");
		}
		req.setAttribute("resultMessageVO", resultMessageVO);
		return "common/message";
	}
}

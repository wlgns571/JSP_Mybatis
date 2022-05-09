package com.study.member.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.study.common.util.MybatisSqlSessionFactory;
import com.study.exception.BizDuplicateKeyException;
import com.study.exception.BizNotEffectedException;
import com.study.exception.BizNotFoundException;
import com.study.free.dao.IFreeBoardDao;
import com.study.member.dao.IMemberDao;
import com.study.member.dao.MemberDaoOracle;
import com.study.member.vo.MemberSearchVO;
import com.study.member.vo.MemberVO;

public class MemberServiceImpl implements IMemberService {

	SqlSessionFactory sqlSessionFactory = MybatisSqlSessionFactory.getSqlSessionFactory();

	@Override
	public List<MemberVO> getMemberList(MemberSearchVO searchVO) {
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			IMemberDao memberDao = session.getMapper(IMemberDao.class);
			int totalRowCount = memberDao.getTotalRowCount(searchVO);
			searchVO.setTotalRowCount(totalRowCount);
			searchVO.pageSetting();
			List<MemberVO> memberList = memberDao.getMemberList(searchVO);
			return memberList;
		}
	}

	@Override
	public MemberVO getMember(String memId) throws BizNotFoundException {
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			IMemberDao memberDao = session.getMapper(IMemberDao.class);
			MemberVO member = memberDao.getMember(memId);
			if (member == null)
				throw new BizNotFoundException();
			return member;
		}
	}

	@Override
	public void modifyMember(MemberVO member) throws BizNotEffectedException, BizNotFoundException {
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			IMemberDao memberDao = session.getMapper(IMemberDao.class);
			MemberVO vo = memberDao.getMember(member.getMemId());

			if (vo == null)
				throw new BizNotFoundException();
			int resultCnt = memberDao.updateMember(member);
			if (resultCnt == 0)
				throw new BizNotEffectedException();
		}
	}

	@Override
	public void removeMember(MemberVO member) throws BizNotEffectedException, BizNotFoundException {
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			IMemberDao memberDao = session.getMapper(IMemberDao.class);
			MemberVO vo = memberDao.getMember(member.getMemId());

			if (vo == null)
				throw new BizNotFoundException();
			int resultCnt = memberDao.deleteMember(member);
			if (resultCnt == 0)
				throw new BizNotEffectedException();
		}
	}

	@Override
	public void registMember(MemberVO member) throws BizNotEffectedException, BizDuplicateKeyException {
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			IMemberDao memberDao = session.getMapper(IMemberDao.class);
			MemberVO vo = memberDao.getMember(member.getMemId());
			if (vo != null) {
				throw new BizDuplicateKeyException();
			}
			if (vo == null) {
				int resultCnt = memberDao.insertMember(member);
				if (resultCnt == 0)
					throw new BizNotEffectedException();
			}
		}
	}
}

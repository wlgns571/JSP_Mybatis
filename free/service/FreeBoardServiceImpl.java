package com.study.free.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.study.common.util.MybatisSqlSessionFactory;
import com.study.exception.BizNotEffectedException;
import com.study.exception.BizNotFoundException;
import com.study.exception.BizPasswordNotMatchedException;
import com.study.free.dao.IFreeBoardDao;
import com.study.free.vo.FreeBoardSearchVO;
import com.study.free.vo.FreeBoardVO;

public class FreeBoardServiceImpl implements IFreeBoardService {
	// IFreeBoardService, FreeBoardServiceImpl
//	try (SqlSession session = sqlSessionFactory.openSession()) {
//		  BlogMapper mapper = session.getMapper(BlogMapper.class);
//		  Blog blog = mapper.selectBlog(101);
//		}
	SqlSessionFactory sqlSessionFactory = MybatisSqlSessionFactory.getSqlSessionFactory();

	@Override
	public List<FreeBoardVO> getBoardList(FreeBoardSearchVO searchVO) {
		// 밑의 메소드 실행전에 searchVO에 firstRow, LastRow가 세팅
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			IFreeBoardDao freeBoardDao = session.getMapper(IFreeBoardDao.class);
			int totalRowCount = freeBoardDao.getTotalRowCount(searchVO);
			searchVO.setTotalRowCount(totalRowCount);
			searchVO.pageSetting();
			List<FreeBoardVO> freeBoardList = freeBoardDao.getBoardList(searchVO);
			return freeBoardList;
		}
	}

	@Override
	public FreeBoardVO getBoard(int boNo) throws BizNotFoundException {
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			IFreeBoardDao freeBoardDao = session.getMapper(IFreeBoardDao.class);
			FreeBoardVO freeBoard = freeBoardDao.getBoard(boNo);
			if (freeBoard == null)
				throw new BizNotFoundException();
			return freeBoard;
		}
	}

	@Override
	public void increaseHit(int boNo) throws BizNotEffectedException {
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			IFreeBoardDao freeBoardDao = session.getMapper(IFreeBoardDao.class);
			int cnt = freeBoardDao.increaseHit(boNo);
			if (cnt == 0)
				throw new BizNotEffectedException();
		}
	}

	@Override
	public void modifyBoard(FreeBoardVO freeBoard)
			throws BizNotFoundException, BizPasswordNotMatchedException, BizNotEffectedException {
		// 사용자가 글 등록했을 때의 비밀번호랑 지금 수정하려는 사람이 입력한 비밀번호가 같으면... 같은사람이라 인식해서
		// 그 때만 수정 할 수 있게.
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			IFreeBoardDao freeBoardDao = session.getMapper(IFreeBoardDao.class);
			FreeBoardVO vo = freeBoardDao.getBoard(freeBoard.getBoNo());

			// 글 등록했을 때의 비밀번호를 가지고 있는 객체 = vo
			// 지금 수정하려는 사람이 입력한 비밀번호를 가지고 있는 객체 = freeBoard
			if (vo == null)
				throw new BizNotFoundException();
			if (!vo.getBoPass().equals(freeBoard.getBoPass()))
				throw new BizPasswordNotMatchedException();
			int resultCnt = freeBoardDao.updateBoard(freeBoard);
			if (resultCnt == 0)
				throw new BizNotEffectedException();
		}
	}

	@Override
	public void removeBoard(FreeBoardVO freeBoard)
			throws BizNotFoundException, BizPasswordNotMatchedException, BizNotEffectedException {
		// 비밀번호가 같으면 삭제, 아니면 삭제 못함
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			IFreeBoardDao freeBoardDao = session.getMapper(IFreeBoardDao.class);
			FreeBoardVO vo = freeBoardDao.getBoard(freeBoard.getBoNo());
			if (vo == null)
				throw new BizNotFoundException();
			if (!vo.getBoPass().equals(freeBoard.getBoPass()))
				throw new BizPasswordNotMatchedException();
			int resultCnt = freeBoardDao.deleteBoard(freeBoard);
			if (resultCnt == 0)
				throw new BizNotEffectedException();
		}
	}

	@Override
	public void registBoard(FreeBoardVO freeBoard) throws BizNotEffectedException {
		try (SqlSession session = sqlSessionFactory.openSession(true)) {
			IFreeBoardDao freeBoardDao = session.getMapper(IFreeBoardDao.class);
			int resultCnt = freeBoardDao.insertBoard(freeBoard);
			if (resultCnt == 0)
				throw new BizNotEffectedException();
		}
	}

}

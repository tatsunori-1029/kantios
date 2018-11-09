package app.saiou.kantios.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.saiou.kantios.auth.Authenticator.UserInfo;
import app.saiou.kantios.entity.TbMemo;
import app.saiou.kantios.mapper.TbMemoMapper;

public class MemoImpl implements Memo {
	
	private static Logger _logger = LoggerFactory.getLogger(MemoImpl.class);

	private static final DateTimeFormatter FORMATTER_FOR_TITLE = DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm:ss.SSS");

	private TbMemoMapper _mapper;
	
	public void setMapper(TbMemoMapper mapper) {
		_mapper = mapper;
	}

	public TbMemo find(String memoId, UserInfo userInfo) {
		return _mapper.selectByPrimaryKey(memoId);
	}

	public TbMemo create(TbMemo memo, UserInfo userInfo) {
		String memoId = UUID.randomUUID().toString();
		LocalDateTime now = LocalDateTime.now();
		String title = memo.getTitle().equals("")
				? new StringBuilder().append(FORMATTER_FOR_TITLE.format(now)).append(" CREATED.").toString()
				: memo.getTitle();
		String content = memo.getContent();

		TbMemo createdMemo = new TbMemo();
		createdMemo.setMemoId(memoId);
		createdMemo.setTitle(title);
		createdMemo.setContent(content);
		createdMemo.setCreateUserId(userInfo.getUserId());
		createdMemo.setUpdateUserId(userInfo.getUserId());
		int count = _mapper.insertSelective(createdMemo);
		_logger.debug("insert count [{}].", count);
		return createdMemo;
	}

	public TbMemo update(TbMemo memo, UserInfo userInfo) {
		memo.setUpdateUserId(userInfo.getUserId());
		int count = _mapper.updateByPrimaryKeyWithBLOBs(memo);
		_logger.debug("update count [{}].", count);
		return memo;
	}
	
	public void delete(String memoId, UserInfo userInfo) {
		int count = _mapper.deleteByPrimaryKey(memoId);
		_logger.debug("delete count [{}].", count);
	}
	
	public List<TbMemo> list(UserInfo userInfo) {
		TbMemo condition = new TbMemo();
		condition.setUpdateUserId(userInfo.getUserId());
		return _mapper.search(condition).stream()
				.sorted((memo1, memo2) -> {
					return memo2.getUpdateTimestamp().compareTo(memo1.getUpdateTimestamp());
				})
				.collect(Collectors.toList());
	}
	
}

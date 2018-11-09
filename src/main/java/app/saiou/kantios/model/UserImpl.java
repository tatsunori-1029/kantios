package app.saiou.kantios.model;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import app.saiou.kantios.entity.TbUser;
import app.saiou.kantios.mapper.TbUserMapper;

public class UserImpl implements User{

	private TbUserMapper _mapper;

	public void setMapper(TbUserMapper mapper) {
		_mapper = mapper;
	}

	public TbUser handle(String authUserId, String userName) {
		TbUser condition = new TbUser();
		condition.setAuthUserId(authUserId);
		List<TbUser> results = _mapper.search(condition);
		
		Optional<TbUser> latestOptional = results.stream()
				.reduce((tbUser1, tbUser2) -> tbUser1.getUpdateTimestamp().getTime() > tbUser2.getUpdateTimestamp().getTime() ? tbUser1 : tbUser2);

		TbUser latest = latestOptional.isPresent() ? latestOptional.get() : null;

		if (latest == null) {
			// 追加
			String uuid = UUID.randomUUID().toString();
			TbUser record = new TbUser();
			record.setUserId(uuid);
			record.setAuthUserId(authUserId);
			record.setDisplayName(userName);
			record.setCreateUserId(uuid);
			record.setUpdateUserId(uuid);
			_mapper.insert(record);
			latest = record;
		} else if (latest.getDisplayName().equals(userName) == false) {
			// 更新
			TbUser record = new TbUser();
			record.setUserId(latest.getUserId());
			record.setDisplayName(userName);
			_mapper.updateByPrimaryKeySelective(record);
			latest.setDisplayName(userName);
		}
		
		// 不要なレコードがあれば削除
		if (results.size() > 1) {
			String uuid = latest.getUserId();
			results.stream()
				.filter(record -> record.getUserId().equals(uuid) == false)
				.forEach(record -> _mapper.deleteByPrimaryKey(record.getUserId()));
		}
		
		return latest;
	}
}

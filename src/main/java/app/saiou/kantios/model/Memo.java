package app.saiou.kantios.model;

import java.util.List;

import app.saiou.kantios.auth.Authenticator.UserInfo;
import app.saiou.kantios.entity.TbMemo;

public interface Memo {

	TbMemo find(String memoId, UserInfo userInfo);
	
	TbMemo create(TbMemo memo, UserInfo userInfo);
	
	TbMemo update(TbMemo memo, UserInfo userInfo);

	void delete(String memoId, UserInfo userInfo);
	
	List<TbMemo> list(UserInfo userInfo);
}

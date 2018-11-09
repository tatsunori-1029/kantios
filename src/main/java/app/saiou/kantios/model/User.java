package app.saiou.kantios.model;

import app.saiou.kantios.entity.TbUser;

public interface User {

	TbUser handle(String authUserId, String userName);

}

package app.saiou.kantios.mapper;

import java.util.List;

import app.saiou.kantios.entity.TbUser;

public interface TbUserMapper {
    int deleteByPrimaryKey(String userId);

    int insert(TbUser record);

    int insertSelective(TbUser record);

    TbUser selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(TbUser record);

    int updateByPrimaryKey(TbUser record);
    
    List<TbUser> search(TbUser condition);
}
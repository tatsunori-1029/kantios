package app.saiou.kantios.mapper;

import java.util.List;

import app.saiou.kantios.entity.TbMemo;

public interface TbMemoMapper {
    int deleteByPrimaryKey(String memoId);

    int insert(TbMemo record);

    int insertSelective(TbMemo record);

    TbMemo selectByPrimaryKey(String memoId);

    int updateByPrimaryKeySelective(TbMemo record);

    int updateByPrimaryKeyWithBLOBs(TbMemo record);

    int updateByPrimaryKey(TbMemo record);
    
    List<TbMemo> search(TbMemo condition);
    
    List<TbMemo> selectAllRecords();
}
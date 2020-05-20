package cn.sk.mask.sys.mapper;

import cn.sk.mask.sys.pojo.SysRoleResource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysRoleResourceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleResource record);

    int insertSelective(SysRoleResource record);

    SysRoleResource selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleResource record);

    int updateByPrimaryKey(SysRoleResource record);

    //批量插入
    int batchInsert(List<SysRoleResource> records);
    //查询通过用户id
    List<SysRoleResource> selectByRoleId(Integer roleId);
    //硬删除
    int realDeleteInRoleId(@Param("roleId") Integer roleId);
}
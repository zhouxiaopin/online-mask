package cn.sk.mask.sys.mapper;

import cn.sk.mask.sys.pojo.SysUserRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysUserRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUserRole record);

    int insertSelective(SysUserRole record);

    SysUserRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUserRole record);

    int updateByPrimaryKey(SysUserRole record);


    //批量插入
    int batchInsert(List<SysUserRole> records);
    //查询通过用户id
    List<SysUserRole> selectByUserId(Integer userId);
    //硬删除
    int realDeleteInUserId(@Param("userId") Integer userId);
}
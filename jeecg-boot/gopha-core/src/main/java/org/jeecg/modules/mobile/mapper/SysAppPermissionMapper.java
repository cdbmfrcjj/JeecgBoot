package org.jeecg.modules.mobile.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.jeecg.modules.mobile.entity.SysAppPermission;
import org.jeecg.modules.mobile.model.TreeAppModel;

import java.util.List;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
public interface SysAppPermissionMapper extends BaseMapper<SysAppPermission> {
	/**
	   * 通过父菜单ID查询子菜单
	 * @param parentId
	 * @return
	 */
	public List<TreeAppModel> queryListByParentId(@Param("parentId") String parentId);
	
	/**
	 * 根据用户查询用户权限
     * @param userId 用户ID
     * @return List<SysAppPermission>
	 */
	public List<SysAppPermission> queryByUser(@Param("userId") String userId);
	
	/**
	 * 修改菜单状态字段： 是否子节点
     * @param id 菜单id
     * @param leaf 叶子节点
     * @return int
	 */
	@Update("update sys_permission set is_leaf=#{leaf} where id = #{id}")
	public int setMenuLeaf(@Param("id") String id,@Param("leaf") int leaf);

	/**
	 * 切换vue3菜单
	 */
	@Update("alter table sys_permission rename to sys_permission_v2")
	public void backupVue2Menu();
	@Update("alter table sys_permission_v3 rename to sys_permission")
	public void changeVue3Menu();
	
	/**
	 * 获取模糊匹配规则的数据权限URL
     * @return List<String>
	 */
	@Select("SELECT url FROM sys_permission WHERE del_flag = 0 and menu_type = 2 and url like '%*%'")
    public List<String> queryPermissionUrlWithStar();


	/**
	 * 根据用户账号查询菜单权限
	 * @param sysPermission
	 * @param username
	 * @return
	 */
	public int queryCountByUsername(@Param("username") String username, @Param("permission") SysAppPermission sysPermission);


	/**
	 * 查询部门权限数据
	 * @param departId
	 * @return
	 */
	List<SysAppPermission> queryDepartPermissionList(@Param("departId") String departId);

	/**
	 * 根据用户名称和test角色id查询权限
	 * @return
	 */
	@InterceptorIgnore(tenantLine = "true")
    List<SysAppPermission> queryPermissionByTestRoleId();
}

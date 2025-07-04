package org.jeecg.modules.mobile.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.mobile.entity.SysAppPermission;
import org.jeecg.modules.mobile.model.TreeAppModel;

import java.util.List;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
public interface ISysAppPermissionService extends IService<SysAppPermission> {
    /**
     * 切换vue3菜单
     */
	public void switchVue3Menu();
	
    /**
     * 通过父id查询菜单
     * @param parentId 父id
     * @return
     */
	public List<TreeAppModel> queryListByParentId(String parentId);
	
	/**
     * 真实删除
     * @param id 菜单id
     * @throws JeecgBootException
     */
	public void deletePermission(String id) throws JeecgBootException;
	/**
     * 逻辑删除
     * @param id 菜单id
     * @throws JeecgBootException
     */
	public void deletePermissionLogical(String id) throws JeecgBootException;

    /**
     * 添加菜单
     * @param sysPermission SysAppPermission对象
     * @throws JeecgBootException
     */
	public void addPermission(SysAppPermission sysPermission) throws JeecgBootException;

    /**
     * 编辑菜单
     * @param sysPermission SysAppPermission对象
     * @throws JeecgBootException
     */
	public void editPermission(SysAppPermission sysPermission) throws JeecgBootException;

    /**
     * 获取登录用户拥有的权限
     * @param username 用户名
     * @return
     */
	public List<SysAppPermission> queryByUser(String username);
	
	/**
	 * 根据permissionId删除其关联的SysAppPermissionDataRule表中的数据
	 * 
	 * @param id
	 * @return
	 */
	public void deletePermRuleByPermId(String id);
	
	/**
	  * 查询出带有特殊符号的菜单地址的集合
	 * @return
	 */
	public List<String> queryPermissionUrlWithStar();

	/**
	 * 判断用户否拥有权限
	 * @param username
	 * @param sysPermission
	 * @return
	 */
	public boolean hasPermission(String username, SysAppPermission sysPermission);

	/**
	 * 根据用户和请求地址判断是否有此权限
	 * @param username
	 * @param url
	 * @return
	 */
	public boolean hasPermission(String username, String url);

	/**
	 * 查询部门权限数据
	 * @param departId
	 * @return
	 */
	List<SysAppPermission> queryDepartPermissionList(String departId);

	/**
	 * 检测地址是否存在(聚合路由的情况下允许使用子菜单路径作为父菜单的路由地址)
	 * @param id
	 * @param url
	 * @param alwaysShow 是否是聚合路由
	 * @return
	 */
	 boolean checkPermDuplication(String id, String url,Boolean alwaysShow);
}

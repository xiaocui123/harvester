package com.harvester.manage.qvo;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author cui
 *
 */
public class MenuVO {
	private String uuid;

    private String name;

    private String url;

    private String iconCls;
    
    private Short sysflg;
    
    private List<MenuVO> menus;

    
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public Short getSysflg() {
		return sysflg;
	}

	public void setSysflg(Short sysflg) {
		this.sysflg = sysflg;
	}

	public List<MenuVO> getMenus() {
		if(menus==null){
			menus= Lists.newArrayList();
		}
		return menus;
	}

	public void setMenus(List<MenuVO> menus) {
		this.menus = menus;
	}
    
    
}

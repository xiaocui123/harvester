package com.harvester.manage.service;


import com.harvester.manage.pojo.DeptUserTreeVO;
import com.harvester.manage.pojo.SysDeptInfo;

import java.util.List;

/**
 * Created by cui on 2017/5/27.
 */
public interface SysDeptInfoService {

    int add(SysDeptInfo sysDeptInfo);

    int update(SysDeptInfo sysDeptInfo);

    int delete(String deptId);

    /**
     * 查询系统内部门人员树
     * @return
     */
    List<DeptUserTreeVO> queryDeptUser();

}

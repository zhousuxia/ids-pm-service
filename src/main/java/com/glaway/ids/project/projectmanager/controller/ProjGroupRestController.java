package com.glaway.ids.project.projectmanager.controller;

import io.swagger.annotations.*;
import com.glaway.foundation.common.dto.TSGroupDto;
import com.glaway.ids.project.projectmanager.service.ProjGroupServiceI;
import com.glaway.ids.project.projectmanager.service.ProjRoleServiceI;
import com.glaway.ids.project.projectmanager.service.ProjRolesServiceI;
import com.glaway.ids.project.projectmanager.vo.TeamVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by LHR on 2019/7/26.
 */
@Api(tags = {"项目团队接口"})
@RestController
@RequestMapping("/feign/projGroupRestController")
public class ProjGroupRestController {
    @Autowired
    private ProjGroupServiceI projGroupServiceI;
    @Autowired
    private ProjRoleServiceI projRoleServiceI;
}

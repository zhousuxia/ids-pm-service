package com.glaway.ids.project.projecttemplate.controller;

import io.swagger.annotations.*;
import com.alibaba.fastjson.JSON;
import com.glaway.foundation.cache.service.RedisService;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.dto.TSRoleDto;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.util.ResourceUtil;
import com.glaway.foundation.core.common.controller.BaseController;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.fdk.dev.dto.rep.RepRoleFileAuthDto;
import com.glaway.foundation.fdk.dev.service.threemember.FeignRoleService;
import com.glaway.ids.config.service.ParamSwitchServiceI;
import com.glaway.ids.config.service.PlanBusinessConfigServiceI;
import com.glaway.ids.project.plan.dto.PlanDto;
import com.glaway.ids.project.plan.entity.Plan;
import com.glaway.ids.project.plan.service.DeliverablesInfoServiceI;
import com.glaway.ids.project.plan.service.PlanServiceI;
import com.glaway.ids.project.projectmanager.service.ProjLibServiceI;
import com.glaway.ids.project.projectmanager.vo.ProjLibRoleFileAuthVo;
import com.glaway.ids.project.projecttemplate.service.ProjTemplateDetailServiceI;
import com.glaway.ids.project.projecttemplate.service.ProjTemplateLibServiceI;
import com.glaway.ids.project.projecttemplate.service.ProjTemplateRoleServiceI;
import com.glaway.ids.project.projecttemplate.service.ProjTemplateServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version V1.0
 * @Title: Controller
 * @Description: 项目模板详细
 * @date 2015-03-27 16:54:10
 */
@Api(tags = {"项目模板详情接口"})
@RestController
@RequestMapping("/feign/projTemplateDetailRestController")
public class ProjTemplateDetailRestController extends BaseController {
    /**
     * 项目模板计划Service
     */
    @Autowired
    private ProjTemplateDetailServiceI projTemplateDetailService;
    /**
     * sessionFacade
     */
    @Autowired
    private SessionFacade sessionFacade;
    @Autowired
    private ProjTemplateLibServiceI projTemplateLibService;
    /**
     * 交付项Service
     */
    @Autowired
    private DeliverablesInfoServiceI deliverablesInfoService;
    /**
     * 项目计划参数接口
     */
    @Autowired
    private ParamSwitchServiceI paramSwitchService;
    /**
     * 配置业务接口
     */
    @Autowired
    private PlanBusinessConfigServiceI businessConfigService;
    /**
     * 项目库Service
     */
    @Autowired
    private ProjLibServiceI projLibService;
    @Autowired
    private FeignRoleService roleService;
    /**
     * 计划Service
     */
    @Autowired
    private PlanServiceI planService;
    /**
     * redisService
     */
    @Autowired
    private RedisService redisService;
    @Autowired
    private ProjTemplateRoleServiceI projTemplateRoleService;
    /**
     * message
     */
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 项目模板Service
     */
    @Autowired
    private ProjTemplateServiceI projTemplateService;
    /**
     * 交付项Service
     */
    @Autowired
    private DeliverablesInfoServiceI deliverableService;

    @ApiOperation(value = "项目模板下计划列表查询,并转换为Plan",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projTemplateId", value = "项目模板id", dataType = "String")
    })
    @RequestMapping(value = "convertPlanjTemplateDetail2Plan")
    public List<PlanDto> convertPlanjTemplateDetail2Plan(@RequestParam("projTemplateId") String projTemplateId) {
        List<Plan> list = projTemplateDetailService.convertPlanjTemplateDetail2Plan(projTemplateId);
        List<PlanDto> dtoList = JSON.parseArray(JSON.toJSONString(list), PlanDto.class);
        return dtoList;
    }

    @ApiOperation(value = "根据项目模板Id查询项目模板的团队Id",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "projTemplateId", value = "项目模板id", dataType = "String")
    })
    @RequestMapping(value = "getTeamIdByTemplateId")
    public FeignJson getTeamIdByTemplateId(@RequestParam("projTemplateId") String projTemplateId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            String teamId = projTemplateRoleService.getTeamIdByTemplateId(projTemplateId);
            j.setObj(teamId);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "判断文件夹角色权限列表的长度和合法性",httpMethod = "GET")
    @RequestMapping(value = "judgeAndAddValidRoleAuthSize")
    public boolean judgeAndAddValidRoleAuthSize(@RequestBody List<RepRoleFileAuthDto> roleFileAuthsList) {
        boolean flag = true;
        flag = projTemplateLibService.judgeAndAddValidRoleAuthSize(roleFileAuthsList);
        return flag;
    }

    @ApiOperation(value = "文件夹角色权限列表转为对应的视图对象列表",httpMethod = "GET")
    @RequestMapping(value = "convertProjTemplateRoleFileAuthsVO")
    public List<ProjLibRoleFileAuthVo> convertProjTemplateRoleFileAuthsVO(@RequestBody List<RepRoleFileAuthDto> roleFileAuthsList) {
        List<ProjLibRoleFileAuthVo> voList = projTemplateLibService.convertProjTemplateRoleFileAuthsVO(roleFileAuthsList);
        return voList;
    }

    @ApiOperation(value = "项目库模板角色目录权限应用到项目库模板对应的项目库角色目录权限",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "templateId", value = "项目模板id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "libId", value = "项目库id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "orgId", value = "组织id", dataType = "String")
    })
    @RequestMapping(value = "applyTemplete")
    public FeignJson applyTemplete(@RequestParam(value = "templateId", required = false) String templateId, @RequestParam(value = "libId", required = false) String libId,
                                   @RequestBody TSUserDto userDto, @RequestParam(value = "orgId", required = false) String orgId) {
        FeignJson j = new FeignJson();
        j.setSuccess(true);
        try {
            String teamId = projTemplateRoleService.getTeamIdByTemplateId(templateId);
            List<TSRoleDto> list = roleService.getSysRoleListByTeamId(ResourceUtil.getApplicationInformation().getAppKey(), teamId);
            Map<String, List<RepRoleFileAuthDto>> map = projTemplateLibService.getTemplateRoleAuths(
                    templateId, libId, list);
            projTemplateLibService.applyTemplate(templateId, map, libId, userDto, orgId);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } finally {
            return j;
        }
    }

    @ApiOperation(value = "判断项目模板中是否存在重名文件夹",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "templateId", value = "项目模板id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "fileName", value = "文件夹名称", dataType = "String")
    })
    @RequestMapping(value = "isFileNameRepeat")
    public boolean isFileNameRepeat(@RequestParam(value = "templateId", required = false) String templateId, @RequestParam(value = "fileName", required = false) String fileName) {
        boolean flag = true;
        flag = projTemplateDetailService.isFileNameRepeat(templateId, fileName);
        return flag;
    }
}

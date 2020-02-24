/*
 * 文件名：ProjLibService.java
 * 版权：Copyright by www.glaway.com
 * 描述：项目库接口
 * 修改人：wangshen
 * 修改时间：2015年5月17日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.glaway.ids.project.projectmanager.service;


import com.glaway.foundation.businessobject.attribute.dto.EntityAttributeAdditionalAttributeDto;
import com.glaway.foundation.common.dto.TSRoleDto;
import com.glaway.foundation.common.dto.TSUserDto;
import com.glaway.foundation.common.entity.GLObject;
import com.glaway.foundation.common.vo.ConditionVO;
import com.glaway.foundation.core.common.hibernate.qbc.PageList;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.fdk.dev.dto.rep.RepFileAttachmentDto;
import com.glaway.foundation.fdk.dev.dto.rep.RepFileDto;
import com.glaway.foundation.fdk.dev.dto.rep.RepRoleFileAuthDto;
import com.glaway.ids.project.menu.entity.ProjTeamLink;
import com.glaway.ids.project.plan.entity.PlanLog;
import com.glaway.ids.project.projectmanager.entity.ProjDocRelation;
import com.glaway.ids.project.projectmanager.entity.ProjLibFile;
import com.glaway.ids.project.projectmanager.entity.ProjectLibAuthTemplateLink;
import com.glaway.ids.project.projectmanager.vo.ProjLibDocumentVo;
import com.glaway.ids.project.projectmanager.vo.ProjLibRoleFileAuthVo;
import com.glaway.ids.project.projectmanager.vo.RepFileAuthVo;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 * 
 * @author xxzhang
 * @version 2016年3月1日
 * @see ProjLibServiceI
 * @since
 */

public interface ProjLibServiceI {
    /**
     * 新建项目创建默认文件夹
     * @param projectId 项目id
     * @param fileName  项目名+项目编号
     * @param userId    用户id
     * @return
     */
    String cerateDefaultFolder(String projectId, String fileName,String userId);


    /**
     * 创建文件
     * @param projectId 项目id
     * @param parentId  父文件Id
     * @param fileName  文件名
     * @param type      文件类型，0：目录，1：文件
     * @param userId    用户id
     * @return
     */
    String createFile(String projectId, String parentId, String fileName, int type,String userId);


    /**
     * 根据引用文档的Id查询项目文档关系对象
     * @param quoteId  引用该文档的ID
     * @return
     * @see
     */
    String getDocRelation(String quoteId);

    /**
     * 根据引用文档的Id查询项目文档关系对象
     * @param quoteId  引用该文档的ID
     * @return
     * @see
     */
    List<ProjDocRelation> getDocRelationList(String quoteId);


    /**
     * 根据文件id获得vo对象
     * @param id  文档id
     * @return
     */
    ProjLibDocumentVo getProjDocmentVoById(String id);


    /**
     * repfile转成vo
     * @param repFile 项目库文档对象
     * @return
     * @see
     */
    ProjLibDocumentVo convertToVo(RepFileDto repFile);


    /**
     * Description: <br>获取文档路径
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param id 文档id
     * @return
     * @see
     */
    String getDocNamePath(String id);


    /**
     * 通过项目id获取最上层的id
     *
     * @param projectId 项目id
     * @return
     */
    String getFoldIdByProjectId(String projectId);


    /**
     *
     * 验证文档编号是否重复
     * @param docNumber  文档编号
     * @return
     * @see
     */
    boolean validateReptDocNum(String docNumber);

    /**
     * 保存文档
     * @param vo     项目库文档
     * @param userId 用户id
     * @return
     */
    String createFile(ProjLibDocumentVo vo,String userId);

    /**
     * 创建项目库文档
     * @param vo           项目库文档
     * @param currentUser  当前用户信息
     * @return
     */
    String createFile(ProjLibDocumentVo vo, TSUserDto currentUser);


    /**
     * Description: <br>增加附件
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param attachment 文件附件对象
     * @see
     */
    void addRepFileAttachment(RepFileAttachmentDto attachment);

    /**
     * Description: <br>更新附件
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param attachment 文件附件对象
     * @see
     */
    void updateFileAttachment(RepFileAttachmentDto attachment);

    /**
     * 查询项目库目录文件夹列表
     *
     * @param projectId     项目id
     * @param foldId        文件夹id
     * @param securityLevel 安全等级
     * @param isPage        是否分页
     * @param page          页码
     * @param rows          当前行数
     * @return
     * @see
     */
    List<ProjLibDocumentVo> getDocFoldDataPageForWeb(String projectId, String foldId,
                                                     Short securityLevel, boolean isPage,
                                                     int page, int rows);

    /**
     *
     * @param vo            项目库文档
     * @param users         中文名+英文名+用户id
     * @param securityLevel 安全等级
     * @return
     */
    String createFile1(ProjLibDocumentVo vo, String users,String securityLevel);

    /**
     * 获得项目的文件夹树
     * @param projectId 项目id
     * @param havePower 是否有权限
     * @param userId    用户id
     * @return
     */
    List<RepFileDto> getFolderTree(String projectId,String havePower,String userId);

    /**
     * 获得项目的文件夹树
     * @param projectId 项目id
     * @param havePower 是否有权限
     * @param userId    用户id
     * @return
     */
    List<RepFileDto> getFolderTreeForProjLib(String projectId,String havePower,String userId);

    /**
     * 判断权限
     * @param folderId  文档id
     * @param projectId 项目id
     * @param userId    用户id
     * @return
     */
    Boolean getPower(String folderId, String projectId,String userId);

    /**
     *
     * 查询项目库目录列表并转成vo
     * @param fileId    文档id
     * @param folderId  文件夹id
     * @param projectId 项目id
     * @return
     * @see
     */
    List<ProjLibDocumentVo> getRepList(String fileId,String folderId,String projectId, String userId);


    /**
     * Description: <br> 是否隐藏项目库文件夹树页面操作按钮
     *
     * @param projectId 项目id
     * @return
     * @see
     */
    boolean isHidProjLibOperForDir(String projectId);


    /**
     * Description: <br>根据文件id获得项目id
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param fileId 文件夹id
     * @return
     * @see
     */
    String getProjectIdByFileId(String fileId);


    /**
     * 查询项目库权限模板与项目关联对象
     * @param projectId 项目id
     * @return
     * @see
     */
    List<ProjectLibAuthTemplateLink> getProjectLibAuthTemplateLinkId(String projectId);


    /**
     * 校验同级的目录名是否已存在
     *
     * @param category 文件对象
     * @return
     */
    boolean checkCategoryNameExist(RepFileDto category);

    /**
     * 判断一个文件夹是否为根目录
     *
     * @param fileId 文档id
     * @return
     */
    boolean isRootFolder(String fileId);


    /**
     * Description: <br>删除Id文档和非空文件夹
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param fileId 文档id
     * @return
     * @see
     */
    boolean delFileById(String fileId);

    /**
     * 删除文档前校验是否可以删除
     * @param folderId 目标id
     * @return
     */
    FeignJson beforeDelFolder(String folderId);

    /**
     * Description: <br> 是否隐藏项目库文档页面操作按钮
     *
     * @param projectId 项目id
     * @return
     * @see
     */
    boolean isHidProjLibOper(String projectId);


    /**
     * Description: <br>根据文档ID获取该文档的所有关联对象
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param docId 文档ID
     * @return
     * @see
     */
    List<ProjDocRelation> getRelationDocByDocId(String docId);


    /**
     * Description: <br>批量删除文档
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param docVos 项目库文档对象集合
     * @see
     */
    void batchDel(List<ProjLibDocumentVo> docVos);

    /**
     * Description: <br>根据文件id 获取ProjLibFile
     * 1、…<br>传用户
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param fileId
     * @return
     * @see
     */
    List<ProjLibFile> getProjLibFile(String fileId);


    /**
     * 更新repfile路径
     * @param parentId   父文档id
     * @param repFileIds 文档id
     * @see
     */
    void updatePathRepFile(String parentId,String repFileIds);


    /**
     * 项目库文档列表查询方法
     * @param conditionList   查询条件
     * @param folderId        文件夹id
     * @param projectId       项目id
     * @param createName      创建者姓名
     * @param modifiName      修改者姓名
     * @param nameAndValueMap 软属性键值对
     * @param docTypeId       文档类型id
     * @param userId          用户id
     * @return
     */
    PageList queryEntity(List<ConditionVO> conditionList, String folderId, String projectId,
                         String createName, String modifiName, Map<String,String> nameAndValueMap, String docTypeId,String userId);


    /**绑定对象的软属性关系
     * @param repFile 文档对象
     * @param list    软属性集合
     * @see
     */
    void saveEntityAttrAdditionalAttribute(RepFileDto repFile, List<EntityAttributeAdditionalAttributeDto> list);

    /**
     * 保存软属性值
     * @param paramsMap 软属性键值对
     * @return
     */
    Map<String, String> saveEntityAttrAdditionalAttributeVal(Map<String, Object> paramsMap);


    /**
     * 修改并保存软属性数据
     * @param obj                                  文件对象
     * @param entityAttrName                       软属性名称
     * @param entityAttrVal                        软属性值
     * @param entityAttrAdditionalAttributeValMap  软熟悉键值对
     */
    void initEntityAttrAdditionalAttribute(GLObject obj, String entityAttrName, String entityAttrVal, Map<String, String> entityAttrAdditionalAttributeValMap);


    /**
     * 保存/更新软属性
     * @param obj       文件对象
     * @param attrNames 软属性名称集合
     * @param paramsMap 软熟悉键值对
     */
    void addOrUpdateEntityAttrAdditionalAttribute(GLObject obj,
                                                  Enumeration<String> attrNames,Map<String, Object> paramsMap);

    /**
     * 获取项目库目录角色权限
     *
     * @param fileId 文档id
     * @return
     */
    List<ProjLibRoleFileAuthVo> getProjLibRoleFileAuths(String fileId);

    /**
     * 判断目录角色权限是否变化
     *
     * @param fileId            文档id
     * @param repFileAuthVoList 库结构与库角色权限映射数据集合
     * @return
     */
    boolean checkRoleFileAuthExistChange(String fileId, List<RepFileAuthVo> repFileAuthVoList);

    /**
     * 保存项目库权目录角色权限
     * @param fileId            文档id
     * @param repFileAuthVoList 库结构与库角色权限映射数据集合
     * @param userId            用户id
     */
    void saveProjLibRoleFileAuth(String fileId, List<RepFileAuthVo> repFileAuthVoList, String userId);

    /**
     * 批量删除数据库附件
     *
     * @param ids 文件附件id集合，逗号隔开
     * @return
     * @see
     */
    void attachmentBatchDel(String ids);

    /**
     * 更新文档
     * @param vo     项目库文档
     * @param userId 用户id
     * @return
     */
    String updateFile(ProjLibDocumentVo vo,String userId);

    /**
     * 更新文件
     * @param method   标识
     * @param document 项目库文档对象
     * @param id       文件id
     * @param message  提示信息
     * @param userId   用户id
     * @return
     */
    String updateFile(String method, ProjLibDocumentVo document, String id,String message,String userId);

    /**
     * 更新流程变量及我的待办
     * @param newFiledId 文件id
     * @param procInstId 流程id
     * @param docName    文件名
     */
    void updateVariablesAndTodoTask(String newFiledId, String procInstId, String docName);


    /**
     * 保存项目日志
     * @param planLog 日志对象
     */
    void saveProjLog(PlanLog planLog);


    /**
     * Description: <br>根据bizid获得文件vo
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param bizId 版本id
     * @return
     * @see
     */
    List<ProjLibDocumentVo> getFilesByBizId(String bizId);

    /**
     * Description: <br>回退文件
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param id    文档id
     * @param bizId 版本id
     * @see
     */
    void backVersion(String id, String bizId);

    /**
     * Description: <br>回退文档版本
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param bizId 版本id
     * @see
     */
    void backToPrevDocVersion(String bizId);
    
    /**
     * 获取所有的项目文档关系
     * @return
     */
    List<ProjDocRelation> getAllDocRelationList();

    /**
     * 保存项目流程启动的实例信息
     * @param proInstanceId 流程id
     * @param id            项目id
     */
    void saveProcessProInstance(String proInstanceId, String id);

    /**
     * 保存项目流程id
     * @param projLibFile
     */
    void saverProcessInstanceId(ProjLibFile projLibFile);

    /**
     * Description: <br>删除文档引用关系接口
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param quoteId 交付项名称库id
     * @return
     * @see
     */
    boolean delDocRelation(String quoteId);

    /**
     * 删除文档引用关系接口
     * @param quoteId 交付项名称库id
     * @param docId   文档id
     * @return
     */
    boolean delDocRelation(String quoteId, String docId);

    /**
     * 增加文档引用关系接口
     * @param quoteId 交付项名称库id
     * @param docId   文档id
     * @return
     */
    boolean addDocRelation(String quoteId, String docId);

    /**
     * 项目文档启动审批流程
     *
     * @param params
     * @return
     */
    FeignJson submitProcess(Map<String,String> params);

    /**
     * 文档生命周期移到下一个状态
     * @param fileId 文档id
     * @param userId 用户id
     */
    void forward(String fileId,String userId);

    /**
     * 文档生命周期回退上一个状态
     * @param fileId 文档id
     */
    void backward(String fileId);

    /**
     * 通过deliverableId获取folderId
     * @param deliverableId 交付项id
     * @param projectId     项目id
     * @return
     */
    FeignJson getfolderIdByDeliverableId(String deliverableId, String projectId);

    /**
     * 查询子文件夹
     * @param fileId 文档id
     * @return
     * @see
     */
    List<RepFileDto> getImmediateChildrenFolders(String fileId);

    /**
     * Description: <br>交换两个文件的位置
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param srcId  文件夹id
     * @param destId 目标文件夹id
     * @see
     */
    void changeEachOtherForVo(String srcId, String destId);

    /**
     * Description: <br>左右移动更新文件夹的ordernumber和父节点
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param docId    文档id
     * @param parentId 父文件夹id
     * @see
     */
    void updateTreeOrderNum(String docId, String parentId);

    /**
     * 获取应用模板的路径权限信息
     *
     * @param projectId  项目id
     * @param templateId 模板id
     * @param roles      角色集合
     * @return
     */
    Map<String, List<RepRoleFileAuthDto>> getTemplateRoleAuths(String projectId, String templateId,
                                                               List<TSRoleDto> roles);

    /**
     * 模板应用
     *
     * @param projectId  项目id
     * @param map        文件附件集合
     * @param templateId 模板id
     * @return
     */
    void applyTemplate(String projectId, Map<String, List<RepRoleFileAuthDto>> map,String templateId,String userId,String orgId);

    /**
     * 获取项目文档树
     *
     * @param projectId 项目id
     * @return
     */
    Map<String, String> getPath(String projectId);

    /**
     * 获取项目库文档版本信息
     *
     * @param bizId    版本id
     * @param pageSize 页码
     * @param pageNum  每页数量
     * @param isPage   是否分页
     * @return
     */
    List<ProjLibDocumentVo> getVersionHistory(String bizId, Integer pageSize,
                                              Integer pageNum, boolean isPage);

    /**
     * 更新计划流程
     * @param fileId 文档id
     */
    void updatePlanProcess(String fileId);

}

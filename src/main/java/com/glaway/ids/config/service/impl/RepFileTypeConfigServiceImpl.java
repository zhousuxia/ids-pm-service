
package com.glaway.ids.config.service.impl;

import com.glaway.foundation.businessobject.attribute.dto.EntityAttributeAdditionalAttributeDto;
import com.glaway.foundation.common.dao.SessionFacade;
import com.glaway.foundation.common.log.BaseLogFactory;
import com.glaway.foundation.common.log.OperationLog;
import com.glaway.foundation.common.service.impl.CommonServiceImpl;
import com.glaway.foundation.common.util.CommonUtil;
import com.glaway.foundation.common.util.I18nUtil;
import com.glaway.foundation.fdk.dev.common.FeignJson;
import com.glaway.foundation.fdk.dev.dto.rep.RepFileTypeDto;
import com.glaway.foundation.fdk.dev.service.FeignAttributeService;
import com.glaway.foundation.fdk.dev.service.rep.FeignRepService;
import com.glaway.foundation.system.serial.dto.SerialNumberGeneratorInfoDto;
import com.glaway.foundation.system.serial.entity.SerialNumberGeneratorInfo;
import com.glaway.ids.common.pbmn.activity.entity.BpmnTask;
import com.glaway.ids.config.entity.RepFileTypeConfigDescLink;
import com.glaway.ids.config.service.RepFileTypeConfigServiceI;
import com.glaway.ids.config.vo.BpmnTaskVo;
import com.glaway.ids.config.vo.RepFileTypeConfigVo;
import com.glaway.ids.constant.RepFileTypeConfigConstants;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/*
 * 文件名：RepFileTypeConfigServiceImpl.java
 * 版权：Copyright by www.glaway.com
 * 描述：文档类型设置接口实现类
 * 修改人：zhousuxia
 * 修改时间：2018年7月26日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

@Service("repFileTypeConfigService")
@Transactional
public class RepFileTypeConfigServiceImpl extends CommonServiceImpl implements RepFileTypeConfigServiceI {

    private static final OperationLog log = BaseLogFactory.getOperationLog(RepFileTypeConfigServiceImpl.class);

    @Autowired
    private FeignRepService repService;

    @Autowired
    private SessionFacade sessionFacade;

    @Autowired
    private FeignAttributeService attributeService;

    @Value(value="${spring.application.name}")
    private String appKey;


    /**
     *
     * @param parentId
     * @param fileTypeCode
     * @param fileTypeName
     * @param entrance
     * @param docTypeId
     * @return
     */
    @Override
    public List<RepFileTypeDto> getRepFileTypeConfigList(String parentId, String fileTypeCode, String fileTypeName, String entrance, String docTypeId) {
        RepFileTypeDto dto = new RepFileTypeDto();
        dto.setFileTypeName(fileTypeName);
        dto.setFileTypeCode(fileTypeCode);
        dto.setParentId(parentId);
        List<RepFileTypeDto> repFileTypeList = repService.getRepFileTypeConfigList(appKey,entrance,docTypeId,dto);
        return repFileTypeList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean checkFileTypeCodeBeforeSave(String parentId, String repFileTypeId,String fileTypeCode) {
        boolean flag = true;
        List<RepFileTypeDto> fileTypeList = repService.getRepFileTypeByParentIdAndTypeCode(appKey,fileTypeCode,parentId);
        if(!CommonUtil.isEmpty(fileTypeList)){
            if(!CommonUtil.isEmpty(repFileTypeId) && repFileTypeId.equals(fileTypeList.get(0).getId())){
                flag = true;
            }else{
                flag = false;
            }
        }
        return flag;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RepFileTypeConfigDescLink> queryDescLinkByRepFileTypeId(String repFileTypeId) {
        List<RepFileTypeConfigDescLink> linkList = sessionFacade.findHql("from RepFileTypeConfigDescLink where repFileTypeId = ? order by createTime desc", repFileTypeId);
        return linkList;
    }

    @Override
    public void saveRepFileTypeConfigDescLink(RepFileTypeConfigDescLink link) {
        if(!CommonUtil.isEmpty(link.getId())){
            update(link);
        }else{
            CommonUtil.glObjectSet(link);
            save(link);
        }
    }

    @Override
    public FeignJson saveRepFileTypeConfig(String repFileTypeId, String fileTypeCode, String fileTypeName,
                                           String generatorInfoId, String description, String userId) {
        FeignJson j = new FeignJson();
        try{

            RepFileTypeDto fileType = new RepFileTypeDto();
            if(CommonUtil.isEmpty(repFileTypeId)){
                fileType.setId(repFileTypeId);

            }else{
                fileType = repService.getRepFileTypeById(appKey,repFileTypeId);
            }
            fileType.setFileTypeCode(fileTypeCode);
            fileType.setFileTypeName(fileTypeName);
            fileType.setGenerateRuleId(generatorInfoId);
            fileType.setDescription(description);
            if(CommonUtil.isEmpty(repFileTypeId)){
                repService.saveRepFileTypeConfig(appKey,userId,fileType);
            }else{
                repService.updateRepFileType(appKey,fileType);
            }


            if(CommonUtil.isEmpty(repFileTypeId)){
                log.info("文档类型新增成功");
                j.setMsg(I18nUtil.getValue("com.glaway.ids.pm.config.repFileTypeConfig.addSuccess"));
            }else{
                log.info("文档类型修改成功");
                j.setMsg(I18nUtil.getValue("com.glaway.ids.pm.config.repFileTypeConfig.updateSuccess"));
            }

        }catch(Exception e){
            j.setSuccess(false);
            if(CommonUtil.isEmpty(repFileTypeId)){
                log.info("文档类型新增失败");
                j.setMsg(I18nUtil.getValue("com.glaway.ids.pm.config.repFileTypeConfig.addFailure"));
            }else{
                log.info("文档类型修改失败");
                j.setMsg(I18nUtil.getValue("com.glaway.ids.pm.config.repFileTypeConfig.updateFailure"));
            }

        }finally{
            return j;
        }
    }

    @Override
    public void deleteRepFileTypeConfig(String ids) {
        if(!CommonUtil.isEmpty(ids)){
            for(String id : ids.split(",")){
                repService.deleteRepFileTypeConfig(appKey,id);
            }
        }
    }

    @Override
    public void changeRepFileTypeStatus(String type, String ids) {
        if(type.equals("enable")){   //启用数据
            if(!CommonUtil.isEmpty(ids)){
                for(String id : ids.split(",")){
                    RepFileTypeDto fileType = repService.getRepFileTypeById(appKey,id);
                    if(!CommonUtil.isEmpty(fileType)){
                        fileType.setStatus("1");
                        repService.updateRepFileType(appKey,fileType);
                    }
                }
            }
        }else if(type.equals("disable")){   //禁用数据
            if(!CommonUtil.isEmpty(ids)){
                for(String id : ids.split(",")){
                    RepFileTypeDto fileType = repService.getRepFileTypeById(appKey,id);
                    if(!CommonUtil.isEmpty(fileType)){
                        fileType.setStatus("0");
                        repService.updateRepFileType(appKey,fileType);
                    }
                }
            }
        }

    }

    @Override
    public Map<String, String> getCustomAttrMap(Map<String, String> map, String entityUri) {
        StringBuffer hqlBuffer = new StringBuffer();
        hqlBuffer.append("select eaa.entityattrname ID, wm_concat(a.title) ATTTNAME from Entity_Attr_Additional_Attr eaa ");
        hqlBuffer.append("left join additional_Attribute a on eaa.addattrid = a.id ");
        hqlBuffer.append("where eaa.entityuri = ?  group by eaa.entityattrname");
        List<Map<String, Object>> objArrayList = sessionFacade.findForJdbc(hqlBuffer.toString(), new Object[]{entityUri});
        if(!CommonUtil.isEmpty(objArrayList)) {
            for(Map<String, Object> tempMap : objArrayList) {
                map.put(tempMap.get("ID").toString(), tempMap.get("ATTTNAME").toString());
            }
        }
        return map;
    }

    @Override
    public void getRuleAndRemarkMap(Map<String, SerialNumberGeneratorInfo> idRuleDescMap,
                                    Map<String, String> idRemarkMap) {
        String hql = "from SerialNumberGeneratorInfo";
        List<SerialNumberGeneratorInfo> generatorList = sessionFacade.findHql(hql);
        if(!CommonUtil.isEmpty(generatorList)) {
            for(SerialNumberGeneratorInfo info : generatorList) {
                idRuleDescMap.put(info.getId(), info);
            }
        }
        hql = "from RepFileTypeConfigDescLink";
        List<RepFileTypeConfigDescLink> descList = sessionFacade.findHql(hql);
        if(!CommonUtil.isEmpty(descList)) {
            for(RepFileTypeConfigDescLink link : descList) {
                idRemarkMap.put(link.getRepFileTypeId(), link.getDescription());
            }
        }
    }

    @Override
    public HSSFWorkbook export(List<RepFileTypeConfigVo> list, int max) {
        if(CommonUtil.isEmpty(list)) {
            return null;
        }
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        //第一行表头字段
        List<String> headList0 = new LinkedList<String>();
        List<String> headList1 = new LinkedList<String>();
        List<String> mergeList = new ArrayList<String>();

        headList0.add(RepFileTypeConfigConstants.CODE);
        headList0.add(RepFileTypeConfigConstants.NAME);
        headList0.add(RepFileTypeConfigConstants.GENERATE_RULE_NAME);
        headList0.add(RepFileTypeConfigConstants.GENERATE_RULE_DESC);
        headList1.add("");
        headList1.add("");
        headList1.add("");
        headList1.add("");
        mergeList.add("0,1,0,0");
        mergeList.add("0,1,1,1");
        mergeList.add("0,1,2,2");
        mergeList.add("0,1,3,3");

        for(int i = 0 ; i < max; i++) {
            headList0.add(RepFileTypeConfigConstants.APPROVE_PROCESS+ (i+1));
            headList0.add("");
            headList0.add("");
            headList0.add("");
            headList0.add("");

            headList1.add(RepFileTypeConfigConstants.NAME);
            headList1.add(RepFileTypeConfigConstants.APPROVE_ROLE);
            headList1.add(RepFileTypeConfigConstants.APPROVE_WAY);
            headList1.add(RepFileTypeConfigConstants.APPROVE_SELECT_NUMBER);
            headList1.add(RepFileTypeConfigConstants.REMARK);

            int num = 4 + i * 5;
            mergeList.add("0,0," + num + "," + (num + 4));
        }

        headList0.add(RepFileTypeConfigConstants.CUSTOMATTR);
        headList0.add(RepFileTypeConfigConstants.STATUS);
        headList0.add(RepFileTypeConfigConstants.REMARK);
        headList1.add("");
        headList1.add("");
        headList1.add("");
        String[] excelHeader0 = new String[headList0.size()];
        headList0.toArray(excelHeader0);

        //第二行表头字段
        String[] excelHeader1 = new String[headList1.size()];
        headList1.toArray(excelHeader1);

        int length3 = excelHeader0.length -3;
        int length2 = excelHeader0.length -2;
        int length = excelHeader0.length -1;
        mergeList.add("0,1,"+ length3 + "," + length3);
        mergeList.add("0,1,"+ length2 + "," + length2);
        mergeList.add("0,1,"+ length + "," + length);
        String[] mergeNum= new String[mergeList.size()];
        mergeList.toArray(mergeNum);

        //生成一个表格
        HSSFSheet sheet = workbook.createSheet(RepFileTypeConfigConstants.EXPORT_TITLE);
        // 设置表格默认列宽度
        sheet.setDefaultColumnWidth(20);
        sheet.setColumnWidth(excelHeader0.length, 11000);
        //生成样式
        HSSFCellStyle headerStyle = createHeaderStyle(workbook);
        HSSFCellStyle dataStyle = createDataStyle(workbook);
        //生成表格的第一行
        HSSFRow row = sheet.createRow(0);
        for(int i = 0; i < excelHeader0.length; i++) {
//            sheet.autoSizeColumn(i, true);
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(excelHeader0[i]);
            cell.setCellStyle(headerStyle);
        }

        //动态合并单元格
        for(int i = 0; i < mergeNum.length; i++) {
//            sheet.autoSizeColumn(i, true);
            String[] temp = mergeNum[i].split(",");
            Integer startrow = Integer.parseInt(temp[0]);
            Integer overrow = Integer.parseInt(temp[1]);
            Integer startcol = Integer.parseInt(temp[2]);
            Integer overcol = Integer.parseInt(temp[3]);
            sheet.addMergedRegion(new CellRangeAddress(startrow, overrow, startcol, overcol));

        }

        //第二行表头
        row = sheet.createRow(1);
        for(int i = 0; i < excelHeader1.length; i++) {
//            sheet.autoSizeColumn(i, true);
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(excelHeader1[i]);
            cell.setCellStyle(headerStyle);
        }

        //生成表格数据
        if(!CommonUtil.isEmpty(list)) {

            for(int i =0; i < list.size(); i++) {
                row = sheet.createRow(i + 2);
                RepFileTypeConfigVo vo = list.get(i);

                //导入对应列数据
                HSSFCell cell = row.createCell(0);
                cell.setCellValue(vo.getCode());
                cell.setCellStyle(dataStyle);

                HSSFCell cell1 = row.createCell(1);
                cell1.setCellValue(vo.getName());
                cell1.setCellStyle(dataStyle);

                HSSFCell cell2 = row.createCell(2);
                cell2.setCellValue(vo.getGenerateRuleName());
                cell2.setCellStyle(dataStyle);

                HSSFCell cell3 = row.createCell(3);
                cell3.setCellValue(vo.getGenerateRuleDesc());
                cell3.setCellStyle(dataStyle);

                //写入审批环节
                List<BpmnTaskVo> approveList = vo.getList();
                int startCol = 4;
                if(!CommonUtil.isEmpty(approveList)) {
                    for(BpmnTaskVo bt : approveList) {
                        HSSFCell cell4 = row.createCell(startCol);
                        cell4.setCellValue(bt.getName());
                        cell4.setCellStyle(dataStyle);
                        startCol++;
                        HSSFCell cell5 = row.createCell(startCol);
                        cell5.setCellValue(bt.getRoles());
                        cell5.setCellStyle(dataStyle);
                        startCol++;
                        HSSFCell cell6 = row.createCell(startCol);
                        cell6.setCellValue(bt.getApproveTypeName());
                        cell6.setCellStyle(dataStyle);
                        startCol++;
                        HSSFCell cell7 = row.createCell(startCol);
                        cell7.setCellValue(bt.getNumbers());
                        cell7.setCellStyle(dataStyle);
                        startCol++;
                        HSSFCell cell8 = row.createCell(startCol);
                        cell8.setCellValue(bt.getRemark());
                        cell8.setCellStyle(dataStyle);
                        startCol++;
                    }
                }

                for(int j = startCol; j < length3; j++) {
                    HSSFCell cellBlank = row.createCell(j);
                    cellBlank.setCellValue("");
                    cellBlank.setCellStyle(dataStyle);
                }

                HSSFCell lastCell3 = row.createCell(length3);
                lastCell3.setCellValue(vo.getCustomAttr());
                lastCell3.setCellStyle(dataStyle);

                HSSFCell lastCell2 = row.createCell(length2);
                lastCell2.setCellValue(vo.getStatus());
                lastCell2.setCellStyle(dataStyle);

                HSSFCell lastCell = row.createCell(length);
                lastCell.setCellValue(vo.getRemark());
                lastCell.setCellStyle(dataStyle);

            }
        }
        return workbook;
    }

    /**
     * 生成表头样式
     *
     * @param workbook
     * @return
     */
    private HSSFCellStyle createHeaderStyle(HSSFWorkbook workbook) {
        // 设置表头字体 - 宋体、不加粗、12
        HSSFFont headerFont = generateHSSFFont(workbook, "宋体", false, 12);
        // 设置表头样式 - 居中、边框宽度1
        HSSFCellStyle headerStyle = generateHSSFCellStyle(workbook, headerFont,
                HSSFCellStyle.ALIGN_CENTER, new int[] {1, 1, 1, 1});

        // 只用前景色填充
        headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        // 自定义前景色索引
        HSSFPalette palette = workbook.getCustomPalette();
        palette.setColorAtIndex((short)9, (byte)217, (byte)217, (byte)217);

        // 设置前景色(使用上面定义的颜色)
        headerStyle.setFillForegroundColor((short)9);

        return headerStyle;
    }

    /**
     * 生成普通数据样式
     *
     * @param workbook
     * @return
     */
    private HSSFCellStyle createDataStyle(HSSFWorkbook workbook) {
        // 设置数据字体 - 宋体、不加粗、11
        HSSFFont dataFont = generateHSSFFont(workbook, "宋体", false, 11);
        // 设置数据样式 - 居中、边框宽度1
        HSSFCellStyle dataStyle = generateHSSFCellStyle(workbook, dataFont,
                HSSFCellStyle.ALIGN_CENTER, new int[] {1, 1, 1, 1});
        // 设置内容自动换行
        // dataStyle.setWrapText(true);

        return dataStyle;
    }

    /**
     * 创建简单HSSFFont的方法
     *
     * @param workbook
     * @param fontName
     *            设置字体
     * @param isBold
     *            设置是否加粗
     * @param fontSize
     *            设置字号
     * @return
     */
    private HSSFFont generateHSSFFont(HSSFWorkbook workbook, String fontName, boolean isBold, int fontSize) {
        HSSFFont font = workbook.createFont();
        font.setFontName(fontName);
        font.setBoldweight((short)(isBold ? 600 : 1));
        font.setFontHeightInPoints((short)fontSize);
        return font;
    }

    /**
     * 创建简单HSSFCellStyle的方法
     *
     * @param workbook
     * @param font
     *            字体
     * @param alignment
     *            对齐方式: 1-左对齐, 2-居中, 3-右对齐
     * @param border
     *            上右下左 - 边框宽度
     * @return
     */
    private HSSFCellStyle generateHSSFCellStyle(HSSFWorkbook workbook, HSSFFont font, int alignment, int[] border) {
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment((short)alignment);
        cellStyle.setBorderTop((short)border[0]);
        cellStyle.setBorderRight((short)border[1]);
        cellStyle.setBorderBottom((short)border[2]);
        cellStyle.setBorderLeft((short)border[3]);
        cellStyle.setFont(font);
        return cellStyle;
    }

    @Override
    public FeignJson editEntityAttributeAdditionalAttribute(String oldId, List<EntityAttributeAdditionalAttributeDto> entityAttributeAdditionalAttributeList) {
        return attributeService.editEntityAttributeAdditionalAttribute(appKey,oldId,entityAttributeAdditionalAttributeList);
    }

    @Override
    public List<BpmnTask> getBpmnTaskList(String originId) {
        List<BpmnTask> list = sessionFacade.findHql("from BpmnTask where avaliable='1' and originid=?",originId);
        return list;
    }
}

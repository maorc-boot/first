package com.asiainfo.biapp.pec.element.jx.service.impl;

import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import com.asiainfo.biapp.pec.element.jx.entity.McdPlanExclus;
import com.asiainfo.biapp.pec.element.jx.entity.McdPlanExclusivity;
import com.asiainfo.biapp.pec.element.jx.mapper.McdPlanDefMapper;
import com.asiainfo.biapp.pec.element.jx.mapper.McdPlanExclusivityMapper;
import com.asiainfo.biapp.pec.element.jx.mapper.McdPlanGroupMapper;
import com.asiainfo.biapp.pec.element.jx.model.McdPlanDef;
import com.asiainfo.biapp.pec.element.jx.model.McdPlanGroup;
import com.asiainfo.biapp.pec.element.jx.query.PlanExcluQuery;
import com.asiainfo.biapp.pec.element.jx.service.McdPlanExclusivityService;
import com.asiainfo.biapp.pec.element.jx.utils.ExcelUtils;
import com.asiainfo.biapp.pec.element.util.CommonUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * <p>
 * 产品互斥关系表 服务实现类
 * </p>
 *
 * @author mamp
 * @since 2022-10-18
 */
@Slf4j
@Service
public class McdPlanExclusivityServiceImpl extends ServiceImpl<McdPlanExclusivityMapper, McdPlanExclusivity> implements McdPlanExclusivityService {

    private static final String[] TITLE = {"NO(序号)", "*主产品Id(多个用英文逗号隔开)", "*互斥产品Id(多个用英文逗号隔开)"};

    @Resource
    private McdPlanExclusivityMapper planExclusivityMapper;

    @Resource
    private McdPlanDefMapper mcdPlanDefMapper;

    @Resource
    private McdPlanGroupMapper mcdPlanGroupMapper;

    @Override
    public IPage<McdPlanExclusivity> getPlanExclusivityList(McdPageQuery excluQuery) {
        Page  pager = new Page();
        pager.setCurrent(excluQuery.getCurrent());
        pager.setSize(excluQuery.getSize());

        LambdaQueryWrapper<McdPlanExclusivity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(McdPlanExclusivity::getSourceType,0)
                .eq(McdPlanExclusivity::getType,1)
                .like(!excluQuery.getKeyWords().isEmpty(),McdPlanExclusivity::getPlanId,excluQuery.getKeyWords()).or()
                .like(!excluQuery.getKeyWords().isEmpty(),McdPlanExclusivity::getPlanName,excluQuery.getKeyWords())
                .orderByDesc(McdPlanExclusivity::getPlanGroupId);

        IPage<McdPlanExclusivity> mcdPlanExclusivityIPage = planExclusivityMapper.selectPage(pager,queryWrapper);
        mcdPlanExclusivityIPage.getRecords().forEach(record->{
            LambdaQueryWrapper<McdPlanExclusivity>  exclusWrapper = new LambdaQueryWrapper<>();
            exclusWrapper.eq(McdPlanExclusivity::getPlanGroupId,record.getPlanGroupId())
                    .eq(McdPlanExclusivity::getSourceType,0)
                    .eq(McdPlanExclusivity::getType,0);

            List<McdPlanExclusivity>  planExclusivityList = planExclusivityMapper.queryExclusByPlanId(record.getPlanId());
            List<String> exPlanIds = new ArrayList<>();
            List<String> exPlanNames = new ArrayList<>();

            for (McdPlanExclusivity exPlan:planExclusivityList) {
                exPlanIds.add(exPlan.getPlanId());
                exPlanNames.add(exPlan.getPlanName());
            }
            record.setExPlanId(StringUtils.join(exPlanIds,","));
            record.setExPlanName(StringUtils.join(exPlanNames,","));

        });

        return mcdPlanExclusivityIPage;
    }


    @Override
    public McdPlanExclus getPlanExclusivityInfo(PlanExcluQuery excluQuery) {

        LambdaQueryWrapper<McdPlanExclusivity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(McdPlanExclusivity::getPlanId,excluQuery.getPlanId())
                    .eq(McdPlanExclusivity::getSourceType,0)
                    .eq(McdPlanExclusivity::getType,1);

        McdPlanExclusivity mcdPlanExclusivity = planExclusivityMapper.selectOne(queryWrapper);
        if (Objects.isNull(mcdPlanExclusivity)){
            return null;
        }

        LambdaQueryWrapper<McdPlanExclusivity>  exclusWrapper = new LambdaQueryWrapper<>();
        exclusWrapper.eq(McdPlanExclusivity::getPlanGroupId,mcdPlanExclusivity.getPlanGroupId())
                     .eq(McdPlanExclusivity::getSourceType,excluQuery.getSourceType())
                     .eq(McdPlanExclusivity::getType,0);

         List<McdPlanExclusivity> planExclusivityList = planExclusivityMapper.selectList(exclusWrapper);
       // List<McdPlanExclusivity>  planExclusivityList = planExclusivityMapper.queryExclusByPlanId(excluQuery.getPlanId());


        McdPlanExclus planExclus = new McdPlanExclus();
        planExclus.setPlanId(mcdPlanExclusivity.getPlanId());
        planExclus.setPlanName(mcdPlanExclusivity.getPlanName());
        planExclus.setPlanGroupId(mcdPlanExclusivity.getPlanGroupId());
        planExclus.setSourceType(mcdPlanExclusivity.getSourceType());
        planExclus.setType(mcdPlanExclusivity.getType());
        planExclus.setPlanExcluList(planExclusivityList);

        return planExclus;



    }

    @Override
    public void saveOrUpdatePlanExclusivity(McdPlanExclusivity exclusivity) {
        planExclusivityMapper.insert(exclusivity);
    }


    /**
     * 互斥产品模板下载
     *
     * @param response 响应
     */
    @Override
    public void exportPlanExcluTemplate(HttpServletResponse response) {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();
        //sheet.createFreezePane(8,100);// 冻结
        HSSFRow row = sheet.createRow((int) 0);
        HSSFCellStyle style = wb.createCellStyle();
        HSSFDataFormat format = wb.createDataFormat();
        style.setDataFormat(format.getFormat("@"));
        style.setAlignment(HorizontalAlignment.CENTER);
        //设置列的样式 和 自动列宽
        for(int i = 0; i < TITLE.length ; i++) {
            sheet.setDefaultColumnStyle(i,style);
            sheet.autoSizeColumn(i);
            int maxWith = 255 * 256;
            int newWidth = sheet.getColumnWidth(i) * 17 / 10;
            if (newWidth < maxWith) {
                sheet.setColumnWidth(i, newWidth);
            }else {
                sheet.setColumnWidth(i, maxWith / 2);
            }
        }

        //字体样式
        HSSFFont columnHeadFont = wb.createFont();
        columnHeadFont.setFontName("宋体");
        columnHeadFont.setFontHeightInPoints((short) 12);
        //columnHeadFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(columnHeadFont);


        HSSFCell cell = row.createCell(0);
        for (int i = 0; i < TITLE.length; i++) {
            HSSFCellStyle style_0 = wb.createCellStyle();
            style_0.setAlignment(HorizontalAlignment.CENTER);
            HSSFFont columnHeadFont_0 = wb.createFont();
            columnHeadFont_0.setFontName("宋体");
            columnHeadFont_0.setFontHeightInPoints((short) 12);
            columnHeadFont_0.setBold(true);
            style_0.setFont(columnHeadFont_0);
            cell.setCellType(CellType.STRING);
            cell = row.createCell(i);
            cell.setCellValue(TITLE[i]);
            cell.setCellStyle(style_0);
        }

        OutputStream os = null;
        try {
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            String fileName = "productUploadModel.xls";
            fileName = URLEncoder.encode(fileName, "utf-8");
            fileName = fileName.replace("+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);
            response.flushBuffer();
            os = response.getOutputStream();
            wb.write(os);
        } catch (Exception e) {
            log.error("互斥产品模板下载异常 {}",e);
        }finally{
            if ( os != null){
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    log.error("互斥产品模板下载关流异常 : {}", e);
                }
            }
        }
    }



    /**
     * 互斥产品导入
     *
     * @param uploadPlanExcluFile 导入文件
     * @return
     */
    @Override
    public boolean uploadPlanExcluFile(MultipartFile uploadPlanExcluFile) throws Exception {

        try {
            int successNum =0;
            List<Object[]> result = ExcelUtils.readeExcelData(uploadPlanExcluFile.getInputStream(),0,1);
            if(CollectionUtils.isEmpty(result)){
                log.error("--uploadPlanExcluFile null 文件--");
                return false;
            }
            log.info(" uploadPlanExcluFile 互斥产品需要导入多少行 "+ result.size());
            for (int i = 0; i < result.size(); i++) {
                if (null == result.get(i) || result.get(i).length==0){
                    log.info("uploadPlanExcluFile 第"+(i+1)+"行，校验错误"+"[数据内容为空]");
                    continue;
                }
                Object[] rowData = result.get(i);
                if(rowData.length<3){
                    log.info("uploadPlanExcluFile 第"+(i+1)+"行，校验错误"+"[数据列信息不正确]");
                    continue;
                }
                doRowData(i,rowData);
                successNum ++ ;
            }

            log.info("uploadPlanExcluFile 互斥产品实际导入多少行 "+ successNum);
            return successNum > 0;
        } catch (Exception e) {
            log.error("uploadPlanExcluFile 互斥产品excel导入异常", e);
        }

        return false;
    }


    private void doRowData(int row,Object[] rowData) {
        String[] excludePlanIds = rowData[2].toString().split(",");
        List<McdPlanExclusivity> excludeData = new ArrayList<>();

        for (String exPlan : excludePlanIds) {
            LambdaQueryWrapper<McdPlanDef> queryWrapper = new LambdaQueryWrapper();
            queryWrapper.eq(McdPlanDef::getPlanId,exPlan)
                        .eq(McdPlanDef::getStatus,1)
                        .ne(McdPlanDef::getPlanSrvType,'4');
            McdPlanDef planDef = mcdPlanDefMapper.selectOne(queryWrapper);
            if (planDef == null) {
                log.info("uploadPlanExcluFile 第" + (row + 1) + "行，互斥产品产品" + exPlan + "不存在");
                continue;
            }
            McdPlanExclusivity pData = new McdPlanExclusivity();
            pData.setPlanId(planDef.getPlanId());
            pData.setPlanName(planDef.getPlanName());
            pData.setType( 0);
            pData.setSourceType(0);
            excludeData.add(pData);
        }
        if (excludeData.size() == 0) {
            return;
        }
        String[] planIds = rowData[1].toString().split(",");
        for (String pl:planIds) {
            try{
                pl = pl.trim();
                McdPlanExclusivity planData = new McdPlanExclusivity();
                LambdaQueryWrapper<McdPlanDef> queryWrapper = new LambdaQueryWrapper();
                queryWrapper.eq(McdPlanDef::getPlanId,pl)
                        .eq(McdPlanDef::getStatus,1)
                        .ne(McdPlanDef::getPlanSrvType,'4');
                McdPlanDef planDef = mcdPlanDefMapper.selectOne(queryWrapper);

                if (planDef == null) {
                    log.info(" uploadPlanExcluFile 第" + (row + 1) +"行，产品" + pl + "不存在");
                    continue;
                }

                LambdaQueryWrapper<McdPlanExclusivity> exclusivityWrapper  = new LambdaQueryWrapper<>();
                exclusivityWrapper.eq(McdPlanExclusivity::getPlanId,pl)
                                  .eq(McdPlanExclusivity::getSourceType,0)
                                  .eq(McdPlanExclusivity::getType,1);
                McdPlanExclusivity mcdPlanExclusivityDo = planExclusivityMapper.selectOne(exclusivityWrapper);

                if (mcdPlanExclusivityDo != null) {
                    LambdaQueryWrapper<McdPlanExclusivity> delWrapper  = new LambdaQueryWrapper<>();
                    delWrapper.eq(McdPlanExclusivity::getPlanGroupId,mcdPlanExclusivityDo.getPlanGroupId())
                            .eq(McdPlanExclusivity::getSourceType,0)
                            .eq(McdPlanExclusivity::getType,0);

                    //删除已存在的互斥产品成员
                    planExclusivityMapper.delete(delWrapper);

                    for (McdPlanExclusivity pData:excludeData) {
                        pData.setPlanGroupId( mcdPlanExclusivityDo.getPlanGroupId());

                        //保存新加入的
                        planExclusivityMapper.insert(pData);
                        log.info("uploadPlanExcluFile 新增互斥产品Id"+ pData.getPlanId() +" 产品组id"+ mcdPlanExclusivityDo.getPlanGroupId());
                    }


                } else {
                    //保存产品组
                    String pgId = CommonUtil.generateId();
                    McdPlanGroup planGroup = new McdPlanGroup();
                    planGroup.setPlanGroupId(pgId);
                    planGroup.setPlanGroupName(pl);
                    planGroup.setPlanGroupType("I");
                    planGroup.setSourceType(0);

                    mcdPlanGroupMapper.insert(planGroup);
                    log.info("uploadPlanExcluFile 新增产品组信息Id"+ pgId);

                    //保存主产品
                    planData.setPlanId(planDef.getPlanId());
                    planData.setPlanName(planDef.getPlanName());
                    planData.setType( 1);
                    planData.setSourceType( 0);
                    //保存互斥产品
                    List<McdPlanExclusivity> temp = new ArrayList<>();
                    temp.addAll(excludeData);
                    temp.add(planData);
                    for (McdPlanExclusivity pData:temp) {
                        pData.setPlanGroupId( pgId);
                        planExclusivityMapper.insert(pData);
                        log.info("uploadPlanExcluFile 新增互斥产品Id"+ pData.getPlanId()+" 产品组ID"+ pgId);
                    }

                }
            }catch (Exception e){
                log.error(" uploadPlanExcluFile 导入互斥产品数据 error:",e);
            }
        }
    }




}

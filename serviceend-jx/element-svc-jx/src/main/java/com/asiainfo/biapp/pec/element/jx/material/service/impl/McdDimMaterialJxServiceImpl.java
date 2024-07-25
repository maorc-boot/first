package com.asiainfo.biapp.pec.element.jx.material.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.client.pec.plan.api.PecPlanFeignClient;
import com.asiainfo.biapp.client.pec.plan.model.IopDimContactReq;
import com.asiainfo.biapp.pec.core.model.IopDimContact;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.core.utils.RedisUtils;
import com.asiainfo.biapp.pec.core.utils.SftpUtils;
import com.asiainfo.biapp.pec.core.utils.UserUtil;
import com.asiainfo.biapp.pec.element.Enum.MaterialStatus;
import com.asiainfo.biapp.pec.element.Enum.OnlineStatus;
import com.asiainfo.biapp.pec.element.dobo.actionFrom.DimActionFrom;
import com.asiainfo.biapp.pec.element.dto.MaterialStatusDTO;
import com.asiainfo.biapp.pec.element.dto.request.*;
import com.asiainfo.biapp.pec.element.dto.response.AdivInfoListResponse;
import com.asiainfo.biapp.pec.element.dto.response.ChannelNameAndIdListResponse;
import com.asiainfo.biapp.pec.element.dto.response.ConctactListResponse;
import com.asiainfo.biapp.pec.element.jx.material.dao.McdDimMaterialJxDao;
import com.asiainfo.biapp.pec.element.jx.material.model.McdDimMaterialJxModel;
import com.asiainfo.biapp.pec.element.jx.material.request.DimAdivInfoJxQuery;
import com.asiainfo.biapp.pec.element.jx.material.request.DimMaterialPageListQuery;
import com.asiainfo.biapp.pec.element.jx.material.request.ExportMaterialInfoQuery;
import com.asiainfo.biapp.pec.element.jx.material.request.McdDimMaterialNewQuery;
import com.asiainfo.biapp.pec.element.jx.material.response.DimMaterialJxResponse;
import com.asiainfo.biapp.pec.element.jx.material.response.actionFrom.DimActionJxFrom;
import com.asiainfo.biapp.pec.element.jx.material.service.IMcdDimAdivInfoJxService;
import com.asiainfo.biapp.pec.element.jx.material.service.IMcdDimMaterialJxService;
import com.asiainfo.biapp.pec.element.model.McdDimAdivInfo;
import com.asiainfo.biapp.pec.element.model.McdDimChannel;
import com.asiainfo.biapp.pec.element.model.McdDimContact;
import com.asiainfo.biapp.pec.element.query.DimMaterialListQuery;
import com.asiainfo.biapp.pec.element.service.IMcdDimAdivInfoService;
import com.asiainfo.biapp.pec.element.service.IMcdDimChannelService;
import com.asiainfo.biapp.pec.element.service.IMcdDimContactService;
import com.asiainfo.biapp.pec.element.util.CommonUtil;
import com.asiainfo.biapp.pec.element.util.DataUtil;
import com.asiainfo.biapp.pec.element.util.RequestUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.hutool.core.util.URLUtil.decode;

/**
 * <p>
 * 营销素材 服务实现类
 * </p>
 *
 * @author ranpf
 * @since 2023-1-3
 */
@Service
@Slf4j
@RefreshScope
public class McdDimMaterialJxServiceImpl extends ServiceImpl<McdDimMaterialJxDao, McdDimMaterialJxModel> implements IMcdDimMaterialJxService {

    @Value("${grouphalls.channel:923}")
    private String groupChannel;
    @Resource
    private IMcdDimChannelService mcdDimChannelService;

    @Resource
    private IMcdDimContactService mcdDimContactService;

    @Resource
    private PecPlanFeignClient planFeignClient;

    @Resource
    private IMcdDimAdivInfoService mcdDimAdivInfoService;

    @Resource
    private IMcdDimAdivInfoJxService mcdDimAdivInfoJxService;

    @Resource
    private McdDimMaterialJxDao mcdDimMaterialJxDao;


    private static final String[] TITLE = {"素材名称", "产品编码","产品名称", "渠道ID","运营位ID", "失效时间", "素材类型", "文字内容", "链接", "描述"};
    private static final String[] IMPORT_DATA_TEMPLATE = {"素材导入名称(长度不超125)", "310095500538","2023流量畅享权益包-轻享会员", "924", "1", "2023-01-05", "1", "文字内容(长度不超2000)", "https://wap.jx.10086.cn/t/hydx", "素材描述信息(长度不超40)"};

    public static final String LIMIT_SIZE = "MCD_MATERIAL_FILE_LIMIT_SIZE";
    public static final String LOCAL_PATH = "MCD_MATERIAL_LOCAL_PATH";
    public static final String SFTP_IP = "MCD_MATERIAL_SFTP_IP";
    public static final String SFTP_PASSWORD = "MCD_MATERIAL_SFTP_PASSWORD";
    public static final String SFTP_PATH = "MCD_MATERIAL_SFTP_PATH";
    public static final String SFTP_PORT = "MCD_MATERIAL_SFTP_PORT";
    public static final String SFTP_USERNAME = "MCD_MATERIAL_SFTP_USERNAME";
    private static final String IS_NEED = "IS_NEED_UPLOAD_TO_SFTP";
    private static final String IS_APPROVE = "IS_NEED_APPROVE";

    /**
     * 素材分页(或根据条件)查询
     *
     * @param req 入参对象
     * @return
     */
    @Override
    public IPage<DimMaterialJxResponse> queryMaterialPageList(DimMaterialPageListQuery req) {
        IPage<DimMaterialJxResponse> responseIPage = new Page<>(req.getCurrent(), req.getSize());

        Page<McdDimMaterialJxModel> page = new Page<>(req.getCurrent(), req.getSize());
        IPage<McdDimMaterialJxModel> planDefPage = page(page, Wrappers.<McdDimMaterialJxModel>query().lambda()
                .eq(StringUtils.isNotBlank(req.getMaterialType()), McdDimMaterialJxModel::getMaterialType, req.getMaterialType())
                .like(StringUtils.isNotBlank(req.getMaterialName()),McdDimMaterialJxModel::getMaterialName, req.getMaterialName())
                .like(StringUtils.isNotBlank(req.getPlanIdOrName()),McdDimMaterialJxModel::getPlanId, req.getPlanIdOrName()).or()
                .like(StringUtils.isNotBlank(req.getPlanIdOrName()),McdDimMaterialJxModel::getPlanName, req.getPlanIdOrName())
                .eq(StringUtils.isNotBlank(req.getChannelId()),McdDimMaterialJxModel::getChannelId, req.getChannelId())
                .eq(StringUtils.isNotBlank(req.getMaterialStatus()),McdDimMaterialJxModel::getMaterialStatus, req.getMaterialStatus())
                .ge(StringUtils.isNotBlank(req.getStartDate()),McdDimMaterialJxModel::getCreateTime, req.getStartDate())
                .le(StringUtils.isNotBlank(req.getEndDate()),McdDimMaterialJxModel::getCreateTime, req.getEndDate())
                .orderByDesc(McdDimMaterialJxModel::getCreateTime)
        );

        return getMaterialResponsePage(responseIPage, planDefPage);
    }

    /**
     * 查询素材信息
     *
     * @param materialId 素材id
     * @return 素材响应对象
     */
    @Override
    public DimMaterialJxResponse getMaterialInfo(String materialId) {

        DimMaterialJxResponse response = new DimMaterialJxResponse();

        LambdaQueryWrapper<McdDimMaterialJxModel> wrapper = Wrappers.<McdDimMaterialJxModel>query().lambda()
                .eq(McdDimMaterialJxModel::getMaterialId, materialId);
        McdDimMaterialJxModel mcdDimMaterial = getBaseMapper().selectOne(wrapper);
        BeanUtils.copyProperties(mcdDimMaterial,response);

        if (StringUtils.isNotBlank(mcdDimMaterial.getChannelId())){
            McdDimChannel channel = mcdDimChannelService.getDimChannel(mcdDimMaterial.getChannelId());
            if (channel != null) {
                response.setChannelName(StringUtils.isNotBlank(channel.getChannelName())? channel.getChannelName() : "");
            } else {
                response.setChannelName("");
            }
        }

        if (StringUtils.isNotBlank(mcdDimMaterial.getContactId())) {
            if (groupChannel.equals(mcdDimMaterial.getChannelId())) {
                IopDimContactReq req = new IopDimContactReq();
                req.setId(mcdDimMaterial.getContactId());
                List<IopDimContact> iopDimContacts = planFeignClient.queryDimContact(req).getData();
                if (CollectionUtil.isNotEmpty(iopDimContacts)) {
                    response.setContactName(iopDimContacts.get(0).getChannelName());
                }
            } else {
                McdDimContact contact = mcdDimContactService.getContactBaseById(mcdDimMaterial.getContactId());
                if (contact != null) {
                    response.setContactName(StringUtils.isNotBlank(contact.getContactName()) ? contact.getContactName() : "");
                } else {
                    response.setContactName("");
                }
            }
        }

        if (StringUtils.isNotBlank(mcdDimMaterial.getPositionId())){
            McdDimAdivInfo adivInfo = mcdDimAdivInfoService.getAdivInfoByAdivIdAndChannelId(mcdDimMaterial.getPositionId(), groupChannel.equals(mcdDimMaterial.getChannelId()) ? mcdDimMaterial.getContactId() : mcdDimMaterial.getChannelId());
            if (adivInfo != null) {
                response.setPositionName(StringUtils.isNotBlank(adivInfo.getAdivName()) ? adivInfo.getAdivName() : "");
            } else {
                response.setPositionName("");
            }
        }
        return response;
    }

    /**
     * 渠道 下拉框信息查询
     *
     * @return
     */
    @Override
    public ChannelNameAndIdListResponse listChannelInfo() {
        return mcdDimChannelService.listAllChannelNameAndId();
    }

    /**
     * 触点 下拉框信息查询
     *
     * @param channelId 渠道ID
     * @return
     */
    @Override
    public ConctactListResponse listContactInfo(String channelId) {
        return mcdDimContactService.listContactInfo(channelId);
    }

    /**
     * 运营位 下拉框信息查询
     *
     * @param dimAdivInfoRequest 入参对象
     * @return
     */
    @Override
    public AdivInfoListResponse listPositionInfo(DimAdivInfoJxQuery dimAdivInfoRequest) {
        return mcdDimAdivInfoJxService.listPositionInfo(dimAdivInfoRequest);
    }

    /**
     * 营销素材导入
     *
     * @param uploadMaterialsFile 导入文件
     * @return
     */
    @Override
    public boolean uploadMaterialsFile(MultipartFile uploadMaterialsFile,String userId) throws Exception {
        InputStream inputStream = null;
        Workbook book = null;
        try {
            inputStream = uploadMaterialsFile.getInputStream();
            String name = uploadMaterialsFile.getOriginalFilename();
            if (name.endsWith("xls") || name.endsWith("XLS")) {
                book = new HSSFWorkbook(inputStream);
            } else if (name.endsWith("xlsx") || name.endsWith("XLSX")) {
                book = new XSSFWorkbook(inputStream);
            } else {
                throw new Exception("文件类型错误");
            }
            Sheet sheet = book.getSheetAt(0);
            //get material list info
            List<McdDimMaterialJxModel> materialsList = getContentWithSheet(sheet,userId);

            //batch save material info
            int num = mcdDimMaterialJxDao.batchSaveMaterials(materialsList);

            log.info("uploadMaterialsFile-batchSaveMaterials num is :{}", num);
            return num > 0;
        } catch (Exception e) {
            log.error("营销素材excel导入异常", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("关闭流失败", e);
                }
            }
        }
        return false;
    }

    /**
     *  获取导出营销素材的list
     * @param exportMaterialsRequest
     * @return
     */

    @Override
    public List<Object> exportMaterialsFile(ExportMaterialInfoQuery exportMaterialsRequest) {
        List<Object> list = new ArrayList<>();
        List<DimActionJxFrom> dimActionFromList = mcdDimMaterialJxDao.selectFromList(exportMaterialsRequest);
        for (DimActionJxFrom dimActionFrom : dimActionFromList) {
            Map<String,Object> map = new HashMap<>();
            map.put("1",dimActionFrom.getMaterialName() == null ? "": dimActionFrom.getMaterialName());
            map.put("2",dimActionFrom.getPlanId() == null ? "":dimActionFrom.getPlanId());
            map.put("3",dimActionFrom.getPlanName() == null ? "":dimActionFrom.getPlanName());
            map.put("4",dimActionFrom.getChannelId() == null ? "":dimActionFrom.getChannelId());
            map.put("5",dimActionFrom.getChannelName() == null ? "":dimActionFrom.getChannelName());
            map.put("6",dimActionFrom.getPositionId() == null ? "":dimActionFrom.getPositionId());
            map.put("7",dimActionFrom.getPositionName() == null ? "":dimActionFrom.getPositionName());
            map.put("8",dimActionFrom.getInvalidDate() == null ? "":getDateTime(dimActionFrom.getInvalidDate()));
            map.put("9",dimActionFrom.getMaterialTypeName() == null ? "":dimActionFrom.getMaterialTypeName());
            map.put("10",dimActionFrom.getContent() == null ? "":dimActionFrom.getContent());
            map.put("11",dimActionFrom.getTargetUrl() == null ? "":dimActionFrom.getTargetUrl());
            map.put("12",dimActionFrom.getDescription() == null ? "":dimActionFrom.getDescription());

            list.add(map);
        }
        return list;
    }

    private String getDateTime(Date createTime) {
        return   DateFormatUtils.format(createTime, "yyyy-MM-dd HH:mm:ss");
    }


    /**
     *  excel导出
     * @param fileName
     * @param excelHeader
     * @param list
     * @param response
     * @throws IOException
     */
    @Override
    public void exportExcel(String fileName, String[] excelHeader, List<Object> list, HttpServletResponse response) throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();
        try {
            HSSFSheet sheet = wb.createSheet("Sheet");
            HSSFRow row = sheet.createRow((int) 0);
            HSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            sheet.setDefaultColumnWidth(15);
            HSSFCell cell;
            for (int i = 0; i < excelHeader.length; i++) {
                cell = row.createCell(i);
                cell.setCellValue(excelHeader[i]);
                row.setHeight((short) 0x150);
                cell.setCellStyle(style);
            }
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    row = sheet.createRow(i + 1);
                    row.setHeight((short) 0x150);
                    final Map obj = (Map) list.get(i);
                    String objValue;
                    for (int j = 0; j < obj.size(); j++) {
                        cell = row.createCell(j);
                        objValue = String.valueOf(obj.get(String.valueOf(j + 1)));
                        System.out.println("objValue = " + objValue);
                        if (checkStrIsNum(objValue) && checkIsDecimal(objValue)) {
                            cell.setCellValue(new BigDecimal(objValue).doubleValue());
                        } else if (checkStrIsNum(objValue) && !checkIsDecimal(objValue) && objValue.length() < 10) {
                            cell.setCellValue(new BigDecimal(objValue).intValue());
                        } else {
                            cell.setCellValue(objValue);
                        }
                        cell.setCellStyle(style);

                    }
                }
            }
            fileName = new String(fileName.getBytes("GBK"), "iso8859-1");
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            OutputStream ouputStream = response.getOutputStream();
            wb.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            log.error("导出失败", e);
        } finally {
            wb.close();
        }
    }

    /**
     * 素材模板下载
     *
     * @param response 响应
     */
    @Override
    public void importMaterialsTemplate(HttpServletResponse response) {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();
        //sheet.createFreezePane(8,100);// 冻结
        HSSFRow row = sheet.createRow((short) 0);
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


        HSSFCell cell = row.createCell((short)0);
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


        HSSFRow row1 = sheet.createRow((short) 1);
        HSSFCell cell1 = row1.createCell((short)0);
        for (int i = 0; i < IMPORT_DATA_TEMPLATE.length; i++) {
            HSSFCellStyle style_0 = wb.createCellStyle();
            style_0.setAlignment(HorizontalAlignment.CENTER);
            HSSFFont columnHeadFont_0 = wb.createFont();
            columnHeadFont_0.setFontName("宋体");
            columnHeadFont_0.setFontHeightInPoints((short) 12);
            columnHeadFont_0.setBold(true);
            style_0.setFont(columnHeadFont_0);
            cell1.setCellType(CellType.STRING);
            cell1 = row1.createCell(i);
            cell1.setCellValue(IMPORT_DATA_TEMPLATE[i]);
            cell1.setCellStyle(style_0);
        }

        OutputStream os = null;
        try {
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            String fileName = "素材管理数据导入模板.xls";
            fileName = URLEncoder.encode(fileName, "utf-8");
            fileName = fileName.replace("+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);
            response.flushBuffer();
            os = response.getOutputStream();
            wb.write(os);
        } catch (Exception e) {
            log.error("素材模板下载异常 {}",e.getMessage());
        }finally{
            if ( os != null){
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    log.error("素材模板下载关流异常 : {}", e.getMessage());
                }
            }
        }
    }

    /**
     * 上传素材 （图片）
     *
     * @param file  文件
     * @return
     */
    @Override
    public Map<String, String> uploadMaterialPicture(MultipartFile file) {
        String path = RedisUtils.getDicValue(LOCAL_PATH);
        Map<String, String> map = new HashMap<>();

        if (!StringUtils.isNotBlank(path)) {
            map.put("status", "0");
            map.put("error", "上传路径为空，请联系管理员！");
            return map;
        }

        String fileName = file.getOriginalFilename();
        Pattern pattern = Pattern.compile(".*(.gif|.jpg|.png|.jpeg|.GIF|.JPG|.PNG|.JPEG)$");
        Matcher matcher = pattern.matcher(fileName);
        if (!matcher.matches()) {
            map.put("status", "0");
            map.put("error", "文件名格式不匹配！");
            return map;
        }

        //文件名称
        int index = fileName.indexOf(".");
        String fileType = fileName.substring(index);
        String id = UUID.randomUUID().toString().trim().replaceAll("-", "");
        String fileNewName = id + fileType;
        File targetFile = new File(path, fileNewName);
        double resourceSize = file.getSize() / 1024.0 / 1024.0;
        String resourceSizes = String.format("%.3f", resourceSize) + "M";

        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }

        if (fileName.length() > 32) {
            map.put("status", "0");
            map.put("error", "附件名称过长，请不要超过32位");
            return map;
        }
        if (file.isEmpty()) {
            map.put("status", "0");
            map.put("error", "文件为空");
            return map;
        }
        double fileSize = file.getSize() / 1024.0 / 1024.0;
        double maxSize = StringUtils.isBlank(RedisUtils.getDicValue(LIMIT_SIZE)) ?
                30.0 : Double.parseDouble(RedisUtils.getDicValue(LIMIT_SIZE));
        // 校验文件大小
        if (fileSize > maxSize) {
            map.put("status", "0");
            map.put("error", "文件不能大于"+ maxSize);
            return map;
        }

        // 上传本地服务器
        try {
            file.transferTo(targetFile);
            map.put("status", "1");
            map.put("resourceUrl", new String(((path+"/"+fileNewName).getBytes())));
            map.put("resourceSize", resourceSizes);
            map.put("resourceName", fileName);
        } catch (Exception e) {
            map.put("status", "0");
            map.put("resourceUrl", null);
            log.error("上传素材失败："+e);
        }

        String isNeedToFtp = StringUtils.isNotBlank(RedisUtils.getDicValue(IS_NEED)) ? RedisUtils.getDicValue(IS_NEED) : "0";
        if ("1".equals(isNeedToFtp)) {
            boolean toSftp = uploadToSftp(path, fileNewName);
            if (toSftp){
                map.put("status", "1");
                map.put("resourceUrl", new String(((path+"/"+fileNewName).getBytes())));
                map.put("resourceSize", resourceSizes);
                map.put("resourceName", fileName);
                map.put("message","图片上传成功");
            } else {
                map.put("status", "0");
                map.put("resourceUrl", null);
                map.put("error","上传sftp失败");
            }
            return map;
        }
        return map;
    }

    /**
     * 文件 上传到 sftp
     *
     * @param fileName
     * @return
     */
    private boolean uploadToSftp(String uploadFilePath,String fileName) {
        boolean flag = false;
        try {
            //String uploadFilePath = RedisUtils.getDicValue(LOCAL_PATH);
            String osName = System.getProperty("os.name");
            String filePath = null;
            if (osName.toLowerCase().startsWith("win")) {
                filePath = uploadFilePath.replace("/", File.separator).replace("\\", File.separator);
            } else {
                filePath = uploadFilePath;
            }
            filePath = CommonUtil.addSlash(filePath);
            String sftpDir = RedisUtils.getDicValue(SFTP_PATH);
            flag = upLoadFileToSftp(sftpDir,fileName,filePath);
            return flag;
        } catch (Exception e) {
            log.error("上传sftp失败", e);
        }
        return flag;
    }

    /**
     * 上传素材 (视频) 服务器本地
     * @param file 文件
     * @return map
     */
    @Override
    public Map<String, String> uploadMaterialVideo(MultipartFile file) {

        Map<String, String> map = new HashMap<>();
        String path = RedisUtils.getDicValue(LOCAL_PATH);
        if (path == null || StringUtils.isBlank(path)) {
            map.put("status", "0");
            map.put("error", "上传路径为空，请联系管理员！");
            return map;
        }

        String fileName = file.getOriginalFilename();
        Pattern pattern = Pattern.compile(".*(.mp3|.mp4|.avi|.MP3|.MP4|.AVI)$");
        assert fileName != null;
        Matcher matcher = pattern.matcher(fileName);
        if (!matcher.matches()) {
            map.put("status", "0");
            map.put("error", "文件名不匹配！");
            return map;
        }
        int index = fileName.indexOf(".");
        String fileType = fileName.substring(index);
        String id = UUID.randomUUID().toString().trim().replaceAll("-", "");
        String fileNewName = id + fileType;
        File targetFile = new File(path, fileNewName);
        double resourceSize = file.getSize() / 1024.0 / 1024.0;
        String resourceSizes = String.format("%.3f", resourceSize) + "M";

        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        if (file.isEmpty()) {
            map.put("status", "0");
            map.put("error", "文件为空");
            return map;
        }
        if (fileName.length() > 32) {
            map.put("status", "0");
            map.put("error", "附件名称过长，请不要超过32位");
            return map;
        }

        double fileSize = file.getSize() / 1024.0 / 1024.0;
        double maxSize = StringUtils.isBlank(RedisUtils.getDicValue(LIMIT_SIZE)) ?
                30.0 : Double.parseDouble(RedisUtils.getDicValue(LIMIT_SIZE));
        if (fileSize > maxSize) {
            map.put("status", "0");
            map.put("error", "文件不能大于"+ maxSize);
            return map;
        }

        try {
            file.transferTo(targetFile);
            map.put("status", "1");
            map.put("resourceUrl", path + "/" + fileNewName);
            map.put("resourceSize", resourceSizes);
            map.put("resourceName", fileName);
        } catch (Exception e) {
            map.put("status", "0");
            map.put("resourceUrl", null);
            log.info("视频上传失败 ", e);
        }

        String isNeedToFtp = StringUtils.isNotBlank(RedisUtils.getDicValue(IS_NEED)) ? RedisUtils.getDicValue(IS_NEED) : "0";
        if ("1".equals(isNeedToFtp)) {
            boolean toSftp = uploadToSftp(path, fileNewName);
            if (toSftp){
                map.put("status", "1");
                map.put("resourceUrl", new String(((path+"/"+fileNewName).getBytes())));
                map.put("resourceSize", resourceSizes);
                map.put("resourceName", fileName);
            } else {
                map.put("status", "0");
                map.put("resourceUrl", null);
                map.put("error","上传sftp失败");
            }
            return map;
        }
        return map;
    }

    /**
     * 上传素材 (远程服务器保存)
     *
     * @param file 文件
     * @return
     */
    @Override
    public Map<String,String> uploadMaterialPictureBySftp(MultipartFile file, HttpServletRequest request) {
        Map<String, String> suc = new HashMap<>();
        String isNeedToFtp = StringUtils.isNotBlank(RedisUtils.getDicValue(IS_NEED)) ? RedisUtils.getDicValue(IS_NEED) : "0" ;

        if ("0".equals(isNeedToFtp)){
            suc.put("status", "0");
            return suc;
        }
        if (file.isEmpty()) {
            suc.put("name", null);
            suc.put("status", "0");
            suc.put("error", "文件为空");
            return suc;
        }

        double fileSize = file.getSize() / 1024.0 / 1024.0;
        double maxSize = StringUtils.isBlank(RedisUtils.getDicValue(LIMIT_SIZE)) ?
                30.0 : Double.parseDouble(RedisUtils.getDicValue(LIMIT_SIZE));
        // 校验文件大小
        if (fileSize > maxSize) {
            suc.put("status", "0");
            suc.put("error", "文件不能大于" + maxSize + "M");
            return suc;
        }
        try {
            final String originalFilename = file.getOriginalFilename();
            log.info("originalFilename = " + originalFilename);

            String uploadFilePath = RedisUtils.getDicValue(LOCAL_PATH);

            String fileName = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf(".")) + "_"
                    + CommonUtil.generateId().substring(12) + "."
                    + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            String osName = System.getProperty("os.name");

            String filePath = null;
            if (osName.toLowerCase().startsWith("win")) {
                filePath = uploadFilePath.replace("/", File.separator).replace("\\", File.separator);
            } else {
                filePath = uploadFilePath;
            }
            // 配置的目录路径不存在则创建目录
            File f = new File(filePath);
            if (!f.exists()) {
                f.mkdirs();
            }
            String wholeFilePath = CommonUtil.addSlash(filePath) + fileName;
            log.info("wholeFilePath" + wholeFilePath);
            log.info("start to upload file：{}", wholeFilePath);
            File dest = new File(wholeFilePath);
            if (!dest.exists()) {
                dest.createNewFile();
            }
            file.transferTo(dest);

            String sftpDir = RedisUtils.getDicValue(SFTP_PATH);
            upLoadFileToSftp(sftpDir,fileName,filePath);

            suc.put("name", fileName);
            suc.put("status", "1");
            suc.put("error", "");
        } catch (Exception e) {
            suc.put("name", "");
            suc.put("status", "0");
            suc.put("error", "上传失败");
            log.error("上传失败", e);
        }
        return suc;
    }

    /**
     * sftp 上传文件
     *
     * @param sftpDir
     * @param fileName
     * @param filePath
     */
    private boolean upLoadFileToSftp(String sftpDir,String fileName,String filePath) {
        boolean upload = false;

        String sftpServer = RedisUtils.getDicValue(SFTP_IP);
        int sftpPort = Integer.parseInt(RedisUtils.getDicValue(SFTP_PORT));
        String username = RedisUtils.getDicValue(SFTP_USERNAME);
        String password = RedisUtils.getDicValue(SFTP_PASSWORD);

        SftpUtils sftpUtils = new SftpUtils();
        ChannelSftp sftp = null;
        try {
            sftp = sftpUtils.connect(sftpServer, sftpPort, username, password);
            log.info("连接sftp服务器");
            upload = sftpUtils.upload(sftpDir, fileName, filePath, sftp);
            log.info("文件上传sftp服务器: sftpDir:{},fileName:{},filePath{}",sftpDir,fileName,filePath);
        } catch (Exception e) {
            log.error("上传sftp异常", e);
        } finally {
            if (sftp != null) {
                try {
                    sftpUtils.disconnect(sftp);
                } catch (JSchException e) {
                    log.error("关闭sftp连接异常",e);
                }
            }
        }
        return upload;
    }

    /**
     * 根据素材 ID 查询所有素材信息
     *
     * @param materialId
     * @return
     */
    @Override
    public List<McdDimMaterialJxModel> queryMaterialById(String materialId) {
        LambdaQueryWrapper<McdDimMaterialJxModel> wrapper = Wrappers.<McdDimMaterialJxModel>query()
                .lambda().eq(McdDimMaterialJxModel::getMaterialId, materialId);
        return baseMapper.selectList(wrapper);
    }

    private static final Map<String,String> imageContentType = new HashMap<>();
    static {
        imageContentType.put("jpg", "image/jpeg");

        imageContentType.put("jpeg","image/jpeg");

        imageContentType.put("png","image/png");

        imageContentType.put("tif", "image/tiff");

        imageContentType.put("tiff","image/tiff");

        imageContentType.put("ico","image/x-icon");

        imageContentType.put("bmp","image/bmp");

        imageContentType.put("gif","image/gif");

        imageContentType.put("mp3","audio/mpeg");

        imageContentType.put("avi","video/x-msvideo");

        imageContentType.put("mp4","video/mp4");
    }
    /**
     * 图片 视频预览
     *
     * @param resourceUrl
     * @param dimMaterialRequest
     */
    @Override
    public void loadImage(String resourceUrl,DimMaterialRequest dimMaterialRequest,HttpServletResponse response) {
        ServletOutputStream out = null;
        FileInputStream ips = null;
        try {
            if(StringUtils.isNotBlank(resourceUrl)){
                if(resourceUrl.lastIndexOf(".") == -1){
                    resourceUrl = decode(resourceUrl);
                }
            }

            File file = new File(resourceUrl);
            log.info("文件 ====> {}",file);
            if (!file.exists()) {
                this.downFile(dimMaterialRequest, resourceUrl);
            }

            // 获取图片存放路径
            ips = new FileInputStream(file);
            String fileType = resourceUrl.substring(resourceUrl.lastIndexOf(".") + 1).toLowerCase();

            if ("png".equalsIgnoreCase(fileType) ||
                    "jpg".equalsIgnoreCase(fileType) ||
                    "jpeg".equalsIgnoreCase(fileType)) {
                response.setContentType(imageContentType.get(fileType));
                response.setHeader("Content-disposition", "inline;");
            } else if ("mp3".equalsIgnoreCase(fileType) ||
                    "mp4".equalsIgnoreCase(fileType) ||
                    "avi".equalsIgnoreCase(fileType)) {
                response.setContentType(imageContentType.get(fileType));
                response.setHeader("Accept-Ranges", "bytes");
            }
            out = response.getOutputStream();
            // 读取文件流
            int len = 0;
            byte[] buffer = new byte[1024 * 10];
            while ((len = ips.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            log.error("预览失败", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("关闭流失败", e);
                }
            }
            if (ips != null){
                try {
                    ips.close();
                } catch (IOException e) {
                    log.error("关闭流失败", e);
                }
            }
        }
    }

    /**
     * 新建素材校验素材名称
     *
     * @param materialName
     * @return
     */
    @Override
    public boolean validateMaterialName(String materialName) {
        LambdaQueryWrapper<McdDimMaterialJxModel> wrapper =
                Wrappers.<McdDimMaterialJxModel>query().lambda().eq(McdDimMaterialJxModel::getMaterialName, materialName);
        Integer count = baseMapper.selectCount(wrapper);
        return count == 0;
    }

    /**
     * 修改素材检验素材名称
     *
     * @param mcdDimMaterial
     * @return
     */
    @Override
    public boolean validateMaterialNameByUpdate(McdDimMaterialNewQuery mcdDimMaterial) {

        LambdaQueryWrapper<McdDimMaterialJxModel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(McdDimMaterialJxModel::getMaterialId,mcdDimMaterial.getMaterialId());
        final McdDimMaterialJxModel material = getOne(wrapper);
        //id存在,名称一致 允许修改
        if (StringUtils.equals(mcdDimMaterial.getMaterialName(),material.getMaterialName())){
            return true;
        }else {
            //修改过名称
            LambdaQueryWrapper<McdDimMaterialJxModel> wrapperByName = new LambdaQueryWrapper<>();
            wrapperByName.eq(McdDimMaterialJxModel::getMaterialName,mcdDimMaterial.getMaterialName());
            final Integer countByName = baseMapper.selectCount(wrapperByName);
            return countByName == 0;
        }

    }

    /**
     * 新建素材
     *
     * @param mcdDimMaterial
     */
    @Transactional
    @Override
    public boolean saveOrUpdateMaterial(McdDimMaterialNewQuery mcdDimMaterial) {
        McdDimMaterialJxModel dimMaterial= new McdDimMaterialJxModel();
        BeanUtils.copyProperties(mcdDimMaterial,dimMaterial);
        ////获取用户
        UserSimpleInfo user = UserUtil.getUser(Objects.requireNonNull(RequestUtils.getRequest()));
        dimMaterial.setCreator(user.getUserId());
        //素材大小
        final String resourceSize = mcdDimMaterial.getResourceSize();
        if (StringUtils.isNotBlank(resourceSize)){
            final Float value = Float.valueOf(resourceSize.replace("M", ""));
            dimMaterial.setMaterialSize(value);
        }
        //附件名称
        if (StringUtils.isNotBlank(mcdDimMaterial.getResourceName())){
            dimMaterial.setAttachmentName(mcdDimMaterial.getResourceName());
        }
        dimMaterial.setUseTimes(0);
        //普通素材
        dimMaterial.setExcellence(0);

        final String isApprove = RedisUtils.getDicValue(IS_APPROVE);
        if (StringUtils.isNotBlank(isApprove)){
            //设置状态为草稿
            dimMaterial.setMaterialStatus(0);
        }else {
            //不走审批 状态直接设置为可使用状态
            dimMaterial.setMaterialStatus(6);
        }

        final int insert = mcdDimMaterialJxDao.insert(dimMaterial);
        return insert == 1;
    }


    /**
     * 修改素材信息
     *
     * @param mcdDimMaterial
     * @return
     */
    @Override
    public boolean updateMaterial(McdDimMaterialNewQuery mcdDimMaterial) {
        McdDimMaterialJxModel dimMaterial= new McdDimMaterialJxModel();
        BeanUtils.copyProperties(mcdDimMaterial,dimMaterial);
        //素材大小
        final String resourceSize = mcdDimMaterial.getResourceSize();
        if (StringUtils.isNotBlank(resourceSize)){
            final Float value = Float.valueOf(resourceSize.replace("M", ""));
            dimMaterial.setMaterialSize(value);
        }
        final int update = mcdDimMaterialJxDao.updateById(dimMaterial);
        return update != 0;
    }

    /**
     *  预览文件下载到本地服务器
     * @param dimMaterialRequest
     * @param resourceUrl
     */
    private void downFile(DimMaterialRequest dimMaterialRequest, String resourceUrl) {
        SftpUtils sftpUtils = new SftpUtils();
        ChannelSftp sftp = null;
        try {
            String sftpServerIp = RedisUtils.getDicValue(SFTP_IP);
            int sftpServerPort =
                    Integer.parseInt(RedisUtils.getDicValue(SFTP_PORT));
            String sftpUserName = RedisUtils.getDicValue(SFTP_USERNAME);
            String sftpUserPwd = RedisUtils.getDicValue(SFTP_PASSWORD);
            String sftpPath = RedisUtils.getDicValue(SFTP_PATH);
            String sftpLocalPath =RedisUtils.getDicValue(LOCAL_PATH);

            String fileName = resourceUrl.split("/")[resourceUrl.split("/").length - 1];

            log.info("1.素材id是：" +dimMaterialRequest.getMaterialId());
            sftp = sftpUtils.connect(sftpServerIp, sftpServerPort, sftpUserName, sftpUserPwd);
            log.info(
                    "2.sftp登录成功！ip=" + sftpServerIp + ";port=" + sftpServerPort+
                            ";userName="+ sftpUserName + ",sftpPath："+ sftpPath+
                            ",sftpLocalPath:" + sftpLocalPath);
            sftpUtils.download(sftpPath, fileName, sftpLocalPath, sftp);
            log.info("3.下载成功！fileName=" + fileName);
        } catch (Exception e) {
            log.error("下载素材到本地目录出错：" + e);
        }finally {
            if (sftp != null) {
                try {
                    sftpUtils.disconnect(sftp);
                } catch (JSchException e) {
                    log.error("关闭sftp连接异常", e);
                }
            }
        }
    }

    private static Pattern NUMBER_PATTERN = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");

    /**
     * 利用正则表达式来判断字符串是否为数字
     */
    public static boolean checkStrIsNum(String str) {
        try {
            // 先将str转成BigDecimal，然后在转成String
            new BigDecimal(str).toString();
        } catch (Exception e) {
            // 如果转换数字失败，说明该str并非全部为数字
            return false;
        }
        Matcher isNum = NUMBER_PATTERN.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否是小数
     * @param obj
     * @return
     */
    public static boolean checkIsDecimal(String obj){
        if (obj.contains(".")){
            return true;
        }
        return false;
    }


    /**
     * phase excel 解析素材导入excel;
     * @param sheet sheet
     * @return
     */
    private List<McdDimMaterialJxModel> getContentWithSheet(Sheet sheet,String userId) throws Exception{
        if (sheet != null && sheet.getLastRowNum() > 0) {
            Row row = sheet.getRow(0);
            short lastCellNum = row.getLastCellNum();
            if (lastCellNum != 10) {
                throw new Exception("未使用素材导入模板导入数据，请重新选择内容文件!");
            }
        }
        List<McdDimMaterialJxModel> materialsList = new ArrayList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (null == row) {
                continue;
            }
            //not allow some important information to be null
            if (StringUtils.isEmpty(this.getCellString(row.getCell(0))) ||
                    StringUtils.isEmpty(this.getCellString(row.getCell(1))) ||
                    StringUtils.isEmpty(this.getCellString(row.getCell(2))) ||
                    StringUtils.isEmpty(this.getCellString(row.getCell(3))) ||
                    StringUtils.isEmpty(this.getCellString(row.getCell(4))) ||
                    StringUtils.isEmpty(this.getCellString(row.getCell(5))) ||
                    StringUtils.isEmpty(this.getCellString(row.getCell(6))) ||
                    StringUtils.isEmpty(this.getCellString(row.getCell(7)))) {
                throw new RuntimeException("导入文件行：" + i  + "，素材名称/产品编码/产品名称/渠道ID/运营位ID/失效时间/类型/文字内容 有一项或多项内容为空！");
            }

            McdDimMaterialJxModel material = new McdDimMaterialJxModel();

            String materialName = this.getCellString(row.getCell(0));
            if (materialName.length() > 125){
                log.error("素材导入第"+i+"行数据素材名称长度超过125!");
                continue;
            }

            //检查类型是否是文字
            String materialType = this.getCellString(row.getCell(6));
            if (!"1".equals(materialType)){
                log.error("素材导入第"+i+"行数据素材类型不为文字类型!");
                continue;
            }

            String content = this.getCellString(row.getCell(7));
            if (content.length() > 2000){
                log.error("素材导入第"+i+"行数据文字内容长度超过2000!");
                continue;
            }
            String targetUrl = this.getCellString(row.getCell(8));
            if (targetUrl.length() > 150){
                log.error("素材导入第"+i+"行数据链接地址长度超过150!");
                continue;
            }

            String description = this.getCellString(row.getCell(9));
            if (description.length() > 40){
                log.error("素材导入第"+i+"行数据素材描述长度超过40!");
                continue;
            }

            material.setMaterialName(materialName);
            material.setPlanId(this.getCellString(row.getCell(1)));
            material.setPlanName(this.getCellString(row.getCell(2)));
            material.setChannelId(this.getCellString(row.getCell(3)));
            material.setPositionId(this.getCellString(row.getCell(4)));
            material.setInvalidDate(getInvalidDate(this.getCellString(row.getCell(5))));

            material.setContent(content);
            material.setTargetUrl(targetUrl);
            material.setDescription(description);

            //生成素材ID
            material.setMaterialId(DataUtil.generateDateStringId());
            material.setMaterialType(1);
            material.setMaterialPicSize("0");
            material.setMaterialStatus(0);
            material.setCreator(userId);
            materialsList.add(material);
        }
        return materialsList;
    }

    /**
     * 获取Excel中的数值为字符串
     *
     * @param cell 单元格
     * @return
     */
    private String getCellString(Cell cell) {
        if (cell == null) {
            return "";
        }
        String cellString = "";

        switch (cell.getCellType()) {
            case STRING: // 字符串
                cellString = cell.getStringCellValue();
                break;
            case NUMERIC: // 数字
                double cellDouble = cell.getNumericCellValue();
                if (this.isEqualDouble(Math.round(cellDouble), cellDouble)) {
                    cellString = String.valueOf((long) cellDouble);
                } else {
                    cellString = String.valueOf(cellDouble);
                }
                break;
            case BOOLEAN: // Boolean
                cellString = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA: // 公式
                cellString = String.valueOf(cell.getCellFormula());
                break;
            case BLANK: // 空值
                cellString = "";
                break;
            case ERROR: // 故障
                cellString = "";
                break;
            default:
                cellString = "";
                break;
        }
        return cellString;
    }


    private Date getInvalidDate(String dateStr){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date date = sdf.parse(dateStr);
            return date;
        }catch (Exception e){
            log.error("导入素材时间转换异常",e);
        }
        return null;
    }

    /**
     * 判断浮点型是否相等
     * @param a 参数
     * @param b 参数
     * @return true/false
     */
    private boolean isEqualDouble(double a, double b) {
        if (Double.isNaN(a) || Double.isNaN(b) || Double.isInfinite(a) || Double.isInfinite(b)) {
            return false;
        }
        return (a - b) < 0.001d;
    }


    /**
     * 优秀素材分页查询
     *
     * @param request
     * @return
     */
    @Override
    public IPage<DimMaterialJxResponse> pagelistExcellentMaterial(MaterialTemplateRequest request) {


        IPage<DimMaterialJxResponse> responseIPage =
                new Page<>(request.getCurrent(), request.getSize());
        //log.info("type:{}",dimMaterialPageListRequest.getMaterialType());
        //return McdDimMaterialDao.selectPageList(responseIPage,dimMaterialPageListRequest);

        Page<McdDimMaterialJxModel> page = new Page<>(request.getCurrent(), request.getSize());
        IPage<McdDimMaterialJxModel> materialIPage = page(page, Wrappers.<McdDimMaterialJxModel>query().lambda()
                .eq(StringUtils.isNotBlank(request.getMaterialType()),McdDimMaterialJxModel::getMaterialType, request.getMaterialType())
                .like(StringUtils.isNotBlank(request.getKeyWords()),McdDimMaterialJxModel::getMaterialName, request.getKeyWords())
                .eq(StringUtils.isNotBlank(request.getCreator()),McdDimMaterialJxModel::getCreator,request.getCreator())
                .eq(McdDimMaterialJxModel::getExcellence,1)
                .orderByDesc(McdDimMaterialJxModel::getCreateTime)
        );

        return getMaterialResponsePage(responseIPage, materialIPage);
    }


    /**
     * 优秀素材查询
     *
     * @param materialId
     * @return
     */
    @Override
    public DimMaterialJxResponse getExcellentMaterialInfo(String materialId) {
        return getMaterialInfo(materialId);
    }

    /**
     * 根据素材id 添加素材到优秀素材库
     *
     * @param dimMaterialRequest
     * @return
     */
    @Override
    public boolean addExcellentMaterialLibrary(DimMaterialRequest dimMaterialRequest) {
        Integer re = mcdDimMaterialJxDao.addExcellenceById(dimMaterialRequest.getMaterialId());
        return re == 1;
    }

    /**
     * 根据素材id 从优秀素材库移除
     *
     * @param dimMaterialRequest
     * @return
     */
    @Override
    public boolean removeExcellentMaterialLibrary(DimMaterialRequest dimMaterialRequest) {
        Integer re = mcdDimMaterialJxDao.removeExcellenceById(dimMaterialRequest.getMaterialId());
        return re == 1;
    }

    /**
     * 优秀素材模板一键创建素材
     *
     * @param dimMaterialRequest
     * @return
     */
    @Override
    public boolean createExcellentMaterial(DimMaterialRequest dimMaterialRequest) {
        LambdaQueryWrapper<McdDimMaterialJxModel> wrapper = Wrappers.<McdDimMaterialJxModel>query().lambda()
                .eq(McdDimMaterialJxModel::getMaterialId, dimMaterialRequest.getMaterialId());
        McdDimMaterialJxModel mcdDimMaterial = getBaseMapper().selectOne(wrapper);
        //生成新的素材id
        mcdDimMaterial.setMaterialId(DataUtil.generateDateStringId());
        log.info("id ====> {}", mcdDimMaterial.getMaterialId());
        //获取用户名称
        UserSimpleInfo user = getUserSimpleInfo();
        if (StringUtils.isNotBlank(user.getUserId())){
            mcdDimMaterial.setCreator(user.getUserId());
        }
        //名称
        LambdaQueryWrapper<McdDimMaterialJxModel> wrapperByName = Wrappers.<McdDimMaterialJxModel>query().lambda()
                .like(McdDimMaterialJxModel::getMaterialName, mcdDimMaterial.getMaterialName());
        Integer count = mcdDimMaterialJxDao.selectCount(wrapperByName);
        String name  = mcdDimMaterial.getMaterialName() + "-A" + count;
        mcdDimMaterial.setMaterialName(name);
        //使用次数置零
        mcdDimMaterial.setUseTimes(0);
        //重置素材状态为草稿
        mcdDimMaterial.setMaterialStatus(0);
        //重置为普通素材
        mcdDimMaterial.setExcellence(0);
        //重置素材为 未上线状态
        mcdDimMaterial.setOnlineStatus(0);
        //重置素材流程id
        mcdDimMaterial.setApproveFlowId(null);
        //mcdDimMaterial.setCreateTime(null);
        //mcdDimMaterial.setUpdateTime(null);
        int insert = mcdDimMaterialJxDao.insert(mcdDimMaterial);
        return insert == 1;
    }

    /**
     * 获取单前用户信息
     *
     * @return
     */
    private UserSimpleInfo getUserSimpleInfo() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();
        return UserUtil.getUser(request);
    }

    /**
     * 我的素材-素材分頁
     *
     * @param request
     * @return
     */
    @Override
    public IPage<DimMaterialJxResponse> pagelistMyMaterial(MaterialTemplateRequest request) {
        IPage<DimMaterialJxResponse> responseIPage =
                new Page<>(request.getCurrent(), request.getSize());
        //log.info("type:{}",dimMaterialPageListRequest.getMaterialType());
        //return McdDimMaterialDao.selectPageList(responseIPage,dimMaterialPageListRequest);

        //获取当前用户信息
        UserSimpleInfo user = getUserSimpleInfo();
        Page<McdDimMaterialJxModel> page = new Page<>(request.getCurrent(), request.getSize());
        IPage<McdDimMaterialJxModel> planDefPage = page(page, Wrappers.<McdDimMaterialJxModel>query().lambda()
                .eq(StringUtils.isNotBlank(request.getMaterialType()),McdDimMaterialJxModel::getMaterialType, request.getMaterialType())
                .like(StringUtils.isNotBlank(request.getKeyWords()),McdDimMaterialJxModel::getMaterialName, request.getKeyWords())
                .eq(StringUtils.isNotBlank(request.getCreator()),McdDimMaterialJxModel::getCreator,request.getCreator())
                .eq(McdDimMaterialJxModel::getCreator,user.getUserId())
                .orderByDesc(McdDimMaterialJxModel::getCreateTime)
        );

        return getMaterialResponsePage(responseIPage, planDefPage);
    }

    /**
     * 素材分页对象封装
     * @param responseIPage
     * @param planDefPage
     * @return
     */
    private IPage<DimMaterialJxResponse> getMaterialResponsePage(IPage<DimMaterialJxResponse> responseIPage, IPage<McdDimMaterialJxModel> planDefPage) {
        List<DimMaterialJxResponse> list =  new ArrayList<>();
        List<McdDimMaterialJxModel> records = planDefPage.getRecords();
        for (McdDimMaterialJxModel item : records) {
            DimMaterialJxResponse response = new DimMaterialJxResponse();
            BeanUtils.copyProperties(item, response);
            McdDimChannel dimChannel = mcdDimChannelService.getDimChannel(item.getChannelId());
            String channelName = dimChannel != null ? StringUtils.isNotBlank(dimChannel.getChannelName()) ? dimChannel.getChannelName() : "" : "";
            response.setChannelName(channelName);
            McdDimAdivInfo adivInfo;
            if (groupChannel.equals(item.getChannelId())) {
                IopDimContactReq req = new IopDimContactReq();
                req.setId(item.getContactId());
                List<IopDimContact> iopDimContacts = planFeignClient.queryDimContact(req).getData();
                if (CollectionUtil.isNotEmpty(iopDimContacts)) {
                    response.setContactName(iopDimContacts.get(0).getChannelName());
                }
                adivInfo = mcdDimAdivInfoService.getAdivInfoByAdivIdAndChannelId(item.getPositionId(), item.getContactId());
            } else {
                McdDimContact contact = mcdDimContactService.getContactBaseById(item.getContactId());
                String contactName = contact != null ? StringUtils.isNotBlank(contact.getContactName()) ? contact.getContactName() : "" : "";
                response.setContactName(contactName);
                adivInfo = mcdDimAdivInfoService.getAdivInfoByAdivIdAndChannelId(item.getPositionId(), item.getChannelId());
            }
            String adivName = adivInfo != null ? StringUtils.isNotBlank(adivInfo.getAdivName()) ? adivInfo.getAdivName() : "" : "";
            response.setPositionName(adivName);
            list.add(response);
        }

        responseIPage.setRecords(list);
        responseIPage.setTotal(planDefPage.getTotal());
        responseIPage.setSize(planDefPage.getSize());
        responseIPage.setCurrent(planDefPage.getCurrent());
        responseIPage.setPages(planDefPage.getPages());
        return responseIPage;
    }

    /**
     * 我的素材-素材详情
     *
     * @param materialId
     * @return
     */
    @Override
    public DimMaterialJxResponse getMyMaterialInfo(String materialId) {
        return getMaterialInfo(materialId);
    }

    /**
     * 热点素材=素材分页
     *
     * @param request
     * @return
     */
    @Override
    public IPage<DimMaterialJxResponse> pagelistHotSpotMaterial(MaterialTemplateRequest request) {
        IPage<DimMaterialJxResponse> responseIPage =
                new Page<>(request.getCurrent(), request.getSize());
        //log.info("type:{}",dimMaterialPageListRequest.getMaterialType());
        //return McdDimMaterialDao.selectPageList(responseIPage,dimMaterialPageListRequest);

        //获取当前用户信息
        UserSimpleInfo user = getUserSimpleInfo();
        Page<McdDimMaterialJxModel> page = new Page<>(request.getCurrent(), request.getSize());
        IPage<McdDimMaterialJxModel> dimMaterialPage = page(page, Wrappers.<McdDimMaterialJxModel>query().lambda()
                .eq(StringUtils.isNotBlank(request.getMaterialType()),McdDimMaterialJxModel::getMaterialType, request.getMaterialType())
                .like(StringUtils.isNotBlank(request.getKeyWords()),McdDimMaterialJxModel::getMaterialName, request.getKeyWords())
                .eq(StringUtils.isNotBlank(request.getCreator()),McdDimMaterialJxModel::getCreator,request.getCreator())
                .ge(McdDimMaterialJxModel::getUseTimes,10)
                .orderByDesc(McdDimMaterialJxModel::getCreateTime)
        );

        return getMaterialResponsePage(responseIPage, dimMaterialPage);
    }

    /**
     * 热点素材-素材详情
     *
     * @param materialId
     * @return
     */
    @Override
    public DimMaterialJxResponse getHotSpotMaterialInfo(String materialId) {
        return getMaterialInfo(materialId);
    }

    /**
     * 热点素材一键创建素材
     *
     * @param dimMaterialRequest
     * @return
     */
    @Override
    public boolean createHotSpotMaterial(DimMaterialRequest dimMaterialRequest) {
        LambdaQueryWrapper<McdDimMaterialJxModel> wrapper = Wrappers.<McdDimMaterialJxModel>query().lambda()
                .eq(McdDimMaterialJxModel::getMaterialId, dimMaterialRequest.getMaterialId());
        McdDimMaterialJxModel mcdDimMaterial = getBaseMapper().selectOne(wrapper);
        //生成新的素材id
        mcdDimMaterial.setMaterialId(DataUtil.generateDateStringId());
        log.info("id ====> {}", mcdDimMaterial.getMaterialId());
        //获取用户名称

        UserSimpleInfo user = getUserSimpleInfo();
        if (StringUtils.isNotBlank(user.getUserId())){
            mcdDimMaterial.setCreator(user.getUserId());
        }
        //名称
        LambdaQueryWrapper<McdDimMaterialJxModel> wrapperByName = Wrappers.<McdDimMaterialJxModel>query().lambda()
                .like(McdDimMaterialJxModel::getMaterialName, mcdDimMaterial.getMaterialName());
        Integer count = mcdDimMaterialJxDao.selectCount(wrapperByName);
        String name  = mcdDimMaterial.getMaterialName() + "-H" + count;
        mcdDimMaterial.setMaterialName(name);
        //使用次数置零
        mcdDimMaterial.setUseTimes(0);
        //重置素材状态为草稿
        mcdDimMaterial.setMaterialStatus(0);
        //重置素材为普通素材
        mcdDimMaterial.setExcellence(0);
        //重置素材为 未上线状态
        mcdDimMaterial.setOnlineStatus(0);
        //重置素材流程id
        mcdDimMaterial.setApproveFlowId(null);
        //mcdDimMaterial.setCreateTime(null);
        //mcdDimMaterial.setUpdateTime(null);
        int insert = mcdDimMaterialJxDao.insert(mcdDimMaterial);
        return insert == 1;
    }


    /**
     * 素材审批管理-审批素材实例分页
     *
     * @param request
     * @return
     */
    @Override
    public IPage<DimMaterialJxResponse> pagelistApproveMaterial(MaterialTemplateRequest request) {
        IPage<DimMaterialJxResponse> responseIPage = new Page<>(request.getCurrent(), request.getSize());

        //获取当前用户信息
        //支持通过素材名称、对应渠道、素材类型进行搜索，或输入字段模糊搜索
        Page<McdDimMaterialJxModel> page = new Page<>(request.getCurrent(), request.getSize());
        IPage<McdDimMaterialJxModel> dimMaterialPage = page(page, Wrappers.<McdDimMaterialJxModel>query().lambda()
                .eq(StringUtils.isNotBlank(request.getMaterialType()),McdDimMaterialJxModel::getMaterialType, request.getMaterialType())
                .eq(StringUtils.isNotBlank(request.getChannelId()),McdDimMaterialJxModel::getChannelId,request.getChannelId())
                .eq(McdDimMaterialJxModel::getMaterialStatus, MaterialStatus.APPROVING.getValue())
                .like(StringUtils.isNotBlank(request.getKeyWords()),McdDimMaterialJxModel::getMaterialName, request.getKeyWords())
                .orderByDesc(McdDimMaterialJxModel::getApproveFlowId)
        );

        return getMaterialResponsePage(responseIPage, dimMaterialPage);
    }

    /**
     * 根据素材ID删除素材
     *
     * @param materialId
     * @return
     */
    @Override
    public boolean removeMyMaterialById(String materialId) {
        LambdaQueryWrapper<McdDimMaterialJxModel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(McdDimMaterialJxModel::getMaterialId,materialId);
        //获取素材信息
        final McdDimMaterialJxModel mcdDimMaterial = mcdDimMaterialJxDao.selectOne(wrapper);
        //审批中素材不可删除
        //if (mcdDimMaterial.getMaterialStatus() != Integer.parseInt(MaterialStatus.APPROVING.getValue()) ){
        //删除数据库该素材数据
        final Integer removeNum = baseMapper.removeMyMaterialById(materialId);
        if (removeNum == 1){
            log.info("删除数据库素材数据线程为 =====>{}",Thread.currentThread().getName());
            final Integer materialType = mcdDimMaterial.getMaterialType();
            if (materialType == 0|| materialType == 2) {
                deleteMaterial(mcdDimMaterial);
            }
            return true;
        }
        //}
        return false;
        //final int delete = baseMapper.deleteById(materialId);
        //return delete == 1;
    }

    /**
     * 修改素材上下线
     *
     * @param materialStatusDTO
     * @return
     */
    @Override
    public boolean saveOrUpdatePlanStatus(MaterialStatusDTO materialStatusDTO) {
        return mcdDimMaterialJxDao.saveOrUpdatePlanStatus(materialStatusDTO.getMaterialId(), materialStatusDTO.getStatusId());
    }

    /**
     * 根据运营位ID 获取素材列表(上线)
     *
     * @param dimMaterialListQuery
     * @return
     */
    @Override
    public List<McdDimMaterialJxModel> getIntelligentMatchingMaterialList(DimMaterialListQuery dimMaterialListQuery) {
        QueryWrapper<McdDimMaterialJxModel> wrapper = Wrappers.query();
        wrapper.eq("POSITION_ID",dimMaterialListQuery.getPositionId())
                .eq("ONLINE_STATUS", OnlineStatus.ONLINE.getValue())
                .eq(StrUtil.isNotBlank(dimMaterialListQuery.getType()), "ADIV_SIZE_TYPE", dimMaterialListQuery.getType())
                .orderByDesc("USE_TIMES")
                .orderByDesc("EXCELLENCE");
        return mcdDimMaterialJxDao.selectList(wrapper);
    }


    /**
     * 删除素材
     *
     * @param materialId
     */
    @Override
    public boolean removeMaterialById(String materialId) {

        LambdaQueryWrapper<McdDimMaterialJxModel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(McdDimMaterialJxModel::getMaterialId,materialId);
        //获取素材信息
        final McdDimMaterialJxModel mcdDimMaterial = mcdDimMaterialJxDao.selectOne(wrapper);
        //删除数据库该素材数据
        final Integer removeNum = baseMapper.removeExcellenceById(materialId);

        if (removeNum == 1){
            log.info("删除数据库素材数据线程为 =====>{}",Thread.currentThread().getName());
            final Integer materialType = mcdDimMaterial.getMaterialType();
            if (materialType == 0|| materialType == 2) {
                deleteMaterial(mcdDimMaterial);
            }
            return true;
        }
        return false;
    }

    @Async
    void deleteMaterial(McdDimMaterialJxModel mcdDimMaterial) {
        log.info("删除素材文件线程为 ====> {}",Thread.currentThread().getName());
        final String resourceUrl = mcdDimMaterial.getResourceUrl();
        File file = new File(resourceUrl);
        // 判断目录或文件是否存在
        if (file.exists()) { // 不存在返回 false
            // 判断是否为文件
            if (file.isFile()) {
                // 为文件时调用删除文件方法
                // 路径为文件且不为空则进行删除
                if (file.isFile() && file.exists()) {
                    file.delete();
                }
            }
        }

        //获取配置
        final String dicValue = RedisUtils.getDicValue(IS_NEED);
        if(StringUtils.isNotBlank(dicValue)){
            //文件有上传到文件服务器时 同步删除文件服务器的文件
            try {
                String sftpServerIp = RedisUtils.getDicValue(SFTP_IP);
                Integer sftpServerPort =
                        Integer.parseInt(RedisUtils.getDicValue(SFTP_PORT));
                String sftpUserName = RedisUtils.getDicValue(SFTP_USERNAME);
                String sftpUserPwd = RedisUtils.getDicValue(SFTP_PASSWORD);

                String fileName = resourceUrl.split("/")[resourceUrl.split("/").length - 1];

                SftpUtils sftpUtils = new SftpUtils();
                ChannelSftp sftp = sftpUtils.connect(sftpServerIp, sftpServerPort, sftpUserName, sftpUserPwd);

                try {
                    sftp.cd(resourceUrl);
                    log.info("目录名称 {}",resourceUrl);
                    sftp.rm(fileName);
                } catch (SftpException e) {
                    log.info("SftpException ====> {}",e.getMessage());
                    throw e;
                } finally {
                    sftpUtils.disconnect(sftp);
                }

            } catch (Exception e) {
                log.error("素材删除异常：" + e);
            }

        }
    }
}

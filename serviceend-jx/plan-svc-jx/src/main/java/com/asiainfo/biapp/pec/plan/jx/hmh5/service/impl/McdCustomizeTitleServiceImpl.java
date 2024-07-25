package com.asiainfo.biapp.pec.plan.jx.hmh5.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.hmh5.contant.CityMappingMap;
import com.asiainfo.biapp.pec.plan.jx.hmh5.dao.McdCustTitleDao;
import com.asiainfo.biapp.pec.plan.jx.hmh5.model.McdCustTitleList;
import com.asiainfo.biapp.pec.plan.jx.hmh5.request.McdCustomTitleQuery;
import com.asiainfo.biapp.pec.plan.jx.hmh5.service.McdCustomizeTitleService;
import com.asiainfo.biapp.pec.plan.jx.utils.ExcelUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class McdCustomizeTitleServiceImpl extends ServiceImpl<McdCustTitleDao, McdCustTitleList> implements McdCustomizeTitleService {


    private static final String[] TITLE = {"手机号码", "地市编码","备注", "导入类型(1导入,2删除)"};
    private static final String[] IMPORT_DATA_TEMPLATE = {"18823456789", "791","样例数据", "1" };
    private static final String IMPORT_DATA = "2099-01-01";

    @Resource
    private McdCustTitleDao custTitleDao;

    @Override
    public Page<McdCustTitleList> getCustomizeTitleList(McdCustomTitleQuery query) {
        Page<McdCustTitleList> page = new Page<>(query.getCurrent(), query.getSize());
        List<McdCustTitleList> list = custTitleDao.queryCustomizeTitleList(page, query);
        page.setRecords(list);
        return page;
    }

    @Override
    public Map<String,String> batchImportCustomizeTitleData(MultipartFile file, UserSimpleInfo user) {
        Map<String,String> map = new HashMap<>();
        try {
        // 1.解析excel
        List<Object[]> objects = ExcelUtils.readeExcelData(file.getInputStream(), 0, 1);
        //详情数据
        Set<McdCustTitleList> set = new HashSet<>();
        //号码列表
        Set<String> existsNum = new LinkedHashSet<>();
        //异常号码列表
        Set<String> errorList = new HashSet<>();
        CityMappingMap cityMappingMap = new CityMappingMap();
        for (Object[] object : objects) {
            String phoneNum = getValueByArray(object,0);
            String titleValue = getValueByArray(object,1);
            String cityName = getValueByArray(object,2);

            McdCustTitleList title = new McdCustTitleList();
            title.setPhoneNum(phoneNum);
            title.setTitle(titleValue);
            title.setCityId(cityMappingMap.getCode(cityName));
            title.setCityName(cityName);
            title.setCreateUserId(user.getUserId());
            title.setCreateUserName(user.getUserName());
            StringBuilder remark = new StringBuilder();//异常信息记录
            //默认正常，1:正常，2：异常
            int dataStatus = 1;
            if (StringUtils.isBlank(phoneNum) || phoneNum.length() != 11){
                remark.append("手机号位数不符合;");
                dataStatus = 2;
                errorList.add(phoneNum);
            }
            if(StringUtils.isBlank(cityMappingMap.getCode(cityName))){
                remark.append("地市为空、非江西地市或存在错别字;");
                dataStatus = 2;
                errorList.add(phoneNum);
            }
            if(StringUtils.isBlank(titleValue) || titleValue.length()>11 || hasRegSpecialChar(titleValue)){
                remark.append("称谓为空、长度超过10位或称谓中可能含有特殊字符;");
                dataStatus = 2;
                errorList.add(phoneNum);
            }
            title.setDataStatus(dataStatus);
            title.setRemarks(remark.toString());
            set.add(title);
            existsNum.add(phoneNum);
        }
            List<String> phoneAllList = existsNum.stream().collect(Collectors.toList());
        // 先删除已经存在的，即重复数据
            int repeatCount = delExistsAndBatchDel(phoneAllList);
            // 批量插入最新数据
            if(set.size()>0){
                this.saveBatch(set);
            }


            map.put("message","导入成功，已导入" + set.size() + "个号码，异常号码有"+errorList.size()+",已覆盖" + repeatCount + "个重复号码");
        } catch (Exception e) {
            log.error("importCustomizeTitleData异常", e);
        }
        return map;
    }

    /**
     * 获取数组指定下标的值，获取不到默认为空
     * @param objects
     * @param index
     * @return
     */
    public String getValueByArray(Object[] objects, int index){
        String value = "";
        try {
            value = (String)objects[index];

        } catch (Exception e) {
           log.error("getValueByArray数据异常");
        }
        return value;
    }

    @Override
    public void delCustomTitleById(String phoneNum) {
        custTitleDao.deleteById(phoneNum);
    }

    private void batchInsertTitleList(Set<McdCustTitleList> set){
        int batchSize = 1000;
        List<McdCustTitleList> titleLists = new ArrayList<>(batchSize + 1);
        for (McdCustTitleList mcdCustTitle : set) {
            titleLists.add(mcdCustTitle);
            if (titleLists.size() >= batchSize) {
                this.saveBatch(titleLists);
                titleLists.clear();
            }
        }
        if (CollectionUtil.isNotEmpty(titleLists)) {
            this.saveBatch(titleLists);
            titleLists.clear();
        }
    }

    private int delExistsAndBatchDel(List<String> phoneList) {
        List<String> existList = new ArrayList<>();
        int updateSize = 1000;
        int patch = phoneList.size() / updateSize + 1;
        for (int i = 0; i < patch; i++) {
            int begin = i * updateSize;
            int end = (i + 1) * updateSize;
            if (end > phoneList.size()) {
                end = phoneList.size();
            }
            // 查询重复号码
            List<String> list = custTitleDao.queryExistCustTitleList(phoneList.subList(begin, end));
            if (CollectionUtil.isNotEmpty(list)) {
                existList.addAll(list);
                // 删除重复号码
                custTitleDao.deleteBatchIds(list);
            }
        }
        return existList.size();
    }


    private  boolean isRowEmpty(XSSFRow row){
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            XSSFCell cell = row.getCell(c);
            if (cell != null && ObjectUtil.isEmpty(getXSSFValue(cell)))
                return false;
        }
        return true;
    }

    public boolean hasRegSpecialChar(String input) {
        String regEx = "[`~!@#$%^&*()\\-+={}':;,\\[\\].<>/?￥%…（）_+|【】‘；：”“’。，、？\\s]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(input);
        return m.find();
    }



    /**
     * excel值处理
     *
     * @param hssfCell
     * @return
     */
    public static Object getXSSFValue(XSSFCell hssfCell) {
        Object result = null;
        CellType cellType = hssfCell.getCellType();
        switch (hssfCell.getCellType()) {
            case NUMERIC: //数字
                result = hssfCell.getNumericCellValue();
                break;
            case BOOLEAN: //Boolean
                result = hssfCell.getBooleanCellValue();
                break;
            case ERROR: //故障
                result = hssfCell.getErrorCellValue();
                break;
            case FORMULA: //公式
                result = hssfCell.getCellFormula();
                break;
            case BLANK: //空值
                result = "";
                break;
            default: //字符串
                result = hssfCell.getStringCellValue();
        }
        return result;
    }

    /**
     * excel值处理
     *
     * @param hssfCell
     * @return
     */
    public static Object getValue(Cell hssfCell) {
        Object result = null;
        switch (hssfCell.getCellType()) {
            case NUMERIC: //数字
                result = hssfCell.getNumericCellValue();
                break;
            case BOOLEAN: //Boolean
                result = hssfCell.getBooleanCellValue();
                break;
            case ERROR: //故障
                result = hssfCell.getErrorCellValue();
                break;
            case FORMULA: //公式
                result = hssfCell.getCellFormula();
                break;
            case BLANK: //空值
                result = "";
                break;
            default: //字符串
                result = hssfCell.getStringCellValue();
        }
        return result;
    }


}

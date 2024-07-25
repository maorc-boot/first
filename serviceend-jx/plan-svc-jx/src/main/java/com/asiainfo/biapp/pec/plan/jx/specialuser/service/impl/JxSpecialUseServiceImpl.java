package com.asiainfo.biapp.pec.plan.jx.specialuser.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.dao.McdSpecialUseDao;
import com.asiainfo.biapp.pec.plan.jx.assembler.JxSpecialUserAssembler;
import com.asiainfo.biapp.pec.plan.jx.specialuser.dao.JxSpecialUseDao;
import com.asiainfo.biapp.pec.plan.jx.specialuser.model.JxMcdSpecialUse;
import com.asiainfo.biapp.pec.plan.jx.specialuser.service.JxSpecialUseService;
import com.asiainfo.biapp.pec.plan.jx.specialuser.thread.McdSpecialUseThread;
import com.asiainfo.biapp.pec.plan.model.McdSpecialUse;
import com.asiainfo.biapp.pec.plan.vo.SpecialUser;
import com.asiainfo.biapp.pec.plan.vo.req.SpecialUseMod;
import com.asiainfo.biapp.pec.plan.vo.req.SpecialUseQuery;
import com.asiainfo.biapp.pec.plan.vo.req.SpecialUseSave;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * <p>
 * 特殊客户群表 服务实现类
 * </p>
 *
 * @author imcd
 * @since 2021-12-06
 */
@Service
@Slf4j
public class JxSpecialUseServiceImpl extends ServiceImpl<McdSpecialUseDao, McdSpecialUse> implements JxSpecialUseService {

    /**
     * 特殊客户群数据同步到redis任务线程池
     */
    private ExecutorService syncSpecialUseExecutor = ThreadUtil.newExecutor(1);

    @Resource
    private JxSpecialUseDao jxSpecialUseDao;

    @Resource
    private JxSpecialUserAssembler assembler;


    @Override
    public IPage<SpecialUser> pageSpecialUser(SpecialUseQuery query) {
        final Page pager = new Page(query.getCurrent(), query.getSize(), false);
        Long count = jxSpecialUseDao.pageSpecialUseCount(query);
        pager.setTotal(count);
        return jxSpecialUseDao.pageSpecialUse(pager, query);
    }

    @Override
    public void saveBatchSpecialUser(List<JxMcdSpecialUse> specialUsesList) {
        // 保存免打扰数据到mysql
        jxSpecialUseDao.saveBatchSpecialUser(specialUsesList);
        // 同步数据到redis 和 BitMap缓存
        syncSpecialUseExecutor.execute(new McdSpecialUseThread(specialUsesList, this));

    }

    /**
     * 删除
     *
     * @param del
     */
    @Override
    public void delSpecialUser(SpecialUseSave del) {
        //base64解码参数
        del.setPhoneNos(new String(Base64.getDecoder().decode(del.getPhoneNos())));
        final List<SpecialUseSave> specialUseSaves = assembler.convertBatchDelModel(del);
        if (CollectionUtils.isEmpty(specialUseSaves)) {
            log.warn("没有需要删除的名单");
            return;
        }
        for (SpecialUseSave specialUseSave : specialUseSaves) {
            final LambdaQueryWrapper<McdSpecialUse> delete = Wrappers.lambdaQuery();
            delete.eq(McdSpecialUse::getChannelId, specialUseSave.getChannelId())
                    .eq(McdSpecialUse::getCustType, specialUseSave.getUserType())
                    .eq(McdSpecialUse::getProductNo, specialUseSave.getPhoneNos());
            this.remove(delete);
        }
        // 同步数据到redis 和 BitMap缓存
        syncSpecialUseExecutor.execute(new McdSpecialUseThread(this, specialUseSaves));
    }

    /**
     * 修改
     *
     * @param mod
     */
    @Override
    public void modSpecialUser(SpecialUseMod mod,UserSimpleInfo user ) throws Exception {
        //base64解码参数
        mod.setPhoneNos(new String(Base64.getDecoder().decode(mod.getPhoneNos())));
        final Map<String, List<String>> phoneListMap = assembler.convertPhone(mod.getPhoneNos());
        final List<String> phoneList = phoneListMap.get("1");
        if (CollectionUtils.isEmpty(phoneList)) {
            throw new Exception("没有需要修改的合规手机号");
        }
        //先删除
        SpecialUseSave del = new SpecialUseSave();
        del.setPhoneNos(mod.getPhoneNos());
        del.setChannelId(mod.getOldChannelId());
        del.setUserType(mod.getOldUserType());
        final List<SpecialUseSave> specialUseSaves = assembler.convertBatchDelModel(del);
        if (CollectionUtils.isEmpty(specialUseSaves)) {
            throw new Exception("没有需要删除的名单");
        }
        for (SpecialUseSave specialUseSave : specialUseSaves) {
            final LambdaQueryWrapper<McdSpecialUse> delete = Wrappers.lambdaQuery();
            delete.eq(McdSpecialUse::getChannelId, specialUseSave.getChannelId())
                    .eq(McdSpecialUse::getCustType, specialUseSave.getUserType())
                    .eq(McdSpecialUse::getProductNo, specialUseSave.getPhoneNos());
            this.remove(delete);
        }
        //后新增
        SpecialUseSave save = new SpecialUseSave();
        save.setPhoneNos(mod.getPhoneNos());
        save.setChannelId(mod.getChannelId());
        save.setUserType(mod.getUserType());
        save.setStartTime(new Date());
        final List<JxMcdSpecialUse> mcdSpecialUses = assembler.convertToModels(save, phoneList, user);
        jxSpecialUseDao.saveBatchSpecialUser(mcdSpecialUses);
        // 同步数据到redis 和 BitMap缓存
        syncSpecialUseExecutor.execute(new McdSpecialUseThread(mod, this));
    }

    @Override
    public String importSpecialUser(MultipartFile multiFile, String userType, String channelId, Date startTime, Date endTime, UserSimpleInfo user) throws Exception {
        log.info("**********文件上传特殊用户相关参数为custType--" + userType + ", channelId--" + channelId);
        final Map<String, List<String>> phoneListMap = assembler.convertPhone(multiFile);
        List<String> phoneList = phoneListMap.get("1");
        if (phoneList.size() == 0) {
            log.error("**********上传特殊用户文件异常，上传失败*************");
            throw new Exception("没有找到需要入库的手机号");
        }
        phoneList = phoneListMap.get("1").stream().distinct().collect(Collectors.toList());
        log.info("解析成功及剔重之后手机号{}条", phoneList.size());
        SpecialUseSave save = new SpecialUseSave();
        save.setUserType(userType);
        save.setChannelId(channelId);
        save.setStartTime(startTime);
        save.setEndTime(endTime);
        final List<JxMcdSpecialUse> mcdSpecialUses = assembler.convertToModels(save, phoneList, user);
        final int size = mcdSpecialUses.size();
        //需要分批保存
        final int batchSize = 1000;
        final int times = size / batchSize;
        for (int i = 0; i <= times; i++) {
            final int lastIndex = (i + 1) * batchSize;
            final int endIndex = Math.min(lastIndex, size);
            final List<JxMcdSpecialUse> current = mcdSpecialUses.subList(i * batchSize, endIndex);
            this.saveBatchSpecialUser(current);
        }
        return StrUtil.format("保存号码{}条，未保存不合规号码{}条", size, phoneListMap.get("0").size() - 1);
    }
}

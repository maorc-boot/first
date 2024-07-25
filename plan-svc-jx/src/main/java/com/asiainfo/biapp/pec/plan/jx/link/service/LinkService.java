package com.asiainfo.biapp.pec.plan.jx.link.service;

import com.asiainfo.biapp.client.pec.approve.model.User;
import com.asiainfo.biapp.pec.core.model.UserSimpleInfo;
import com.asiainfo.biapp.pec.plan.jx.link.vo.Link;
import com.asiainfo.biapp.pec.plan.jx.link.vo.McdDeleteLinkQuery;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface LinkService {

    String saveOrUpdateLink(Link link) throws Exception;

    Map<String, Object> searchAllLink(Map map) throws Exception;

    Map<String, Object> searchCampsByUrl(String url, String pageSize, String pageNum) throws Exception;

    String proxyPost(String url, String params);

    void downloadLinkTemplate(HttpServletResponse response) throws Exception;

    Map<String, String> uploadBatchLinkFile(MultipartFile uploadBatchLinkFile, UserSimpleInfo user) throws Exception;

    String getToShortUrlLinkId();

    boolean deleteLink(McdDeleteLinkQuery req) throws Exception;
}

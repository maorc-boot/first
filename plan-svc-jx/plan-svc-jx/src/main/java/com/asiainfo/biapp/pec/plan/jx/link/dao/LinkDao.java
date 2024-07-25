package com.asiainfo.biapp.pec.plan.jx.link.dao;


import com.asiainfo.biapp.pec.plan.jx.link.vo.Link;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface LinkDao {
    List<Link> searchAllLink(@Param("map") Map map) throws Exception;

    int tosizeAllLinik(@Param("map") Map map) throws Exception;

    void saveLink(@Param("link") Link link) throws Exception;

    void updateLink(@Param("link") Link link) throws Exception;

    boolean checkLinkIsExist(@Param("link") Link link) throws Exception;

    void updateLinkStatus(@Param("link") Link link) throws Exception;

    List<String> searchCampIdsByLinkUrl(@Param("url") String url, @Param("urls") String urls, @Param("pageSize") String pageSize, @Param("pageNum") String pageNum) throws Exception;

    List<String> queryCampIdsByLinkUrlNum(@Param("url") String url,@Param("urls") String urls);

    int saveBatchLink(@Param("list") List<Link> list);

    String getToShortUrlLinkId();

    boolean deleteLink(@Param("link") Link link);

    List<String> queryCampStatusByLinkUrl(@Param("url") String url );


}

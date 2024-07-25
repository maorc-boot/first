
package com.asiainfo.biapp.pec.approve.jx.utils;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Title : Pager
 *
 * @author  ranpf
 * @version 1.0.0.2023-05-24
 */
public class Pager {

    /**
     * 分页标识：F：首页
     */
    public static final String FIRST_PAGE = "F";

    /**
     * L：尾页
     */
    public static final String LAST_PAGE = "L";

    /**
     * N：下一页
     */
    public static final String NEXT_PAGE = "N";

    /**
     * P:上一页
     */
    public static final String PREV_PAGE = "P";

    /**
     * G:页面输入页码
     */
    public static final String GO_PAGE = "G";

    /** 当前页记录集 */
    @SuppressWarnings("rawtypes")
	private List result = new ArrayList();

    /** 总页数 */
    private int totalPage = 0;

    /** 总记录条数 */
    private long totalSize = 0;

    /** 每页最大记录条数 */
    private int pageSize = 10;

    /** 当前页码 */
    private int pageNum = 1;

    /** 是否首页 */
    private boolean firstPage = false;

    /** 是否尾页 */
    private boolean lastPage = false;

    /** 是否有前页 */
    private boolean hasPrevPage = false;

    /**是否有后页 */
    private boolean hasNextPage = false;

    /** 翻页起始页 */
    private int pageStart = 0;

    /** 是否任意页 */
    private boolean goPage = false;

    /** 分页标识 */
    private String pageFlag = Pager.FIRST_PAGE;

    /**
     * 重点策略条目数
     */
    private int importActSize;

    public int getImportActSize() {
        return importActSize;
    }

    public void setImportActSize(int importActSize) {
        this.importActSize = importActSize;
    }

    /**
     * Description :the pageFlag to get
     * 
     * @return the pageFlag
     * @see Pager#pageFlag
     */
    public String getPageFlag() {
        return pageFlag;
    }

    /**
     * Description ： the pageFlag to set
     * 
     * @param pageFlag
     * @see Pager#pageFlag
     */
    public void setPageFlag(String pageFlag) {
        this.pageFlag = pageFlag;
    }

    public Pager() {
    }

    public Pager(long totalSize) {
        this.totalSize = totalSize;
        this.totalPage = (int) Math.ceil(totalSize / (double) pageSize);
    }
    public Pager(long totalSize, int pageSize) {
        this.totalSize = totalSize;
        this.pageSize = pageSize;
        this.totalPage = (int) Math.ceil(totalSize / (double) pageSize);
    }
	/**
	 * 翻页功能 
	 * @return
	 */
	public Pager pagerFlip(){
		Pager pager =  null;
		if (StringUtils.isNotBlank(pageFlag)) {
			if (pageFlag.equals(Pager.FIRST_PAGE)) {
				pager = this.firstPage();
			} else if (pageFlag.equals(Pager.LAST_PAGE)) {
				pager = this.lastPage();
			} else if (pageFlag.equals(Pager.PREV_PAGE)) {
				pager = this.prevPage();
			} else if (pageFlag.equals(Pager.NEXT_PAGE)) {
				pager = this.nextPage();
			}else if (pageFlag.equals(Pager.GO_PAGE)) {
				pager = this.gotoPage(pageNum);
			}

		} else {
			pager = this.firstPage();
		}

		return pager;
	}
    
    /**
     * 下一页
     * 
     * @return Pager对象
     */
    public Pager nextPage() {
        Pager pager = new Pager();
    	pager.setTotalPage(totalPage);
    	pager.setTotalSize(totalSize);
    	pager.setPageSize(pageSize);
        if (pageNum < totalPage) {
            pager.setPageNum(++pageNum);
        } else {
            pager.setPageNum(pageNum);
        }
        pager.setImportActSize(importActSize);
        pager.handlePage();
        return pager;
    }

    /**
     * 上一页
     * 
     * @return Pager对象
     */
    public Pager prevPage() {
        Pager pager = new Pager();
    	pager.setTotalPage(totalPage);
    	pager.setTotalSize(totalSize);
    	pager.setPageSize(pageSize);
        if ((pageNum-1) > 1) {
            pager.setPageNum(--pageNum);
        }
        pager.setImportActSize(importActSize);
        pager.handlePage();
        return pager;
    }

    /**
     * 首页
     * 
     * @return Pager对象
     */
    public Pager firstPage() {
        Pager pager = new Pager();
        pager.setTotalPage(totalPage);
        pager.setTotalSize(totalSize);
        pager.setPageSize(pageSize);
        pager.setPageNum(1);
        pager.setImportActSize(importActSize);
        pager.handlePage();
        return pager;
    }

    /**
     * 尾页
     * 
     * @return Pager对象
     */
    public Pager lastPage() {
        Pager pager = new Pager();
    	pager.setTotalPage(totalPage);
    	pager.setTotalSize(totalSize);
    	pager.setPageSize(pageSize);
        pager.setPageNum(totalPage);
        pager.setImportActSize(importActSize);
        pager.handlePage();
        return pager;
    }

    // 任意页
    public Pager goPage() {
        Pager pager = new Pager();
        pager.setTotalPage(totalPage);
    	pager.setTotalSize(totalSize);
    	pager.setPageSize(pageSize);
        pager.setPageNum(pageNum);
        pager.setImportActSize(importActSize);
        pager.handlePage();
        return pager;
    }

    /**
     * 跳转到给定页
     * 
     * @return Pager对象
     */
    public Pager gotoPage(int pageNum) {
        Pager pager = new Pager();
        pager.setTotalPage(totalPage);
        pager.setTotalSize(totalSize);
        pager.setPageSize(pageSize);
        pager.setPageNum(pageNum);
        pager.setImportActSize(importActSize);
        pager.handlePage();
        return pager;
    }

    /**
     * 根据当前页与总页数判断前一页和后一页是否存在
     * 
     * @return void
     */
    private void handlePage() {
        if (pageNum > 1) {
            hasPrevPage = true;
        }
        if (pageNum < totalPage) {
            hasNextPage = true;
        }
        if (pageNum == 1) {
            firstPage = true;
        }
        if (pageNum == totalPage) {
            lastPage = true;
        }
    }

    // getters and setters
    public int getPageStart() {
        return pageStart;
    }

    public void setPageStart(int pageStart) {
        this.pageStart = pageStart;
    }

    @SuppressWarnings("rawtypes")
	public List getResult() {
        return result;
    }

    @SuppressWarnings("rawtypes")
	public void setResult(List result) {
        this.result = result;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public void setHasPrevPage(boolean hasPrevPage) {
        this.hasPrevPage = hasPrevPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public int getTotalPage() {
         this.setTotalPage((int) Math.ceil(totalSize / (double) pageSize));
        return totalPage;
    }

    public boolean isFirstPage() {
        return firstPage;
    }

    public void setFirstPage(boolean firstPage) {
        this.firstPage = firstPage;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public boolean isHasPrevPage() {
        return hasPrevPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public boolean isGoPage() {
        return goPage;
    }

    public void setGoPage(boolean goPage) {
        this.goPage = goPage;
    }
    
    /************ 转换开始 *************************/
    // 对于 struts2 自动invocation 注入，如果是 String 类型报错，进行了String 到 Number 类型的转换，
    // 对于 pager 中 totalesize totalpage pagesize pagenum 必须是数字型的或为 “”，
    public void setTotalSize(String totalSize){
    	setTotalSize(parseString2Num(totalSize));
    }
    public void setTotalPage(String totalPage){
    	setTotalPage(parseString2Num(totalPage));
    }
    public void setPageSize(String pageSize){
    	setPageSize(parseString2Num(pageSize));
    }
    public void setPageNum(String pageSize){
    	setPageNum(parseString2Num(pageSize));
    }
    private static int parseString2Num(String str){
    	if(null == str || "".equals(str) || str.trim().length() == 0 || "null".equals(str) || "undefined".equals(str) ){
    		str = "0";
    	}
    	return Integer.parseInt(str);
    }
    /************ 转换结束  *************************/
    
    
}

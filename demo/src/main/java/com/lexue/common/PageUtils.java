package com.lexue.common;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * Created by UI03 on 2017/6/15.
 */
public class PageUtils {
    /* @param pageNum 当前页
    * @param pageSize 每页条数
    * @param sortType 排序字段
    * @param direction 排序方向
    */
    public static PageRequest buildPageRequest(int pageNum, int pageSize, String sortType, String direction) {
        Sort sort = null;

        if (null == sortType || "".equals(sortType)) {
            return new PageRequest(pageNum - 1, pageSize);
        } else if (!(null == direction || "".equals(direction))) {
            if (Sort.Direction.ASC.equals(direction)) {
                sort = new Sort(Sort.Direction.ASC, sortType);
            } else {
                sort = new Sort(Sort.Direction.DESC, sortType);
            }
            return new PageRequest(pageNum - 1, pageSize, sort);
        } else {
            sort = new Sort(Sort.Direction.ASC, sortType);
            return new PageRequest(pageNum - 1, pageSize, sort);
        }
    }

    public static PageRequest buildPageRequest(int pageNum, int pageSize, String sortType){
        return buildPageRequest(pageNum, pageSize, sortType ,"") ;
    }

}

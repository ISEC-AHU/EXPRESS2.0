package com.example.utils;

/**
 * @author: pwz
 * @create: 2022/9/13 16:18
 * @Description:
 * @FileName: PageUtils
 */
public class PageUtils {

    /**
     * @param current
     * @param totalPage
     * @param navSize
     * @return
     */
    public static int[] pageNav(int current, int totalPage, int navSize) {
        int before = navSize / 2;
        int start = current - before < 1 ? 1 : current - before;
        int end = start + navSize - 1;
        if (end >= totalPage) {
            end = totalPage;
            start = end - navSize + 1;
            if (start < 1) {
                start = 1;
            }
        }
        int[] navs = new int[totalPage < navSize ? totalPage : navSize];
        for (int i = start, j = 0; i <= end; i++, j++) {
            navs[j] = i;
        }
        return navs;
    }

}
package com.sys.filter;

import com.sys.util.WebUtil;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.constructs.blocking.LockTimeoutException;
import net.sf.ehcache.constructs.web.AlreadyCommittedException;
import net.sf.ehcache.constructs.web.AlreadyGzippedException;
import net.sf.ehcache.constructs.web.filter.FilterNonReentrantException;
import net.sf.ehcache.constructs.web.filter.SimplePageCachingFilter;
import org.apache.commons.lang.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 页面缓存
 * 缓存所有同步请求
 * @author zzl
 * Date:2014-10-09
 */
public class EhcacheFilter  extends SimplePageCachingFilter{
    private final static String FILTER_URL_PATTERNS = "patterns";
    private static String[] cacheURLs;
    private static String[] excludes;

    private void init() throws CacheException {
        String patterns = filterConfig.getInitParameter(FILTER_URL_PATTERNS);
        cacheURLs = StringUtils.split(patterns, ",");
        excludes=StringUtils.split(filterConfig.getInitParameter("exclude"),",");
    }
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws AlreadyGzippedException, AlreadyCommittedException, FilterNonReentrantException, LockTimeoutException, Exception {
        if (cacheURLs == null) {
            init();
        }
        String url = request.getRequestURI();
        boolean isExclude=false;
        if (excludes != null && excludes.length > 0) {
            for (String cacheURL : excludes) {
                if (url.contains(cacheURL.trim())) {
                    isExclude = true;
                    break;
                }
            }
        }
        boolean flag = false;
        if (cacheURLs != null && cacheURLs.length > 0) {
            for (String cacheURL : cacheURLs) {
                if (url.contains(cacheURL.trim())) {
                    flag = true;
                    break;
                }
            }
        }
        if(isExclude)
            chain.doFilter(request, response);
        else if(!WebUtil.isAjaxRequest(request)||flag)
            super.doFilter(request, response, chain);
        else
            chain.doFilter(request, response);

    }
}

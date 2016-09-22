/***********************************************************************
 * Module:  DataGrid.java
 * Author:  lenovo
 * Purpose: Defines the Class DataGrid
 ***********************************************************************/

package com.sys.util;

/** 带分页的datagrid
 * 
 * @pdOid b6513507-6dee-4817-865b-b544d6fdf898 */
public class DataGrid {
   /** 页码
    * 
    * @pdOid 25d94266-7c9e-479b-b7cb-0392f17096c2 */
   private Integer pageNum = 1;
   /** 总数量
    * 
    * @pdOid bf75046d-f6e2-47d6-b246-6e2c034bf50c */
   private Integer totalCount;
   /** 每页数量
    * 
    * @pdOid 2756efc5-0ee4-43dd-9d57-f2109fda2514 */
   private Integer pagesize=10;
   /** 数据列表
    * 
    * @pdOid c1ed4102-f992-47b4-8e40-8ef1b22ac9c5 */
   private java.util.List dataList;
    /**
     * 排序字段，多个以逗号分开
     */
   private String orders;
   /** @pdOid 58f54804-bc70-4bd4-bc26-170d55dc5efb */
   public java.util.List getDataList() {
      return dataList;
   }

   /** @param newDataList
    * @pdOid 4092657d-251e-4d7e-94d1-0ed6eb6f69ae */
   public void setDataList(java.util.List newDataList) {
      dataList = newDataList;
   }

   /** @pdOid 6e0aa3cb-1b8b-405a-aa80-49063df2f222 */
   public Integer getPageNum() {
      return pageNum;
   }

   /** @param newPageNum
    * @pdOid 45b73d07-2d8a-4458-9736-cb16d7662a99 */
   public void setPageNum(Integer newPageNum) {
       if(newPageNum==null||newPageNum<1)
           newPageNum=1;
      pageNum = newPageNum;
   }

   /** @pdOid 9fef392f-a09b-4e62-ac08-dd1d45a26a10 */
   public Integer getTotalCount() {
      return totalCount;
   }

   /** @param newTotalCount
    * @pdOid 97ebecec-15cb-4fb0-a7d3-c6b49182dce2 */
   public void setTotalCount(Integer newTotalCount) {
       if(newTotalCount==null)
           newTotalCount=0;
      totalCount = newTotalCount;
   }

   /** @pdOid 3e6d41a8-da80-40e1-bace-9dd0bd84f7cd */
   public Integer getPagesize() {
      return pagesize;
   }

   /** @param newPagesize
    * @pdOid 075fa407-3349-4ce6-b85a-b4e0f86b3f06 */
   public void setPagesize(Integer newPagesize) {
       if(newPagesize==null)
           newPagesize=15;
      pagesize = newPagesize;
   }

   /** @pdOid 38499aef-2383-4f88-b378-1048907b5a1a */
   public Integer getCurrentPoint() {
      // TODO: implement
      return (pageNum-1)*pagesize;
   }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }
}
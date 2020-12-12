package com.example.warehouseproject.utilityClasses;

import com.example.warehouseproject.Code.historyitem;

import java.util.ArrayList;

public class HistoryPaginator {
    ArrayList<historyitem> list;

    public HistoryPaginator(ArrayList<historyitem> _list)
    {
        list = _list;
        TOTAL_NUM_ITEMS = list.size();
    }
    public  int TOTAL_NUM_ITEMS;
    public int ITEMS_PER_PAGE = 3;



    public int getTotalPages() {
        int remainingItems=TOTAL_NUM_ITEMS % ITEMS_PER_PAGE;
        if(remainingItems>0)
        {
            return TOTAL_NUM_ITEMS / ITEMS_PER_PAGE;
        }
        else{
            return (TOTAL_NUM_ITEMS / ITEMS_PER_PAGE)-1;}

    }

    public ArrayList<historyitem> getCurrentGalaxys(int currentPage) {
        int startItem = currentPage * ITEMS_PER_PAGE;
        int lastItem = startItem + ITEMS_PER_PAGE;

        ArrayList<historyitem> pagelist = new ArrayList<>();

        //LOOP THRU LIST OF GALAXIES AND FILL CURRENTGALAXIES LIST
        try {
            for (int i = 0; i < list.size(); i++) {

                //ADD CURRENT PAGE'S DATA
                if (i >= startItem && i < lastItem) {
                    pagelist.add(list.get(i));
                }
            }
            return pagelist;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

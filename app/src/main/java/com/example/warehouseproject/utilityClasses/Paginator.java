package com.example.warehouseproject.utilityClasses;

import com.example.warehouseproject.Code.item;

import java.util.ArrayList;

public class Paginator {

    ArrayList<item> list;

    public Paginator(ArrayList<item> _list)
    {
        list = _list;
        TOTAL_NUM_ITEMS = list.size();
    }
    public  int TOTAL_NUM_ITEMS;
    public int ITEMS_PER_PAGE = 6;



    public int getTotalPages() {
        int remainingItems=TOTAL_NUM_ITEMS % ITEMS_PER_PAGE;
        if(remainingItems>0)
        {
            return TOTAL_NUM_ITEMS / ITEMS_PER_PAGE;
        }
        else{
        return (TOTAL_NUM_ITEMS / ITEMS_PER_PAGE)-1;}

    }

    public ArrayList<item> getCurrentGalaxys(int currentPage) {
        int startItem = currentPage * ITEMS_PER_PAGE;
        int lastItem = startItem + ITEMS_PER_PAGE;

        ArrayList<item> pagelist = new ArrayList<>();

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

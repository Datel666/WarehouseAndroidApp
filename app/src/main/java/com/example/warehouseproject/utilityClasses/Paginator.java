package com.example.warehouseproject.utilityClasses;

import com.example.warehouseproject.Code.Item;
import java.util.ArrayList;

/**
 * HistoryPaginator class
 *
 * Класс, помогающий организовать страничное представление структуры ListView для формы товаров
 */
public class Paginator {

    //region variables
    public  int TOTAL_NUM_ITEMS;
    public int ITEMS_PER_PAGE = 6;
    ArrayList<Item> list;
    //endregion

    public Paginator(ArrayList<Item> _list)
    {
        list = _list;
        TOTAL_NUM_ITEMS = list.size();
    }

    /**
     * Получение общего количества страниц товаров
     * @return количество страниц
     */
    public int getTotalPages() {
        int remainingItems=TOTAL_NUM_ITEMS % ITEMS_PER_PAGE;
        if(remainingItems>0)
        {
            return TOTAL_NUM_ITEMS / ITEMS_PER_PAGE;
        }
        else{
        return (TOTAL_NUM_ITEMS / ITEMS_PER_PAGE)-1;}
    }

    /**
     * Получить коллекцию предметов для текущей страницы
     * @param currentPage текущая страница
     * @return коллекция предметов для текущей страницы
     */
    public ArrayList<Item> getCurrentGalaxys(int currentPage) {
        int startItem = currentPage * ITEMS_PER_PAGE;
        int lastItem = startItem + ITEMS_PER_PAGE;

        ArrayList<Item> pagelist = new ArrayList<>();

        try {
            for (int i = 0; i < list.size(); i++) {
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

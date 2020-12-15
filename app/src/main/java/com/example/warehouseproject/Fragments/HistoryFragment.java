package com.example.warehouseproject.Fragments;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.warehouseproject.Adapters.HistoryItemAdapter;
import com.example.warehouseproject.R;
import com.example.warehouseproject.customForms.ExpandableHeightGridView;
import com.example.warehouseproject.utilityClasses.DBHelper;
import com.example.warehouseproject.utilityClasses.HistoryPaginator;
import com.example.warehouseproject.Code.historyitem;
import com.example.warehouseproject.utilityClasses.QueriesProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * HistoryFragment class
 *
 * Класс содержит функционал для fragment_history layout
 */
public class HistoryFragment extends Fragment {

    //region variables
    // Формы
    private Button nextBtn;
    private Button prevBtn;
    private ExpandableHeightGridView ehgrid;
    private EditText searchF;

    // Переменные
    private int totalpages;
    private int currentpage;
    private Context act;
    private View view;

    // Экземпляры классов
    private HistoryPaginator paginator;
    private SQLiteDatabase database;
    private DBHelper helper;
    private QueriesProcessor qprocessor;

    // Структуры
    private List<historyitem> queryResults;

    //endregion

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_history,container,false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        initializeViews();
        initializeValues();

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentpage++;
                toggleButtons();
                bindData(currentpage);
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentpage--;
                toggleButtons();
                bindData(currentpage);
            }
        });

        searchF.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchF.removeTextChangedListener(this);
                if (!database.isOpen()) {
                    database = helper.getWritableDatabase();
                }
                queryResults = qprocessor.loadhistoryfiltered(searchF.getText().toString(),database);
                paginator = new HistoryPaginator((ArrayList<historyitem>) queryResults);
                totalpages = paginator.getTotalPages();
                currentpage = 0;
                toggleButtons();
                bindData(currentpage);
                ehgrid.setExpanded(true);
                if (ehgrid.getCount() == 0) {
                    Toast toast = Toast.makeText(getContext(), "Предметов по заданному запросу не найдено", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                searchF.addTextChangedListener(this);
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        act = context;

    }

    /**
     * Инициализация форм
     */
    private void initializeViews() {
        nextBtn = (Button) view.findViewById(R.id.nextHistoryBtn);
        prevBtn = (Button) view.findViewById(R.id.prevhistoryBtn);
        ehgrid = (ExpandableHeightGridView) view.findViewById(R.id.historyGridView);
        searchF = (EditText) view.findViewById(R.id.historySeachField);
    }

    /**
     * Инициализация значений переменных
     */
    private void initializeValues(){
        helper = new DBHelper(view.getContext());
        database = helper.getWritableDatabase();
        qprocessor = new QueriesProcessor();

        if(!database.isOpen()){
            database = helper.getWritableDatabase();
        }
        queryResults = qprocessor.loadhistory(database);
        paginator = new HistoryPaginator((ArrayList<historyitem>) queryResults);
        totalpages = paginator.getTotalPages();
        currentpage = 0;
        toggleButtons();
        bindData(currentpage);
        ehgrid.setExpanded(true);
    }

    /**
     * Получение коллекции товаров для текущей страницы
     * @param page
     */
    private void bindData(int page) {
        HistoryItemAdapter adapter = new HistoryItemAdapter(view.getContext(), paginator.getCurrentGalaxys(page));
        ehgrid.setAdapter(adapter);
    }

    /**
     * Изменение состояний кнопок, используемых для изменения текущей страницы списка товаров
     */
    private void toggleButtons() {
        //SINGLE PAGE DATA
        if (totalpages <= 0) {
            nextBtn.setEnabled(false);
            prevBtn.setEnabled(false);
        }
        //LAST PAGE
        else if (currentpage == totalpages) {
            nextBtn.setEnabled(false);
            prevBtn.setEnabled(true);
        }
        //FIRST PAGE
        else if (currentpage == 0) {
            prevBtn.setEnabled(false);
            nextBtn.setEnabled(true);
        }
        //SOMEWHERE IN BETWEEN
        else if (currentpage >= 1 && currentpage < totalpages) {
            nextBtn.setEnabled(true);
            prevBtn.setEnabled(true);
        }
    }
}

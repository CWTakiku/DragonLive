package com.mysdk.chart;

import android.graphics.Color;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by cwl on 2018/4/9.
 */

public class PieChartUtils {

    public static void initPieChart(PieChart pieChart,String centerName,String des){
        //设置描述
        Description description = new Description();
        description.setText(des);
        pieChart.setDescription(description);

        //pieChart.setDescriptionTextSize(20);

        //设置中心说明文字
        pieChart.setCenterText(centerName);
        pieChart.setCenterTextSize(20);
        pieChart.setCenterTextColor(Color.RED);
        pieChart.animateXY(3000,3000);
    }
    public static void setPieData(PieChart pieChart,Map<String,Float> map){
       List<PieEntry> list = new ArrayList<>();
        for (String key : map.keySet()) {
            Float value=map.get(key);
            PieEntry entry=new PieEntry(value,key);
            list.add(entry);
        }
        //创建一组饼块的数据
        PieDataSet pieDataSet = new PieDataSet(list,"月份");

        //设置饼块的间距
        pieDataSet.setSliceSpace(4);
        //设置饼快颜色
        pieDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
    }
}

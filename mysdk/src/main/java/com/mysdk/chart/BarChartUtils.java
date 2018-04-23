package com.mysdk.chart;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwl on 2018/4/9.
 */

public class BarChartUtils {

    /**
     *
     * @param barChart
     * @param max Y最大值
     * @param min Y最小值
     * @param isDrawbg 是否绘制表格背景
     */
    public static void initBarChart( BarChart barChart,int max,int min,boolean isDrawbg) {
        barChart.setNoDataText("目前木有数据哦");
//1.给Chart设置数据
      //  barChart.setData(getBarData());
//2.设置y轴的取值范围
        YAxis axisLeft = barChart.getAxisLeft();
//设置y轴最小值
        axisLeft.setAxisMinValue(min);
//设置y轴最大值
        axisLeft.setAxisMaxValue(max);
//3.设置去掉右边的y轴线
        YAxis axisRight = barChart.getAxisRight();
        axisRight.setEnabled(false);
//4.设置是否启用x轴线
        XAxis xAxis = barChart.getXAxis();
        xAxis.setEnabled(true);//显示x轴线
//5.设置表格描述信息
        // barChart.setDescription("表格描述");
//6.设置是否绘制表格背景

        if (isDrawbg) {
            barChart.setDrawGridBackground(true);
            //设置表格网格背景的颜色
            barChart.setGridBackgroundColor(Color.BLUE);
        }


//7.设置绘制动画的时间
        barChart.animateXY(3000, 3000);

    }

    /**
     *
     * @param barChart
     * @param mlist Y轴数值
     * @param name 表示的含义
     * @param showValue 是否显示柱子上面的数值
     */
    public static void setBarData(BarChart barChart, List<Float> mlist,String name,boolean showValue) {

        List<BarEntry> list = new ArrayList<>();
        for (int i = 0; i < mlist.size(); i++) {
            //一个BarEntry就是一个柱子的数据对象
            BarEntry barEntry = new BarEntry(i, mlist.get(i));
            list.add(barEntry);
        }
//创建BarDateSet对象，其实就是一组柱形数据
        BarDataSet barSet = new BarDataSet(list, name);

//设置柱形的颜色
        barSet.setColor(Color.BLUE);
//设置是否显示柱子上面的数值
        if (showValue) {
            barSet.setDrawValues(true);
        }
//设置柱子阴影颜色
        barSet.setBarShadowColor(Color.GRAY);
//创建集合，存放所有组的柱形数据
        List<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barSet);
        BarData barData = new BarData(dataSets);
       barChart.setData(barData);
    }
}

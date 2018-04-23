package com.mysdk.chart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class LineChartUtils {

    /**
     * 设置图表数据的方法
     * @param lineChart 要设置数据的图表
     * @param formData  表格的Y轴数据
     * @param mode      线条形状
     * @param color    线条的颜色设置
     * @param mList    X轴value
     */
    public static void setLineData(Context context, LineChart lineChart, List<Float> formData, LineDataSet.Mode mode, int color, final List<String> mList) {

        //根据Y轴数据产生表格数据
        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < formData.size(); i++) {
            values.add(new Entry(i,formData.get(i)));
        }

        LineDataSet set1;
        if (lineChart.getData() != null &&
                lineChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, "金融产品热度");
            set1.setDrawValues(false); //不显示数值
            set1.setDrawCircles(false); //数值不以原点显示
            if (mode != null) {
                set1.setMode(mode); //设置线条的形状
            }

            set1.setColor(color);    //设置线条颜色
            set1.setDrawHorizontalHighlightIndicator(false);
            set1.setHighLightColor(color);
              //设置小圆圈是否显示具体数据
            set1.setDrawValues(true);
            //set1.enableDashedLine(10f, 5f, 0f); //设置折线为虚线
            //set1.enableDashedHighlightLine(10f, 5f, 0f);
            //set1.setCircleColor(Color.BLUE);   //设置圆圈颜色
            set1.setLineWidth(2f);     //设置线条宽度
            //set1.setCircleRadius(3f);   //设置眼圈直径
            //set1.setDrawCircleHole(true);  //设置圆圈空心
            set1.setValueTextSize(9f);
            set1.setDrawFilled(false);  //设置绘制阴影区域
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15f);
            XAxis xAxis = lineChart.getXAxis();
            xAxis.setLabelCount(formData.size(), true);//设置X轴刻度数量 第二个参数为是否平均分配
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return mList.get((int) value); //mList为存有月份的集合
                }
            });

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            LineData data = new LineData(dataSets);

            lineChart.setData(data);
        }
    }

    /**
     * 初始化表格
     * @param lineChart 表格
     * @param maxRange  Y轴最大显示数值
     * @param minRange  Y轴最小显示数值
     * @param b         是否绘制Y轴Label
     *
     */
    public static void initLineChart(Context context, LineChart lineChart, float maxRange, int minRange ,boolean b) {
        lineChart.setDrawGridBackground(false); //是否绘制表格背景
        lineChart.setTouchEnabled(true); //设置触摸
        lineChart.setDragEnabled(false); //设置课拖动
        lineChart.setScaleEnabled(false); //设置可缩放
        lineChart.setPinchZoom(false);  //设置中心缩放
        lineChart.getDescription().setEnabled(false); //数据描述

        lineChart.setNoDataText("没有数据");
        /*lineChart.setNoDataTextColor(Color.RED);*/

        //设置一个MarkView,该View继承于MarkView
       // CustomMarkerView mv = new CustomMarkerView(context, R.layout.layout_custom_marker_view);
        //mv.setChartView(lineChart);
       // lineChart.setMarker(mv);

        //设置Y轴相关数据
        lineChart.getAxisRight().setEnabled(false); //隐藏Y轴右边的轴线
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setAxisMaximum(maxRange);  //设置最大数值范围
        leftAxis.setAxisMinimum(minRange);    //设置最小数值范围
        leftAxis.setDrawAxisLine(false); //不绘制轴线
        leftAxis.setDrawLabels(b);
       // leftAxis.setValueFormatter(new CustomYValueFormatter(drawValue));
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART); //设置Y轴Label的位置
        leftAxis.setDrawGridLines(false);

        //设置X轴相关数据
        XAxis xAxis = lineChart.getXAxis();
        YAxis leftYAxis = lineChart.getAxisLeft();
       leftYAxis.setDrawGridLines(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setGridColor(android.R.color.transparent); //设置X轴线为透明
        xAxis.setDrawGridLines(true); //绘制X轴
        xAxis.setTextSize(9);

        lineChart.animateXY(1500, 1500);  //设置图表动画
        lineChart.getLegend().setEnabled(true); //不显示表名
    }

    /**
     * 初始化表格
     * @param lineChart 表格
     * @param maxRange  Y轴最大显示数值
     * @param minRange  Y轴最小显示数值
     * @param b         是否绘制Y轴Label
     */
    public static void initLineChart(Context context,LineChart lineChart, int maxRange, int minRange, boolean b) {
        lineChart.setDrawGridBackground(false); //是否绘制表格背景
        lineChart.setTouchEnabled(true); //设置触摸
        lineChart.setDragEnabled(false); //设置课拖动
        lineChart.setScaleEnabled(false); //设置可缩放
        lineChart.setPinchZoom(false);  //设置中心缩放
        lineChart.getDescription().setEnabled(false); //数据描述

        lineChart.setNoDataText("没有数据");
        /*lineChart.setNoDataTextColor(Color.RED);*/

        //设置一个MarkView,该View继承于MarkView
       // CustomMarkerView mv = new CustomMarkerView(context, R.layout.layout_custom_marker_view);
       // mv.setChartView(lineChart);
       // lineChart.setMarker(mv);

        //设置Y轴相关数据
        lineChart.getAxisRight().setEnabled(false); //隐藏Y轴右边的轴线
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setAxisMaximum(maxRange);  //设置最大数值范围
        leftAxis.setAxisMinimum(minRange);    //设置最小数值范围
        leftAxis.setDrawAxisLine(false); //不绘制轴线
        leftAxis.setDrawLabels(b);
       // leftAxis.setValueFormatter(new CustomYValueFormatter(b));
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART); //设置Y轴Label的位置
        leftAxis.setDrawGridLines(false);

        //设置X轴相关数据
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setGridColor(android.R.color.transparent); //设置X轴线为透明
        xAxis.setDrawGridLines(false); //不绘制X轴
        xAxis.setTextSize(9);

        lineChart.animateXY(1500, 1500);  //设置图表动画
        lineChart.getLegend().setEnabled(true); //显示表名
    }

    /**
     * 设置图表数据的方法 多个数据组
     * @param lineChart 要设置数据的图表
     * @param formData1  表格的Y轴数据1
     * @param formData2  表格的Y轴数据2
     * @param mode      线条形状
     * @param fadeDrawableId 阴影区渐变色的Drawable文件id
     */
    public static void setLineData(Context context,LineChart lineChart, List<Float>  formData1,List<Float>  formData2,LineDataSet.Mode mode,int fadeDrawableId) {

        //根据Y轴数据产生表格数据
        ArrayList<Entry> values1 = new ArrayList<>();
        for (int i = 0; i < formData1.size(); i++) {
            values1.add(new Entry(i,formData1.get(i)));
        }

        ArrayList<Entry> values2 = new ArrayList<>();
        for (int i = 0; i < formData2.size(); i++) {
            values2.add(new Entry(i,formData2.get(i)));
        }


        LineDataSet set1,set2;
        if (lineChart.getData() != null &&
                lineChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) lineChart.getData().getDataSetByIndex(1);
            set1.setValues(values1);
            set2.setValues(values2);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {

            set1 = new LineDataSet(values1, "金融产品热度");
            set1.setDrawValues(false); //不显示数值
            set1.setDrawCircles(true); //数值不以原点显示
            if (mode != null) {
                set1.setMode(mode); //设置线条的形状
            }

            //set1.enableDashedLine(10f, 5f, 0f); //设置折线为虚线
            //set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setCircleColor(Color.parseColor("#6666ff"));   //设置圆圈颜色
            set1.setColor(Color.parseColor("#6666ff"));
            set1.setLineWidth(2f);     //设置线条宽度
            set1.setCircleRadius(3f);   //设置眼圈直径
            set1.setDrawCircleHole(false);  //设置圆圈空心
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);  //设置绘制阴影区域
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            set2 = new LineDataSet(values2, "金融产品热度");
            set2.setCircleColor(Color.parseColor("#ff7d00"));
            set2.setDrawCircleHole(false);
            set2.setCircleRadius(3f);
            set2.setColor(Color.parseColor("#ff7d00"));
            set2.setDrawValues(false); //不显示数值
            set2.setDrawCircles(true); //数值不以原点显示
            set2.setMode(mode); //设置线条的形状
            set2.setLineWidth(2f);     //设置线条宽度
            set2.setValueTextSize(9f);
            set2.setDrawFilled(true);  //设置绘制阴影区域
            set2.setFormLineWidth(1f);
            set2.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set2.setFormSize(15.f);

            //设置阴影区域的颜色值
            if (Build.VERSION.SDK_INT >= 18) {
                Drawable drawable = ContextCompat.getDrawable(context, fadeDrawableId);
                set1.setFillDrawable(drawable);
                set2.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLUE);
                set2.setFillColor(Color.BLUE);
            }

            LineData data = new LineData(set1,set2);
            lineChart.setData(data);
        }
    }
}
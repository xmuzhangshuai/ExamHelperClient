package com.bishe.examhelper.customerwidget;

import java.math.BigDecimal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.view.View;

/* *
 * 柱状图
 * */
public class BarChartView extends View{
	/**背景颜色*/
    private int backColor = Color.WHITE;
    /**分割线颜色*/
    private int lineColor = Color.GRAY;
    /**提示文本颜色*/
    private int textColor = Color.GRAY;
    /**数据组个数*/
    private int groupCount = 0;
    /**每组数据个数*/
    private int dataCount = 0;
    /**最大的数据 */
    private float maxData = 0;
    /**最大的数据(取整) */
    private int maxCeil = 0;
    /**最大分割数*/
    private int maxGrid = 10;
    /**最小分割数*/
    private int minGrid = 2;
    /**当前分割数*/
    private int currentGrid = 2;
    /**垂直方向刻度*/
    private float gridGap = 0;
    /**最右边空白区域大小*/
    private float rightSpace = 0;
    /**最底端控件*/
    private float bottomSpace = 40;
    /**柱状图数据*/
    private float[][] data = null;
    /**柱状图中柱的颜色*/
    private int[] barColor = null;
    /**柱状图中柱之间间隔*/
    private int barSpace = 10;
    
    private int maxDataWidth = 0;
    
    private float widthData = 0;
    /**分组标题*/
    private String[] groupTitle = null;
    /**数据标题*/
    private String[] dataTitle = null;
    
    public BarChartView(Context context){
        super(context);
    }
    
    public BarChartView(Context context, AttributeSet attrs){
        super(context,attrs);
    }
    
    public BarChartView(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs,defStyle);
    }
    
    /**
     * 设置数据组数
     * @param count
     */
    public void setGroupCount(int count){
        if(count > 0){
            groupCount = count;
            data = new float[count][];
        }
    }
    /**
     * 设置每组数据个数
     * 本方法必须在setGroupCount之后使用
     * @param count
     */
    public void setDataCount(int count){
    	if(groupCount == 0) return;
        if(count > 0 && data != null && data.length > 0){
            dataCount = count;
            barColor = new int[count];
            for(int i = 0; i < dataCount; i++){
                barColor[i] = Color.GREEN;
            }
        }
    }
    
    public void setGroupTitle(String[] title){
        if(title != null && groupCount == title.length){
            groupTitle = title;
        }
    }
    
    public void setDataTitle(String[] title){
        if(title != null && dataCount == title.length){
            dataTitle = title;
        }
    }
    /**
     * 设置每组数据
     * @param index 第几组数据，从0开始
     * @param d
     */   
    public void setGroupData(int index,float[] d){
        if(data != null && data.length > index && d.length == dataCount){
            data[index] = d;
            for(int i = 0; i < d.length; i++){
                if(maxData < d[i])
                    maxData = d[i];
                
            }
            maxCeil = (int)Math.ceil(maxData);
            calGrid();
        }
    }
    
    /**
     * 设置柱图颜色
     * 颜色个数必须和每组数据个数相同
     * @param color
     */
    public void setBarColor(int[] color){
        if(data != null && barColor != null && barColor.length == color.length){
            barColor = color;
        }
    }
    
    private void calGrid(){
        if(maxCeil <= 1){
            float tmp = maxData;
            int c = 0;
            while(tmp < 1){
                tmp = tmp * 10;
                if(c == 0)
                    c = 10;
                else
                    c *= 10;
            }
            
            gridGap = maxData / 10;
            if(gridGap * c < 0.5){
                gridGap = 0.5f / c;
            }else if(gridGap * c > 0.5){
                gridGap = 1f / c;
            }
            currentGrid = 1;
            tmp = gridGap;
            while(tmp + (1f / (c * 10))< maxData){
                currentGrid++;
                tmp += gridGap;
                if(tmp + (1f / (c * 10)) < maxData)
                    calDatawidth(roundFloat(tmp));
            }
        }else if(maxCeil <= 10){
            currentGrid = maxCeil;
            gridGap = 1;
        }else{
            float tmp = maxCeil;
            int c = 0;
            while(tmp > 10){
                tmp = tmp / 10.0f;
                if(c == 0)
                    c =1;
                else
                    c *= 10;
            }
            int d = (int)tmp;
            if(d == tmp){
                gridGap = 10;
            }else if(d + 0.5 > tmp){
                gridGap = d + 0.5f;
            }else{
                gridGap = d + 1;
            }
            gridGap *= c;
            currentGrid = 1;
            tmp = gridGap;
            while(tmp < maxCeil){
                currentGrid++;
                tmp += gridGap;
            }
            
        }
        if(currentGrid < minGrid)
            currentGrid = minGrid;
        if(currentGrid > maxGrid)
            currentGrid = maxGrid;
    }
    
    private void calDatawidth(float f){
        String tmp = String.valueOf(f);
        if(tmp.endsWith(".0"))
            tmp = tmp.substring(0,tmp.length() - 2);
        if(tmp.length() > maxDataWidth){
            maxDataWidth = tmp.length();
            widthData = f;
        }
    }
    /**
     * 设置背景色
     */
    public void setBackgroundColor(int color){
        backColor = color;
    }
    
    protected void onDraw(Canvas canvas) {
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        
        int height = getHeight() - paddingTop - paddingBottom;
        int width = getWidth() - paddingLeft - paddingRight;
        
        canvas.save();
        canvas.translate(paddingLeft, paddingTop);
        canvas.clipRect(0,0,width,height);
        
        canvas.drawColor(backColor);
        
        Rect rt = new Rect();
        String txt = String.valueOf(maxCeil);
        if(maxCeil > 1){
            int gg = (int)gridGap;
            if(gg < gridGap)
                txt += ".5";
        }else{
            txt = String.valueOf(widthData);
        }
        Paint p = new Paint();
        p.getTextBounds(txt, 0, txt.length(), rt);
        float txtWidth = rt.width();
        int txtPadd = 5;
        
        p.setColor(lineColor);
        int bt = 20;
        int h = (int)(height - bottomSpace);
        float lsp = (h * 1.0f - bt) / (currentGrid * 1.0f);
        /*Y轴刻度线*/
        canvas.drawLine(txtWidth + 2* txtPadd + 5, 5, txtWidth + 2* txtPadd + 5, h - bt + 10, p);
        
        Paint paint = new Paint();
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.RIGHT);
        FontMetrics fm = paint.getFontMetrics();
    
        for(int i = 0 ; i <= currentGrid; i++){
        	/*根据Y轴刻度线绘出对应的水平线*/
            canvas.drawLine(txtWidth + 2* txtPadd,  h -bt - i * lsp + 5, width - rightSpace, h -bt - i * lsp + 5, p);
            float c = i * gridGap;
            if(maxCeil <= 1){
                if(String.valueOf(c).length() > maxDataWidth){
                    c = roundFloat(c);
                }
            }
            String dtxt = String.valueOf(c);
            if(dtxt.endsWith(".0"))
                dtxt = dtxt.substring(0,dtxt.length() - 2);
            /*绘制Y轴刻度值*/
            canvas.drawText(dtxt, txtWidth + txtPadd, h - bt - i * lsp - fm.ascent , paint);
        }
        
        float w = width - rightSpace - txtWidth - 2* txtPadd - 5;
        float gp = (w - 2 *groupCount * barSpace );
        float barWidth = gp / groupCount;
        barWidth /= dataCount;
        float sx = txtWidth + 2* txtPadd + 5 ;
        float xx = sx;
        for(int i = 0; i < groupCount - 1; i++){
            xx += barSpace * 2;
            xx += barWidth * dataCount;
            /*绘制X轴上组别之间间隔的一个小短线*/
            canvas.drawLine(xx, h -bt + 5, xx, h - bt + 10, p);       
        }
        
        for(int i = 0; i < groupCount; i++){
            sx += barSpace;
            for(int j = 0; j < dataCount; j++){
                p.setColor(barColor[j]);
                float v = data[i][j];
                float y = h - bt + 5 - (v * lsp / gridGap);
                /*绘制各组中每项数据*/
                canvas.drawRect(sx, y, sx + barWidth, h - bt + 5, p);
                sx += barWidth;
            }
            sx += barSpace;
        }
        
        float yy = h + bt - 5;
        xx = 0;
        for(int i = 0; i < dataCount; i++){
            p.setColor(barColor[i]);
            xx += 20;
            /*绘制每组中数据指示块*/
            canvas.drawRect(xx, yy, xx + 15, yy + 15, p);
            xx += 20;
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setColor(textColor);
            if(dataTitle != null && dataTitle[i] != null){
            	/*绘制每组中数据指示块的中文提示*/
                canvas.drawText(dataTitle[i], xx, yy - fm.ascent, paint);
                Rect r = new Rect();
                paint.getTextBounds(dataTitle[i], 0, dataTitle.length, r);
                xx += r.width();
            }
        }
        
        if(groupTitle != null && groupTitle.length > 0){
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setColor(textColor);
            yy = h + 2;
            xx =txtWidth + 2* txtPadd + 5;
            for(int i = 0; i < groupCount ; i++){
                xx += barSpace;
                xx += (dataCount * barWidth) / 2;
                /*绘制X轴方向不同组中文提示*/
                canvas.drawText(groupTitle[i], xx, yy, paint);
                xx += barSpace;
                xx += dataCount * barWidth / 2;
            }
        }
        
        canvas.restore();
    }
    
    private float roundFloat(float f){
        String tmp = String.valueOf(gridGap);
        int ind = tmp.indexOf(".");
        int m = tmp.length() - ind;
        BigDecimal   bd = new BigDecimal(f);
        bd = bd.setScale(m, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
    
    
}

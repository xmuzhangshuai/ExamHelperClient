package com.bishe.examhelper.ui;

import java.text.NumberFormat;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.app.Dialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseActivity;
import com.bishe.examhelper.config.DefaultValues;
import com.bishe.examhelper.dao.StudyRecordDao.Properties;
import com.bishe.examhelper.dbService.MaterialAnalysisService;
import com.bishe.examhelper.dbService.MultiChoiceService;
import com.bishe.examhelper.dbService.QuestionTypeService;
import com.bishe.examhelper.dbService.SingleChoiceService;
import com.bishe.examhelper.dbService.StudyRecordService;
import com.bishe.examhelper.entities.StudyRecord;

public class StatisticActivity extends BaseActivity {
	private GraphicalView mChartView;// 显示PieChart
	private LinearLayout mLinear;// 布局方式
	private TextView allQuestionTextView;// 总题数
	private TextView allRateTextView;// 做过题目的百分比
	private TextView rightQuestionTextView;// 正确数量
	private TextView rightRateTextView;// 正确率
	private TextView wrongQuestionTextView;// 错误题数
	private TextView wrongRateTextView;// 错误率
	double data[] = new double[3];// 展示数据
	int[] COLORS;// 颜色

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistic);
		findViewById();
		getActionBar().setTitle("统计分析");
		new InitChartData().execute();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		mLinear = (LinearLayout) findViewById(R.id.chart);
		allQuestionTextView = (TextView) findViewById(R.id.all_question);
		allRateTextView = (TextView) findViewById(R.id.all_rate);
		rightQuestionTextView = (TextView) findViewById(R.id.right_question);
		rightRateTextView = (TextView) findViewById(R.id.right_rate);
		wrongQuestionTextView = (TextView) findViewById(R.id.wrong_question);
		wrongRateTextView = (TextView) findViewById(R.id.wrong_rate);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		if (mChartView == null) {// 为空需要从ChartFactory获取PieChartView
			COLORS = new int[] { Color.argb(255, 46, 109, 234), Color.argb(255, 153, 204, 0), Color.MAGENTA,
					Color.CYAN, Color.argb(255, 255, 187, 51) };

			final CategorySeries mSeries = new CategorySeries("");
			final String[] lables = new String[] { "未做", "做对", "做错" };

			final DefaultRenderer mRenderer = new DefaultRenderer();

			double VALUE = 0;// 总数
			mRenderer.setZoomButtonsVisible(true);// 显示放大缩小功能按钮
			mRenderer.setStartAngle(180);// 设置为水平开始
			mRenderer.setDisplayValues(true);// 显示数据
			mRenderer.setFitLegend(true);// 设置是否显示图例
			mRenderer.setLegendTextSize(30);// 设置图例字体大小
			mRenderer.setLegendHeight(30);// 设置图例高度
			mRenderer.setLabelsColor(Color.BLACK);// 设置字体颜色
			mRenderer.setChartTitle("练习统计");// 设置饼图标题
			mRenderer.setChartTitleTextSize(40);// 设置饼图标题大小
			mRenderer.setMargins(new int[] { 20, 0, 0, 20 });

			for (int i = 0; i < data.length; i++)
				VALUE += data[i];
			for (int i = 0; i < data.length; i++) {
				mSeries.add(lables[i], data[i] / VALUE);// 设置种类名称和对应的数值，前面是（key,value）键值对
				SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
				renderer.setColor(COLORS[i]);// 设置描绘器的颜色

				renderer.setChartValuesFormat(NumberFormat.getPercentInstance());// 设置百分比
				mRenderer.addSeriesRenderer(renderer);// 将最新的描绘器添加到DefaultRenderer中
			}

			mChartView = ChartFactory.getPieChartView(getApplicationContext(), mSeries, mRenderer);// 构建mChartView

			mRenderer.setClickEnabled(true);// 允许点击事件
			mChartView.setOnClickListener(new View.OnClickListener() {// 具体内容
						@Override
						public void onClick(View v) {
							SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();// 获取当前的类别和指针
							if (seriesSelection == null) {
								Toast.makeText(getApplicationContext(), "您未选择数据", Toast.LENGTH_SHORT).show();
							} else {
								for (int i = 0; i < mSeries.getItemCount(); i++) {
									mRenderer.getSeriesRendererAt(i).setHighlighted(
											i == seriesSelection.getPointIndex());
								}
								mChartView.repaint();
								Toast.makeText(
										getApplicationContext(),
										"您的" + lables[seriesSelection.getPointIndex()] + "题目数量百分比为  "
												+ NumberFormat.getPercentInstance().format(seriesSelection.getValue()),
										Toast.LENGTH_SHORT).show();
							}
						}
					});
			mLinear.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		} else {
			mChartView.repaint();
		}
	}

	/**
	 * 
	*    
	* 项目名称：ExamHelper   
	* 类名称：InitChartData   
	* 类描述：   
	* 创建人：张帅  
	* 创建时间：2014-1-29 下午6:23:54   
	* 修改人：张帅   
	* 修改时间：2014-1-29 下午6:23:54   
	* 修改备注：   
	* @version    
	*
	 */
	private class InitChartData extends AsyncTask<Void, Void, Void> {
		Dialog dialog;
		StudyRecordService studyRecordService;
		SingleChoiceService singleChoiceService;
		MultiChoiceService multiChoiceService;
		MaterialAnalysisService materialAnalysisService;
		QuestionTypeService questionTypeService;
		int allQuestion;// 总题目数量
		int allFinished;// 完成题目数量
		int finishedRate;// 完成率
		int sinCount;// 单选题数量
		int mulCount;// 多选题数量
		int materCount;// 材料题数量
		int sinFinished;// 单选题完成数量
		int mulFinished;// 多选题完成数量
		int materFinished;// 材料题完成数量
		int sinRight = 0;// 单选题正确数量
		int mulRight = 0;// 多选题正确数量
		int materRight = 0;// 材料题正确数量
		int allRight;// 正确题目数量
		int allWrong;// 错误题目数量
		int rightRate;// 正确率
		int wrongRate;// 错误率

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = StatisticActivity.this.showProgressDialog();
			studyRecordService = StudyRecordService.getInstance(StatisticActivity.this);
			singleChoiceService = SingleChoiceService.getInstance(StatisticActivity.this);
			multiChoiceService = MultiChoiceService.getInstance(StatisticActivity.this);
			materialAnalysisService = MaterialAnalysisService.getInstance(StatisticActivity.this);
			questionTypeService = QuestionTypeService.getInstance(StatisticActivity.this);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			sinCount = singleChoiceService.loadAllSingleChoice().size();
			mulCount = multiChoiceService.loadAllMultiChoice().size();
			materCount = materialAnalysisService.loadAllMaterialAnalysis().size();
			List<StudyRecord> sinList = studyRecordService.studyRecordDao.queryBuilder()
					.where(Properties.QuestionType_id.eq(questionTypeService.getIdByName(DefaultValues.SINGLE_CHOICE)))
					.list();
			List<StudyRecord> mulList = studyRecordService.studyRecordDao.queryBuilder()
					.where(Properties.QuestionType_id.eq(questionTypeService.getIdByName(DefaultValues.MULTI_CHOICE)))
					.list();
			List<StudyRecord> materList = studyRecordService.studyRecordDao
					.queryBuilder()
					.where(Properties.QuestionType_id.eq(questionTypeService
							.getIdByName(DefaultValues.MATERIAL_ANALYSIS))).list();
			sinFinished = sinList.size();
			mulFinished = mulList.size();
			materFinished = materList.size();

			for (StudyRecord studyRecord : sinList) {
				if (studyRecord.getIs_right()) {
					sinRight++;
				}
			}
			for (StudyRecord studyRecord : mulList) {
				if (studyRecord.getIs_right()) {
					mulRight++;
				}
			}
			for (StudyRecord studyRecord : materList) {
				if (studyRecord.getIs_right()) {
					materRight++;
				}
			}

			allFinished = sinFinished + mulFinished + materFinished;
			allRight = sinRight + mulRight + materRight;
			allQuestion = sinCount + mulCount + materCount;
			allWrong = allFinished - allRight;
			finishedRate = (int) (((float) allFinished / (float) allQuestion) * 100);
			rightRate = (int) (((float) allRight / (float) allFinished) * 100);
			wrongRate = (int) (((float) allWrong / (float) allFinished) * 100);

			data[0] = allQuestion - allFinished;
			data[1] = allRight;
			data[2] = allWrong;

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			initView();
			allQuestionTextView.setText("  " + allFinished + "  ");
			allQuestionTextView.setTextColor(COLORS[0]);
			allRateTextView.setText("  " + finishedRate + "%" + "  ");
			allRateTextView.setTextColor(COLORS[0]);

			rightQuestionTextView.setText("  " + allRight + "  ");
			rightQuestionTextView.setTextColor(COLORS[1]);
			rightRateTextView.setText("  " + rightRate + "%" + "  ");
			rightRateTextView.setTextColor(COLORS[1]);

			wrongQuestionTextView.setText("  " + allWrong + "  ");
			wrongQuestionTextView.setTextColor(COLORS[2]);
			wrongRateTextView.setText("  " + wrongRate + "% ");
			wrongRateTextView.setTextColor(COLORS[2]);
			dialog.dismiss();
		}
	}
}

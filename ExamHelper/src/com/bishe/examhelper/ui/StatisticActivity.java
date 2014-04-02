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
	private GraphicalView mChartView;// ��ʾPieChart
	private LinearLayout mLinear;// ���ַ�ʽ
	private TextView allQuestionTextView;// ������
	private TextView allRateTextView;// ������Ŀ�İٷֱ�
	private TextView rightQuestionTextView;// ��ȷ����
	private TextView rightRateTextView;// ��ȷ��
	private TextView wrongQuestionTextView;// ��������
	private TextView wrongRateTextView;// ������
	double data[] = new double[3];// չʾ����
	int[] COLORS;// ��ɫ

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistic);
		findViewById();
		getActionBar().setTitle("ͳ�Ʒ���");
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
		if (mChartView == null) {// Ϊ����Ҫ��ChartFactory��ȡPieChartView
			COLORS = new int[] { Color.argb(255, 46, 109, 234), Color.argb(255, 153, 204, 0), Color.MAGENTA,
					Color.CYAN, Color.argb(255, 255, 187, 51) };

			final CategorySeries mSeries = new CategorySeries("");
			final String[] lables = new String[] { "δ��", "����", "����" };

			final DefaultRenderer mRenderer = new DefaultRenderer();

			double VALUE = 0;// ����
			mRenderer.setZoomButtonsVisible(true);// ��ʾ�Ŵ���С���ܰ�ť
			mRenderer.setStartAngle(180);// ����Ϊˮƽ��ʼ
			mRenderer.setDisplayValues(true);// ��ʾ����
			mRenderer.setFitLegend(true);// �����Ƿ���ʾͼ��
			mRenderer.setLegendTextSize(30);// ����ͼ�������С
			mRenderer.setLegendHeight(30);// ����ͼ���߶�
			mRenderer.setLabelsColor(Color.BLACK);// ����������ɫ
			mRenderer.setChartTitle("��ϰͳ��");// ���ñ�ͼ����
			mRenderer.setChartTitleTextSize(40);// ���ñ�ͼ�����С
			mRenderer.setMargins(new int[] { 20, 0, 0, 20 });

			for (int i = 0; i < data.length; i++)
				VALUE += data[i];
			for (int i = 0; i < data.length; i++) {
				mSeries.add(lables[i], data[i] / VALUE);// �����������ƺͶ�Ӧ����ֵ��ǰ���ǣ�key,value����ֵ��
				SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
				renderer.setColor(COLORS[i]);// �������������ɫ

				renderer.setChartValuesFormat(NumberFormat.getPercentInstance());// ���ðٷֱ�
				mRenderer.addSeriesRenderer(renderer);// �����µ��������ӵ�DefaultRenderer��
			}

			mChartView = ChartFactory.getPieChartView(getApplicationContext(), mSeries, mRenderer);// ����mChartView

			mRenderer.setClickEnabled(true);// �������¼�
			mChartView.setOnClickListener(new View.OnClickListener() {// ��������
						@Override
						public void onClick(View v) {
							SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();// ��ȡ��ǰ������ָ��
							if (seriesSelection == null) {
								Toast.makeText(getApplicationContext(), "��δѡ������", Toast.LENGTH_SHORT).show();
							} else {
								for (int i = 0; i < mSeries.getItemCount(); i++) {
									mRenderer.getSeriesRendererAt(i).setHighlighted(
											i == seriesSelection.getPointIndex());
								}
								mChartView.repaint();
								Toast.makeText(
										getApplicationContext(),
										"����" + lables[seriesSelection.getPointIndex()] + "��Ŀ�����ٷֱ�Ϊ  "
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
	* ��Ŀ���ƣ�ExamHelper   
	* �����ƣ�InitChartData   
	* ��������   
	* �����ˣ���˧  
	* ����ʱ�䣺2014-1-29 ����6:23:54   
	* �޸��ˣ���˧   
	* �޸�ʱ�䣺2014-1-29 ����6:23:54   
	* �޸ı�ע��   
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
		int allQuestion;// ����Ŀ����
		int allFinished;// �����Ŀ����
		int finishedRate;// �����
		int sinCount;// ��ѡ������
		int mulCount;// ��ѡ������
		int materCount;// ����������
		int sinFinished;// ��ѡ���������
		int mulFinished;// ��ѡ���������
		int materFinished;// �������������
		int sinRight = 0;// ��ѡ����ȷ����
		int mulRight = 0;// ��ѡ����ȷ����
		int materRight = 0;// ��������ȷ����
		int allRight;// ��ȷ��Ŀ����
		int allWrong;// ������Ŀ����
		int rightRate;// ��ȷ��
		int wrongRate;// ������

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

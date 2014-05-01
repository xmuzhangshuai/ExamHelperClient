package com.bishe.examhelper.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bishe.examhelper.R;
import com.bishe.examhelper.base.BaseActivity;
import com.bishe.examhelper.entities.ExamQuestion;
import com.bishe.examhelper.entities.ExamSection;
import com.bishe.examhelper.entities.Examination;
import com.bishe.examhelper.entities.MaterialAnalysis;
import com.bishe.examhelper.entities.MultiChoice;
import com.bishe.examhelper.entities.QuestionsOfMaterial;
import com.bishe.examhelper.entities.Section;
import com.bishe.examhelper.entities.SingleChoice;
import com.bishe.examhelper.entities.Subject;
import com.bishe.examhelper.service.CollectionService;
import com.bishe.examhelper.service.DivideIntoGroup;
import com.bishe.examhelper.service.ErrorQuestionsService;
import com.bishe.examhelper.service.ExaminationService;
import com.bishe.examhelper.service.MaterialAnalysisService;
import com.bishe.examhelper.service.MultiChoiceService;
import com.bishe.examhelper.service.NoteService;
import com.bishe.examhelper.service.QuestionTypeService;
import com.bishe.examhelper.service.SectionService;
import com.bishe.examhelper.service.SingleChoiceService;
import com.bishe.examhelper.service.StudyRecordService;
import com.bishe.examhelper.service.SubjectService;
import com.bishe.examhelper.utils.FastJsonTool;
import com.bishe.examhelper.utils.HttpUtil;
import com.bishe.examhelper.utils.NetworkUtils;
import com.jsonobjects.JExamQuestion;
import com.jsonobjects.JExamSection;
import com.jsonobjects.JExamination;
import com.jsonobjects.JMaterialAnalysis;
import com.jsonobjects.JMultiChoice;
import com.jsonobjects.JQuestionsOfMaterial;
import com.jsonobjects.JSection;
import com.jsonobjects.JSingleChoice;
import com.jsonobjects.JSubject;

/**
 * 类名称：ChangeSubjectActivity
 * 类描述：切换科目
 * 创建人： 张帅
 * 创建时间：2014-4-30 下午9:54:35
 *
 */
public class ChangeSubjectActivity extends BaseActivity {
	ListView subjectListView;
	EditText searchContentEditText;
	ImageView searchImageView;
	List<JSubject> jSubjectList;
	List<String> subjectNameList;
	ArrayAdapter<String> adapter;

	// 提醒用户网络状况有异常
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (!NetworkUtils.isNetworkAvailable(ChangeSubjectActivity.this)) {
				NetworkUtils.networkStateTips(ChangeSubjectActivity.this);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_subject);
		jSubjectList = new ArrayList<JSubject>();
		subjectNameList = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(ChangeSubjectActivity.this, R.layout.exam_guide_list_item,
				R.id.list_item_title, subjectNameList);
		findViewById();

		subjectListView.setAdapter(adapter);

		new GetSubjectListTask().execute();
		initView();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 注册广播
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		ChangeSubjectActivity.this.registerReceiver(broadcastReceiver, intentFilter);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// 卸载广播
		if (broadcastReceiver != null) {
			ChangeSubjectActivity.this.unregisterReceiver(broadcastReceiver);
		}
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		subjectListView = (ListView) findViewById(R.id.subjectList);
		searchContentEditText = (EditText) findViewById(R.id.searchContent);
		searchImageView = (ImageView) findViewById(R.id.search);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		getActionBar().setTitle("选择科目");
		getActionBar().setIcon(R.drawable.function_random_practice);

		searchImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				subjectNameList.removeAll(subjectNameList);

				String searchText = searchContentEditText.getText().toString();
				List<JSubject> temp = new ArrayList<JSubject>();
				if (searchText.length() > 0) {
					if (jSubjectList != null) {
						for (int i = 0; i < jSubjectList.size(); i++) {
							if (!jSubjectList.get(i).getSubject_name().contains(searchText.trim())) {
								temp.add(jSubjectList.get(i));
							} else {
								subjectNameList.add(jSubjectList.get(i).getSubject_name());
							}
						}
						jSubjectList.removeAll(temp);
						adapter.notifyDataSetChanged();
					}
				} else {
					new GetSubjectListTask().execute();
				}
			}
		});

		subjectListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				// TODO Auto-generated method stub
				if (NetworkUtils.checkNetAndTip(ChangeSubjectActivity.this)) {
					if (NetworkUtils.isWifi(ChangeSubjectActivity.this)) {
						new AlertDialog.Builder(ChangeSubjectActivity.this).setTitle("温馨提示").setMessage("是否确定选择该科目？")
								.setIcon(R.drawable.icon_warning)
								.setPositiveButton("是", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										new ChooseSubjectTask().execute(jSubjectList.get(position));
									}
								}).setNegativeButton("算了吧", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
									}
								}).show();
					} else {
						new AlertDialog.Builder(ChangeSubjectActivity.this).setTitle("温馨提示")
								.setMessage("您正在使用非wifi环境，是否继续？").setIcon(R.drawable.icon_warning)
								.setPositiveButton("继续", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub

										new AlertDialog.Builder(ChangeSubjectActivity.this).setTitle("温馨提示")
												.setMessage("是否确定选择该科目？").setIcon(R.drawable.icon_warning)
												.setPositiveButton("是", new DialogInterface.OnClickListener() {
													@Override
													public void onClick(DialogInterface dialog, int which) {
														// TODO Auto-generated method stub
														new ChooseSubjectTask().execute(jSubjectList.get(position));
													}
												}).setNegativeButton("算了吧", new DialogInterface.OnClickListener() {
													@Override
													public void onClick(DialogInterface dialog, int which) {
														// TODO Auto-generated method stub
													}
												}).show();
									}
								}).setNegativeButton("算了吧", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
									}
								}).show();
					}
				}
			}
		});
	}

	/**
	 * 
	 * 类名称：GetSubjectListTask
	 * 类描述：从网络端获取列表
	 * 创建人： 张帅
	 * 创建时间：2014-4-30 下午4:32:44
	 *
	 */
	class GetSubjectListTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String URL = "ChangeSubjectServlet";
			try {
				jSubjectList = new ArrayList<JSubject>();
				jSubjectList = FastJsonTool.getObjectList(HttpUtil.getRequest(URL), JSubject.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (jSubjectList != null) {
				Subject subject = SubjectService.getInstance(ChangeSubjectActivity.this).getCurrentSubject();
				if (subject != null) {
					for (JSubject jSubject : jSubjectList) {
						if (subject.getId() == jSubject.getId()) {
							jSubjectList.remove(jSubject);
							break;
						}
					}
				}
				for (JSubject jSubject : jSubjectList) {
					subjectNameList.add(jSubject.getSubject_name());
				}
			}
			adapter.notifyDataSetChanged();
		}

	}

	/**
	 * 
	 * 类名称：ChooseSubjectTask
	 * 类描述：选择科目异步任务
	 * 创建人： 张帅
	 * 创建时间：2014-4-30 下午4:31:45
	 *
	 */
	class ChooseSubjectTask extends AsyncTask<JSubject, Void, Void> {
		ProgressDialog dialog = new ProgressDialog(ChangeSubjectActivity.this);
		SectionService sectionService = SectionService.getInstance(ChangeSubjectActivity.this);
		SubjectService subjectService = SubjectService.getInstance(ChangeSubjectActivity.this);
		QuestionTypeService questionTypeService = QuestionTypeService.getInstance(ChangeSubjectActivity.this);
		SingleChoiceService singleChoiceService = SingleChoiceService.getInstance(ChangeSubjectActivity.this);
		MultiChoiceService multiChoiceService = MultiChoiceService.getInstance(ChangeSubjectActivity.this);
		MaterialAnalysisService materialAnalysisService = MaterialAnalysisService
				.getInstance(ChangeSubjectActivity.this);
		ExaminationService examinationService = ExaminationService.getInstance(ChangeSubjectActivity.this);
		Subject subject = null;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.setTitle("提示");
			dialog.setMessage("正在下载题库,请稍后...");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(JSubject... params) {
			// TODO Auto-generated method stub
			String URL = "ChangeSubjectServlet";

			//删除科目
			subjectService.subjectDao.deleteAll();
			//删除章节
			sectionService.deleteAllSections();
			//删除单选题
			singleChoiceService.singleChoiceDao.deleteAll();
			//删除多选题
			multiChoiceService.multiChoiceDao.deleteAll();
			//删除材料题
			materialAnalysisService.materialAnalysisDao.deleteAll();
			//删除材料题小题
			materialAnalysisService.questionsOfMaterialDao.deleteAll();
			//删除试卷
			examinationService.mExaminationDao.deleteAll();
			//删除试卷章节
			examinationService.mExamSectionDao.deleteAll();
			//删除试卷题目
			examinationService.mExamQuestionDao.deleteAll();

			//重下科目
			subject = new Subject(params[0].getId(), params[0].getSubject_name());
			subjectService.subjectDao.insert(subject);

			int subjectId = subject.getId().intValue();
			Map<String, String> map = new HashMap<String, String>();

			//重下章节
			map = new HashMap<String, String>();
			map.put("subjectId", String.valueOf(subjectId));
			map.put("questionType", "section");
			try {
				String result = HttpUtil.postRequest(URL, map);
				List<JSection> jSections = FastJsonTool.getObjectList(result, JSection.class);
				if (jSections != null) {
					for (JSection jSection : jSections) {
						Section section = new Section(jSection.getId(), jSection.getSection_name(),
								jSection.getSubject_id());
						sectionService.addSection(section);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//重下单选题
			map = new HashMap<String, String>();
			map.put("subjectId", String.valueOf(subjectId));
			map.put("questionType", "singleChoice");
			try {
				String result = HttpUtil.postRequest(URL, map);
				singleChoiceService.singleChoiceDao.deleteAll();
				List<JSingleChoice> jSingleChoiceList = FastJsonTool.getObjectList(result, JSingleChoice.class);
				if (jSingleChoiceList != null) {
					for (JSingleChoice jSingleChoice : jSingleChoiceList) {
						SingleChoice singleChoice = new SingleChoice(jSingleChoice.getId(),
								jSingleChoice.getQuestion_stem(), jSingleChoice.getOptionA(),
								jSingleChoice.getOptionB(), jSingleChoice.getOptionC(), jSingleChoice.getOptionD(),
								jSingleChoice.getOptionE(), jSingleChoice.getAnswer(), jSingleChoice.getAnalysis(),
								jSingleChoice.getRemark(), jSingleChoice.getFlag(), jSingleChoice.getSection_id());
						singleChoiceService.singleChoiceDao.insert(singleChoice);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//重下多选题
			map = new HashMap<String, String>();
			map.put("subjectId", String.valueOf(subjectId));
			map.put("questionType", "multiChoice");
			try {
				String result = HttpUtil.postRequest(URL, map);
				multiChoiceService.multiChoiceDao.deleteAll();
				List<JMultiChoice> jMultiChoiceList = FastJsonTool.getObjectList(result, JMultiChoice.class);
				if (jMultiChoiceList != null) {
					for (JMultiChoice jMultiChoice : jMultiChoiceList) {
						MultiChoice multiChoice = new MultiChoice(jMultiChoice.getId(),
								jMultiChoice.getQuestion_stem(), jMultiChoice.getOptionA(), jMultiChoice.getOptionB(),
								jMultiChoice.getOptionC(), jMultiChoice.getOptionD(), jMultiChoice.getOptionE(),
								jMultiChoice.getOptionF(), jMultiChoice.getAnswerA(), jMultiChoice.getAnswerB(),
								jMultiChoice.getAnswerC(), jMultiChoice.getAnswerD(), jMultiChoice.getAnswerE(),
								jMultiChoice.getAnswerF(), jMultiChoice.getAnalysis(), jMultiChoice.getRemark(),
								jMultiChoice.getFlag(), jMultiChoice.getSection_id());
						multiChoiceService.multiChoiceDao.insert(multiChoice);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//重下材料题
			map = new HashMap<String, String>();
			map.put("subjectId", String.valueOf(subjectId));
			map.put("questionType", "materialAnalisis");
			try {
				String result = HttpUtil.postRequest(URL, map);
				materialAnalysisService.materialAnalysisDao.deleteAll();
				List<JMaterialAnalysis> jMaterialAnalysiList = FastJsonTool.getObjectList(result,
						JMaterialAnalysis.class);
				if (jMaterialAnalysiList != null) {
					for (JMaterialAnalysis jMaterialAnalysis : jMaterialAnalysiList) {
						MaterialAnalysis materialAnalysis = new MaterialAnalysis(jMaterialAnalysis.getId(),
								jMaterialAnalysis.getMaterial(), jMaterialAnalysis.getMaterial(),
								jMaterialAnalysis.getRemark(), jMaterialAnalysis.getFlag(),
								jMaterialAnalysis.getSection_id());
						materialAnalysisService.materialAnalysisDao.insert(materialAnalysis);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//重下材料题小题
			map = new HashMap<String, String>();
			map.put("subjectId", String.valueOf(subjectId));
			map.put("questionType", "questionOfMateria");
			try {
				String result = HttpUtil.postRequest(URL, map);
				materialAnalysisService.questionsOfMaterialDao.deleteAll();
				List<JQuestionsOfMaterial> jQuestionsOfMaterialList = FastJsonTool.getObjectList(result,
						JQuestionsOfMaterial.class);
				if (jQuestionsOfMaterialList != null) {
					for (JQuestionsOfMaterial jQuestionsOfMaterial : jQuestionsOfMaterialList) {
						QuestionsOfMaterial questionsOfMaterial = new QuestionsOfMaterial(jQuestionsOfMaterial.getId(),
								jQuestionsOfMaterial.getQusetion_number(), jQuestionsOfMaterial.getQuestion_stem(),
								jQuestionsOfMaterial.getAnswer(), jQuestionsOfMaterial.getAnalysis(),
								jQuestionsOfMaterial.getScore(), jQuestionsOfMaterial.getMaterial_id());
						materialAnalysisService.questionsOfMaterialDao.insert(questionsOfMaterial);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//重下试卷
			map = new HashMap<String, String>();
			map.put("subjectId", String.valueOf(subjectId));
			map.put("questionType", "examination");
			try {
				String result = HttpUtil.postRequest(URL, map);
				examinationService.mExaminationDao.deleteAll();
				List<JExamination> jExaminationList = FastJsonTool.getObjectList(result, JExamination.class);
				if (jExaminationList != null) {
					for (JExamination jExamination : jExaminationList) {
						Examination examination = new Examination(jExamination.getId(), jExamination.getExam_type(),
								jExamination.getExam_name(), jExamination.getExam_request(),
								jExamination.getExam_time(), jExamination.getSubject_id());
						examinationService.mExaminationDao.insert(examination);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//重下试卷章节
			map = new HashMap<String, String>();
			map.put("subjectId", String.valueOf(subjectId));
			map.put("questionType", "examSection");
			try {
				String result = HttpUtil.postRequest(URL, map);
				examinationService.mExamSectionDao.deleteAll();
				List<JExamSection> jExamSectionList = FastJsonTool.getObjectList(result, JExamSection.class);
				if (jExamSectionList != null) {
					for (JExamSection jExamSection : jExamSectionList) {
						ExamSection examSection = new ExamSection(jExamSection.getId(), jExamSection.getRequest(),
								jExamSection.getQuestion_num(), jExamSection.getQuestion_score(),
								jExamSection.getQuestionType_id(), jExamSection.getExam_id());
						examinationService.mExamSectionDao.insert(examSection);
					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//重下试卷题目
			map = new HashMap<String, String>();
			map.put("subjectId", String.valueOf(subjectId));
			map.put("questionType", "examQuestion");
			try {
				String result = HttpUtil.postRequest(URL, map);
				examinationService.mExamQuestionDao.deleteAll();
				List<JExamQuestion> jExamQuestionList = FastJsonTool.getObjectList(result, JExamQuestion.class);
				if (jExamQuestionList != null) {
					for (JExamQuestion jExamQuestion : jExamQuestionList) {
						ExamQuestion examQuestion = new ExamQuestion(jExamQuestion.getId(),
								jExamQuestion.getQuestion_number(), jExamQuestion.getQuestion_id(),
								jExamQuestion.getExanSection_id());
						examinationService.mExamQuestionDao.insert(examQuestion);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			new DivideIntoGroup(ChangeSubjectActivity.this).divideIntoGroup();// 对数据分组
			StudyRecordService.getInstance(ChangeSubjectActivity.this).studyRecordDao.deleteAll();
			CollectionService.getInstance(ChangeSubjectActivity.this).mCollectionDao.deleteAll();
			NoteService.getInstance(ChangeSubjectActivity.this).noteDao.deleteAll();
			ErrorQuestionsService.getInstance(ChangeSubjectActivity.this).errorQuestionsDao.deleteAll();
			dialog.cancel();
			Toast.makeText(ChangeSubjectActivity.this, "切换科目成功", 1).show();
			ChangeSubjectActivity.this.startActivity(new Intent(ChangeSubjectActivity.this,MainActivity.class));
			ChangeSubjectActivity.this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		}
	}

}

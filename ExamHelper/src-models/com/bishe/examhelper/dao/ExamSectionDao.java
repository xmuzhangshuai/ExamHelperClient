package com.bishe.examhelper.dao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import com.bishe.examhelper.entities.Examination;
import com.bishe.examhelper.entities.QuestionType;

import com.bishe.examhelper.entities.ExamSection;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table EXAM_SECTION.
*/
public class ExamSectionDao extends AbstractDao<ExamSection, Long> {

    public static final String TABLENAME = "EXAM_SECTION";

    /**
     * Properties of entity ExamSection.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Request = new Property(1, String.class, "request", false, "REQUEST");
        public final static Property Question_num = new Property(2, Integer.class, "question_num", false, "QUESTION_NUM");
        public final static Property Question_score = new Property(3, Integer.class, "question_score", false, "QUESTION_SCORE");
        public final static Property QuestionType_id = new Property(4, long.class, "questionType_id", false, "QUESTION_TYPE_ID");
        public final static Property Exam_id = new Property(5, long.class, "exam_id", false, "EXAM_ID");
    };

    private DaoSession daoSession;

    private Query<ExamSection> questionType_ExamSectionListQuery;
    private Query<ExamSection> examination_ExamSectionListQuery;

    public ExamSectionDao(DaoConfig config) {
        super(config);
    }
    
    public ExamSectionDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'EXAM_SECTION' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'REQUEST' TEXT," + // 1: request
                "'QUESTION_NUM' INTEGER," + // 2: question_num
                "'QUESTION_SCORE' INTEGER," + // 3: question_score
                "'QUESTION_TYPE_ID' INTEGER NOT NULL ," + // 4: questionType_id
                "'EXAM_ID' INTEGER NOT NULL );"); // 5: exam_id
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'EXAM_SECTION'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ExamSection entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String request = entity.getRequest();
        if (request != null) {
            stmt.bindString(2, request);
        }
 
        Integer question_num = entity.getQuestion_num();
        if (question_num != null) {
            stmt.bindLong(3, question_num);
        }
 
        Integer question_score = entity.getQuestion_score();
        if (question_score != null) {
            stmt.bindLong(4, question_score);
        }
        stmt.bindLong(5, entity.getQuestionType_id());
        stmt.bindLong(6, entity.getExam_id());
    }

    @Override
    protected void attachEntity(ExamSection entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ExamSection readEntity(Cursor cursor, int offset) {
        ExamSection entity = new ExamSection( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // request
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // question_num
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // question_score
            cursor.getLong(offset + 4), // questionType_id
            cursor.getLong(offset + 5) // exam_id
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ExamSection entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setRequest(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setQuestion_num(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setQuestion_score(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setQuestionType_id(cursor.getLong(offset + 4));
        entity.setExam_id(cursor.getLong(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(ExamSection entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(ExamSection entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "examSectionList" to-many relationship of QuestionType. */
    public List<ExamSection> _queryQuestionType_ExamSectionList(long questionType_id) {
        synchronized (this) {
            if (questionType_ExamSectionListQuery == null) {
                QueryBuilder<ExamSection> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.QuestionType_id.eq(null));
                questionType_ExamSectionListQuery = queryBuilder.build();
            }
        }
        Query<ExamSection> query = questionType_ExamSectionListQuery.forCurrentThread();
        query.setParameter(0, questionType_id);
        return query.list();
    }

    /** Internal query to resolve the "examSectionList" to-many relationship of Examination. */
    public List<ExamSection> _queryExamination_ExamSectionList(long exam_id) {
        synchronized (this) {
            if (examination_ExamSectionListQuery == null) {
                QueryBuilder<ExamSection> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.Exam_id.eq(null));
                examination_ExamSectionListQuery = queryBuilder.build();
            }
        }
        Query<ExamSection> query = examination_ExamSectionListQuery.forCurrentThread();
        query.setParameter(0, exam_id);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getQuestionTypeDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getExaminationDao().getAllColumns());
            builder.append(" FROM EXAM_SECTION T");
            builder.append(" LEFT JOIN QUESTION_TYPE T0 ON T.'QUESTION_TYPE_ID'=T0.'_id'");
            builder.append(" LEFT JOIN EXAMINATION T1 ON T.'EXAM_ID'=T1.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected ExamSection loadCurrentDeep(Cursor cursor, boolean lock) {
        ExamSection entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        QuestionType questionType = loadCurrentOther(daoSession.getQuestionTypeDao(), cursor, offset);
         if(questionType != null) {
            entity.setQuestionType(questionType);
        }
        offset += daoSession.getQuestionTypeDao().getAllColumns().length;

        Examination examination = loadCurrentOther(daoSession.getExaminationDao(), cursor, offset);
         if(examination != null) {
            entity.setExamination(examination);
        }

        return entity;    
    }

    public ExamSection loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<ExamSection> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<ExamSection> list = new ArrayList<ExamSection>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<ExamSection> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<ExamSection> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}

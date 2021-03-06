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

import com.bishe.examhelper.entities.Subject;

import com.bishe.examhelper.entities.Examination;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table EXAMINATION.
*/
public class ExaminationDao extends AbstractDao<Examination, Long> {

    public static final String TABLENAME = "EXAMINATION";

    /**
     * Properties of entity Examination.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Exam_type = new Property(1, String.class, "exam_type", false, "EXAM_TYPE");
        public final static Property Exam_name = new Property(2, String.class, "exam_name", false, "EXAM_NAME");
        public final static Property Exam_request = new Property(3, String.class, "exam_request", false, "EXAM_REQUEST");
        public final static Property Exam_time = new Property(4, Integer.class, "exam_time", false, "EXAM_TIME");
        public final static Property Subject_id = new Property(5, long.class, "subject_id", false, "SUBJECT_ID");
    };

    private DaoSession daoSession;

    private Query<Examination> subject_ExaminationListQuery;

    public ExaminationDao(DaoConfig config) {
        super(config);
    }
    
    public ExaminationDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'EXAMINATION' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'EXAM_TYPE' TEXT," + // 1: exam_type
                "'EXAM_NAME' TEXT," + // 2: exam_name
                "'EXAM_REQUEST' TEXT," + // 3: exam_request
                "'EXAM_TIME' INTEGER," + // 4: exam_time
                "'SUBJECT_ID' INTEGER NOT NULL );"); // 5: subject_id
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'EXAMINATION'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Examination entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String exam_type = entity.getExam_type();
        if (exam_type != null) {
            stmt.bindString(2, exam_type);
        }
 
        String exam_name = entity.getExam_name();
        if (exam_name != null) {
            stmt.bindString(3, exam_name);
        }
 
        String exam_request = entity.getExam_request();
        if (exam_request != null) {
            stmt.bindString(4, exam_request);
        }
 
        Integer exam_time = entity.getExam_time();
        if (exam_time != null) {
            stmt.bindLong(5, exam_time);
        }
        stmt.bindLong(6, entity.getSubject_id());
    }

    @Override
    protected void attachEntity(Examination entity) {
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
    public Examination readEntity(Cursor cursor, int offset) {
        Examination entity = new Examination( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // exam_type
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // exam_name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // exam_request
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // exam_time
            cursor.getLong(offset + 5) // subject_id
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Examination entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setExam_type(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setExam_name(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setExam_request(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setExam_time(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setSubject_id(cursor.getLong(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Examination entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Examination entity) {
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
    
    /** Internal query to resolve the "examinationList" to-many relationship of Subject. */
    public List<Examination> _querySubject_ExaminationList(long subject_id) {
        synchronized (this) {
            if (subject_ExaminationListQuery == null) {
                QueryBuilder<Examination> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.Subject_id.eq(null));
                subject_ExaminationListQuery = queryBuilder.build();
            }
        }
        Query<Examination> query = subject_ExaminationListQuery.forCurrentThread();
        query.setParameter(0, subject_id);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getSubjectDao().getAllColumns());
            builder.append(" FROM EXAMINATION T");
            builder.append(" LEFT JOIN SUBJECT T0 ON T.'SUBJECT_ID'=T0.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Examination loadCurrentDeep(Cursor cursor, boolean lock) {
        Examination entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Subject subject = loadCurrentOther(daoSession.getSubjectDao(), cursor, offset);
         if(subject != null) {
            entity.setSubject(subject);
        }

        return entity;    
    }

    public Examination loadDeep(Long key) {
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
    public List<Examination> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Examination> list = new ArrayList<Examination>(count);
        
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
    
    protected List<Examination> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Examination> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}

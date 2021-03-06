package com.bishe.examhelper.entities;

import java.util.List;
import com.bishe.examhelper.dao.DaoSession;
import de.greenrobot.dao.DaoException;

import com.bishe.examhelper.dao.ExaminationDao;
import com.bishe.examhelper.dao.SectionDao;
import com.bishe.examhelper.dao.SubjectDao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table SUBJECT.
 */
public class Subject implements java.io.Serializable {

    private Long id;
    /** Not-null value. */
    private String subject_name;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient SubjectDao myDao;

    private List<Section> sectionList;
    private List<Examination> examinationList;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Subject() {
    }

    public Subject(Long id) {
        this.id = id;
    }

    public Subject(Long id, String subject_name) {
        this.id = id;
        this.subject_name = subject_name;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSubjectDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getSubject_name() {
        return subject_name;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Section> getSectionList() {
        if (sectionList == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SectionDao targetDao = daoSession.getSectionDao();
            List<Section> sectionListNew = targetDao._querySubject_SectionList(id);
            synchronized (this) {
                if(sectionList == null) {
                    sectionList = sectionListNew;
                }
            }
        }
        return sectionList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetSectionList() {
        sectionList = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Examination> getExaminationList() {
        if (examinationList == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ExaminationDao targetDao = daoSession.getExaminationDao();
            List<Examination> examinationListNew = targetDao._querySubject_ExaminationList(id);
            synchronized (this) {
                if(examinationList == null) {
                    examinationList = examinationListNew;
                }
            }
        }
        return examinationList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetExaminationList() {
        examinationList = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}

package com.kristian.android.simpletodo;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by kristianss27 on 8/28/16.
 */

@Entity(
        active = true,
        generateConstructors = true,
        generateGettersSetters = true
)
public class Item {
    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "TEXT")
    @NotNull
    private String text;

    @Property(nameInDb = "COMMENT")
    private String comment;


    @Property(nameInDb = "DATE")
    private java.util.Date date;

    @Property(nameInDb = "STATUS")
    private String status;

    @Transient
    private String extra;

/** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;

/** Used for active entity operations. */
@Generated(hash = 182764869)
private transient ItemDao myDao;

@Generated(hash = 708609558)
public Item(Long id, @NotNull String text, String comment, java.util.Date date,
                String status) {
        this.id = id;
        this.text = text;
        this.comment = comment;
        this.date = date;
        this.status = status;
}

@Generated(hash = 1470900980)
public Item() {
}

public Long getId() {
        return this.id;
}

public void setId(Long id) {
        this.id = id;
}

public String getText() {
        return this.text;
}

public void setText(String text) {
        this.text = text;
}

public String getComment() {
        return this.comment;
}

public void setComment(String comment) {
        this.comment = comment;
}

public java.util.Date getDate() {
        return this.date;
}

public void setDate(java.util.Date date) {
        this.date = date;
}

public String getStatus() {
        return this.status;
}

public void setStatus(String status) {
        this.status = status;
}

/**
 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
 * Entity must attached to an entity context.
 */
@Generated(hash = 128553479)
public void delete() {
        if (myDao == null) {
                throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
}

/**
 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
 * Entity must attached to an entity context.
 */
@Generated(hash = 1942392019)
public void refresh() {
        if (myDao == null) {
                throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
}

/**
 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
 * Entity must attached to an entity context.
 */
@Generated(hash = 713229351)
public void update() {
        if (myDao == null) {
                throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
}

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 881068859)
public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getItemDao() : null;
}

}

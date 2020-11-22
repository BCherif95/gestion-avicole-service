package com.gestvicole.gestionavicole.entities.parents;

import com.gestvicole.gestionavicole.entities.User;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity implements Serializable, Comparable <AbstractEntity>{

    @ManyToOne
    @JoinColumn(name = "create_by")
    private User createBy;

    @ManyToOne
    @JoinColumn(name = "update_by")
    private User updateBy;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate = new Date();

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date")
    private Date updateDate;

    @Override
    public int compareTo(AbstractEntity abstractEntity) {
        return createDate.compareTo(abstractEntity.getCreateDate());
    }
}

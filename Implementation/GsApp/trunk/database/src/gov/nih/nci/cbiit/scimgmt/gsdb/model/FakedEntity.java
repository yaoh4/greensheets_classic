package gov.nih.nci.cbiit.scimgmt.gsdb.model;

import javax.persistence.*;
import java.util.Date;

@SqlResultSetMapping(name = "WorkingTemplatesResult",
        classes = {
                @ConstructorResult(targetClass = WorkingTemplates.class,
                   columns = {@ColumnResult(name = "templateId", type = Integer.class),
                           @ColumnResult(name = "formRoleCode"),
                           @ColumnResult(name = "applTypeCode"),
                           @ColumnResult(name = "activityCode"),
                           @ColumnResult(name = "createDate", type = Date.class),
                           @ColumnResult(name = "currentFlag", type = Boolean.class)
                   })
        })
@Entity
public class FakedEntity {
    @Id int id;
}

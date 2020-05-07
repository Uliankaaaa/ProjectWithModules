package com.netcracker.ec.model.db;

import com.netcracker.ec.services.db.DbWorker;
import com.netcracker.ec.services.db.impl.NcParamsServiceImpl;
import com.netcracker.ec.services.db.impl.NcReferencesServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NcEntity {
/*    private Integer id;
    private String name;

    public NcEntity() {
    }
*/
    private final Integer id;
    @Setter()
    private String name;

    public NcEntity() {
        this.id = DbWorker.getInstance().generateId();
    }

    public String toFormattedOutput() {
        return id + " - " + name;
    }
}

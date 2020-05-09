package com.netcracker.ec.model.domain.order;

import com.netcracker.ec.model.db.NcAttribute;
import com.netcracker.ec.model.db.NcObject;
import com.netcracker.ec.model.db.NcObjectType;
import com.netcracker.ec.model.domain.enums.LifeCycleEvent;
import com.netcracker.ec.model.domain.enums.OrderAim;
import com.netcracker.ec.model.domain.enums.OrderStatus;
import com.netcracker.ec.services.OpfUtils;
import com.netcracker.ec.services.db.impl.NcObjectServiceImpl;
import com.netcracker.ec.services.lc.LifeCycleManager;
import com.netcracker.ec.services.omresolver.annotations.ObjectType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

<<<<<<< HEAD
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.netcracker.ec.common.OmConstants.ATTR_ORDER_COMPLETION_DATE;
import static com.netcracker.ec.common.OmConstants.ATTR_ORDER_STATUS;
=======
import static com.netcracker.ec.common.OmConstants.*;
import static com.netcracker.ec.services.OpfUtils.getCurrentDate;
>>>>>>> master

@Getter
@Setter
@NoArgsConstructor
@ObjectType(objectTypeId = Order.OBJECT_TYPE)
public class Order extends NcObject {
<<<<<<< HEAD
 /*   public Order(NcObjectType objectType) {
=======
    public static final int OBJECT_TYPE = 10;

    public Order(NcObjectType objectType, OrderAim orderAim) {
>>>>>>> master
        super(objectType);
        OpfUtils.generateOrderName(this);
        new NcObjectServiceImpl().insert(this);
        setCreatedWhen(getCurrentDate().toString());
        setStatus(OrderStatus.NA);
<<<<<<< HEAD
    }*/

    Set<NcAttribute> parameters;

    public Order(NcObjectType objectType) {
        super(objectType);
        this.parameters = new HashSet<>();
    }

    public Order(Integer id, String name, NcObjectType objectType) {
        super(id, name, objectType);
        this.parameters = new HashSet<>();
=======
        setOrderAim(orderAim);
>>>>>>> master
    }

    public void save() {
        LifeCycleManager.sendEvent(LifeCycleEvent.INIT, this);
        LifeCycleManager.sendEvent(LifeCycleEvent.CREATE, this);
        LifeCycleManager.sendEvent(LifeCycleEvent.START_PROCESSING, this);
        LifeCycleManager.sendEvent(LifeCycleEvent.COMPLETE, this);
    }

    public OrderStatus getStatus() {
        return OrderStatus.valueOf(getListValueId(ATTR_ORDER_STATUS));
    }

    public void setStatus(OrderStatus status) {
        setListValueId(ATTR_ORDER_STATUS, status.getLvId());
    }

    public OrderAim getOrderAim() {
        return OrderAim.valueOf(getListValueId(ATTR_ORDER_AIM));
    }

    public void setOrderAim(OrderAim orderAim) {
        setListValueId(ATTR_ORDER_AIM, orderAim.getLvId());
    }

    public String getCompletionDate() {
        return getStringValue(ATTR_ORDER_COMPLETION_DATE);
    }

    public void setCompletionDate(String completionDate) {
        setStringValue(ATTR_ORDER_COMPLETION_DATE, completionDate);
    }
}

/*
 * @date 2016年12月26日 17:51
 */
package com.icourt.orm.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author june
 */
public abstract class Junction implements Criterion{

    private List<Criterion> criterions;

    public List<Criterion> getCriterions() {
        return criterions;
    }

    public void setCriterions(List<Criterion> criterions) {
        this.criterions = criterions;
    }

    public void addCriterion(Criterion criterion) {
        if (criterions == null) {
            criterions = new ArrayList<>();
        }
        criterions.add(criterion);
    }
}

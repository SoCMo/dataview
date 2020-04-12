package com.nCov.DataView.model.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class AssessDOExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AssessDOExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andPathIdIsNull() {
            addCriterion("path_id is null");
            return (Criteria) this;
        }

        public Criteria andPathIdIsNotNull() {
            addCriterion("path_id is not null");
            return (Criteria) this;
        }

        public Criteria andPathIdEqualTo(Integer value) {
            addCriterion("path_id =", value, "pathId");
            return (Criteria) this;
        }

        public Criteria andPathIdNotEqualTo(Integer value) {
            addCriterion("path_id <>", value, "pathId");
            return (Criteria) this;
        }

        public Criteria andPathIdGreaterThan(Integer value) {
            addCriterion("path_id >", value, "pathId");
            return (Criteria) this;
        }

        public Criteria andPathIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("path_id >=", value, "pathId");
            return (Criteria) this;
        }

        public Criteria andPathIdLessThan(Integer value) {
            addCriterion("path_id <", value, "pathId");
            return (Criteria) this;
        }

        public Criteria andPathIdLessThanOrEqualTo(Integer value) {
            addCriterion("path_id <=", value, "pathId");
            return (Criteria) this;
        }

        public Criteria andPathIdIn(List<Integer> values) {
            addCriterion("path_id in", values, "pathId");
            return (Criteria) this;
        }

        public Criteria andPathIdNotIn(List<Integer> values) {
            addCriterion("path_id not in", values, "pathId");
            return (Criteria) this;
        }

        public Criteria andPathIdBetween(Integer value1, Integer value2) {
            addCriterion("path_id between", value1, value2, "pathId");
            return (Criteria) this;
        }

        public Criteria andPathIdNotBetween(Integer value1, Integer value2) {
            addCriterion("path_id not between", value1, value2, "pathId");
            return (Criteria) this;
        }

        public Criteria andAreaNameIsNull() {
            addCriterion("area_name is null");
            return (Criteria) this;
        }

        public Criteria andAreaNameIsNotNull() {
            addCriterion("area_name is not null");
            return (Criteria) this;
        }

        public Criteria andAreaNameEqualTo(String value) {
            addCriterion("area_name =", value, "areaName");
            return (Criteria) this;
        }

        public Criteria andAreaNameNotEqualTo(String value) {
            addCriterion("area_name <>", value, "areaName");
            return (Criteria) this;
        }

        public Criteria andAreaNameGreaterThan(String value) {
            addCriterion("area_name >", value, "areaName");
            return (Criteria) this;
        }

        public Criteria andAreaNameGreaterThanOrEqualTo(String value) {
            addCriterion("area_name >=", value, "areaName");
            return (Criteria) this;
        }

        public Criteria andAreaNameLessThan(String value) {
            addCriterion("area_name <", value, "areaName");
            return (Criteria) this;
        }

        public Criteria andAreaNameLessThanOrEqualTo(String value) {
            addCriterion("area_name <=", value, "areaName");
            return (Criteria) this;
        }

        public Criteria andAreaNameLike(String value) {
            addCriterion("area_name like", value, "areaName");
            return (Criteria) this;
        }

        public Criteria andAreaNameNotLike(String value) {
            addCriterion("area_name not like", value, "areaName");
            return (Criteria) this;
        }

        public Criteria andAreaNameIn(List<String> values) {
            addCriterion("area_name in", values, "areaName");
            return (Criteria) this;
        }

        public Criteria andAreaNameNotIn(List<String> values) {
            addCriterion("area_name not in", values, "areaName");
            return (Criteria) this;
        }

        public Criteria andAreaNameBetween(String value1, String value2) {
            addCriterion("area_name between", value1, value2, "areaName");
            return (Criteria) this;
        }

        public Criteria andAreaNameNotBetween(String value1, String value2) {
            addCriterion("area_name not between", value1, value2, "areaName");
            return (Criteria) this;
        }

        public Criteria andPassOrderIsNull() {
            addCriterion("pass_order is null");
            return (Criteria) this;
        }

        public Criteria andPassOrderIsNotNull() {
            addCriterion("pass_order is not null");
            return (Criteria) this;
        }

        public Criteria andPassOrderEqualTo(Integer value) {
            addCriterion("pass_order =", value, "passOrder");
            return (Criteria) this;
        }

        public Criteria andPassOrderNotEqualTo(Integer value) {
            addCriterion("pass_order <>", value, "passOrder");
            return (Criteria) this;
        }

        public Criteria andPassOrderGreaterThan(Integer value) {
            addCriterion("pass_order >", value, "passOrder");
            return (Criteria) this;
        }

        public Criteria andPassOrderGreaterThanOrEqualTo(Integer value) {
            addCriterion("pass_order >=", value, "passOrder");
            return (Criteria) this;
        }

        public Criteria andPassOrderLessThan(Integer value) {
            addCriterion("pass_order <", value, "passOrder");
            return (Criteria) this;
        }

        public Criteria andPassOrderLessThanOrEqualTo(Integer value) {
            addCriterion("pass_order <=", value, "passOrder");
            return (Criteria) this;
        }

        public Criteria andPassOrderIn(List<Integer> values) {
            addCriterion("pass_order in", values, "passOrder");
            return (Criteria) this;
        }

        public Criteria andPassOrderNotIn(List<Integer> values) {
            addCriterion("pass_order not in", values, "passOrder");
            return (Criteria) this;
        }

        public Criteria andPassOrderBetween(Integer value1, Integer value2) {
            addCriterion("pass_order between", value1, value2, "passOrder");
            return (Criteria) this;
        }

        public Criteria andPassOrderNotBetween(Integer value1, Integer value2) {
            addCriterion("pass_order not between", value1, value2, "passOrder");
            return (Criteria) this;
        }

        public Criteria andCleanlinessScoreIsNull() {
            addCriterion("cleanliness_score is null");
            return (Criteria) this;
        }

        public Criteria andCleanlinessScoreIsNotNull() {
            addCriterion("cleanliness_score is not null");
            return (Criteria) this;
        }

        public Criteria andCleanlinessScoreEqualTo(Integer value) {
            addCriterion("cleanliness_score =", value, "cleanlinessScore");
            return (Criteria) this;
        }

        public Criteria andCleanlinessScoreNotEqualTo(Integer value) {
            addCriterion("cleanliness_score <>", value, "cleanlinessScore");
            return (Criteria) this;
        }

        public Criteria andCleanlinessScoreGreaterThan(Integer value) {
            addCriterion("cleanliness_score >", value, "cleanlinessScore");
            return (Criteria) this;
        }

        public Criteria andCleanlinessScoreGreaterThanOrEqualTo(Integer value) {
            addCriterion("cleanliness_score >=", value, "cleanlinessScore");
            return (Criteria) this;
        }

        public Criteria andCleanlinessScoreLessThan(Integer value) {
            addCriterion("cleanliness_score <", value, "cleanlinessScore");
            return (Criteria) this;
        }

        public Criteria andCleanlinessScoreLessThanOrEqualTo(Integer value) {
            addCriterion("cleanliness_score <=", value, "cleanlinessScore");
            return (Criteria) this;
        }

        public Criteria andCleanlinessScoreIn(List<Integer> values) {
            addCriterion("cleanliness_score in", values, "cleanlinessScore");
            return (Criteria) this;
        }

        public Criteria andCleanlinessScoreNotIn(List<Integer> values) {
            addCriterion("cleanliness_score not in", values, "cleanlinessScore");
            return (Criteria) this;
        }

        public Criteria andCleanlinessScoreBetween(Integer value1, Integer value2) {
            addCriterion("cleanliness_score between", value1, value2, "cleanlinessScore");
            return (Criteria) this;
        }

        public Criteria andCleanlinessScoreNotBetween(Integer value1, Integer value2) {
            addCriterion("cleanliness_score not between", value1, value2, "cleanlinessScore");
            return (Criteria) this;
        }

        public Criteria andTimeIsNull() {
            addCriterion("time is null");
            return (Criteria) this;
        }

        public Criteria andTimeIsNotNull() {
            addCriterion("time is not null");
            return (Criteria) this;
        }

        public Criteria andTimeEqualTo(Double value) {
            addCriterion("time =", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotEqualTo(Double value) {
            addCriterion("time <>", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeGreaterThan(Double value) {
            addCriterion("time >", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeGreaterThanOrEqualTo(Double value) {
            addCriterion("time >=", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeLessThan(Double value) {
            addCriterion("time <", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeLessThanOrEqualTo(Double value) {
            addCriterion("time <=", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeIn(List<Double> values) {
            addCriterion("time in", values, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotIn(List<Double> values) {
            addCriterion("time not in", values, "time");
            return (Criteria) this;
        }

        public Criteria andTimeBetween(Double value1, Double value2) {
            addCriterion("time between", value1, value2, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotBetween(Double value1, Double value2) {
            addCriterion("time not between", value1, value2, "time");
            return (Criteria) this;
        }

        public Criteria andTimeScoreIsNull() {
            addCriterion("time_score is null");
            return (Criteria) this;
        }

        public Criteria andTimeScoreIsNotNull() {
            addCriterion("time_score is not null");
            return (Criteria) this;
        }

        public Criteria andTimeScoreEqualTo(Double value) {
            addCriterion("time_score =", value, "timeScore");
            return (Criteria) this;
        }

        public Criteria andTimeScoreNotEqualTo(Double value) {
            addCriterion("time_score <>", value, "timeScore");
            return (Criteria) this;
        }

        public Criteria andTimeScoreGreaterThan(Double value) {
            addCriterion("time_score >", value, "timeScore");
            return (Criteria) this;
        }

        public Criteria andTimeScoreGreaterThanOrEqualTo(Double value) {
            addCriterion("time_score >=", value, "timeScore");
            return (Criteria) this;
        }

        public Criteria andTimeScoreLessThan(Double value) {
            addCriterion("time_score <", value, "timeScore");
            return (Criteria) this;
        }

        public Criteria andTimeScoreLessThanOrEqualTo(Double value) {
            addCriterion("time_score <=", value, "timeScore");
            return (Criteria) this;
        }

        public Criteria andTimeScoreIn(List<Double> values) {
            addCriterion("time_score in", values, "timeScore");
            return (Criteria) this;
        }

        public Criteria andTimeScoreNotIn(List<Double> values) {
            addCriterion("time_score not in", values, "timeScore");
            return (Criteria) this;
        }

        public Criteria andTimeScoreBetween(Double value1, Double value2) {
            addCriterion("time_score between", value1, value2, "timeScore");
            return (Criteria) this;
        }

        public Criteria andTimeScoreNotBetween(Double value1, Double value2) {
            addCriterion("time_score not between", value1, value2, "timeScore");
            return (Criteria) this;
        }

        public Criteria andCrowdScoreIsNull() {
            addCriterion("crowd_score is null");
            return (Criteria) this;
        }

        public Criteria andCrowdScoreIsNotNull() {
            addCriterion("crowd_score is not null");
            return (Criteria) this;
        }

        public Criteria andCrowdScoreEqualTo(Integer value) {
            addCriterion("crowd_score =", value, "crowdScore");
            return (Criteria) this;
        }

        public Criteria andCrowdScoreNotEqualTo(Integer value) {
            addCriterion("crowd_score <>", value, "crowdScore");
            return (Criteria) this;
        }

        public Criteria andCrowdScoreGreaterThan(Integer value) {
            addCriterion("crowd_score >", value, "crowdScore");
            return (Criteria) this;
        }

        public Criteria andCrowdScoreGreaterThanOrEqualTo(Integer value) {
            addCriterion("crowd_score >=", value, "crowdScore");
            return (Criteria) this;
        }

        public Criteria andCrowdScoreLessThan(Integer value) {
            addCriterion("crowd_score <", value, "crowdScore");
            return (Criteria) this;
        }

        public Criteria andCrowdScoreLessThanOrEqualTo(Integer value) {
            addCriterion("crowd_score <=", value, "crowdScore");
            return (Criteria) this;
        }

        public Criteria andCrowdScoreIn(List<Integer> values) {
            addCriterion("crowd_score in", values, "crowdScore");
            return (Criteria) this;
        }

        public Criteria andCrowdScoreNotIn(List<Integer> values) {
            addCriterion("crowd_score not in", values, "crowdScore");
            return (Criteria) this;
        }

        public Criteria andCrowdScoreBetween(Integer value1, Integer value2) {
            addCriterion("crowd_score between", value1, value2, "crowdScore");
            return (Criteria) this;
        }

        public Criteria andCrowdScoreNotBetween(Integer value1, Integer value2) {
            addCriterion("crowd_score not between", value1, value2, "crowdScore");
            return (Criteria) this;
        }

        public Criteria andLocalScoreIsNull() {
            addCriterion("local_score is null");
            return (Criteria) this;
        }

        public Criteria andLocalScoreIsNotNull() {
            addCriterion("local_score is not null");
            return (Criteria) this;
        }

        public Criteria andLocalScoreEqualTo(Integer value) {
            addCriterion("local_score =", value, "localScore");
            return (Criteria) this;
        }

        public Criteria andLocalScoreNotEqualTo(Integer value) {
            addCriterion("local_score <>", value, "localScore");
            return (Criteria) this;
        }

        public Criteria andLocalScoreGreaterThan(Integer value) {
            addCriterion("local_score >", value, "localScore");
            return (Criteria) this;
        }

        public Criteria andLocalScoreGreaterThanOrEqualTo(Integer value) {
            addCriterion("local_score >=", value, "localScore");
            return (Criteria) this;
        }

        public Criteria andLocalScoreLessThan(Integer value) {
            addCriterion("local_score <", value, "localScore");
            return (Criteria) this;
        }

        public Criteria andLocalScoreLessThanOrEqualTo(Integer value) {
            addCriterion("local_score <=", value, "localScore");
            return (Criteria) this;
        }

        public Criteria andLocalScoreIn(List<Integer> values) {
            addCriterion("local_score in", values, "localScore");
            return (Criteria) this;
        }

        public Criteria andLocalScoreNotIn(List<Integer> values) {
            addCriterion("local_score not in", values, "localScore");
            return (Criteria) this;
        }

        public Criteria andLocalScoreBetween(Integer value1, Integer value2) {
            addCriterion("local_score between", value1, value2, "localScore");
            return (Criteria) this;
        }

        public Criteria andLocalScoreNotBetween(Integer value1, Integer value2) {
            addCriterion("local_score not between", value1, value2, "localScore");
            return (Criteria) this;
        }

        public Criteria andFinalScoreIsNull() {
            addCriterion("final_score is null");
            return (Criteria) this;
        }

        public Criteria andFinalScoreIsNotNull() {
            addCriterion("final_score is not null");
            return (Criteria) this;
        }

        public Criteria andFinalScoreEqualTo(Double value) {
            addCriterion("final_score =", value, "finalScore");
            return (Criteria) this;
        }

        public Criteria andFinalScoreNotEqualTo(Double value) {
            addCriterion("final_score <>", value, "finalScore");
            return (Criteria) this;
        }

        public Criteria andFinalScoreGreaterThan(Double value) {
            addCriterion("final_score >", value, "finalScore");
            return (Criteria) this;
        }

        public Criteria andFinalScoreGreaterThanOrEqualTo(Double value) {
            addCriterion("final_score >=", value, "finalScore");
            return (Criteria) this;
        }

        public Criteria andFinalScoreLessThan(Double value) {
            addCriterion("final_score <", value, "finalScore");
            return (Criteria) this;
        }

        public Criteria andFinalScoreLessThanOrEqualTo(Double value) {
            addCriterion("final_score <=", value, "finalScore");
            return (Criteria) this;
        }

        public Criteria andFinalScoreIn(List<Double> values) {
            addCriterion("final_score in", values, "finalScore");
            return (Criteria) this;
        }

        public Criteria andFinalScoreNotIn(List<Double> values) {
            addCriterion("final_score not in", values, "finalScore");
            return (Criteria) this;
        }

        public Criteria andFinalScoreBetween(Double value1, Double value2) {
            addCriterion("final_score between", value1, value2, "finalScore");
            return (Criteria) this;
        }

        public Criteria andFinalScoreNotBetween(Double value1, Double value2) {
            addCriterion("final_score not between", value1, value2, "finalScore");
            return (Criteria) this;
        }

        public Criteria andSumTimeIsNull() {
            addCriterion("sum_time is null");
            return (Criteria) this;
        }

        public Criteria andSumTimeIsNotNull() {
            addCriterion("sum_time is not null");
            return (Criteria) this;
        }

        public Criteria andSumTimeEqualTo(String value) {
            addCriterion("sum_time =", value, "sumTime");
            return (Criteria) this;
        }

        public Criteria andSumTimeNotEqualTo(String value) {
            addCriterion("sum_time <>", value, "sumTime");
            return (Criteria) this;
        }

        public Criteria andSumTimeGreaterThan(String value) {
            addCriterion("sum_time >", value, "sumTime");
            return (Criteria) this;
        }

        public Criteria andSumTimeGreaterThanOrEqualTo(String value) {
            addCriterion("sum_time >=", value, "sumTime");
            return (Criteria) this;
        }

        public Criteria andSumTimeLessThan(String value) {
            addCriterion("sum_time <", value, "sumTime");
            return (Criteria) this;
        }

        public Criteria andSumTimeLessThanOrEqualTo(String value) {
            addCriterion("sum_time <=", value, "sumTime");
            return (Criteria) this;
        }

        public Criteria andSumTimeLike(String value) {
            addCriterion("sum_time like", value, "sumTime");
            return (Criteria) this;
        }

        public Criteria andSumTimeNotLike(String value) {
            addCriterion("sum_time not like", value, "sumTime");
            return (Criteria) this;
        }

        public Criteria andSumTimeIn(List<String> values) {
            addCriterion("sum_time in", values, "sumTime");
            return (Criteria) this;
        }

        public Criteria andSumTimeNotIn(List<String> values) {
            addCriterion("sum_time not in", values, "sumTime");
            return (Criteria) this;
        }

        public Criteria andSumTimeBetween(String value1, String value2) {
            addCriterion("sum_time between", value1, value2, "sumTime");
            return (Criteria) this;
        }

        public Criteria andSumTimeNotBetween(String value1, String value2) {
            addCriterion("sum_time not between", value1, value2, "sumTime");
            return (Criteria) this;
        }

        public Criteria andSumPriceIsNull() {
            addCriterion("sum_price is null");
            return (Criteria) this;
        }

        public Criteria andSumPriceIsNotNull() {
            addCriterion("sum_price is not null");
            return (Criteria) this;
        }

        public Criteria andSumPriceEqualTo(Double value) {
            addCriterion("sum_price =", value, "sumPrice");
            return (Criteria) this;
        }

        public Criteria andSumPriceNotEqualTo(Double value) {
            addCriterion("sum_price <>", value, "sumPrice");
            return (Criteria) this;
        }

        public Criteria andSumPriceGreaterThan(Double value) {
            addCriterion("sum_price >", value, "sumPrice");
            return (Criteria) this;
        }

        public Criteria andSumPriceGreaterThanOrEqualTo(Double value) {
            addCriterion("sum_price >=", value, "sumPrice");
            return (Criteria) this;
        }

        public Criteria andSumPriceLessThan(Double value) {
            addCriterion("sum_price <", value, "sumPrice");
            return (Criteria) this;
        }

        public Criteria andSumPriceLessThanOrEqualTo(Double value) {
            addCriterion("sum_price <=", value, "sumPrice");
            return (Criteria) this;
        }

        public Criteria andSumPriceIn(List<Double> values) {
            addCriterion("sum_price in", values, "sumPrice");
            return (Criteria) this;
        }

        public Criteria andSumPriceNotIn(List<Double> values) {
            addCriterion("sum_price not in", values, "sumPrice");
            return (Criteria) this;
        }

        public Criteria andSumPriceBetween(Double value1, Double value2) {
            addCriterion("sum_price between", value1, value2, "sumPrice");
            return (Criteria) this;
        }

        public Criteria andSumPriceNotBetween(Double value1, Double value2) {
            addCriterion("sum_price not between", value1, value2, "sumPrice");
            return (Criteria) this;
        }

        public Criteria andSumScoreIsNull() {
            addCriterion("sum_score is null");
            return (Criteria) this;
        }

        public Criteria andSumScoreIsNotNull() {
            addCriterion("sum_score is not null");
            return (Criteria) this;
        }

        public Criteria andSumScoreEqualTo(Double value) {
            addCriterion("sum_score =", value, "sumScore");
            return (Criteria) this;
        }

        public Criteria andSumScoreNotEqualTo(Double value) {
            addCriterion("sum_score <>", value, "sumScore");
            return (Criteria) this;
        }

        public Criteria andSumScoreGreaterThan(Double value) {
            addCriterion("sum_score >", value, "sumScore");
            return (Criteria) this;
        }

        public Criteria andSumScoreGreaterThanOrEqualTo(Double value) {
            addCriterion("sum_score >=", value, "sumScore");
            return (Criteria) this;
        }

        public Criteria andSumScoreLessThan(Double value) {
            addCriterion("sum_score <", value, "sumScore");
            return (Criteria) this;
        }

        public Criteria andSumScoreLessThanOrEqualTo(Double value) {
            addCriterion("sum_score <=", value, "sumScore");
            return (Criteria) this;
        }

        public Criteria andSumScoreIn(List<Double> values) {
            addCriterion("sum_score in", values, "sumScore");
            return (Criteria) this;
        }

        public Criteria andSumScoreNotIn(List<Double> values) {
            addCriterion("sum_score not in", values, "sumScore");
            return (Criteria) this;
        }

        public Criteria andSumScoreBetween(Double value1, Double value2) {
            addCriterion("sum_score between", value1, value2, "sumScore");
            return (Criteria) this;
        }

        public Criteria andSumScoreNotBetween(Double value1, Double value2) {
            addCriterion("sum_score not between", value1, value2, "sumScore");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterionForJDBCDate("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterionForJDBCDate("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterionForJDBCDate("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterionForJDBCDate("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterionForJDBCDate("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterionForJDBCDate("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}
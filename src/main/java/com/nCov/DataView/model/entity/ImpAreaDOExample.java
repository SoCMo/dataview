package com.nCov.DataView.model.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ImpAreaDOExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ImpAreaDOExample() {
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

        public Criteria andProvinceNameIsNull() {
            addCriterion("province_name is null");
            return (Criteria) this;
        }

        public Criteria andProvinceNameIsNotNull() {
            addCriterion("province_name is not null");
            return (Criteria) this;
        }

        public Criteria andProvinceNameEqualTo(String value) {
            addCriterion("province_name =", value, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameNotEqualTo(String value) {
            addCriterion("province_name <>", value, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameGreaterThan(String value) {
            addCriterion("province_name >", value, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameGreaterThanOrEqualTo(String value) {
            addCriterion("province_name >=", value, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameLessThan(String value) {
            addCriterion("province_name <", value, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameLessThanOrEqualTo(String value) {
            addCriterion("province_name <=", value, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameLike(String value) {
            addCriterion("province_name like", value, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameNotLike(String value) {
            addCriterion("province_name not like", value, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameIn(List<String> values) {
            addCriterion("province_name in", values, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameNotIn(List<String> values) {
            addCriterion("province_name not in", values, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameBetween(String value1, String value2) {
            addCriterion("province_name between", value1, value2, "provinceName");
            return (Criteria) this;
        }

        public Criteria andProvinceNameNotBetween(String value1, String value2) {
            addCriterion("province_name not between", value1, value2, "provinceName");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andRemainConfirmRankIsNull() {
            addCriterion("remain_confirm_rank is null");
            return (Criteria) this;
        }

        public Criteria andRemainConfirmRankIsNotNull() {
            addCriterion("remain_confirm_rank is not null");
            return (Criteria) this;
        }

        public Criteria andRemainConfirmRankEqualTo(Integer value) {
            addCriterion("remain_confirm_rank =", value, "remainConfirmRank");
            return (Criteria) this;
        }

        public Criteria andRemainConfirmRankNotEqualTo(Integer value) {
            addCriterion("remain_confirm_rank <>", value, "remainConfirmRank");
            return (Criteria) this;
        }

        public Criteria andRemainConfirmRankGreaterThan(Integer value) {
            addCriterion("remain_confirm_rank >", value, "remainConfirmRank");
            return (Criteria) this;
        }

        public Criteria andRemainConfirmRankGreaterThanOrEqualTo(Integer value) {
            addCriterion("remain_confirm_rank >=", value, "remainConfirmRank");
            return (Criteria) this;
        }

        public Criteria andRemainConfirmRankLessThan(Integer value) {
            addCriterion("remain_confirm_rank <", value, "remainConfirmRank");
            return (Criteria) this;
        }

        public Criteria andRemainConfirmRankLessThanOrEqualTo(Integer value) {
            addCriterion("remain_confirm_rank <=", value, "remainConfirmRank");
            return (Criteria) this;
        }

        public Criteria andRemainConfirmRankIn(List<Integer> values) {
            addCriterion("remain_confirm_rank in", values, "remainConfirmRank");
            return (Criteria) this;
        }

        public Criteria andRemainConfirmRankNotIn(List<Integer> values) {
            addCriterion("remain_confirm_rank not in", values, "remainConfirmRank");
            return (Criteria) this;
        }

        public Criteria andRemainConfirmRankBetween(Integer value1, Integer value2) {
            addCriterion("remain_confirm_rank between", value1, value2, "remainConfirmRank");
            return (Criteria) this;
        }

        public Criteria andRemainConfirmRankNotBetween(Integer value1, Integer value2) {
            addCriterion("remain_confirm_rank not between", value1, value2, "remainConfirmRank");
            return (Criteria) this;
        }

        public Criteria andRemainCountRankIsNull() {
            addCriterion("remain_count_rank is null");
            return (Criteria) this;
        }

        public Criteria andRemainCountRankIsNotNull() {
            addCriterion("remain_count_rank is not null");
            return (Criteria) this;
        }

        public Criteria andRemainCountRankEqualTo(Integer value) {
            addCriterion("remain_count_rank =", value, "remainCountRank");
            return (Criteria) this;
        }

        public Criteria andRemainCountRankNotEqualTo(Integer value) {
            addCriterion("remain_count_rank <>", value, "remainCountRank");
            return (Criteria) this;
        }

        public Criteria andRemainCountRankGreaterThan(Integer value) {
            addCriterion("remain_count_rank >", value, "remainCountRank");
            return (Criteria) this;
        }

        public Criteria andRemainCountRankGreaterThanOrEqualTo(Integer value) {
            addCriterion("remain_count_rank >=", value, "remainCountRank");
            return (Criteria) this;
        }

        public Criteria andRemainCountRankLessThan(Integer value) {
            addCriterion("remain_count_rank <", value, "remainCountRank");
            return (Criteria) this;
        }

        public Criteria andRemainCountRankLessThanOrEqualTo(Integer value) {
            addCriterion("remain_count_rank <=", value, "remainCountRank");
            return (Criteria) this;
        }

        public Criteria andRemainCountRankIn(List<Integer> values) {
            addCriterion("remain_count_rank in", values, "remainCountRank");
            return (Criteria) this;
        }

        public Criteria andRemainCountRankNotIn(List<Integer> values) {
            addCriterion("remain_count_rank not in", values, "remainCountRank");
            return (Criteria) this;
        }

        public Criteria andRemainCountRankBetween(Integer value1, Integer value2) {
            addCriterion("remain_count_rank between", value1, value2, "remainCountRank");
            return (Criteria) this;
        }

        public Criteria andRemainCountRankNotBetween(Integer value1, Integer value2) {
            addCriterion("remain_count_rank not between", value1, value2, "remainCountRank");
            return (Criteria) this;
        }

        public Criteria andGrowthRankIsNull() {
            addCriterion("growth_rank is null");
            return (Criteria) this;
        }

        public Criteria andGrowthRankIsNotNull() {
            addCriterion("growth_rank is not null");
            return (Criteria) this;
        }

        public Criteria andGrowthRankEqualTo(Integer value) {
            addCriterion("growth_rank =", value, "growthRank");
            return (Criteria) this;
        }

        public Criteria andGrowthRankNotEqualTo(Integer value) {
            addCriterion("growth_rank <>", value, "growthRank");
            return (Criteria) this;
        }

        public Criteria andGrowthRankGreaterThan(Integer value) {
            addCriterion("growth_rank >", value, "growthRank");
            return (Criteria) this;
        }

        public Criteria andGrowthRankGreaterThanOrEqualTo(Integer value) {
            addCriterion("growth_rank >=", value, "growthRank");
            return (Criteria) this;
        }

        public Criteria andGrowthRankLessThan(Integer value) {
            addCriterion("growth_rank <", value, "growthRank");
            return (Criteria) this;
        }

        public Criteria andGrowthRankLessThanOrEqualTo(Integer value) {
            addCriterion("growth_rank <=", value, "growthRank");
            return (Criteria) this;
        }

        public Criteria andGrowthRankIn(List<Integer> values) {
            addCriterion("growth_rank in", values, "growthRank");
            return (Criteria) this;
        }

        public Criteria andGrowthRankNotIn(List<Integer> values) {
            addCriterion("growth_rank not in", values, "growthRank");
            return (Criteria) this;
        }

        public Criteria andGrowthRankBetween(Integer value1, Integer value2) {
            addCriterion("growth_rank between", value1, value2, "growthRank");
            return (Criteria) this;
        }

        public Criteria andGrowthRankNotBetween(Integer value1, Integer value2) {
            addCriterion("growth_rank not between", value1, value2, "growthRank");
            return (Criteria) this;
        }

        public Criteria andRemainConfirmIsNull() {
            addCriterion("remain_confirm is null");
            return (Criteria) this;
        }

        public Criteria andRemainConfirmIsNotNull() {
            addCriterion("remain_confirm is not null");
            return (Criteria) this;
        }

        public Criteria andRemainConfirmEqualTo(Double value) {
            addCriterion("remain_confirm =", value, "remainConfirm");
            return (Criteria) this;
        }

        public Criteria andRemainConfirmNotEqualTo(Double value) {
            addCriterion("remain_confirm <>", value, "remainConfirm");
            return (Criteria) this;
        }

        public Criteria andRemainConfirmGreaterThan(Double value) {
            addCriterion("remain_confirm >", value, "remainConfirm");
            return (Criteria) this;
        }

        public Criteria andRemainConfirmGreaterThanOrEqualTo(Double value) {
            addCriterion("remain_confirm >=", value, "remainConfirm");
            return (Criteria) this;
        }

        public Criteria andRemainConfirmLessThan(Double value) {
            addCriterion("remain_confirm <", value, "remainConfirm");
            return (Criteria) this;
        }

        public Criteria andRemainConfirmLessThanOrEqualTo(Double value) {
            addCriterion("remain_confirm <=", value, "remainConfirm");
            return (Criteria) this;
        }

        public Criteria andRemainConfirmIn(List<Double> values) {
            addCriterion("remain_confirm in", values, "remainConfirm");
            return (Criteria) this;
        }

        public Criteria andRemainConfirmNotIn(List<Double> values) {
            addCriterion("remain_confirm not in", values, "remainConfirm");
            return (Criteria) this;
        }

        public Criteria andRemainConfirmBetween(Double value1, Double value2) {
            addCriterion("remain_confirm between", value1, value2, "remainConfirm");
            return (Criteria) this;
        }

        public Criteria andRemainConfirmNotBetween(Double value1, Double value2) {
            addCriterion("remain_confirm not between", value1, value2, "remainConfirm");
            return (Criteria) this;
        }

        public Criteria andRemainCountIsNull() {
            addCriterion("remain_count is null");
            return (Criteria) this;
        }

        public Criteria andRemainCountIsNotNull() {
            addCriterion("remain_count is not null");
            return (Criteria) this;
        }

        public Criteria andRemainCountEqualTo(Integer value) {
            addCriterion("remain_count =", value, "remainCount");
            return (Criteria) this;
        }

        public Criteria andRemainCountNotEqualTo(Integer value) {
            addCriterion("remain_count <>", value, "remainCount");
            return (Criteria) this;
        }

        public Criteria andRemainCountGreaterThan(Integer value) {
            addCriterion("remain_count >", value, "remainCount");
            return (Criteria) this;
        }

        public Criteria andRemainCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("remain_count >=", value, "remainCount");
            return (Criteria) this;
        }

        public Criteria andRemainCountLessThan(Integer value) {
            addCriterion("remain_count <", value, "remainCount");
            return (Criteria) this;
        }

        public Criteria andRemainCountLessThanOrEqualTo(Integer value) {
            addCriterion("remain_count <=", value, "remainCount");
            return (Criteria) this;
        }

        public Criteria andRemainCountIn(List<Integer> values) {
            addCriterion("remain_count in", values, "remainCount");
            return (Criteria) this;
        }

        public Criteria andRemainCountNotIn(List<Integer> values) {
            addCriterion("remain_count not in", values, "remainCount");
            return (Criteria) this;
        }

        public Criteria andRemainCountBetween(Integer value1, Integer value2) {
            addCriterion("remain_count between", value1, value2, "remainCount");
            return (Criteria) this;
        }

        public Criteria andRemainCountNotBetween(Integer value1, Integer value2) {
            addCriterion("remain_count not between", value1, value2, "remainCount");
            return (Criteria) this;
        }

        public Criteria andGrowthIsNull() {
            addCriterion("growth is null");
            return (Criteria) this;
        }

        public Criteria andGrowthIsNotNull() {
            addCriterion("growth is not null");
            return (Criteria) this;
        }

        public Criteria andGrowthEqualTo(Integer value) {
            addCriterion("growth =", value, "growth");
            return (Criteria) this;
        }

        public Criteria andGrowthNotEqualTo(Integer value) {
            addCriterion("growth <>", value, "growth");
            return (Criteria) this;
        }

        public Criteria andGrowthGreaterThan(Integer value) {
            addCriterion("growth >", value, "growth");
            return (Criteria) this;
        }

        public Criteria andGrowthGreaterThanOrEqualTo(Integer value) {
            addCriterion("growth >=", value, "growth");
            return (Criteria) this;
        }

        public Criteria andGrowthLessThan(Integer value) {
            addCriterion("growth <", value, "growth");
            return (Criteria) this;
        }

        public Criteria andGrowthLessThanOrEqualTo(Integer value) {
            addCriterion("growth <=", value, "growth");
            return (Criteria) this;
        }

        public Criteria andGrowthIn(List<Integer> values) {
            addCriterion("growth in", values, "growth");
            return (Criteria) this;
        }

        public Criteria andGrowthNotIn(List<Integer> values) {
            addCriterion("growth not in", values, "growth");
            return (Criteria) this;
        }

        public Criteria andGrowthBetween(Integer value1, Integer value2) {
            addCriterion("growth between", value1, value2, "growth");
            return (Criteria) this;
        }

        public Criteria andGrowthNotBetween(Integer value1, Integer value2) {
            addCriterion("growth not between", value1, value2, "growth");
            return (Criteria) this;
        }

        public Criteria andRemainScoreIsNull() {
            addCriterion("remain_score is null");
            return (Criteria) this;
        }

        public Criteria andRemainScoreIsNotNull() {
            addCriterion("remain_score is not null");
            return (Criteria) this;
        }

        public Criteria andRemainScoreEqualTo(Integer value) {
            addCriterion("remain_score =", value, "remainScore");
            return (Criteria) this;
        }

        public Criteria andRemainScoreNotEqualTo(Integer value) {
            addCriterion("remain_score <>", value, "remainScore");
            return (Criteria) this;
        }

        public Criteria andRemainScoreGreaterThan(Integer value) {
            addCriterion("remain_score >", value, "remainScore");
            return (Criteria) this;
        }

        public Criteria andRemainScoreGreaterThanOrEqualTo(Integer value) {
            addCriterion("remain_score >=", value, "remainScore");
            return (Criteria) this;
        }

        public Criteria andRemainScoreLessThan(Integer value) {
            addCriterion("remain_score <", value, "remainScore");
            return (Criteria) this;
        }

        public Criteria andRemainScoreLessThanOrEqualTo(Integer value) {
            addCriterion("remain_score <=", value, "remainScore");
            return (Criteria) this;
        }

        public Criteria andRemainScoreIn(List<Integer> values) {
            addCriterion("remain_score in", values, "remainScore");
            return (Criteria) this;
        }

        public Criteria andRemainScoreNotIn(List<Integer> values) {
            addCriterion("remain_score not in", values, "remainScore");
            return (Criteria) this;
        }

        public Criteria andRemainScoreBetween(Integer value1, Integer value2) {
            addCriterion("remain_score between", value1, value2, "remainScore");
            return (Criteria) this;
        }

        public Criteria andRemainScoreNotBetween(Integer value1, Integer value2) {
            addCriterion("remain_score not between", value1, value2, "remainScore");
            return (Criteria) this;
        }

        public Criteria andRemainCountScoreIsNull() {
            addCriterion("remain_count_score is null");
            return (Criteria) this;
        }

        public Criteria andRemainCountScoreIsNotNull() {
            addCriterion("remain_count_score is not null");
            return (Criteria) this;
        }

        public Criteria andRemainCountScoreEqualTo(Integer value) {
            addCriterion("remain_count_score =", value, "remainCountScore");
            return (Criteria) this;
        }

        public Criteria andRemainCountScoreNotEqualTo(Integer value) {
            addCriterion("remain_count_score <>", value, "remainCountScore");
            return (Criteria) this;
        }

        public Criteria andRemainCountScoreGreaterThan(Integer value) {
            addCriterion("remain_count_score >", value, "remainCountScore");
            return (Criteria) this;
        }

        public Criteria andRemainCountScoreGreaterThanOrEqualTo(Integer value) {
            addCriterion("remain_count_score >=", value, "remainCountScore");
            return (Criteria) this;
        }

        public Criteria andRemainCountScoreLessThan(Integer value) {
            addCriterion("remain_count_score <", value, "remainCountScore");
            return (Criteria) this;
        }

        public Criteria andRemainCountScoreLessThanOrEqualTo(Integer value) {
            addCriterion("remain_count_score <=", value, "remainCountScore");
            return (Criteria) this;
        }

        public Criteria andRemainCountScoreIn(List<Integer> values) {
            addCriterion("remain_count_score in", values, "remainCountScore");
            return (Criteria) this;
        }

        public Criteria andRemainCountScoreNotIn(List<Integer> values) {
            addCriterion("remain_count_score not in", values, "remainCountScore");
            return (Criteria) this;
        }

        public Criteria andRemainCountScoreBetween(Integer value1, Integer value2) {
            addCriterion("remain_count_score between", value1, value2, "remainCountScore");
            return (Criteria) this;
        }

        public Criteria andRemainCountScoreNotBetween(Integer value1, Integer value2) {
            addCriterion("remain_count_score not between", value1, value2, "remainCountScore");
            return (Criteria) this;
        }

        public Criteria andGrowthScoreIsNull() {
            addCriterion("growth_score is null");
            return (Criteria) this;
        }

        public Criteria andGrowthScoreIsNotNull() {
            addCriterion("growth_score is not null");
            return (Criteria) this;
        }

        public Criteria andGrowthScoreEqualTo(Integer value) {
            addCriterion("growth_score =", value, "growthScore");
            return (Criteria) this;
        }

        public Criteria andGrowthScoreNotEqualTo(Integer value) {
            addCriterion("growth_score <>", value, "growthScore");
            return (Criteria) this;
        }

        public Criteria andGrowthScoreGreaterThan(Integer value) {
            addCriterion("growth_score >", value, "growthScore");
            return (Criteria) this;
        }

        public Criteria andGrowthScoreGreaterThanOrEqualTo(Integer value) {
            addCriterion("growth_score >=", value, "growthScore");
            return (Criteria) this;
        }

        public Criteria andGrowthScoreLessThan(Integer value) {
            addCriterion("growth_score <", value, "growthScore");
            return (Criteria) this;
        }

        public Criteria andGrowthScoreLessThanOrEqualTo(Integer value) {
            addCriterion("growth_score <=", value, "growthScore");
            return (Criteria) this;
        }

        public Criteria andGrowthScoreIn(List<Integer> values) {
            addCriterion("growth_score in", values, "growthScore");
            return (Criteria) this;
        }

        public Criteria andGrowthScoreNotIn(List<Integer> values) {
            addCriterion("growth_score not in", values, "growthScore");
            return (Criteria) this;
        }

        public Criteria andGrowthScoreBetween(Integer value1, Integer value2) {
            addCriterion("growth_score between", value1, value2, "growthScore");
            return (Criteria) this;
        }

        public Criteria andGrowthScoreNotBetween(Integer value1, Integer value2) {
            addCriterion("growth_score not between", value1, value2, "growthScore");
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

        public Criteria andAllrankIsNull() {
            addCriterion("allRank is null");
            return (Criteria) this;
        }

        public Criteria andAllrankIsNotNull() {
            addCriterion("allRank is not null");
            return (Criteria) this;
        }

        public Criteria andAllrankEqualTo(Integer value) {
            addCriterion("allRank =", value, "allrank");
            return (Criteria) this;
        }

        public Criteria andAllrankNotEqualTo(Integer value) {
            addCriterion("allRank <>", value, "allrank");
            return (Criteria) this;
        }

        public Criteria andAllrankGreaterThan(Integer value) {
            addCriterion("allRank >", value, "allrank");
            return (Criteria) this;
        }

        public Criteria andAllrankGreaterThanOrEqualTo(Integer value) {
            addCriterion("allRank >=", value, "allrank");
            return (Criteria) this;
        }

        public Criteria andAllrankLessThan(Integer value) {
            addCriterion("allRank <", value, "allrank");
            return (Criteria) this;
        }

        public Criteria andAllrankLessThanOrEqualTo(Integer value) {
            addCriterion("allRank <=", value, "allrank");
            return (Criteria) this;
        }

        public Criteria andAllrankIn(List<Integer> values) {
            addCriterion("allRank in", values, "allrank");
            return (Criteria) this;
        }

        public Criteria andAllrankNotIn(List<Integer> values) {
            addCriterion("allRank not in", values, "allrank");
            return (Criteria) this;
        }

        public Criteria andAllrankBetween(Integer value1, Integer value2) {
            addCriterion("allRank between", value1, value2, "allrank");
            return (Criteria) this;
        }

        public Criteria andAllrankNotBetween(Integer value1, Integer value2) {
            addCriterion("allRank not between", value1, value2, "allrank");
            return (Criteria) this;
        }

        public Criteria andDateIsNull() {
            addCriterion("date is null");
            return (Criteria) this;
        }

        public Criteria andDateIsNotNull() {
            addCriterion("date is not null");
            return (Criteria) this;
        }

        public Criteria andDateEqualTo(Date value) {
            addCriterionForJDBCDate("date =", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotEqualTo(Date value) {
            addCriterionForJDBCDate("date <>", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateGreaterThan(Date value) {
            addCriterionForJDBCDate("date >", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("date >=", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateLessThan(Date value) {
            addCriterionForJDBCDate("date <", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("date <=", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateIn(List<Date> values) {
            addCriterionForJDBCDate("date in", values, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotIn(List<Date> values) {
            addCriterionForJDBCDate("date not in", values, "date");
            return (Criteria) this;
        }

        public Criteria andDateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("date between", value1, value2, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("date not between", value1, value2, "date");
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
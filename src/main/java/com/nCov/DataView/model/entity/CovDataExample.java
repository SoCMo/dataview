package com.nCov.DataView.model.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class CovDataExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CovDataExample() {
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
            addCriterion("Id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("Id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("Id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("Id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("Id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("Id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("Id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("Id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("Id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("Id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("Id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("Id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andProvincenameIsNull() {
            addCriterion("provinceName is null");
            return (Criteria) this;
        }

        public Criteria andProvincenameIsNotNull() {
            addCriterion("provinceName is not null");
            return (Criteria) this;
        }

        public Criteria andProvincenameEqualTo(String value) {
            addCriterion("provinceName =", value, "provincename");
            return (Criteria) this;
        }

        public Criteria andProvincenameNotEqualTo(String value) {
            addCriterion("provinceName <>", value, "provincename");
            return (Criteria) this;
        }

        public Criteria andProvincenameGreaterThan(String value) {
            addCriterion("provinceName >", value, "provincename");
            return (Criteria) this;
        }

        public Criteria andProvincenameGreaterThanOrEqualTo(String value) {
            addCriterion("provinceName >=", value, "provincename");
            return (Criteria) this;
        }

        public Criteria andProvincenameLessThan(String value) {
            addCriterion("provinceName <", value, "provincename");
            return (Criteria) this;
        }

        public Criteria andProvincenameLessThanOrEqualTo(String value) {
            addCriterion("provinceName <=", value, "provincename");
            return (Criteria) this;
        }

        public Criteria andProvincenameLike(String value) {
            addCriterion("provinceName like", value, "provincename");
            return (Criteria) this;
        }

        public Criteria andProvincenameNotLike(String value) {
            addCriterion("provinceName not like", value, "provincename");
            return (Criteria) this;
        }

        public Criteria andProvincenameIn(List<String> values) {
            addCriterion("provinceName in", values, "provincename");
            return (Criteria) this;
        }

        public Criteria andProvincenameNotIn(List<String> values) {
            addCriterion("provinceName not in", values, "provincename");
            return (Criteria) this;
        }

        public Criteria andProvincenameBetween(String value1, String value2) {
            addCriterion("provinceName between", value1, value2, "provincename");
            return (Criteria) this;
        }

        public Criteria andProvincenameNotBetween(String value1, String value2) {
            addCriterion("provinceName not between", value1, value2, "provincename");
            return (Criteria) this;
        }

        public Criteria andAreanameIsNull() {
            addCriterion("areaName is null");
            return (Criteria) this;
        }

        public Criteria andAreanameIsNotNull() {
            addCriterion("areaName is not null");
            return (Criteria) this;
        }

        public Criteria andAreanameEqualTo(String value) {
            addCriterion("areaName =", value, "areaname");
            return (Criteria) this;
        }

        public Criteria andAreanameNotEqualTo(String value) {
            addCriterion("areaName <>", value, "areaname");
            return (Criteria) this;
        }

        public Criteria andAreanameGreaterThan(String value) {
            addCriterion("areaName >", value, "areaname");
            return (Criteria) this;
        }

        public Criteria andAreanameGreaterThanOrEqualTo(String value) {
            addCriterion("areaName >=", value, "areaname");
            return (Criteria) this;
        }

        public Criteria andAreanameLessThan(String value) {
            addCriterion("areaName <", value, "areaname");
            return (Criteria) this;
        }

        public Criteria andAreanameLessThanOrEqualTo(String value) {
            addCriterion("areaName <=", value, "areaname");
            return (Criteria) this;
        }

        public Criteria andAreanameLike(String value) {
            addCriterion("areaName like", value, "areaname");
            return (Criteria) this;
        }

        public Criteria andAreanameNotLike(String value) {
            addCriterion("areaName not like", value, "areaname");
            return (Criteria) this;
        }

        public Criteria andAreanameIn(List<String> values) {
            addCriterion("areaName in", values, "areaname");
            return (Criteria) this;
        }

        public Criteria andAreanameNotIn(List<String> values) {
            addCriterion("areaName not in", values, "areaname");
            return (Criteria) this;
        }

        public Criteria andAreanameBetween(String value1, String value2) {
            addCriterion("areaName between", value1, value2, "areaname");
            return (Criteria) this;
        }

        public Criteria andAreanameNotBetween(String value1, String value2) {
            addCriterion("areaName not between", value1, value2, "areaname");
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

        public Criteria andIsprovinceIsNull() {
            addCriterion("isProvince is null");
            return (Criteria) this;
        }

        public Criteria andIsprovinceIsNotNull() {
            addCriterion("isProvince is not null");
            return (Criteria) this;
        }

        public Criteria andIsprovinceEqualTo(Integer value) {
            addCriterion("isProvince =", value, "isprovince");
            return (Criteria) this;
        }

        public Criteria andIsprovinceNotEqualTo(Integer value) {
            addCriterion("isProvince <>", value, "isprovince");
            return (Criteria) this;
        }

        public Criteria andIsprovinceGreaterThan(Integer value) {
            addCriterion("isProvince >", value, "isprovince");
            return (Criteria) this;
        }

        public Criteria andIsprovinceGreaterThanOrEqualTo(Integer value) {
            addCriterion("isProvince >=", value, "isprovince");
            return (Criteria) this;
        }

        public Criteria andIsprovinceLessThan(Integer value) {
            addCriterion("isProvince <", value, "isprovince");
            return (Criteria) this;
        }

        public Criteria andIsprovinceLessThanOrEqualTo(Integer value) {
            addCriterion("isProvince <=", value, "isprovince");
            return (Criteria) this;
        }

        public Criteria andIsprovinceIn(List<Integer> values) {
            addCriterion("isProvince in", values, "isprovince");
            return (Criteria) this;
        }

        public Criteria andIsprovinceNotIn(List<Integer> values) {
            addCriterion("isProvince not in", values, "isprovince");
            return (Criteria) this;
        }

        public Criteria andIsprovinceBetween(Integer value1, Integer value2) {
            addCriterion("isProvince between", value1, value2, "isprovince");
            return (Criteria) this;
        }

        public Criteria andIsprovinceNotBetween(Integer value1, Integer value2) {
            addCriterion("isProvince not between", value1, value2, "isprovince");
            return (Criteria) this;
        }

        public Criteria andTotalconfirmIsNull() {
            addCriterion("totalConfirm is null");
            return (Criteria) this;
        }

        public Criteria andTotalconfirmIsNotNull() {
            addCriterion("totalConfirm is not null");
            return (Criteria) this;
        }

        public Criteria andTotalconfirmEqualTo(Integer value) {
            addCriterion("totalConfirm =", value, "totalconfirm");
            return (Criteria) this;
        }

        public Criteria andTotalconfirmNotEqualTo(Integer value) {
            addCriterion("totalConfirm <>", value, "totalconfirm");
            return (Criteria) this;
        }

        public Criteria andTotalconfirmGreaterThan(Integer value) {
            addCriterion("totalConfirm >", value, "totalconfirm");
            return (Criteria) this;
        }

        public Criteria andTotalconfirmGreaterThanOrEqualTo(Integer value) {
            addCriterion("totalConfirm >=", value, "totalconfirm");
            return (Criteria) this;
        }

        public Criteria andTotalconfirmLessThan(Integer value) {
            addCriterion("totalConfirm <", value, "totalconfirm");
            return (Criteria) this;
        }

        public Criteria andTotalconfirmLessThanOrEqualTo(Integer value) {
            addCriterion("totalConfirm <=", value, "totalconfirm");
            return (Criteria) this;
        }

        public Criteria andTotalconfirmIn(List<Integer> values) {
            addCriterion("totalConfirm in", values, "totalconfirm");
            return (Criteria) this;
        }

        public Criteria andTotalconfirmNotIn(List<Integer> values) {
            addCriterion("totalConfirm not in", values, "totalconfirm");
            return (Criteria) this;
        }

        public Criteria andTotalconfirmBetween(Integer value1, Integer value2) {
            addCriterion("totalConfirm between", value1, value2, "totalconfirm");
            return (Criteria) this;
        }

        public Criteria andTotalconfirmNotBetween(Integer value1, Integer value2) {
            addCriterion("totalConfirm not between", value1, value2, "totalconfirm");
            return (Criteria) this;
        }

        public Criteria andTotalsuspectIsNull() {
            addCriterion("totalSuspect is null");
            return (Criteria) this;
        }

        public Criteria andTotalsuspectIsNotNull() {
            addCriterion("totalSuspect is not null");
            return (Criteria) this;
        }

        public Criteria andTotalsuspectEqualTo(Integer value) {
            addCriterion("totalSuspect =", value, "totalsuspect");
            return (Criteria) this;
        }

        public Criteria andTotalsuspectNotEqualTo(Integer value) {
            addCriterion("totalSuspect <>", value, "totalsuspect");
            return (Criteria) this;
        }

        public Criteria andTotalsuspectGreaterThan(Integer value) {
            addCriterion("totalSuspect >", value, "totalsuspect");
            return (Criteria) this;
        }

        public Criteria andTotalsuspectGreaterThanOrEqualTo(Integer value) {
            addCriterion("totalSuspect >=", value, "totalsuspect");
            return (Criteria) this;
        }

        public Criteria andTotalsuspectLessThan(Integer value) {
            addCriterion("totalSuspect <", value, "totalsuspect");
            return (Criteria) this;
        }

        public Criteria andTotalsuspectLessThanOrEqualTo(Integer value) {
            addCriterion("totalSuspect <=", value, "totalsuspect");
            return (Criteria) this;
        }

        public Criteria andTotalsuspectIn(List<Integer> values) {
            addCriterion("totalSuspect in", values, "totalsuspect");
            return (Criteria) this;
        }

        public Criteria andTotalsuspectNotIn(List<Integer> values) {
            addCriterion("totalSuspect not in", values, "totalsuspect");
            return (Criteria) this;
        }

        public Criteria andTotalsuspectBetween(Integer value1, Integer value2) {
            addCriterion("totalSuspect between", value1, value2, "totalsuspect");
            return (Criteria) this;
        }

        public Criteria andTotalsuspectNotBetween(Integer value1, Integer value2) {
            addCriterion("totalSuspect not between", value1, value2, "totalsuspect");
            return (Criteria) this;
        }

        public Criteria andTotaldeadIsNull() {
            addCriterion("totalDead is null");
            return (Criteria) this;
        }

        public Criteria andTotaldeadIsNotNull() {
            addCriterion("totalDead is not null");
            return (Criteria) this;
        }

        public Criteria andTotaldeadEqualTo(Integer value) {
            addCriterion("totalDead =", value, "totaldead");
            return (Criteria) this;
        }

        public Criteria andTotaldeadNotEqualTo(Integer value) {
            addCriterion("totalDead <>", value, "totaldead");
            return (Criteria) this;
        }

        public Criteria andTotaldeadGreaterThan(Integer value) {
            addCriterion("totalDead >", value, "totaldead");
            return (Criteria) this;
        }

        public Criteria andTotaldeadGreaterThanOrEqualTo(Integer value) {
            addCriterion("totalDead >=", value, "totaldead");
            return (Criteria) this;
        }

        public Criteria andTotaldeadLessThan(Integer value) {
            addCriterion("totalDead <", value, "totaldead");
            return (Criteria) this;
        }

        public Criteria andTotaldeadLessThanOrEqualTo(Integer value) {
            addCriterion("totalDead <=", value, "totaldead");
            return (Criteria) this;
        }

        public Criteria andTotaldeadIn(List<Integer> values) {
            addCriterion("totalDead in", values, "totaldead");
            return (Criteria) this;
        }

        public Criteria andTotaldeadNotIn(List<Integer> values) {
            addCriterion("totalDead not in", values, "totaldead");
            return (Criteria) this;
        }

        public Criteria andTotaldeadBetween(Integer value1, Integer value2) {
            addCriterion("totalDead between", value1, value2, "totaldead");
            return (Criteria) this;
        }

        public Criteria andTotaldeadNotBetween(Integer value1, Integer value2) {
            addCriterion("totalDead not between", value1, value2, "totaldead");
            return (Criteria) this;
        }

        public Criteria andTotalhealIsNull() {
            addCriterion("totalHeal is null");
            return (Criteria) this;
        }

        public Criteria andTotalhealIsNotNull() {
            addCriterion("totalHeal is not null");
            return (Criteria) this;
        }

        public Criteria andTotalhealEqualTo(Integer value) {
            addCriterion("totalHeal =", value, "totalheal");
            return (Criteria) this;
        }

        public Criteria andTotalhealNotEqualTo(Integer value) {
            addCriterion("totalHeal <>", value, "totalheal");
            return (Criteria) this;
        }

        public Criteria andTotalhealGreaterThan(Integer value) {
            addCriterion("totalHeal >", value, "totalheal");
            return (Criteria) this;
        }

        public Criteria andTotalhealGreaterThanOrEqualTo(Integer value) {
            addCriterion("totalHeal >=", value, "totalheal");
            return (Criteria) this;
        }

        public Criteria andTotalhealLessThan(Integer value) {
            addCriterion("totalHeal <", value, "totalheal");
            return (Criteria) this;
        }

        public Criteria andTotalhealLessThanOrEqualTo(Integer value) {
            addCriterion("totalHeal <=", value, "totalheal");
            return (Criteria) this;
        }

        public Criteria andTotalhealIn(List<Integer> values) {
            addCriterion("totalHeal in", values, "totalheal");
            return (Criteria) this;
        }

        public Criteria andTotalhealNotIn(List<Integer> values) {
            addCriterion("totalHeal not in", values, "totalheal");
            return (Criteria) this;
        }

        public Criteria andTotalhealBetween(Integer value1, Integer value2) {
            addCriterion("totalHeal between", value1, value2, "totalheal");
            return (Criteria) this;
        }

        public Criteria andTotalhealNotBetween(Integer value1, Integer value2) {
            addCriterion("totalHeal not between", value1, value2, "totalheal");
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
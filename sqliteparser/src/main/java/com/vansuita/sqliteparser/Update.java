package com.vansuita.sqliteparser;

import java.util.Date;
import java.util.LinkedList;

public class Update extends SqlParser {

    private String table;
    private LinkedList<String> setters;
    private LinkedList<String> where;

    Update(String name){
        table = name;
        setters = new LinkedList<>();
        where = new LinkedList<>();
    }

    /**
     * Override method to clear class attributes.
     *
     * @return the same instance of {@link Delete} class.
     */
    @Override
    protected SqlParser clear() {
        table = "";
        where = new LinkedList<>();

        return this;
    }

    // --- Setters --- //

    private Update add(String name, String value){
        setters.add(name + " = " + value);
        return this;
    }

    public Update set(String name, String value){
        return add(name, "'" + value + "'");
    }

    public Update set(String name, int value){
        return add(name, String.valueOf(value));
    }

    public Update set(String name, Boolean value){
        return set(name, value ? 1 : 0);
    }

    public Update set(String name, Date value){
        return add(name, String.valueOf(value.getTime()));
    }

    public Update set(String name, long value){
        return add(name, String.valueOf(value));
    }

    // --- Where --- //

    /**
     * Sets a where clause to update command
     *
     * @param column the name of the column
     * @param op     the operation of the clause
     * @param value  the value of the clause
     * @return the same instance of {@link Update} class.
     */
    private Update where(String column, String op, Object value) {
        return where(column + " " + op + " " + String.valueOf(value));
    }

    /**
     * Sets a where clause to update command
     *
     * @param value full value of the where clause
     * @return the same instance of {@link Update} class.
     */
    public Update where(String value) {
        if (!value.isEmpty()) {
            where.add(value);
        }
        return this;
    }

    /**
     * Sets an and as a divider of the where clauses
     *
     * @return the same instance of {@link Update} class.
     */
    public Update and() {
        if (!where.isEmpty()) {
            where("", SqlParser.AND, "");
        }

        return this;
    }

    /**
     * Sets an or as a divider of the where clauses
     *
     * @return the same instance of {@link Update} class.
     */
    public Update or() {
        if (!where.isEmpty()) {
            where("", SqlParser.OR, "");
        }

        return this;
    }

    /**
     * Sets a where clause to update command
     *
     * @param alias  the nick for the column
     * @param column the name of the column
     * @param op     the operation of the clause
     * @param value  the value of the clause
     * @return the same instance of {@link Update} class.
     */
    private Update where(String alias, String column, String op, String value) {
        return where(colAlias(alias, column), op, value);
    }


    /**
     * Sets a equal clause to update command
     *
     * @param cAlias  the nick for the column
     * @param column the name of the column
     * @param vAlias the nick for the value
     * @param value the value of the clause
     * @return the same instance of {@link Update} class.
     */
    public Update equal(String cAlias, String column, String vAlias, Object value) {
        return where(cAlias, column, EQUAL, colAlias(vAlias, String.valueOf(value)));
    }


    /**
     * Sets a equal clause to update command
     *
     * @param cAlias  the nick for the column
     * @param column the name of the column
     * @param value the value of the clause
     * @return the same instance of {@link Update} class.
     */
    public Update equal(String cAlias, String column, Object value) {
        return where(cAlias, column, "=", String.valueOf(value));
    }

    /**
     * Sets a equal clause to update command
     *
     * @param column the name of the column
     * @param value the int value of the clause
     * @return the same instance of {@link Update} class.
     */
    public Update equal(String column, int value) {
        return where(column, EQUAL, value);
    }

    /**
     * Sets a equal clause to update command
     *
     * @param column the name of the column
     * @param value the int value of the clause
     * @return the same instance of {@link Update} class.
     */
    public Update equal(String column, String value) {
        return where(column, EQUAL, "'" + value + "'");
    }

    public Update greaterEqual(String column, Object value) {
        return where(column, GREATER_EQUAL, value);
    }

    public Update smallerEqual(String column, Object value) {
        return where(column, SMALLER_EQUAL, value);
    }

    public Update in(String column, String subSelect) {
        return where(column + IN + " (" + subSelect + ")");
    }

    public Update dif(String column, Object value) {
        return where(column, DIFFERENT, String.valueOf(value));
    }

    // --- Utilities --- //

    private String colAlias(String alias, String column) {
        return alias.isEmpty() ? column : alias + "." + column;
    }

    @Override
    protected String getSql() {
        String sql = UPDATE + " " + table;

        if (!setters.isEmpty()){
            sql += " " + Utils.breakList(",", setters);
        }

        if (!where.isEmpty())
            sql += " " + WHERE + " " + Utils.breakList("", where);

        return sql;
    }
}

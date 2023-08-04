package com.bcol.vtd.api.enviocorreo.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InyeccionSQLValidador implements ConstraintValidator<InyeccionSQL, String> {
    private static final String[] sqlRegexps = new String[]{"(?i)(.*)(\\b)+SELECT(\\b)+\\s.*(\\b)+FROM(\\b)+\\s.*(.*)", "(?i)(.*)(\\b)+INSERT(\\b)+\\s.*(\\b)+INTO(\\b)+\\s.*(.*)", "(?i)(.*)(\\b)+UPDATE(\\b)+\\s.*(.*)", "(?i)(.*)(\\b)+DELETE(\\b)+\\s.*(\\b)+FROM(\\b)+\\s.*(.*)", "(?i)(.*)(\\b)+UPSERT(\\b)+\\s.*(.*)", "(?i)(.*)(\\b)+SAVEPOINT(\\b)+\\s.*(.*)", "(?i)(.*)(\\b)+CALL(\\b)+\\s.*(.*)", "(?i)(.*)(\\b)+ROLLBACK(\\b)+\\s.*(.*)", "(?i)(.*)(\\b)+KILL(\\b)+\\s.*(.*)", "(?i)(.*)(\\b)+DROP(\\b)+\\s.*(.*)", "(?i)(.*)(\\b)+CREATE(\\b)+(\\s)*(" + "TABLE, TABLESPACE, PROCEDURE, FUNCTION, TRIGGER, KEY, VIEW, MATERIALIZED VIEW, LIBRARYDATABASE LINK, DBLINK, INDEX, CONSTRAINT, TRIGGER, USER, SCHEMA, DATABASE, PLUGGABLE DATABASE, BUCKET, CLUSTER, COMMENT, SYNONYM, TYPE, JAVA, SESSION, ROLE, PACKAGE, PACKAGE BODY, OPERATORSEQUENCE, RESTORE POINT, PFILE, CLASS, CURSOR, OBJECT, RULE, USER, DATASET, DATASTORE, COLUMN, FIELD, OPERATOR".replaceAll(",", "|") + ")(\\b)+\\s.*(.*)", "(?i)(.*)(\\b)+ALTER(\\b)+(\\s)*(" + "TABLE, TABLESPACE, PROCEDURE, FUNCTION, TRIGGER, KEY, VIEW, MATERIALIZED VIEW, LIBRARYDATABASE LINK, DBLINK, INDEX, CONSTRAINT, TRIGGER, USER, SCHEMA, DATABASE, PLUGGABLE DATABASE, BUCKET, CLUSTER, COMMENT, SYNONYM, TYPE, JAVA, SESSION, ROLE, PACKAGE, PACKAGE BODY, OPERATORSEQUENCE, RESTORE POINT, PFILE, CLASS, CURSOR, OBJECT, RULE, USER, DATASET, DATASTORE, COLUMN, FIELD, OPERATOR".replaceAll(",", "|") + ")(\\b)+\\s.*(.*)", "(?i)(.*)(\\b)+TRUNCATE(\\b)+(\\s)*(" + "TABLE, TABLESPACE, PROCEDURE, FUNCTION, TRIGGER, KEY, VIEW, MATERIALIZED VIEW, LIBRARYDATABASE LINK, DBLINK, INDEX, CONSTRAINT, TRIGGER, USER, SCHEMA, DATABASE, PLUGGABLE DATABASE, BUCKET, CLUSTER, COMMENT, SYNONYM, TYPE, JAVA, SESSION, ROLE, PACKAGE, PACKAGE BODY, OPERATORSEQUENCE, RESTORE POINT, PFILE, CLASS, CURSOR, OBJECT, RULE, USER, DATASET, DATASTORE, COLUMN, FIELD, OPERATOR".replaceAll(",", "|") + ")(\\b)+\\s.*(.*)", "(?i)(.*)(\\b)+LOCK(\\b)+(\\s)*(" + "TABLE, TABLESPACE, PROCEDURE, FUNCTION, TRIGGER, KEY, VIEW, MATERIALIZED VIEW, LIBRARYDATABASE LINK, DBLINK, INDEX, CONSTRAINT, TRIGGER, USER, SCHEMA, DATABASE, PLUGGABLE DATABASE, BUCKET, CLUSTER, COMMENT, SYNONYM, TYPE, JAVA, SESSION, ROLE, PACKAGE, PACKAGE BODY, OPERATORSEQUENCE, RESTORE POINT, PFILE, CLASS, CURSOR, OBJECT, RULE, USER, DATASET, DATASTORE, COLUMN, FIELD, OPERATOR".replaceAll(",", "|") + ")(\\b)+\\s.*(.*)", "(?i)(.*)(\\b)+UNLOCK(\\b)+(\\s)*(" + "TABLE, TABLESPACE, PROCEDURE, FUNCTION, TRIGGER, KEY, VIEW, MATERIALIZED VIEW, LIBRARYDATABASE LINK, DBLINK, INDEX, CONSTRAINT, TRIGGER, USER, SCHEMA, DATABASE, PLUGGABLE DATABASE, BUCKET, CLUSTER, COMMENT, SYNONYM, TYPE, JAVA, SESSION, ROLE, PACKAGE, PACKAGE BODY, OPERATORSEQUENCE, RESTORE POINT, PFILE, CLASS, CURSOR, OBJECT, RULE, USER, DATASET, DATASTORE, COLUMN, FIELD, OPERATOR".replaceAll(",", "|") + ")(\\b)+\\s.*(.*)", "(?i)(.*)(\\b)+RELEASE(\\b)+(\\s)*(" + "TABLE, TABLESPACE, PROCEDURE, FUNCTION, TRIGGER, KEY, VIEW, MATERIALIZED VIEW, LIBRARYDATABASE LINK, DBLINK, INDEX, CONSTRAINT, TRIGGER, USER, SCHEMA, DATABASE, PLUGGABLE DATABASE, BUCKET, CLUSTER, COMMENT, SYNONYM, TYPE, JAVA, SESSION, ROLE, PACKAGE, PACKAGE BODY, OPERATORSEQUENCE, RESTORE POINT, PFILE, CLASS, CURSOR, OBJECT, RULE, USER, DATASET, DATASTORE, COLUMN, FIELD, OPERATOR".replaceAll(",", "|") + ")(\\b)+\\s.*(.*)", "(?i)(.*)(\\b)+DESC(\\b)+(\\w)*\\s.*(.*)", "(?i)(.*)(\\b)+DESCRIBE(\\b)+(\\w)*\\s.*(.*)", "(.*)(/\\*|\\*/|;){1,}(.*)", "(.*)(-){2,}(.*)"};
    private List<Pattern> validationPatterns = getValidationPatterns();

    public InyeccionSQLValidador() {
    }

    private static List<Pattern> getValidationPatterns() {
        List<Pattern> patterns = new ArrayList();
        String[] var1 = sqlRegexps;
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            String sqlExpression = var1[var3];
            patterns.add(getPattern(sqlExpression));
        }

        return patterns;
    }

    private static Pattern getPattern(String regEx) {
        return Pattern.compile(regEx, 66);
    }

    public void initialize(InyeccionSQL sqlInjectionSafe) {
    }

    public boolean isValid(String dataString, ConstraintValidatorContext cxt) {
        return this.isSqlInjectionSafe(dataString);
    }

    private boolean isSqlInjectionSafe(String dataString) {
        if (this.isEmpty(dataString)) {
            return true;
        } else {
            Iterator var2 = this.validationPatterns.iterator();

            Pattern pattern;
            do {
                if (!var2.hasNext()) {
                    return true;
                }

                pattern = (Pattern) var2.next();
            } while (!this.matches(pattern, dataString));

            return false;
        }
    }

    private boolean matches(Pattern pattern, String dataString) {
        Matcher matcher = pattern.matcher(dataString);
        return matcher.matches();
    }

    private boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }
}

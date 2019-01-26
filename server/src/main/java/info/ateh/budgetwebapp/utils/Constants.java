package info.ateh.budgetwebapp.utils;

public final class Constants {

    public static final String NAME_REGEXP = "([А-ЯЁа-яёA-Za-z]+(([-])|(\\.)|(\\s)|(\\.\\s))?)+";
    public static final int MIN_NAME_LEN = 2;
    public static final int MAX_NAME_LEN = 30;

    public static final String COMMENT_PATTERN = ".+";
    public static final int MIN_COMMENT_LEN = 3;
    public static final int MAX_COMMENT_LEN = 150;

    public static final String DATE_PATTERN = "yyyy-MM-dd";

}
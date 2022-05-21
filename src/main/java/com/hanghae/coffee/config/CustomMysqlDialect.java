package com.hanghae.coffee.config;

import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class CustomMysqlDialect extends MySQL5Dialect {
	public CustomMysqlDialect() {
		super();
		registerFunction("group_concat",
				new StandardSQLFunction("group_concat", StandardBasicTypes.STRING));
	}
}
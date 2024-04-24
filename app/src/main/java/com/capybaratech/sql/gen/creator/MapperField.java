package com.capybaratech.sql.gen.creator;


public record MapperField(String tableField, String classField, boolean isInsertable, boolean isUpdatable, boolean isPrimaryKey){}

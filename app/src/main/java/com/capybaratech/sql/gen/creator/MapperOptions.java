/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package com.capybaratech.sql.gen.creator;

import java.io.File;

/**
 *
 * @author etelhado
 */
public class MapperOptions {
    
    private boolean genInsert; 
    private boolean genUpdate; 
    private boolean genSelectAll; 
    private boolean genSelectByPk;
    private boolean genDelete;
    private String resultClassFqn;
    private String packageFqn;
    private String tableName;
    private File outputDirectory;
    private String paramName;

    public boolean isGenInsert() {
        return genInsert;
    }

    public void setGenInsert(boolean genInsert) {
        this.genInsert = genInsert;
    }

    public boolean isGenUpdate() {
        return genUpdate;
    }

    public void setGenUpdate(boolean genUpdate) {
        this.genUpdate = genUpdate;
    }

    public boolean isGenSelectAll() {
        return genSelectAll;
    }

    public void setGenSelectAll(boolean genSelectAll) {
        this.genSelectAll = genSelectAll;
    }

    public boolean isGenSelectByPk() {
        return genSelectByPk;
    }

    public void setGenSelectByPk(boolean genSelectByPk) {
        this.genSelectByPk = genSelectByPk;
    }

    public boolean isGenDelete() {
        return genDelete;
    }

    public void setGenDelete(boolean genDelete) {
        this.genDelete = genDelete;
    }

    public String getResultClassFqn() {
        return resultClassFqn;
    }

    public void setResultClassFqn(String resultClassFqn) {
        this.resultClassFqn = resultClassFqn;
    }

    public String getPackageFqn() {
        return packageFqn;
    }

    public void setPackageFqn(String packageFqn) {
        this.packageFqn = packageFqn;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }
    
    

}

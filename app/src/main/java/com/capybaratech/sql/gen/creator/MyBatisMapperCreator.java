/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.capybaratech.sql.gen.creator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ProcessingInstruction;

/**
 *
 * @author etelhado
 */
public class MyBatisMapperCreator {

    private DocumentBuilderFactory docFactory = null;
    private DocumentBuilder docBuilder = null;
    private Document doc = null;
    private List<MapperField> fields;
    private MapperOptions options;

    public MyBatisMapperCreator() throws ParserConfigurationException {
        docFactory = DocumentBuilderFactory.newInstance();
        docBuilder = docFactory.newDocumentBuilder();
        doc = docBuilder.newDocument();
        String doctypeDeclaration = "-//mybatis.org//DTD Mapper 3.0//EN";
        String doctypeLocation = "http://mybatis.org/dtd/mybatis-3-mapper.dtd";
        doc.appendChild(doc.createComment(" Generated using MyBatis Generator "));
        ProcessingInstruction doctype = doc.createProcessingInstruction("DOCTYPE", "mapper PUBLIC \"" + doctypeDeclaration + "\" \"" + doctypeLocation + "\"");
        doc.appendChild(doctype);
    }

    public void create(List<MapperField> fieldList, MapperOptions optionsMap) {
        try {
            this.fields = fieldList;
            this.options = optionsMap;

            Element mapperElement = doc.createElement("mapper");
            mapperElement.setAttribute("namespace", options.getPackageFqn());

            var primaryKey = fields.stream().filter((f) -> f.isPrimaryKey()).findFirst().orElse(null);
            doc.appendChild(mapperElement);
            if (optionsMap.isGenInsert()) {
                String stm = genInsertStm();
                mapperElement.appendChild(buildInsert(stm, primaryKey));
            }

            if (optionsMap.isGenUpdate() && primaryKey != null) {
                String stm = genUpdateStm();
                mapperElement.appendChild(buildUpdate(stm));
            }
            if (optionsMap.isGenDelete() && primaryKey != null) {
                String stm = genDeletePkStm();
                mapperElement.appendChild(buildDelete(stm));
            }
            if (optionsMap.isGenSelectAll()) {
                String stm = genSelectStm();
                mapperElement.appendChild(buildFindAll(stm, optionsMap.getResultClassFqn()));
            }
            if (optionsMap.isGenSelectByPk() && primaryKey != null) {
                String stm = genSelectPkStm();
                mapperElement.appendChild(buildFindByPk(stm, optionsMap.getResultClassFqn()));
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            String fqn = optionsMap.getPackageFqn();
            String[] packageDeclaration = fqn.split("\\.");
            String fileName = packageDeclaration[packageDeclaration.length - 1];
            StreamResult result = new StreamResult(new File(optionsMap.getOutputDirectory(), fileName + ".xml"));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(source, result);

            System.out.println("Arquivo XML criado com sucesso!");

        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }

    private Element buildInsert(String stm, MapperField primaryKey) {
        Element insertElement = doc.createElement("insert");
        insertElement.setAttribute("id", "insere");
        if (primaryKey != null) {
            insertElement.setAttribute("useGeneratedKeys", "true");
            insertElement.setAttribute("keyProperty", primaryKey.tableField());
        }

        insertElement.setTextContent(stm);
        return insertElement;
    }

    private Element buildUpdate(String stm) {
        Element updateElement = doc.createElement("update");
        updateElement.setAttribute("id", "atualiza");
        updateElement.setTextContent(stm);
        return updateElement;
    }

    private Element buildFindAll(String stm, String resultClassFqn) {
        Element selectElement = doc.createElement("select");
        selectElement.setAttribute("id", "lista");
        selectElement.setAttribute("resultType", resultClassFqn);
        selectElement.setTextContent(stm);
        return selectElement;
    }

    private Element buildFindByPk(String stm, String resultClassFqn) {
        Element selectElement = doc.createElement("select");
        selectElement.setAttribute("id", "obter");
        selectElement.setAttribute("resultType", resultClassFqn);
        selectElement.setTextContent(stm);
        return selectElement;
    }

    private Element buildDelete(String stm) {
        Element deleteElement = doc.createElement("delete");
        deleteElement.setAttribute("id", "deleta");
        deleteElement.setTextContent(stm);
        return deleteElement;
    }

    private String genUpdateStm() {
        var primaryKey = fields.stream().filter((f) -> f.isPrimaryKey()).findFirst();
        String tableName = (String) options.getTableName();
        StringBuilder bu = new StringBuilder();
        bu.append(String.format("UPDATE %s SET", tableName));
        List<String> sentenceList = new ArrayList<>();
        for (MapperField field : fields) {
            if (field.isUpdatable() && !field.isPrimaryKey()) {
                String sentence = String.format(" %s = #{%s.%s}", field.tableField(), options.getParamName(), field.classField());
                sentenceList.add(sentence);
            }
        }
        bu.append(String.join(",", sentenceList));
        bu.append(String.format(" WHERE %s = #{%s}", primaryKey.get().tableField(), primaryKey.get().classField()));
        return bu.toString();
    }

    private String genInsertStm() {
        String tableName = (String) options.getTableName();
        StringBuilder bu = new StringBuilder();
        List<String> tableFields = new ArrayList<>();
        List<String> classFields = new ArrayList<>();
        for (MapperField field : fields) {
            if (field.isInsertable() && !field.isPrimaryKey()) {
                classFields.add(String.format(" #{%s.%s}", options.getParamName(), field.classField()));
                tableFields.add(field.tableField());
            }
        }
        String fieldsInsert = String.join(",", tableFields);
        String classInsert = String.join(",", classFields);
        bu.append(String.format("INSERT INTO %s(%s) VALUES (%s) ", tableName, fieldsInsert, classInsert));
        var primaryKey = fields.stream().filter((f) -> f.isPrimaryKey()).findFirst();
        if (primaryKey.isPresent()) {
            String returning = String.format(" RETURNING %s", primaryKey.get().tableField());
            bu.append(returning);

        }
        return bu.toString();

    }

    private String genSelectStm() {
        String tableName = (String) options.getTableName();
        return String.format("SELECT * FROM %s", tableName);
    }

    private String genSelectPkStm() {
        var primaryKey = fields.stream().filter((f) -> f.isPrimaryKey()).findFirst();
        String tableName = (String) options.getTableName();
        return String.format("SELECT * FROM %s WHERE %s = #{%s}", tableName, primaryKey.get().tableField(), primaryKey.get().classField());
    }

    private String genDeletePkStm() {
        var primaryKey = fields.stream().filter((f) -> f.isPrimaryKey()).findFirst();
        String tableName = (String) options.getTableName();
        return String.format("DELETE FROM %s WHERE %s = #{%s}", tableName, primaryKey.get().tableField(), primaryKey.get().classField());
    }
}

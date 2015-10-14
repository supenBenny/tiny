package ${basePackage};


import lombok.Data;

${importClasses}

@Data
public class ${table.className} implements java.io.Serializable {
	
	private static final long serialVersionUID = ${serialVersionUID}L;
	
	<#list table.columns>
	<#items as column>
	/**
	 * <#if column.comment??>${column.comment}</#if>
	 */
	private ${column.fieldTypeName} ${column.fieldName};
		
	</#items>
</#list>
}


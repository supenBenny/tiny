package ${package};

import me.fb.tiny.dal.repository.mybatis.MybatisRepository;
import ${domainClass.fullName};

/**
 * 
 *@author ${author}
 *@version 
 *@since ${date}
 */
public interface ${domainClass.simpleName}Repository extends MybatisRepository<${domainClass.simpleName}, ${domainClass.identifier.type.simpleName}>{
	

}

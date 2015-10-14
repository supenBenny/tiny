
package ${package};


import org.springframework.stereotype.Repository;

import me.fb.tiny.dal.repository.mybatis.MybatisRepositoryImpl;
import ${domainClass.fullName};
import ${basepackage}.repository.${domainClass.simpleName}Repository;

/**
 * 
 * @author ${author}
 * @version 
 * @since ${date}
 */
@Repository
public class ${domainClass.simpleName}RepositoryImpl extends MybatisRepositoryImpl<${domainClass.simpleName}, ${domainClass.identifier.type.simpleName}>implements ${domainClass.simpleName}Repository {



}

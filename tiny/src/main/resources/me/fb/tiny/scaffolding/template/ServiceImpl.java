
package ${package};


import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.fb.tiny.dal.repository.GenericRepository;
import me.fb.tiny.dal.service.AbstractService;
import ${basepackage}.domain.${domainClass.simpleName};
import ${basepackage}.repository.${domainClass.simpleName}Repository;
import ${basepackage}.service.${domainClass.simpleName}Service;

/**
 * 
 *@author ${author}
 *@version 
 *@since ${date}
 */
@Service
@Transactional(readOnly=true)
public class ${domainClass.simpleName}ServiceImpl extends AbstractService<${domainClass.simpleName}, ${domainClass.identifier.type.simpleName}> implements ${domainClass.simpleName}Service {

	@Resource
	private ${domainClass.simpleName}Repository ${domainClass.propertyName}Repository;
	

	@Override
	protected GenericRepository<${domainClass.simpleName}, ${domainClass.identifier.type.simpleName}> getRepository() {
		return this.${domainClass.propertyName}Repository;
	}

}

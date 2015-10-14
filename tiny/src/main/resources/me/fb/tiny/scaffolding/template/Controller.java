package ${package};

import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;


import me.fb.tiny.dal.search.Pageable;
import me.fb.tiny.dal.search.Search;
import me.fb.tiny.dal.utils.SearchUtils;
import me.fb.tiny.utils.Result;
import ${basepackage}.service.${domainClass.simpleName}Service;
import ${basepackage}.domain.${domainClass.simpleName};
 
/**
 * 
 *@author ${author}
 *@version 
 *@since ${date}
 */
@RestController
@RequestMapping("/${domainClass.propertyName}")
public class ${domainClass.simpleName}Controller {
	
	@Resource
	private ${domainClass.simpleName}Service ${domainClass.propertyName}Service;
	
	@RequestMapping(method = RequestMethod.POST)
	public Result create(@RequestBody @Valid ${domainClass.simpleName} ${domainClass.propertyName}){
		
		${domainClass.propertyName}Service.create(${domainClass.propertyName});

		return Result.success(${domainClass.propertyName});
	} 
	
	@RequestMapping(method = RequestMethod.PUT)
	public Result update(@RequestBody @Valid ${domainClass.simpleName} ${domainClass.propertyName}){
		${domainClass.propertyName}Service.update(${domainClass.propertyName});
		return Result.success(${domainClass.propertyName});
	}

	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public Result delete(@PathVariable Integer id){
		${domainClass.propertyName}Service.deleteById(id);
		return Result.success(null);
	}
	
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public Result get(@PathVariable Integer id){
		${domainClass.simpleName} ${domainClass.propertyName} = ${domainClass.propertyName}Service.get(id);
		if(${domainClass.propertyName} != null){
			return Result.success(${domainClass.propertyName});
		}
		return Result.failure("");
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public Result list(@RequestParam Map<String,Object> args){
		Search<${domainClass.simpleName}> search = SearchUtils.build(${domainClass.simpleName}.class, args);
	    Pageable<${domainClass.simpleName}> pageable = ${domainClass.propertyName}Service.search(search);
	    
	    Result result = Result.success(pageable.getContent());
	    result.setPagination(pageable.getCurrentPage(), pageable.getPageSize(),
	    		pageable.getTotalPages(),pageable.getTotalResults());
	    
	    return result;
	}
	
	@RequestMapping("/view")
	public ModelAndView view(){
		return new ModelAndView("${domainClass.propertyName}/index");
	}
}

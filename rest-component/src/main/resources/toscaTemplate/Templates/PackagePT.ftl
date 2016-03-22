package{<#if package_name?has_content>'${package_name}',</#if>:
<#if require?has_content>require => ${require},</#if> 
<#if before?has_content>before => ${before},</#if>
}
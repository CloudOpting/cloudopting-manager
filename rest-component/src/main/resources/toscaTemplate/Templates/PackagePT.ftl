package{<#if package_name?has_content>'${package_name}'</#if>:
<#if provider?has_content>provider => '${provider}',</#if>
<#if source?has_content>source => "${source}",</#if>
<#if install_options?has_content>install_options => ${install_options},</#if>
<#if require?has_content>require => ${require},</#if> 
<#if before?has_content>before => ${before},</#if>
}
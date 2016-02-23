class{'liferay':
<#if catalina_base?has_content>catalina_base => '${catalina_base}',</#if>
<#if catalina_home?has_content>catalina_home => '${catalina_home}',</#if>
<#if dbhost?has_content>dbhost => '${dbhost}',</#if>
<#if dbname?has_content>dbname => '${dbname}',</#if>
<#if dbuser?has_content>dbuser => '${dbuser}',</#if>
<#if dbpass?has_content>dbpass => '${dbpass}',</#if>
<#if wizard?has_content>wizard => ${wizard},</#if>
<#if version?has_content>version => ${version},</#if>
require => Tomcat::Service['default'],
}


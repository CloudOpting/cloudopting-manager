class { 'tomcat': 
<#if catalina_home?has_content>catalina_home => '${catalina_home}',</#if>}
class { 'java': 
distribution => 'jdk',}

tomcat::instance { '<#if tomcat?has_content>${tomcat}</#if>':
<#if catalina_base?has_content>catalina_base => '${catalina_base}',</#if>
<#if catalina_home?has_content>catalina_home => '${catalina_home}',</#if>
<#if source_url?has_content>source_url => '${source_url}',</#if>
<#if install_from_source?has_content>install_from_source => ${install_from_source},</#if>
}->
tomcat::config::server { '<#if tomcat?has_content>${tomcat}</#if>':
  <#if catalina_base?has_content>catalina_base => '${catalina_base}',</#if>
  <#if server_port?has_content>port => ${server_port},</#if>
}<#if http_port?has_content>->
tomcat::config::server::connector { '<#if tomcat?has_content>${tomcat}</#if>-http':
  <#if catalina_base?has_content>catalina_base => '${catalina_base}',</#if>
  port                  => '<#if http_port?has_content>${http_port}</#if>',
  protocol              => 'HTTP/1.1',
  <#if additional_attributes_http_conn?has_content>additional_attributes => {${additional_attributes_http_conn} },</#if>
}</#if><#if ajp_port?has_content>->
tomcat::config::server::connector { '<#if tomcat?has_content>${tomcat}</#if>-ajp':
  <#if catalina_base?has_content>catalina_base => '${catalina_base}',</#if>
  port                  => '<#if ajp_port?has_content>${ajp_port}</#if>',
  protocol              => 'AJP/1.3',
  <#if additional_attributes_ajp_conn?has_content>additional_attributes => {${additional_attributes_ajp_conn} },</#if>
}</#if>->
tomcat::service { 'default':
<#if catalina_base?has_content>catalina_base => '${catalina_base}',</#if>
service_ensure => 'stopped',
}

<#foreach childTemplate in childtemplates>
${childTemplate}
</#foreach>


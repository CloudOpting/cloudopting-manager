<#if version?has_content>class { '::mongodb::globals':
<#if version?has_content>version => '${version}',</#if>
}-></#if>
class { '::mongodb::server':
      <#if bind_ip?has_content>bind_ip => '${bind_ip}',</#if>
      <#if admin_password?has_content>admin_password => '${admin_password}',</#if>
      <#if port?has_content>port => ${port},</#if>
      <#if admin_username?has_content>admin_username => '${admin_username}',</#if>
    }

<#foreach childTemplate in childtemplates>
${childTemplate}
</#foreach>

class { 'mysql::server':
  <#if root_password?has_content>root_password => '${root_password}',</#if>
  <#if remove_defaul_account?has_content>remove_defaul_account => '${remove_defaul_account}',</#if>
  <#if package_ensure?has_content>package_ensure => '${package_ensure}',</#if>
}

<#foreach childTemplate in childtemplates>
${childTemplate}
</#foreach>

<#if version?has_content>class { 'postgresql::globals':
manage_package_repo => true,
<#if version?has_content>version => '${version}',</#if>
<#if postgis_version?has_content>postgis_version => '${postgis_version}',</#if>
}-></#if>
class { 'postgresql::server':
      <#if listen_addresses?has_content>listen_addresses => '${listen_addresses}',</#if>
      <#if postgres_password?has_content>postgres_password => '${postgres_password}',</#if>
      <#if port?has_content>port => ${port},</#if>
      <#if ip_mask_allow_all_users?has_content>ip_mask_allow_all_users => '${ip_mask_allow_all_users}',</#if>
    }
<#if log_filename?has_content>
postgresql::server::config_entry { 'log_filename':
  value => '${log_filename}',
}
</#if>

<#foreach childTemplate in childtemplates>
${childTemplate}
</#foreach>

class { 'wordpress':
<#if db_user?has_content>db_user => ${db_user},</#if>
<#if db_password?has_content>db_password => ${db_password},</#if>
<#if db_host?has_content>db_host => ${db_host},</#if>
<#if db_name?has_content>db_name => ${db_name},</#if>
<#if install_dir?has_content>install_dir => ${install_dir},</#if>


create_db      => false,
create_db_user => false,
}
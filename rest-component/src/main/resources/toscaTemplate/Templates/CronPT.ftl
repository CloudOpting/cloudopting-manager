class{'cron':
<#if installed?has_content>package_ensure => 'installed',</#if>
 
}
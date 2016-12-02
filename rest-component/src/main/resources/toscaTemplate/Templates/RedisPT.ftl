class { 'redis':
<#if bind?has_content>bind => '${bind}',</#if>
<#if requirepass?has_content>requirepass => '${requirepass}',</#if>
service_ensure => 'stopped',
}


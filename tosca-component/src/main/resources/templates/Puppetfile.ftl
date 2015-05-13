<#list modData as module>
mod '<#if module['module']?has_content>${module['module']}</#if>',
  <#if module['git']?has_content>:git => '${module['git']}'</#if>

</#list>

mod 'concat',
  :git => 'https://github.com/puppetlabs/puppetlabs-concat'

mod 'stdlib',
  :git => 'https://github.com/puppetlabs/puppetlabs-stdlib'
  
mod 'java',
  :git => 'https://github.com/puppetlabs/puppetlabs-java'
  
mod 'staging',
  :git => 'https://github.com/nanliu/puppet-staging.git'
  
mod 'liferay',
  :git => 'https://github.com/gioppoluca/puppet-liferay.git'  
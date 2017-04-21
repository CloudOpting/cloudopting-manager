<#list modData as module>
mod '<#if module['module']?has_content>${module['module']}</#if>',
  <#if module['git']?has_content>:git => '${module['git']}'</#if>

</#list>

mod 'concat',
  :git => 'https://github.com/puppetlabs/puppetlabs-concat',
  :tag => '2.2.0'

mod 'stdlib',
  :git => 'https://github.com/puppetlabs/puppetlabs-stdlib',
  :tag => '4.15.0'
  
mod 'java',
  :git => 'https://github.com/puppetlabs/puppetlabs-java'
  
mod 'staging',
  :git => 'https://github.com/nanliu/puppet-staging.git'
  

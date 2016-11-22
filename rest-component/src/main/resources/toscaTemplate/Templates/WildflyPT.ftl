java::oracle { 'jdk8' :
  ensure  => 'present',
  version => '8',
  java_se => 'jdk',
}

class { 'wildfly':
<#if version?has_content>version => '${version}',</#if>
<#if install_source?has_content>install_source => '${install_source}',</#if>
service_enable => false,
service_ensure => false,
<#if public_bind?has_content>public_bind => '${public_bind}',</#if>
<#if public_http_port?has_content>public_http_port => ${public_http_port},</#if>
require => Java::Oracle['jdk8'],
}

$str = "<#if public_bind?has_content>jboss.bind.address=${public_bind}</#if>
<#if public_http_port?has_content>jboss.http.port=${public_http_port}</#if>
        "

file { '/opt/wildfly/jboss.properties':
      content => $str,
require => Class['wildfly'],
}

<#if childtemplates?has_content>
exec { 'start_wildfly':
require => File['/opt/wildfly/jboss.properties'], 
command => '/opt/wildfly/bin/standalone.sh -P=/opt/wildfly/jboss.properties &',
logoutput => true,
}

exec { 'wait' :
  require => Exec["start_wildfly"],
  command => "sleep 10 && wget http://localhost:<#if public_http_port?has_content>${public_http_port}<#else>8080</#if>",
  path => "/usr/bin:/bin",
}
</#if>

<#foreach childTemplate in childtemplates>
${childTemplate}
</#foreach>


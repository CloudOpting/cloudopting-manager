#### Install packages requirements
$packages = [ 'python-pip', 'unzip', 'wget', 'python-virtualenv', 'python-devel', 'libxslt', 'libxslt-devel', 'libxml2', 'libxml2-devel', 'gcc', 'gcc-c++', 'make', 'postgresql-devel', 'augeas' ]

$ckan_ver = "<#if ckan_version?has_content>${ckan_version}</#if>"
$ckan_path = "/usr/lib/ckan/ckan-ckan-<#if ckan_version?has_content>${ckan_version}</#if>"

package { $packages: 
	before => Exec['pip requirements'],
}

class { 'apache':
      default_mods => true,
      
      servername => fqdn,
      default_vhost => false,
      mpm_module => prefork,
      service_ensure => false,
}->
apache::vhost { '<#if servername?has_content>${servername}</#if>':
      port    => 8080,
      vhost_name    => '*',
      <#if servername?has_content>servername    => '${servername}',</#if>
      docroot => '/var/www/wcic',
      access_log_file => "/dev/stdout",
#      error_log_pipe => "/dev/stderr",
      
      wsgi_pass_authorization => "On",
      wsgi_script_aliases         => { '/' => "$ckan_path/apache.wsgi" },
      
      proxy_preserve_host => false,

}->
class { 'apache::mod::wsgi':
 
}

exec {"https://github.com/ckan/ckan/archive/ckan-$ckan_ver.zip":
command => "wget --verbose --output-document=\"/tmp/ckan-$ckan_ver.zip\" \"https://github.com/ckan/ckan/archive/ckan-$ckan_ver.zip\"",
path => '/bin:/usr/bin:/usr/sbin',
before => Class['apache'],
require => Package['wget'],
}->
archive::extract{"ckan-$ckan_ver":
target => '/usr/lib/ckan',
src_target => '/tmp',
extension => 'zip',
user => 'root',
root_dir => '',
require => Package['unzip'],
}
->
file{'/usr/lib/ckan':
ensure => directory,
recurse => true,
owner => 'apache',
}
->
exec {'virtualenv1':
command => "virtualenv --no-site-packages $ckan_path",
path => '/bin:/usr/bin:/usr/sbin',
}
->
exec {'pip requirements':
command => ". $ckan_path/bin/activate && pip install -r $ckan_path/requirements.txt",
path => '/bin:/usr/bin:/usr/sbin',
provider => 'shell',
}
->
exec {'pip ckan':
command => ". $ckan_path/bin/activate && pip install -e $ckan_path/",
path => '/bin:/usr/bin:/usr/sbin',
provider => 'shell',
}
->
file{"$ckan_path/apache.wsgi":
content => "import os
ckan_home = os.environ.get('CKAN_HOME', '$ckan_path')
activate_this = os.path.join(ckan_home, 'bin/activate_this.py')
execfile(activate_this, dict(__file__=activate_this))

from paste.deploy import loadapp
config_filepath = os.path.join(os.path.dirname(os.path.abspath(__file__)), 'ckan.ini')
from paste.script.util.logging_config import fileConfig
fileConfig(config_filepath)
application = loadapp('config:%s' % config_filepath)",
mode => 700,
owner => 'apache',
group => 'apache',
}
->
exec {'paster':
command => ". $ckan_path/bin/activate && paster make-config ckan $ckan_path/ckan.ini",
path => '/bin:/usr/bin:/usr/sbin',
provider => 'shell',
}
->
augeas{"$ckan_path/ckan.ini":
incl => "$ckan_path/ckan.ini",
lens => "Puppet.lns",
context => "/files/$ckan_path/ckan.ini",
changes => ["set app:main/sqlalchemy.url 'postgresql://ckanadmin:pgckanpwd@ckandbdk/ckandb'",
"set app:main/ckan.site_url 'http://www.testcar.se'",
"set app:main/ckan.site_id 'www.testcar.se'",
"set app:main/host 'http://www.testcar.se'",
#"ins app:main/solr_url before loggers",
"set app:main/solr_url 'http://ckansolrdk:8983/solr'",
"set app:main/ckan.storage_path '/usr/lib/ckan'"
],
}


file{'/root/start.sh':
content => "#! /usr/bin/env bash
#set -eu
#( umask 0 && truncate -s0 /var/log/httpd/www.testcar.se_access.log )
#tail --pid $$ -n0 -F /var/log/httpd/*_access.log &
cd $ckan_path
. $ckan_path/bin/activate && 
paster --plugin=ckan db init --config=ckan.ini &&
paster --plugin=ckan user add admin password=test email=gioppo@csi.it --config=ckan.ini &&
paster --plugin=ckan sysadmin add admin --config=ckan.ini &&
set -eu
exec /usr/sbin/httpd -DFOREGROUND",
mode => 700,
}



##########################à


#class{'solr':
#<#if url?has_content>url => '${url}',</#if>
#<#if version?has_content>version => '${version}',</#if>
#<#if solr_host?has_content>dbhost => '${solr_host}',</#if>
#<#if solr_port?has_content>dbname => ${solr_port},</#if>
#}


##### in the file we need to read the file from GIT
#file {'/tmp/schema.xml':
#  ensure => file,
#  content => inline_template('....'),
#}

#solr::core{'ckan':
#  schema_src_file => '/tmp/schema.xml',
#  require         => File ['/tmp/schema.xml'],
#}
